# producers-auth

0.0.0 build 9 Added setSmtpUser and sertSmtpPass to ResetPasswordMailer
0.0.0 build 10 Making LoginServlet extendable
0.0.0 build 11 Added Recaptcha support
0.0.0 build 12 Moved repo to producersmarket. New DB struture. Moved prepared statements to db.
0.0.0 build 13 RegisterMailer better exception handling and returning resetLink even if mail fails.
0.0.0 build 14 Made RegisterMailer multipart alternative for sending HTML content
0.0.0 build 15 Changed SmtpClient provider to Javapoets for RegisterMailer
0.0.0 build 17 Added SecurityUtil.java for SHA1 password hashing
0.0.0 build 18 Dynamically injecting the ConnectionPool object
0.0.0 build 19 Added `getConnectionPool()` to `ParentServlet`
0.0.0 build 20 Added `getConnectionManager()` to `ParentServlet`
0.0.0 build 21 Added `ConnectionManager` to `RegisterMailer`
0.0.0 build 22 Added `ConnectionManager` to `ResetPasswordMailer`
0.0.0 build 23 Added `jetty.version`, `javamail.version` to `gradle.properties`
0.0.0 build 24 Added `jetty.version`, `javamail.version` to `gradle.properties`
