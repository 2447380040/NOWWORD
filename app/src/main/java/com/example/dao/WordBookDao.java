package com.example.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.model.Word;
import com.example.model.WordBook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class WordBookDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;
    private Context context;
    //构造方法
    public WordBookDao(Context context){
        this.context=context;
    }

    //添加方法
    public void add(WordBook wordBook) {
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();

        ContentValues values=new ContentValues();
        values.put("wordId",wordBook.getWordId());
        values.put("isFalse",wordBook.getIsFalse());
        values.put("isFlag",wordBook.getIsFlag());
        values.put("reperaNum",wordBook.getReperaNum());
        values.put("timeFirst",wordBook.getTimeFirst());
        values.put("timeFinish",wordBook.getTimeFinish());
        db.insert("tb_wordbook",null,values);
        //关闭数据库
        db.close();
        helper.close();
    }

    //更新方法
    public void update(WordBook wordBook) {
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();

        ContentValues values=new ContentValues();
        values.put("wordId",wordBook.getWordId());
        values.put("isFalse",wordBook.getIsFalse());
        values.put("isFlag",wordBook.getIsFlag());
        values.put("reperaNum",wordBook.getReperaNum());
        values.put("timeFirst",wordBook.getTimeFirst());
        values.put("timeFinish",wordBook.getTimeFinish());
        db.update("tb_wordbook",values,"wordId=?",new String[]{String.valueOf(wordBook.getWordId())});
        //关闭数据库
        db.close();
        helper.close();
    }

    //查找方法
    public WordBook find(Word word) {
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();

        String sql="select * from tb_wordbook where wordId=?";
        Cursor cursor=db.rawQuery(sql,new String[]{String.valueOf(word.get_id())});
        if (cursor.moveToNext()){
            int _id=cursor.getInt(cursor.getColumnIndex("_id"));
            int wordId=cursor.getInt(cursor.getColumnIndex("wordId"));
            int isFalse=cursor.getInt(cursor.getColumnIndex("isFalse"));
            int isFlag=cursor.getInt(cursor.getColumnIndex("isFlag"));
            int reperaNum=cursor.getInt(cursor.getColumnIndex("reperaNum"));
            long timeFirst=cursor.getLong(cursor.getColumnIndex("timeFirst"));
            long timeFinish=cursor.getLong(cursor.getColumnIndex("timeFinish"));
            WordBook wordBook=new WordBook(_id,wordId,isFalse,isFlag,reperaNum,timeFirst,timeFinish);
            //关闭游标和数据库
            cursor.close();
            db.close();
            helper.close();
            return wordBook;
        }
        //关闭游标和数据库
        cursor.close();
        db.close();
        helper.close();
        return null;
    }

    //返回本难度所有背过的单词数据
    public List<Word> getLearnedWords() {
        List<Word> words_list=new ArrayList<Word>();
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        String type=settingDao.getDifficulty();
        String sql="select * from tb_word,tb_wordbook where tb_word._id=tb_wordbook.wordId and tb_word.wordType=?";
        Cursor cursor=db.rawQuery(sql,new String[]{type});
        while (cursor.moveToNext()){
            int _id=cursor.getInt(cursor.getColumnIndex("wordId"));
            int wordRank=cursor.getInt(cursor.getColumnIndex("wordRank"));
            String headWord=cursor.getString(cursor.getColumnIndex("headWord"));
            String sentences=cursor.getString(cursor.getColumnIndex("sentences"));
            String usphone=cursor.getString(cursor.getColumnIndex("usphone"));
            String ukphone=cursor.getString(cursor.getColumnIndex("ukphone"));
            String syno=cursor.getString(cursor.getColumnIndex("syno"));
            String phrases=cursor.getString(cursor.getColumnIndex("phrases"));
            String tranCN=cursor.getString(cursor.getColumnIndex("tranCN"));
            String tranEN=cursor.getString(cursor.getColumnIndex("tranEN"));
            String wordType=cursor.getString(cursor.getColumnIndex("wordType"));
            Word word=new Word(_id,wordRank,headWord,sentences,usphone,ukphone,syno,phrases,tranCN,tranEN,wordType);
            words_list.add(word);
        }
        cursor.close();
        db.close();
        helper.close();
        return words_list;
    }


    //返回本难度需复习的单词数据
    public List<Word> getNeedReWords(int todayneedreview) {
        List<Word> words_list=new ArrayList<Word>();
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        String type=settingDao.getDifficulty();
        String sql="select * from tb_word,tb_wordbook where tb_word._id=tb_wordbook.wordId and reperaNum<5  and  tb_word.wordType=? order by timeFirst ASC limit ? ";
        Cursor cursor=db.rawQuery(sql,new String[]{type,String.valueOf(todayneedreview)});
        while (cursor.moveToNext()){
            int _id=cursor.getInt(cursor.getColumnIndex("wordId"));
            int wordRank=cursor.getInt(cursor.getColumnIndex("wordRank"));
            String headWord=cursor.getString(cursor.getColumnIndex("headWord"));
            String sentences=cursor.getString(cursor.getColumnIndex("sentences"));
            String usphone=cursor.getString(cursor.getColumnIndex("usphone"));
            String ukphone=cursor.getString(cursor.getColumnIndex("ukphone"));
            String syno=cursor.getString(cursor.getColumnIndex("syno"));
            String phrases=cursor.getString(cursor.getColumnIndex("phrases"));
            String tranCN=cursor.getString(cursor.getColumnIndex("tranCN"));
            String tranEN=cursor.getString(cursor.getColumnIndex("tranEN"));
            String wordType=cursor.getString(cursor.getColumnIndex("wordType"));
            Word word=new Word(_id,wordRank,headWord,sentences,usphone,ukphone,syno,phrases,tranCN,tranEN,wordType);
            words_list.add(word);
        }
        cursor.close();
        db.close();
        helper.close();
        return words_list;
    }

    //返回本难度被标记的单词数据
    public List<Word> getFlagWords(int needCount) {
        List<Word> words_list0=new ArrayList<Word>();
        List<Word> words_list=new ArrayList<Word>();
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        String type=settingDao.getDifficulty();
        String sql="select * from tb_word,tb_wordbook where tb_word._id=tb_wordbook.wordId and isFlag=1 and  tb_word.wordType=?";
        Cursor cursor=db.rawQuery(sql,new String[]{type});
        while (cursor.moveToNext()) {
            int _id = cursor.getInt(cursor.getColumnIndex("wordId"));
            int wordRank = cursor.getInt(cursor.getColumnIndex("wordRank"));
            String headWord = cursor.getString(cursor.getColumnIndex("headWord"));
            String sentences = cursor.getString(cursor.getColumnIndex("sentences"));
            String usphone = cursor.getString(cursor.getColumnIndex("usphone"));
            String ukphone = cursor.getString(cursor.getColumnIndex("ukphone"));
            String syno = cursor.getString(cursor.getColumnIndex("syno"));
            String phrases = cursor.getString(cursor.getColumnIndex("phrases"));
            String tranCN = cursor.getString(cursor.getColumnIndex("tranCN"));
            String tranEN = cursor.getString(cursor.getColumnIndex("tranEN"));
            String wordType = cursor.getString(cursor.getColumnIndex("wordType"));
            Word word = new Word(_id, wordRank, headWord, sentences, usphone, ukphone, syno, phrases, tranCN, tranEN, wordType);
            words_list0.add(word);
        }
        if (words_list0.size() <= needCount) {//若单词数不够需要的则全部返回
            cursor.close();
            db.close();
            helper.close();
            return words_list0;
        } else {//若单词数够需要的则随机选择部分返回
            Random random = new Random();
            for (int i = 0; i < needCount; i++) {
                words_list.add(words_list0.get(random.nextInt(words_list0.size())));
            }
            cursor.close();
            db.close();
            helper.close();
            return words_list;
        }
    }

    //返回本难度错误过的单词数据，按错误次数从大到小排列
    public List<Word> getWrongWords(int needCount){
        List<Word> words_list0=new ArrayList<Word>();
        List<Word> words_list=new ArrayList<Word>();
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        String type=settingDao.getDifficulty();
        String sql="select * from tb_word,tb_wordbook where tb_word._id=tb_wordbook.wordId and isFalse>0 and  tb_word.wordType=? order by isFalse DESC";
        Cursor cursor=db.rawQuery(sql,new String[]{type});
        while (cursor.moveToNext()){
            int _id=cursor.getInt(cursor.getColumnIndex("wordId"));
            int wordRank=cursor.getInt(cursor.getColumnIndex("wordRank"));
            String headWord=cursor.getString(cursor.getColumnIndex("headWord"));
            String sentences=cursor.getString(cursor.getColumnIndex("sentences"));
            String usphone=cursor.getString(cursor.getColumnIndex("usphone"));
            String ukphone=cursor.getString(cursor.getColumnIndex("ukphone"));
            String syno=cursor.getString(cursor.getColumnIndex("syno"));
            String phrases=cursor.getString(cursor.getColumnIndex("phrases"));
            String tranCN=cursor.getString(cursor.getColumnIndex("tranCN"));
            String tranEN=cursor.getString(cursor.getColumnIndex("tranEN"));
            String wordType=cursor.getString(cursor.getColumnIndex("wordType"));
            Word word=new Word(_id,wordRank,headWord,sentences,usphone,ukphone,syno,phrases,tranCN,tranEN,wordType);
            words_list0.add(word);
        }
        if (words_list0.size() <= needCount) {//若单词数不够需要的则全部返回
            cursor.close();
            db.close();
            helper.close();
            return words_list0;
        } else {//若单词数够需要的则随机选择部分返回
            Random random = new Random();
            for (int i = 0; i < needCount; i++) {
                words_list.add(words_list0.get(random.nextInt(words_list0.size())));
            }
            cursor.close();
            db.close();
            helper.close();
            return words_list;
        }
    }

    //返回目前类型已背诵和完成复习的单词数据
    public List<Word> getTypeFinishWords(int needCount){
        List<Word> words_list0=new ArrayList<Word>();
        List<Word> words_list=new ArrayList<Word>();
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        String type=settingDao.getDifficulty();
        String sql="select * from tb_word,tb_wordbook where tb_word._id=tb_wordbook.wordId and reperaNum>4 and tb_word.wordType=?";
        Cursor cursor=db.rawQuery(sql,new String[]{type});
        while (cursor.moveToNext()){
            int _id=cursor.getInt(cursor.getColumnIndex("wordId"));
            int wordRank=cursor.getInt(cursor.getColumnIndex("wordRank"));
            String headWord=cursor.getString(cursor.getColumnIndex("headWord"));
            String sentences=cursor.getString(cursor.getColumnIndex("sentences"));
            String usphone=cursor.getString(cursor.getColumnIndex("usphone"));
            String ukphone=cursor.getString(cursor.getColumnIndex("ukphone"));
            String syno=cursor.getString(cursor.getColumnIndex("syno"));
            String phrases=cursor.getString(cursor.getColumnIndex("phrases"));
            String tranCN=cursor.getString(cursor.getColumnIndex("tranCN"));
            String tranEN=cursor.getString(cursor.getColumnIndex("tranEN"));
            String wordType=cursor.getString(cursor.getColumnIndex("wordType"));
            Word word=new Word(_id,wordRank,headWord,sentences,usphone,ukphone,syno,phrases,tranCN,tranEN,wordType);
            words_list0.add(word);
        }
        if (words_list0.size() <= needCount) {//若单词数不够需要的则全部返回
            cursor.close();
            db.close();
            helper.close();
            return words_list0;
        } else {//若单词数够需要的则随机选择部分返回
            Random random = new Random();
            for (int i = 0; i < needCount; i++) {
                words_list.add(words_list0.get(random.nextInt(words_list0.size())));
            }
            cursor.close();
            db.close();
            helper.close();
            return words_list;
        }
    }

    //返回某难度单词数据，按第一次背诵的时间排序
    public List<Word> getFirstTimeWords(){
        List<Word> words_list=new ArrayList<Word>();
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        String type=settingDao.getDifficulty();
        String sql="select * from tb_word,tb_wordbook where tb_word._id=tb_wordbook.wordId and tb_word.wordType=? order by timeFirst DESC ";
        Cursor cursor=db.rawQuery(sql,new String[]{type});
        while (cursor.moveToNext()){
            int _id=cursor.getInt(cursor.getColumnIndex("wordId"));
            int wordRank=cursor.getInt(cursor.getColumnIndex("wordRank"));
            String headWord=cursor.getString(cursor.getColumnIndex("headWord"));
            String sentences=cursor.getString(cursor.getColumnIndex("sentences"));
            String usphone=cursor.getString(cursor.getColumnIndex("usphone"));
            String ukphone=cursor.getString(cursor.getColumnIndex("ukphone"));
            String syno=cursor.getString(cursor.getColumnIndex("syno"));
            String phrases=cursor.getString(cursor.getColumnIndex("phrases"));
            String tranCN=cursor.getString(cursor.getColumnIndex("tranCN"));
            String tranEN=cursor.getString(cursor.getColumnIndex("tranEN"));
            String wordType=cursor.getString(cursor.getColumnIndex("wordType"));
            Word word=new Word(_id,wordRank,headWord,sentences,usphone,ukphone,syno,phrases,tranCN,tranEN,wordType);
            words_list.add(word);
        }
        cursor.close();
        db.close();
        helper.close();
        return words_list;
    }

    //返回单词数据，按最终完成背诵和复习的时间排序
    public List<Word> getFinishTimeWords(){
        List<Word> words_list=new ArrayList<Word>();
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        String type=settingDao.getDifficulty();
        String sql="select * from tb_word,tb_wordbook where tb_word._id=tb_wordbook.wordId and tb_word.wordType=? order by timeFinish DESC ";
        Cursor cursor=db.rawQuery(sql,new String[]{type});
        while (cursor.moveToNext()){
            int _id=cursor.getInt(cursor.getColumnIndex("wordId"));
            int wordRank=cursor.getInt(cursor.getColumnIndex("wordRank"));
            String headWord=cursor.getString(cursor.getColumnIndex("headWord"));
            String sentences=cursor.getString(cursor.getColumnIndex("sentences"));
            String usphone=cursor.getString(cursor.getColumnIndex("usphone"));
            String ukphone=cursor.getString(cursor.getColumnIndex("ukphone"));
            String syno=cursor.getString(cursor.getColumnIndex("syno"));
            String phrases=cursor.getString(cursor.getColumnIndex("phrases"));
            String tranCN=cursor.getString(cursor.getColumnIndex("tranCN"));
            String tranEN=cursor.getString(cursor.getColumnIndex("tranEN"));
            String wordType=cursor.getString(cursor.getColumnIndex("wordType"));
            Word word=new Word(_id,wordRank,headWord,sentences,usphone,ukphone,syno,phrases,tranCN,tranEN,wordType);
            words_list.add(word);
        }
        cursor.close();
        db.close();
        helper.close();
        return words_list;
    }

    //返回指定数量的错误高危单词，高危单词是错误次数大于等于2的单词
    public List<Word> getHighWrongWords(int needCount,int highWrongtime ) {//传入测试的单词数量和最小错误次数
        List<Word> words_list0 = new ArrayList<Word>();
        List<Word> words_list = new ArrayList<Word>();
        helper = new DBOpenHelper(context);
        db = helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        String type=settingDao.getDifficulty();
        String sql = "select * from tb_word,tb_wordbook where tb_word._id=tb_wordbook.wordId and isFalse>=? and tb_word.wordType=?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(highWrongtime),type});
        while (cursor.moveToNext()) {
            int _id = cursor.getInt(cursor.getColumnIndex("wordId"));
            int wordRank = cursor.getInt(cursor.getColumnIndex("wordRank"));
            String headWord = cursor.getString(cursor.getColumnIndex("headWord"));
            String sentences = cursor.getString(cursor.getColumnIndex("sentences"));
            String usphone = cursor.getString(cursor.getColumnIndex("usphone"));
            String ukphone = cursor.getString(cursor.getColumnIndex("ukphone"));
            String syno = cursor.getString(cursor.getColumnIndex("syno"));
            String phrases = cursor.getString(cursor.getColumnIndex("phrases"));
            String tranCN = cursor.getString(cursor.getColumnIndex("tranCN"));
            String tranEN = cursor.getString(cursor.getColumnIndex("tranEN"));
            String wordType = cursor.getString(cursor.getColumnIndex("wordType"));
            Word word = new Word(_id, wordRank, headWord, sentences, usphone, ukphone, syno, phrases, tranCN, tranEN, wordType);
            words_list0.add(word);
        }
        if (words_list0.size() <= needCount) {//若单词数不够需要的则全部返回
            cursor.close();
            db.close();
            helper.close();
            return words_list0;
        } else {//若单词数够需要的则随机选择部分返回
            Random random = new Random();
            for (int i = 0; i < needCount; i++) {
                words_list.add(words_list0.get(random.nextInt(words_list0.size())));
            }
            cursor.close();
            db.close();
            helper.close();
            return words_list;
        }
    }

    //获取总共的背过单词数
    public int getAllCount(){
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();

        String sql="select count(tb_word._id) from tb_wordbook,tb_word where tb_word._id=tb_wordbook.wordId";
        Cursor cursor=db.rawQuery(sql,null);
        if (cursor.moveToNext()){
            int count= cursor.getInt(0);
            //关闭游标和数据库
            cursor.close();
            db.close();
            helper.close();
            return count;
        }
        return 0;
    }

    //获取某类型的背过单词数
    public int getTypeCount(){
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        String difficulty=settingDao.getDifficulty();
        String sql="select count(tb_word._id) from tb_wordbook,tb_word where wordType=? and tb_word._id=tb_wordbook.wordId";
        Cursor cursor=db.rawQuery(sql,new String[]{difficulty});
        if (cursor.moveToNext()){
            int count= cursor.getInt(0);
            //关闭游标和数据库
            cursor.close();
            db.close();
            helper.close();
            return count;
        }
        return 0;
    }

    //获取总共的已完成背诵和复习的单词数
    public int getAllFinishCount(){
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();

        String sql="select count(tb_word._id) from tb_wordbook,tb_word where tb_word._id=tb_wordbook.wordId and reperaNum>4";
        Cursor cursor=db.rawQuery(sql,null);
        if (cursor.moveToNext()){
            int count= cursor.getInt(0);
            //关闭游标和数据库
            cursor.close();
            db.close();
            helper.close();
            return count;
        }
        return 0;
    }

    //获取某类型的已完成背诵和复习的单词数
    public int getTypeFinishCount(){
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        String difficulty=settingDao.getDifficulty();
        String sql="select count(tb_word._id) from tb_wordbook,tb_word where wordType=? and tb_word._id=tb_wordbook.wordId and reperaNum>4";
        Cursor cursor=db.rawQuery(sql,new String[]{difficulty});
        if (cursor.moveToNext()){
            int count= cursor.getInt(0);
            //关闭游标和数据库
            cursor.close();
            db.close();
            helper.close();
            return count;
        }
        return 0;
    }
    //获取某类型的标记的单词数
    public int getTypeFlagCount(){
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        String difficulty=settingDao.getDifficulty();
        String sql="select count(tb_word._id) from tb_wordbook,tb_word where wordType=? and tb_word._id=tb_wordbook.wordId and isFlag=1";
        Cursor cursor=db.rawQuery(sql,new String[]{difficulty});
        if (cursor.moveToNext()){
            int count= cursor.getInt(0);
            //关闭游标和数据库
            cursor.close();
            db.close();
            helper.close();
            return count;
        }
        return 0;
    }
    //获取某难度需要复习的单词数量
    public int getTypeReCount(){
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        String difficulty=settingDao.getDifficulty();
        String sql="select count(tb_word._id) from tb_wordbook,tb_word where tb_word._id=tb_wordbook.wordId and reperaNum<5 and wordType=?";
        Cursor cursor=db.rawQuery(sql,new String[]{difficulty});
        if (cursor.moveToNext()){
            int count= cursor.getInt(0);
            //关闭游标和数据库
            cursor.close();
            db.close();
            helper.close();
            return count;
        }
        return 0;
    }
    //获取某个难度错误的单词数
    public int getTypeWrongCount(){
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        String difficulty=settingDao.getDifficulty();
        String sql="select count(tb_word._id) from tb_wordbook,tb_word where tb_word._id=tb_wordbook.wordId and isFalse>0 and wordType=?";
        Cursor cursor=db.rawQuery(sql,new String[]{difficulty});
        if (cursor.moveToNext()){
            int count= cursor.getInt(0);
            //关闭游标和数据库
            cursor.close();
            db.close();
            helper.close();
            return count;
        }
        return 0;
    }
    public int getTypeHighWrongCount(int miniwrongcount){
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        String difficulty=settingDao.getDifficulty();
        String sql="select count(tb_word._id) from tb_wordbook,tb_word where tb_word._id=tb_wordbook.wordId and isFalse>? and wordType=?";
        Cursor cursor=db.rawQuery(sql,new String[]{String.valueOf(miniwrongcount),difficulty});
        if (cursor.moveToNext()){
            int count= cursor.getInt(0);
            //关闭游标和数据库
            cursor.close();
            db.close();
            helper.close();
            return count;
        }
        return 0;
    }

    //选择正确时的调用方法
    public void trueSaveDate(Word word){
            WordBook wordBook=find(word);
            if(wordBook!=null){//如果单词本中存在本单词数据
                wordBook.setReperaNum(wordBook.getReperaNum()+1);
                if (wordBook.getReperaNum()==4){//若这是第4次复习
                    wordBook.setTimeFinish(System.currentTimeMillis());
                }
            }
        else {//如果单词本中未有此单词数据，就说明这是新背的单词
            WordBook wordBook1=new WordBook(word.get_id(),0,0,0,System.currentTimeMillis(),0);
            add(wordBook1);
        }
    }

    //选择错误时的调用方法
    public void wrongSaveDate(Word word){
        WordBook wordBook=find(word);
        if(wordBook!=null){//如果单词本中存在本单词数据
            wordBook.setReperaNum(wordBook.getReperaNum()+1);
            wordBook.setIsFalse(wordBook.getIsFalse()+1);
            if (wordBook.getReperaNum()==4){//若这是第4次复习
                wordBook.setTimeFinish(System.currentTimeMillis());
            }
            update(wordBook);
        }
        else {//如果单词本中未有此单词数据，就说明这是新背的单词
            WordBook wordBook1=new WordBook(word.get_id(),1,0,0,System.currentTimeMillis(),0);
            add(wordBook1);
        }
    }
    //五五循环只对错误的次数进行更新
    public void fivewrongSaveDate(Word word){
        WordBook wordBook=find(word);
        if(wordBook!=null){//如果单词本中存在本单词数据
            wordBook.setIsFalse(wordBook.getIsFalse()+1);
            update(wordBook);
            }
        }
}
