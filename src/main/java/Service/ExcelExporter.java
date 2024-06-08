package Service;

import Entity.Evaluation;
import Entity.Questions;
import Entity.Quiz;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

public class ExcelExporter {
    public void generateExcel(List<Evaluation> evaluations) throws SQLException, IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
        );
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Evaluation Data");

            Row magicRow = sheet.createRow(0); // Créer la rangée pour "Magic minds"
            sheet.addMergedRegion(new CellRangeAddress(
                    0, // Première ligne
                    1, // Dernière ligne (trois lignes en tout)
                    0, // Première colonne
                    3  // Dernière colonne (quatre premières colonnes)
            ));
            Cell magicCell = magicRow.createCell(0);
            magicCell.setCellValue("Magic minds");




            // Appliquer le style à la cellule "Magic minds"
            CellStyle magicCellStyle = workbook.createCellStyle();
            magicCellStyle.setAlignment(HorizontalAlignment.CENTER);
            magicCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex()); // Couleur de fond
            magicCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font magicFont = workbook.createFont();
            magicFont.setFontHeightInPoints((short) 16); // Taille de la police
            magicFont.setBold(true); // Texte en gras
            magicCellStyle.setFont(magicFont);
            magicCell.setCellStyle(magicCellStyle);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex()); // Couleur de fond foncée
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 13);
            headerFont.setColor(IndexedColors.WHITE.getIndex()); // Couleur du texte en blanc
            headerStyle.setFont(headerFont);

            // Création du style pour les cellules de données
            CellStyle dataCellStyle = workbook.createCellStyle();
            dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
            // Couleur de fond pour les lignes de données
            CellStyle dataStyle = workbook.createCellStyle();

            dataStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            dataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Row headerRow = sheet.createRow(2);

            String[] headers = {"ID User", "Quiz Title", "Result", "Date"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle); // Appliquer le style aux en-têtes de colonne
            }


            // Ajoutez les en-têtes des autres colonnes ici

            for (int i = 0; i < evaluations.size(); i++) {
                Row dataRow = sheet.createRow(i + 3); // +1 pour éviter d'écraser l'en-tête
                Evaluation campaign = evaluations.get(i);
                int quizId=campaign.getId_quiz_id();
                QuizCrud d=new QuizCrud();
                Quiz q=d.getByQuizId(quizId);
                int r=campaign.getResultat();
                QuestionsCrud qs=new QuestionsCrud();
                List<Questions>l=qs.recupererParQuizId(quizId);
                String rr=r+"/"+l.size();
                dataRow.createCell(0).setCellValue(campaign.getId_user_id());
                dataRow.createCell(1).setCellValue(q.getTitre());
                dataRow.createCell(2).setCellValue(rr);
                LocalDate localDate = campaign.getDate();
                // Convertir LocalDate en java.util.Date
                java.util.Date utilDate = java.sql.Date.valueOf(localDate);

// Convertir java.util.Date en java.sql.Date
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

// Formatter de date pour formater la date en format LocalDate
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

// Formatter de date pour convertir java.sql.Date en String
                String formattedDate = dateFormat.format(sqlDate);

// Utiliser la date formatée dans la méthode setCellValue
                dataRow.createCell(3).setCellValue(formattedDate);

                for (Cell cell : dataRow) {
                    cell.setCellStyle(dataCellStyle);
                }

                for (int j = 0; j < headers.length; j++) {
                    Cell cell = dataRow.getCell(j);
                    if (cell != null) {
                        cell.setCellStyle(dataStyle);
                    }
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                workbook.close();
                System.out.println("Excel generated successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}