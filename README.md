# databaseTitanModel
This code will create 3 Titan Data Model for you, 1 data model is basic which use no indexing, 2 data model 
use indexing at screen_name and sub_category [CompositeIndex] and 3 data model is with reduce hops.

1)  Import gremlin_1 in Eclipse or any IDE. 

2) Add external jars, click on configure build path and add external jars  
jars : a. ooxml-schemas-1.0.jar
       b. poi-3.9.jar
       c. poi-ooxml-3.9.jar
       d. xmlbeans-2.6.0.jar
       e. all jars provided by Titan-1, go to lib folder to add all jars.

3 ) Set path of directory (export files from Neo4J :-XLS files) according to you in ReadExcel and ReadExcel1. 
    At CreateDataModel, give path of Titan Database. Note all data properties are set for Tweet,User,Mentioned_User,HashTag and Url.
    For Help, check our XLS files in example.
    Check the directory whether all the files are created properly or not.

4) Now open gremlin and Run this commands or you can change the file  "titan-berkeleyje-es.properties" in conf if you want to use
  graph = TitanFactory.open(path/titan/conf/titan-berkeleyje-es.properties)

OR
  conf = new BaseConfiguration()
  conf.setProperty('storage.directory','/Users/prateek/Downloads/titan-1.0.0-hadoop1/db1')
  conf.setProperty('storage.batch-loading','true')
  conf.setProperty('storage.backend','berkeleyje')
  graph = TitanFactory.open(conf)
  now triversal :
  g = graph.traversal(standard())
  g.E() .

  Now check the documentation to do all queries : http://tinkerpop.apache.org/docs/3.0.1-incubating/#traversal
  
Note : Run datamodel one by one in CreateDataModel, as data use HeapMemory, might cause an exception.
        All edges from tweet are mapped with Mentioned_User,HashTag,Url and user and tweet are connected by screen_name.
        Used TitanVertex documentation API's for development, any changes might cause error, in reference you can check the API's 
        Titan documentation for help.
        @Author : prateekg@iastate.edu
