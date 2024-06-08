package Controller;

import Entity.Cours;
import Service.CoursService;
import Service.RessourceService;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class Outil {
    CoursService coursService = new CoursService();
    RessourceService ressourceService= new RessourceService();
    public void showEAlert(String title, String contentText) {
        Alert alertType = new Alert(Alert.AlertType.ERROR);
        alertType.setTitle(title);
        alertType.setHeaderText(contentText);
        alertType.getDialogPane().setGraphic(null);
        alertType.show();
    }

    public void showSAlert(String title, String contentText) {
        Alert alertType = new Alert(Alert.AlertType.INFORMATION);
        alertType.setTitle(title);
        alertType.setHeaderText(contentText);
        alertType.getDialogPane().setGraphic(null);
        alertType.show();
    }
    public boolean DeleteAlert(String title, String contentText,boolean defaultYes) {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setTitle(title);
        deleteAlert.setHeaderText(contentText);
        deleteAlert.getButtonTypes().clear();
        deleteAlert.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button cancel = (Button) deleteAlert.getDialogPane().lookupButton(ButtonType.CANCEL);
        Button delete = (Button) deleteAlert.getDialogPane().lookupButton(ButtonType.OK);
        cancel.setDefaultButton(!defaultYes);
        delete.setDefaultButton(defaultYes);
        Optional<ButtonType> result = deleteAlert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    public  String trimUrl(String url,int a){
        Path pdfpath = Paths.get(url);
        Path trimmedPath = pdfpath.subpath(pdfpath.getNameCount() - a, pdfpath.getNameCount());
        String trim=trimmedPath.toString().replace("\\", "/");
        trim="/"+trim;
        return  trim ;
    }
    public int NbCourse(int id){
        List<Cours> CL=coursService.getCoursesByCat(id);
        int nbCours=CL.size();
        return nbCours;
    }
    public int NbChap(int id){
        int nbChap=0;
        List<Cours> CL=coursService.getCoursesByCat(id);
        for (Cours c : CL){
            List RL= ressourceService.getChaptersByCat(c.getId());
            nbChap=nbChap+RL.size();
        }
        return nbChap;
    }
    public int totalCourses(){
        ObservableList<Cours> CL= coursService.getAll();
        int N= CL.size();
        return N ;
    }
}
