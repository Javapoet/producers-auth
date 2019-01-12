package com.producersmarket.auth.database;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.ispaces.database.connection.ConnectionManager;

public class RegisterDatabaseManager {

    private static final String className = RegisterDatabaseManager.class.getSimpleName();
    private static final Logger logger = LogManager.getLogger();

    private static final String insertActivationCode = "insertActivationCode";
    private static final String deleteActivationCode = "deleteActivationCode";
    private static final String updateActivationCode = "updateActivationCode";
    private static final String INSERT_FORGOT_PASSWORD = "insertForgotPassword";
    private static final String DELETE_FORGOT_PASSWORD = "deleteForgotPassword";
    private static final String UPDATE_PASSWORD_HASH = "updatePasswordHash";
    private static final String selectUserIdByPasswordResetCode = "selectUserIdByPasswordResetCode";

    public static void updatePassword(int userId, String password) throws SQLException, Exception {
        logger.debug("updatePassword("+userId+", '"+password+"')");

        ConnectionManager connMgr = new ConnectionManager(className, UPDATE_PASSWORD_HASH);

        try {

            PreparedStatement stmt = connMgr.loadStatement(UPDATE_PASSWORD_HASH);
            stmt.setString(1, password);
            stmt.setInt(2, userId);
            stmt.executeUpdate();

        } finally {
            connMgr.commit();
        }

    }

    public static int selectUserIdByPasswordResetCode(String code) throws SQLException, Exception {
        logger.debug("selectUserByPasswordResetCode("+code+")");

        ConnectionManager connMgr = new ConnectionManager(className, selectUserIdByPasswordResetCode);

        try {

            //PreparedStatement preparedStatement = connMgr.loadStatement(selectUserIdByPasswordResetCode);
            String sql = "SELECT id FROM user WHERE activation_code = ?";
            PreparedStatement preparedStatement = connMgr.prepareStatement(sql);
            preparedStatement.setString(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {

                return resultSet.getInt(1);
            }

        } finally {
            connMgr.commit();
        }

        return -1;
    }

    public static void insertActivationCode(int userId, String code) throws SQLException, Exception {
        logger.debug("insertActivationCode(userId:"+userId+", '"+code+"')");

        ConnectionManager connectionManager = new ConnectionManager(className, insertActivationCode);

        try {

            String sql = "UPDATE user SET activation_code=? WHERE id=?";
            PreparedStatement preparedStatement = connectionManager.prepareStatement(sql);
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

    //public static void deleteActivationCode(int userId, String code) throws SQLException, Exception {
    public static void deleteActivationCode(int userId) throws SQLException, Exception {
        logger.debug("deleteActivationCode("+userId+")");

        ConnectionManager connectionManager = new ConnectionManager(className, deleteActivationCode);

        try {

            String sql = "UPDATE user SET activation_code = ? WHERE id = ?";
            PreparedStatement preparedStatement = connectionManager.prepareStatement(sql);
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

    public static void updateName(int userId, String name) throws SQLException, Exception {
        logger.debug("deleteActivationCode("+userId+", "+name+")");

        ConnectionManager connectionManager = new ConnectionManager(className, "updateName");

        try {

            String sql = "UPDATE user SET name = ? WHERE id = ?";
            PreparedStatement preparedStatement = connectionManager.prepareStatement(sql);
            preparedStatement.setString(1, name);
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

    public static void insertActivationCode(String email, String code) throws SQLException, Exception {
        logger.debug("insertActivationCode("+email+", '"+code+"')");

        ConnectionManager connectionManager = new ConnectionManager(className, insertActivationCode);

        try {

            String sql = "UPDATE user SET activation_code=? WHERE email=?";
            PreparedStatement preparedStatement = connectionManager.prepareStatement(sql);
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

    public static void deleteActivationCode(String email, String code) throws SQLException, Exception {
        logger.debug("deleteActivationCode("+email+", '"+code+"')");

        ConnectionManager connectionManager = new ConnectionManager(className, deleteActivationCode);

        try {

            String sql = "UPDATE user SET activation_code=? WHERE email=?";
            PreparedStatement preparedStatement = connectionManager.prepareStatement(sql);
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

        ConnectionManager connMgr = new ConnectionManager(className);

        try {

            PreparedStatement stmt = connMgr.loadStatement(INSERT_FORGOT_PASSWORD);

            stmt.setString(1, code);
            stmt.setInt(2, userId);
            stmt.executeUpdate();

        } finally {
            connMgr.commit();
        }

    }

    public static void delete(String code) throws SQLException, Exception {
        logger.debug("delete('"+code+"')");

        ConnectionManager connMgr = new ConnectionManager(className);

        try {

            PreparedStatement stmt = connMgr.loadStatement(DELETE_FORGOT_PASSWORD);

            stmt.setString(1, code);
            stmt.executeUpdate();

        } finally {
            connMgr.commit();
        }

    }

}
