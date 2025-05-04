
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DatabaseManager {

    private static final String DB_URL = "jdbc:ucanaccess://C:/Users/jwillis/Documents/CarRental.accdb";

    // connect to Microsoft Access database
    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // method to add a customer to the database
    public static int addCustomer(Customer customer) {
        String sql = "INSERT INTO Customers (CustomerName, License, Phone) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getLicense());
            pstmt.setString(3, customer.getPhone());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return auto-generated CustomerID
            }
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // method to display the customers in the database
    public static ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        String sql = "SELECT CustomerID, CustomerName, License, Phone FROM Customers";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int customerID = rs.getInt("CustomerID");
                String name = rs.getString("CustomerName");
                String license = rs.getString("License");
                String phone = rs.getString("Phone");
                customers.add(new Customer(customerID, name, license, phone));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public static boolean addCar(Car car, boolean available) {
        String sql = "INSERT INTO Cars (CarID, Make, Model, CarYear, DailyCost, IsAvailable) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, car.getCarID());
            pstmt.setString(2, car.getMake());
            pstmt.setString(3, car.getModel());
            pstmt.setInt(4, car.getYear());
            pstmt.setDouble(5, car.getDailyCost());
            pstmt.setBoolean(6, available);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // method to select a customer by their ID
    public static Customer getCustomerByID(int customerID) {
        String sql = "SELECT CustomerID, CustomerName, License, Phone FROM Customers WHERE CustomerID = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("CustomerID"),
                            rs.getString("CustomerName"),
                            rs.getString("License"),
                            rs.getString("Phone")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if customer not found
    }

    // Get available cars from the database
    public static ArrayList<Car> getAvailableCars() {
        ArrayList<Car> cars = new ArrayList<>();
        String sql = "SELECT CarID, Make, Model, CarYear, DailyCost FROM Cars WHERE IsAvailable = true";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                cars.add(new Car(
                        rs.getString("Make"),
                        rs.getString("Model"),
                        rs.getInt("CarYear"),
                        rs.getInt("CarID"),
                        rs.getDouble("DailyCost")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }
}
