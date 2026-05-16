package dao;

import util.DBConnection;
import java.sql.*;

public class OrderDAO {

    public void placeOrder(int userId) {
        String cartSql = "SELECT c.product_id, c.quantity, p.price, p.stock " +
                         "FROM cart c JOIN products p ON c.product_id = p.id WHERE c.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(cartSql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            double total = 0;
            boolean empty = true;

            // Calculate total
            while (rs.next()) {
                empty = false;
                int qty = rs.getInt("quantity");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                if (qty > stock) {
                    System.out.println("Insufficient stock for product ID: " + rs.getInt("product_id"));
                    return;
                }
                total += price * qty;
            }

            if (empty) {
                System.out.println("Cart is empty. Add items before placing order.");
                return;
            }

            // Insert order
            String orderSql = "INSERT INTO orders (user_id, total_price) VALUES (?, ?)";
            PreparedStatement orderPs = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderPs.setInt(1, userId);
            orderPs.setDouble(2, total);
            orderPs.executeUpdate();
            ResultSet keys = orderPs.getGeneratedKeys();
            int orderId = 0;
            if (keys.next()) orderId = keys.getInt(1);

            // Insert order items
            ps.setInt(1, userId);
            ResultSet rs2 = ps.executeQuery();
            while (rs2.next()) {
                String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
                PreparedStatement itemPs = conn.prepareStatement(itemSql);
                itemPs.setInt(1, orderId);
                itemPs.setInt(2, rs2.getInt("product_id"));
                itemPs.setInt(3, rs2.getInt("quantity"));
                itemPs.setDouble(4, rs2.getDouble("price"));
                itemPs.executeUpdate();

                // Update stock
                String stockSql = "UPDATE products SET stock = stock - ? WHERE id = ?";
                PreparedStatement stockPs = conn.prepareStatement(stockSql);
                stockPs.setInt(1, rs2.getInt("quantity"));
                stockPs.setInt(2, rs2.getInt("product_id"));
                stockPs.executeUpdate();
            }

            // Clear cart
            new CartDAO().clearCart(userId);
            System.out.println("Order placed successfully! Order ID: " + orderId + " | Total: Rs." + total);

        } catch (SQLException e) {
            System.out.println("Error placing order: " + e.getMessage());
        }
    }

    public void viewOrderHistory(int userId) {
        String sql = "SELECT o.id, o.total_price, o.order_date, p.name, oi.quantity " +
                     "FROM orders o JOIN order_items oi ON o.id = oi.order_id " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "JOIN users u ON o.user_id = u.id " +
                     "WHERE o.user_id = ? ORDER BY o.order_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            System.out.println("\n--- Order History ---");
            boolean empty = true;
            while (rs.next()) {
                empty = false;
                System.out.println("Order ID: " + rs.getInt("o.id") +
                        " | Product: " + rs.getString("p.name") +
                        " | Qty: " + rs.getInt("oi.quantity") +
                        " | Total: Rs." + rs.getDouble("o.total_price") +
                        " | Date: " + rs.getString("o.order_date"));
            }
            if (empty) System.out.println("No orders found.");
        } catch (SQLException e) {
            System.out.println("Error fetching orders: " + e.getMessage());
        }
    }
}