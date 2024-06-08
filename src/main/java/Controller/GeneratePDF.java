package Controller;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import Entity.Categorie;
import Entity.Cours;
import Service.CategorieService;
import Service.CoursService;
import javafx.collections.ObservableList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class GeneratePDF {
    CategorieService categorieService= new CategorieService();
    CoursService coursService = new CoursService();
    public void pdf(String pathToD)  {
        try {
            ObservableList<Categorie> categories = categorieService.getAll();
            PDDocument pdf = new PDDocument();
            PDPage page1 = new PDPage(PDRectangle.A4);
            pdf.addPage(page1);
            String imagePath = "C:/Users/achra/Desktop/New folder/Magic-Minds-JavaFX/src/main/resources/images/logo.png";
            PDImageXObject image = null;

            image = PDImageXObject.createFromFile(imagePath, pdf);

            PDPageContentStream contentStream1 = new PDPageContentStream(pdf, page1, AppendMode.APPEND, true);
            PDRectangle mediaBox = page1.getMediaBox();
            float pageWidth = mediaBox.getWidth();
            float pageHeight = mediaBox.getHeight();

            // Calculate the coordinates to position the image in the center
            float imageWidth = 400;
            float imageHeight = 400;
            float startIX = (pageWidth - imageWidth) / 2;
            float startIY = ((pageHeight - imageHeight) / 2)+40;
            contentStream1.drawImage(image, startIX, startIY, imageWidth, imageHeight);
            startIY -= 20;
            contentStream1.setFont(PDType1Font.HELVETICA_BOLD, 20);
            contentStream1.setStrokingColor(0,48,143);
            contentStream1.beginText();
            contentStream1.newLineAtOffset(startIX+30, startIY);
            contentStream1.showText("List of categories and their courses");
            contentStream1.endText();
            contentStream1.close();


            for (Categorie c : categories) {
                PDPage page = new PDPage(PDRectangle.A4);
                pdf.addPage(page);
                PDPageContentStream contentStream = new PDPageContentStream(pdf, page, AppendMode.APPEND, true);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                float startX = 50;
                float startY = page.getMediaBox().getHeight() - 50;

                // Categorie
                contentStream.beginText();
                contentStream.newLineAtOffset(startX, startY);
                contentStream.showText("Category title: " + c.getTitre());
                contentStream.endText();
                startY -= 20;
                contentStream.beginText();
                contentStream.newLineAtOffset(startX, startY);
                contentStream.showText("Total of courses: " + c.getNbr_cours());
                contentStream.endText();
                startY -= 20;
                contentStream.beginText();
                contentStream.newLineAtOffset(startX, startY);
                contentStream.showText("Total of chapters: " + c.getNbr_chapitre());
                contentStream.endText();

                // Table of courses
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                float tableStartX = startX;
                float tableStartY = startY -50;
                float cellWidth = 120;
                float cellHeight = 20;
                
                List<Cours> CL=coursService.getCoursesByCat(c.getId());
                String[][] CTable = new String[CL.size() + 1][4];
                CTable[0][0] = "Title";
                CTable[0][1] = "Status";
                CTable[0][2] = "Period";
                CTable[0][3] = "Number of chapters";
                for (int r = 1; r < CTable.length; r++) {
                    Cours cours = CL.get(r - 1);  // Retrieve the course from the list

                    CTable[r][0] = cours.getTitre();
                    CTable[r][1] = cours.getStatus();
                    CTable[r][2] = String.valueOf(cours.getDuree());
                    CTable[r][3] = String.valueOf(cours.getNb_chapitre());
                }
                for (int row = 0; row < CTable.length; row++) {
                    for (int col = 0; col < CTable[row].length; col++) {
                        float x = tableStartX + (cellWidth * col);
                        float y = tableStartY - (cellHeight * row);
                        contentStream.setLineWidth(1);
                        contentStream.setStrokingColor(0, 0, 0); // Black border color
                        contentStream.addLine(x, y, x + cellWidth, y); // Top border
                        contentStream.addLine(x, y, x, y - cellHeight); // Left border
                        contentStream.addLine(x + cellWidth, y, x + cellWidth, y - cellHeight); // Right border
                        contentStream.addLine(x, y - cellHeight, x + cellWidth, y - cellHeight); // Bottom border
                        contentStream.setNonStrokingColor(0, 0, 0); // Black text color
                        contentStream.beginText();
                        contentStream.newLineAtOffset(x + 5, y + 15); // Offset to position the text inside the cell
                        contentStream.showText(CTable[row][col]);
                        contentStream.endText();
                    }
                }
                contentStream.close();
            }
            pdf.save(pathToD+"/Categories.pdf");
            pdf.close();

            System.out.println("Table created successfully.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}