package Controller;

import Entity.Comment;
import Service.CommentCRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SampleCommantController implements Initializable {
    private CommentCRUD pc=new CommentCRUD();
    @FXML
    private Label date;

    @FXML
    private Label desc;

    @FXML
    private ImageView img;

    @FXML
    private Label name;
    @FXML
    private Button detbtn;

    @FXML
    private Button updatebtn;

    private Runnable refreshCommentsCallback; // To refresh comments

    public void setRefreshCommentsCallback(Runnable callback) {
        this.refreshCommentsCallback = callback;
    }
    @FXML
    void delcommant(ActionEvent event) {
        int commentId = (Integer) detbtn.getUserData(); // Retrieve the stored comment ID
        try {
            Comment commentToDelete = new Comment();
            commentToDelete.setId(commentId); // Set only ID for deletion
            pc.supprimer(commentToDelete); // Call the delete method
            // Refresh the comments display if deletion was successful
            if (refreshCommentsCallback != null) {
                refreshCommentsCallback.run();
            }
            // Optionally refresh the display or remove the element from the GUI
        } catch (SQLException e) {
            System.out.println("Error deleting comment: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    void updatecommant(ActionEvent event) {
        Button btn = (Button) event.getSource();
        int commentId = (Integer) btn.getUserData();  // Retrieve the stored comment ID

        try {
            // Load the UpdateCommmant.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateCommmant.fxml"));
            Parent root = loader.load();

            // Get the controller and set the comment ID and existing comment text
            UpdateCommmantController controller = loader.getController();
            controller.initData(commentId, desc.getText()); // Pass comment ID and existing comment text
            Scene scene = detbtn.getScene();
            System.out.println(detbtn.getScene());
            scene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void setData(Comment comment){
        desc.setText(comment.getDescription());
        date.setText(comment.getDate().toString()); // Make sure date is formatted as needed
        detbtn.setUserData(comment.getId());
        updatebtn.setUserData(comment.getId());
        // img.setImage(new Image("path/to/image")); // Set image if applicable
        // name.setText("User Name"); // Set this if you have user data
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    private ArrayList<Comment>comments;

    {
        try {
            comments = pc.afficherAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
