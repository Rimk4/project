package servlet;

import java.sql.*;

public class Database {
    private Connection connection;

    public Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            createTable();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public boolean authenticate(String username, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private void createTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, username TEXT, password TEXT)");
    }

    public void addUser(String username, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}