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

public class ResetPasswordDatabaseManager {

    private static final String className = ResetPasswordDatabaseManager.class.getSimpleName();
    private static final Logger logger = LogManager.getLogger();

    private static final String insertActivationCode = "insertActivationCode";
    private static final String deleteActivationCode = "deleteActivationCode";
    private static final String updateActivationCode = "updateActivationCode";
    private static final String INSERT_FORGOT_PASSWORD = "insertForgotPassword";
    private static final String DELETE_FORGOT_PASSWORD = "deleteForgotPassword";
    private static final String UPDATE_PASSWORD_HASH = "updatePasswordHash";
    private static final String selectUserIdByPasswordResetCode = "selectUserIdByPasswordResetCode";

    public static void updatePassword(int userId, String password, Object connectionPoolObject) throws SQLException, Exception {
        logger.debug("updatePassword("+userId+", password, "+connectionPoolObject+")");

        updatePassword(userId, password, (ConnectionPool) connectionPoolObject);
    }

    public static void updatePassword(int userId, String password, ConnectionPool connectionPool) throws SQLException, Exception {
        logger.debug("updatePassword("+userId+", password, "+connectionPool+")");

        ConnectionManager connectionManager = new ConnectionManager(connectionPool);

        updatePassword(userId, password, connectionManager);
    }

    public static void updatePassword(int userId, String password, ConnectionManager connectionManager) throws SQLException, Exception {
        logger.debug("updatePassword("+userId+", password, connectionManager)");

        try {

            PreparedStatement stmt = connectionManager.loadStatement(UPDATE_PASSWORD_HASH);
            stmt.setString(1, password);
            stmt.setInt(2, userId);
            stmt.executeUpdate();

        } finally {
            connectionManager.commit();
        }

    }

    public static void updatePassword(int userId, String password) throws SQLException, Exception {
        logger.debug("updatePassword("+userId+", '"+password+"')");

        ConnectionManager connectionManager = new ConnectionManager(className, UPDATE_PASSWORD_HASH);

        try {

            PreparedStatement stmt = connectionManager.loadStatement(UPDATE_PASSWORD_HASH);
            stmt.setString(1, password);
            stmt.setInt(2, userId);
            stmt.executeUpdate();

        } finally {
            connectionManager.commit();
        }

    }

