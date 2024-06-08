package Controller;

import Entity.Quiz;
import Service.QuizCrud;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterQuiz {
    QuizCrud qc=new QuizCrud();
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button fxAdd;

    @FXML
    private TextField tfNbbu;

    @FXML
    private TextField tfTemps;

    @FXML
    private TextField tfTitre;

    @FXML
    void addQuiz(ActionEvent event) throws SQLException{


            if (isInputValid()) {
                String titre = tfTitre.getText();
                int temp = Integer.parseInt(tfTemps.getText());
                int nbquestion = Integer.parseInt(tfNbbu.getText());


                QuizCrud quiz = new QuizCrud();
                QuizCrud ps = new QuizCrud();



                Quiz q = new Quiz(titre,temp,nbquestion);
                ps.ajouter(q);
                System.out.println("ajouter avec succés");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Afficher.fxml"));
                try {
                    Parent root = loader.load();
                    tfTitre.getScene().setRoot(root);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            }



    private boolean isInputValid() {
        // Vérification des champs vides
        if (tfTitre.getText().isEmpty() || tfTemps.getText().isEmpty() || tfNbbu.getText().isEmpty()) {
            showAlert("Error", "Enter a valid content !");
            return false;
        }

        // Vérification si le titre est un nombre
        if (tfTitre.getText().matches("[0-9]+")) {
            showAlert("Error", "Title must be a string, not a number !");
            return false;
        }

        // Vérification si le temps est numérique et compris entre 20 et 30
        try {
            int temps = Integer.parseInt(tfTemps.getText());
            if (temps < 20 || temps > 30) {
                showAlert("Error", "Time must be between 20 and 30 !");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Time must be a numeric value !");
            return false;
        }

        // Vérification si le nombre de questions est numérique et compris entre 10 et 15
        try {
            int nbQuestions = Integer.parseInt(tfNbbu.getText());
            if (nbQuestions < 5 || nbQuestions > 10) {
                showAlert("Error", "Number of questions must be between 5 and 10 !");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Number of questions must be a numeric value !");
            return false;
        }

        //  vérifier si le titre est unique
        String titre = tfTitre.getText();
        if (qc.titreExisteDeja(titre)) {
            showAlert("Error", "Title already exists! Please choose a different one.");
            return false;
        }
        return true;
    }

    void showAlert(String title, String contentText) {
            Alert alertType = new Alert(Alert.AlertType.ERROR);
            alertType.setTitle(title);
            alertType.setHeaderText(contentText);
            alertType.show();
            }
    @FXML
    void afficher(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/AfficherQuiz.fxml"));
        try {
            Parent root = loader.load();

            tfTitre.getScene().setRoot(root);

        } catch (IOException e) {

            System.out.println(e.getMessage());
        }
    }

}
