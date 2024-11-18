package store.validator;

import store.model.Product;
import store.model.ProductManager;

public class Validator {
    public static void validateQuantity(String productName, int quantity) {
        Product promotionProductQuantity = ProductManager.findWithoutPromotionProductByName(productName);
        Product nonPromotionProductQuantity = ProductManager.findPromotionProductByName(productName);
        int availableStock = 0;

        if (promotionProductQuantity != null) {
            availableStock += promotionProductQuantity.getQuantity();
        }
        if (nonPromotionProductQuantity != null) {
            availableStock += nonPromotionProductQuantity.getQuantity();
        }

        if (quantity > availableStock) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    public static void validateInput(String[] split) {
        if (split.length != 2) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    public static void validateNoExist(String orderedProductName) {
        Product promotionProductNoExist = ProductManager.findProductByName(orderedProductName);
        if (promotionProductNoExist == null) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }
    }

    public static void validateYesOrNo(String inputAnswer) {
        if (!inputAnswer.equals("Y") && !inputAnswer.equals("N") ) {
            throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }
}
