package model;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import structure.BinarySearchTree;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This was taken directly from your video guides, but modified to work with newer .xlsx files.
 * This reads the excel file and places it inside of an arraylist.
 */
public class ReadExcelFile {

    /** Throws an exception if an error occurs while reading the excel file.
     *
     *  Reads through the excel file specified, and places the row and column information inside of an arraylist.
     *
     * @param newFilename
     * @return
     * @throws Exception
     */

    public static ArrayList readExcelFile(String newFilename) throws Exception{

        String filename = newFilename;

        ArrayList sheetData = new ArrayList();
        FileInputStream fileInputStream = null;
        //InputStream fileInputStream = null;

        try{

            fileInputStream = new FileInputStream(filename);

            // Create an excel workbook form the file system

            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

            // Grab the first sheet in the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //https://poi.apache.org/apidocs/org/apache/poi/ss/usermodel/DataFormatter.html
            //https://stackoverflow.com/questions/30125465/cannot-get-a-text-value-from-a-numeric-cell-poi
            DataFormatter formatter = new DataFormatter();

            Iterator rows = sheet.rowIterator();
            while (rows.hasNext()){
                XSSFRow row = (XSSFRow) rows.next();
                Iterator cells = row.cellIterator();

                ArrayList data = new ArrayList();
                while (cells.hasNext()){
                    XSSFCell cell = (XSSFCell) cells.next();

                    data.add(formatter.formatCellValue(cell));
                }

                sheetData.add(data);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        //The finally keyword is used in a try catch statement to specify a block of code to execute regardless of thrown exceptions.
        finally{
            // If it's null then that means the file is already closed.
            if (fileInputStream != null){
                fileInputStream.close();
            }
        }

        return sheetData;

    }

    public static void showExcelData(ArrayList sheetData){

        for (int i = 0; i < sheetData.size(); i++){
            ArrayList list = (ArrayList) sheetData.get(i);
            for (int j = 0; j < list.size(); j++){
                Cell cell = (Cell) list.get(j);
                if (cell.getCellTypeEnum() == CellType.NUMERIC){
                    System.out.print(cell.getNumericCellValue());
                }
                else if (cell.getCellTypeEnum() == CellType.STRING){
                    System.out.print(cell.getRichStringCellValue());
                }
                if (j < list.size() - 1){
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }
}
