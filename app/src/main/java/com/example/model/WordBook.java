package com.example.model;

//我的单词本实体类
public class WordBook {
    private int _id;            //Id
    private int wordId;         //外键，对应单词表中的Id
    private int isFalse;        //错误次数，默认为0
    private int isFlag;         //是否标记，0：未标记 1：标记
    private int reperaNum;      //复习次数，默认为0
    private long timeFirst;     //首次背诵时间
    private long timeFinish;    //单词完成时间

    //默认构造方法
    public WordBook(){             //默认构造方法
        super();
    }

    //定义有参数构造方法，初始化信息实体中的字段
    public WordBook(int wordId, int isFalse, int isFlag, int reperaNum, long timeFirst, long timeFinish) {
        this.wordId = wordId;
        this.isFalse = isFalse;
        this.isFlag = isFlag;
        this.reperaNum = reperaNum;
        this.timeFirst = timeFirst;
        this.timeFinish = timeFinish;
    }

    public WordBook(int id, int wordId, int isFalse, int isFlag, int reperaNum, long timeFirst, long timeFinish) {
        this._id=id;
        this.wordId = wordId;
        this.isFalse = isFalse;
        this.isFlag = isFlag;
        this.reperaNum = reperaNum;
        this.timeFirst = timeFirst;
        this.timeFinish = timeFinish;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public int getIsFalse() {
        return isFalse;
    }

    public void setIsFalse(int isFalse) {
        this.isFalse = isFalse;
    }

    public int getIsFlag() {
        return isFlag;
    }

    public void setIsFlag(int isFlag) {
        this.isFlag = isFlag;
    }

    public int getReperaNum() {
        return reperaNum;
    }

    public void setReperaNum(int reperaNum) {
        this.reperaNum = reperaNum;
    }

    public long getTimeFirst() {
        return timeFirst;
    }

    public void setTimeFirst(long timeFirst) {
        this.timeFirst = timeFirst;
    }

    public long getTimeFinish() {
        return timeFinish;
    }

    public void setTimeFinish(long timeFinish) {
        this.timeFinish = timeFinish;
    }

}

