import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
	Database DBC = new Database();

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
		System.out.println("Total No.of records: "+ (sheet.getPhysicalNumberOfRows() - 1)); 
		String datacolumn[][] = new String[sheet.getPhysicalNumberOfRows()][8];
		
		int rowcount = 0;
		int cellcount = 0;
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
            	   datacolumn[rowcount][cellcount] = getdatefromcell(cell.toString(),cellcount);
            	   //System.out.print(getdatefromcell(cell.toString(),cellcount) +"\t");
               }
               else {
               if(cell.getCellType() == CellType.FORMULA) {
            	   switch(cell.getCachedFormulaResultType()) {
            	   case NUMERIC:
            		   datacolumn[rowcount][cellcount] = String.valueOf(cell.getNumericCellValue());
            		 //  System.out.print(cell.getNumericCellValue() + "\t");
            		   break;
            	   case ERROR:
            		   if(cell.getErrorCellValue() == 7) {
            			   datacolumn[rowcount][cellcount] = "0";
            			//   System.out.print(0 + "\t");
            		   }
            		   break;
				   default:
					   System.out.print("unknown");
					   break;
            	   	}      	   
               }else {
            	datacolumn[rowcount][cellcount] = String.valueOf(cell);   
               // System.out.print(cell + "\t"); 
               }
            }
               cellcount++;
            }
        }
            //insert/update DB based on current row
            rowcount++;
        } 
        testlist(datacolumn);
	}
	
	private String getdatefromcell(String filedate,int datecolumn) {
		String dateinstring = filedate;	
		LocalDateTime localdate = null;
		if(!dateinstring.isEmpty()) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
		switch(datecolumn) {
		case 4:
			localdate = LocalDateTime.parse(dateinstring + " 00:00:00", dtf);
			break;
		case 5:
			localdate = LocalDateTime.parse(dateinstring + " 23:59:59", dtf);
			break;
		}
		//System.out.print(localdate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy \t")));
		return localdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		}else {
			System.out.print("empty space \t");
			return null;
		}	
	}
	
	private void testlist(String datacolumn[][]) {
		System.out.println("reading list:");
		for (int i = 1; i< datacolumn.length; i++) {
			for(int j = 0; j< 6; j++) {
				System.out.print(datacolumn[i][j].toString() + "\t");
			}
			System.out.println("");
		}
		
	}

}
