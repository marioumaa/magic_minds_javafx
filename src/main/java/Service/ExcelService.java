package Service;

import Entity.User;


import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ExcelService {

    public void generateExcel(List<User> listView) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
        );
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet1");



            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("firstname");
            headerRow.createCell(1).setCellValue("lastname");
            headerRow.createCell(2).setCellValue(" email ");
            headerRow.createCell(3).setCellValue("age ");
            headerRow.createCell(4).setCellValue("gender");





            for (int i = 0; i < listView.size(); i++) {
                Row dataRow = sheet.createRow(i + 1);


                dataRow.createCell(0).setCellValue(listView.get(i).getFirstName());
                dataRow.createCell(1).setCellValue(listView.get(i).getLastName());
                dataRow.createCell(2).setCellValue(listView.get(i).getEmail());
                dataRow.createCell(3).setCellValue( listView.get(i).getAge());
                dataRow.createCell(4).setCellValue(listView.get(i).getGender());


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
