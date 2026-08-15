# ECommerce Web Application

A full-stack e-commerce web application built with Spring Boot, supporting both customer shopping and admin management.

## Features

- User registration, login & role-based access (User / Admin)
- Product browsing with categories
- Shopping cart & checkout
- Order management and order history
- Online payments via Razorpay
- Email notifications (order confirmation, password reset) via Gmail SMTP
- Admin dashboard for managing products, categories, orders and users
- Profile management with image upload

## Tech Stack

- **Backend:** Spring Boot 3.5.6, Spring Security, Spring Data JPA
- **Frontend:** Thymeleaf, HTML, CSS, JavaScript
- **Database:** MySQL
- **Payments:** Razorpay
- **Email:** Spring Mail (Gmail SMTP)
- **Build Tool:** Maven
- **Java Version:** 21

## Prerequisites

- Java 21+
- Maven
- MySQL Server running locally
- A Gmail account with an [App Password](https://myaccount.google.com/apppasswords) (for email notifications)
- A Razorpay account (Test Mode keys are enough for local development)

## Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/Yashupase09/ecommerce-web-application.git
   cd ecommerce-web-application
   ```

2. **Create a MySQL database**
   ```sql
   CREATE DATABASE ecommerce;
   ```

3. **Configure environment variables**

   Copy `.env.example` to `.env` in the project root, and fill in your own values:
   ```bash
   cp .env.example .env
   ```

   `.env` requires:
   ```
   DB_URL=jdbc:mysql://localhost:3306/ecommerce
   DB_USERNAME=root
   DB_PASSWORD=your_mysql_password

   MAIL_USERNAME=your_email@gmail.com
   MAIL_PASSWORD=your_gmail_app_password

   RAZORPAY_KEY_ID=your_razorpay_key_id
   RAZORPAY_KEY_SECRET=your_razorpay_key_secret
   ```

4. **Build the project**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

6. **Access the app**

   Open [http://localhost:8080](http://localhost:8080) in your browser.

## Notes

- Never commit your `.env` file — it's already excluded via `.gitignore`.
- Use Razorpay **Test Mode** keys during local development.
