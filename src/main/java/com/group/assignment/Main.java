package com.group.assignment;

import java.io.IOException;

public class Main{
    public static void main(String[] args){
        //choose index

        //creat index

        try {
            CreateIndex.createIndex(args);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //query
    }
}