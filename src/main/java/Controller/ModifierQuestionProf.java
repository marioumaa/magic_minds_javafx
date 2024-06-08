package Controller;

import Entity.Questions;
import Service.QuestionsCrud;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifierQuestionProf {

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
    private Button fxUpdatequestion;
    private final QuestionsCrud qc=new QuestionsCrud();
    private Questions QuestionToModify=new Questions();
    @FXML
    void UpdateQuestion(ActionEvent event) {
        if (isInputValid()) {
            try {
                // Mettre à jour les données de la borne avec les nouvelles valeurs
                QuestionToModify.setQuestion(fxQuestion.getText());
                QuestionToModify.setChoix1(fxChoice1.getText());
                QuestionToModify.setChoix2(fxChoice2.getText());
                QuestionToModify.setChoix3(fxChoice3.getText());
                QuestionToModify.setReponse(fxCorrectanswer.getText());
                QuestionToModify.setIdQuiz(Integer.parseInt(fxQuizid.getText()));


                // Appeler la méthode de mise à jour dans le service de borne
                qc.modifier(QuestionToModify);
                Stage stage = (Stage) fxQuestion.getScene().getWindow();
                stage.close();
                // Afficher une alerte pour confirmer la modification
                showAlert("Modification réussie", "La borne a été modifiée avec succès.");

            } catch (Exception e) {
                showAlert("Erreur", "Une erreur s'est produite lors de la modification de la borne.");
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


        // Ajoutez des conditions similaires pour les autres champs...

        return true;
    }
    public void setQuestionToModify(Questions question) {
        this.QuestionToModify = question;
        // Définir les champs avec les données du quiz
        fxQuestion.setText(question.getQuestion());
        fxChoice1.setText(question.getChoix1());
        fxChoice2.setText(question.getChoix2());
        fxChoice3.setText(question.getChoix3());
        fxCorrectanswer.setText(question.getReponse());
        fxQuizid.setText(Integer.toString(question.getIdQuiz()));

    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
