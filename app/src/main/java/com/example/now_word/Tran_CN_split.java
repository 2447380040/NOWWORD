package com.example.now_word;

public class Tran_CN_split {

    static public String getspit(String wordCN){
        StringBuffer stringBuffer=new StringBuffer();
        String[] allTypeCn=wordCN.split("\\s");//将不同的类型的中文意思进行切割
        for(int i=0;i<allTypeCn.length;i++){//注意i=0的是“”,所以从1开始
            stringBuffer.append(allTypeCn[i].split("；")[0]+";") ;//将每个类型只提取第一个意思
        }
        return stringBuffer.toString();
    }
    static public String getCNvoice(String wordCN){
        String regex="[a-zA-Z\\p{Punct}]";
        wordCN=wordCN.replaceAll(regex,"");
        System.out.println(wordCN);
        return wordCN;
    }
}

