package Controller;

import Entity.Questions;
import Service.QuestionsCrud;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AfficherQuestion {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button fxBacktoquizzes;
    @FXML
    private Button fxAddQuestion;

    @FXML
    private TableColumn<Questions,String> fxChoice1;

    @FXML
    private TableColumn<Questions,String> fxChoice2;

    @FXML
    private TableColumn<Questions,String> fxChoice3;

    @FXML
    private TableColumn<Questions,String> fxCorrectAnswer;

    @FXML
    private TableColumn<Questions,String > fxQuestion;

    @FXML
    private TableColumn<Questions, String> fxQuizId;
    @FXML
    private TableColumn<Questions,Void> fxDeleteQuestion;
    @FXML
    private TableColumn<Questions,Void> fxUpdateQuestion;

    @FXML
    private TableView<Questions> fxTable;
    QuestionsCrud qc=new QuestionsCrud();


    @FXML
    public void initialize() throws SQLException {
        // Initialize TableView columns
        fxQuizId.setCellValueFactory(new PropertyValueFactory<Questions, String>("nomQuiz"));
        fxQuestion.setCellValueFactory(new PropertyValueFactory<>("question"));
        fxChoice1.setCellValueFactory(new PropertyValueFactory<>("choix1"));
        fxChoice2.setCellValueFactory(new PropertyValueFactory<>("choix2"));
        fxChoice3.setCellValueFactory(new PropertyValueFactory<>("choix3"));
        fxCorrectAnswer.setCellValueFactory(new PropertyValueFactory<>("reponse"));


        configureDeleteColumn();
        configureModifyColumn();
        loadQuestionData();
    }

    private void loadQuestionData()throws SQLException {
        List<Questions> q =qc.recupererAvecNomQuiz();
        fxTable.getItems().addAll(q);

    }


    public void afficherQuestion() throws SQLException {
        QuestionsCrud qc=new QuestionsCrud();
        List<Questions> q=new ArrayList<>();

        q=qc.recupererAvecNomQuiz();
        ObservableList<Questions> list = FXCollections.observableList(q);
       // QuizCrud d=new QuizCrud();
        //Quiz qq=d.getByQuizId(quizId);
        fxTable.setItems(list);
        fxQuestion.setCellValueFactory(new PropertyValueFactory<Questions,String>("question"));
        fxChoice1.setCellValueFactory(new PropertyValueFactory<Questions,String>("choix1"));
        fxChoice2.setCellValueFactory(new PropertyValueFactory<Questions,String>("choix2"));
        fxChoice3.setCellValueFactory(new PropertyValueFactory<Questions,String>("choix3"));
        fxCorrectAnswer.setCellValueFactory(new PropertyValueFactory<Questions,String>("reponse"));
        fxQuizId.setCellValueFactory(new PropertyValueFactory<Questions,String>("nomQuiz"));



    }

    private void configureDeleteColumn() {
        // Set up the delete button column
        fxDeleteQuestion.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> {
                        Questions QuestionToDelete = getTableView().getItems().get(getIndex());

                        try {
                            deleteQuestion(QuestionToDelete);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    });
                }
            }
        });
    }
    private void deleteQuestion(Questions QuestionToDelete) throws SQLException {
        qc.supprimer(QuestionToDelete.getId());
        fxTable.getItems().remove(QuestionToDelete);
    }
    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void modifyBorne(Questions borneToModify) {
        try {
            // Charger l'interface de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierQuestion.fxml"));
            Parent root = loader.load();

            // Passer les données de la borne à modifier au contrôleur de l'interface de modification
            ModifierQuestionController modifierController = loader.getController();
            modifierController.initData(borneToModify);
            modifierController.setAfficherQuestionController(this);
            // Afficher l'interface de modification
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Modifier Borne");
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Une erreur s'est produite lors de l'ouverture de l'interface de modification.");
        }
    }

    private void configureModifyColumn() {
        fxUpdateQuestion.setCellFactory(param -> new TableCell<>() {
            private final Button modifyButton = new Button("Modify");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(modifyButton);
                    modifyButton.setOnAction(event -> {
                        Questions QuestionToModify = getTableView().getItems().get(getIndex());
                        modifyBorne(QuestionToModify);
                    });
                }
            }
        });
    }
    @FXML
    void fxAddQuestion(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/AjouterQuestion.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void Backtoquizzes(ActionEvent event)throws IOException {
        root = FXMLLoader.load(getClass().getResource("/Afficher.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void updateQuestionTable() {
        try {
            // Effacer la liste actuelle des quiz dans la table
            fxTable.getItems().clear();

            // Recharger les données des quiz depuis la base de données
            List<Questions> q = qc.recuperer();
            fxTable.getItems().addAll(q);
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur s'est produite lors de la mise à jour de la liste des quiz.");
        }
    }
}
