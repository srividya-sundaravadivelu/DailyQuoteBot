# 🌞 DailyQuoteBot 
Your daily dose of motivation, delivered straight to your inbox.

DailyQuoteBot is a Java-based automation tool that scrapes motivational quotes from the web and emails them to you every morning. It's powered by GitHub Actions, ensuring timely delivery without manual intervention.

For a detailed walkthrough of how this project was conceived and built, check out my blog post:
https://www.numpyninjaacademy.com/post/how-i-built-a-daily-motivation-bot-using-web-scraping-and-github-actions
In the post, I delve into the motivations behind the project, the challenges faced during development, and the solutions implemented to overcome them.

----

## 📌 Features
Automated Web Scraping: Extracts a fresh motivational quote daily from a designated website.

Email Delivery: Sends the quote to your specified email address.

Scheduled Execution: Utilizes GitHub Actions to run the bot every day at 9:00 AM CDT.

Secure Credentials: Manages sensitive information using GitHub Secrets.

--

## 🛠️ Setup Instructions
1. Clone the Repository
```bash
git clone https://github.com/srividya-sundaravadivelu/DailyQuoteBot.git
cd DailyQuoteBot
```
2. Configure GitHub Secrets
Navigate to your repository's Settings > Secrets and variables > Actions and add the following secrets:

EMAIL_USER – Your Gmail address.

EMAIL_PASS – Your Gmail App Password.
Note: Ensure you have enabled 2-Step Verification on your Google account and generated an App Password for this application.

3. Review GitHub Actions Workflow
The .github/workflows/daily-email.yml file is pre-configured to:

Run daily at 9:00 AM CDT.

Compile and execute the Java program.

Send the scraped quote via email.

No additional configuration is required unless you wish to modify the schedule or functionality.

## 📧 How It Works

Quote Extraction: The Java program scrapes a motivational quote from the target website.

Email Composition: Formats the quote into an email-friendly message.

Email Sending: Authenticates using the provided credentials and sends the email to the specified address.

Automation: GitHub Actions ensures this process runs seamlessly every day.

## 🔒 Security Considerations
Credential Management: All sensitive data is stored securely using GitHub Secrets.

App Passwords: Utilizing Gmail App Passwords adds an extra layer of security, especially when 2-Step Verification is enabled.

## 🧠 Inspiration
The idea behind DailyQuoteBot stemmed from a desire to combine web scraping with automation. Instead of relying on an API to get the quotes, this project delves into the intricacies of web scraping, scheduling, and secure credential management, offering a hands-on experience in automation.
