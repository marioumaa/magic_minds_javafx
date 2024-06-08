package Controller.Etudiant;

import Controller.UserManagementController;
import Entity.Evenement;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import Entity.Participation;
import Entity.User;
import Service.ServiceEvenement;
import Service.ServiceParticipation;
import Service.SessionManager;
import Service.UserService;
import Utili.Mail;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AfficherEventController {
    private final UserService serviceUser = new UserService();

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private final ServiceParticipation serviceParticipation = new ServiceParticipation();

    @FXML
    private VBox eventContainer;

    @FXML
    private Button calendarButton;


    @FXML
    void initialize() {
        loadCalendarButton();
        loadEvents();
        calendarButton.setVisible(true); // Rendre le bouton visible après le chargement des événements
    }
    @FXML
    private void voirCalendrier() {
        Logger logger = Logger.getLogger(getClass().getName());
        logger.info("Le bouton voirCalendrier() a été déclenché.");

        redirectToFullCalendar();
    }
    private void loadCalendarButton() {
        calendarButton.setOnAction(event -> redirectToFullCalendar());
    }

    private void redirectToFullCalendar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FullCalendar.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) eventContainer.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadEvents() {
        List<Evenement> evenements = serviceEvenement.getAll();
        eventContainer.getChildren().clear();

        int eventsPerRow = 5;
        int rowCount = (int) Math.ceil((double) evenements.size() / eventsPerRow);

        for (int i = 0; i < rowCount; i++) {
            TilePane rowPane = new TilePane();
            rowPane.setOrientation(Orientation.HORIZONTAL);
            rowPane.setPrefColumns(eventsPerRow);
            rowPane.setAlignment(Pos.CENTER);
            rowPane.setPadding(new Insets(10));
            rowPane.setHgap(10);
            rowPane.setVgap(10);

            int startIndex = i * eventsPerRow;
            int endIndex = Math.min(startIndex + eventsPerRow, evenements.size());

            for (int j = startIndex; j < endIndex; j++) {
                Evenement evenement = evenements.get(j);
                VBox card = createEventCard(evenement);
                rowPane.getChildren().add(card);
            }

            eventContainer.getChildren().add(rowPane);
        }
    }

    private VBox createEventCard(Evenement evenement) {
        VBox card = new VBox();
        card.getStyleClass().add("event-card");

        ImageView imageView = loadEventImage();
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);


        Label nomLabel = new Label("Event Title : " + evenement.getNom());
        nomLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold; -fx-text-fill: #FE5D37;");
        Label descriptionLabel = new Label("Event Description : " + evenement.getDescription());
        Label localisationLabel = new Label("Event Location : " + evenement.getLocalisation());
        Label categorieLabel = new Label("Event Category : " + evenement.getCategorie());
        Label dateDebutLabel = new Label("Start Date : " + evenement.getDate_debut());
        Label dateFinLabel = new Label("End Date : " + evenement.getDate_fin());
        Label nbParticipantsLabel = new Label("Number of Participants : " + evenement.getNb_participant());

        Button participationButton = new Button("Participate");
        participationButton.setStyle("-fx-background-color: #fe5d37; -fx-font-weight: bold; -fx-text-fill: white;");

        participationButton.setOnAction(event -> {
            User user = SessionManager.getCurrentUser();
            try {
                participer(evenement, user.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


        card.getChildren().addAll(
                imageView,
                nomLabel,
                descriptionLabel,
                localisationLabel,
                categorieLabel,
                dateDebutLabel,
                dateFinLabel,
                nbParticipantsLabel,
                participationButton
        );


        return card;
    }

    private ImageView loadEventImage() {
        ImageView imageView = new ImageView();
        try {
            InputStream inputStream = getClass().getResourceAsStream("/images/kids-event.jpg");
            if (inputStream != null) {
                Image image = new Image(inputStream);
                imageView.setImage(image);
            } else {
                System.err.println("L'image n'a pas pu être chargée. Assurez-vous que le chemin de l'image est correct.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageView;
    }

    private void participer(Evenement evenement, int id_user_id) throws SQLException {
        // Enregistrer la date et l'heure actuelles de la participation
        LocalDateTime now = LocalDateTime.now();
        Date dateParticipation = Date.valueOf(now.toLocalDate()); // Convertir la LocalDate en Date
        Time heureParticipation = Time.valueOf(now.toLocalTime()); // Convertir la LocalTime en Time

        Participation participation = new Participation();
        participation.setEvenementId(evenement.getId());
        participation.setId_user_id(id_user_id);
        participation.setDate(dateParticipation);
        participation.setHeure(heureParticipation);

        System.out.println("Evenement ID: " + participation.getEvenementId());

        serviceParticipation.add(participation);

        // Récupérer le nom de l'événement
        String eventName = serviceEvenement.getEventNameById(participation.getEvenementId());
        User user = serviceUser.getUserById(participation.getId_user_id());

        String username= user.getFirstName();
        // Afficher les noms récupérés
        System.out.println("Nom de l'événement : " + eventName);
        // Créer le contenu personnalisé de l'e-mail
        String emailContent = "Bonjour Professeur,\n";
        emailContent += username + " a participé à " + eventName + ".\n";
        emailContent += "Cordialement,\n !! ";

        try {
            // Envoyer l'e-mail personnalisé
            Mail.sendEmail("hadididouaa65@gmail.com", emailContent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Redirection vers une autre vue
        redirectTo("AfficherParticipationEtudiant.fxml");

    }

    private void redirectTo(String AfficherParticipationEtudiant) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherParticipationEtudiant.fxml"));
            Parent root = loader.load();


            Scene scene = new Scene(root);


            Stage stage = (Stage) eventContainer.getScene().getWindow();


            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void goCourse(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CoursesEnfant.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenementEtudiant.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/demo4/Home_front.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherAllQuizzesEnfant.fxml"));
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
    public void Login1() throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("/loginController.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

    }
    @FXML
    void logout(MouseEvent event) {
        SessionManager sessionManager= SessionManager.getInstance();
        sessionManager.endSession();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        try {
            Login1();
        } catch (IOException e) {
            System.out.println(e.getMessage());
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