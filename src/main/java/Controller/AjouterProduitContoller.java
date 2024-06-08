package Controller;

import Entity.Produit;
import Service.CommentCRUD;
import Service.ProduitCRUD;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AjouterProduitContoller {
    private final ProduitCRUD ps=new ProduitCRUD();
    private final CommentCRUD commentCRUD=new CommentCRUD();
    @FXML
    private Button btn_corses;

    @FXML
    private Button btn_events;

    @FXML
    private Button btn_forum;

    @FXML
    private Button btn_quizzs;

    @FXML
    private Button btn_store;

    @FXML
    private Button btn_user;


    @FXML
    private TableColumn<Produit, String> categoryColumn;

    @FXML
    private TextField categorypro;

    @FXML
    private TableColumn<Produit, String> descColumn;

    @FXML
    private TextField descpro;

    @FXML
    private TextField img1pro;

    @FXML
    private TextField img2pro;

    @FXML
    private TextField img3pro;

    @FXML
    private TableColumn<Produit, String> imgColumn;

    @FXML
    private Pane inner_pane;

    @FXML
    private Button mc;

    @FXML
    private TableColumn<Produit, String> nameColumn;

    @FXML
    private TextField namepro;

    @FXML
    private TableColumn<Produit, Double> priceColumn;

    @FXML
    private TextField pricepro;

    @FXML
    private TableView<Produit> productTable;

    @FXML
    private TableColumn<Produit, Integer> quantityColumn;

    @FXML
    private TextField quantitypro;

    @FXML
    private HBox root;

    @FXML
    private AnchorPane side_bar;
    @FXML
    private Button showbtn;

    @FXML
    private TextField txt_search;

    @FXML
    void gostorefront(MouseEvent event) {
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
    void showcommant(ActionEvent event) {
        Produit selectedItem = productTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Product Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a product to view comments.");
            alert.showAndWait();
            return; // Exit the method if no product is selected
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowCommantBack.fxml"));
            Parent root = loader.load();
            ShowCommantBackController controller = loader.getController();
            Produit selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                controller.initData(selectedProduct);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Comments for " + selectedProduct.getNom());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addPro(ActionEvent event) throws SQLException {
        ArrayList<Produit>produits=ps.afficherAll();
        for (Produit p:produits){
            if(p.getNom().equals(namepro.getText())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setContentText("Product Exist with the same name");
                alert.showAndWait();
               return;
            }
        }
        if (namepro.getText().isEmpty()) {
            showError(namepro, "Please check your Product name input.");
            return;
        }
        if (img1pro.getText().isEmpty()) {
            showError(img1pro, "Please provide the link for the first image.");
            return;
        }
        if (img2pro.getText().isEmpty()) {
            showError(img2pro, "Please provide the link for the second image.");
            return;
        }
        if (img3pro.getText().isEmpty()) {
            showError(img3pro, "Please provide the link for the third image.");
            return;
        }
        if (!isValidDouble(pricepro.getText())) {
            showError(pricepro, "Price should be a valid number.");
            return;
        }
        if (categorypro.getText().isEmpty()) {
            showError(categorypro, "Please provide a product category.");
            return;
        }

        if (!isValidInteger(quantitypro.getText())) {
            showError(quantitypro, "Quantity should be a valid integer.");
            return;
        }
        if (descpro.getText().isEmpty()) {
            showError(descpro, "Please provide a product description.");
            return;
        }


        try {
            ps.ajouter(new Produit(
                    Integer.parseInt(quantitypro.getText()),
                    Double.parseDouble(pricepro.getText()),
                    namepro.getText(),
                    descpro.getText(),
                    img1pro.getText(),
                    img2pro.getText(),
                    img3pro.getText(),
                    categorypro.getText()
            ));
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Product Added");
            successAlert.setContentText("The product was successfully added.");
            successAlert.showAndWait();
            clearFields(); // Clear fields after adding
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setContentText("Please check your number inputs.");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setContentText("There was a problem accessing the database. Please try again.");
            alert.showAndWait();
        }
    }


    private void showError(TextField field, String message) {
        field.setStyle("-fx-text-fill: red; -fx-border-color: red;");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setContentText(message);
        alert.showAndWait();
        field.setStyle(""); // Reset style after showing alert if needed
    }

    private boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private void showValidationError(String message) {
        // Display an alert with the provided error message
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void clearFields() {
        quantitypro.clear();
        pricepro.clear();
        namepro.clear();
        descpro.clear();
        img1pro.clear();
        img2pro.clear();
        img3pro.clear();
        categorypro.clear();
        loadTableData();
    }

    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        imgColumn.setCellValueFactory(new PropertyValueFactory<>("img1"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        loadTableData();
        img1pro.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() > 255) { // Adjust 255 to your desired character limit
                img1pro.setText(newValue.substring(0, 255)); // This truncates any input past the character limit
            }
        });
        img2pro.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() > 255) { // Adjust 255 to your desired character limit
                img2pro.setText(newValue.substring(0, 255)); // This truncates any input past the character limit
            }
        });
        img3pro.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() > 255) { // Adjust 255 to your desired character limit
                img3pro.setText(newValue.substring(0, 255)); // This truncates any input past the character limit
            }
        });

        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                namepro.setText(newSelection.getNom());
                pricepro.setText(String.valueOf(newSelection.getPrix()));
                descpro.setText(newSelection.getDescription());
                img1pro.setText(newSelection.getImg1());
                img2pro.setText(newSelection.getImg2());
                img3pro.setText(newSelection.getImg3());
                categorypro.setText(newSelection.getCategorie());
                quantitypro.setText(String.valueOf(newSelection.getQuantity()));
            }

        });
        loadTableData();  // Load initial table data

        // Add a listener to the search text field to update the table based on the search query
        txt_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTableData(newValue); // This method will be responsible for filtering table data
        });

    }
    private void filterTableData(String filter) {
        if (filter == null || filter.isEmpty()) {
            loadTableData();  // Reload all data if filter is cleared
            return;
        }

        try {
            ArrayList<Produit> allProducts = ps.afficherAll();
            ArrayList<Produit> filteredProducts = new ArrayList<>();

            String lowerCaseFilter = filter.toLowerCase();

            for (Produit produit : allProducts) {
                // Check if any property contains the search string
                if (produit.getNom().toLowerCase().contains(lowerCaseFilter) ||
                        produit.getDescription().toLowerCase().contains(lowerCaseFilter) ||
                        produit.getCategorie().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(produit.getPrix()).contains(lowerCaseFilter) ||
                        String.valueOf(produit.getQuantity()).contains(lowerCaseFilter)) {
                    filteredProducts.add(produit);
                }
            }

            productTable.setItems(FXCollections.observableArrayList(filteredProducts)); // Update table view
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setContentText("Error accessing the database for filtering: " + e.getMessage());
            alert.showAndWait();
        }
    }


    private void loadTableData() {
        try {
            ArrayList<Produit>produits=new ArrayList<>();
            produits=ps.afficherAll();
            productTable.setItems(FXCollections.observableArrayList(produits));

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void deletepro(ActionEvent event) {
        // Get selected item(s)
        Produit selectedItem = productTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                // Remove selected item from the TableView and the underlying data source
                ArrayList<Produit>produits=ps.afficherAll();
                for (Produit p:produits){
                commentCRUD.supprimerwithproduit(selectedItem.getId());
                }
                ps.supprimer(selectedItem); // Pass the selected product object
                loadTableData(); // Reload the table data after deletion
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setContentText("There was a problem deleting the product. Please try again.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select a product to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    void updateprod(ActionEvent event) {
        Produit selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                selectedProduct.setNom(namepro.getText());
                selectedProduct.setPrix(Double.parseDouble(pricepro.getText()));
                selectedProduct.setDescription(descpro.getText());
                selectedProduct.setImg1(img1pro.getText());
                selectedProduct.setImg2(img2pro.getText());
                selectedProduct.setImg3(img3pro.getText());
                selectedProduct.setCategorie(categorypro.getText());
                selectedProduct.setQuantity(Integer.parseInt(quantitypro.getText()));

                ps.modifier(selectedProduct);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Product Updated");
                successAlert.setContentText("Product details updated successfully.");
                successAlert.showAndWait();
                loadTableData();
            } catch (NumberFormatException | SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Updating Product");
                alert.setContentText("Error updating product: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Product Selected");
            alert.setContentText("Please select a product to update.");
            alert.showAndWait();
        }
    }
    @FXML
    void clearspace(ActionEvent event) {
        clearFields();
    }
    @FXML
    void gotocommand(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CommandBack.fxml"));
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
    @FXML
    void gotostat(MouseEvent event) {
        try {
            // Load the FXML file for the statistics view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductStac.fxml"));
            Parent root = loader.load();

            // Create a new stage for the statistics view
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Product Statistics");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions possibly thrown by the FXML loading
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load the statistics view.");
            alert.showAndWait();
        }


    }


    @FXML
    void goCourses(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionCours.fxml"));
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

    @FXML
    void goEvent(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenementAdmin.fxml"));
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

    @FXML
    void goForum(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/demo4/Admin.fxml"));
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

    @FXML
    void goQuizz(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Afficher.fxml"));
            Parent cartRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene of the current stage to the cart view
            Scene scene = new Scene(cartRoot,1050,700);
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

    @FXML
    void goStore(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterProduit.fxml"));
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

    @FXML
    void goUser(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserManagementController.fxml"));
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


    @FXML
    void GoToDashboard(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home_back.fxml"));
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

    @FXML
    void updateProfile(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/showUserProfileController.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(UserManagementController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
