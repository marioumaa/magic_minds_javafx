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

public class AjouterQuestion {
QuestionsCrud qc=new QuestionsCrud();
    @FXML
    private Button tfAddQuestion;

    @FXML
    private TextField tfChoix1;

    @FXML
    private TextField tfChoix2;

    @FXML
    private TextField tfChoix3;

    @FXML
    private TextField tfQuestion;

    @FXML
    private TextField tfQuizId;

    @FXML
    private TextField tfReponse;

    @FXML
    void AddQuestion(ActionEvent event) throws SQLException {

        if (isInputValid()) {
            String question = tfQuestion.getText();
            String choix1 = tfChoix1.getText();
            String choix2 = tfChoix2.getText();
            String choix3 = tfChoix3.getText();
            String reponse = tfReponse.getText();
            int idQuiz = Integer.parseInt(tfQuizId.getText());

            QuestionsCrud questions=new QuestionsCrud();

            Questions q=new Questions(question,choix1,choix2,choix3,reponse,idQuiz);
          questions.ajouter(q);

            System.out.println("ajouté avec succés");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherQuestion.fxml"));
            try {
                Parent root = loader.load();
                tfQuestion.getScene().setRoot(root);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    private boolean isInputValid() {
        if (tfQuestion.getText().isEmpty() || tfChoix1.getText().isEmpty() || tfChoix2.getText().isEmpty() || tfChoix3.getText().isEmpty() || tfReponse.getText().isEmpty() || tfQuizId.getText().isEmpty() ) {
            showAlert("Error", "Enter a valid content !");
            return false;
        }

        if (tfQuestion.getText().matches("[0-9]+")) {
            showAlert("Error", "Question must be string not number !");
            return false;
        }

        String question = tfQuestion.getText();
        if (qc.questionExisteDeja(question)) {
            showAlert("Error", "Question already exists! Please choose a different one.");
            return false;
        }

        if (tfChoix1.getText().matches("[0-9]+")) {
            showAlert("Error", "Choice 1 must be string not number !");
            return false;
        }
        if (tfChoix2.getText().matches("[0-9]+")) {
            showAlert("Error", "Choice 2 must be string not number !");
            return false;
        }
        if (tfChoix3.getText().matches("[0-9]+")) {
            showAlert("Error", "Choice 3 must be string not number !");
            return false;
        }

        String choix1 = tfChoix1.getText();
        String choix2 = tfChoix2.getText();
        String choix3 = tfChoix3.getText();

        // Vérification que les choix sont différents les uns des autres
        if (choix1.equals(choix2) || choix1.equals(choix3) || choix2.equals(choix3)) {
            showAlert("Error", "Choices must be different from each other!");
            return false;
        }

        String reponse = tfReponse.getText();

        if (tfReponse.getText().matches("[0-9]+")) {
            showAlert("Error", "Correct answer must be string not number !");
            return false;
        }
        if (!reponse.equals(choix1) && !reponse.equals(choix2) && !reponse.equals(choix3)) {
            showAlert("Error", "Answer must be one of the choices!");
            return false;
        }



        if (tfQuizId.getText().matches("[A-Z]+") || tfQuizId.getText().matches("[a-z]+")) {
            showAlert("Error", "Quiz Id must be numeric !");
            return false;
        }


        // Ajoutez des conditions similaires pour les autres champs...

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
                .getResource("/AfficherQuestion.fxml"));
        try {
            Parent root = loader.load();

            tfQuestion.getScene().setRoot(root);

        } catch (IOException e) {

            System.out.println(e.getMessage());
        }
    }

}
