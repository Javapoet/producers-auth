package com.producersmarket.auth.mailer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import javax.mail.MessagingException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

//import com.ispaces.dbcp.ConnectionPool;
import com.ispaces.dbcp.ConnectionManager;

/*
import com.ispaces.mail.model.MailClient;
import com.ispaces.mail.model.MailMessage;
*/
import com.ispaces.mail.model.PreparedEmail;
import com.ispaces.mail.model.PreparedEmails;
import com.ispaces.mail.util.HttpUtil;
//import com.ispaces.util.SecurityUtil;

import com.javapoets.mail.client.SmtpClient;
import com.javapoets.mail.server.smtp.SmtpMessage;
//import com.javapoets.mail.model.SmtpMessage; // @Future
//import com.javapoets.mail.model.MailMessage;

import com.producersmarket.auth.util.SecurityUtil;
import com.producersmarket.auth.util.UniqueId;

public class RegisterMailer implements Runnable {

    private static final Logger logger = LogManager.getLogger();

    //private final String SMTP_SERVER = "smtp.server";
    private final String SMTP_SERVER = "smtpServer";
    //private final String SMTP_PORT = "smtp.port";
    private final String SMTP_PORT = "smtpPort";
    private final String SMTP_USER = "smtpUser";
    private final String SMTP_PASS = "smtpPass";
    private final String EMAIL_TO = "emailTo";
    private final String EMAIL_FROM = "emailFrom";
    //private final String EMAIL_ADDRESS_FROM_SUPPORT = "email.address.from.support";
    private final String EMAIL_ADDRESS_FROM_SUPPORT = "emailAddressSupport";
    private final String CONTEXT_URL = "contextUrl";
    private final String PREPARED_EMAIL_PATH = "view/email/confirm-your-email.jsp"; // default

    private String preparedEmailJspPath = null;
    private String smtpServer = null;
    private String smtpPort = null;
    private String smtpUser = null;
    private String smtpPass = null;
    private String toAddress = null;
    private String fromAddress = null;
    private String subject = null;
    private String body = null;
    private String bodyHtml = null;
    private String webAddress = null;
    private String contextUrl = null;
    private Properties properties = null;
    private PreparedEmail preparedEmail = null;
    //private MailClient mailClient;
    private SmtpClient mailClient;
    private String accountActivationLink = null;

    public RegisterMailer(String emailAddress) {
        logger.debug("("+emailAddress+")");

        //this.smtpServer = InitServlet.init.getProperty(SMTP_SERVER);
        //this.smtpPort   = InitServlet.init.getProperty(SMTP_PORT);
        //this.fromAddress       = InitServlet.init.getProperty(EMAIL_ADDRESS_FROM_SUPPORT);
        //this.user       = user;
        this.toAddress = emailAddress;

        //String fromAddress = InitServlet.init.getProperty(EMAIL_ADDRESS_FROM_SUPPORT);
        //if(fromAddress == null) fromAddress = "support@ispaces.com";
        //this.fromAddress = fromAddress;

        logger.debug("this.smtpServer = " + this.smtpServer);
        logger.debug("this.smtpPort = " + this.smtpPort);
        //logger.debug("this.preparedEmailJspPath = " + this.preparedEmailJspPath);
        logger.debug("this.toAddress = " + this.toAddress);
        logger.debug("fromAddress = " + fromAddress);
    }

    public RegisterMailer(Properties properties) {
        logger.debug("("+properties+")");

        this.properties = properties;

        this.smtpServer  = properties.getProperty(SMTP_SERVER);
        this.smtpPort    = properties.getProperty(SMTP_PORT);
        this.smtpUser    = properties.getProperty(SMTP_USER);
        this.smtpPass    = properties.getProperty(SMTP_PASS);
        this.fromAddress = properties.getProperty(EMAIL_FROM);
        this.toAddress   = properties.getProperty(EMAIL_TO);
        this.contextUrl  = properties.getProperty(CONTEXT_URL);
        this.accountActivationLink = properties.getProperty("accountActivationLink");
        this.preparedEmailJspPath = properties.getProperty("preparedEmailJspPath");
        if(this.preparedEmailJspPath == null) this.preparedEmailJspPath = PREPARED_EMAIL_PATH;

        String name = properties.getProperty("name");
        String subject = properties.getProperty("subject");

        logger.debug("this.smtpServer = " + this.smtpServer);
        logger.debug("this.smtpPort = " + this.smtpPort);
        logger.debug("this.preparedEmailJspPath = " + this.preparedEmailJspPath);
        logger.debug("this.toAddress = " + this.toAddress);
        logger.debug("fromAddress = " + fromAddress);
    }

    public RegisterMailer(Properties properties, PreparedEmail preparedEmail) {
        logger.debug("("+properties+", preparedEmail)");

        this.properties = properties;
        this.preparedEmail = preparedEmail;

        this.smtpServer  = properties.getProperty(SMTP_SERVER);
        this.smtpPort    = properties.getProperty(SMTP_PORT);
        this.smtpUser    = properties.getProperty(SMTP_USER);
        this.smtpPass    = properties.getProperty(SMTP_PASS);
        this.fromAddress = properties.getProperty(EMAIL_FROM);
        this.toAddress   = properties.getProperty(EMAIL_TO);
        this.contextUrl  = properties.getProperty(CONTEXT_URL);
        this.accountActivationLink = properties.getProperty("accountActivationLink");
        this.preparedEmailJspPath = properties.getProperty("preparedEmailJspPath");

        String name = properties.getProperty("name");
        String subject = properties.getProperty("subject");

        logger.debug("this.smtpServer = " + this.smtpServer);
        logger.debug("this.smtpPort = " + this.smtpPort);
        logger.debug("this.preparedEmailJspPath = " + this.preparedEmailJspPath);
        logger.debug("this.toAddress = " + this.toAddress);
        logger.debug("fromAddress = " + fromAddress);
    }

