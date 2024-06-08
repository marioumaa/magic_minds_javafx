package Controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class PreviewFile implements Initializable {

    @FXML
    private ImageView ImgP;

    @FXML
    private MediaView VideoP;
    @FXML
    private Pane fullscreen;
    @FXML
    private Pane media_control;

    @FXML
    private Button minus;

    @FXML
    private Button play;
    @FXML
    private Label ongoing;

    @FXML
    private Button plus;

    @FXML
    private HBox volume;

    String userDir = System.getProperty("user.dir").replace("\\", "/");
    String trimmedPath = userDir.substring(0, userDir.lastIndexOf("/")).replace("\\", "/");

    String pathName="";
    String type="" ;

    public void fetchUrl(String url, String Type) {
        type=Type ;
        pathName = url;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ImgP.setVisible(false);
        VideoP.setVisible(false);
        media_control.setVisible(false);
        if (type.equals("Video")){
            VideoP.setVisible(true);
            media_control.setVisible(true);
            String mediaFile="file:/"+trimmedPath+"/MagicMinds/public/uploads/ressources/"+pathName;
            mediaFile.replace("\\", "/");
            System.out.println(mediaFile);
            Media video = new Media(mediaFile);
            MediaPlayer mediaPlayer = new MediaPlayer(video);
            mediaPlayer.setAutoPlay(true);
            VideoP.setMediaPlayer(mediaPlayer);
            VideoP.setPreserveRatio(false);
            play_stop();
            plus10();
            minus10();
            volumeEdit();
            timing();
        }
        else if (type.equals("Image")){
            ImgP.setVisible(true);
            Image image =new Image("file:/"+trimmedPath+"/MagicMinds/public/uploads/ressources/"+pathName);
            ImgP.setImage(image);
        }
    }
    public void play_stop() {
        FontAwesomeIconView playI = new FontAwesomeIconView(FontAwesomeIcon.PAUSE,"18");
        play.setGraphic(playI);
        MediaPlayer mediaPlayer=VideoP.getMediaPlayer();
        play.setOnAction(e -> {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                FontAwesomeIconView playIcon = new FontAwesomeIconView(FontAwesomeIcon.PLAY,"20");
                play.setGraphic(playIcon);
            } else {
                mediaPlayer.play();
                FontAwesomeIconView playIcon = new FontAwesomeIconView(FontAwesomeIcon.PAUSE,"18");
                play.setGraphic(playIcon);
            }
        });
    }
    public void minus10() {
        FontAwesomeIconView advanceIcon = new FontAwesomeIconView(FontAwesomeIcon.ROTATE_LEFT,"20");
        minus.setGraphic(advanceIcon);
        minus.setOnAction(e -> {
            MediaPlayer mediaPlayer=VideoP.getMediaPlayer();
            Duration currentTime = mediaPlayer.getCurrentTime();
            Duration newTime = currentTime.subtract(Duration.seconds(10));
            mediaPlayer.seek(newTime);
        });
    }

    public void plus10() {
        FontAwesomeIconView advanceIcon = new FontAwesomeIconView(FontAwesomeIcon.ROTATE_RIGHT,"20");
        plus.setGraphic(advanceIcon);
        plus.setOnAction(e -> {
            MediaPlayer mediaPlayer=VideoP.getMediaPlayer();
            Duration currentTime = mediaPlayer.getCurrentTime();
            Duration newTime = currentTime.add(Duration.seconds(10));
            mediaPlayer.seek(newTime);
        });
    }
    public void volumeEdit(){
        Slider volumeSlider = new Slider(0, 1, 0.5);
        volumeSlider.setPadding(new Insets(12,0,0,0));
        MediaPlayer mediaPlayer=VideoP.getMediaPlayer();
        mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty());
        Button vol = new Button();
        vol.setStyle("-fx-background-color:#cccccc");
        vol.setPadding(new Insets(9,0,0,0));
        FontAwesomeIconView advanceIcon = new FontAwesomeIconView(FontAwesomeIcon.VOLUME_UP,"20");
        vol.setGraphic(advanceIcon);
        vol.setOnAction(e -> {
            if (mediaPlayer.isMute()) {
                mediaPlayer.setMute(false);
                FontAwesomeIconView advanceI = new FontAwesomeIconView(FontAwesomeIcon.VOLUME_UP,"20");
                vol.setGraphic(advanceI);
            } else {
                mediaPlayer.setMute(true);
                FontAwesomeIconView advanceI = new FontAwesomeIconView(FontAwesomeIcon.VOLUME_OFF,"20");
                vol.setGraphic(advanceI);
            }
        });
        volume.getChildren().addAll(vol,volumeSlider);

    }
    public void timing(){
        ongoing.setText("00:00 / 00:00");
        MediaPlayer mediaPlayer=VideoP.getMediaPlayer();
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            Duration currentTime = mediaPlayer.getCurrentTime();
            Duration duration = mediaPlayer.getMedia().getDuration();
            String formattedTime = formatTime(currentTime) + " / " + formatTime(duration);
            ongoing.setText(formattedTime);
        });
    }

    private String formatTime(Duration duration) {
        int totalSeconds = (int) Math.floor(duration.toSeconds());
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}