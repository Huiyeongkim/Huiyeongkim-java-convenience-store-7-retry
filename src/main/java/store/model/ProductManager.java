package store.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private static final String file = "src/main/resources/products.md";
    private static List<Product> productsWithPromotion;
    private static List<Product> productsWithoutPromotion;
    private static List<Product> products;

    public ProductManager() throws IOException {
        productsWithPromotion = new ArrayList<>();
        productsWithoutPromotion = new ArrayList<>();
        products = new ArrayList<>();
        readProductsFile();
        readDetailProductsFile();
    }

    public static Product findProductByName(String orderedProductName) {
        for (Product product : products) {
            if (product.getProductName().equals(orderedProductName)) {
                return product;
            }
        }
        return null;
    }

    public static Product findPromotionProductByName(String orderedProductName) {
        for (Product product : productsWithPromotion) {
            if (product.getProductName().equals(orderedProductName)) {
                return product;
            }
        }
        return null;
    }

    public static Product findWithoutPromotionProductByName(String orderedProductName) {
        for (Product product : productsWithoutPromotion) {
            if (product.getProductName().equals(orderedProductName)) {
                return product;
            }
        }
        return null;
    }

    private void readProductsFile() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        bufferedReader.readLine();

        String line;
        while (true) {
            line = bufferedReader.readLine();
            if (line == null) {
                bufferedReader.close();
                return;
            }
            Product product = splitLine(line);
            if (product.getPromotion() == null || product.getPromotion().isEmpty()) {
                productsWithoutPromotion.add(product);
            }
            else {
                productsWithPromotion.add(product);
            }
            products.add(product);
        }
    }

    private void readDetailProductsFile() {
        for (Product product : productsWithPromotion) {
            boolean hasRegularStock = false;
            for (Product regularProduct : productsWithoutPromotion) {
                if (product.getProductName().equals(regularProduct.getProductName()) && regularProduct.getQuantity() <= 0) {
                    hasRegularStock = true;
                    break;
                }
            }

            if (!hasRegularStock) {
                productsWithoutPromotion.add(new Product(product.getProductName(), product.getPrice(), 0, null));
                products.add(new Product(product.getProductName(), product.getPrice(), 0, null));
            }
        }
    }
    private Product splitLine(String line) {
        String[] str = line.split(",");
        String name = str[0];
        int price = Integer.parseInt(str[1]);
        int quantity = Integer.parseInt(str[2]);
        String promotion = str[3];

        if (promotion.isEmpty() || promotion.equals("null")) {
            promotion = null;
        }

        return new Product(name, price, quantity, promotion);
    }

    public List<Product> getProducts() {
        return products;
    }

    public boolean hasPromotions() {
        return !productsWithPromotion.isEmpty();
    }
}
