package Controller;


import Entity.Evaluation;
import Entity.Questions;
import Entity.Quiz;
import Service.EvaluationCrud;
import Service.QuestionsCrud;
import Service.QuizCrud;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ShowQuizHistoryEnfant {

    private List<Evaluation> evaluationList;
    private EvaluationCrud ec=new EvaluationCrud();
    private List<Quiz> quizy;
    private QuizCrud qqc=new QuizCrud();
    @FXML
    private VBox mainVbox;
    private final EvaluationCrud qc = new EvaluationCrud();
    private QuestionsCrud questionsCrud = new QuestionsCrud();
    private List<Questions> questions;

    public void initialize() {
        try {
            evaluationList=ec.recuperer();
            int quizzesPassed=evaluationList.size();
            quizy=qqc.recuperer();
            int numbrequizzes=quizy.size();
            System.out.println(quizzesPassed+"/"+numbrequizzes);

            List<Evaluation> evaluations = qc.recuperer();

            mainVbox.setSpacing(10);
            int maxColumns = 4;
            for (int i = 0; i < evaluations.size(); i += maxColumns) {
                HBox rowHBox = new HBox();
                rowHBox.setSpacing(20);
                for (int j = i; j < Math.min(i + maxColumns, evaluations.size()); j++) {
                    Evaluation evaluation=evaluations.get(j);

                    StackPane userCard = createEvaluationCard(evaluation);
                    rowHBox.getChildren().add(userCard);
                }
                mainVbox.getChildren().add(rowHBox);
            }
            Button BackToQuizzesButton = new Button("Back to quizzes");
            BackToQuizzesButton.setStyle("-fx-background-radius: 10px");
            BackToQuizzesButton.setOnAction(event -> {
                try {
                    // Charger la vue de la nouvelle interface depuis le fichier FXML
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherAllQuizzesEnfant.fxml"));
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
            Button StatisticsButton = new Button("show statistics");
            StatisticsButton.setStyle("-fx-background-radius: 10px");
            StatisticsButton.setOnAction(event -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Statistics.fxml"));
                try {
                    Parent root = loader.load();
                    StatisticController controller = loader.getController();

                    int remainingQuizzes = quizy.size() - quizzesPassed;

                    controller.setData(quizzesPassed, remainingQuizzes);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));


                    stage.show();
                    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                    stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                    stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                    stage.setTitle("Passed quizzes vs remaining quizzes ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            mainVbox.getChildren().addAll(BackToQuizzesButton,StatisticsButton);



        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private StackPane createEvaluationCard(Evaluation evaluation) throws SQLException {
        StackPane stackPane = new StackPane();
        int quizId=evaluation.getId_quiz_id();
        QuizCrud d=new QuizCrud();
        Quiz q=d.getByQuizId(quizId);
        questions = questionsCrud.recupererParQuizId(quizId);

        int userId = evaluation.getId_user_id();
        Text nameTextuser = new Text("User Id:  " + Integer.toString(userId));

        Text nameTextQuiz = new Text("Quiz name:  " + q.getTitre());
        String resultat = evaluation.getResultat()+"/"+questions.size();
        Text nameTextResult = new Text("Result:  " + resultat);
        LocalDate date = evaluation.getDate();
        Text dateText = new Text("Date:  " +date.toString());
        nameTextQuiz.setFont(Font.font("Arial", FontWeight.BOLD, 12));


        nameTextQuiz.setTranslateY(5);
        nameTextuser.setTranslateY(50);
        nameTextResult.setTranslateY(60);
        dateText.setTranslateY(70);

        VBox contentBox = new VBox(nameTextuser, nameTextQuiz, nameTextResult,dateText);

        contentBox.setAlignment(Pos.TOP_CENTER);



        stackPane.setStyle("-fx-background-color: #ed6350; -fx-background-radius: 8px;-fx-cursor: pointer");
        stackPane.setMinHeight(160);
        stackPane.setMinWidth(160);
        stackPane.getChildren().addAll(contentBox);
        return stackPane;
    }

}
