package com.example.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.model.Record;
import com.example.model.Word;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecordDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;
    private Context context;
    //构造方法
    public RecordDao(Context context){ this.context=context; }

    //添加方法
    public void add(Record record){
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();

        ContentValues values=new ContentValues();
        values.put("date",record.getDate());
        values.put("firstNum",record.getFirstNum());
        values.put("repeatNum",record.getRepeatNum());
        values.put("need_FirstNum",record.getNeed_FirstNum());
        values.put("need_RepeatNum",record.getNeed_RepeatNum());
        values.put("difficulty",record.getDifficulty());
        db.insert("tb_record",null,values);
        //关闭数据库
        db.close();
        helper.close();
    }
    //更新方法
    public void update(Record record){
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();

        ContentValues values=new ContentValues();
        values.put("date",record.getDate());
        values.put("firstNum",record.getFirstNum());
        values.put("repeatNum",record.getRepeatNum());
        values.put("need_FirstNum",record.getNeed_FirstNum());
        values.put("need_RepeatNum",record.getNeed_RepeatNum());
        db.update("tb_record",values,"_id=?",new String[]{String.valueOf(record.get_id())});
        //关闭数据库
        db.close();
        helper.close();
    }
    //插入或者返回方法,若有今天数据就返回，没有就新建
    public Record addOrGet(){
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        final Calendar c=Calendar.getInstance();//获取系统当前数据
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH)+1;
        int day=c.get(Calendar.DAY_OF_MONTH);
        String date0=year+"-"+month+"-"+day;
        String difficulty=settingDao.getDifficulty();
        String sql="select * from tb_record where date=? and difficulty=?";
        Cursor cursor=db.rawQuery(sql,new String[]{date0,difficulty});
        Record record;
        if (cursor.moveToNext()){
            int _id=cursor.getInt(cursor.getColumnIndex("_id"));
            String date=cursor.getString(cursor.getColumnIndex("date"));
            int firstNum=cursor.getInt(cursor.getColumnIndex("firstNum"));
            int repeatNum=cursor.getInt(cursor.getColumnIndex("repeatNum"));
            //获取最新的每天需要背的单词数量
            int need_FirstNum=settingDao.getNewNum();
            int need_RepeatNum=cursor.getInt(cursor.getColumnIndex("need_RepeatNum"));
            record=new Record(_id,date,firstNum,repeatNum,need_FirstNum,need_RepeatNum,difficulty);
            cursor.close();
            db.close();
            helper.close();
            update(record);
        }else {//没有就新建
            Log.i("今日首次进软件","新建了今日背单词的数据");
            int firstNum=0;
            int repeatNum=0;
            int need_FirstNum=settingDao.getNewNum();
            WordBookDao wordBookDao=new WordBookDao(context);
            int need_RepeatNum=wordBookDao.getTypeCount();
            record=new Record(date0,firstNum,repeatNum,need_FirstNum,need_RepeatNum,difficulty);
            add(record);
        }
        return record;
    }
    //返回列表中的开始索引为start的count条数据
    public List<Record> getRecord(int start, int count){
        List<Record> record_list=new ArrayList<Record>();
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();
        SettingDao settingDao=new SettingDao(context);
        String difficulty=settingDao.getDifficulty();
        String sql="select * from tb_record  where difficulty=? order by date DESC limit ?,? ";
        Cursor cursor=db.rawQuery(sql,new String[]{String.valueOf(start),String.valueOf(count),difficulty});
        while (cursor.moveToNext()){
            int _id=cursor.getInt(cursor.getColumnIndex("_id"));
           String date=cursor.getString(cursor.getColumnIndex("date"));
            int firstNum=cursor.getInt(cursor.getColumnIndex("firstNum"));
            int repeatNum=cursor.getInt(cursor.getColumnIndex("repeatNum"));
            int need_FirstNum=cursor.getInt(cursor.getColumnIndex("need_FirstNum"));
            int need_RepeatNum=cursor.getInt(cursor.getColumnIndex("need_RepeatNum"));
            Record record=new Record(_id,date,firstNum,repeatNum,need_FirstNum,need_RepeatNum,difficulty);
            record_list.add(record);
        }
        cursor.close();
        db.close();
        helper.close();
        return record_list;
    }

    //返回总记录数
    public int getCount(){
        helper=new DBOpenHelper(context);
        db=helper.getReadableDatabase();

        String sql="select count(_id) from tb_record";
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

    //更新今天已经背的单词数
    public void updatefirstNum(){
        Record record=addOrGet();
        record.setFirstNum(record.getFirstNum()+1);
        update(record);
    }
    //更新今天已经复习的单词数
    public void updatereviewNum(){
        Record record=addOrGet();
        record.setRepeatNum(record.getRepeatNum()+1);
        update(record);
    }
}
