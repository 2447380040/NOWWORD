package com.example.model;
//设置信息类
public class Setting {
    private int _id;        //Id
    private int sock;       //是否开启了锁屏背单词，0：否 1：是
    private String difficulty ;     //难度
    private int newnum;     //每天需要新背的单词数量
    private int socknum;    //解锁需要背的单词数量
    //默认构造方法
    public Setting(){ super(); }
    //定义有参数构造方法，初始化信息实体中的字段
    public Setting(int _id, int sock, String difficulty, int newnum, int socknum) {
        this._id = _id;
        this.sock = sock;
        this.difficulty = difficulty;
        this.newnum = newnum;
        this.socknum = socknum;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getSock() {
        return sock;
    }

    public void setSock(int sock) {
        this.sock = sock;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getNewnum() {
        return newnum;
    }

    public void setNewnum(int newnum) {
        this.newnum = newnum;
    }

    public int getSocknum() {
        return socknum;
    }

    public void setSocknum(int socknum) {
        this.socknum = socknum;
    }
}
