package store;


import store.controller.StoreController;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        // TODO: 프로그램 구현
        StoreController storeController = new StoreController();
        storeController.start();
    }
}
