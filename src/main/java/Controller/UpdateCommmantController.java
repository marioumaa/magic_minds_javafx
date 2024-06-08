package Controller;
import Entity.Comment;
import Service.CommentCRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class UpdateCommmantController {
    private CommentCRUD cmd=new CommentCRUD();
    @FXML
    private TextArea updatecontaner;
    @FXML
    private Button upcommant;
    private int commentId;
    private int productId;
    @FXML
    void updatecommant(ActionEvent event) throws SQLException {
        if(updatecontaner.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setContentText("the comment field is Empty !!! ");
            alert.showAndWait();
            return;
        }
        try {
            String newComment = updatecontaner.getText();
            for (Comment cm : cmd.afficherAll()) {
                if (cm.getId() == commentId) {
                    cm.setDescription(newComment);
                    cm.setDate(LocalDate.now());
                    productId= cm.getId_prod();
                    cmd.modifier(cm);
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle this exception according to your application's error handling strategy
        }

        // Return to the product details screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductDetails.fxml"));
        try {
            Parent root = loader.load();
            ProductDetailsController productDetailsController = loader.getController();
            productDetailsController.receiveData(productId);

            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace(); // Handle this exception according to your application's error handling strategy
        }

    }
    public void initData(int commentId, String commentText) {
        this.commentId = commentId; // Set the comment ID
        this.updatecontaner.setText(commentText); // Set the existing comment text in TextArea
    }
}
