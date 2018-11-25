package com.group.assignment;

/**
 * store constant value for this project
 *
 * Created by baolei chen on 2018/11/11.
 */
class LuceneConstants {

    static final String DOCNO = "DOCNO";
    static final String HT = "HT";
    static final String PARENT = "PARENT";
    static final String HEADER = "HEADER";
    static final String BYLINE = "BYLINE";
    static final String TEXT = "TEXT";
    static final String GRAPHIC = "GRAPHIC";

    static final int MAX_RESULTS = 2000;


    static final int MAX_SEARCH = 10;
    static final String HOME_PATH = System.getProperty("user.home")+"/lucene_data";
    static final String DOCUMENT_PATH = System.getProperty("user.home")+"/lucene_data/collection";
    static final String INDEX_PATH = System.getProperty("user.home")+"/lucene_data/index";
    static final String SEARCH_DIRECTORY = System.getProperty("user.home")+"/lucene_data/query-doc/topics.401-450";
}
