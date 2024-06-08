package Controller;

import Entity.Comment;
import Entity.CurrentUser;
import Entity.Produit;
import Service.CartFileManager;
import Service.CommentCRUD;
import Service.ProduitCRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductDetailsController implements Initializable {
    private final ProduitCRUD ps=new ProduitCRUD();
    @FXML
    private TextArea newcommant;
    @FXML
    private VBox commantcontaint;
    @FXML
    private ImageView proImage;

    @FXML
    private Label proName;

    @FXML
    private Label prodescription;

    @FXML
    private Label proprice;

    @FXML
    private Button sendcommant;
    private int curentproductid;

    @FXML
    void addtocart(ActionEvent event) {
        CartFileManager.addProduct(curentproductid, 1); // Add the current product to the cart with quantity 1
        showAlert("Product added to cart successfully!");
    }
    @FXML
    void gotocart(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Panier.fxml"));
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
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void gotostore(MouseEvent event) {
        try {
            // Load the FXML file for the store view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Store.fxml"));
            Parent storeRoot = loader.load();

            // Get the current stage (window) using the event source
            Stage stage;
            if (event.getSource() instanceof Node) {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            } else {
                // Alternatively, if not triggered by a Node, use a default method to get the Stage
                stage = new Stage(); // This line should be adjusted according to your context
            }

            // Set the store scene in the current stage
            Scene scene = new Scene(storeRoot);
            stage.setScene(scene);
            stage.show();
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
    @FXML
    void addcommant(ActionEvent event) {
        // Retrieve the comment text from the TextArea
        String commentText = newcommant.getText().trim();

        // Check if the comment text is not empty
        if (!commentText.isEmpty()) {
            Comment newComment = new Comment();
            newComment.setId_prod(curentproductid); // Assuming you have set currentProductId when loading the product details
            newComment.setDescription(commentText);
            newComment.setId_user(CurrentUser.user_id); // Assuming a user ID, modify as necessary to suit your authentication context
            newComment.setDate(LocalDate.now()); // Current date

            CommentCRUD commentService = new CommentCRUD();
            try {
                commentService.ajouter(newComment); // Add the comment to the database
                newcommant.clear(); // Clear the TextArea after posting
                loadCommentsForProduct(curentproductid); // Refresh comments display
            } catch (SQLException e) {
                System.out.println("Error adding comment: " + e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setContentText("the comment section is empty !!!");
            alert.showAndWait();
        }
    }
    public void receiveData(int productId) {
        try {
            ArrayList<Produit>produits=ps.afficherAll();
            Optional<Produit> matchingProduct = produits.stream() // Use stream to find the product
                    .filter(p -> p.getId() == productId)
                    .findFirst();
            if (matchingProduct.isPresent()) {
                updateUI(matchingProduct.get());
            } else {
                System.out.println("Product not found");
                // Optionally clear the UI or notify the user that the product was not found
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void updateUI(Produit produit) {
        proName.setText(produit.getNom());
        prodescription.setText(produit.getDescription());
        proprice.setText(String.format("%.2f", produit.getPrix()));
        curentproductid=produit.getId();

        try {
            Image image = new Image(produit.getImg1(), true); // Load the image, true to load in background
            proImage.setImage(image);
        } catch (Exception e) {
            System.out.println("Failed to load image: " + e.getMessage());
            e.printStackTrace();

        }
        loadCommentsForProduct(produit.getId());
    }
    private void loadCommentsForProduct(int productId) {
        CommentCRUD commentService = new CommentCRUD();
        try {
            ArrayList<Comment> comments = commentService.afficherAll(); // Ideally should be filtered by productId
            commantcontaint.getChildren().clear(); // Clear existing content
            for (Comment comment : comments) {
                if (comment.getId_prod() == productId) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SampleCommant.fxml"));
                    HBox commentBox = fxmlLoader.load();
                    SampleCommantController controller = fxmlLoader.getController();
                    controller.setData(comment);
                    controller.setRefreshCommentsCallback(() -> loadCommentsForProduct(productId));
                    commantcontaint.getChildren().add(commentBox);
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Comment>comments=new ArrayList<>();
        for (Comment com:comments) {
            FXMLLoader fxmlLoader=new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/SampleCommant.fxml"));
            try {
                HBox hBox=fxmlLoader.load();
             SampleCommantController sampleCommantController=new SampleCommantController();
             sampleCommantController.setData(com);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}

