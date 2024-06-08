package Controller;

import Entity.Command;
import Service.CommandCRUD;
import Service.ProduitCRUD;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CommandBackController implements Initializable {
    @FXML
    private Button btn_corses;

    @FXML
    private Button btn_events;

    @FXML
    private Button btn_forum;

    @FXML
    private Button btn_quizzs;

    @FXML
    private Button btn_store;

    @FXML
    private Button btn_user;

    @FXML
    private TableView<Command>  commandTable;

    @FXML
    private TableColumn<Command, Integer> idCommand;

    @FXML
    private Pane inner_pane;

    @FXML
    private TableColumn<Command, Integer> qt;

    @FXML
    private HBox root;

    @FXML
    private AnchorPane side_bar;

    @FXML
    private TableColumn<Command, Double> totalprice;

    @FXML
    private TextField txt_search;

    @FXML
    private TableColumn<Command, String> username;
    private CommandCRUD commandService;
    private Command command;
    ProduitCRUD ps =new ProduitCRUD();
    CommandCRUD cm=new CommandCRUD();
    public void initialize(URL location, ResourceBundle resources) {
        commandService = new CommandCRUD();

        // Initialize table columns
        idCommand.setCellValueFactory(new PropertyValueFactory<>("id"));
        totalprice.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Custom cell value factory for the username column
        username.setCellValueFactory(cellData -> new SimpleStringProperty(getUsername(cellData.getValue().getId_user())));

        // Custom cell value factory for quantity
        qt.setCellValueFactory(cellData -> {
            int quantity = cellData.getValue().getId_produit().size();
            return new SimpleIntegerProperty(quantity).asObject();
        });

        // Load the data into the table
        loadCommandData();
    }

    private String getUsername(int userId) {
        try {
            return commandService.getUsername(userId);
        } catch (SQLException e) {
            e.printStackTrace(); // handle this better in real applications
            return "Error";
        }
    }

    private void loadCommandData() {
        try {
            ObservableList<Command> commands = FXCollections.observableArrayList(commandService.afficherAll());
            commandTable.setItems(commands);
        } catch (SQLException e) {
            e.printStackTrace(); // handle this better in real applications
        }
    }
    @FXML
    void pdf_facture(ActionEvent event) {
        Command selectedCommand = commandTable.getSelectionModel().getSelectedItem();

        if (selectedCommand == null) {
            // No command selected, show an alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Command Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a command from the table.");
            alert.showAndWait();
            return;
        }

        // Assign the selected command to the local 'command' object
        command = selectedCommand;

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Draw the logo with padding from the top
            PDImageXObject pdImage = PDImageXObject.createFromFile("src/main/resources/images/mg1.png", document);
            contentStream.drawImage(pdImage, 20, 690, 100, 50); // Adjusted padding from the top

            // Title
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
            contentStream.newLineAtOffset(200, 720);
            contentStream.showText("Facture");
            contentStream.endText();
            //name client
            float yStart = 690; // Initial y position
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(200, yStart);
            contentStream.showText("Client Name: " + cm.getUsername(command.getId_user()));
            contentStream.newLine();
            contentStream.newLineAtOffset(0, -20); // Space between name and address
            contentStream.showText("Company Address: 1234 Street Name, City, Country");
            contentStream.endText();

            yStart -= 60; // Additional space before starting table

            // Table setup
            float margin = 50;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float rowHeight = 20;
            int numCols = 3;
            int numRows = command.getId_produit().size() + 1; // Number of products + headers
            float tableHeight = numRows * rowHeight;

            // Draw Table Borders
            drawTableBorders(contentStream, margin, yStart, tableWidth, tableHeight, numRows, numCols);

            // Draw the table headers
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            float textx = margin;
            float texty = yStart - 15;
            String[] headers = {"  Product Name", "  Quantity", "  Price Total"};
            for (String header : headers) {
                contentStream.beginText();
                contentStream.newLineAtOffset(textx, texty);
                contentStream.showText(header);
                contentStream.endText();
                textx += tableWidth / numCols;
            }

            // Draw table rows
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            texty -= rowHeight;
            for (int productId : command.getId_produit()) {
                String productName = ps.getById(productId).getNom();
                double productPrice = ps.getById(productId).getPrix();
                int productQuantity = 1;

                textx = margin;
                String[] rowData = {"  "+productName, "  "+String.valueOf(productQuantity), "   $"+String.valueOf(productPrice * productQuantity)};
                for (String data : rowData) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(textx, texty);
                    contentStream.showText(data);
                    contentStream.endText();
                    textx += tableWidth / numCols;
                }
                texty -= rowHeight;
            }

            // Draw total price
            textx = margin;
            texty -= rowHeight;
            contentStream.beginText();
            contentStream.newLineAtOffset(textx, texty);
            contentStream.showText("Total Price: $ " + command.getTotal());
            contentStream.endText();

            contentStream.close();
            document.save("command.pdf");

            // Open the generated PDF file automatically
            Desktop.getDesktop().open(new File("command.pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    private void drawTableBorders(PDPageContentStream contentStream, float startX, float startY, float width, float height, int numRows, int numCols) throws IOException {
        contentStream.setLineWidth(1f);
        float cellWidth = width / numCols;
        float cellHeight = height / numRows;

        // Draw horizontal lines
        for (int i = 0; i <= numRows; i++) {
            contentStream.moveTo(startX, startY - (i * cellHeight));
            contentStream.lineTo(startX + width, startY - (i * cellHeight));
            contentStream.stroke();
        }

        // Draw vertical lines
        for (int i = 0; i <= numCols; i++) {
            contentStream.moveTo(startX + (i * cellWidth), startY);
            contentStream.lineTo(startX + (i * cellWidth), startY - height);
            contentStream.stroke();
        }
    }
    @FXML
    void gotostore(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterProduit.fxml"));
            Parent storeRoot = loader.load();

            Stage stage = null;
            // Attempt to get the stage from the event source if it's a Node
            if (event.getSource() instanceof Node) {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            }
            // If the stage could not be retrieved from the event, try the current scene's window
            if (stage == null && event.getPickResult().getIntersectedNode() != null) {
                stage = (Stage) event.getPickResult().getIntersectedNode().getScene().getWindow();
            }
            // Ensure that we have a valid stage to use
            if (stage != null) {
                Scene scene = new Scene(storeRoot);
                stage.setScene(scene);
                stage.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions possibly thrown by the FXML loading
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load the store view.");
            alert.showAndWait();
        }
    }
    @FXML
    void delete_command(ActionEvent event) {
        Command selectedCommand = commandTable.getSelectionModel().getSelectedItem();

        // Check if a command is selected
        if (selectedCommand == null) {
            // If no command is selected, show an alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Command Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a command from the table to delete.");
            alert.showAndWait();
        } else {
            try {
                // Delete the selected command from the database
                commandService.supprimer(selectedCommand);

                // Remove the selected command from the table view
                commandTable.getItems().remove(selectedCommand);
            } catch (SQLException e) {
                // Handle any SQL exceptions
                e.printStackTrace();
                // Show an error alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while deleting the command.");
                alert.showAndWait();
            }
        }
    }

}
