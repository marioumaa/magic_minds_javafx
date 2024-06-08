package Controller;

import Entity.Quiz;
import Service.QuizCrud;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AfficherQuiz {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField rechercherTF;
    @FXML
    private Button fxShowquestions;


    @FXML
    private Button fxAdd;

    @FXML
    private TableColumn<Quiz,Integer> cNb;

    @FXML
    private TableColumn<Quiz,Integer> cTime;

    @FXML
    private TableColumn<Quiz,String> cTitre;

    @FXML
    private TableColumn<Quiz,Void> deleteTc;

    @FXML
    private TableColumn<Quiz,Void> updateTc;
    @FXML
    private TableView<Quiz> tableId;
       QuizCrud qc =new QuizCrud();
    @FXML
    public void initialize() throws SQLException {
        // Initialize TableView columns
        cTime.setCellValueFactory(new PropertyValueFactory<>("temp"));
        cNb.setCellValueFactory(new PropertyValueFactory<>("nb_question"));
        cTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));



        configureDeleteColumn();
       configureModifyColumn();
        loadQuizData();
    }

    private void loadQuizData()throws SQLException {
        List<Quiz> q =qc.recuperer();
        tableId.getItems().addAll(q);

    }

    public void afficherQuiz() throws SQLException {
        QuizCrud qc = new QuizCrud();
        List<Quiz> q = new ArrayList<>();
        q=qc.recuperer();
        ObservableList<Quiz> list = FXCollections.observableList(q);

        tableId.setItems(list);
        cTitre.setCellValueFactory(new PropertyValueFactory<Quiz,String>("titre"));

        cNb.setCellValueFactory(new PropertyValueFactory<Quiz,Integer>("nb_question"));
        cTime.setCellValueFactory(new PropertyValueFactory<Quiz,Integer>("temp"));


    }

    private void configureDeleteColumn() {
        // Set up the delete button column
        deleteTc.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> {
                        Quiz QuizToDelete = getTableView().getItems().get(getIndex());

                        try {
                            deleteQuiz(QuizToDelete);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    });
                }
            }
        });
    }

    private void deleteQuiz(Quiz QuizToDelete) throws SQLException {
        qc.supprimer(QuizToDelete.getId());
        tableId.getItems().remove(QuizToDelete);
    }
    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void modifyBorne(Quiz borneToModify) {
        try {
            // Charger l'interface de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierQuiz.fxml"));
            Parent root = loader.load();

            // Passer les données de la borne à modifier au contrôleur de l'interface de modification
            ModifierQuizController modifierController = loader.getController();
            modifierController.initData(borneToModify);
            modifierController.setAfficherQuizController(this);
            // Afficher l'interface de modification
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Modifier Borne");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ouverture de l'interface de modification.");
        }
    }

    private void configureModifyColumn() {
        updateTc.setCellFactory(param -> new TableCell<>() {
            private final Button modifyButton = new Button("Modify");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(modifyButton);
                    modifyButton.setOnAction(event -> {
                       Quiz QuizToModify = getTableView().getItems().get(getIndex());
                        modifyBorne(QuizToModify);
                    });
                }
            }
        });
    }


    @FXML
    void addQuiz(ActionEvent event) throws IOException {
            root = FXMLLoader.load(getClass().getResource("/Ajouter.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }



    @FXML
    void Showquestions(ActionEvent event)throws IOException {
        root = FXMLLoader.load(getClass().getResource("/AfficherQuestion.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void updateQuizTable() {
        try {
            // Effacer la liste actuelle des quiz dans la table
            tableId.getItems().clear();

            // Recharger les données des quiz depuis la base de données
            List<Quiz> q = qc.recuperer();
            tableId.getItems().addAll(q);
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur s'est produite lors de la mise à jour de la liste des quiz.");
        }
    }
    @FXML
    void rechercher(ActionEvent event) throws SQLException{
QuizCrud qc=new QuizCrud();
            String titre = rechercherTF.getText();
            if (!titre.isEmpty()) {
                Quiz quizy = qc.getByTitre(titre);
                if ( titre!= null) {
                    ObservableList<Quiz> observableList = FXCollections.observableArrayList(quizy);
                    tableId.setItems(observableList);
                } else {
                    tableId.setItems(FXCollections.emptyObservableList());
                }
            } else {
                List<Quiz> quiz1= null;
                quiz1 = qc.recuperer();
                ObservableList<Quiz> observableList = FXCollections.observableList(quiz1);
                tableId.setItems(observableList);
            }

        }

    @FXML
    void goCourses(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionCours.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenementAdmin.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/demo4/Admin.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Afficher.fxml"));
            Parent cartRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene of the current stage to the cart view
            Scene scene = new Scene(cartRoot,1050,700);
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
    void goStore(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterProduit.fxml"));
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
    void goUser(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserManagementController.fxml"));
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
    void GoToDashboard(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home_back.fxml"));
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


