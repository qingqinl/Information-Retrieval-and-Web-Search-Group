package com.group.assignment;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;

public class QueryIndex {
    // Limit the number of search results we get
    private static int MAX_RESULTS = 50;

    public void query(String qryContent, int queryId, int resultNum) throws IOException, ParseException {
        // Analyzer used by the query parser.
        // Must be the same as the one used when creating the index
        Analyzer analyzer = new StandardAnalyzer();

        // Open the folder that contains our search index
        Directory directory = FSDirectory.open(Paths.get(LuceneConstants.INDEX_PATH));

        // create objects to read and search across the index
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);

        // Create the query parser. The default search field is "content", but
        // we can use this to search across any field
        QueryParser parser = new QueryParser("contents", analyzer);

        String queryString = qryContent.replace(")", "").replace("(", "").replace("?", "");
        //Scanner scanner = new Scanner(System.in);
        ScoreDoc[] hits = null;
        //do {
        // trim leading and trailing whitespace from the query
        queryString = queryString.trim();

        // Write file
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(LuceneConstants.HOME_PATH + "result/result.txt"), true));
        float biggestNum = 0;
        float smallestNum = 100;


        // if the user entered a querystring
        if (queryString.length() > 0) {
            // parse the query with the parser
            Query query = parser.parse(queryString);

            // Get the set of results
            hits = isearcher.search(query, MAX_RESULTS).scoreDocs;

            // Print the results
            System.out.println("Documents: " + hits.length);
            for (int i =0; i < hits.length; i++) {
                if (hits[i].score > biggestNum) {
                    biggestNum = hits[i].score;
                }
                if (hits[i].score < smallestNum) {
                    smallestNum = hits[i].score;
                }
            }
            float scope = biggestNum - smallestNum;
            for (int i = 0; i < hits.length; i++) {
                if(i > resultNum -1) continue;
                Document hitDoc = isearcher.doc(hits[i].doc);
                System.out.println(i + ") " + hitDoc.get("filename") + " " + hits[i].score + ",  " + hitDoc.get("fileid"));
                writer.write(queryId + " " + "Q0 " + hitDoc.get(LuceneConstants.DOCNO) + " " + (i+1) + " " + hits[i].score + " " + "STANDARD" + "\r\n");
            }

        }

        // prompt the user for input and quit the loop if they escape
        System.out.print(">>> ");
        //queryString = scanner.nextLine();
        // close everything and quit
        ireader.close();
        directory.close();
        writer.close();
    }

    public static void main(String[] args) {
        QueryIndex queryIndex = new QueryIndex();
        try {
            queryIndex.query("", 1, 1);
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        } catch (ParseException e) {
            System.out.println("ParseException: " + e);
        }
    }

}
