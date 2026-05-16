package model;

public class Order {
    private int id;
    private int userId;
    private double totalPrice;
    private String orderDate;

    public Order(int id, int userId, double totalPrice, String orderDate) {
        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public double getTotalPrice() { return totalPrice; }
    public String getOrderDate() { return orderDate; }
}