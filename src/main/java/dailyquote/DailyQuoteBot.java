package dailyquote;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.mail.*;
import javax.mail.internet.*;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DailyQuoteBot {

	public static void main(String[] args) throws InterruptedException, IOException {
		// Scrape the quote
		String quote = getRandomQuote();

		// Send the quote via email
		sendEmail(quote);
	}

	public static String getRandomQuote() throws InterruptedException, IOException {
		WebDriver driver = null;
		Path userDataDir = null;
		String selectedQuote = "Keep going, you're doing great!"; // Default quote;

		try {
			System.out.println("Launching Chrome");

			ChromeOptions options = new ChromeOptions();
		    options.addArguments("--headless=new");
			options.addArguments("--disable-gpu"); // Disable GPU acceleration
			options.addArguments("--no-sandbox"); // Bypass OS security model
			options.addArguments("--disable-dev-shm-usage"); // Overcome limited resource problems

			userDataDir = Files.createTempDirectory("chrome-user-data");
			options.addArguments("--user-data-dir=" + userDataDir.toAbsolutePath().toString());

			driver = new ChromeDriver(options);

			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
			driver.get("https://www.brainyquote.com/topics/motivational-quotes");
			driver.manage().window().maximize();

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

			List<WebElement> quoteTags = wait
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("a.b-qt")));
			System.out.println("Found " + quoteTags.size() + " quotes.");

			Random random = new Random();

			if (!quoteTags.isEmpty()) {
				selectedQuote = quoteTags.get(random.nextInt(quoteTags.size())).getText();
			}
		}

		finally {
			if (driver != null) {
				driver.quit();
			}
			Thread.sleep(1000); // Wait for 1 second
			DirectoryDeleter.deleteDirectory(userDataDir);
			
//			if (userDataDir != null) {
//				Files.deleteIfExists(userDataDir);
//			}
		}
		return selectedQuote;
	}

	public static void sendEmail(String quote) {
		String senderEmail = "srividya18.2002@gmail.com";
		String receiverEmail = "srividya18.2002@gmail.com";
		String emailPassword = "qhaopqeommzqwriu";

		// Email content
		String subject = "Your Daily Motivation Quote";
		String body = "Here’s your daily dose of motivation:\n\n\"" + quote + "\"\n\nHave a great day!";

		// Set email properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		// Authenticate and send email
		Session session = Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, emailPassword);
			}
		});

		try {
			// Create email message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
			message.setSubject(subject);
			message.setText(body);

			// Send email
			Transport.send(message);
			System.out.println("Email sent successfully!");
		} catch (MessagingException e) {
			e.printStackTrace();
			System.out.println("Error sending email: " + e.getMessage());
		}
	}
}
