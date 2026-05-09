package vn.ptit.judge.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import vn.ptit.judge.model.ProductY;

@Service
public class DataGenerationService {

    private static final String[] WORDS = {
            "apple", "banana", "cherry", "date", "elderberry",
            "fig", "grape", "honeydew", "kiwi", "lemon"
    };

    private static final String[] PRODUCT_NAMES = {
            "Laptop", "Smartphone", "Tablet", "Monitor", "Keyboard",
            "Mouse", "Headphones", "Speaker", "Camera", "Printer"
    };

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private final Random random = new Random();

    public List<Integer> generateIntegers(int count, int min, int max) {
        List<Integer> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(random.nextInt(max - min + 1) + min);
        }
        return result;
    }

    public String generateWords(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(WORDS[random.nextInt(WORDS.length)]);
        }
        return sb.toString();
    }

    public ProductY generateProduct() {
        String name = PRODUCT_NAMES[random.nextInt(PRODUCT_NAMES.length)];
        double price = 50 + random.nextInt(451);
        double taxRate = 5 + random.nextInt(16);
        double discount = random.nextInt(16);
        ProductY product = new ProductY();
        product.setName(name);
        product.setPrice(price);
        product.setTaxRate(taxRate);
        product.setDiscount(discount);
        product.setFinalPrice(0.0);
        return product;
    }

    public String generateProductGrpc() {
        String name = PRODUCT_NAMES[random.nextInt(PRODUCT_NAMES.length)];
        int price = 50 + random.nextInt(451);
        int taxRate = 5 + random.nextInt(16);
        int discount = random.nextInt(16);
        return String.format(
                "{\"name\":\"%s\",\"price\":%d,\"taxRate\":%d,\"discount\":%d}",
                name, price, taxRate, discount);
    }

    public String generateString() {
        int length = 5 + random.nextInt(11);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }
}
