package store.controller;

import store.model.*;
import store.view.InputView;
import store.view.OutputView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StoreController {
    private String membership;
    private ProductManager productManager;

    public StoreController() throws IOException {
        this.productManager = new ProductManager();
    }

    public void start() throws IOException {
        InputView.displayWelcomeMessage();
        PromotionManager promotionManager = new PromotionManager();
        List<Product> products = productManager.getProducts();
        for (Product product : products) {
            String productName = product.getProductName();
            int price = product.getPrice();
            int quantity = product.getQuantity();
            String promotion = product.getPromotion();
            OutputView.displayStock(productName, price, quantity, promotion);
        }

        getProducts();
    }

    private void getProducts() throws IOException {
        String inputProduct = InputView.readProduct();
        List<Order> orders = handleOrder(inputProduct);
        membership = InputView.readMembership();
        OutputView.displayReceiptStart();

        for (Order order : orders) {
            String orderedProductName = order.getOrderedProductName();
            int orderedProductQuantity = order.getOrderedProductQuantity();
            int orderedProductPrice = order.getProductPrice();
            int orderedPrice = orderedProductPrice * orderedProductQuantity;
            OutputView.displayReceipt(orderedProductName, orderedProductQuantity, orderedPrice);
        }

        int discountPromotionMoney = getPromotions(orders);
        for (Order order : orders) {
            String orderedProductName = order.getOrderedProductName();
            int orderedProductQuantity = order.getOrderedProductQuantity();
            order.decreaseProductQuantity(orderedProductName, orderedProductQuantity);
        }

        getCountingMoney(orders, discountPromotionMoney);
    }

    private List<Order> handleOrder(String inputProduct) throws IOException {
        String[] tokens = inputProduct.split(",");
        List<Order> orders = new ArrayList<>();
        for (String token : tokens) {
            token = token.trim();
            String s = token.replaceAll("[\\[\\]]", "");
            String[] split = s.split("-");
            String orderedProductName = split[0].trim();
            int orderedProductQuantity = Integer.parseInt(split[1].trim());

            orders.add(new Order(orderedProductName, orderedProductQuantity));
        }
        return orders;

    }

    private int getPromotions(List<Order> orders) throws IOException {
        OutputView.displayGiveawayStart();
        LocalDate currentDate = LocalDate.now();
        int discountPromotionMoney = 0;

        for (Order order : orders) {
            if (order.isPromotionProduct(currentDate) && order.getOrderedPromotionQuantity()!=0) {
                String promotionProductName = order.getOrderedProductName();
                int promotionProductQuantity = order.getOrderedPromotionQuantity();
                discountPromotionMoney += promotionProductQuantity * order.getProductPrice();
                OutputView.displayGiveaway(promotionProductName, promotionProductQuantity);
            }
        }
        return discountPromotionMoney;
    }

    private void getCountingMoney(List<Order> orders, int discountPromotionMoney) throws IOException {
        int count = 0;
        int money = 0;
        int nonPromotionMoney = 0;

        for (Order order : orders) {
            count += order.getOrderedProductQuantity();
            money += order.getOrderedProductQuantity() * order.getProductPrice();

            if (!order.isPromotionProduct(LocalDate.now())) {
                nonPromotionMoney += order.getOrderedProductQuantity() * order.getProductPrice();
            }
        }
        int discountMembershipMoney = 0;
        if (getMembership()) {
            discountMembershipMoney = (int) (nonPromotionMoney * 0.3);
        }
        if (discountMembershipMoney > 8000) {
            discountMembershipMoney = 8000;
        }
        int totalMoney = money - discountPromotionMoney - discountMembershipMoney;
        OutputView.displayMoney(count, money, discountPromotionMoney, discountMembershipMoney,totalMoney);

        getAgain();
    }

    private boolean getMembership() {
        return membership.equals("Y");
    }

    private void getAgain() throws IOException {
        String again = InputView.readOtherProduct();

        if (again.equals("Y")) {
            InputView.displayWelcomeMessage();
            List<Product> products = productManager.getProducts();
            for (Product product : products) {
                String productName = product.getProductName();
                int price = product.getPrice();
                int quantity = product.getQuantity();
                String promotion = product.getPromotion();
                OutputView.displayStock(productName, price, quantity, promotion);
            }

            getProducts();
        }
    }
}
