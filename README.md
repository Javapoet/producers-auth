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
0.0.0 build 24 Added `getConnectionManager()` to `ResetPasswordDatabaseManager` and `ConfirmEmailServlet`
0.0.0 build 25 Added `httpSession.setAttribute("userId", userId)` to `ConfirmEmailServlet`
0.0.0 build 26 Added `UserDatabaseManager` and `groupIdList`
0.0.0 build 27 Removed database access dependency from `RegisterMailer`
0.0.0 build 28 Added email capture from login form to forgot password
0.0.0 build 29 Capturing user/email when click log in from register page
0.0.0 build 30 Passing ConnectionManager to ResetPasswordDatabaseManager
0.0.0 build 31 Added logoutUser
0.0.0 build 32 Added dynamic `preparedEmailJspPath` to RegisterMailer
0.0.0 build 33 Adjust JSP path in ResetPasswordMailer.java for new contextUrl
0.0.0 build 34 Changed to SessionDatabaseManager.insert(session, getConnectionPool())
0.0.0 build 35 Google Secret Key now dynamic, not hard coded
0.0.0 build 36 Changed all database manager classes to use ConnectionPool
0.0.0 build 37 Changed login redirect from `servletContext().getRequestDispatcher(url).forward(request, response)` to `response.sendRedirect(path)`
0.0.0 build 38 Made resetPasswordEmailSentPage dynamic with default /view/confirmation/reset-password-email-sent.jsp