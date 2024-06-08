package Controller;

import Entity.Produit;
import Service.CartFileManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SamplePanierController {

    @FXML
    private ImageView img;

    @FXML
    private Label name;

    @FXML
    private Label price;

    @FXML
    private Label qt;

    @FXML
    private Label tt;
    private Produit produit;
    @FXML
    private Button deleteButton;
    private Runnable onDelete;

    public void setProduct(Produit produit) {
        this.produit = produit;
        name.setText(produit.getNom());
        price.setText(String.format("%.2f", produit.getPrix()));
        qt.setText(String.valueOf(produit.getQuantity()));
        tt.setText(String.format("%.2f", produit.getPrix() * produit.getQuantity()));
        if (!produit.getImg1().isEmpty()) {
            img.setImage(new Image(produit.getImg1()));
        }
        // Set the delete button to handle deletion of this specific product
        deleteButton.setOnAction(this::delete_item);
    }
    public void setOnDelete(Runnable onDelete) {
        this.onDelete = onDelete;
    }

    @FXML
    void delete_item(ActionEvent event) {
        if (produit != null) {
            CartFileManager.deleteProduct(produit.getId());
            if (onDelete != null) {
                onDelete.run();
            }
        }
    }

}
