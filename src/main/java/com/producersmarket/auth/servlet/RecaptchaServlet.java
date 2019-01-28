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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONObject;

public class RecaptchaServlet extends ParentServlet {

    private static final Logger logger = LogManager.getLogger();
    private static final String EMPTY = "";
    private static final String SPACE = " ";
    private static final String COMMA = ",";
    private static final String LEFT_SQUARE_BRACKET = "[";
    private static final String RIGHT_SQUARE_BRACKET = "]";

    private String registerPage = null;

    /*
     * Secret key
     * Use this for communication between your site and Google. Be sure to keep it a secret.
     */
    private String siteKey = "6Ld5B40UAAAAAJ6MEjJiYZQiTlCuBvJSduqcnfzO";
    private String secretKey = "6Ld5B40UAAAAAL65r3R16dgVU467wUHZmEPFDN_I";

    /**
     * Validates Google reCAPTCHA V2 or Invisible reCAPTCHA.
     * @param secretKey Secret key (key given for communication between your site and Google)
     * @param response reCAPTCHA response from client side. (g-recaptcha-response)
     * @return true if validation successful, false otherwise.
     */
    public boolean isCaptchaValid(String secretKey, String response, String remoteAddr) {

        try {

            String url = new StringBuilder()
                .append("https://www.google.com/recaptcha/api/siteverify")
                .append("?secret=").append(secretKey)
                .append("&remoteip=").append(remoteAddr)
                .append("&response=").append(response)
                .toString();

            InputStream inputStream = new URL(url).openStream();
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

            StringBuilder stringBuilder = new StringBuilder();
            int codePoint;
            while((codePoint = bufferedReader.read()) != -1) {
                stringBuilder.append((char) codePoint);
            }

            String jsonText = stringBuilder.toString();
            inputStream.close();

            JSONObject jsonObject = new JSONObject(jsonText);
            
            return jsonObject.getBoolean("success");

        } catch (Exception e) {
            return false;
        }
    }
  
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
    
        String remoteAddr = request.getRemoteAddr();
        //String secretKey = request.getParameter("email");
        String recaptchaResponse = request.getParameter("g-recaptcha-response");
    
        logger.debug("secretKey = " + secretKey);
        logger.debug("recaptchaResponse = "+recaptchaResponse);
        logger.debug("remoteAddr = "+remoteAddr);

        boolean captchaValid = isCaptchaValid(secretKey, recaptchaResponse, remoteAddr);
        logger.debug("captchaValid = "+captchaValid);

        //include(request, response, "/view/confirmation-message.jsp");

    }
}
