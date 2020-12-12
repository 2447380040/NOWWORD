package com.example.model;


//单词收入实体类


public class Word {
    private int _id;        //单词编号
    private int wordRank;   //在本类型中的编号
    private String headWord; //单词
    private String sentences;   //例句
    private String usphone;     //美式音标
    private String ukphone;     //英式音标
    private String syno;        //同近词
    private String phrases;     //词组
    private String tranCN;      //中文释义
    private String tranEN;      //英文释义
    private String wordType;      //类型id

    //默认构造方法
    public Word(){             //默认构造方法
        super();
    }
    //定义有参数构造方法，初始化信息实体中的字段
    public Word(int wordRank, String headWord, String sentences, String usphone, String ukphone, String syno, String phrases, String tranCN, String tranEN, String wordType){
        this.wordRank = wordRank;
        this.headWord = headWord;
        this.sentences = sentences;
        this.usphone = usphone;
        this.ukphone = ukphone;
        this.syno = syno;
        this.phrases = phrases;
        this.tranCN = tranCN;
        this.tranEN = tranEN;
        this.wordType = wordType;
    }

    public Word(int _id, int wordRank, String headWord, String sentences, String usphone, String ukphone, String syno, String phrases, String tranCN, String tranEN, String wordType) {
        this._id = _id;
        this.wordRank = wordRank;
        this.headWord = headWord;
        this.sentences = sentences;
        this.usphone = usphone;
        this.ukphone = ukphone;
        this.syno = syno;
        this.phrases = phrases;
        this.tranCN = tranCN;
        this.tranEN = tranEN;
        this.wordType = wordType;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getWordRank() {
        return wordRank;
    }

    public void setWordRank(int wordRank) {
        this.wordRank = wordRank;
    }

    public String getHeadWord() {
        return headWord;
    }

    public void setHeadWord(String headWord) {
        this.headWord = headWord;
    }

    public String getSentences() {
        return sentences;
    }

    public void setSentences(String sentences) {
        this.sentences = sentences;
    }

    public String getUsphone() {
        return usphone;
    }

    public void setUsphone(String usphone) {
        this.usphone = usphone;
    }

    public String getUkphone() {
        return ukphone;
    }

    public void setUkphone(String ukphone) {
        this.ukphone = ukphone;
    }

    public String getSyno() {
        return syno;
    }

    public void setSyno(String syno) {
        this.syno = syno;
    }

    public String getPhrases() {
        return phrases;
    }

    public void setPhrases(String phrases) {
        this.phrases = phrases;
    }

    public String getTranCN() {
        return tranCN;
    }

    public void setTranCN(String tranCN) {
        this.tranCN = tranCN;
    }

    public String getTranEN() {
        return tranEN;
    }

    public void setTranEN(String tranEN) {
        this.tranEN = tranEN;
    }

    public String getWordType() {
        return wordType;
    }

    public void setWordType(String wordType) {
        this.wordType =wordType;
    }
}
