package com.epam.xlsreader;

import com.epam.converter.Main;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Almas_Doskozhin
 * on 1/26/2018.
 */
public class XLSReader {
    private static Map<String, double[]> coeffMap = new HashMap<>();
    private int firstBracketCellNumber;
    private int maxCellNumber;


    public Map<String, double[]> readXls() {
        try {
            FileInputStream excelFile = new FileInputStream(new File(Main.class.getClassLoader().getResource(Main.FILE_NAME).toURI()));

            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();

            readHeaderRow(iterator);

            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                String logRoute = "";
                int cellNumber = 0;
                int numericCellNumber = 0;

                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();

                    if (cellNumber == 0) {
                        logRoute = currentCell.getStringCellValue();
                        if (!logRoute.isEmpty()) {
                            double[] coeffs = new double[maxCellNumber - firstBracketCellNumber + 1];
                            coeffMap.put(logRoute, coeffs);
                        }
                    }


                    //getCellTypeEnum shown as deprecated for version 3.15
                    //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
//                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
//                        if(cellNumber >= firstBracketCellNumber) {
//                            coeffMap.get(logRoute)[cellNumber-firstBracketCellNumber] = Double.parseDouble(currentCell.getStringCellValue());
//                        }
//                    } else

                    if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        coeffMap.get(logRoute)[numericCellNumber] = currentCell.getNumericCellValue();
                        numericCellNumber++;
                    }

                    cellNumber++;
                }
                System.out.println();

            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        System.out.println("Read success");
        return coeffMap;
    }

    private void readHeaderRow(Iterator<Row> iterator) {
        boolean firstBracketFound = false;
        int cellNumber = 0;
        Row headerRow = iterator.next();

        for (Cell currentCell : headerRow) {
            if (currentCell.getCellTypeEnum() == CellType.STRING) {
                if (!firstBracketFound && currentCell.getStringCellValue().startsWith("[")) {
                    firstBracketCellNumber = cellNumber;
                    firstBracketFound = true;
                }
            } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                System.out.print(currentCell.getNumericCellValue() + "--");
            }
            cellNumber++;
        }

        maxCellNumber = cellNumber - 1;
        System.out.println();
    }
}
