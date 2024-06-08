package Controller;

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

public class ShowUserDetailsController implements Initializable {

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
    private ImageView image;

    @FXML
    private Text lastname;

    @FXML
    private Text tel;

    @FXML
    private Text role;
     String url;
    String userDir = System.getProperty("user.dir");
    String trimmedPath = userDir.substring(0, userDir.lastIndexOf("\\"));
    File UploadDirectory = new File(trimmedPath+"/MagicMinds/public/uploads/images");

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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    void setTextField( String firstname, String lastname, String email, String tel, String gender, int age, String role, String url) {
     this.grandName.setText(firstname+" "+lastname);
        this.firstname.setText(firstname);
        this.lastname.setText(lastname);
        this.email.setText(email);
        this.tel.setText(tel);
        this.age.setText(String.valueOf(age));
        String roleName = role.replaceAll("\\[\"ROLE_(.*?)\"\\]", "$1").toLowerCase();
        this.role.setText(roleName);
        this.gender.setText(gender);

        this.url = url;
        String imp="file:/"+trimmedPath+"/MagicMinds/public/uploads/images/"+url;
        imp.replace("\\", "/");
        Image image1 = new Image(imp);
        image.setImage(image1);


    }
}
