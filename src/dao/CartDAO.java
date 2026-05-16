package dao;

import util.DBConnection;
import java.sql.*;

public class CartDAO {

    public void addToCart(int userId, int productId, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return;
        }
        String check = "SELECT * FROM cart WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(check)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String update = "UPDATE cart SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";
                PreparedStatement ps2 = conn.prepareStatement(update);
                ps2.setInt(1, quantity);
                ps2.setInt(2, userId);
                ps2.setInt(3, productId);
                ps2.executeUpdate();
            } else {
                String insert = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
                PreparedStatement ps2 = conn.prepareStatement(insert);
                ps2.setInt(1, userId);
                ps2.setInt(2, productId);
                ps2.setInt(3, quantity);
                ps2.executeUpdate();
            }
            System.out.println("Added to cart successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding to cart: " + e.getMessage());
        }
    }

    public void viewCart(int userId) {
        String sql = "SELECT p.name, p.price, c.quantity, (p.price * c.quantity) as total " +
                     "FROM cart c JOIN products p ON c.product_id = p.id WHERE c.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            System.out.println("\n--- Your Cart ---");
            double grandTotal = 0;
            boolean empty = true;
            while (rs.next()) {
                empty = false;
                System.out.println("Product: " + rs.getString("name") +
                        " | Price: " + rs.getDouble("price") +
                        " | Qty: " + rs.getInt("quantity") +
                        " | Total: " + rs.getDouble("total"));
                grandTotal += rs.getDouble("total");
            }
            if (empty) System.out.println("Cart is empty.");
            else System.out.println("Grand Total: Rs." + grandTotal);
        } catch (SQLException e) {
            System.out.println("Error viewing cart: " + e.getMessage());
        }
    }

    public void removeFromCart(int userId, int productId) {
        String sql = "DELETE FROM cart WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
            System.out.println("Item removed from cart.");
        } catch (SQLException e) {
            System.out.println("Error removing from cart: " + e.getMessage());
        }
    }

    public void clearCart(int userId) {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error clearing cart: " + e.getMessage());
        }
    }
}