package com.group.assignment;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

public class Main{
    public static void main(String[] args) throws IOException {
       // IndexFiles.createIndex(args);
          //  QueryIndex.testGetAllQueries();
      //  String test = "<helf>hello<dfdf>";
     //   System.out.println(test.replaceAll("<.*?>",""));

            IndexFiles.createIndex(args);
        try {
            QueryIndex.query();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}