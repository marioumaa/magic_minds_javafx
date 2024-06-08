package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.concurrent.Worker;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class PreviewPDF implements Initializable {

    @FXML
    private WebView pdfViewer;
    String pathName;
    String userDir = System.getProperty("user.dir").replace("\\", "/");
    String trimmedPath = userDir.substring(0, userDir.lastIndexOf("/")).replace("\\", "/");

    public void fetchPDF(String url) {
        pathName = url;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WebEngine engine = pdfViewer.getEngine();
        String url = null;
        try {
            url = getClass().getResource("/web/viewer.html").toURI().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            engine.setUserStyleSheetLocation(getClass().getResource("/web/viewer.css").toURI().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        engine.setJavaScriptEnabled(true);
        engine.load(url);
        engine.getLoadWorker()
                .stateProperty()
                .addListener((obs, oldV, newV) -> {
                    if (Worker.State.SUCCEEDED == newV) {
                        InputStream stream = null;
                        try {
                            Outil outil=new Outil();
                            String url1=System.getProperty("user.dir").replace("\\", "/")+"/src/main/resources/UploadedFile/"+pathName;
                            InputStream is = getClass().getResourceAsStream(outil.trimUrl(url1,2));
                            byte[] bytes = IOUtils.toByteArray(is);
                            String base64 = Base64.getEncoder().encodeToString(bytes);

                            engine.executeScript("openFileFromBase64('" + base64 + "')");

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            if (stream != null) {
                                try {
                                    stream.close();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                });

    }

}