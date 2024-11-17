package store.model;

import store.validator.Validator;
import store.view.OutputView;

import java.time.LocalDate;

public class Order {
    private final String orderedProductName;
    private final int orderedProductQuantity;

    public Order(String orderedProductName, int orderedProductQuantity) {
        this.orderedProductName = orderedProductName;
        this.orderedProductQuantity = orderedProductQuantity;
    }

    public int getProductPrice() {
        Product product = ProductManager.findProductByName(orderedProductName);
        if (product == null) {
            return 0;
        }
        return product.getPrice();
    }

    public String getOrderedProductName() {
        return orderedProductName;
    }

    public int getOrderedProductQuantity() {
        return orderedProductQuantity;
    }

    public Promotion findPromotion() {
        Product product = ProductManager.findPromotionProductByName(orderedProductName);
        if (product == null) {
            return null;
        }
        Promotion promotion = PromotionManager.findPromotionByPromotionName(product.getPromotion());
        if (promotion == null) {
            return null;
        }
        return promotion;
    }

    public boolean isPromotionProduct(LocalDate currentDate) {
        Product product = ProductManager.findPromotionProductByName(orderedProductName);
        if (product == null) {
            return false;
        }
        return findPromotion().isPromotionValid(currentDate);
    }

    public int getOrderedPromotionQuantity() {
        Product product = ProductManager.findPromotionProductByName(orderedProductName);
        if (product == null) {
            return 0;
        }
        Promotion promotion = PromotionManager.findPromotionByPromotionName(product.getPromotion());
        if (promotion == null) {
            return 0;
        }

        if (orderedProductQuantity > product.getQuantity()) {
            return (product.getQuantity() / (promotion.getBuyQuantity() + promotion.getFreeQuantity())) * promotion.getFreeQuantity();
        }
        return (orderedProductQuantity / (promotion.getBuyQuantity() + promotion.getFreeQuantity())) * promotion.getFreeQuantity();
    }

    public int decreaseProductQuantity(String productName, int quantity) {
        Product product = ProductManager.findPromotionProductByName(productName);
        Product product2 = ProductManager.findWithoutPromotionProductByName(productName);
        int remainingQuantity = quantity;

        if (product != null) {
            int currentQuantity = product.getQuantity();
            if (currentQuantity >= remainingQuantity) {
                product.setQuantity(currentQuantity - remainingQuantity);
            } else {
                product.setQuantity(0);
            }
            remainingQuantity -= currentQuantity;
        }

        if (remainingQuantity > 0) {
            if (product2 != null) {
                int currentQuantity = product2.getQuantity();
                if (currentQuantity >= remainingQuantity) {
                    product2.setQuantity(currentQuantity - remainingQuantity);
                }
            }
        }
        return 0;
    }
}
