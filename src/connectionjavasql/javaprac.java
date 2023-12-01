package connectionjavasql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
 
public class javaprac {
 
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javapocprac", "root", "root");
 
            createCalculationTable(connection);
 
Scanner scanner = new Scanner(System.in);
            System.out.println("Enter first number:");
            double num1 = scanner.nextDouble();
 
            System.out.println("Enter operator (+, -, *, /):");
            String operator = scanner.next();
 
            System.out.println("Enter second number:");
            double num2 = scanner.nextDouble();
 
            
            double result = performCalculation(num1, operator, num2);
 
            System.out.println("Result: " + result);
 
            logToDatabase(connection, num1, operator, num2, result);
 
            connection.close();
            scanner.close();
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    private static void createCalculationTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS calculation (" +
                               "id INT AUTO_INCREMENT PRIMARY KEY," +
                               "num1 DOUBLE," +
                               "operator VARCHAR(10)," +
                               "num2 DOUBLE," +
                               "result DOUBLE)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
            preparedStatement.executeUpdate();
        }
    }
 
    private static double performCalculation(double num1, String operator, double num2) {
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                return (num2 != 0) ? num1 / num2 : 0; // Handle division by zero
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
 
    private static void logToDatabase(Connection connection, double num1, String operator, double num2, double result) throws SQLException {
        String insertSQL = "INSERT INTO calculation (num1, operator, num2, result) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setDouble(1, num1);
            preparedStatement.setString(2, operator);
            preparedStatement.setDouble(3, num2);
            preparedStatement.setDouble(4, result);
            preparedStatement.executeUpdate();
        }
    }
}

