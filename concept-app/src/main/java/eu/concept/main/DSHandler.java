package eu.concept.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton pattern example using Java Enumeration
 */
public enum DSHandler {

    INSTANCE;

    private Connection connection = null;
    private final String DBURL;

    DSHandler() {

        this.DBURL = "jdbc:mysql://localhost:3306/openproject?user=openproject&password=!dev35!&useEncoding=true&characterEncoding=UTF-8&useTimezone=true&&serverTimezone=UTC&autoReconnect=true";

    }

    /**
     * Creates a new connection to MySQL database system
     *
     * @return An instance of MySQL Connection object
     */
    public Connection getConnection() {
        try {

            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                this.connection = DriverManager.getConnection(this.DBURL);
            } catch (SQLException ex) {
                Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.connection;
    }

    /**
     * Close all the resources used to access a Database
     *
     * @param connection A MySql Connection instance
     * @param preparedStatement A PreparedStatment stream
     * @param resultSet A ResultSet stream
     * @return
     */
    public boolean closeDBStreams(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        boolean status = true;
        if (null != resultSet) {
            try {
                resultSet.close();
            } catch (SQLException ex) {
                Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
                status = false;
            }
        }
        if (null != preparedStatement) {
            try {
                preparedStatement.close();
            } catch (SQLException ex) {
                Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
                status = false;
            }
        }
        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
                status = false;
            }
        }

        if (status) {
            //Logger.getLogger(DSHandler.class.getName()).info("Successfuly closed all Database streams...");
        } else {
            Logger.getLogger(DSHandler.class.getName()).severe("Could not close all Database streams... This may lead to memory leak..");
        }

        return status;
    }

    public boolean rollback(Connection connection) {
        boolean isSuccess = false;
        if (null != connection) {
            try {
                connection.rollback();
                isSuccess = !isSuccess;
            } catch (SQLException ex) {
                Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return isSuccess;
    }

//    public static void main(String[] args) {
//        Connection ds = DSHandler.INSTANCE.getConnection();
//        PreparedStatement stm;
//        try {
//            stm = ds.prepareStatement("SELECT * from work_packages");
//            ResultSet rs = stm.executeQuery();
//            if (rs.next()) {
//                Logger.getLogger(DSHandler.class.getName()).info(rs.getString("project_id"));
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(DSHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

}
