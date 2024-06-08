package Controller;

import Entity.Quiz;
import Service.QuizCrud;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifierquizProf {

    @FXML
    private TextField fxNbquestion;

    @FXML
    private TextField fxTime;

    @FXML
    private TextField fxTitle;

    @FXML
    private Button fxUpdateQuiz;
    private final QuizCrud qc = new QuizCrud();
    private Quiz QuizToModify =new Quiz();
    @FXML
    void UpdateQuiz(ActionEvent event) {
        if (isInputValid()) {
            try {
                // Mettre à jour les données de la borne avec les nouvelles valeurs
                QuizToModify.setTitre(fxTitle.getText());
                QuizToModify.setTemp(Integer.parseInt(fxTime.getText()));
                QuizToModify.setNb_question(Integer.parseInt(fxNbquestion.getText()));


                // Appeler la méthode de mise à jour dans le service de borne
                qc.modifier(QuizToModify);
                Stage stage = (Stage) fxTitle.getScene().getWindow();
                stage.close();

                // Afficher une alerte pour confirmer la modification
                showAlert("update succeeded", "The quiz is successfully updated.");

            } catch (Exception e) {
                showAlert("Error", "Error.");
            }
        }
    }

    private boolean isInputValid() {
        // Vérification des champs vides
        if (fxTitle.getText().isEmpty() || fxTime.getText().isEmpty() || fxNbquestion.getText().isEmpty()) {
            showAlert("Error", "Enter a valid content !");
            return false;
        }

        // Vérification si le titre est un nombre
        if (fxTitle.getText().matches("[0-9]+")) {
            showAlert("Error", "Title must be a string, not a number !");
            return false;
        }

        // Vérification si le temps est numérique et compris entre 20 et 30
        try {
            int temps = Integer.parseInt(fxTime.getText());
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
            int nbQuestions = Integer.parseInt(fxNbquestion.getText());
            if (nbQuestions < 5 || nbQuestions > 10) {
                showAlert("Error", "Number of questions must be between 5 and 10 !");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Number of questions must be a numeric value !");
            return false;
        }

        //  vérifier si le titre est unique
        String titre = fxTitle.getText();
        if (qc.titreExisteDeja(titre)) {
            showAlert("Error", "Title already exists! Please choose a different one.");
            return false;
        }
        return true;
    }

    public void setQuizToModify(Quiz quiz) {
        this.QuizToModify = quiz;
        // Définir les champs avec les données du quiz
        fxTitle.setText(quiz.getTitre());
        fxTime.setText(Integer.toString(quiz.getTemp()));
        fxNbquestion.setText(Integer.toString(quiz.getNb_question()));
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }








    }


