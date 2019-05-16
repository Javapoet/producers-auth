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

    private String confirmEmailPage = null;
  
    public void init(ServletConfig config) throws ServletException {
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
            logger.debug("code = "+code);

            int userId = ResetPasswordDatabaseManager.selectUserIdByPasswordResetCode(code, getConnectionPool());
            if(userId != -1) {

                logger.debug("userId = "+userId);
                request.setAttribute("code", code);
                request.setAttribute("userId", userId);

                HttpSession httpSession = request.getSession(true); // create the session
                logger.debug("httpSession.getId() = "+httpSession.getId());
                httpSession.setAttribute("userId", userId);

                ResetPasswordDatabaseManager.updateUserEmailVerified(userId, getConnectionPool());  // delete the reset code after it has been used
                                            
                includeUtf8(request, response, this.confirmEmailPage != null ? this.confirmEmailPage : "/view/confirm-email.jsp");
                return;

            } else {

                logger.warn("User Not Found! Account activation token may have expired or has been used already.");

                String header = "Whoops! Looks like this link has expired.";
                String message = "Request a new <a href='reset-password'>reset link</a>";
                request.setAttribute("header", header);
                request.setAttribute("message", message);
                request.setAttribute("iconImage", "view/svg/producers-humming-bird1.svg");

                include(request, response, "/view/confirmation-message.jsp");
                return;
            }

        } catch(Exception e) {

            logger.warn("Error in ResetPasswordControl: "+e.getMessage());
            e.printStackTrace();

        }

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

            RegisterDatabaseManager.updateName(userId, name, getConnectionPool());

            //include(request, response, "/view/home.jsp");
            includeUtf8(request, response, this.confirmEmailPage != null ? this.confirmEmailPage : "/view/confirm-email.jsp");

            return;

        } catch(java.sql.SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

}


