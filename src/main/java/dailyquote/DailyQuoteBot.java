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
import java.util.ArrayList;
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

	public static String getRandomQuote() throws IOException, InterruptedException {
		WebDriver driver = null;
		Path userDataDir = null;
		String randomFormattedQuote = "Keep going, you're doing great!"; // Default

		try {
			System.out.println("Launching Chrome");
			
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless=new");
//			options.addArguments("--disable-gpu");
//			options.addArguments("--no-sandbox");
//			options.addArguments("--disable-dev-shm-usage");
//			options.addArguments(
//					"--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

			userDataDir = Files.createTempDirectory("chrome-user-data");
			options.addArguments("--user-data-dir=" + userDataDir.toAbsolutePath());

			driver = new ChromeDriver(options);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			driver.get("https://quotes.toscrape.com");

			List<String> allQuotes = new ArrayList<>();

			while (true) {
				List<WebElement> quotes = wait
						.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("quote")));

				for (WebElement quote : quotes) {
					String text = quote.findElement(By.className("text")).getText();
					String author = quote.findElement(By.className("author")).getText();

					String formatted = String.format("%s — %s", text, author);
					allQuotes.add(formatted);
				}				
				
				List<WebElement> nextButton = driver.findElements(By.cssSelector(".pager .next a"));
				if (nextButton.isEmpty())
					break;
				nextButton.get(0).click();
			}
			
			System.out.println("Total quotes scraped: " + allQuotes.size());		


			if (!allQuotes.isEmpty()) {
				randomFormattedQuote = allQuotes.get(new Random().nextInt(allQuotes.size()));
			}

		} finally {
			if (driver != null)
				driver.quit();
			if (userDataDir != null)
				DirectoryDeleter.deleteDirectory(userDataDir);
		}

		return randomFormattedQuote;
	}

	public static void sendEmail(String quote) {
//		String senderEmail = System.getenv("EMAIL_USER");
//		String receiverEmail = System.getenv("EMAIL_USER");
//		String emailPassword = System.getenv("EMAIL_PASS");

		String senderEmail = System.getenv().getOrDefault("EMAIL_USER", "srividya18.2002@gmail.com");
		String receiverEmail = System.getenv().getOrDefault("EMAIL_USER", "srividya18.2002@gmail.com");
		String emailPassword = System.getenv().getOrDefault("EMAIL_PASS", "qhaopqeommzqwriu");

		// Email content
		String subject = "Your Daily Motivation Quote";
		String body = "Here’s your daily dose of motivation:\n\n" + quote + "\n\nHave a great day!";

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
