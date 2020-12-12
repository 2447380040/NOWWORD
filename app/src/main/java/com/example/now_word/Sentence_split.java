package com.example.now_word;

import com.example.dao.WordDao;

public class Sentence_split {

    static public String getspit(String wordCN){
        String[] a=wordCN.split("\n");
        return a[0];
    }
}