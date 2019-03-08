package com.producersmarket.auth.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegisterServlet
  extends ParentServlet
{
    private static final Logger logger = LogManager.getLogger();
    private static final String EMPTY = "";
    private static final String SPACE = " ";
    private static final String COMMA = ",";
    private static final String LEFT_SQUARE_BRACKET = "[";
    private static final String RIGHT_SQUARE_BRACKET = "]";
    private String registerPage = null;
  
    public void init(ServletConfig config)
      throws ServletException
    {
        logger.debug("init(" + config + ")");
    
        this.registerPage = config.getInitParameter("registerPage");
    
        logger.debug("registerPage = " + this.registerPage);
    
        super.init(config);
    }
  
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException
    {
        logger.debug("doGet(request, response)");

        try
        {

            String username = null;
            String pathInfo = request.getPathInfo();
            if(pathInfo != null) username = pathInfo.substring(1, pathInfo.length());

            logger.debug("username = "+username);

            request.setAttribute("email", username);

            includeUtf8(request, response, this.registerPage);
      
            return;
        }
        catch (FileNotFoundException e)
        {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            logger.error(stringWriter.toString());
      
            response.sendError(404);
        }
    }
  
    public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException
    {
        logger.debug("doPost(request, response)");
    
        String email = request.getParameter("email");
        String password = request.getParameter("hash");
    
        logger.debug("email = " + email);
        logger.debug("password = " + password);

        if ((email == null) || (email.equals("")))
        {
          request.setAttribute("errorMessage", "Please Enter an Email Address");
          includeUtf8(request, response, this.registerPage);
          return;
        }

        email = email.toLowerCase();
    
        String header = "Now, confirm your email address";
        String message = "We sent a confirmation email to <br/>" + email;
        message = message + "<p>Check your email and click on the confirmation link to continue.</p>";
        request.setAttribute("header", header);
        request.setAttribute("message", message);
    
        include(request, response, "/view/confirmation-message.jsp");
    }
}
