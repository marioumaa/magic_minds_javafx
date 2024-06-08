package Service;
import Entity.Produit;

import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
public class CartFileManager {
    private static final String FILE_PATH = "src/main/resources/cart.txt";

    public static Map<Integer, Integer> loadCart() {
        Map<Integer, Integer> cart = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int productId = Integer.parseInt(parts[0]);
                int quantity = Integer.parseInt(parts[1]);
                cart.put(productId, quantity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cart;
    }

    public static void saveCart(Map<Integer, Integer> cart) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addProduct(int productId, int quantity) {
        Map<Integer, Integer> cart = loadCart();
        cart.merge(productId, quantity, Integer::sum); // Sum new quantity with existing one
        saveCart(cart);
    }

    public static void deleteProduct(int productId) {
        Map<Integer, Integer> cart = loadCart();
        if (cart.containsKey(productId)) {
            cart.remove(productId);
            saveCart(cart);
        }
    }

    public static void updateProduct(int productId, int newQuantity) {
        Map<Integer, Integer> cart = loadCart();
        if (cart.containsKey(productId)) {
            if (newQuantity > 0) {
                cart.put(productId, newQuantity);
            } else {
                cart.remove(productId); // Remove product if quantity is zero or negative
            }
            saveCart(cart);
        }
    }

    public static void clearCart() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            writer.print(""); // Clear the contents of the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static double calculateTotalPrice() {
        ProduitCRUD produitCrud = new ProduitCRUD(); // Initialize your CRUD operations class
        Map<Integer, Integer> cart = loadCart();
        double total = 0;

        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();
            try {
                Produit product = produitCrud.getById(productId);
                if (product != null) {
                    double price = product.getPrix();
                    total += price * quantity;
                } else {
                    System.err.println("No product found with ID: " + productId);
                }
            } catch (SQLException e) {
                System.err.println("Error fetching product with ID: " + productId);
                e.printStackTrace();
            }
        }
        return total;
    }
}