    public void sendEmail() {
        logger.debug("sendEmail()");

        try {

            Hashtable<String, String> emailParams  = new Hashtable<String, String>();
            emailParams.put("LINK", this.accountActivationLink);

            this.subject = this.preparedEmail.getSubject();
            this.body = this.preparedEmail.getBody();

            logger.debug("this.subject = "+this.subject);
            logger.debug("this.body = "+this.body);

            String bodyHtmlTemplate = HttpUtil.getRequestString(new StringBuilder()
                .append(contextUrl)
                //.append("view/email/confirm-your-email.jsp")
                .append(this.preparedEmailJspPath)
                .toString());   

            logger.debug("bodyHtmlTemplate.length() = "+bodyHtmlTemplate.length());
            preparedEmail.setBody(bodyHtmlTemplate);

            this.bodyHtml = preparedEmail.generateBody(emailParams);
            logger.debug("this.bodyHtml.length() = "+this.bodyHtml.length());

            // Create the MailMessage object
            //MailMessage mailMessage = new MailMessage();
            SmtpMessage mailMessage = new SmtpMessage();
            //MailMessage mailMessage = new SmtpMessage();
            logger.debug("mailMessage = "+mailMessage);

            //try {

                mailMessage.setFrom(this.fromAddress);

            /*
            } catch(javax.mail.internet.AddressException e) {

                logger.error(e.getMessage());
                
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                e.printStackTrace(printWriter);
                logger.error(stringWriter.toString());

                throw e;
            }
            */

            //try {

                mailMessage.setTo(this.toAddress);

            /*
            } catch(javax.mail.internet.AddressException e) {

                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                e.printStackTrace(printWriter);
                logger.error(stringWriter.toString());

                throw e;
            }
            */

            //mailMessage.setContentType("text/plain");
            mailMessage.setContentType("text/html");
            //mailMessage.setContentType("multipart/alternative");
            mailMessage.setSubject(this.subject);
            //mailMessage.setBody(this.body);
            mailMessage.setBody(this.bodyHtml);


            /*
            MailMessage htmlBodyPart = new MailMessage();
            htmlBodyPart.setBody(this.bodyHtml);
            htmlBodyPart.setContentType("text/html");

            mailMessage.addBodyPart(htmlBodyPart);
            */


            /*
            MailMessage mb1 = new MailMessage();
            //SmtpMessage mb1 = new SmtpMessage();

            try {
                //mb1.setBody(baos.toByteArray());
                logger.debug("this.body.length() = "+this.body.length());
                mb1.setBody(this.body);
                mb1.setContentType("text/plain");
                mailMessage.addBodyPart(mb1);
            }catch(Exception e){
                e.printStackTrace(System.err);
            }

            MailMessage mb2 = new MailMessage();
            //SmtpMessage mb2 = new SmtpMessage();

            try {
                //mb2.setBody(baos.toByteArray());
                
                logger.debug("this.bodyHtml.length() = "+this.bodyHtml.length());

                mb2.setBody(this.bodyHtml);
                mb2.setContentType("text/html");
                mailMessage.addBodyPart(mb2);

            } catch(Exception e) {
                e.printStackTrace(System.err);
            }
            */

            //mailMessage.setBcc("dermot@producersmarket.com");

            //mailClient = new MailClient();
            mailClient = new SmtpClient(mailMessage);
            mailClient.setSmtpServer(this.smtpServer);
            mailClient.setSmtpPort(this.smtpPort);
            mailClient.setSmtpUsername(this.smtpUser);
            mailClient.setSmtpPassword(this.smtpPass);

            try {

                //mailClient.sendMessage(mailMessage);
                mailClient.send();

            } catch(MessagingException me) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                me.printStackTrace(printWriter);
                logger.debug(stringWriter.toString());
            }


        } catch(Exception e) {
            
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            logger.debug(stringWriter.toString());

        }

    }

    /**
     * Used to send a message
     * Messages is generated from 'messages.properties'
     */
    public static void send(String emailAddress) throws MessagingException {
        logger.debug("send("+emailAddress+")");

        RegisterMailer m = new RegisterMailer(emailAddress);
        m.sendEmail();

    }

    /**
     * Used to send a message
     * Messages is generated from 'messages.properties'
     */
    public static void send(String emailAddress, boolean differentThread) throws MessagingException {
        logger.debug("send("+emailAddress+", differentThread:"+(differentThread)+")");

        RegisterMailer registerMailer = new RegisterMailer(emailAddress);

        if(differentThread) {
            new Thread(registerMailer).start();
        } else {
            registerMailer.sendEmail();
        }

    }

    public static void send(Properties properties, PreparedEmail preparedEmail) throws MessagingException {
        logger.debug("send("+properties+", preparedEmail)");

        RegisterMailer registerMailer = new RegisterMailer(properties, preparedEmail);

        registerMailer.sendEmail();
    }

    public static void send(Properties properties, boolean differentThread) {
        logger.debug("send("+properties+", differentThread:"+(differentThread)+")");

        RegisterMailer registerMailer = new RegisterMailer(properties);

        if(differentThread) {
            new Thread(registerMailer).start();
        } else {
            registerMailer.sendEmail();
        }

    }

    public void run() {
        logger.debug("run()");

        //try {
            sendEmail();
        /*
        } catch(MessagingException me) {
            me.printStackTrace(System.err);
        }
        */
    }

}
