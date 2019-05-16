package com.producersmarket.auth.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/*
import com.ispaces.manager.ForgotPasswordManager;
import com.ispaces.manager.UserManager;
import com.ispaces.model.User;
import com.ispaces.util.ResourceBundles;
import com.ispaces.util.SecurityUtil;
*/
//import com.producersmarket.blog.database.UserDatabaseManager;
import com.producersmarket.auth.database.ResetPasswordDatabaseManager;
//import com.producersmarket.model.User;

public class PasswordResetServlet extends ParentServlet {

    private static final Logger logger = LogManager.getLogger();

    /*
    private static final String USERNAME = "username";
    private static final String FORGOT_PASSWORD_EMAIL_SENT_CONFIRMATION = "forgot.password.email.sent.confirmation";
    private static final String RESET_PASSWORD_EMAIL_SENT = "reset.password.email.sent";
    private static final String FORGOT_PASSWORD_EMAIL_INVALID = "forgot.password.email.invalid";
    private static final String FORGOT_PASSWORD_USER_NOT_FOUND = "forgot.password.user.not.found";
    */

    private String passwordResetPage = "/view/password-reset.jsp";
    private String loginPage = "/view/login.jsp";
    private String resetPasswordEmailSentPage = "/view/confirmation/reset-password-email-sent.jsp";

    public void init(ServletConfig config) throws ServletException {
        logger.debug("init("+config+")");

        String passwordResetPage = config.getInitParameter("passwordResetPage");
        if(passwordResetPage != null) this.passwordResetPage = passwordResetPage;
        logger.debug("this.passwordResetPage = " + this.passwordResetPage);

        String loginPage = config.getInitParameter("loginPage");
        if(loginPage != null) this.loginPage = loginPage;
        logger.debug("this.loginPage = " + this.loginPage);

        String resetPasswordEmailSentPage = config.getInitParameter("resetPasswordEmailSentPage");
        if(resetPasswordEmailSentPage != null) this.resetPasswordEmailSentPage = resetPasswordEmailSentPage;
        logger.debug("this.resetPasswordEmailSentPage = " + this.resetPasswordEmailSentPage);

        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("doGet(request, response)");

        try {

            String code = null;
            String pathInfo = request.getPathInfo();
            if(pathInfo != null) code = pathInfo.substring(1, pathInfo.length());

            //logger.debug("doGet(request, response): code = '"+code+QUOTE);

            //User user = UserDatabaseManager.selectUserByPasswordResetCode(code);
            int userId = ResetPasswordDatabaseManager.selectUserIdByPasswordResetCode(code, getConnectionPool());
            logger.debug("userId = "+userId);

            //if(user != null) {
            if(userId != -1) {

                request.setAttribute("code", code);
                
                //include(request, response, "/view/password-reset.jsp", "text/html; charset=UTF-8");
                //include(request, response, this.passwordResetPage, "text/html; charset=UTF-8");
                includeUtf8(request, response, this.passwordResetPage);

                return;

            } else {

                logger.warn("User Not Found. Password reset token may have expired or been used already.");

                /*
                java.util.ResourceBundle rb = null;
                rb = (java.util.ResourceBundle)request.getAttribute("rb");
                if(rb == null) {
                    HttpSession session = request.getSession(false);
                    rb = (java.util.ResourceBundle)session.getAttribute("rb");
                }
                if(rb == null) rb = (java.util.ResourceBundle)getServletContext().getAttribute("rb");
                if(rb == null) rb = java.util.ResourceBundle.getBundle("iSpaces", com.ispaces.web.servlet.InitServlet.DEFAULT_LOCALE);
                */

                /*
                String lang = (String)request.getAttribute("lang");
                if(lang == null) {
                    lang = (String)getServletContext().getAttribute("lang");
                    if(lang == null) {
                        //lang = com.ispaces.servlet.InitServlet.DEFAULT_LOCALE.getLanguage();
                        lang = java.util.Locale.getDefault().getLanguage();
                        if(lang == null) lang = "en";
                    }
                }

                ResourceBundles rb = new ResourceBundles(lang);

                String FORGOT_PASSWORD_USER_NOT_FOUND = "forgot.password.user.not.found";
                */

                //String message = rb.getString(FORGOT_PASSWORD_USER_NOT_FOUND);
                String header = "Whoops! Looks like this link has expired.";
                String message = "Request a new <a href='reset-password'>reset link</a>";
                request.setAttribute("header", header);
                request.setAttribute("message", message);

                /*
                PrintWriter out = response.getWriter();
                response.setContentType(TEXT_PLAIN);
                out.write(message);
                out.close();
                */


                //include(request, response, DIR_VIEW + "confirmationMessage.jsp");
                //include(request, response, "/view/confirmation-message.jsp");
                //include(request, response, "/view/confirmation-message.jsp");
                includeUtf8(request, response, "/view/confirmation-message.jsp");

                return;
            }

        } catch(Exception e) {

            logger.warn("Error in "+getClass().getName()+": "+e.getMessage());
            e.printStackTrace();

        }

        //writeOut(response, ZERO);
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("doPost(request, response)");

        String code = request.getParameter("code");
        String hash = request.getParameter("hash");

        logger.debug("code = "+code);
        logger.debug("hash = "+hash);
        
        if( hash == null || hash.equals(EMPTY) ) {

            String errorMessage = "Please enter a password";
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("code", code);
            include(request, response, this.passwordResetPage, "text/html; charset=UTF-8");

        } else {

            try {
    
                int userId = ResetPasswordDatabaseManager.selectUserIdByPasswordResetCode(code, getConnectionPool());
    
                if(userId != -1) {
    
                    ResetPasswordDatabaseManager.updatePassword(userId, hash, getConnectionPool());
    
                    String message = "Your password has been reset.<br/>Please log in below.";
                    request.setAttribute("message", message);
    
                    includeUtf8(request, response, this.loginPage);
    
                    ResetPasswordDatabaseManager.deleteActivationCode(userId, getConnectionPool());  // Delete the reset code after it has been used.
                }
    
            } catch(java.sql.SQLException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
                String errorMessage = "Error: please try again later";
                request.setAttribute("errorMessage", errorMessage);
                request.setAttribute("code", code);
                include(request, response, this.passwordResetPage, "text/html; charset=UTF-8");
            }
        }
    }

}