    public static int selectUserIdByPasswordResetCode(String code) throws SQLException, Exception {
        logger.debug("selectUserIdByPasswordResetCode("+code+")");

        ConnectionManager connectionManager = new ConnectionManager(className, selectUserIdByPasswordResetCode);

        try {

            //PreparedStatement preparedStatement = connectionManager.loadStatement(selectUserIdByPasswordResetCode);
            //String sql = "SELECT id FROM user WHERE activationcode = ?";
            //PreparedStatement preparedStatement = connectionManager.prepareStatement(sql);
            PreparedStatement preparedStatement = connectionManager.loadStatement("selectUserIdByPasswordResetCode");
            preparedStatement.setString(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {

                return resultSet.getInt(1);
            }

        } finally {
            connectionManager.commit();
        }

        return -1;
    }

    public static int selectUserIdByPasswordResetCode(String code, Object connectionPoolObject) throws SQLException, Exception {
        logger.debug("selectUserIdByPasswordResetCode("+code+", "+connectionPoolObject+")");

        return selectUserIdByPasswordResetCode(code, (ConnectionPool) connectionPoolObject);
    }

    public static int selectUserIdByPasswordResetCode(String code, ConnectionPool connectionPool) throws SQLException, Exception {
        logger.debug("selectUserIdByPasswordResetCode("+code+", connectionPool)");

        return selectUserIdByPasswordResetCode(code, new ConnectionManager(connectionPool));
    }

    public static int selectUserIdByPasswordResetCode(String code, ConnectionManager connectionManager) throws SQLException, Exception {
        logger.debug("selectUserIdByPasswordResetCode("+code+", connectionManager)");

        try {

            PreparedStatement preparedStatement = connectionManager.loadStatement("selectUserIdByPasswordResetCode");
            preparedStatement.setString(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {

                return resultSet.getInt(1);
            }

        } finally {
            connectionManager.commit();
        }

        return -1;
    }

    public static void insertActivationCode(int userId, String code) throws SQLException, Exception {
        logger.debug("insertActivationCode(userId:"+userId+", '"+code+"')");

        ConnectionManager connectionManager = new ConnectionManager(className, insertActivationCode);

        try {

            //String sql = "UPDATE user SET activationcode = ? WHERE id=?";
            //PreparedStatement preparedStatement = connectionManager.prepareStatement(sql);
            PreparedStatement preparedStatement = connectionManager.loadStatement("insertActivationCode");
            preparedStatement.setString(1, code);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();

        } catch(Exception e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            logger.error(stringWriter.toString());
        } finally {
            connectionManager.commit();
        }

    }

    public static void insertActivationCode(String email, String code, Object connectionPoolObject) throws SQLException, Exception {
        logger.debug("insertActivationCode("+email+", "+code+", connectionPoolObject)");

        insertActivationCode(email, code, (ConnectionPool) connectionPoolObject);
    }

    public static void insertActivationCode(String email, String code, ConnectionPool connectionPool) throws SQLException, Exception {
        logger.debug("insertActivationCode("+email+", "+code+", connectionPool)");

        insertActivationCode(email, code, new ConnectionManager(connectionPool));
    }

    public static void insertActivationCode(String email, String code, ConnectionManager connectionManager) throws SQLException, Exception {
        logger.debug("insertActivationCode("+email+", "+code+", connectionManager)");

        try {

            //String sql = "UPDATE user SET activation_code=? WHERE email=?";
            //PreparedStatement preparedStatement = connectionManager.prepareStatement(sql);
            PreparedStatement preparedStatement = connectionManager.loadStatement("insertActivationCodeByEmail");
            preparedStatement.setString(1, code);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();

        } catch(Exception e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            logger.error(stringWriter.toString());
        } finally {
            connectionManager.commit();
        }

    }

    public static void insertActivationCode(String email, String code) throws SQLException, Exception {
        logger.debug("insertActivationCode("+email+", '"+code+"')");

        ConnectionManager connectionManager = new ConnectionManager(className, insertActivationCode);

        try {

            //String sql = "UPDATE user SET activation_code=? WHERE email=?";
            //PreparedStatement preparedStatement = connectionManager.prepareStatement(sql);
            PreparedStatement preparedStatement = connectionManager.loadStatement("insertActivationCodeByEmail");
            preparedStatement.setString(1, code);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();

        } catch(Exception e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            logger.error(stringWriter.toString());
        } finally {
            connectionManager.commit();
        }

    }

    public static void deleteActivationCode(int userId, Object connectionPoolObject) throws SQLException, Exception {
        logger.debug("deleteActivationCode("+userId+", connectionPoolObject)");

        deleteActivationCode(userId, (ConnectionPool) connectionPoolObject);
    }

    public static void deleteActivationCode(int userId, ConnectionPool connectionPool) throws SQLException, Exception {
        logger.debug("deleteActivationCode("+userId+", connectionPool)");

        deleteActivationCode(userId, new ConnectionManager(connectionPool));
    }

    public static void deleteActivationCode(int userId, ConnectionManager connectionManager) throws SQLException, Exception {
        logger.debug("deleteActivationCode("+userId+", connectionManager)");

        try {

            //String sql = "UPDATE user SET activation_code = ? WHERE id = ?";
            //PreparedStatement preparedStatement = connectionManager.prepareStatement(sql);
            PreparedStatement preparedStatement = connectionManager.loadStatement("deleteActivationCode");
            preparedStatement.setString(1, null);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();

        } catch(Exception e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            logger.error(stringWriter.toString());
        } finally {
            connectionManager.commit();
        }

    }

    //public static void deleteActivationCode(int userId, String code) throws SQLException, Exception {
    public static void deleteActivationCode(int userId) throws SQLException, Exception {
        logger.debug("deleteActivationCode("+userId+")");

        ConnectionManager connectionManager = new ConnectionManager(className, deleteActivationCode);

        try {

            //String sql = "UPDATE user SET activation_code = ? WHERE id = ?";
            //PreparedStatement preparedStatement = connectionManager.prepareStatement(sql);
            PreparedStatement preparedStatement = connectionManager.loadStatement("deleteActivationCode");
            preparedStatement.setString(1, null);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();

        } catch(Exception e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            logger.error(stringWriter.toString());
        } finally {
            connectionManager.commit();
        }

    }
    public static void deleteActivationCode(String email, String code) throws SQLException, Exception {
        logger.debug("deleteActivationCode("+email+", '"+code+"')");

        ConnectionManager connectionManager = new ConnectionManager(className, deleteActivationCode);

        try {

            //String sql = "UPDATE user SET activation_code=? WHERE email=?";
            //PreparedStatement preparedStatement = connectionManager.prepareStatement(sql);
            PreparedStatement preparedStatement = connectionManager.loadStatement("deleteActivationCode");
            preparedStatement.setString(1, null);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();

        } catch(Exception e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            logger.error(stringWriter.toString());
        } finally {
            connectionManager.commit();
        }

    }

    /*
     * INSERT INTO forgot_password (password_reset_code, user_id) VALUES (?,?);
     * INSERT INTO forgot_password (password_reset_code, user_id) VALUES ('test',8);
     */
    public static void insert(int userId, String code) throws SQLException, Exception {
        logger.debug("insert("+userId+", '"+code+"')");

        ConnectionManager connectionManager = new ConnectionManager(className);

        try {

            PreparedStatement stmt = connectionManager.loadStatement(INSERT_FORGOT_PASSWORD);

            stmt.setString(1, code);
            stmt.setInt(2, userId);
            stmt.executeUpdate();

        } finally {
            connectionManager.commit();
        }

    }

    public static void delete(String code) throws SQLException, Exception {
        logger.debug("delete('"+code+"')");

        ConnectionManager connectionManager = new ConnectionManager(className);

        try {

            PreparedStatement stmt = connectionManager.loadStatement(DELETE_FORGOT_PASSWORD);

            stmt.setString(1, code);
            stmt.executeUpdate();

        } finally {
            connectionManager.commit();
        }

    }

}
