package main;

import dao.*;
import model.*;
import java.util.*;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static UserDAO userDAO = new UserDAO();
    static ProductDAO productDAO = new ProductDAO();
    static CartDAO cartDAO = new CartDAO();
    static OrderDAO orderDAO = new OrderDAO();
    static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("=== Welcome to Java E-Commerce System ===");
        while (true) {
            if (currentUser == null) {
                showAuthMenu();
            } else if (currentUser.getRole().equals("admin")) {
                showAdminMenu();
            } else {
                showCustomerMenu();
            }
        }
    }

    static void showAuthMenu() {
        System.out.println("\n1. Register\n2. Login\n3. Exit");
        System.out.print("Choice: ");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1:
                System.out.print("Username: ");
                String u = sc.nextLine();
                System.out.print("Password: ");
                String p = sc.nextLine();
                if (userDAO.register(u, p))
                    System.out.println("Registered successfully! Please login.");
                break;
            case 2:
                System.out.print("Username: ");
                String lu = sc.nextLine();
                System.out.print("Password: ");
                String lp = sc.nextLine();
                currentUser = userDAO.login(lu, lp);
                if (currentUser != null)
                    System.out.println("Welcome, " + currentUser.getUsername() + "!");
                else
                    System.out.println("Invalid credentials.");
                break;
            case 3:
                System.out.println("Goodbye!");
                System.exit(0);
        }
    }

    static void showCustomerMenu() {
        System.out.println("\n1. View Products\n2. Add to Cart\n3. View Cart\n4. Remove from Cart\n5. Place Order\n6. Order History\n7. Logout");
        System.out.print("Choice: ");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1:
                List<Product> products = productDAO.getAllProducts();
                System.out.println("\n--- Products ---");
                for (Product pr : products)
                    System.out.println("ID: " + pr.getId() + " | " + pr.getName() + " | Rs." + pr.getPrice() + " | Stock: " + pr.getStock());
                break;
            case 2:
                System.out.print("Product ID: ");
                int pid = sc.nextInt();
                System.out.print("Quantity: ");
                int qty = sc.nextInt();
                sc.nextLine();
                cartDAO.addToCart(currentUser.getId(), pid, qty);
                break;
            case 3:
                cartDAO.viewCart(currentUser.getId());
                break;
            case 4:
                System.out.print("Product ID to remove: ");
                int rpid = sc.nextInt();
                sc.nextLine();
                cartDAO.removeFromCart(currentUser.getId(), rpid);
                break;
            case 5:
                orderDAO.placeOrder(currentUser.getId());
                break;
            case 6:
                orderDAO.viewOrderHistory(currentUser.getId());
                break;
            case 7:
                currentUser = null;
                System.out.println("Logged out.");
                break;
        }
    }

    static void showAdminMenu() {
        System.out.println("\n1. View Products\n2. Add Product\n3. Update Product\n4. Delete Product\n5. Logout");
        System.out.print("Choice: ");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1:
                List<Product> products = productDAO.getAllProducts();
                System.out.println("\n--- Products ---");
                for (Product pr : products)
                    System.out.println("ID: " + pr.getId() + " | " + pr.getName() + " | Rs." + pr.getPrice() + " | Stock: " + pr.getStock());
                break;
            case 2:
                System.out.print("Name: ");
                String name = sc.nextLine();
                System.out.print("Price: ");
                double price = sc.nextDouble();
                System.out.print("Stock: ");
                int stock = sc.nextInt();
                System.out.print("Category ID (1=Electronics, 2=Accessories, 3=Audio): ");
                int catId = sc.nextInt();
                sc.nextLine();
                productDAO.addProduct(name, price, stock, catId);
                break;
            case 3:
                System.out.print("Product ID to update: ");
                int uid = sc.nextInt();
                System.out.print("New Price: ");
                double np = sc.nextDouble();
                System.out.print("New Stock: ");
                int ns = sc.nextInt();
                sc.nextLine();
                productDAO.updateProduct(uid, np, ns);
                break;
            case 4:
                System.out.print("Product ID to delete: ");
                int did = sc.nextInt();
                sc.nextLine();
                productDAO.deleteProduct(did);
                break;
            case 5:
                currentUser = null;
                System.out.println("Logged out.");
                break;
        }
    }
}