
package com.producersmarket.database;

import java.io.PrintWriter;
import java.io.StringWriter;
/*
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
*/
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.ispaces.database.connection.ConnectionManager;

import com.producersmarket.model.Session;

public class SessionDatabaseManager {

    private static final Logger logger = LogManager.getLogger();
    private static final String className = SessionDatabaseManager.class.getSimpleName();

    public static void insert(Session session) throws SQLException, Exception {
        logger.debug("updateUserLoggedIn("+session+")");

        ConnectionManager connectionManager = new ConnectionManager(className);

        try {

            /*
            String[] fields = new String[] {
                "user_id"
              , "session_id"
              , "user_agent"
              , "remote_addr"
              , "host"
              , "protocol"
              , "locale"
              , "character_encoding"
              , "accept"
              , "accept_encoding"
              , "accept_language"
              , "accept_charset"
              , "referer"
              , "server_name"
              , "server_port"
              , "server_info"
              , "updated_by"
              , "created_by"
              , "date_created"
            };
            */

            String fields = "user_id, session_id, user_agent, remote_addr, host, protocol, locale, character_encoding, accept, accept_encoding, accept_language, accept_charset, referer, server_name, server_port, server_info, updated_by, created_by, date_created";
            String values = "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW()";

            String sql = new StringBuilder()
              .append("INSERT INTO session (")
              .append(fields)
              .append(") VALUES (")
              .append(values)
              .append(")")
              .toString();

            PreparedStatement preparedStatement = connectionManager.prepareStatement(sql);
            //PreparedStatement preparedStatement = connectionManager.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

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
