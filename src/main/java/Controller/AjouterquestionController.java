package Controller;

import Entity.User;
import Entity.question;
import Entity.reponse;
import Service.SessionManager;
import Service.questionService;
import Service.reponseService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AjouterquestionController implements Initializable {

    @FXML
    private TextField descriptionevField;
    @FXML
    private ComboBox<String> typecb;
    @FXML
    private DatePicker dateevField;
    @FXML
    private TextField imageevField;
    @FXML
    private TextField nameField;
    @FXML
    private TableView<question> questionTv;
    @FXML
    private TableColumn<question, String> nomevTv;
    @FXML
    private TableColumn<question, String> imageevTv;
    @FXML
    private TableColumn<question, String> dateevTv;
    @FXML
    private TableColumn<question, String> descriptionevTv;
    @FXML
    private TableColumn<question, String> typeTv;
    @FXML
    private ImageView imageview;
    @FXML
    private TextField rechercher;
    @FXML
    private ImageView captchaImageView;
    @FXML
    private TextField captchaInput;

    private DefaultKaptcha kaptchaProducer;
    private String correctCaptchaAnswer;
    private questionService Ev = new questionService();
    reponseService Pservice = new reponseService();
    String userDir = System.getProperty("user.dir");
    String trimmedPath = userDir.substring(0, userDir.lastIndexOf("\\"));
    File UploadDirectory = new File(trimmedPath+"/Magic-Minds-haifa/public/uploads/images");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        typecb.setItems(FXCollections.observableArrayList("eleve", "parent"));
        typecb.getSelectionModel().selectFirst();
        initCaptchaProducer();
        refreshCaptcha();
        getevs();
    }

    private void initCaptchaProducer() {
        kaptchaProducer = new DefaultKaptcha();
        Properties props = new Properties();
        props.setProperty("kaptcha.border", "yes");
        props.setProperty("kaptcha.textproducer.font.color", "black");
        props.setProperty("kaptcha.image.width", "250");
        props.setProperty("kaptcha.image.height", "100");
        props.setProperty("kaptcha.textproducer.font.size", "40");
        kaptchaProducer.setConfig(new Config(props));
    }

    private void refreshCaptcha() {
        correctCaptchaAnswer = kaptchaProducer.createText();
        BufferedImage bi = kaptchaProducer.createImage(correctCaptchaAnswer);
        Image fxImage = SwingFXUtils.toFXImage(bi, null);
        captchaImageView.setImage(fxImage);
    }
    private boolean NoDate() {
        LocalDate currentDate = LocalDate.now();
        LocalDate myDate = dateevField.getValue();
        int comparisonResult = myDate.compareTo(currentDate);
        boolean test = true;
        if (comparisonResult <= 0) {
            // myDate est antérieure à currentDate
            test = true;
        } else if (comparisonResult > 0) {
            // myDate est postérieure à currentDate
            test = false;
        }
        return test;
    }

    @FXML
    private void ajouterquestion(ActionEvent ev) {
        if (!captchaInput.getText().equals(correctCaptchaAnswer)) {
            Alert captchaAlert = new Alert(Alert.AlertType.ERROR);
            captchaAlert.setTitle("CAPTCHA Error");
            captchaAlert.setHeaderText("Incorrect CAPTCHA");
            captchaAlert.setContentText("Please solve the CAPTCHA correctly.");
            captchaAlert.showAndWait();
            refreshCaptcha();
            return;
        }

        if ((nameField.getText().length() == 0) || (imageevField.getText().length() == 0) ||
                (descriptionevField.getText().length() == 0) || (typecb.getValue() == null)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText("Error!");
            alert.setContentText("Fields cannot be empty");
            alert.showAndWait();
        } else if (NoDate()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText("Error!");
            alert.setContentText("la date de date doit être après la date d'aujourd'hui");
            alert.showAndWait();
        } else {
            question e = new question();
            e.setName(nameField.getText());
            e.setCommentaire(descriptionevField.getText());
            e.setType(typecb.getValue());

            java.util.Date date_debut = java.util.Date.from(dateevField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date sqlDate = new Date(date_debut.getTime());
            e.setDate(sqlDate);
            e.setImage(imageevField.getText());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information ");
            alert.setHeaderText("Question Added");
            alert.setContentText("Question added successfully!");
            alert.showAndWait();

            try {
                Ev.ajouterquestion(e);
                reset();
                getevs();
            } catch (Exception ex) {
                Alert dbAlert = new Alert(Alert.AlertType.ERROR);
                dbAlert.setTitle("Database Error");
                dbAlert.setHeaderText("Failed to Add Question");
                dbAlert.setContentText("Error: " + ex.getMessage());
                dbAlert.showAndWait();
            }
        }
    }

    private void reset() {
        nameField.setText("");
        descriptionevField.setText("");
        imageevField.setText("");
        dateevField.setValue(null);
        typecb.getSelectionModel().selectFirst();
        refreshCaptcha();
    }

    private void getevs() {
        try {
            List<question> questions = Ev.recupererquestion();
            ObservableList<question> olQuestions = FXCollections.observableArrayList(questions);
            questionTv.setItems(olQuestions);
            nomevTv.setCellValueFactory(new PropertyValueFactory<>("name"));
            imageevTv.setCellValueFactory(new PropertyValueFactory<>("image"));
            dateevTv.setCellValueFactory(new PropertyValueFactory<>("date"));
            descriptionevTv.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
            typeTv.setCellValueFactory(new PropertyValueFactory<>("type"));
        } catch (Exception ex) {
            System.out.println("Error in getevs: " + ex.getMessage());
        }
    }

    @FXML
    private void modifierquestion(ActionEvent ev) {
        if (questionTv.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a question from the table to modify.");
            alert.showAndWait();
            return;
        }

        // Get the selected question
        question selectedQuestion = questionTv.getSelectionModel().getSelectedItem();

        // Update the selected question's properties with the fields' values
        selectedQuestion.setName(nameField.getText());
        selectedQuestion.setCommentaire(descriptionevField.getText());
        selectedQuestion.setType(typecb.getValue());
        selectedQuestion.setImage(imageevField.getText());

        // If the date is needed to be updated too
        if (dateevField.getValue() != null) {
            java.util.Date date = java.util.Date.from(dateevField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date sqlDate = new Date(date.getTime());
            selectedQuestion.setDate(sqlDate);
        }

        try {
            Ev.modifierquestion(selectedQuestion);  // Assuming 'modifierquestion' handles the SQL update
            reset();
            getevs();  // Refresh the table view
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Question Updated");
            alert.setHeaderText(null);
            alert.setContentText("The question has been updated successfully.");
            alert.showAndWait();
        } catch (SQLException ex) {
            Logger.getLogger(AjouterquestionController.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Error Modifying Question");
            alert.setContentText("The question could not be updated due to a database error:\n" + ex.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    private void supprimerquestion(ActionEvent ev) {
        question e = questionTv.getItems().get(questionTv.getSelectionModel().getSelectedIndex());
        try {
            Ev.supprimerquestion(e);
        } catch (SQLException ex) {
            Logger.getLogger(AjouterquestionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information ");
        alert.setHeaderText("question delete");
        alert.setContentText("question deleted successfully!");
        alert.showAndWait();
        getevs();
    }

    @FXML
    private void afficherquestion(ActionEvent ev) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/demo4/afficherquestion.fxml")));
            nameField.getScene().setRoot(loader);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    private void choisirev(MouseEvent ev) throws IOException {
        question selectedQuestion = questionTv.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            nameField.setText(selectedQuestion.getName());
            imageevField.setText(selectedQuestion.getImage());
            descriptionevField.setText(selectedQuestion.getCommentaire());
            typecb.setValue(selectedQuestion.getType());
            if (selectedQuestion.getDate() != null) {
                LocalDate localDate = selectedQuestion.getDate().toLocalDate();
                dateevField.setValue(localDate);
            }

            if (selectedQuestion.getImage() != null && !selectedQuestion.getImage().isEmpty()) {
                File file = new File(selectedQuestion.getImage());
                if (file.exists()) {
                    Image img = new Image(file.toURI().toString());
                    imageview.setImage(img);
                } else {
                    imageview.setImage(null); // or set to a default image
                }
            }
        }
    }


    private void repondre(ActionEvent ev) {

        User u = new User();

        reponse p = new reponse();

        //p.setquestion();

        p.setId_user(u.getId());
        Pservice.ajouterreponse(p);
        try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("afficherreponse.fxml")));
            nameField.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void afficherreponses(ActionEvent ev) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/demo4/afficherreponse.fxml")));
            nameField.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void uploadImage(ActionEvent ev) throws FileNotFoundException, IOException {
        Random rand = new Random();
        int x = rand.nextInt(1000);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File Path");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File file = fileChooser.showOpenDialog(null);
        String FileName = file.getName().substring(file.getName().lastIndexOf('.'));
        // Adjust the path to store images inside the project's resources directory
        String relativePath = "src/main/resources/demo4/images/" + x + FileName;

        if (file != null) {
            FileInputStream sourceStream = new FileInputStream(file.getAbsolutePath());
            FileOutputStream destinationStream = new FileOutputStream(relativePath);
            BufferedInputStream bin = new BufferedInputStream(sourceStream);
            BufferedOutputStream bout = new BufferedOutputStream(destinationStream);

            System.out.println("File path: " + file.getAbsolutePath());
            Image img = new Image(file.toURI().toString());
            imageview.setImage(img);
            imageevField.setText(x + FileName);

            int byteContent;
            while ((byteContent = bin.read()) != -1) {
                bout.write(byteContent);
            }
            bin.close();
            bout.close();

            // You might need to adjust this part to work with your specific database setup
            // For example: insertFileNameIntoDatabase(file.getName(), relativePath);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Image uploaded successfully!");
            alert.showAndWait();
        } else {
            System.out.println("No file selected");
        }
    }


    @FXML
    private void rechercherev(KeyEvent ev) {

        questionService bs = new questionService();
        question b = new question();
        ObservableList<question> filter = bs.chercherev(rechercher.getText());
        populateTable(filter);
    }

    private void populateTable(ObservableList<question> branlist) {
        questionTv.setItems(branlist);

    }

    @FXML
    void goCourse(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CoursesEnfant.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenementEtudiant.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/demo4/Home_front.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherAllQuizzesEnfant.fxml"));
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
    public void Login1() throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("/loginController.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

    }
    @FXML
    void logout(MouseEvent event) {
        SessionManager sessionManager= SessionManager.getInstance();
        sessionManager.endSession();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        try {
            Login1();
        } catch (IOException e) {
            System.out.println(e.getMessage());
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
