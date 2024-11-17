package store.validator;

import store.model.Product;
import store.model.ProductManager;

public class Validator {
    public static void validateQuantity(String productName, int quantity) {
        Product product = ProductManager.findWithoutPromotionProductByName(productName);
        Product product1 = ProductManager.findPromotionProductByName(productName);
        int availableStock = product.getQuantity() + product1.getQuantity();

        if (quantity > availableStock) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }
}
