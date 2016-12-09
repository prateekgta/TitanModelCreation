	import java.io.File;
	import java.io.FileInputStream;
	import java.util.ArrayList;
	import java.util.Iterator;
	import org.apache.poi.ss.usermodel.Cell;
	import org.apache.poi.ss.usermodel.Row;
	import org.apache.poi.ss.usermodel.Sheet;
	import org.apache.poi.ss.usermodel.Workbook;
	import org.apache.poi.ss.usermodel.WorkbookFactory;
	
	public class ReadExcel
	{
		static ArrayList<ArrayList<String>> userdata;
		static ArrayList<ArrayList<String>> tweetdata1;
		static ArrayList<ArrayList<String>> mentioned_user;
		static ArrayList<ArrayList<String>> hashtag;
		static ArrayList<ArrayList<String>> url_used;
		
		
		
		
		static int count = 0;
		// read xls file to store output in arrayList of arrayList
	    public static ArrayList<ArrayList<String>> readxls(String excelPath) throws Exception
	    {
	    	count = 0;
	    	ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
	      
	    	FileInputStream inputStream = new FileInputStream(new File(excelPath));   
	    	Workbook workbook = WorkbookFactory.create(inputStream);
	        Sheet firstSheet = workbook.getSheetAt(0);
	        Iterator<Row> iterator = firstSheet.iterator();
	        
	        ArrayList<ArrayList<String>> farrayList = new ArrayList<ArrayList<String>>();
	      
	        while(iterator.hasNext()){ 
	    	 
	        	Row nextRow = iterator.next();
	            
	            ArrayList<String> newarray = new ArrayList<String>();
	            
	            Iterator<Cell> cellIterator = nextRow.cellIterator();
	             
	            while (cellIterator.hasNext()) {
	                Cell cell = cellIterator.next();
	                switch (cell.getCellType()) {
	                    case Cell.CELL_TYPE_STRING:
	                    	
	                    	 String s = cell.getStringCellValue();
	           //        	 System.out.println(s);
	                    	 newarray.add(s);
	                         break;
	                    case Cell.CELL_TYPE_NUMERIC:
	                    	 double i = cell.getNumericCellValue();
	                    	 newarray.add(String.valueOf(i));
	                         break;
	                    case Cell.CELL_TYPE_BOOLEAN:
	                    	boolean b = cell.getBooleanCellValue();
	                    	newarray.add(String.valueOf(b));
	                    	break;
	                }  
	               }count++;
	         //      System.out.println(count);
	               farrayList.add(newarray);
	            if(farrayList.size() > 10000){
	            	 data.addAll(farrayList);
	            	 farrayList = new ArrayList<ArrayList<String>>();
	            }    
	      }
	       data.addAll(farrayList);
	       inputStream.close();     
	       return data;  
	    }
	    
	    
	    
	    public static void init(){
	    	
	    	String excelFilePath_user = "/Users/prateek/Desktop/DataInserted/Users/User_complete_data.xls";
	    	String excelFilePath_tweet1 = "/Users/prateek/Desktop/DataInserted/tweetData/tweet_data_ups.xls";
	    	String excelFilePath_mentiondUser = "/Users/prateek/Desktop/DataInserted/mentionedData/Mentioned_data.xls";
	    	String excelFilePath_hashTag = "/Users/prateek/Desktop/DataInserted/taggeddata/Tagged_data.xls";
	    	String excelFilePath_urlUsed = "/Users/prateek/Desktop/DataInserted/urlData/URLdata.xls";
	    	
	    
	    	
	    	try{
	    	// User Data
	    	userdata = ReadExcel.readxls(excelFilePath_user);
	    	tweetdata1 = ReadExcel.readxls(excelFilePath_tweet1);
	    	mentioned_user = ReadExcel.readxls(excelFilePath_mentiondUser);
			hashtag = ReadExcel.readxls(excelFilePath_hashTag);
			url_used = ReadExcel.readxls(excelFilePath_urlUsed);
			
	//		System.out.println(userdata);
	    	
	    	System.out.println(userdata.size());
	    	System.out.println(tweetdata1.size());
	    	System.out.println(mentioned_user.size());
	    	System.out.println(hashtag.size());
	    	System.out.println(url_used.size());
	    	
	
	  
	    	}
	    	catch(Exception e){
	    		System.out.println(e);
	    		System.out.println(e.getStackTrace());
	    	}
	    	
	    	
	    }
	    
	}