package com.producersmarket.auth.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.ispaces.mail.model.PreparedEmail;
import com.ispaces.mail.model.PreparedEmails;

import com.producersmarket.auth.database.RegisterDatabaseManager;
import com.producersmarket.auth.database.ResetPasswordDatabaseManager;
import com.producersmarket.auth.database.UserDatabaseManager;
import com.producersmarket.auth.mailer.RegisterMailer;
import com.producersmarket.model.User;

public class ResendEmailServlet extends ParentServlet {

    private static final Logger logger = LogManager.getLogger();

    private String confirmEmailPage = null;
  
    public void init(ServletConfig config)
      throws ServletException
    {
        logger.debug("init(" + config + ")");
    
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("doGet(request, response)");

        try {

            String activationCode = null;
            String pathInfo = request.getPathInfo();
            if(pathInfo != null) activationCode = pathInfo.substring(1, pathInfo.length());

            logger.debug("activationCode = "+activationCode);

            //User user = UserDatabaseManager.selectUserByPasswordResetCode(code);
            //int userId = ResetPasswordDatabaseManager.selectUserIdByPasswordResetCode(activationCode, getConnectionManager());
            User user = UserDatabaseManager.selectUserByActivationCode(activationCode, getConnectionManager());

            if(user != null) {
            //if(userId != -1) {

                String email = user.getEmail();
                logger.debug("email = "+email);

                ServletContext servletContext = getServletContext();
                logger.debug("servletContext = "+servletContext);

                String smtpServer = (String)servletContext.getAttribute("smtpServer");
                String smtpPort = (String)servletContext.getAttribute("smtpPort");
                String smtpUser = (String)servletContext.getAttribute("smtpUser");
                String smtpPass = (String)servletContext.getAttribute("smtpPass");
                String emailAddressSupport = (String)servletContext.getAttribute("emailAddressSupport");
                String contextUrl = (String)servletContext.getAttribute("contextUrl");
                String resetPasswordEmailFrom = (String)servletContext.getAttribute("resetPasswordEmailFrom");
                    
                logger.debug("contextUrl = "+contextUrl);
                logger.debug("resetPasswordEmailFrom = "+resetPasswordEmailFrom);
                logger.debug("smtpServer = "+smtpServer);
                logger.debug("smtpPort = "+smtpPort);
                logger.debug("smtpUser = "+smtpUser);

                Properties properties = new Properties();

                properties.put("smtpServer", smtpServer);
                properties.put("smtpPort", smtpPort);
                properties.put("smtpUser", smtpUser);
                properties.put("smtpPass", smtpPass);
                properties.put("emailTo", email);
                properties.put("emailFrom", resetPasswordEmailFrom);
                properties.put("contextUrl", contextUrl);

                logger.debug("properties = "+properties);
                request.setAttribute("properties", properties);

                logger.debug("activationCode = "+activationCode);
                request.setAttribute("activationCode", activationCode);

                String accountActivationLink = new StringBuilder()
                  .append(contextUrl)
                  .append("/confirm-email/")
                  .append(activationCode)
                  .toString()
                ;

                logger.debug("accountActivationLink = "+accountActivationLink);
                request.setAttribute("accountActivationLink", accountActivationLink);
                properties.setProperty("accountActivationLink", accountActivationLink);

                try {

                    PreparedEmail preparedEmail = PreparedEmails.getPreparedEmail("confirm-email", getConnectionManager());

                    try {

                        RegisterMailer.send(properties, preparedEmail);

                        includeUtf8(request, response, "/view/register-confirmation.jsp");

                        return;

                    } catch(Exception e) {
                        StringWriter stringWriter = new StringWriter();
                        PrintWriter printWriter = new PrintWriter(stringWriter);
                        e.printStackTrace(printWriter);
                        logger.error(stringWriter.toString());
                    }

                } catch(Exception e) {
                    StringWriter stringWriter = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(stringWriter);
                    e.printStackTrace(printWriter);
                    logger.error(stringWriter.toString());
                }

            } else {

                logger.warn("User Not Found! activationCode = "+activationCode);

                include(request, response, "/reset-password");

                return;
            }

        } catch(Exception e) {

            logger.warn(e.getMessage(), e);
            e.printStackTrace();

        }

        //writeOut(response, ZERO);
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("doPost(request, response)");

        String userIdString = request.getParameter("userId");
        String name = request.getParameter("name");
        int userId = -1;

        logger.debug("userIdString = "+userIdString);
        logger.debug("name = "+name);

        if(userIdString != null) {
            userId = Integer.parseInt(userIdString);
        }
        
        logger.debug("userId = "+userId);

        try {

            /*
            HttpSession httpSession = request.getSession(false);

            if(httpSession != null) {

                String sessionId = httpSession.getId();
                logger.debug("sessionId = "+sessionId);

                Integer userIdInteger = (Integer)httpSession.getAttribute("userId");
                logger.debug("userIdInteger = "+userIdInteger);

                if(userIdInteger != null) {

                    int userId = userIdInteger.intValue();

                    logger.debug("userId = "+userId);
            */                    

                    RegisterDatabaseManager.updateName(userId, name, getConnectionManager());

                    //include(request, response, "/view/home.jsp");
                    includeUtf8(request, response, this.confirmEmailPage != null ? this.confirmEmailPage : "/view/confirm-email.jsp");

                    return;
            /*                    
                }
            }
            */
        } catch(java.sql.SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

/*
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("doPost(request, response)");

        String code = request.getParameter("code");
        String hash = request.getParameter("hash");

        //String password = request.getParameter("password");
        //String confirmPassword = request.getParameter("confirmPassword");

        logger.debug("code = '"+code+"'");
        logger.debug("hash = '"+hash+"'");
        //logger.debug("doPost(request, response): password = '"+password+"', confirmPassword = '"+confirmPassword+"'");

        try {

            //User user = UserDatabaseManager.selectUserByPasswordResetCode(code);
            int userId = ResetPasswordDatabaseManager.selectUserIdByPasswordResetCode(code);

            //if(user != null) {
            if(userId != -1) {

                //ResetPasswordDatabaseManager.updatePassword(user.getId(), hash);
                ResetPasswordDatabaseManager.updatePassword(userId, hash);

                //String message = "Your password has been reset.<br/>You can log in below.";
                String message = "Your password has been reset.<br/>Please log in below.";

                request.setAttribute("message", message);

                //response.sendRedirect(com.ispaces.web.servlet.InitServlet.init.getProperty("contextUrl"));
                //response.sendRedirect(com.ispaces.web.servlet.InitServlet.init.getProperty("contextUrl")+"/admin/login");
                //include(request, response, DIR_VIEW+"admin/login.jsp", "text/html; charset=UTF-8");
                include(request, response, "/view/login.jsp");

                //ResetPasswordDatabaseManager.deleteActivationCode(user.getId(), code);  // Delete the reset code after it has been used.
                //ResetPasswordDatabaseManager.deleteActivationCode(user.getId());  // Delete the reset code after it has been used.
                ResetPasswordDatabaseManager.deleteActivationCode(userId);  // Delete the reset code after it has been used.
            }

        } catch(java.sql.SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
*/


}


