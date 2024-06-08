package Controller;

import Entity.Produit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class Sampleprod2Controller {

    @FXML
    private Label categoryproduit;

    @FXML
    private ImageView imageproduit;

    @FXML
    private Label nameproduit;

    @FXML
    private Label priceproduit;
    @FXML
    private Button viewbtn;
    public void setData(Produit produit) {
        nameproduit.setText(produit.getNom());
        categoryproduit.setText(produit.getCategorie());
        priceproduit.setText(String.format("%.2f", produit.getPrix())); // formatted to show two decimal places
        viewbtn.setUserData(produit.getId());
        viewbtn.setOnAction(this::viewdetails);


        try {
            String imageUrl = produit.getImg1();
            Image image = new Image(imageUrl, true); // true for loading in background if needed
            imageproduit.setImage(image);

        } catch (Exception e) {
            System.out.println("Failed to load image: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    void viewdetails(ActionEvent event) {
        int productId = (int) viewbtn.getUserData();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductDetails.fxml"));
        try {
            Parent root = loader.load();
            ProductDetailsController productDetailsController=loader.getController();
            productDetailsController.receiveData(productId);
            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            currentStage.getScene().setRoot(root);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int getProductIDFromButton() {
        return (int) viewbtn.getUserData();
    }
}
