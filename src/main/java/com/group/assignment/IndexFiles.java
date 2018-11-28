package com.group.assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexFiles {

    // Directory where the search index will be saved
    //private static String INDEX_DIRECTORY = "C:\\Users\\winnie\\Desktop\\index";

    public static void createIndex(String[] args) throws IOException {
        //File files = new File("C:\\Users\\winnie\\Desktop\\data");
        //File files = new File(LuceneConstants.INDEX_PATH);
        List<File> fs = Files.walk(Paths.get(LuceneConstants.DOCUMENT_PATH))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .filter(file -> {
                    String name = file.getName();
                    return !name.contains("read") && !name.contains("READ") && !name.contains("Read") &&
                            !name.equals("fbisdtd.dtd") && !name.equals("fr94dtd") && !name.equals("ftdtd") && !name.equals("latimesdtd.dtd");
                })
                .collect(Collectors.toList());
        //File[] fs = files.listFiles();
        String[] fileNames = new String[fs.size()];
        for (int i = 0; i < fs.size(); i++) {
            fileNames[i] = fs.get(i).getPath();
            System.out.println(fileNames[i]);
        }
        args = fileNames;
        // Make sure we were given something to index
        if (args.length <= 0) {
            //   System.out.println("Expected corpus as input");
            System.exit(1);
        }

        Analyzer analyzer = new EnglishAnalyzer();

        // ArrayList of documents in the corpus
        ArrayList<Document> documents = new ArrayList<Document>();


        // Open the directory that contains the search index
        Directory directory = FSDirectory.open(Paths.get(LuceneConstants.INDEX_PATH));
        BM25Similarity bm25Similarity = new BM25Similarity(0.8f,0.8f);
        //IndexWriterConfig config = new IndexWriterConfig(new SynonymAnalyzer(new SimpleSynonymEngine()));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        config.setSimilarity(bm25Similarity);
        //config.setSimilarity(bm25Similarity);

        IndexWriter iwriter = new IndexWriter(directory, config);

        for (String arg : args) {
            // Load the contents of the file
            System.out.printf("Indexing \"%s\"\n", arg);
            // The name of the file to open.
            String fileName = arg;
            // This will reference one line at a time
            String line = null, flag = null;
            String DOCNO = null, HEADER = null, TEXT = null, PARENT = null, BYLINE = null, GRAPHIC = null;
            try {
                // FileReader reads text files in the default encoding.
                FileReader fileReader = new FileReader(fileName);
                // Always wrap FileReader in BufferedReader.
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuffer tempcontent = new StringBuffer("");
                StringBuilder allLines = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
        //            if (line.isEmpty() || line.contains("<P>") || line.contains("</P>")) {
          //              continue;
            //        }
                  //  line = line.;
                    allLines.append(line).append(" ");
                    if(line.isEmpty())
                        continue;
              //      allLines.append(line).append(" ");
//                    if (line.isEmpty() || line.contains("<!--") || line.contains("-->") || line.contains("<P>") || line.contains("</P>")) {
//                        continue;
//                    }
                    if(line.contains("<TI>")) {
                        line +=  line +  line;
                        allLines.append(line).append(line);
                    }
                    if (line.contains("<DOC>"))
                        flag = "doc";
                    if (line.contains("</DOC>"))
                        flag = "enddoc";
                    if (line.contains("<DOCNO>"))
                        flag = "docno";
                    if (line.contains("<HT>"))
                        //flag = "ht";
                    if (line.contains("<PARENT>"))
                        flag = "parent";
                    if (line.contains("<HEADER>") || line.contains("<HEADLINE>"))
                        flag = "header";
                    if (line.contains("</HEADER>") || line.contains("</HEADLINE>"))
                        flag = "endheader";
                    if (line.contains("<BYLINE>"))
                        flag = "byline";
                    if (line.contains("</BYLINE>"))
                        flag = "endbyline";
                    if (line.contains("<TEXT>"))
                        flag = "text";
                    if (line.contains("</TEXT>"))
                        flag = "endtext";
                    if (line.contains("<GRAPHIC>"))
                        flag = "graphic";
                    if (line.contains("</GRAPHIC>"))
                        flag = "endgraphic";
                    if (flag == "docno") {
                        if (line.contains("FR") || line.contains("LA")) {
                            String[] temp;
                            String delimeter = " ";
                            temp = line.split(delimeter);
                            DOCNO = temp[1];
                        } else {
                            String[] temp;
                            String delimeter1 = ">", delimeter2 = "<";
                            temp = line.split(delimeter1);
                            DOCNO = temp[1].split(delimeter2)[0];
                        }
                        flag = "enddocno";
                    }
                    //  line = line.replaceAll("<.*>","");
                    //if(flag=="ht")
                    //{
                    //String[] temp;
                    // String delimeter = "\"";
                    // temp = line.split(delimeter);
                    //System.out.println(line);
                    //HT=temp[1];
                    //}
                    if (flag == "parent") {
                        String[] temp;
                        String delimeter = " ";
                        temp = line.split(delimeter);
                        PARENT = temp[1];
                    }
            //        line = line.replaceAll("<.*>", "");
                    if (flag == "header") {
                        if (line.contains("<HEADER>") || line.contains("<HEADLINE>"))
                            continue;
                        else
                            tempcontent.append(line + " ");
                    }
                    if (flag == "endheader") {
                        HEADER = tempcontent.toString();
                        //System.out.println(DOCNO);
                        StringBuffer empty = new StringBuffer("");
                        tempcontent = empty;
                        flag = "nothing";
                    }
                    if (flag == "byline") {
                        if (line.contains("<BYLINE>"))
                            continue;
                        else
                            tempcontent.append(line + " ");
                    }
                    if (flag == "endbyline") {
                        BYLINE = tempcontent.toString();
                        //System.out.println(DOCNO);
                        StringBuffer empty = new StringBuffer("");
                        tempcontent = empty;
                        flag = "nothing";
                    }
                    if (flag == "text") {
                        if (line.contains("<TEXT>"))
                            continue;
                        else
                            tempcontent.append(line + " ");
                    }
                    if (flag == "endtext") {
                        TEXT = tempcontent.toString();
                        //System.out.println(TEXT);
                        StringBuffer empty = new StringBuffer("");
                        tempcontent = empty;
                        flag = "nothing";
                    }
                    if (flag == "graphic") {
                        if (line.contains("<GRAPHIC>"))
                            continue;
                        else
                            tempcontent.append(line + " ");
                    }
                    if (flag == "endgraphic") {
                        GRAPHIC = tempcontent.toString();
                        //System.out.println();
                        StringBuffer empty = new StringBuffer("");
                        tempcontent = empty;
                        flag = "nothing";
                    }
                    if (flag == "enddoc") {
                        //if(HT==null)
                        //  HT="unkown";
                        if (PARENT == null)
                            PARENT = "";
                        if (HEADER == null)
                            HEADER = "";
                        if (BYLINE == null)
                            BYLINE = "";
                        if (GRAPHIC == null)
                            GRAPHIC = "";
                        if (TEXT == null)
                            TEXT = "";
                        Document doc = new Document();
                        //System.out.println("Adding document:"+DOCNO);
                        doc.add(new StringField("DOCNO", DOCNO, Field.Store.YES));
//            			doc.add(new TextField("PARENT", PARENT, Field.Store.YES));
            			doc.add(new TextField("HEADER", HEADER, Field.Store.YES));
//            			doc.add(new TextField("BYLINE", BYLINE, Field.Store.YES));
//            			doc.add(new TextField("TEXT", TEXT, Field.Store.YES));
//            			doc.add(new TextField("GRAPHIC", GRAPHIC, Field.Store.YES));
                        PARENT = PARENT.replaceAll("[<>\\-]"," ");
                        HEADER = HEADER.replaceAll("[<>\\-]"," ");
                        BYLINE = BYLINE.replaceAll("[<>\\-]"," ");
                        TEXT = TEXT.replaceAll("[<>\\-]"," ");
                        GRAPHIC = GRAPHIC.replaceAll("[<>\\-]"," ");
                   //     doc.add(new TextField("All", PARENT + HEADER + BYLINE + TEXT + GRAPHIC, Field.Store.YES));
                        doc.add(new TextField("All",replace(allLines.toString().replaceAll("[\\pP\\p{Punct}]","")), Field.Store.YES));
                        allLines = new StringBuilder();
                        documents.add(doc);
                        DOCNO = null;
                        PARENT = null;
                        HEADER = null;
                        BYLINE = null;
                        TEXT = null;
                        GRAPHIC = null;
                        iwriter.addDocuments(documents);
                        documents = new ArrayList<Document>();
                    }
                }
                // Always close files.
                bufferedReader.close();
            } catch (FileNotFoundException ex) {
                System.out.println(
                        "Unable to open file '" +
                                fileName + "'");
            } catch (IOException ex) {
                System.out.println(
                        "Error reading file '"
                                + fileName + "'");
                // Or we could just do this:
                // ex.printStackTrace();
            }
        }

        // Write all the documents in the linked list to the search index
        //iwriter.addDocuments(documents);

        // Commit everything and close
        iwriter.close();
        directory.close();
    }

    private static String replace(String ori){
        String[] nouse = {"document","relevant","discuss",
                "describing","information"};
        for(String r : nouse){

            ori = ori.replaceAll(r,"");
        }
        return ori;
    }
}