package com.group.assignment;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main{
    public static void main(String[] args) throws IOException {
       // IndexFiles.createIndex(args);
          //  QueryIndex.testGetAllQueries();
            IndexFiles.createIndex(args);
        try {
            QueryIndex.query();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}