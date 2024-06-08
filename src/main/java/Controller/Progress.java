package Controller;

import Entity.Categorie;
import Entity.Cours;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.IndexedCheckModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class Progress implements Initializable{
    @FXML
    private AnchorPane content;
    private List<Categorie> FCPL = new ArrayList<>();
    private List<Cours> FCOPL=new ArrayList<>();
    Cards card=new Cards();
    public void fetchPData(List<Categorie> cpl, List<Cours> copl) {
        FCOPL=copl;
        FCPL=cpl;
    }

    public int getProgressByCat(Categorie cat){
        List<Cours> COBCL=new ArrayList<>();
        List<Cours> FilCours = new ArrayList<>();
        for (Cours c : FCOPL) {
            if (!FilCours.contains(c)) {
                FilCours.add(c);
            }
        }
        for (Cours c :FilCours ){
            if (c.getCategorie_id()==cat.getId()){
                COBCL.add(c);
            }
        }
        return (int) COBCL.size();

    }

    public int coloredpart(Categorie cat){
        int cSee=getProgressByCat(cat);
        int total=(int) cat.getNbr_cours();
        int result ;
        if (total==0){
            return 0 ;
        }else {
            result = (260 * cSee) / total;
        }
        return  result;

    }

    public List<Categorie> filterCat(){
        List<Categorie> uniqueList = new ArrayList<>();
        for (Categorie c : FCPL) {
            if (!uniqueList.contains(c)) {
                uniqueList.add(c);
            }
        }
        return uniqueList;
    }
    public void affichagePrgre(){
        VBox table=card.colomns();
        table.setAlignment(Pos.TOP_CENTER);
        List<Categorie> L=filterCat();
        for (Categorie c:L){
            HBox Tline=card.tabLine();
            Tline.setAlignment(Pos.CENTER);
            int pp=coloredpart(c);
            System.out.println(pp);
            Pane progH=card.progressHolder();
            Label l=new Label();
            if (pp!=0){
                if (pp<=86){
                    Pane prog=card.progressPane(pp,"#FE5D37");
                    progH.getChildren().add(prog);
                    l=card.mess(" Keep up","#FE5D37");
                }else if (pp>174){
                    Pane prog=card.progressPane(pp,"#369669");
                    progH.getChildren().addAll(prog);
                    l=card.mess(" Good job","#369669");
                }else {
                    Pane prog=card.progressPane(pp,"#FFC107");
                    progH.getChildren().add(prog);
                    l=card.mess(" Almost","#FFC107");
                }
            }
            else{
                l=card.mess(" Come on","#B21313FF");
            }
            Pane carte=card.progressCard(progH,c.getTitre(),l);
            Tline.getChildren().add(carte);
            table.getChildren().add(Tline);
        }
        content.getChildren().add(table);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (FCPL.size()!=0) {
            affichagePrgre();
        }
        else {
            System.out.println("empty");
        }
    }
}
