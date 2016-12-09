import com.thinkaurelius.titan.core.TitanGraph;

public class CreateDataModel {
	
	
	private static String dicPath = "/Users/prateek/Downloads/titan-1.0.0-hadoop1/";
	
	public static void main(String args[])
    {
		
		/*
		 * 
		 *  Run DataModel One by One in your system.
		 */
	/*	
		System.out.println("---Database configuring for Basic Model----database : db1");
		TitanGraph g1 = CreateGraphModelBasic.configure(dicPath+"ddcheck");
		System.out.println("---- Start Database 1----");
		CreateGraphModelBasic.load(g1);
		System.out.println("---Data end of Loading---");
        g1.close();
		
	*/	
    /*  
		System.out.println("---Database configuring for Indexing----database : db2");
		TitanGraph g2 = CreateGraphMode1.configure(dicPath+"ddcheck2");
		System.out.println("---- Start Database 2----");
		CreateGraphMode1.load(g2);
		System.out.println("---Data end of Loading---");
        g2.close();
        
        */
       
     
        System.out.println("---Database configuring for Indexing and hoping----database : db3");
        TitanGraph g3 = CreateGraphModel2.configure(dicPath+"ddcheck3");
        System.out.println("---- Start Database 3----");
    	CreateGraphModel2.load(g3);
    	System.out.println("---Data end of Loading---");
        g3.close();
      
    } 

}
