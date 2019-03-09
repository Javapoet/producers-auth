package com.producersmarket.auth.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.json.JSONObject;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.producersmarket.auth.database.LoginDatabaseManager;
import com.producersmarket.auth.database.SessionDatabaseManager;
import com.producersmarket.auth.database.UserDatabaseManager;
import com.producersmarket.auth.model.Session;
import com.producersmarket.model.User;

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

    /*
     * Secret key
     * Use this for communication between your site and Google. Be sure to keep it a secret.
     */
    private String secretKey = "6Ld5B40UAAAAAJ6MEjJiYZQiTlCuBvJSduqcnfzO";

    /*
     * Site key
     */
    private String siteKey = "6Ld5B40UAAAAAL65r3R16dgVU467wUHZmEPFDN_I";

    /**
     * Validates Google reCAPTCHA V2 or Invisible reCAPTCHA.
     * https://developers.google.com/recaptcha/docs/verify
     *
     * @param secretKey Secret key (key given for communication between your site and Google)
     * @param response reCAPTCHA response from client side. (g-recaptcha-response)
     * @return true if validation successful, false otherwise.
     */
    //public boolean isCaptchaValid(String secretKey, String response, String remoteAddr) {
    public boolean isCaptchaValid(String secretKey, String response) {
        //logger.debug("isCaptchaValid("+secretKey+", "+response+", "+remoteAddr+")");
        logger.debug("isCaptchaValid("+secretKey+", "+response+")");

        try {

            String requestUrl = "https://www.google.com/recaptcha/api/siteverify";

            String url = new StringBuilder()
                .append("https://www.google.com/recaptcha/api/siteverify")
                .append("?secret=").append(secretKey)
                //.append("&remoteip=").append(remoteAddr)
                .append("&response=").append(response)
                .toString();

            logger.debug(url);

            InputStream inputStream = new URL(url).openStream();
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            StringBuilder stringBuilder = new StringBuilder();
            int codePoint;

            while((codePoint = bufferedReader.read()) != -1) {
                stringBuilder.append((char) codePoint);
            }

            String jsonString = stringBuilder.toString();
            logger.debug(jsonString);

            inputStream.close();

            JSONObject jsonObject = new JSONObject(jsonString);
            logger.debug(jsonObject);
            
            return jsonObject.getBoolean("success");

        } catch (Exception e) {
            return false;
        }
    }

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

            String username = null;
            String pathInfo = request.getPathInfo();
            if(pathInfo != null) username = pathInfo.substring(1, pathInfo.length());

            logger.debug("username = "+username);

            request.setAttribute("username", username);

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

        //String remoteAddr = request.getRemoteAddr();
        String email = request.getParameter("username");
        String password = request.getParameter("hash");
        String recaptchaResponse = request.getParameter("g-recaptcha-response");
        //String secretKey = "6Ld5B40UAAAAAJ6MEjJiYZQiTlCuBvJSduqcnfzO";

        boolean isRememberMe = false;
        String rememberMe = request.getParameter("rememberMe");
        if(rememberMe != null) {
            try {
                isRememberMe = new Boolean(rememberMe).booleanValue();
            } catch(Exception e) {}  // Ignore exception... remember me is not checked.
        }
        logger.debug("isRememberMe = "+(isRememberMe));
        //user.setRememberMe(isRememberMe);

            
        //logger.debug("remoteAddr = "+remoteAddr);
        logger.debug("email = "+email);
        logger.debug("password = "+password);
        logger.debug("recaptchaResponse = "+recaptchaResponse);
        //logger.debug("secretKey = "+secretKey);

        if(recaptchaResponse != null) {

            //boolean captchaValid = isCaptchaValid(secretKey, recaptchaResponse, remoteAddr);
            boolean captchaValid = isCaptchaValid(secretKey, recaptchaResponse);
            logger.debug("captchaValid = "+captchaValid);

            if(!captchaValid) {
                //request.setAttribute("usernameError", "Please Enter an Email Address");
                //request.setAttribute("passwordError", "Please Enter an Email Address");
                //request.setAttribute("errorMessage", "Recaptcha Failure");
                //request.setAttribute("errorMessage", getServletContext().getAttribute("preparedMessage").getMessage("RecaptchaFailure");
                //request.setAttribute("errorMessage", getServletContext().getAttribute("preparedMessages").getMessage("recaptcha.not.checked");
                request.setAttribute("errorMessage", "Check the box to verify you are not a robot");

                includeUtf8(request, response, this.loginPage);

                return;
            }
        }

        if(email == null || email.equals(EMPTY)) {

            request.setAttribute("usernameError", "Please Enter an Email Address");
            includeUtf8(request, response, this.loginPage);

            return;

        } else if(password == null || password.equals(EMPTY)) {

            request.setAttribute("passwordError", "Please Enter a Password");
            includeUtf8(request, response, this.loginPage);
            
            return;

        } else {
            // make sure the email is lowercase
            email = email.toLowerCase();
        }

        try {

            //String passwordHash = LoginDatabaseManager.selectPasswordHashByEmail(email);
            //String passwordHash = LoginDatabaseManager.selectPasswordHashByEmail(email, getServletContext().getAttribute("connectionPool"));
            String passwordHash = LoginDatabaseManager.selectPasswordHashByEmail(email, getConnectionPool());
            logger.debug("passwordHash = "+passwordHash);

            if(
                 passwordHash != null
              && passwordHash.equals(password)
            ) {

                HttpSession httpSession = request.getSession(true); // create the session
                logger.debug("httpSession.getId() = "+httpSession.getId());

                //User user = UserDatabaseManager.selectUserByEmail(email);
                User user = UserDatabaseManager.selectUserByEmail(email, getConnectionManager());
                //int userId = LoginDatabaseManager.selectUserIdByEmail(email);
                //int userId = LoginDatabaseManager.selectUserIdByEmail(email, getConnectionPool());

                logger.debug("user = "+user);

                if(user != null) {
                //if(userId != -1) {

                    int userId = user.getId();
                    logger.debug("userId = "+userId);

                    //logger.debug("user.getId() = "+user.getId());
                    //httpSession.setAttribute("userId", user.getId());
                    //request.setAttribute("user", user);
                    httpSession.setAttribute("userId", userId); // set the userId on the session

                    java.util.List<Integer> groupIdList = user.getGroupIdList();
                    if(groupIdList != null) {
                        //log.debug("httpSession.setAttribute('groups', "+groupIdList+")");
                        httpSession.setAttribute("groupIdList", groupIdList); // set the user groups on the session
                    }

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
                    //SessionDatabaseManager.insert(session);
                    //SessionDatabaseManager.insert(session, getConnectionPool());
                    SessionDatabaseManager.insert(session, getConnectionManager());

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

                    String redirect = request.getParameter("redirect");
                    logger.debug("redirect = "+redirect);
                    if(redirect != null) {
                        
                        //response.sendRedirect(redirect);
                        includeUtf8(request, response, redirect);

                    } else {
                        includeUtf8(request, response, this.loggedInPage);
                    }

                    return;
                    
                } // if(userId != null) {

            } else { // if(passwordHash != null

                //request.setAttribute("errorMessage", "Incorrect password");
                request.setAttribute("passwordError", "Incorrect password");
                //request.setAttribute("usernameError", "No account with that email address exists");
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
