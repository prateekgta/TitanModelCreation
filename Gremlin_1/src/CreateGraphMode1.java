import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.thinkaurelius.titan.core.*;
import com.thinkaurelius.titan.core.schema.TitanManagement;

public class CreateGraphMode1
{
	public static final String INDEX_NAME = "search";
	
	public static TitanGraph configure(final String directory)
	{
        TitanFactory.Builder config = TitanFactory.build();
   
        config.set("storage.backend", "berkeleyje");
        config.set("storage.directory", directory);
        config.set("index."+INDEX_NAME+".backend","elasticsearch");
        config.set("index." + INDEX_NAME + ".directory", directory + File.separator + "es");
        config.set("index."+INDEX_NAME+".elasticsearch.local-mode",true);
        config.set("index."+INDEX_NAME+".elasticsearch.client-only",false);
        TitanGraph graph = config.open();
     
        return graph;
    }
	
	public static void load(final TitanGraph graph)
	{
		
		//Create Schema

		
		graph.tx().rollback() ;
		TitanManagement mgmt = graph.openManagement();
		
		final PropertyKey screen_name = mgmt.makePropertyKey("screen_name").dataType(String.class).make();
	    final PropertyKey tid = mgmt.makePropertyKey("sub_category").dataType(String.class).make();
	    
	    mgmt.buildIndex("snameandsub_category", TitanVertex.class).addKey(screen_name).addKey(tid).buildCompositeIndex();
	        
     
		ReadExcel.init();
        
        //Define Vertex for User
        mgmt.makeVertexLabel("User").make();
        
        //Define Vertex for Tweet
        mgmt.makeVertexLabel("Tweet").make();
        
        //Define Vertex for mentioned_user
        mgmt.makeVertexLabel("Mentioned_User").make();
        
        // Define Vertex for HashTag
        mgmt.makeVertexLabel("HashTag").make();
        
        //Define Vertex for Url_Used
        mgmt.makeVertexLabel("Url_Used").make();
        
        /*---------------------------------------*/
        
        //Define Edge
        mgmt.makeEdgeLabel("posted").make();   
        
      //Define Edge
        mgmt.makeEdgeLabel("mentioned").make();   
      
      //Define Edge
        mgmt.makeEdgeLabel("tagged").make();   
        
      //Define Edge
        mgmt.makeEdgeLabel("url").make();   
        
        
        
        mgmt.commit();
        
        TitanTransaction tx = graph.newTransaction();
         
        ArrayList<ArrayList<String>> userdata = ReadExcel.userdata;
		ArrayList<ArrayList<String>> tweetdata1 = ReadExcel.tweetdata1;
		ArrayList<ArrayList<String>> mentioneddata = ReadExcel.mentioned_user;
		ArrayList<ArrayList<String>> url_used_data = ReadExcel.url_used;
		ArrayList<ArrayList<String>> tagged_data = ReadExcel.hashtag;
		
	
		// Collection of Vertex 
		
		ArrayList<TitanVertex> vertexUserList = new ArrayList<TitanVertex>();
		HashMap<String,ArrayList<TitanVertex>> posting_vertexEdgeList = new HashMap<String,ArrayList<TitanVertex>>();
		HashMap<String,ArrayList<TitanVertex>> tweetid_vertexEdgeList = new HashMap<String,ArrayList<TitanVertex>>();
	
		HashMap<String,ArrayList<TitanVertex>> tweetid_hashtag = new HashMap<String,ArrayList<TitanVertex>>();
		HashMap<String,ArrayList<TitanVertex>> tweetid_mentioned_user = new HashMap<String,ArrayList<TitanVertex>>();
	
		HashMap<String,ArrayList<TitanVertex>> tweetid_url = new HashMap<String,ArrayList<TitanVertex>>();
		HashMap<String,ArrayList<TitanVertex>> screen_name_user = new HashMap<String,ArrayList<TitanVertex>>();
	
		ArrayList<TitanVertex> vertexMentionedList = new ArrayList<TitanVertex>();
		ArrayList<TitanVertex> vertexUrlList = new ArrayList<TitanVertex>();
		ArrayList<TitanVertex> vertexTaggedList = new ArrayList<TitanVertex>();
		
		// Create User Vertex
		for(ArrayList<String> user : userdata){
			
			
			TitanVertex v1 = tx.addVertex("User");
			
			   v1.property("following",user.get(0)); 
			   v1.property("followers", user.get(1));
			   v1.property("category", user.get(2));
			   v1.property("location", user.get(3));
			   v1.property("name", user.get(4));
			   v1.property("screen_name", user.get(5));
			   v1.property("sub_category", user.get(6));			 
			   vertexUserList.add(v1);
			
			   String key =  user.get(5);
			   
			   // HashMap for screen_name 
				 if(screen_name_user.containsKey(key)){
					 ArrayList<TitanVertex> al= screen_name_user.get(key);
					 al.add(v1);
				 }
				 else{
					 ArrayList<TitanVertex> al = new ArrayList<TitanVertex>();
		    		 al.add(v1);
		    		 screen_name_user.put(key,al);
				 }
			 
		}
		 System.out.println("User Created");
		
		
		
		// Create Tweet Vertex
		
		for(ArrayList<String> tweet : tweetdata1){
			
			TitanVertex v2 = tx.addVertex("Tweet");
			
			v2.property("text",tweet.get(1));
			v2.property("retweet_count",tweet.get(2));
			v2.property("retweeted",tweet.get(3));
			v2.property("year",tweet.get(4));
			v2.property("month",tweet.get(5));
			v2.property("day",tweet.get(6));
			
			String key1 =  tweet.get(7);
			String key2 =  tweet.get(0);
			
		
			 
			 // HashMap for tid in Edges
			 if(tweetid_vertexEdgeList.containsKey(key2)){
				 ArrayList<TitanVertex> al= tweetid_vertexEdgeList.get(key2);
				 al.add(v2);
			 }
			 else{
				 ArrayList<TitanVertex> al = new ArrayList<TitanVertex>();
	    		 al.add(v2);
	    		 tweetid_vertexEdgeList.put(key2,al);
			 }
			
			 // HashMap for screen_name in Edges
			if(posting_vertexEdgeList.containsKey(key1)){
				ArrayList<TitanVertex> al= posting_vertexEdgeList.get(key1);
    			al.add(v2);
    		}
    		else{
    			ArrayList<TitanVertex> al = new ArrayList<TitanVertex>();
    			al.add(v2);
    			posting_vertexEdgeList.put(key1, al);
    		}	
		
		}
		
		System.out.println("Tweet Created");
		
		
		// Create Mentioned_User vertex

		for(ArrayList<String> mdata : mentioneddata){
			TitanVertex v1 = tx.addVertex("Mentioned_User");
			   v1.property("screen_name",mdata.get(1));
			   vertexMentionedList.add(v1);
			   
			   String key= mdata.get(0);
			   
			   if(tweetid_mentioned_user.containsKey(key)){
					 ArrayList<TitanVertex> al= tweetid_mentioned_user.get(key);
					 al.add(v1);
				 }
				 else{
					 ArrayList<TitanVertex> al = new ArrayList<TitanVertex>();
		    		 al.add(v1);
		    		 tweetid_mentioned_user.put(key,al);
				 }
			   
			 
		}
		
		  System.out.println("Mentioned_User Created");
		
		
		// Create Url vertex
		
			for(ArrayList<String> urlData : url_used_data){
				
				  TitanVertex v1 = tx.addVertex("Url_Used");
				   v1.property("tweeturl",urlData.get(1));
				   vertexUrlList.add(v1);	
				   
				   String key = urlData.get(0);
				   
				   if(tweetid_url.containsKey(key)){
						 ArrayList<TitanVertex> al= tweetid_url.get(key);
						 al.add(v1);
					 }
					 else{
						 ArrayList<TitanVertex> al = new ArrayList<TitanVertex>();
			    		 al.add(v1);
			    		 tweetid_url.put(key,al);
					 }
				   
				  
			}
		
			 System.out.println("Url Created");
			
		// Create Tagged vertex
		
		for(ArrayList<String> tagged : tagged_data){
			TitanVertex v1 = tx.addVertex("HashTag");
			   v1.property("hashtagname",tagged.get(1));
			   vertexTaggedList.add(v1); 
			   
			   String key = tagged.get(0);
			   
			   if(tweetid_hashtag.containsKey(key)){
					 ArrayList<TitanVertex> al= tweetid_hashtag.get(key);
					 al.add(v1);
				 }
				 else{
					 ArrayList<TitanVertex> al = new ArrayList<TitanVertex>();
		    		 al.add(v1);
		    		 tweetid_hashtag.put(key,al);
				 }
			   
			   
		}
		

		   System.out.println("Tagged Created");
		
		
	try{
		
		// Add Edge between User and Tweet-- ref by screen_name
	 	Set<String> keyset = screen_name_user.keySet();
	   
	 	for (String key : keyset){
	    	
			 ArrayList<TitanVertex> al1 = screen_name_user.get(key);
	    
	        if(posting_vertexEdgeList.containsKey(key)){
	        	
	        	ArrayList<TitanVertex> al2 = posting_vertexEdgeList.get(key);
	        	
	        	for(TitanVertex v1 : al1){
	        	
	     //   		System.out.println("vertex 1"+v1);
	        	
	        		for(TitanVertex v2 : al2){
	        		
	    //    			System.out.print(v2);
	        			v1.addEdge("posted", v2);
	        			System.out.println("edge created for posted");
	        		}
	        	
	        }
	      }
	 }
		    
		    
		    
	 	 // Add Edge between Tweet and Mentioned User -- ref by tid
	    
		  keyset  = tweetid_vertexEdgeList.keySet();
		    
		   for (String key : keyset){
		       
				ArrayList<TitanVertex> al1 = tweetid_vertexEdgeList.get(key);
		      
		        if(tweetid_mentioned_user.containsKey(key)){
		        	
		        	// List of User by screen Name
		        	ArrayList<TitanVertex> al2 = tweetid_mentioned_user.get(key);
		        	
		        	for(TitanVertex v1 : al1){
		    //    		System.out.println("vertex 1"+v1);
		        		for(TitanVertex v2 : al2){
		        	
		        //			System.out.print(v2);
		        			v1.addEdge("mentioned", v2);
		        			
		    				System.out.println("edge created for Mentioned");
		        		}
		        	}
		        }    
		    }
		
		
		   
		    
		   // Add Edge between tweet and hashtag
		    
		   keyset   = tweetid_vertexEdgeList.keySet();
		    
		    for (String key : keyset){
		    	
				ArrayList<TitanVertex> al1 = tweetid_vertexEdgeList.get(key);
		        
				if(tweetid_hashtag.containsKey(key)){
		        	
		        	// List of User by screen Name
		        	ArrayList<TitanVertex> al2 = tweetid_hashtag.get(key);
		        	
		        	for(TitanVertex v1 : al1){
		        		for(TitanVertex v2 : al2){
		        			v1.addEdge("tagged", v2);
		    				System.out.println("edge created for tagged");
		        		}
		        	}
		        }
		    }
		
		
		
		// Add Edge between tweet and Url_Used
		
		    keyset   = tweetid_vertexEdgeList.keySet();
		    
		    for (String key : keyset){
		    	
		      
				ArrayList<TitanVertex> al1 = tweetid_vertexEdgeList.get(key);
		        if(tweetid_url.containsKey(key)){
		        	
		        	// List of url 
		        	ArrayList<TitanVertex> al2 = tweetid_url.get(key);
		        	
		        	for(TitanVertex v1 : al1){
		        		for(TitanVertex v2 : al2){
		        			v1.addEdge("url", v2);
		   				System.out.println("edge created for url");
		        		}
		        	}
		        }
		        
		      
		    }
		    
		    System.out.println("edge created for url");
		    
		  System.out.println("wait graph to commit to database");
		
	}
	catch(Exception e){
		System.out.println(e);
		System.out.println(e.getStackTrace());
	}
	
	 	tx.commit();
	}
	
	
	
	
}
