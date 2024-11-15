package store.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PromotionManager {
    private static final String promotionsFile = "src/main/resources/promotions.md";
    private static List<Promotion> promotions;

    public PromotionManager() throws IOException {
        promotions = new ArrayList<>();
        readPromotionsFile();
    }

    public static Promotion findPromotionByPromotionName(String promotionName) {
       for (Promotion promotion : promotions) {
            if (promotion.getPromotionName().equals(promotionName)) {
                return promotion;
            }
        }
        return null;
    }

    public static void readPromotionsFile() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(promotionsFile));
        bufferedReader.readLine();

        String line;
        while (true) {
            line = bufferedReader.readLine();
            if (line == null) {
                bufferedReader.close();
                return;
            }
            promotions.add(splitLine(line));
        }
    }

    private static Promotion splitLine(String line) throws IOException {
        String[] str = line.split(",");
        String promotionName = str[0];
        int buyQuantity = Integer.parseInt(str[1]);
        int freeQuantity = Integer.parseInt(str[2]);
        String startDate = str[3];
        String endDate = str[4].trim();

        return new Promotion(promotionName, buyQuantity, freeQuantity, startDate, endDate);
    }

}
