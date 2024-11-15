package store.model;

public class Product {
    private final String productName;
    private final int price;
    private int quantity;
    private final String promotion;

    public Product(String productName, int price, int quantity, String promotion) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
