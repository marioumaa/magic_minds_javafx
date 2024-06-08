package Controller;
import Entity.Comment;
import Entity.Produit;
import Service.CommentCRUD;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ShowCommantBackController {

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
    private TableView<Comment> commanttable;

    @FXML
    private TableColumn<Comment, LocalDate> datecommant;

    @FXML
    private Pane inner_pane;

    @FXML
    private TableColumn<Comment,String> nameuser;

    @FXML
    private HBox root;

    @FXML
    private AnchorPane side_bar;

    @FXML
    private TextField txt_search;

    @FXML
    private TableColumn<Comment,String> usercommant;

    @FXML
    private Label productdesc;

    @FXML
    private ImageView productimg;

    @FXML
    private Label productname;

    @FXML
    private Label productprice;

    @FXML
    private Label productquantity;

    public void initData(Produit product) {
        loadComments(product.getId());
         productname.setText(product.getNom());
         productprice.setText(Double.toString(product.getPrix()));
         productquantity.setText(Integer.toString(product.getQuantity()));
         productdesc.setText(product.getDescription());
        try {
            Image image = new Image(product.getImg1(), true); // Load the image, true to load in background
            productimg.setImage(image);
        } catch (Exception e) {
            System.out.println("Failed to load image: " + e.getMessage());
            e.printStackTrace();

        }
    }

    private void loadComments(int productId) {
        CommentCRUD commentCRUD = new CommentCRUD();
        try {
            ArrayList<Comment> comments = commentCRUD.afficherAllByProductId(productId);
            commanttable.setItems(FXCollections.observableArrayList(comments));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void initialize() {
        datecommant.setCellValueFactory(new PropertyValueFactory<>("date"));
       nameuser.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        usercommant.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    @FXML
    void clearspace(ActionEvent event) {

    }

    @FXML
    void deletepro(ActionEvent event) {
        Comment selectedComment = commanttable.getSelectionModel().getSelectedItem();
        if (selectedComment != null) {
            try {
                CommentCRUD commentCRUD = new CommentCRUD();
                commentCRUD.supprimer(selectedComment);
                // Remove the selected comment from the table
                commanttable.getItems().remove(selectedComment);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
