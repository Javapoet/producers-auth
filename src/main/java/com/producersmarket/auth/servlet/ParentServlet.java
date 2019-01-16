package com.producersmarket.auth.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.ispaces.database.connection.ConnectionPool;

//public class ParentServlet extends InitServlet {
public class ParentServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger();
    public static final String UTF_8 = "UTF-8";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String TEXT_HTML_UTF8 = "text/html; charset=UTF-8";
    public static final String APPLICATION_XJAVASCRIPT = "application/x-javascript";
    public static final String APPLICATION_XJAVASCRIPT_UTF8 = "application/x-javascript;charset=utf-8";
    public static final String APPLICATION_JSON = "application/json";
    public static final String COMMA = ",";
    public static final String LEFT_SQUARE = "[";
    public static final String RIGHT_SQUARE = "]";
    public static final String DOUBLE_QUOTE = "\"";
    public static final String EMPTY = "";
    public static final String COLON = ":";

    //public static ServletContext servletContext;
    //public ServletContext servletContext;
    private ServletContext servletContext;
    public Properties properties = null;
    //public static java.util.concurrent.Executor executor = null;
    public java.util.concurrent.Executor executor = null;
	
    public String doubleQuotes(String string) {
        return new StringBuilder().append(DOUBLE_QUOTE).append(string).append(DOUBLE_QUOTE).toString();
    }

    public Properties getProperties() {
        //return super.properties;
        //return super.getProperties();
        return null;
    }

    public void init(ServletConfig config) throws ServletException {
        logger.debug("init("+config+")");

        logger.info("getClass().getName() = "+getClass().getName());

        this.servletContext = config.getServletContext();
        logger.debug("this.servletContext = " + this.servletContext);

        String contextInitialized = (String)this.servletContext.getAttribute("contextInitialized");
        logger.debug("contextInitialized = "+contextInitialized);

        if(contextInitialized == null) {

            try {

                java.io.InputStream inputStream = servletContext.getResourceAsStream("/WEB-INF/init.properties");
                logger.debug("inputStream = "+inputStream);

                //this.properties = (Properties)com.ispaces.util.PropertiesUtil.getProperties(inputStream);
                this.properties = new Properties();

                try {

                    this.properties.load(inputStream);

                    servletContext.setAttribute("properties", this.properties);

                } catch(java.io.IOException ioException) {
                    StringWriter stringWriter = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(stringWriter);
                    ioException.printStackTrace(printWriter);
                    logger.error(stringWriter.toString());
                }

                logger.debug(this.properties);

                String protocol    = properties.getProperty("protocol");
                String server      = properties.getProperty("server");
                String port        = properties.getProperty("port");
                String context     = properties.getProperty("context");

                logger.debug("protocol = "+protocol);
                logger.debug("server = "+server);
                logger.debug("port = "+port);
                logger.debug("context = "+context);

                servletContext.setAttribute("server", server);
                servletContext.setAttribute("port", port);

                /*
                 * Create the URL of the current context.
                 */
                StringBuilder stringBuilder = new StringBuilder();

                if(protocol != null) {
                    stringBuilder.append(protocol);
                } else {
                    stringBuilder.append("http");
                }
                stringBuilder.append("://").append(server);
                if(port != null && !port.equals(EMPTY)) stringBuilder.append(COLON).append(port);
                String serverUrl = stringBuilder.toString();  // Capture the serverUrl

                logger.debug("serverUrl = "+serverUrl);
                properties.setProperty("serverUrl", serverUrl);
                servletContext.setAttribute("serverUrl", serverUrl);

                stringBuilder.append("/");

                if(context != null && !context.equals(EMPTY)) stringBuilder.append(context);
                String contextUrl = stringBuilder.toString();
                logger.debug("contextUrl = "+contextUrl);
                properties.setProperty("contextUrl", contextUrl);
                servletContext.setAttribute("contextUrl", contextUrl);

                String resetPasswordEmailFrom = properties.getProperty("reset-password.email.from");
                logger.debug("resetPasswordEmailFrom = "+resetPasswordEmailFrom);
                servletContext.setAttribute("resetPasswordEmailFrom", resetPasswordEmailFrom);

                // Database Connection Pool Properties
                String jdbcDriver = properties.getProperty("database-driver");
                String databaseUrl = properties.getProperty("database-url");
                String databaseUsername = properties.getProperty("database-username");
                String databasePassword = properties.getProperty("database-password");

                logger.debug("jdbcDriver = "+jdbcDriver);
                logger.debug("databaseUrl = "+databaseUrl);
                logger.debug("databaseUsername = "+databaseUsername);
                logger.debug("databasePassword = "+databasePassword);

                properties.setProperty("databaseUrl", databaseUrl);
                servletContext.setAttribute("databaseUrl", databaseUrl);
                properties.setProperty("jdbcDriver", jdbcDriver);
                servletContext.setAttribute("jdbcDriver", jdbcDriver);
                properties.setProperty("databaseUsername", databaseUsername);
                servletContext.setAttribute("databaseUsername", databaseUsername);
                properties.setProperty("databasePassword", databasePassword);
                servletContext.setAttribute("databasePassword", databasePassword);

                java.util.Properties connectionPoolProperties = new java.util.Properties();
                connectionPoolProperties.setProperty("id", "-1");
                connectionPoolProperties.setProperty("url", databaseUrl);
                connectionPoolProperties.setProperty("driver", jdbcDriver);
                connectionPoolProperties.setProperty("username", databaseUsername);
                connectionPoolProperties.setProperty("password", databasePassword);
                connectionPoolProperties.setProperty("minConns", "2");
                connectionPoolProperties.setProperty("maxConns", "10");
                connectionPoolProperties.setProperty("maxAgeDays", "0.1");
                connectionPoolProperties.setProperty("maxIdleSeconds", "60");

                ConnectionPool connectionPool = new ConnectionPool(connectionPoolProperties);

                logger.debug("connectionPoolProperties = "+connectionPoolProperties);
                logger.debug("connectionPool = "+connectionPool);

                com.ispaces.database.connection.ConnectionManager.loadStatements(connectionPool);
                com.ispaces.database.manager.JavaClassManager.init(connectionPool);
                com.ispaces.database.manager.ConnectionPoolManager.initConnectionPoolMap(connectionPool);

                this.executor = new java.util.concurrent.ThreadPoolExecutor(
                    10
                  , 10
                  , 50000L
                  , java.util.concurrent.TimeUnit.MILLISECONDS
                  , new java.util.concurrent.LinkedBlockingQueue<Runnable>(100)
                );

            } catch(Exception exception) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                exception.printStackTrace(printWriter);
                logger.error(stringWriter.toString());
            }

            this.servletContext.setAttribute("contextInitialized", "true");

        } // if(contextInitialized != null) {

        super.init(config);
    }

    public void logException(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        logger.error(stringWriter.toString());
    }

    protected void include(HttpServletRequest request, HttpServletResponse response, String jspPath) throws IOException, ServletException {
        include(request, response, jspPath, TEXT_HTML_UTF8);
    }

    protected void include(HttpServletRequest request, HttpServletResponse response, String jspPath, String contentType) throws IOException, ServletException {
        logger.debug("include(request, response, '"+jspPath+"', '"+contentType+"')");
        //logger.debug("include("+request+", "+response+", '"+jspPath+"', '"+contentType+"')");

        response.setContentType(contentType);

        try {

            /*
             * http://stackoverflow.com/questions/27352672/how-to-use-the-org-json-java-library-with-java-7
             */
            //logger.debug("this.getServletConfig() = "+this.getServletConfig());
            //if(this.getServletConfig() != null) logger.debug("this.getServletConfig().getServletContext() = "+this.getServletConfig().getServletContext());
            //this.getServletConfig().getServletContext().getRequestDispatcher(jspPath).include(request, response);
            this.servletContext.getRequestDispatcher(jspPath).include(request, response);
            //request.getRequestDispatcher(jspPath).include(request, response);

        } catch(java.io.FileNotFoundException e) {

            logger.error(
              new StringBuilder()
                .append("FileNotFoundException: include(request, response, '")
                .append(jspPath)
                .append("', '")
                .append(contentType)
                .append("'): e.getMessage() = ")
                .append(e.getMessage())
                .toString()
            );
        }
    }

    protected void includeUtf8(HttpServletRequest request, HttpServletResponse response, String path) throws IOException, ServletException {
        logger.debug("includeUtf8(request, response, '"+path+"')");

        includeUtf8(
          request
          , response
          , path
          , TEXT_HTML_UTF8
        );
    }

    /**
     * @param contentType e.g. "text/html;charset=UTF-8";
     */
    protected void includeUtf8(HttpServletRequest request, HttpServletResponse response, String path, String contentType) throws IOException, ServletException {
        logger.debug("includeUtf8(request, response, '"+path+"', '"+contentType+"')");

        response.setContentType(contentType);
        response.setCharacterEncoding(UTF_8);

        // logging
        //StringBuilder sb = new StringBuilder();
        //sb.append(RESPONSE_COLON_SPACE);
        //sb.append(path);

        //HttpSession session = request.getSession(false);
        //logger.debug("session = "+session);

        //if(session != null) {
        //    sb.append(" - ").append(LEFT_PAREN);
        //    sb.append(SPACE).append(session.getId());
        //    //sb.append(COLON).append(user.getCookieId());
        //    sb.append(SPACE).append(session.getAttribute(SESSION_HASH));
        //    sb.append(RIGHT_PAREN);
        //}

        //logger.info(sb.toString());

        try {

            //logger.debug("context = "+context);
            //logger.debug("servletContext = "+servletContext);
            //logger.debug("this.getServletConfig() = "+this.getServletConfig());
            //logger.debug("this.getServletConfig().getServletContext() = "+this.getServletConfig().getServletContext());

            //this.getServletConfig().getServletContext().getRequestDispatcher(path).include(request, response);
            //context.getRequestDispatcher(path).include(request, response);

            this.servletContext.getRequestDispatcher(path).include(request, response);

            try {
                //response.getOutputStream().flush(); // yes/no/why?
                response.getOutputStream().close(); // yes/no/why?
            } catch(Exception e){
                logger.error(e);
            }

        } catch(java.io.FileNotFoundException e) {

            logger.error("FileNotFoundException: include(request, response, '"+path+"', '"+contentType+"'): e.getMessage() = "+e.getMessage());
            logger.error(e);

        }

    }

    protected void writeOut(HttpServletResponse response, int responseInt) throws IOException {
        writeOut(response, String.valueOf(responseInt), TEXT_PLAIN);
    }

    protected void writeOut(HttpServletResponse response, String text) throws IOException {
        writeOut(response, text, TEXT_PLAIN);
    }

    protected void writeOut(HttpServletResponse response, String text, String contentType) throws IOException {
        logger.debug("writeOut(request, '"+text+"', '"+contentType+"')");

        response.setHeader("Content-Length", String.valueOf(text.length()));  // Set the "Content-Length" header.

        PrintWriter out = response.getWriter();
        response.setContentType(contentType);
        out.write(text);
        out.flush();
        out.close();
    }

    public int getUserId(HttpServletRequest request) { // throws IOException, ServletException {
        logger.debug("getUserId(request)");

        HttpSession session = request.getSession(false);
        logger.debug("session = "+session);

        if(session != null) {

            Integer userIdInteger = (Integer)session.getAttribute("userId");

            if(userIdInteger != null) return userIdInteger.intValue();

        }
        
        return -1;
    }

}
