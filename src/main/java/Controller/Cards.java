package Controller;


import Entity.Categorie;
import Entity.Cours;
import Entity.Ressource;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Cards {
     String userDir = System.getProperty("user.dir").replace("\\", "/");
     String trimmedPath = userDir.substring(0, userDir.lastIndexOf("/")).replace("\\", "/");

    public Pane PCard(Categorie cat){
        Pane cardCat =new Pane();
        cardCat.getStylesheets().add("HStyle.css");
        cardCat.getStyleClass().add("cardCat");
        VBox vBox=new VBox();
        vBox.setPadding(new Insets(10,10,10,10));
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        //Image
        String cati="file:/"+trimmedPath+"/MagicMinds/public/uploads/images/"+cat.getImage();
        Image image=new Image(cati);
        ImageView imageView=new ImageView(image);
        imageView.setFitWidth(180);
        imageView.setFitHeight(130);

        //title
        Label title=new Label();
        title.getStylesheets().add("HStyle.css");
        title.getStyleClass().add("title");
        title.setText(cat.getTitre());
        //adding
        vBox.getChildren().addAll(imageView,title);
        cardCat.getChildren().add(vBox);
        return cardCat ;
    }
    public Pane PageContainer(int height){
        Pane page=new Pane();
        page.setPrefWidth(764);
        page.setPrefHeight(height);
        return page ;
    }
    public HBox Hbar(){
        HBox bar =new HBox();
        bar.setPadding(new Insets(8,0,8,10));
        bar.setSpacing(10);
        bar.setPrefSize(764,54);
        //labeltest
        Label test=new Label("");
        test.getStylesheets().add("HStyle.css");
        test.getStyleClass().add("test");
        //title
        Label title=new Label("Categories list");
        title.getStylesheets().add("HStyle.css");
        title.getStyleClass().add("title");
        //refresh
        Button refresh=new Button();
        FontAwesomeIconView RIcon = new FontAwesomeIconView(FontAwesomeIcon.REFRESH,"20");
        refresh.setGraphic(RIcon);
        refresh.getStylesheets().add("HStyle.css");
        refresh.getStyleClass().add("refresh");
        //addC

        bar.getChildren().addAll(title,test,refresh);
        return bar ;
    }
    public HBox HEbar(){
        HBox bar =new HBox();
        bar.setPadding(new Insets(8,0,8,10));
        bar.setSpacing(388);
        bar.setPrefSize(764,54);
        Label title=new Label("Categories list");
        title.getStylesheets().add("HStyle.css");
        title.getStyleClass().add("title");

        bar.getChildren().addAll(title);
        return bar ;
    }
    public  HBox line(int height,int pr){
        HBox line=new HBox();
        line.setPrefSize(762,height);
        line.setPadding(new Insets(13,pr,13,pr));
        line.setSpacing(41);
        return line;
    }
    public VBox table(int height){
        VBox container=new VBox();
        container.setPrefWidth(762);
        container.setPrefHeight(height);
        container.setSpacing(0);
        return container;
    }

    public Button back_btn(){
        Button back=new Button();
        FontAwesomeIconView BIcon = new FontAwesomeIconView(FontAwesomeIcon.ARROW_CIRCLE_ALT_LEFT,"25");
        back.setGraphic(BIcon);
        back.getStylesheets().add("HStyle.css");
        back.getStyleClass().add("back");
        return back;
    }
    public Button add(String addTitle){
        Button addCourse=new Button(addTitle);
        addCourse.getStylesheets().add("HStyle.css");
        addCourse.getStyleClass().add("Add");
        return addCourse;
    }
    public Label heading(String head){
        Label title=new Label(head);
        title.getStylesheets().add("HStyle.css");
        title.getStyleClass().add("title");
        return title;
    }
    public Button refr(){
        Button refresh=new Button();
        FontAwesomeIconView RIcon = new FontAwesomeIconView(FontAwesomeIcon.REFRESH,"20");
        refresh.setGraphic(RIcon);
        refresh.getStylesheets().add("HStyle.css");
        refresh.getStyleClass().add("refresh");
        return refresh;
    }
    public Pane CatDP(){
        Pane catD=new Pane();
        catD.getStylesheets().add("HStyle.css");
        catD.getStyleClass().add("catD");
        catD.setLayoutX(41);
        return catD;
    }
    public HBox CatDe(Categorie cat){

        HBox hBox=new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);

        //Image
        String cate="file:/"+trimmedPath+"/MagicMinds/public/uploads/images/"+cat.getImage();
        cate.replace("\\", "/");
        Image image=new Image(cate);
        System.out.println(cate);
        ImageView imageView=new ImageView(image);
        imageView.setFitWidth(180);
        imageView.setFitHeight(130);

        VBox desc=new VBox();
        desc.setPadding(new Insets(5,5,5,5));
        desc.setSpacing(8);

        //title
        Label title=new Label();
        title.getStylesheets().add("HStyle.css");
        title.getStyleClass().add("titleD");
        title.setText(cat.getTitre());
        //desc
        Label descr=new Label();
        descr.getStylesheets().add("HStyle.css");
        descr.getStyleClass().add("descr");
        descr.setText(cat.getDescription());
        descr.setWrapText(true);
        desc.getChildren().addAll(title,descr);
        hBox.getChildren().addAll(imageView,desc);
        return hBox;
    }
    public Button deleteC(){
        Button delete=new Button();
        FontAwesomeIconView DIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH,"30");
        DIcon.setFill(Color.web("#103741"));
        delete.setGraphic(DIcon);
        delete.getStylesheets().add("HStyle.css");
        delete.getStyleClass().add("back");
        return delete;
    }
    public Button deleteChap(){
        Button delete=new Button();
        FontAwesomeIconView DIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH,"20");
        DIcon.setFill(Color.web("#103741"));
        delete.setGraphic(DIcon);
        delete.getStylesheets().add("HStyle.css");
        delete.getStyleClass().add("del");
        return delete;
    }
    public Button UpdateC(){
        Button up=new Button();
        FontAwesomeIconView UIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL,"30");
        UIcon.setFill(Color.web("#103741"));
        up.setGraphic(UIcon);
        up.getStylesheets().add("HStyle.css");
        up.getStyleClass().add("back");
        return up;
    }
    public Button UpdateChap(){
        Button up=new Button();
        FontAwesomeIconView UIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL,"20");
        UIcon.setFill(Color.web("#103741"));
        up.setGraphic(UIcon);
        up.getStylesheets().add("HStyle.css");
        up.getStyleClass().add("del");
        return up;
    }
    public Pane courCard(Cours C){
        Pane cardC =new Pane();
        cardC.getStylesheets().add("HStyle.css");
        cardC.getStyleClass().add("cardC");

        VBox TandS=new VBox();
        TandS.setPadding(new Insets(10,10,10,10));
        TandS.setPrefSize(127,100);
        TandS.setSpacing(3);

        VBox ch=new VBox();
        ch.setPadding(new Insets(12,10,10,10));
        ch.setSpacing(5);
        ch.setAlignment(Pos.CENTER);

        HBox d=new HBox();
        d.setPrefSize(227,100);
        d.setSpacing(20);
        d.setPadding(new Insets(0,15,0,0));

        //title
        Label title=new Label();
        title.getStylesheets().add("HStyle.css");
        title.getStyleClass().add("titleC");
        title.setText(C.getTitre());

        Label status=new Label();
        status.setStyle("-fx-font-size: 16");
        status.setText(C.getStatus());

        Label chap=new Label();
        chap.getStylesheets().add("HStyle.css");
        chap.getStyleClass().add("Nchap");
        chap.setText(String.valueOf(C.getNb_chapitre()));
        Label tt=new Label("Chapter");
        tt.setStyle("-fx-font-size:12");
        //adding
        TandS.getChildren().addAll(title,status);
        ch.getChildren().addAll(chap,tt);
        d.getChildren().addAll(TandS,ch);
        cardC.getChildren().add(d);
        return cardC;
    }

    public HBox coursD(Cours c){
        HBox hBox=new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(90);
        hBox.setAlignment(Pos.CENTER);

        VBox desc=new VBox();
        desc.setPadding(new Insets(5,5,5,5));
        desc.setPrefSize(450,30);
        desc.setSpacing(8);

        HBox TandS=new HBox();
        TandS.setPadding(new Insets(5,5,5,5));
        TandS.setPrefSize(450,30);
        TandS.setSpacing((50));

        HBox PandCHAP=new HBox();
        PandCHAP.setPadding(new Insets(5,5,5,5));
        PandCHAP.setPrefSize(400,30);
        PandCHAP.setSpacing(50);

        //title
        Label title=new Label();
        title.getStylesheets().add("HStyle.css");
        title.getStyleClass().add("titleD");
        title.setText("Title: "+c.getTitre());
        //Status
        Label status=new Label("Status: "+ c.getStatus());
        status.setStyle("-fx-font-size: 14");

        //Period
        Label period=new Label();
        period.setStyle("-fx-font-size: 14");
        period.setStyle("-fx-pref-width:180");
        period.setText("Period: "+c.getDuree());
        //chapter
        Label chapter=new Label();
        chapter.setStyle("-fx-font-size: 14");
        chapter.setText("chapters: "+c.getNb_chapitre());
        //desc
        Label descr=new Label();
        descr.getStylesheets().add("HStyle.css");
        descr.getStyleClass().add("descr");
        descr.setText("Description: "+c.getDescription());
        descr.setWrapText(true);

        TandS.getChildren().addAll(title,status);
        PandCHAP.getChildren().addAll(period,chapter);
        desc.getChildren().addAll(TandS,PandCHAP,descr);
        hBox.getChildren().addAll(desc);
        return hBox;
    }
    public Pane Chapcarte(){
        Pane chapCard=new Pane();
        chapCard.getStylesheets().add("HStyle.css");
        chapCard.getStyleClass().add("cardChap");
        return chapCard;
    }
    public HBox Chapbox(Ressource r){
        HBox place=new HBox();
        place.setPrefSize(227,100);
        place.setSpacing(2);
        place.setPadding(new Insets(30,0,30,2));
        place.setAlignment(Pos.CENTER);
        //title
        Label title=new Label();
        title.getStylesheets().add("HStyle.css");
        title.getStyleClass().add("titleChap");
        title.setText(r.getTitre());

        place.getChildren().addAll(title);

        return place;
    }
    public Pane progressHolder(){
        Pane pH=new Pane();
        pH.setPrefSize(260,2);
        pH.setMaxHeight(16);
        pH.getStylesheets().add("HStyle.css");
        pH.getStyleClass().add("ph");
        return pH;
    }
    public Pane progressPane(int width,String x){
        Pane p=new Pane();
        p.setPrefHeight(16);
        p.setPrefWidth(width);
        p.setStyle("-fx-background-color:"+x);
        p.getStylesheets().add("HStyle.css");
        p.getStyleClass().add("prog");
        return p;
    }
    public Label mess(String mm ,String color){
        Label msg=new Label(mm);
        msg.setStyle("-fx-text-fill:"+color);
        msg.getStylesheets().add("HStyle.css");
        msg.getStyleClass().add("msg");
        return  msg ;
    }
    public Pane progressCard(Pane progressPane,String catName,Label msg){
        Pane card=new Pane();
        card.getStylesheets().add("HStyle.css");
        card.getStyleClass().add("cardPro");
        HBox hBox=new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(8,10,5,20));
        Label title=new Label();
        title.getStylesheets().add("HStyle.css");
        title.getStyleClass().add("titleC");
        title.setText(catName);
        hBox.getChildren().addAll(title,progressPane,msg);
        card.getChildren().add(hBox);
        return card;
    }
    public VBox colomns(){
        VBox vBox=new VBox();
        vBox.setPrefWidth(635);
        vBox.setPrefHeight(430);
        vBox.setSpacing(0);
        return vBox;
    }
    public HBox tabLine(){
        HBox hBox=new HBox();
        hBox.setPrefWidth(500);
        hBox.setPrefHeight(70);
        hBox.setPadding(new Insets(8,8,8,8));
        hBox.setSpacing(10);
        return hBox;
    }











}
