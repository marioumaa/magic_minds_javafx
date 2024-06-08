package Controller;

import Entity.Command;
import Entity.CurrentUser;
import Entity.Produit;
import Service.CartFileManager;
import Service.CommandCRUD;
import Service.ProduitCRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
public class PanierController {

    @FXML
    private Label total;
    @FXML
    private VBox commantcontaint;

    @FXML
    void toorder(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/checkout.fxml"));
            Parent root = loader.load();
            CheckoutController controller = loader.getController();

            double totalPrice = CartFileManager.calculateTotalPrice();
            controller.initialize(totalPrice, () -> {
                completeOrder();
            });

            Stage checkoutStage = new Stage();
            checkoutStage.setScene(new Scene(root));
            checkoutStage.setTitle("Checkout");
            checkoutStage.showAndWait();

            checkoutStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to open the checkout window.");
        }
    }

    private void completeOrder() {
        // Process order completion after payment
        CommandCRUD commandCRUD = new CommandCRUD();
        Map<Integer, Integer> cart = CartFileManager.loadCart();
        try {
            double totalPrice = CartFileManager.calculateTotalPrice();

            Command newCommand = new Command(0, CurrentUser.user_id, new ArrayList<>(cart.keySet()), totalPrice);
            commandCRUD.ajouter(newCommand);
            CartFileManager.clearCart();
            commantcontaint.getChildren().clear();
            total.setText("0.0");
            showAlert("Order placed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error placing order: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /* @FXML
    void toorder(ActionEvent event) {
        CommandCRUD commandCRUD = new CommandCRUD();
        ProduitCRUD produitCrud = new ProduitCRUD();
        Map<Integer, Integer> cart = CartFileManager.loadCart();

        try {
            // Calculate the total price
            double totalPrice = 0;
            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                Produit produit = produitCrud.getById(entry.getKey());
                totalPrice += produit.getPrix() * entry.getValue();
            }

            // Process payment
            boolean paymentSuccessful = PaymentService.processPayment(totalPrice);
            if (!paymentSuccessful) {
                showAlert("Payment failed. Please try again.");
                return;
            }

            // Create a new command
            Command newCommand = new Command(0, 3, new ArrayList<>(cart.keySet()), totalPrice);
            commandCRUD.ajouter(newCommand); // Add the command to the database

            // Clear the cart
            CartFileManager.clearCart();
            commantcontaint.getChildren().clear(); // Clear the cart UI
            total.setText("0.0");
            // Show a success message
            showAlert("Order placed successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error placing order: " + e.getMessage());
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }*/
    @FXML
    void initialize() {
        loadCartProducts();

    }

    @FXML
    void gotostore(MouseEvent event) {
        // Check if the controller is being initialized
        if (commantcontaint == null) {
            return; // Exit the method without executing further code
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Store.fxml"));
            Parent storeRoot = loader.load();

            Stage stage = null;
            // Attempt to get the stage from the event source if it's a Node
            if (event.getSource() instanceof Node) {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            }
            // If the stage could not be retrieved from the event, try the current scene's window
            if (stage == null && event.getPickResult().getIntersectedNode() != null) {
                stage = (Stage) event.getPickResult().getIntersectedNode().getScene().getWindow();
            }
            // Ensure that we have a valid stage to use
            if (stage != null) {
                Scene scene = new Scene(storeRoot);
                stage.setScene(scene);
                stage.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions possibly thrown by the FXML loading
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load the store view.");
            alert.showAndWait();
        }
    }


    private void loadCartProducts() {
        Map<Integer, Integer> cart = CartFileManager.loadCart();
        ProduitCRUD produitCrud = new ProduitCRUD();

        try {
            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                Produit produit = produitCrud.getById(entry.getKey());
                if (produit != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/SamplePanier.fxml"));
                    Node productNode = loader.load();
                    SamplePanierController controller = loader.getController();
                    produit.setQuantity(entry.getValue());
                    controller.setProduct(produit);
                    controller.setOnDelete(this::refreshCart);  // Set the delete callback
                    commantcontaint.getChildren().add(productNode);
                }
            }
            total.setText(String.format("%.2f", CartFileManager.calculateTotalPrice()));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshCart() {
        commantcontaint.getChildren().clear();  // Clear the existing UI elements for cart items
        loadCartProducts();  // Reload the cart products and add them to the UI
        total.setText(String.format("%.2f", CartFileManager.calculateTotalPrice()));  // Update the total price display
    }
    @FXML
    void gotocommand(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Command.fxml"));
            Parent cartRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene of the current stage to the cart view
            Scene scene = new Scene(cartRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load the cart view.");
            alert.showAndWait();
        }
    }

}
