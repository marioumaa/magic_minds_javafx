package Controller;
import Service.PaymentService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CheckoutController {

    @FXML
    private TextField cardNumberField;

    @FXML
    private TextField cvcField;

    @FXML
    private TextField expirationDateField;
    private double amountToBePaid;
    private Runnable onPaymentSuccess;

    private static Stage checkoutStage; // Static variable to hold the checkout stage instance

    public void initialize(double amount, Runnable onSuccess) {
        this.amountToBePaid = amount;
        this.onPaymentSuccess = onSuccess;

        // Add listeners to validate input fields
        addInputListeners();
    }

    @FXML
    void payNow(ActionEvent event) {
        String cardNumber = cardNumberField.getText();
        String cvc = cvcField.getText();
        String expirationDate = expirationDateField.getText();

        if (cardNumber.isEmpty() || cvc.isEmpty() || expirationDate.isEmpty()) {
            showAlert("Please fill in all payment details.");
            return;
        }
        boolean paymentSuccessful = PaymentService.processPayment(amountToBePaid);
        if (paymentSuccessful) {
            if (onPaymentSuccess != null) {
                onPaymentSuccess.run();  // Execute any callbacks (such as completing the order)
            }
            closeStage(event);  // Close only the checkout stage
        } else {
            showAlert("Payment failed. Please try again.");
        }
    }

    private void closeStage(ActionEvent event) {
        // Retrieve the stage from the event and close it
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Payment Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addInputListeners() {
        cardNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,16}")) {
                cardNumberField.setText(oldValue);
            }
        });

        cvcField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,3}")) {
                cvcField.setText(oldValue);
            }
        });

        expirationDateField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,4}")) {
                expirationDateField.setText(oldValue);
            }
        });
    }

    // Method to prevent opening multiple checkout windows
    public static boolean isCheckoutOpen() {
        return checkoutStage != null && checkoutStage.isShowing();
    }

    // Method to set the checkout stage
    public static void setCheckoutStage(Stage stage) {
        checkoutStage = stage;
    }
}
