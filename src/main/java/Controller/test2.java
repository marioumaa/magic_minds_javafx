package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.stage.FileChooser;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class test2 implements Initializable {

    @FXML
    private ImageView imageView;
    @FXML
    private Button upload;

    File selectedFile = new File("C:\\");
    File UploadDirectory = new File("C:/Users/hp/Desktop/Magic-Minds-JavaFX/src/main/resources/uploderUser");
    File destinationFile = new File("C:\\");
    String url ;
    @FXML
    void uploaedImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG Image","*.jpg"),
                new FileChooser.ExtensionFilter("PNG Image", "*.png"),
                new FileChooser.ExtensionFilter("All image files","*.jpg","*.png")
        );
        selectedFile = fileChooser.showOpenDialog(upload.getScene().getWindow());
        if (selectedFile != null){
            String pathName =selectedFile.getAbsolutePath().replace("\\","/");
            System.out.println(pathName);
            Image image = new Image("file:/" +pathName);
            imageView.setImage(image);
            destinationFile=new File(UploadDirectory,selectedFile.getName());
            url = UploadDirectory.getAbsolutePath().replace("\\", "/");
            url += "/" + selectedFile.getName();
            System.out.println(url);
            try {
                Files.copy(selectedFile.toPath(),destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
