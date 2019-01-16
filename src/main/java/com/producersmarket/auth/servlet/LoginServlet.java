package com.producersmarket.auth.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.producersmarket.auth.database.LoginDatabaseManager;
import com.producersmarket.auth.database.SessionDatabaseManager;
import com.producersmarket.auth.model.Session;

public class LoginServlet extends ParentServlet {

    private static final Logger logger = LogManager.getLogger();
    private static final String EMPTY = "";
    private static final String SPACE = " ";
    private static final String COMMA = ",";
    private static final String LEFT_SQUARE_BRACKET = "[";
    private static final String RIGHT_SQUARE_BRACKET = "]";

    private String loginPage = "/view/login.jsp";
    private String loggedInPage = "/view/home.jsp";

    /*
    protected String loginPage = null;
    protected String loggedInPage = null;

    private String loginPage = null;
    private String loggedInPage = null;
    */

    public void init(ServletConfig config) throws ServletException {
        logger.debug("init("+config+")");

        /*
        this.loginPage = config.getInitParameter("loginPage");
        this.loggedInPage = config.getInitParameter("loggedInPage");

        logger.debug("loginPage = "+loginPage);
        logger.debug("loggedInPage = "+loggedInPage);
        */

        super.init(config);
    }

    protected void setLoginPage(String loginPage) {
        logger.debug("setLoginPage("+loginPage+")");
        this.loginPage = loginPage;
    }

    protected void setLoggedInPage(String loggedInPage) {
        logger.debug("setLoggedInPage("+loggedInPage+")");
        this.loggedInPage = loggedInPage;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("doGet(request, response)");

        try {

            //includeUtf8(request, response, this.loginPage);
            includeUtf8(request, response, loginPage);

        } catch(java.io.FileNotFoundException e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            logger.error(stringWriter.toString());
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("doPost(request, response)");

        String email = request.getParameter("username");
        String password = request.getParameter("hash");
            
        logger.debug("email = "+email);
        logger.debug("password = "+password);

        if(email == null || email.equals(EMPTY)) {

            request.setAttribute("usernameError", "Please Enter an Email Address");
            includeUtf8(request, response, this.loginPage);
            return;

        } else if(password == null || password.equals(EMPTY)) {

            request.setAttribute("passwordError", "Please Enter a Password");

        } else {
            // make sure the email is lowercase
            email = email.toLowerCase();
        }

        try {

            String passwordHash = LoginDatabaseManager.selectPasswordHashByEmail(email);
            logger.debug("passwordHash = "+passwordHash);

            if(
                 passwordHash != null
              && passwordHash.equals(password)
            ) {

                HttpSession httpSession = request.getSession(true); // create the session
                logger.debug("httpSession.getId() = "+httpSession.getId());

                //User user = UserDatabaseManager.selectUserByEmail(email);
                int userId = LoginDatabaseManager.selectUserIdByEmail(email);
                //logger.debug("user = "+user);
                logger.debug("userId = "+userId);

                //if(user != null) {
                if(userId != -1) {

                    //logger.debug("user.getId() = "+user.getId());
                    //httpSession.setAttribute("userId", user.getId());
                    //request.setAttribute("user", user);
                    httpSession.setAttribute("userId", userId);

                    String serverInfo = getServletContext().getServerInfo();
                    String serverName = request.getServerName();
                    int serverPort = request.getServerPort();
                    String accept = request.getHeader("Accept");
                    String acceptEncoding = request.getHeader("Accept-Encoding");
                    String acceptCharset = request.getHeader("Accept-Charset");
                    String acceptLanguage = request.getHeader("Accept-Language");
                    String host = request.getHeader("Host");
                    String userAgent = request.getHeader("User-Agent");
                    String referer = request.getHeader("Referer");
                    String locale = (request.getLocale()).toString();
                    String characterEncoding = request.getCharacterEncoding();
                    String remoteAddr = request.getRemoteAddr();
                    String protocol = request.getProtocol();

                    logger.debug("accept = " + accept);
                    logger.debug("acceptEncoding = " + acceptEncoding);
                    logger.debug("acceptCharset = " + acceptCharset);
                    logger.debug("acceptLanguage = " + acceptLanguage);
                    logger.debug("host = " + host);
                    logger.debug("userAgent = " + userAgent);
                    logger.debug("referer = " + referer);
                    logger.debug("locale = " + locale);
                    logger.debug("characterEncoding = " + characterEncoding);
                    logger.debug("remoteAddr = " + remoteAddr);
                    logger.debug("serverInfo = " + serverInfo);
                    logger.debug("serverName = " + serverName);
                    logger.debug("serverPort = " + serverPort);
                    logger.debug("protocol = " + protocol);

                    Session session = new Session();
                    session.setUserId(userId);
                    session.setSessionId(httpSession.getId());
                    session.setServerInfo(serverInfo);
                    session.setServerName(serverName);
                    session.setServerPort(serverPort);
                    session.setRemoteAddr(remoteAddr);
                    session.setLocale(locale);
                    session.setCharacterEncoding(characterEncoding);
                    session.setUserAgent(userAgent);
                    session.setAccept(accept);
                    session.setAcceptEncoding(acceptEncoding);
                    session.setAcceptCharset(acceptCharset);
                    session.setAcceptLanguage(acceptLanguage);
                    session.setHost(host);
                    session.setReferer(referer);
                    session.setProtocol(protocol);

                    /*
                    int sessionId = SessionManager.insert(session);
                    SessionDatabaseManager.insertSession(user.getId(), session.getId());
                    */
                    SessionDatabaseManager.insert(session);

                    /*
                    LoginDatabaseManager.updateUserLoggedIn(user.getId(), session.getId());

                    // Request attributes
                    //request.setAttribute("userId", user.getId());
                    //request.setAttribute("userName", firstName + SPACE + lastName);
                    //request.setAttribute("userName", name);
                    //request.setAttribute("email", email);
                    //request.setAttribute("humanApiAccessToken", humanApiAccessToken);
                    //request.setAttribute("humanApiPublicToken", humanApiPublicToken);
                    //request.setAttribute("googleId", googleIdString);
                    //request.setAttribute("googleId", id);
                    //request.setAttribute("googleAccessToken", accessToken);
                    //logger.debug("backendUrl = "+backendUrl);
                    //response.sendRedirect(backendUrl + "/store");
                    */

                    includeUtf8(request, response, this.loggedInPage);

                    return;
                    
                } // if(userId != null) {

            } else { // if(passwordHash != null

                //request.setAttribute("errorMessage", "Incorrect password");
                request.setAttribute("passwordError", "Incorrect password, try again");
                includeUtf8(request, response, this.loginPage);
                return;

            } // if(passwordHash != null

        } catch(java.sql.SQLException sqlException) {

            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            sqlException.printStackTrace(printWriter);
            logger.error(stringWriter.toString());

            logException(sqlException);

        } catch(Exception exception) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            exception.printStackTrace(printWriter);
            logger.error(stringWriter.toString());
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

}
