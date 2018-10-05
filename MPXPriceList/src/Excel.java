import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Excel {
	public static final String PriceListFile = "./src/Pricelist.xlsx";
	private static Workbook workbook;

	public Excel() throws EncryptedDocumentException, IOException {
		// TODO Auto-generated constructor stub
		workbook = WorkbookFactory.create(new File(PriceListFile));
		System.out.println("Workbook has " + workbook.getNumberOfSheets() + " sheets: ");

		
		for(Sheet sheet: workbook) {
			if(sheet.getSheetName().contains("Pricing")) {
				String sheetname = sheet.getSheetName();
				System.out.println("This is a Pricing Sheet, Do Pricing Queries");
				readContents(workbook.getSheet(sheetname));
			}
			else if(sheet.getSheetName().contains("Holdback")) {
				String sheetname = sheet.getSheetName();
				System.out.println("This is a Holdback Sheet, Do Holdback Queries");
				readContents(workbook.getSheet(sheetname));
			} else {
				System.out.println(sheet.getSheetName() + ", this sheet will not be read.");
			}
		}
	}
	
	
	private void readContents(Sheet sheet) {
		System.out.println("reading...");
		int rowcount = 0;
		int cellcount = 0;
		DataFormatter dataFormatter = new DataFormatter();
		Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if(rowcount != 0) {
            // Now let's iterate over the columns of the current row
            Iterator<Cell> cellIterator = row.cellIterator();
            cellcount = 0;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
               if(cellcount == 4 || cellcount == 5) {
            	   getdatefromcell(cell.toString());
               }
               else {
               if(cell.getCellType() == CellType.FORMULA) {
            	   switch(cell.getCachedFormulaResultType()) {
            	   case NUMERIC:
            		   System.out.print(cell.getNumericCellValue() + "\t");
            		   break;
            	   case ERROR:
            		   if(cell.getErrorCellValue() == 7) {
            			   System.out.print(0 + "\t");
            		   }
            		   break;
				   default:
					   System.out.print("unknown");
					   break;
            	   	}      	   
               }else {
            	   if(cell.getCellType() == CellType.FORMULA && cell.getNumericCellValue() < 0) {
            		   System.out.print(0 + "\t"); 
            	   }
 
                System.out.print(cell + "\t"); 
               }
              cellcount++;
            }
            }
            System.out.println();
        }
            //insert/update DB based on current row
            rowcount++;
        }
        
       //convertdate();
	}
	
	
	private String convertdate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDateTime currentDate = LocalDateTime.now();
		System.out.println(dtf.format(currentDate));
		return null;
	}
	
	private void getdatefromcell(String filedate) {
		String dateinstring = filedate;	
		if(!dateinstring.isEmpty()) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		LocalDate localdate = LocalDate.parse(dateinstring, dtf);
		System.out.print(localdate.format(dtf.ofPattern("dd/MM/yyyy \t")));
		}else {
			System.out.print("empty space \t");
		}
	
		 
	}

}
