package Controller;

import Entity.User;
import Entity.question;
import Service.SessionManager;
import Service.questionService;
import Service.reponseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * FXML Controller class
 */
public class questionController implements Initializable {

    @FXML private Label nomevLabel, descriptionevLabel, typeLabel, dateevLabel, likeLabel, dislikeLabel;
    @FXML private Button repondreevButton, likeButton, dislikeButton;
    @FXML private ImageView imageview;
    @FXML private TextField idevF, iduserF, idPartField;
    private Set<Integer> likedQuestions = new HashSet<>();
    private Set<Integer> dislikedQuestions = new HashSet<>();
    private question eve;
    private reponseService responseService;
    private questionService questionService;
    private User currentUser;
    File UploadDirectory = new File("C:/Users/achra/Desktop/New folder/Magic-Minds-JavaFX/src/main/resources/images/");

    /**
     * Initializes the controller class, setting initial hidden state and loading default values.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idevF.setVisible(false);
        iduserF.setVisible(false);
        idPartField.setVisible(false);
        responseService = new reponseService();
        questionService = new questionService(); // Initialize questionService
        currentUser = SessionManager.getCurrentUser(); // This should be set to the logged-in user, ensure correct context
    }

    public void setQuestion(question e) {
        User user= SessionManager.getCurrentUser();
        this.eve = e;
        nomevLabel.setText(e.getName());
        descriptionevLabel.setText(e.getCommentaire());
        typeLabel.setText(e.getType());
        dateevLabel.setText(String.valueOf(e.getDate()));
        idevF.setText(String.valueOf(e.getId()));
        iduserF.setText(String.valueOf(currentUser.getId()));
        likeLabel.setText(String.valueOf(e.getLike()));
        dislikeLabel.setText(String.valueOf(e.getDislike()));
        loadImage(e.getImage());
    }

  /*  private void loadImage(String imageName) {
//        String path = "demo4/images/" + imageName;
//        Image img = new Image(getClass().getResourceAsStream(path));
        String path = "C:/Users/achra/Desktop/New folder/Magic-Minds-JavaFX/src/main/resources/images/" + imageName;
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            System.out.println("Image non trouvée à l'emplacement : " + path);
            // Vous pouvez ici définir une image par défaut ou gérer l'erreur autrement
        } else {
            Image img = new Image("C:/Users/achra/Desktop/New folder/Magic-Minds-JavaFX/src/main/resources/images/"+imageName);
            imageview.setImage(img);
        }
//        imageview.setImage(img);
    }*/

    private void loadImage(String imageName) {
        String path = "C:/Users/achra/Desktop/New folder/Magic-Minds-JavaFX/src/main/resources/images/" + imageName;
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("Image non trouvée à l'emplacement : " + path);
            // Vous pouvez ici définir une image par défaut ou gérer l'erreur autrement
        } else {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                Image img = new Image(inputStream);
                imageview.setImage(img);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                // Gérer l'exception si le fichier n'est pas trouvé
            }
        }
    }

    @FXML
    private void handleLike() {
        if (!likedQuestions.contains(eve.getId())) {
            questionService.updateLikes(eve.getId());
            likedQuestions.add(eve.getId()); // Mark this question as liked by this user
            int newLikeCount = questionService.getLikes(eve.getId()); // Fetch updated count
            likeLabel.setText(String.valueOf(newLikeCount));
            eve.setLike(newLikeCount);
        }
    }

    @FXML
    private void handleDislike() {
        if (!dislikedQuestions.contains(eve.getId())) {
            questionService.updateDislikes(eve.getId());
            dislikedQuestions.add(eve.getId()); // Mark this question as disliked by this user
            int newDislikeCount = questionService.getDislikes(eve.getId()); // Fetch updated count
            dislikeLabel.setText(String.valueOf(newDislikeCount));
            eve.setDislike(newDislikeCount);
        }
    }

    @FXML
    private void repondreev(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/demo4/Home_front_Prof.fxml"));
        Parent root = loader.load();

        // Now get the controller class for Home_front_Prof.fxml and call the method to pass the question
        AfficherreponseController homeController = loader.getController();
        homeController.setQuestion(eve);

        // Navigate to the home
        idevF.getScene().setRoot(root);
        repondreevButton.setVisible(false);
    }
}
