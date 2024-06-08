package Controller.Categorie;

import Entity.Categorie;
import Service.CategorieService;
import Service.CoursService;
import Controller.Outil;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;

public class AddCategory {

    @FXML
    private TextArea addCatDescrip;
    @FXML
    private ImageView addCatImg;
    @FXML
    private TextField addCatTitle;
    @FXML
    private Label ECDesc;
    @FXML
    private Label ECattitre;
    @FXML
    private Label Eimage;
    @FXML
    private Button upload;

    @FXML
    private Button backCat;

    CategorieService categorieService = new CategorieService();

    Outil outil =new Outil();
    String url1="Please choose an image";
    String url="";
    File selectedFile = new File("C:\\");
    String userDir = System.getProperty("user.dir");
    String trimmedPath = userDir.substring(0, userDir.lastIndexOf("\\"));
    File UploadDirectory = new File(trimmedPath+"/MagicMinds/public/uploads/images");
    File destinationFile = new File("C:\\");


    @FXML
    public void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG Image","*.jpg"),
                new FileChooser.ExtensionFilter("PNG Image", "*.png"),
                new FileChooser.ExtensionFilter("All image files","*.jpg","*.png")
        );
        selectedFile = fileChooser.showOpenDialog(upload.getScene().getWindow());
        if (selectedFile != null){
                String pathName =selectedFile.getAbsolutePath().replace("\\","/");
                Image image = new Image("file:/" +pathName);
                addCatImg.setImage(image);
                destinationFile=new File(UploadDirectory,selectedFile.getName());
                url1 = UploadDirectory.getAbsolutePath().replace("\\", "/");
                url1 += "/" + selectedFile.getName();
                url=selectedFile.getName();

        }

    }
    @FXML
    void addCat(MouseEvent event) {
        Categorie cat = new Categorie();
        if (isValid()){
            try {
                Files.copy(selectedFile.toPath(),destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }catch (IOException e){
                e.printStackTrace();
            }
            cat.setTitre(addCatTitle.getText());
            cat.setDescription(addCatDescrip.getText());
            cat.setImage(url);
            categorieService.insert(cat);
            outil.showSAlert("Addition Success", "The category "+cat.getTitre()+" was successfully added");
            Stage stage = (Stage) upload.getScene().getWindow();
            stage.close();
        }
    }
    public boolean isValid(){

            ObservableList<Categorie> categories = categorieService.getAll();
            String title = addCatTitle.getText();
            String descrip = addCatDescrip.getText();
            ECattitre.setText("");
            ECDesc.setText("");

            if (title == null || title.isEmpty()) {
                ECattitre.setText("The title can't be empty");
                return false;
            }
            if (title.length() < 3 || title.length() > 20) {
                ECattitre.setText("The title is either too long or too short");
                return false;
            }
            for (Categorie cat : categories) {
                if (title.equals(cat.getTitre())) {
                    ECattitre.setText("This title already exist");
                    return false;
                }
            }
            if (descrip == null || descrip.isEmpty()) {
                ECDesc.setText("The description can't be empty");
                return false;
            }
            if (descrip.length() < 10 || descrip.length() > 150) {
                ECDesc.setText("The description is either too long or too short");
                return false;
            }
            if (url.equals("Please choose an image")) {
                Eimage.setText(url);
                return false;
            }
            for (Categorie c : categories){
                if (url.equals(c.getImage())){
                    Eimage.setText("This image already exist");
                    return false;
                }
            }

            return true ;

    }
    @FXML
    void goBack(MouseEvent event) {
        Stage closestage = (Stage) backCat.getScene().getWindow();
        closestage.close();
    }


}
