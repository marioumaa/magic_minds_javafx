package Controller;

import Entity.User;
import Service.UserService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ResourceBundle;


import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import org.mindrot.jbcrypt.BCrypt;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SaveUserController implements Initializable {

    @FXML
    private Text ageControl;

    @FXML
    private TextField ageInput;

    @FXML
    private Text emailControl;

    @FXML
    private TextField emailInput;

    @FXML
    private Text firstNameControl;

    @FXML
    private TextField firstnameInput;

    @FXML
    private Text genderControl;

    @FXML
    private ComboBox<String> genderInput;

    @FXML
    private Text lastNameControl;

    @FXML
    private TextField lastNameInput;

    @FXML
    private Text passwordControl;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Text pictureControle;

    @FXML
    private Button pictureInput;

    @FXML
    private Text telControl;

    @FXML
    private TextField telInput;

    @FXML
    private ImageView imageView;

    @FXML
    private Text urlText;

    @FXML
    private Text roleControl;


    @FXML
    private ComboBox<String> roleInput;

    private int userId;

    private boolean update;
    File selectedFile = new File("C:\\");
    String userDir = System.getProperty("user.dir");
    String trimmedPath = userDir.substring(0, userDir.lastIndexOf("\\"));
    File UploadDirectory = new File(trimmedPath+"/MagicMinds/public/uploads/images");
    File destinationFile = new File("C:\\");
    String url ;
    String url1;
    private String emailRegex = "^([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+)\\.([a-zA-Z]{2,})$";
    private String passwordRegex ="^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";


    @FXML
    void Clear(MouseEvent event) {
        ageInput.setText(null);
        firstnameInput.setText(null);
        lastNameInput.setText(null);
        passwordInput.setText(null);
        emailInput.setText(null);
        telInput.setText(null);
        genderInput.setValue(null);
        urlText.setText(null);
        imageView.setImage(null);
        url = null;

        deleteError();

    }

    void deleteError() {
        firstNameControl.setText(null);
        firstNameControl.setStyle("-fx-fill: red;");
        firstnameInput.setStyle("-fx-border-color: #353A56 ;");

        lastNameControl.setText(null);
        lastNameControl.setStyle("-fx-fill: red;");
        lastNameInput.setStyle("-fx-border-color: #353A56 ;");

        passwordControl.setText(null);
        passwordControl.setStyle("-fx-fill: red;");
        passwordInput.setStyle("-fx-border-color: #353A56;");

        emailControl.setText(null);
        emailControl.setStyle("-fx-fill: red;");
        emailInput.setStyle("-fx-border-color: #353A56 ;");

        telControl.setText(null);
        telControl.setStyle("-fx-fill: red;");
        telInput.setStyle("-fx-border-color: #353A56;");

        ageControl.setText(null);
        ageControl.setStyle("-fx-fill: red;");
        ageInput.setStyle("-fx-border-color:#353A56 ;");

        pictureControle.setText(null);
        pictureControle.setStyle("-fx-fill: red;");
        pictureInput.setStyle("-fx-background-color: #007bff ;");

        genderControl.setText(null);
        genderControl.setStyle("-fx-fill: red;");
        genderInput.setStyle("-fx-border-color: #353A56 ;");

        roleControl.setText(null);
        roleControl.setStyle("-fx-fill: red;");
        roleInput.setStyle("-fx-border-color: #353A56 ;");
    }

    void emtyError() {
        firstNameControl.setText("Please fill in this field.");
        firstNameControl.setStyle("-fx-fill: red;");
        firstnameInput.setStyle("-fx-border-color: red ;");

        lastNameControl.setText("Please fill in this field.");
        lastNameControl.setStyle("-fx-fill: red;");
        lastNameInput.setStyle("-fx-border-color: red ;");

        passwordControl.setText("Please fill in this field.");
        passwordControl.setStyle("-fx-fill: red;");
        passwordInput.setStyle("-fx-border-color: red;");

        emailControl.setText("Please fill in this field.");
        emailControl.setStyle("-fx-fill: red;");
        emailInput.setStyle("-fx-border-color: red ;");

        telControl.setText("Please fill in this field.");
        telControl.setStyle("-fx-fill: red;");
        telInput.setStyle("-fx-border-color: red;");

        ageControl.setText("Please fill in this field.");
        ageControl.setStyle("-fx-fill: red;");
        ageInput.setStyle("-fx-border-color:red;");

        pictureControle.setText("Please fill in this field.");
        pictureControle.setStyle("-fx-fill: red;");
        pictureInput.setStyle("-fx-background-color: red ;");

        genderControl.setText("Please fill in this field.");
        genderControl.setStyle("-fx-fill: red;");
        genderInput.setStyle("-fx-border-color: red ;");

        roleControl.setText("Please fill in this field.");
        roleControl.setStyle("-fx-fill: red;");
        roleInput.setStyle("-fx-border-color: red ;");
    }

    @FXML
    void Close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();




    }

    void affichage() throws IOException {
        Stage stage = new Stage();
        Parent parent = FXMLLoader.load(getClass().getResource("/UserManagementController.fxml"));


        Scene scene = new Scene(parent);

        stage.setTitle("Dashboard");

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);

        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderInput.setItems(FXCollections.observableArrayList("male", "female"));
        roleInput.setItems(FXCollections.observableArrayList("admin", "child", "professor", "parent"));
    }

    @FXML
    void UploadImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG Image", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG Image", "*.png"),
                new FileChooser.ExtensionFilter("All image files", "*.jpg", "*.png")
        );
        selectedFile = fileChooser.showOpenDialog(pictureInput.getScene().getWindow());
        if (selectedFile != null) {
            String pathName = selectedFile.getAbsolutePath().replace("\\", "/");
            System.out.println(pathName);
            Image image = new Image("file:/" + pathName);
            imageView.setImage(image);
            destinationFile = new File(UploadDirectory, selectedFile.getName());
            url1 = UploadDirectory.getAbsolutePath().replace("\\", "/");
            url1 += "/" + selectedFile.getName();
            System.out.println(url1);
            urlText.setText(url1);
            url=selectedFile.getName();

        }
    }

    @FXML
    void Save(MouseEvent event) {
        UserService service = new UserService();

        String FIRSTNAME = firstnameInput.getText();
        String lastNAME = lastNameInput.getText();
        String PASSWORD = passwordInput.getText();
        String EMAIL = emailInput.getText();
        String TEL = telInput.getText();
        String GENDER = genderInput.getValue();
        String age = ageInput.getText();
        String ROLE = roleInput.getValue();
        if ((FIRSTNAME == null) || (lastNAME == null) || (PASSWORD == null)
                || (EMAIL == null) || (TEL == null) || (age == null)) {
            emtyError();

        } else if ((FIRSTNAME.isBlank()) || (lastNAME.isBlank()) || (PASSWORD.isEmpty())
                || (EMAIL.isEmpty()) || (TEL.isEmpty()) || (age.isEmpty())) {
//          Alert alertType = new Alert(Alert.AlertType.ERROR);
//          alertType.setTitle("Error");
//          alertType.setHeaderText("Enter a valid content !");
//          alertType.show();
            emtyError();

        } else if (FIRSTNAME.toString().matches("[0-9]+")) {
            firstNameControl.setText(" first name must be string not number !");
            firstNameControl.setStyle("-fx-fill: red;");
            firstnameInput.setStyle("-fx-border-color: red ;");

        } else if (lastNAME.toString().matches("[0-9]+")) {
            lastNameControl.setText(" last name must be string not number !");
            lastNameControl.setStyle("-fx-fill: red;");
            lastNameInput.setStyle("-fx-border-color: red ;");
        } else if (PASSWORD.toString().length() < 8) {
            passwordControl.setText(" Your password must be at least 8 characters long!");
            passwordControl.setStyle("-fx-fill: red;");
            passwordInput.setStyle("-fx-border-color: red ;");
        } else if (!EMAIL.toString().matches(emailRegex)) {
            emailControl.setText("The email address is invalid. Please enter a valid email address (e.g., username@example.com)!");
            emailControl.setStyle("-fx-fill: red;");
            emailInput.setStyle("-fx-border-color: red ;");

        } else if ((TEL.matches("[A-Z]+")) || (TEL.matches("[a-z]+"))) {
            telControl.setText("The telephone  msut be a number!");
            telControl.setStyle("-fx-fill: red;");
            telInput.setStyle("-fx-border-color: red ;");

        } else if ((age.matches("[A-Z]+")) || (age.matches("[a-z]+"))) {
            ageControl.setText("The telephone  msut be a number!");
            ageControl.setStyle("-fx-fill: red;");
            ageInput.setStyle("-fx-border-color: red ;");

        } else if (url == null) {
            pictureControle.setText("The picture is emty !");
            pictureControle.setStyle("-fx-fill: red;");
            pictureInput.setStyle("-fx-background-color: red ;");
        } else if (GENDER.isEmpty()) {
            genderControl.setText("The gender is emty !");
            genderControl.setStyle("-fx-fill: red;");
            genderInput.setStyle("-fx-border-color: red ;");
        } else if (ROLE.isEmpty()) {
            roleControl.setText("The role is emty !");
            roleControl.setStyle("-fx-fill: red;");
            roleInput.setStyle("-fx-border-color: red ;");

        } else {
            if (update == true) {
                String newhashedPassword = BCrypt.hashpw(PASSWORD, BCrypt.gensalt());
                User user = new User(userId, Integer.parseInt(age), FIRSTNAME, lastNAME, EMAIL, TEL, GENDER, newhashedPassword, url, ROLE);
                System.out.println(userId);
                try {
                    service.update(user, userId);
                    try {
                        Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Alert alertType = new Alert(Alert.AlertType.INFORMATION);
                    alertType.setTitle("Sucess");
                    alertType.setHeaderText("update success ");
                    alertType.show();
                } catch (SQLException ex) {
                    Alert alertType = new Alert(Alert.AlertType.ERROR);
                    alertType.setTitle("Error");
                    alertType.setHeaderText("echec");
                    alertType.show();
                    System.out.println(ex.getMessage());
                }

            } else {
                String newhashedPassword = BCrypt.hashpw(PASSWORD, BCrypt.gensalt());
                User user = new User(Integer.parseInt(age), FIRSTNAME, lastNAME, EMAIL, TEL, GENDER, newhashedPassword, url, ROLE);
                try {
                    service.insert(user);
                    try {
                        Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Alert alertType = new Alert(Alert.AlertType.INFORMATION);
                    alertType.setTitle("Sucess");
                    alertType.setHeaderText("add success ");
                    alertType.show();
                } catch (SQLException e) {
                    Alert alertType = new Alert(Alert.AlertType.ERROR);
                    alertType.setTitle("Error");
                    alertType.setHeaderText("echec");
                    alertType.show();
                    System.out.println(e.getMessage());
                }
            }
        }

    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    void setTextField(int userId, String firstname, String lastname, String email, String tel, String gender, int age, String role, String url) {
        this.userId = userId;
        firstnameInput.setText(firstname);
        lastNameInput.setText(lastname);
        emailInput.setText(email);
        telInput.setText(tel);
        ageInput.setText(String.valueOf(age));
        String roleName = role.replaceAll("\\[\"ROLE_(.*?)\"\\]", "$1").toLowerCase();
        roleInput.setValue(roleName);
        genderInput.setValue(gender);
        urlText.setText(url);
        this.url = url;
        String imp="file:/"+trimmedPath+"/MagicMinds/public/uploads/images/"+url;
        imp.replace("\\", "/");
        Image image = new Image(imp);
        imageView.setImage(image);


    }
}

