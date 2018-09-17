package kempot.domparser;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import kempot.jexcel.ReadExcel;


public class GetMarginalProb {
	
	public static float MargProb[] = new float[10];
	private String inputFile;
	public String workingSheet;
	
//    public static void main(String[] args) throws IOException {
//    	GetMarginalProb test = new GetMarginalProb();
//        test.setInputFile("Crohn.xls");
//        test.setWorkingSheet("Genetic");
//        test.read();
//    }

	public GetMarginalProb(String inputFile, String workingSheet) throws IOException  {
        File inputWorkbook = new File(inputFile);
        Workbook w;
        try {
            w = Workbook.getWorkbook(inputWorkbook);

            //for (int k=0; k < w.getNumberOfSheets(); k++){
            	Sheet sheet = w.getSheet(workingSheet);
            	//System.out.println(workingSheet);
                //for (int j = 0; j < 1; j++) {
                        for (int i = 0; i < sheet.getRows(); i++) {
                        		//System.out.println(sheet.getRows()+" "+i);
                                Cell cell = sheet.getCell(0, i);
                                CellType type = cell.getType();

                                if (type == CellType.NUMBER) {
                                		float temp = Float.parseFloat(cell.getContents());
                                		MargProb[i]=temp;
                                        //System.out.println(MargProb[i]);
                                }	
                        }
                //}
           //}
            
            w.close();
            
    } catch (BiffException e) {
            e.printStackTrace();
    }
        
}
	
}
