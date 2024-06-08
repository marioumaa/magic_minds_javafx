package Controller;

import Entity.Questions;
import Entity.Quiz;
import Service.QuestionsCrud;
import Service.QuizCrud;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherQuestionProf {

    @FXML
    private VBox mainVbox;
    private final QuestionsCrud qc=new QuestionsCrud();
    private Questions questionToUpdate;

    public void initialize() {
        try {
            List<Questions> questions = qc.recuperer();

            mainVbox.setSpacing(10);
            int maxColumns = 2;
            for (int i = 0; i < questions.size(); i += maxColumns) {
                HBox rowHBox = new HBox();
                rowHBox.setSpacing(20);
                for (int j = i; j < Math.min(i + maxColumns, questions.size()); j++) {

                    Questions question=questions.get(j);
                    StackPane userCard = createQuestionCard(question);
                    rowHBox.getChildren().add(userCard);
                }
                mainVbox.getChildren().add(rowHBox);
            }

            Button BackToQuizzesButton = new Button("Back to quizzes");
            BackToQuizzesButton.setStyle("-fx-background-radius: 10px");
            BackToQuizzesButton.setOnAction(event -> {
                try {
                    // Charger la vue de la nouvelle interface depuis le fichier FXML
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherQuizProf.fxml"));
                    Parent root = loader.load();

                    // Créer une nouvelle scène avec la vue chargée
                    Scene scene = new Scene(root);

                    // Obtenir la référence à la fenêtre actuelle
                    Stage stage = (Stage) BackToQuizzesButton.getScene().getWindow();

                    // Changer la scène de la fenêtre pour afficher la nouvelle interface
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace(); // Gérer les exceptions liées au chargement de la vue
                }
            });
            Button AddQuestionButton = new Button("Add question");
            AddQuestionButton.setStyle("-fx-background-radius: 10px");
            AddQuestionButton.setOnAction(event -> {
                try {
                    // Charger la vue de la nouvelle interface depuis le fichier FXML
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterQuestionProf.fxml"));
                    Parent root = loader.load();

                    // Créer une nouvelle scène avec la vue chargée
                    Scene scene = new Scene(root);

                    // Obtenir la référence à la fenêtre actuelle
                    Stage stage = (Stage) AddQuestionButton.getScene().getWindow();

                    // Changer la scène de la fenêtre pour afficher la nouvelle interface
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace(); // Gérer les exceptions liées au chargement de la vue
                }
            });

            mainVbox.getChildren().addAll(new HBox(BackToQuizzesButton, AddQuestionButton));
            mainVbox.setAlignment(Pos.CENTER); // Aligner les éléments au centre de la VBox

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private StackPane createQuestionCard(Questions question) {
        int idQuiz= question.getIdQuiz();
        QuizCrud d=new QuizCrud();
        Quiz q=d.getByQuizId(idQuiz);
        StackPane stackPane = new StackPane();


        // Name label

        Text nameQuestion = new Text("Question: "+question.getQuestion());
        Text nameChoice1 = new Text("Choice 1: "+question.getChoix1());
        Text nameChoice2 = new Text("Choice 2: "+question.getChoix2());
        Text nameChoice3 = new Text("Choice 3: "+question.getChoix3());
        Text nameAnswer = new Text("Correct Answer: "+question.getReponse());
        Text nameQuiz = new Text("Quiz name: "+q.getTitre());


        //Text nameIdquiz = new Text("idQuiz:  " + Integer.toString(idQuiz));




        nameQuiz.setTranslateY(0);
        nameQuestion.setTranslateY(12);
        nameChoice1.setTranslateY(24);
        nameChoice2.setTranslateY(36);
        nameChoice3.setTranslateY(48);
        nameAnswer.setTranslateY(60);

        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");

        updateButton.setStyle("-fx-background-radius: 10px");
        deleteButton.setStyle("-fx-background-radius: 10px");
        HBox button=new HBox();
        button.getChildren().addAll(updateButton,deleteButton);
        button.setAlignment(Pos.BOTTOM_CENTER);
        button.setTranslateY(20);
        button.setSpacing(10);

        VBox contentBox = new VBox(nameQuiz,nameQuestion,nameChoice1,nameChoice2,nameChoice3,nameAnswer,button);

        contentBox.setAlignment(Pos.TOP_CENTER);
        // Positioning buttons
        updateButton.setTranslateY(50);
        deleteButton.setTranslateY(50);

        // Button actions
        updateButton.setOnAction(event -> {
            questionToUpdate = question;
            loadUpdateQuestionScene();

        });

        deleteButton.setOnAction(event -> {
            try {
                qc.supprimer(question.getId());
                refreshDisplay(); // Rafraîchir l'affichage après la suppression
            } catch (SQLException ex) {
                ex.printStackTrace();
                // Gérer l'erreur de suppression de la question
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Error deleting question: " + ex.getMessage());
                alert.showAndWait();
            }

        });

        stackPane.setStyle("-fx-background-color: #ed6350; -fx-background-radius: 8px;-fx-cursor: pointer");
        stackPane.setMinHeight(250);
        stackPane.setMinWidth(300);
        stackPane.getChildren().addAll(contentBox);

        return stackPane;
    }
    private void refreshDisplay() {
        // Effacer le contenu actuel de la VBox
        mainVbox.getChildren().clear();
        // Recharger les quizzes et réafficher
        initialize();
    }
    private void loadUpdateQuestionScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierQuestionProf.fxml"));
            Parent root = loader.load();
            ModifierQuestionProf controller = loader.getController();
            controller.setQuestionToModify(questionToUpdate);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();
            refreshDisplay();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}