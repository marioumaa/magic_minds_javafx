package Controller;

import Entity.User;
import Service.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class showUserProfileController implements Initializable {

    @FXML
    private Text age;

    @FXML
    private Text email;

    @FXML
    private Text firstname;

    @FXML
    private Text gender;

    @FXML
    private Text grandName;

    @FXML
    private ImageView image1;

    @FXML
    private Text lastname;

    @FXML
    private Text role;

    @FXML
    private Text tel;
    String url;
    String userDir = System.getProperty("user.dir");
    String trimmedPath = userDir.substring(0, userDir.lastIndexOf("\\"));
    File UploadDirectory = new File(trimmedPath+"/MagicMinds/public/uploads/images");
    @FXML
    void changePasswordPage(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/UpdateUserPasswordController.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(UserManagementController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }
    void affichage() throws IOException {
        Stage stage = new Stage();
        Parent parent = FXMLLoader.load(getClass().getResource("/UserManagementController.fxml"));


        Scene scene = new Scene(parent);

        stage.setTitle("Dashboard");

        stage.initStyle(StageStyle.UTILITY);
        stage.setScene(scene);

        stage.show();
    }

    @FXML
    void updateProfilePage(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/UpdateUserProfileController.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(UserManagementController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User user = SessionManager.getCurrentUser();
        this.firstname.setText(user.getFirstName());
        this.lastname.setText(user.getLastName());
        this.email.setText(user.getEmail());
        this.tel.setText(user.getTel());
        this.age.setText(String.valueOf(user.getAge()));
        String roleName = user.getRoles().replaceAll("\\[\"ROLE_(.*?)\"\\]", "$1").toLowerCase();
        this.role.setText(roleName);
        this.gender.setText(user.getGender());

        this.url = user.getPicture();
        String imp="file:/"+trimmedPath+"/MagicMinds/public/uploads/images/"+url;
        imp.replace("\\", "/");
        Image image = new Image(imp);
        image1.setImage(image);


    }
}

