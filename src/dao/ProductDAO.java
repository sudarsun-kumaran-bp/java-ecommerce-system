package dao;

import model.Product;
import util.DBConnection;
import java.sql.*;
import java.util.*;

public class ProductDAO {

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.id, p.name, p.price, p.stock, p.category_id, c.name as category_name " +
                     "FROM products p JOIN categories c ON p.category_id = c.id";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getInt("category_id")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
        return products;
    }

    public void addProduct(String name, double price, int stock, int categoryId) {
        if (name.isEmpty() || price <= 0 || stock < 0) {
            System.out.println("Invalid product details.");
            return;
        }
        String sql = "INSERT INTO products (name, price, stock, category_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, stock);
            ps.setInt(4, categoryId);
            ps.executeUpdate();
            System.out.println("Product added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    public void updateProduct(int id, double price, int stock) {
        if (price <= 0 || stock < 0) {
            System.out.println("Invalid price or stock.");
            return;
        }
        String sql = "UPDATE products SET price = ?, stock = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, price);
            ps.setInt(2, stock);
            ps.setInt(3, id);
            ps.executeUpdate();
            System.out.println("Product updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    public void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Product deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }

    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getInt("category_id")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching product: " + e.getMessage());
        }
        return null;
    }
}