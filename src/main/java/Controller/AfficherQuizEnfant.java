package Controller;

import Entity.Evaluation;
import Entity.Questions;
import Service.EvaluationCrud;
import Service.QuestionsCrud;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AfficherQuizEnfant {
    @FXML
    private Button fxRedirectionButton;
    @FXML
    private Label fxScoreLabel;

    @FXML
    private RadioButton fxChoice1;

    @FXML
    private RadioButton fxChoice2;

    @FXML
    private RadioButton fxChoice3;

    @FXML
    private Button fxNext;

    @FXML
    private TextField fxQuestion;

    @FXML
    private Button fxSubmit;

private final EvaluationCrud evaluationCrud=new EvaluationCrud();
    private List<Questions> questions;
    private int currentQuestionIndex = 0;
    private Map<Integer, String> reponses = new HashMap<>(); // Pour stocker les réponses sélectionnées

    private QuestionsCrud questionsCrud = new QuestionsCrud();
    // Assurez-vous que la variable quizId est définie dans la classe
    private int quizId;
    private int userId;
    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Assurez-vous que la méthode setQuizId est correctement définie



    public void initialize() {


        try {
            questions = questionsCrud.recupererParQuizId(quizId);
            if (!questions.isEmpty()) {
                afficherQuestionCourante();
            } else {
                System.out.println("Aucune question trouvée pour ce quiz.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    void Next(ActionEvent event) {
        if (currentQuestionIndex < questions.size() -1) {
            // Enregistrer la réponse sélectionnée
            enregistrerReponse();
            currentQuestionIndex++;
            afficherQuestionCourante();
        } else {
            // Afficher les réponses sélectionnées dans la console une fois que toutes les questions ont été parcourues
            afficherReponses();
        }
    }

    private void afficherQuestionCourante() {
        Questions questionCourante = questions.get(currentQuestionIndex);
        fxQuestion.setText(questionCourante.getQuestion());
        fxChoice1.setText(questionCourante.getChoix1());
        fxChoice2.setText(questionCourante.getChoix2());
        fxChoice3.setText(questionCourante.getChoix3());

        // Désélectionner tous les boutons radio
        fxChoice1.setSelected(false);
        fxChoice2.setSelected(false);
        fxChoice3.setSelected(false);
    }

    @FXML
    void choixSelectionne(ActionEvent event) {
        // Désélectionner tous les autres boutons radio seulement si l'événement est lié à une sélection
        if (event.getSource() instanceof RadioButton) {
            RadioButton selectedRadioButton = (RadioButton) event.getSource();
            if (selectedRadioButton.isSelected()) {
                if (selectedRadioButton.equals(fxChoice1)) {
                    fxChoice2.setSelected(false);
                    fxChoice3.setSelected(false);
                } else if (selectedRadioButton.equals(fxChoice2)) {
                    fxChoice1.setSelected(false);
                    fxChoice3.setSelected(false);
                } else if (selectedRadioButton.equals(fxChoice3)) {
                    fxChoice1.setSelected(false);
                    fxChoice2.setSelected(false);
                }
            }
        }
    }

    private void enregistrerReponse() {
        if (fxChoice1.isSelected()) {
            reponses.put(currentQuestionIndex, fxChoice1.getText());
        } else if (fxChoice2.isSelected()) {
            reponses.put(currentQuestionIndex, fxChoice2.getText());
        } else if (fxChoice3.isSelected()) {
            reponses.put(currentQuestionIndex, fxChoice3.getText());
        }
    }

    private void afficherReponses() {
        System.out.println("Réponses sélectionnées :");
        for (Map.Entry<Integer, String> entry : reponses.entrySet()) {
            System.out.println("Question " + (entry.getKey() + 1) + ": " + entry.getValue());
        }
    }

    @FXML
    void Submit(ActionEvent event) throws SQLException{

// Enregistrer la réponse sélectionnée avant de soumettre le quiz
        enregistrerReponse();
        int resultat=calculerScore();
        LocalDate date=LocalDate.now();

       Evaluation evaluation=new Evaluation(quizId,resultat,userId,date);
        evaluationCrud.ajouter(evaluation);
        fxScoreLabel.setVisible(true);
        fxRedirectionButton.setVisible(true);

    }
    @FXML
    void redirectionButtonClicked(ActionEvent event) {
        // Code pour rediriger vers une autre interface
        // Par exemple, charger une nouvelle interface FXML
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherAllQuizzesEnfant.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int calculerScore() {
        int score = 0;
        for (Map.Entry<Integer, String> entry : reponses.entrySet()) {
            int indexQuestion = entry.getKey();
            String reponseUtilisateur = entry.getValue();
            String reponseCorrecte = questions.get(indexQuestion).getReponse();

            if (reponseUtilisateur.equals(reponseCorrecte)) {
                score++;
            }
        }
        fxScoreLabel.setText("Score: " + score+"/"+questions.size());
        System.out.println("Score du quiz : " + score+"/"+questions.size());
        return score;
    }

}

