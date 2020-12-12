package com.example.now_word;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dao.RecordDao;
import com.example.dao.SettingDao;
import com.example.dao.WordBookDao;
import com.example.dao.WordDao;
import com.example.model.Record;
import com.example.model.Word;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MainStudyActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private TextView word_text,ukphone_text,usphone_text,sentence_text,tranEN_text;//用来显示单词和音标的
    private RadioGroup radioGroup;      //用于加载单词的四个选项
    private RadioButton radioOne, radioTwo, radioThree,radioFour;   //  单词意思的四个选项
    private RadioButton[] radioButtonsgroups=new RadioButton[4];
    private TextView today_neednewCount,today_needreviewCount;//提示框
    private ImageView play_uk_voiceButton,play_us_voiceButton;//播放声音按钮
    private AudioService audioService;//播放音频类
    //数据库操作类
    private WordDao wordDao;
    private WordBookDao wordBookDao;
    private SettingDao settingDao;
    private RecordDao recordDao;
    //public MediaHelper mediaHelper;
    int start;    //定位要开始背单词的位置
    List<Word> needstudy;//需要学习的所有单词
    List<Word> needreview;//需要新学的单词
    List<Word> neednewstudy;//需要复习的单词
    LinkedList<Word> five_five;    //五五循环列表
    Iterator<Word> iterator_five;
    Word nowword;   //现在正在背的单词
    int i=-1;//目前背诵单词的位置
    int size;//要学习的单词总数量
    boolean five_flag=false;//五五循环的标志
    int today_neednewcount,today_needreviewcount;//记录还需要背和复习的单词数量
    int rightCount;//正确次数
    int wrongCount;//错误次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_study);    //绑定布局文件
        init();//初始化控件
    }

    /**
     * 初始化控件
     */
    public void init() {
        //初始化按钮
        word_text=findViewById(R.id.word_text);
        ukphone_text=findViewById(R.id.ukphone_text);
        usphone_text=findViewById(R.id.usphone_text);
        sentence_text=findViewById(R.id.sentence_text);
        tranEN_text=findViewById(R.id.tranEN_text);
        radioGroup=findViewById(R.id.choose_group);
        radioOne=findViewById(R.id.choose_btn_one);
        radioTwo=findViewById(R.id.choose_btn_two);
        radioThree=findViewById(R.id.choose_btn_three);
        radioFour=findViewById(R.id.choose_btn_four);
        radioButtonsgroups[0]=radioOne;
        radioButtonsgroups[1]=radioTwo;
        radioButtonsgroups[2]=radioThree;
        radioButtonsgroups[3]=radioFour;
        today_neednewCount=findViewById(R.id.today_neednewCount);
        today_needreviewCount=findViewById(R.id.today_needreviewCount);
        //初始化播放声音
        play_uk_voiceButton =findViewById(R.id.play_vioce_uk);
        play_us_voiceButton=findViewById(R.id.play_vioce_us);
        audioService=new AudioService(this);

        //初始化数据库操作
        wordDao=new WordDao(this);
        wordBookDao=new WordBookDao(this);
        settingDao=new SettingDao(this);
        recordDao=new RecordDao(this);

        //给按钮添加监听
        radioGroup.setOnCheckedChangeListener(this);
        play_uk_voiceButton.setOnClickListener(this);
        play_us_voiceButton.setOnClickListener(this);
        sentence_text.setOnClickListener(this);
        tranEN_text.setOnClickListener(this);


        start=wordBookDao.getTypeCount();//获取背过的该类单词数来定位开始位置
        needstudy=new ArrayList<>();
        Record record = recordDao.addOrGet();
        int needreviewwordCount=record.getNeed_RepeatNum()-record.getRepeatNum();//今日还需复习的单词数量
        int neednewwordCount=record.getNeed_FirstNum()-record.getFirstNum();//今日还需背的单词数量
        neednewstudy=wordDao.getWords(start,neednewwordCount);//获取需要新背的单词
        needreview=wordBookDao.getNeedReWords(needreviewwordCount);//获取需要复习的单词
        five_five=new LinkedList<Word>();//初始化五五循环列表
        iterator_five=five_five.iterator();
        Iterator<Word> iterable1=neednewstudy.iterator();
        //将需要背的单词加入到学习列表中
        while (iterable1.hasNext()){
            needstudy.add(iterable1.next());
        }
        Iterator<Word> iterator2=needreview.iterator();
        while (iterator2.hasNext()){
            needstudy.add(iterator2.next());
        }
        size=needstudy.size();//学习单词的总数量
        getNextData();

    }


    /**
     * 设置选项
     */
    private void setword(Word word) {
            word_text.setText(word.getHeadWord());
            ukphone_text.setText(word.getUkphone());
            usphone_text.setText(word.getUsphone());
            sentence_text.setText(Sentence_split.getspit(word.getSentences()));
            tranEN_text.setText(word.getTranEN());

            Random random = new Random();
            List<Word> worrywords = new ArrayList<Word>();
            //随机获得三个其他错误单词
            worrywords=wordDao.getwrongwords(3);
            int r = random.nextInt(4);//获取正确答案的选项位置
            switch (r) {
                case 0:
                    radioOne.setText("A: " + Tran_CN_split.getspit(word.getTranCN()));
                    radioTwo.setText("B: " + Tran_CN_split.getspit(worrywords.get(0).getTranCN()));
                    radioThree.setText("C: " + Tran_CN_split.getspit(worrywords.get(1).getTranCN()));
                    radioFour.setText("D: " + Tran_CN_split.getspit(worrywords.get(2).getTranCN()));
                    break;
                case 1:
                    radioOne.setText("A: " + Tran_CN_split.getspit(worrywords.get(0).getTranCN()));
                    radioTwo.setText("B: " + Tran_CN_split.getspit(word.getTranCN()));
                    radioThree.setText("C: " + Tran_CN_split.getspit(worrywords.get(1).getTranCN()));
                    radioFour.setText("D: " + Tran_CN_split.getspit(worrywords.get(2).getTranCN()));
                    break;
                case 2:
                    radioOne.setText("A: " + Tran_CN_split.getspit(worrywords.get(1).getTranCN()));
                    radioTwo.setText("B: " + Tran_CN_split.getspit(worrywords.get(0).getTranCN()));
                    radioThree.setText("C: " + Tran_CN_split.getspit(word.getTranCN()));
                    radioFour.setText("D: " + Tran_CN_split.getspit(worrywords.get(2).getTranCN()));
                    break;
                case 3:
                    radioOne.setText("A: " + Tran_CN_split.getspit(worrywords.get(2).getTranCN()));
                    radioTwo.setText("B: " + Tran_CN_split.getspit(worrywords.get(0).getTranCN()));
                    radioThree.setText("C: " + Tran_CN_split.getspit(worrywords.get(1).getTranCN()));
                    radioFour.setText("D: " + Tran_CN_split.getspit(word.getTranCN()));
                    break;
            }
        audioService.updateMP(nowword.getHeadWord(),0);
           // audioService.updateMP(nowword.getTranEN());
        }


    /**
     * 点击事件,播放单词语音
     */
    @Override
    public void onClick(View v) {
        //播放单词语音
        switch (v.getId()) {
            case R.id.play_vioce_uk://播放单词声音
                audioService.updateMP(nowword.getHeadWord(),0);
                break;
            case R.id.play_vioce_us:
                audioService.updateMP(nowword.getHeadWord(),1);
                break;
            case R.id.sentence_text:
                audioService.updateMP(Sentence_split.getspit(nowword.getSentences()),0);
                break;
            case R.id.tranEN_text:
                audioService.updateMP(nowword.getTranEN(),0);
                break;
        }
    }
    /**
     * 选项的点击事件
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        radioGroup.setClickable(false);            //默认选项未被选中

        radioGroup.setEnabled(false);
        radioTwo.setEnabled(false);
        radioOne.setEnabled(false);
        radioThree.setEnabled(false);
        radioFour.setEnabled(false);
        switch (checkedId) {                        //选项的点击事件
            case R.id.choose_btn_one:                //点击“A”选项
                String msg = radioOne.getText().toString().substring(3);        //截取字符串
                btnGetText(msg, radioOne);            //将参数传入到对应的方法里
                break;
            case R.id.choose_btn_two:                //点击“B”选项
                String msg1 = radioTwo.getText().toString().substring(3);    //截取字符串
                btnGetText(msg1, radioTwo);        //将参数传入到对应的方法里
                break;
            case R.id.choose_btn_three:            //点击“C”选项
                String msg2 = radioThree.getText().toString().substring(3);    //截取字符串
                btnGetText(msg2, radioThree);        //将参数传入到对应的方法里
                break;
            case R.id.choose_btn_four:            //点击“D”选项
                String msg3 = radioThree.getText().toString().substring(3);    //截取字符串
                btnGetText(msg3, radioFour);        //将参数传入到对应的方法里
                break;
        }
    }

    /**
     * 设置选项的不同颜色
     */
    private void btnGetText(String msg, RadioButton btn) {
        /**
         * 答题答对了 设置绿色 答错设置红色
         * */
        if (five_flag) {//若是五五循环
            if (msg.equals(Tran_CN_split.getspit(nowword.getTranCN()))) {
                btn.setBackgroundColor(getResources().getColor(R.color.TIANYUANLV));        //设置选项为绿色
            } else {
                btn.setBackgroundColor(getResources().getColor(R.color.YANHONG));            //设置选项为红色
                for (int k = 0; k < 4; k++) {                   //设置正确答案的颜色为绿色
                    if (radioButtonsgroups[k].getText().toString().substring(3).equals(Tran_CN_split.getspit(nowword.getTranCN()))) {
                        radioButtonsgroups[k].setBackgroundColor(getResources().getColor(R.color.TIANYUANLV));
                    }
                }
                wordBookDao.fivewrongSaveDate(nowword);//五五循环单独的处理
            }
        } else {//背新的单词
            if (msg.equals(Tran_CN_split.getspit(nowword.getTranCN()))) {
                btn.setBackgroundColor(getResources().getColor(R.color.TIANYUANLV));        //设置选项为绿色
                wordBookDao.trueSaveDate(nowword);
                rightCount++;
            }//保存到数据库
            else{
                btn.setBackgroundColor(getResources().getColor(R.color.YANHONG));            //设置选项为红色
                for (int k = 0; k < 4; k++) {                   //设置正确答案的颜色为绿色
                    if (radioButtonsgroups[k].getText().toString().substring(3).equals(Tran_CN_split.getspit(nowword.getTranCN()))) {
                    radioButtonsgroups[k].setBackgroundColor(getResources().getColor(R.color.TIANYUANLV));
                    }
                }
                wordBookDao.wrongSaveDate(nowword);//保存到数据库
                wrongCount++;

            }
        }
        //先设置不被点击然后设置延时
        radioOne.setChecked(false);        //默认不被点击
        radioTwo.setChecked(false);        //默认不被点击
        radioThree.setChecked(false);        //默认不被点击
        radioFour.setChecked(false);        //默认不被点击
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getNextData();
            }
        },1000);
    }

    /**
     * 还原单词与选项的颜色
     */
    private void setTextColor() {
        //还原单词选项的颜色
        radioGroup.setEnabled(true);
        radioOne.setEnabled(true);
        radioThree.setEnabled(true);
        radioTwo.setEnabled(true);
        radioFour.setEnabled(true);

        radioOne.setBackgroundColor(Color.parseColor("#ffffff"));      //将选项的按钮设置为白色
        radioTwo.setBackgroundColor(Color.parseColor("#ffffff"));     //将选项的按钮设置为白色
        radioThree.setBackgroundColor(Color.parseColor("#ffffff"));     //将选项的按钮设置为白色
        radioFour.setBackgroundColor(Color.parseColor("#ffffff"));        //将选项的按钮设置为白色

    }
    //更新今日的背单词数量和复习词数量
    private void update(Word word){
        System.out.println(word.getHeadWord());
        if(wordBookDao.find(word).getReperaNum()==0){//新学单词，今日的新学单词数加1
            recordDao.updatefirstNum();
        }else {//复习次数大于0就是复习单词，今日的复习单词数加1
            recordDao.updatereviewNum();
        }
    }


    private void getNextData() {
        if (iterator_five.hasNext()) {  //五五循环未结束,复习今日的五五循环
            five_flag=true;
            nowword=iterator_five.next();
            setTextColor();
            setword(nowword);
        } else {            //五五循环已结束,学习一个新的词
            five_flag=false;
            i++;
            if (i < size - 1) {
                setTextColor();
                nowword=needstudy.get(i);
                setword(nowword);
                five_five.addLast(nowword);
                if (five_five.size()>=5){
                    System.out.println(five_five.size());
                    //循环五次之后移除的词可算完成，更新今天的数据
                    update(five_five.getFirst());
                    System.out.println(five_five.getFirst());
                    five_five.removeFirst();
                }
                //更新左上角还要背的单词数量信息
                Record record=recordDao.addOrGet();
                today_neednewcount=record.getNeed_FirstNum()-record.getFirstNum();
                today_needreviewcount=record.getNeed_RepeatNum()-record.getRepeatNum();
                if (today_needreviewcount<0){today_needreviewcount=0;}
                today_neednewCount.setText(String.valueOf(today_neednewcount));
                today_needreviewCount.setText(String.valueOf(today_needreviewcount));

                iterator_five=five_five.iterator();//五五循环重新开始
                System.out.println("下一个单词!");
            } else {
                Toast.makeText(this, "已完成今天的任务,总共："+size+"\n"+"正确："+rightCount+"\t"+"错误："+wrongCount, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }



}
