package Controller;

import Entity.Questions;
import Service.QuestionsCrud;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
public class AjouterQuestionProf {
    QuestionsCrud qc=new QuestionsCrud();
    @FXML
    private Button fxAddquestion;

    @FXML
    private TextField fxChoice1;

    @FXML
    private TextField fxChoice2;

    @FXML
    private TextField fxChoice3;

    @FXML
    private TextField fxCorrectanswer;

    @FXML
    private TextField fxQuestion;

    @FXML
    private TextField fxQuizid;

    @FXML
    void Addquestion(ActionEvent event) throws SQLException {


            if (isInputValid()) {
                String question = fxQuestion.getText();
                String choix1 = fxChoice1.getText();
                String choix2 = fxChoice2.getText();
                String choix3 = fxChoice3.getText();
                String reponse = fxCorrectanswer.getText();
                int idQuiz = Integer.parseInt(fxQuizid.getText());

                QuestionsCrud questions=new QuestionsCrud();

                Questions q=new Questions(question,choix1,choix2,choix3,reponse,idQuiz);
                questions.ajouter(q);

                System.out.println("ajouté avec succés");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherQuestionProf.fxml"));
                try {
                    Parent root = loader.load();
                    fxQuestion.getScene().setRoot(root);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

    private boolean isInputValid() {
        if (fxQuestion.getText().isEmpty() || fxChoice1.getText().isEmpty() || fxChoice2.getText().isEmpty() || fxChoice3.getText().isEmpty() || fxCorrectanswer.getText().isEmpty() || fxQuizid.getText().isEmpty() ) {
            showAlert("Error", "Enter a valid content !");
            return false;
        }

        if (fxQuestion.getText().matches("[0-9]+")) {
            showAlert("Error", "Question must be string not number !");
            return false;
        }

        String question = fxQuestion.getText();
        if (qc.questionExisteDeja(question)) {
            showAlert("Error", "Question already exists! Please choose a different one.");
            return false;
        }

        if (fxChoice1.getText().matches("[0-9]+")) {
            showAlert("Error", "Choice 1 must be string not number !");
            return false;
        }
        if (fxChoice2.getText().matches("[0-9]+")) {
            showAlert("Error", "Choice 2 must be string not number !");
            return false;
        }
        if (fxChoice3.getText().matches("[0-9]+")) {
            showAlert("Error", "Choice 3 must be string not number !");
            return false;
        }

        String choix1 = fxChoice1.getText();
        String choix2 = fxChoice2.getText();
        String choix3 = fxChoice3.getText();

        // Vérification que les choix sont différents les uns des autres
        if (choix1.equals(choix2) || choix1.equals(choix3) || choix2.equals(choix3)) {
            showAlert("Error", "Choices must be different from each other!");
            return false;
        }

        String reponse = fxCorrectanswer.getText();

        if (fxCorrectanswer.getText().matches("[0-9]+")) {
            showAlert("Error", "Correct answer must be string not number !");
            return false;
        }
        if (!reponse.equals(choix1) && !reponse.equals(choix2) && !reponse.equals(choix3)) {
            showAlert("Error", "Answer must be one of the choices!");
            return false;
        }



        if (fxQuizid.getText().matches("[A-Z]+") || fxQuizid.getText().matches("[a-z]+")) {
            showAlert("Error", "Quiz Id must be numeric !");
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
                .getResource("/AfficherQuestionProf.fxml"));
        try {
            Parent root = loader.load();

            fxQuestion.getScene().setRoot(root);

        } catch (IOException e) {

            System.out.println(e.getMessage());
        }
    }

}
