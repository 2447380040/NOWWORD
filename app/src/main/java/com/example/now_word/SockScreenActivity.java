package com.example.now_word;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SockScreenActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener{
    private TextView word_text,ukphone_text,usphone_text;//用来显示单词和音标的
    private RadioGroup radioGroup;      //用于加载单词的四个选项
    private RadioButton radioOne, radioTwo, radioThree,radioFour;   //  单词意思的四个选项
    private String mMonth, mDay, mWay, mHours, mMinute;  // 用来显示时间
    private TextView dateText,timeText;
    private RadioButton[] radioButtonsgroups=new RadioButton[4];
    private TextView needfinishCountText;//提示框
    private ImageView play_uk_voiceButton,play_us_voiceButton;//播放声音按钮
    private AudioService audioService;//播放音频类
    //数据库操作类
    private WordDao wordDao;
    private WordBookDao wordBookDao;
    private SettingDao settingDao;
    private RecordDao recordDao;
    int needCount;  //要检测解锁的数量
    List<Word> needstudy;//需要学习的所有单词
    Iterator<Word> iterator;
    Word nowword;   //现在正在背的单词
    int size;//还要学习的单词总数量
    int size0;//总共的学习单词次数
    int wrongCount=0;//错误的单词次数
    int rightCount=0;//正确的单词次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sock_screen);
        init();//初始化控件
    }

    /**
     * 初始化控件
     */
    public void init() {
        //初始化按钮
        timeText = (TextView) findViewById(R.id.time_text);            //用于显示分钟绑定id
        dateText = (TextView) findViewById(R.id.date_text);             //用于显示日期绑定id
        needfinishCountText =findViewById(R.id.needfinishCountText);
        word_text=findViewById(R.id.word_text);
        ukphone_text=findViewById(R.id.ukphone_text);
        radioGroup=findViewById(R.id.choose_group);
        radioOne=findViewById(R.id.choose_btn_one);
        radioTwo=findViewById(R.id.choose_btn_two);
        radioThree=findViewById(R.id.choose_btn_three);
        radioFour=findViewById(R.id.choose_btn_four);
        radioButtonsgroups[0]=radioOne;
        radioButtonsgroups[1]=radioTwo;
        radioButtonsgroups[2]=radioThree;
        radioButtonsgroups[3]=radioFour;
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

        needstudy=new ArrayList<>();
        //将需要背的单词加入到学习列表中
        needCount=settingDao.getSockNum();
        needstudy=wordDao.getwrongwords(needCount);
        iterator=needstudy.iterator();
        size0=needstudy.size();//学习单词的总数量
        size=size0;
        getNextData();

    }
    /**
     * 获取系统日期  并设置显示出来
     * */
    private void updateTime(){
    Calendar calendar = Calendar.getInstance();
    mMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);        //获取日期的月
    mDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));      //获取日期的天
    mWay = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));      //获取日期的星期
     //如果小时是个位数 则在前面加“0”
        if (calendar.get(Calendar.HOUR) < 10) {
        mHours = "0" + calendar.get(Calendar.HOUR);
    } else {
        mHours = String.valueOf(calendar.get(Calendar.HOUR));
    }

     //如果分钟是个位数则在前面价格“0”
        if (calendar.get(Calendar.MINUTE) < 10) {
        mMinute = "0" + calendar.get(Calendar.MINUTE);
    } else {
        mMinute = String.valueOf(calendar.get(Calendar.MINUTE));
    }


     //获取星期并设置出来
        if ("1".equals(mWay)) {
        mWay = "天";
    } else if ("2".equals(mWay)) {
        mWay = "一";
    } else if ("3".equals(mWay)) {
        mWay = "二";
    } else if ("4".equals(mWay)) {
        mWay = "三";
    } else if ("5".equals(mWay)) {
        mWay = "四";
    } else if ("6".equals(mWay)) {
        mWay = "五";
    } else if ("7".equals(mWay)) {
        mWay = "六";
    }
        timeText.setText(mHours + ":" + mMinute);
        dateText.setText(mMonth + "月" + mDay + "日" + "    " + "星期" + mWay);
    }


    /**
     * 设置选项
     */
    private void setword(Word word) {
        word_text.setText(word.getHeadWord());
        ukphone_text.setText(word.getUkphone());
        usphone_text.setText(word.getUsphone());

        updateTime();        //更新系统时间

        //随机获得三个其他错误单词
        Random random = new Random();
        List<Word> worrywords = new ArrayList<Word>();
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
        }
    }



    /**
     * 设置选项的不同颜色
     */
    private void btnGetText(String msg, RadioButton btn) {
        /**
         * 答题答对了 设置绿色 答错设置红色
         * */
        if (msg.equals(Tran_CN_split.getspit(nowword.getTranCN()))) {
            btn.setBackgroundColor(getResources().getColor(R.color.TIANYUANLV));        //设置选项为绿色
            rightCount+=1;
            wordBookDao.trueSaveDate(nowword);
        }//保存到数据库
        else{
            btn.setBackgroundColor(getResources().getColor(R.color.YANHONG));            //设置选项为红色
            for (int k = 0; k < 4; k++) {                   //设置正确答案的颜色为绿色
                if (radioButtonsgroups[k].getText().toString().substring(3).equals(Tran_CN_split.getspit(nowword.getTranCN()))) {
                    radioButtonsgroups[k].setBackgroundColor(getResources().getColor(R.color.TIANYUANLV));
                }
            }
            wrongCount+=1;
            wordBookDao.wrongSaveDate(nowword);//保存到数据库

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
        },500);
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
    //更新还需要的背单词数量
    private void update(){
        size=size-1;
        needfinishCountText.setText(String.valueOf(size));
    }


    private void getNextData() {
        if (iterator.hasNext()) {
            setTextColor();
            nowword=iterator.next();
            setword(nowword);
            update();
        } else {
            String msg="总共："+size0+'\t'+"正确："+rightCount+"\t"+"错误:"+wrongCount;
            Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
