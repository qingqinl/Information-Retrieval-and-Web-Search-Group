package com.group.assignment;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.surround.query.SrndPrefixQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QueryIndex {
    // Limit the number of search results we get
    private static int MAX_RESULTS = 50;

    public static void query() throws IOException, ParseException {
        // Open the folder that contains our search index
        Directory directory = FSDirectory.open(Paths.get(LuceneConstants.INDEX_PATH));
        List<String> lines = Files.readAllLines(Paths.get(LuceneConstants.SEARCH_DIRECTORY));
        List<MyQuery> queries = getAllQueries(lines);
        //queries.forEach(System.out::println);

        // create objects to read and search across the index
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);

        // builder class for creating our query

        List<String> results = new ArrayList<>();
        Analyzer analyzer = new StandardAnalyzer();

        Similarity similarity;

        similarity = new BM25Similarity();

        isearcher.setSimilarity(similarity);
        QueryParser parser = new QueryParser("All", analyzer);
        for(MyQuery myQuery : queries){
            // BooleanQuery.Builder query = new BooleanQuery.Builder();
            //Query term = new TermQuery(new Term("W", myQuery.getW()));
            //query.add(new BooleanClause(term, BooleanClause.Occur.SHOULD));
            Query query = parser.parse(QueryParser.escape(myQuery.getDescription()+myQuery.getNarriative()+myQuery.getDescription()));
            ScoreDoc[] hits = isearcher.search(query,MAX_RESULTS).scoreDocs;

            //   System.out.println(myQuery.getI());
            for (int i = 0; i < hits.length; i++) {
                Document hitDoc = isearcher.doc(hits[i].doc);
                int order = i + 1;
                // System.out.println(myQuery.getI() + " Q0 " + hitDoc.get("I")  +" " +order+" "+ hits[i].score + " STANDARD");
                results.add(myQuery.getId() + " Q0 " + hitDoc.get("I")  +" " +order+" "+ hits[i].score + " STANDARD");

                //  System.out.println(myQuery.getI() + " Q0 " + hitDoc.get("I")  +" " +order+" "+ hits[i].score + " STANDARD");

            }

        }
        wirteToFile("myrel", results);


        ireader.close();
        directory.close();

        }




    public static List<MyQuery> getAllQueries(List<String> lines) {
        //lines.forEach(System.out::println);
        List<MyQuery> queries = new ArrayList<>();
        StringBuilder title = new StringBuilder();
        StringBuilder desc = new StringBuilder();
        StringBuilder narr = new StringBuilder();
        StringBuilder current = new StringBuilder();

        for(String line : lines){
            line = line.trim();
            if(line.contains("<num>"))
                continue;
            if(line.trim().isEmpty())
                continue;

            if(line.contains("<top>")){
                title = new StringBuilder();
                desc = new StringBuilder();
                narr = new StringBuilder();
            }else if(line.contains("<title>")){
                title.append(line.replace("<title>", "").trim()).append("\n");

            }else if(line.contains("<desc>")){
                current = desc;
            }else if(line.contains("<narr>")){
                current = narr;
            } else if(line.contains("</top>")){
                MyQuery myQuery = new MyQuery();
                myQuery.setDescription(desc.toString());
                myQuery.setNarriative(narr.toString());
                myQuery.setTitle(title.toString());
                queries.add(myQuery);
            }
            else{
                current.append(line).append("\n");
            }
        }

        return queries;
    }

    public static void testGetAllQueries(){
        try {
            List<String> lines = Files.readAllLines(Paths.get("/home/jinchi/websearch/groupwork/query-doc"));
            List<MyQuery> queries = getAllQueries(lines);
            for(MyQuery q : queries){
                System.out.println(q.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void wirteToFile(String myrel, List<String> lines) throws IOException {
        //lines.stream().map()
        String file = LuceneConstants.HOME_PATH+"/"+"myrel";
        Files.deleteIfExists(Paths.get(file));
        //System.out.println(lines.size());
        lines.forEach((line) ->{
            try {
                //System.out.println(myrel);
                Files.write(Paths.get(file), (line+System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                // System.out.println(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println("the result file is " + file);


    }

  /*  public static void main(String[] args) {
        QueryIndex queryIndex = new QueryIndex();
        try {
            queryIndex.query("", 1, 1);
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        } catch (ParseException e) {
            System.out.println("ParseException: " + e);
        }
    }*/
}
