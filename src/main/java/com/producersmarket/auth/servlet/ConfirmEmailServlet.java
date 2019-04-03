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

import com.producersmarket.auth.database.RegisterDatabaseManager;
import com.producersmarket.auth.database.ResetPasswordDatabaseManager;

public class ConfirmEmailServlet extends ParentServlet {

    private static final Logger logger = LogManager.getLogger();

    /*
    private static final String USERNAME = "username";
    private static final String FORGOT_PASSWORD_EMAIL_SENT_CONFIRMATION = "forgot.password.email.sent.confirmation";
    private static final String RESET_PASSWORD_EMAIL_SENT = "reset.password.email.sent";
    private static final String FORGOT_PASSWORD_EMAIL_INVALID = "forgot.password.email.invalid";
    private static final String FORGOT_PASSWORD_USER_NOT_FOUND = "forgot.password.user.not.found";
    */

    private String confirmEmailPage = null;
  
    public void init(ServletConfig config)
      throws ServletException
    {
        logger.debug("init(" + config + ")");
    
        this.confirmEmailPage = config.getInitParameter("confirmEmailPage");
    
        logger.debug("confirmEmailPage = " + this.confirmEmailPage);
    
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("doGet(request, response)");

        try {

            String code = null;
            String pathInfo = request.getPathInfo();
            if(pathInfo != null) code = pathInfo.substring(1, pathInfo.length());

            //logger.debug("doGet(request, response): code = '"+code+QUOTE);
            logger.debug("code = "+code);

            //User user = UserDatabaseManager.selectUserByPasswordResetCode(code);
            int userId = ResetPasswordDatabaseManager.selectUserIdByPasswordResetCode(code, getConnectionManager());

            //if(user != null) {
            if(userId != -1) {

                //logger.debug("user = "+user);
                logger.debug("userId = "+userId);

                request.setAttribute("code", code);
                request.setAttribute("userId", userId);

                HttpSession httpSession = request.getSession(true); // create the session
                logger.debug("httpSession.getId() = "+httpSession.getId());

                httpSession.setAttribute("userId", userId);

                

                ResetPasswordDatabaseManager.deleteActivationCode(userId, getConnectionManager());  // Delete the reset code after it has been used.
                
                //include(request, response, "/view/home.jsp", "text/html; charset=UTF-8");
                //include(request, response, "/view/login.jsp", "text/html; charset=UTF-8");
                //include(request, response, "/view/user-profile.jsp", "text/html; charset=UTF-8");
                //includeUtf8(request, response, this.confirmEmailPage);
                includeUtf8(request, response, this.confirmEmailPage != null ? this.confirmEmailPage : "/view/confirm-email.jsp");

                return;

            } else {

                logger.warn("User Not Found! Account activation token may have expired or has been used already.");

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
                String header = "Password Reset";
                String message = "Password reset token has expired or been used already.";
                request.setAttribute("header", header);
                request.setAttribute("message", message);
                request.setAttribute("iconImage", "view/svg/producers-humming-bird1.svg");


                /*
                PrintWriter out = response.getWriter();
                response.setContentType(TEXT_PLAIN);
                out.write(message);
                out.close();
                */


                //include(request, response, DIR_VIEW + "confirmationMessage.jsp");
                include(request, response, "/view/confirmation-message.jsp");

                return;
            }

        } catch(Exception e) {

            logger.warn("Error in ResetPasswordControl: "+e.getMessage());
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


