package Controller.Ressource;

import Entity.Cours;
import Entity.Ressource;
import Service.CategorieService;
import Service.CoursService;
import Service.RessourceService;
import Controller.Outil;
import Controller.PreviewPDF;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateChapter implements Initializable {

    @FXML
    private Label ECtitre;

    @FXML
    private Button UpuploadFile;

    @FXML
    private Button back;

    @FXML
    private ChoiceBox<String> upChapCour;

    @FXML
    private TextField upChapTiltle;

    @FXML
    private ChoiceBox<String> upChapType;

    @FXML
    private Button updateChap;

    @FXML
    private Label updisplayUrl;

    @FXML
    private Button uppreview;
    File selectedFile = new File("C:\\");
    String userDir = System.getProperty("user.dir");
    String trimmedPath = userDir.substring(0, userDir.lastIndexOf("\\"));
    File UploadDirectory = new File(trimmedPath+"/MagicMinds/public/uploads/ressources");
    File UploadDirectoryJava = new File(System.getProperty("user.dir").replace("\\", "/")+"/src/main/resources/UploadedFile");
    File destinationFile = new File("C:\\");
    File destinationFileJava = new File("C:\\");
    String url1 ="Please choose a file";
    String url="";
    Ressource ressource=new Ressource();
    RessourceService ressourceService=new RessourceService();
    Cours cours=new Cours();
    CoursService coursService=new CoursService();
    CategorieService categorieService =new CategorieService();
    Outil outil =new Outil();


    public void fetchChapData(Ressource r) {
        ressource=r;
        upChapTiltle.setText(r.getTitre());
        cours=coursService.getbyId(r.getId_cours_id());
        upChapCour.setValue(cours.getTitre());
        upChapType.setValue(r.getType());
        url=r.getUrl();
        updisplayUrl.setText(r.getUrl());
    }

    @FXML
    void updateChap(MouseEvent event) {
        Ressource chap = new Ressource();
        Cours co= new Cours();
        if (isValid()){
            try {
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Files.copy(selectedFile.toPath(),destinationFileJava.toPath() ,StandardCopyOption.REPLACE_EXISTING);

            }catch (IOException e){
                e.printStackTrace();
            }
            chap.setTitre(upChapTiltle.getText());
            chap.setType(upChapType.getValue());
            String coursName = upChapCour.getValue();
            co= coursService.getbyId(coursService.getCourstId(coursName));
            chap.setId_cours_id(co.getId());
            chap.setId_cours_id(ressource.getId_cours_id());
            System.out.println(url);
            chap.setUrl(url);
            ressourceService.update(ressource.getId(),chap);
            int id =co.getCategorie_id();
            categorieService.updateChap(id,outil.NbChap(id));
            outil.showSAlert("Update Success", "The chapter "+chap.getTitre()+" was successfully updated");
            Stage stage = (Stage) updateChap.getScene().getWindow();
            stage.close();
        }

    }

    @FXML
    void goBack(MouseEvent event) {
        Stage closestage = (Stage) UpuploadFile.getScene().getWindow();
        closestage.close();

    }



    @FXML
    void uploadFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\"));
        if (upChapType.getValue().equals("PDF")) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        }else if (upChapType.getValue().equals("Image")) {
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPEG Image","*.jpg"),
                    new FileChooser.ExtensionFilter("PNG Image", "*.png"),
                    new FileChooser.ExtensionFilter("All image files","*.jpg","*.png")
            );
        }else {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mkv"));
        }
        selectedFile = fileChooser.showOpenDialog(upChapType.getScene().getWindow());
        if (selectedFile != null) {
            String pathName = selectedFile.getAbsolutePath().replace("\\", "/");
            destinationFile = new File(UploadDirectory, selectedFile.getName());
            destinationFileJava=new File(UploadDirectoryJava ,selectedFile.getName());
            url1 = UploadDirectory.getAbsolutePath().replace("\\", "/");
            url1 += "/" + selectedFile.getName();
            updisplayUrl.setText(url1);
            url=selectedFile.getName();
        }
    }
    public boolean isValid(){
        ObservableList<Ressource> ressources = ressourceService.getAll();
        Cours co= coursService.getbyId(coursService.getCourstId(upChapCour.getValue()));
        Cours coursEncien=coursService.getbyId(ressource.getId_cours_id());

        String title = upChapTiltle.getText();
        ECtitre.setText("");

        if (title == null || title.isEmpty()) {
            ECtitre.setText("The title can't be empty");
            return false;
        }
        if (title.length() < 3 || title.length() > 20) {
            ECtitre.setText("The title is either too long or too short");
            return false;
        }
        for (Ressource r : ressources) {
            if (title.equals(r.getTitre()) && !title.equals(ressource.getTitre())) {
                ECtitre.setText("This title already exist");
                return false;
            }
        }
        for (Ressource r : ressources) {
            if (url.equals(r.getUrl()) && !url.equals(ressource.getUrl())) {
                updisplayUrl.setText("this chapter already exist");
                updisplayUrl.setStyle("-fx-font-fill:red");
                return false;
            }
        }
        if (!co.getTitre().equals(coursEncien.getTitre())) {
            if (co.getNb_chapitre() <= ressources.size()) {
                outil.showEAlert("Error", "you reached the maximum number of chapter\n for " + upChapCour.getValue());
                return false;
            }
        }
        return true ;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        upChapCour.setItems(coursService.getCoursNames());
        ObservableList<String> types = FXCollections.observableArrayList("PDF", "Image", "Video");
        upChapType.setItems(types);
    }
}
