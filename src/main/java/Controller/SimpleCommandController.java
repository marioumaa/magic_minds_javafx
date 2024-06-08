package Controller;

import Entity.Command;
import Service.CommandCRUD;
import Service.ProduitCRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
public class SimpleCommandController {

    @FXML
    private Label id;

    @FXML
    private Label qt;

    @FXML
    private Label totalprice;
    private Command command;
    ProduitCRUD ps =new ProduitCRUD();
    CommandCRUD cm=new CommandCRUD();
    public void setCommand(Command command) {
        this.command = command;
        id.setText(String.valueOf(command.getId()));
        qt.setText(String.valueOf(command.getId_produit().size()));
        totalprice.setText(String.valueOf(command.getTotal()));
    }

    @FXML
    void pdf(ActionEvent event) {
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
}


