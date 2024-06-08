package Controller;

import Entity.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class SampleproductController {
    @FXML
    private HBox box;

    @FXML
    private Label categorypro;

    @FXML
    private ImageView imagepro;

    @FXML
    private Label namepro;

    @FXML
    private Label pricepro;

    // Ensure color codes are correct hexadecimal strings for CSS
    private String[] colors = {"B9E5FF", "BDB2FE", "FB9AA8", "FF5056"};

    public void setData(Produit produit) {
        namepro.setText(produit.getNom());
        categorypro.setText(produit.getCategorie());
        pricepro.setText(String.format("%.2f", produit.getPrix())); // formatted to show two decimal places

        // Correctly setting random background color from colors array
        int randomIndex = (int) (Math.random() * colors.length); // generate a random index
        String colorCode = colors[randomIndex]; // get the hexadecimal color code
        // Set transparent background color with border radius using rgba()
        box.setStyle("-fx-background-color: rgba(" + hexToR(colorCode) + "," + hexToG(colorCode) + "," + hexToB(colorCode) + ",0.5);" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 10);");
        try {
            String imageUrl = produit.getImg1();
            Image image = new Image(imageUrl, true); // true for loading in background if needed
            imagepro.setImage(image);
        } catch (Exception e) {
            System.out.println("Failed to load image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to convert hexadecimal color code to RGB values
    private int hexToR(String hex) {
        return Integer.parseInt(hex.substring(0, 2), 16);
    }

    private int hexToG(String hex) {
        return Integer.parseInt(hex.substring(2, 4), 16);
    }

    private int hexToB(String hex) {
        return Integer.parseInt(hex.substring(4, 6), 16);
    }
}
