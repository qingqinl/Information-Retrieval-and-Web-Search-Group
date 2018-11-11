package com.group.assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.nio.file.*;
import com.qingqinli.websearch.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.similarities.PerFieldSimilarityWrapper;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
 
public class IndexFiles1 {

	// Directory where the search index will be saved
	private static String INDEX_DIRECTORY = "C:\\Users\\winnie\\Desktop\\index";

	public static void main(String[] args) throws IOException {
		//File files = new File("C:\\Users\\winnie\\Desktop\\data");
		File files = new File("C:\\Users\\winnie\\Documents\\information retrieval and web search\\Assignment Two\\Assignment Two\\data\\la");
		File[] fs = files.listFiles();
		String[] fileNames = new String[fs.length];
		for(int i = 0; i < fs.length; i++) {
			fileNames[i] = fs[i].getPath();
			System.out.println(fileNames[i]);
		}
		args = fileNames;
		// Make sure we were given something to index
		if (args.length <= 0) {
         //   System.out.println("Expected corpus as input");
            System.exit(1);            
        }

		Analyzer analyzer = new StandardAnalyzer();
		
		// ArrayList of documents in the corpus
		ArrayList<Document> documents = new ArrayList<Document>();

		
		// Open the directory that contains the search index
		Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
		//BM25Similarity bm25Similarity = new BM25Similarity();
		//IndexWriterConfig config = new IndexWriterConfig(new SynonymAnalyzer(new SimpleSynonymEngine()));
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		//config.setSimilarity(bm25Similarity);
		
		IndexWriter iwriter = new IndexWriter(directory, config);  
		
		for (String arg : args) {
			// Load the contents of the file
			System.out.printf("Indexing \"%s\"\n", arg);
	        // The name of the file to open.
	        String fileName = arg;
	        // This will reference one line at a time
	        String line = null,flag=null;
	        String DOCNO=null,HEADER=null,TEXT=null,PARENT=null,BYLINE=null,GRAPHIC=null;
	        try {
	            // FileReader reads text files in the default encoding.
	            FileReader fileReader = new FileReader(fileName);
	            // Always wrap FileReader in BufferedReader.
	            BufferedReader bufferedReader =  new BufferedReader(fileReader);
	             StringBuffer tempcontent = new StringBuffer("");         
	            while((line = bufferedReader.readLine()) != null) {
	            	if(line.isEmpty()||line.contains("<P>")||line.contains("</P>"))
	            		{continue;}
	            	if(line.contains("<DOC>"))
	            		flag="doc";
	            	if(line.contains("</DOC>"))
	            	    flag="enddoc";
	            	if(line.contains("<DOCNO>"))
	            	    flag="docno";
	            	if(line.contains("<HT>"))
	            	    flag="ht";
	            	if(line.contains("<PARENT>"))
	            		flag="parent";
	            	if(line.contains("<HEADER>")||line.contains("<HEADLINE>"))
	            	    flag="header";
	            	if(line.contains("</HEADER>")||line.contains("</HEADLINE>"))
	            	    flag="endheader";
	            	if(line.contains("<BYLINE>"))
	            		flag="byline";
	            	if(line.contains("</BYLINE>"))
	            		flag="endbyline";
	            	if(line.contains("<TEXT>"))
	            	    flag="text";
	            	if(line.contains("</TEXT>"))
	            	    flag="endtext";
	            	if(line.contains("<GRAPHIC>"))
	            		flag="graphic";
	            	if(line.contains("</GRAPHIC>"))
	            		flag="endgraphic";
	            	if(flag=="docno")
                      {
	            		if(line.contains("FR")||line.contains("LA"))
	            		{ 
	            			String[] temp;
                            String delimeter = " ";  
                            temp = line.split(delimeter);
                            DOCNO=temp[1];
                          }
	            		else
	            		{
	            			String[] temp;
	                        String delimeter1 = ">",delimeter2="<";  
	                        temp = line.split(delimeter1);
	                        DOCNO=temp[1].split(delimeter2)[0];
	            		}
	            		flag="enddocno";
                      }
	            	//if(flag=="ht")
	            	//{
	            		//String[] temp;
                       // String delimeter = "\"";  
                       // temp = line.split(delimeter);
                        //System.out.println(line);
                        //HT=temp[1];
	            		//}
	            	if(flag=="parent")
                    {
                  	    String[] temp;
                        String delimeter = " ";  
                        temp = line.split(delimeter);
                        PARENT=temp[1];
                        }
                    if(flag=="header")
                    {
                    	if(line.contains("<HEADER>")||line.contains("<HEADLINE>"))
                    		continue;  
                    	else
                    		tempcontent.append(line+" ");
                    }
                    if(flag=="endheader")
                    {
                    	HEADER=tempcontent.toString();
                    	//System.out.println(DOCNO);
                    	StringBuffer empty = new StringBuffer("");
                        tempcontent=empty;
                        flag="nothing";
                        }
                    if(flag=="byline")
                    {
                    	if(line.contains("<BYLINE>"))
                    		continue;  
                    	else
                    		tempcontent.append(line+" ");
                    }
                    if(flag=="endbyline")
                    {
                    	BYLINE=tempcontent.toString();
                    	//System.out.println(DOCNO);
                    	StringBuffer empty = new StringBuffer("");
                        tempcontent=empty;
                        flag="nothing";
                        }
                    if(flag=="text")
                    {
                    	if(line.contains("<TEXT>"))
                    		continue;  
                    	else
                    		tempcontent.append(line+" ");
                    	}                    
                    if(flag=="endtext")
                    {
                    	TEXT=tempcontent.toString();
                    	//System.out.println(TEXT);
                    	StringBuffer empty = new StringBuffer("");
                        tempcontent=empty;
                        flag="nothing";
                        }
                    if(flag=="graphic")
                    {
                    	if(line.contains("<GRAPHIC>"))
                    		continue;  
                    	else
                    		tempcontent.append(line+" ");
                    	}                    
                    if(flag=="endgraphic")
                    {
                    	GRAPHIC=tempcontent.toString();
                    	//System.out.println();
                    	StringBuffer empty = new StringBuffer("");
                        tempcontent=empty;
                        flag="nothing";
                        }
                    if(flag=="enddoc")
                    {
                    	//if(HT==null)
                	     //  HT="unkown";
                    	if(PARENT==null)
                 	       PARENT="unkown";
                    	if(HEADER==null)
                  	       HEADER="unkown";
                    	if(BYLINE==null)
                 	       BYLINE="unkown";
                    	if(GRAPHIC==null)
                 	       GRAPHIC="unkown";
                    	if(TEXT==null)
                  	       TEXT="unkown";
                    	Document doc = new Document();
                    	System.out.println("Adding document:"+DOCNO);
                    	doc.add(new StringField("DOCNO", DOCNO, Field.Store.YES)); 
            			doc.add(new TextField("PARENT", PARENT, Field.Store.YES));
            			doc.add(new TextField("HEADER", HEADER, Field.Store.YES));
            			doc.add(new TextField("BYLINE", BYLINE, Field.Store.YES));
            			doc.add(new TextField("TEXT", TEXT, Field.Store.YES));
            			doc.add(new TextField("GRAPHIC", GRAPHIC, Field.Store.YES));
            			documents.add(doc);
            			DOCNO=null;
            			PARENT=null;
            			HEADER=null;
            			BYLINE=null;
            			TEXT=null;
            			GRAPHIC=null;
                    }
                    }  	
	            // Always close files.
	            bufferedReader.close();         
	            }
	        catch(FileNotFoundException ex) {
	            System.out.println(
	                "Unable to open file '" + 
	                fileName + "'");                
	        }
	        catch(IOException ex) {
	            System.out.println(
	                "Error reading file '" 
	                + fileName + "'");                  
	            // Or we could just do this: 
	            // ex.printStackTrace();
	        }	
		}

		// Write all the documents in the linked list to the search index
		iwriter.addDocuments(documents);

		// Commit everything and close
		iwriter.close();
		directory.close();
	}
}