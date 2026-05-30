package com.coffeeshop.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp DAO cơ sở - quản lý kết nối JDBC đến CSDL PostgreSQL.
 * Tất cả các DAO khác đều kế thừa từ lớp này.
 */
public class DAO {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/coffeeshop";
    private static final String DB_USERNAME = "coffee_admin";
    private static final String DB_PASSWORD = "123";

    protected static Connection connection;

    /**
     * Lấy kết nối đến CSDL. Nếu chưa có hoặc đã đóng thì tạo mới.
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection to database failed.");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Đóng kết nối CSDL.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
