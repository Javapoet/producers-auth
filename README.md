# producers-auth

0.0.0.9 Added setSmtpUser and sertSmtpPass to ResetPasswordMailer
0.0.0.10 Making LoginServlet extendable
0.0.0.11 Added Recaptcha support
0.0.0.12 Moved repo to producersmarket. New DB struture. Moved prepared statements to db.
0.0.0.13 RegisterMailer better exception handling and returning resetLink even if mail fails.
0.0.0.14 Made RegisterMailer multipart alternative for sending HTML content
0.0.0.15 Changed SmtpClient provider to Javapoets for RegisterMailer
0.0.0.17 Added SecurityUtil.java for SHA1 password hashing
0.0.0.17 Dynamically injecting the ConnectionPool object
