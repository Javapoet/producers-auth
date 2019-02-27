package com.producersmarket.auth.database;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.ispaces.dbcp.ConnectionManager;
import com.ispaces.dbcp.ConnectionPool;

import com.producersmarket.auth.model.Session;

public class SessionDatabaseManager {

    private static final Logger logger = LogManager.getLogger();
    private static final String className = SessionDatabaseManager.class.getSimpleName();

    /*
    public static void insert(Session session, Object connectionPoolObject) throws SQLException, Exception {
        logger.debug("insert("+session+", Object: "+connectionPoolObject+")");

        insert(session, (ConnectionPool) connectionPoolObject);
    }

    public static void insert(Session session, ConnectionPool connectionPool) throws SQLException, Exception {
        logger.debug("insert("+session+", ConnectionPool: "+connectionPool+")");

        //ConnectionManager connectionManager = new ConnectionManager(className, com.producersmarket.servlet.InitServlet.connectionPool);
        //ConnectionManager connectionManager = new ConnectionManager();
        //ConnectionManager connectionManager = new ConnectionManager(className);
        ConnectionManager connectionManager = new ConnectionManager(connectionPool);

        insert(session, connectionManager);
    }
    */

    public static void insert(Session session, ConnectionManager connectionManager) throws SQLException, Exception {
        logger.debug("insert("+session+", "+connectionManager+")");

        String statementName = "insertSession";

        try {

            PreparedStatement preparedStatement = connectionManager.loadStatement(statementName);

            preparedStatement.setInt(1, session.getUserId());
            preparedStatement.setString(2, session.getSessionId());
            preparedStatement.setString(3, session.getUserAgent());
            preparedStatement.setString(4, session.getRemoteAddr());
            preparedStatement.setString(5, session.getHost());
            preparedStatement.setString(6, session.getProtocol());
            preparedStatement.setString(7, session.getLocale());
            preparedStatement.setString(8, session.getCharacterEncoding());
            preparedStatement.setString(9, session.getAccept());
            preparedStatement.setString(10, session.getAcceptEncoding());
            preparedStatement.setString(11, session.getAcceptLanguage());
            preparedStatement.setString(12, session.getAcceptCharset());
            preparedStatement.setString(13, session.getReferer());
            preparedStatement.setString(14, session.getServerName());
            preparedStatement.setInt(15, session.getServerPort());
            preparedStatement.setString(16, session.getServerInfo());
            preparedStatement.setInt(17, session.getUserId());
            preparedStatement.setInt(18, session.getUserId());

            preparedStatement.executeUpdate();

            //ResultSet rs = preparedStatement.getGeneratedKeys();
            //if(rs.next()) return rs.getInt(1);

        } catch(Exception e) {

            logger.error(new StringBuilder().append(e.getMessage()).append(" ").append(statementName));

            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            logger.error(stringWriter.toString());

        } finally {

            try {
                connectionManager.commit();
            } catch(Exception exception) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                exception.printStackTrace(printWriter);
                logger.error(stringWriter.toString());
            }

        }
    }

    public static void insert(Session session) throws SQLException, Exception {
        logger.debug("insert("+session+")");

        ConnectionManager connectionManager = new ConnectionManager(className);
        //ConnectionManager connectionManager = new ConnectionManager(getClass().getSimpleName());

        String statementName = "insertSession";

        try {

            PreparedStatement preparedStatement = connectionManager.loadStatement(statementName);

            preparedStatement.setInt(1, session.getUserId());
            preparedStatement.setString(2, session.getSessionId());
            preparedStatement.setString(3, session.getUserAgent());
            preparedStatement.setString(4, session.getRemoteAddr());
            preparedStatement.setString(5, session.getHost());
            preparedStatement.setString(6, session.getProtocol());
            preparedStatement.setString(7, session.getLocale());
            preparedStatement.setString(8, session.getCharacterEncoding());
            preparedStatement.setString(9, session.getAccept());
            preparedStatement.setString(10, session.getAcceptEncoding());
            preparedStatement.setString(11, session.getAcceptLanguage());
            preparedStatement.setString(12, session.getAcceptCharset());
            preparedStatement.setString(13, session.getReferer());
            preparedStatement.setString(14, session.getServerName());
            preparedStatement.setInt(15, session.getServerPort());
            preparedStatement.setString(16, session.getServerInfo());
            preparedStatement.setInt(17, session.getUserId());
            preparedStatement.setInt(18, session.getUserId());

            preparedStatement.executeUpdate();

            //ResultSet rs = preparedStatement.getGeneratedKeys();
            //if(rs.next()) return rs.getInt(1);

        } catch(Exception e) {

            logger.error(new StringBuilder().append(e.getMessage()).append(" ").append(statementName));

            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            logger.error(stringWriter.toString());

        } finally {

            try {
                connectionManager.commit();
            } catch(Exception exception) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                exception.printStackTrace(printWriter);
                logger.error(stringWriter.toString());
            }

        }
    }

}
