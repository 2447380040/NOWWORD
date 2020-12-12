package com.example.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.model.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;
    private Context context;
    //构造方法
    public WordDao(Context context){
        this.context=context;
    }

    //添加方法,向数据库中添加单词
    public void add(Word word) {
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("wordRank",word.getWordRank());
        values.put("headWord",word.getHeadWord());
        values.put("sentences",word.getSentences());
        values.put("usphone",word.getUsphone());
        values.put("ukphone",word.getUkphone());
        values.put("syno",word.getSyno());
        values.put(" phrases",word.getPhrases());
        values.put("tranCN",word.getTranCN());
        values.put("tranEN",word.getTranEN());
        values.put("wordType",word.getWordType());
        db.insert("tb_word",null,values);
        //关闭数据库
        db.close();
        helper.close();
    }
    //查找方法
    public Word find(int wordId){
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();

        String sql="select * from tb_word where _id=?";
        Cursor cursor=db.rawQuery(sql,new String[]{String.valueOf(wordId)});
        if (cursor.moveToNext()){
            int _id=cursor.getInt(cursor.getColumnIndex("_id"));
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
            //关闭游标和数据库
            cursor.close();
            db.close();
            helper.close();
            return word;
        }
        //关闭游标和数据库
        cursor.close();
        db.close();
        helper.close();
        return null;
    }

    //返回索引为start开始的count条数据
    public List<Word> getWords(int start,int neednewwordCount) {
        List<Word> words_list=new ArrayList<Word>();
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        String type=settingDao.getDifficulty();
        System.out.println(type);
        String sql="select * from tb_word where wordType=? limit ?,? ";
        Cursor cursor=db.rawQuery(sql,new String[]{type,String.valueOf(start),String.valueOf(neednewwordCount)});
        while (cursor.moveToNext()){
            int _id=cursor.getInt(cursor.getColumnIndex("_id"));
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
    //返回某一类的单词
    public List<Word> getTypeWords() {
        List<Word> words_list=new ArrayList<Word>();
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        String type=settingDao.getDifficulty();
        System.out.println(type);
        String sql="select * from tb_word where wordType=?  ";
        Cursor cursor=db.rawQuery(sql,new String[]{type});
        while (cursor.moveToNext()){
            int _id=cursor.getInt(cursor.getColumnIndex("_id"));
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
    public static boolean isContainChinese( String str) {
        String regex = "[\\u4e00-\\u9fa5]";   //汉字的Unicode取值范围
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(str);
        return match.find();
    }
        //返回搜索的单词
    public List<Word> getSerchWords(String str) {//0表示英文，1表示中文
        String sql;
        List<Word> words_list=new ArrayList<Word>();
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        if (str==null){return words_list;}
        //判断传入的字符串是否包含中文
        if (!isContainChinese(str)){
            str=str+"%";
            sql="select * from tb_word where headWord like ?";
        }else {
            str="%"+str+"%";
            sql="select * from tb_word where tranCN like ?";
        }

        Cursor cursor=db.rawQuery(sql,new String[]{str});
        while (cursor.moveToNext()){
            int _id=cursor.getInt(cursor.getColumnIndex("_id"));
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


    //获取某一类型的总单词数
    public int getTypeCount()  {
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        String type=settingDao.getDifficulty();
        String sql="select count(_id) from tb_word where wordType=?";
        Cursor cursor=db.rawQuery(sql,new String[]{type});
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

    //随机随机的单词
    public  List<Word> getwrongwords(int needCount){
        List<Word> wronglist=new ArrayList<Word>();
        Random random = new Random();
        for (int n = 0; n < needCount; n++) {
            int j = random.nextInt(getTypeCount());
            wronglist.add(find(j));
        }
        return wronglist;
    }

}
