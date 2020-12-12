package com.example.model;

//记录实体类

public class Record {
    private int _id;        //Id
    private String date;    //日期
    private int firstNum;    //今日已经背诵新单词数量
    private int repeatNum;      //今日已经复习单词数量
    private int need_FirstNum;  //今日需要背诵新单词的数量
    private int need_RepeatNum; //今日需要复习的单词数量
    private String difficulty ;       //背单词难度类型
    //默认构造方法
    public Record(){             //默认构造方法
        super();
    }
    //定义有参数构造方法，初始化信息实体中的字段
    public Record( String date, int firstNum, int repeatNum, int need_FirstNum, int need_RepeatNum,String difficulty) {
        this.date = date;
        this.firstNum = firstNum;
        this.repeatNum = repeatNum;
        this.need_FirstNum = need_FirstNum;
        this.need_RepeatNum = need_RepeatNum;
        this.difficulty=difficulty;
    }

    public Record(int _id, String date, int firstNum, int repeatNum,  int need_FirstNum, int need_RepeatNum,String difficulty) {
        this._id = _id;
        this.date = date;
        this.firstNum = firstNum;
        this.repeatNum = repeatNum;
        this.need_FirstNum = need_FirstNum;
        this.need_RepeatNum = need_RepeatNum;
        this.difficulty=difficulty;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFirstNum() {
        return firstNum;
    }

    public void setFirstNum(int firstNum) {
        this.firstNum = firstNum;
    }

    public int getRepeatNum() {
        return repeatNum;
    }

    public void setRepeatNum(int repeatNum) {
        this.repeatNum = repeatNum;
    }

    public int getNeed_FirstNum() {
        return need_FirstNum;
    }

    public void setNeed_FirstNum(int need_FirstNum) {
        this.need_FirstNum = need_FirstNum;
    }

    public int getNeed_RepeatNum() {
        return need_RepeatNum;
    }

    public void setNeed_RepeatNum(int need_RepeatNum) {
        this.need_RepeatNum = need_RepeatNum;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
