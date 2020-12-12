package com.example.now_word;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dao.SettingDao;
import com.example.model.Setting;
import com.suke.widget.SwitchButton;

//我的页面
public class SettingActivity extends AppCompatActivity {
    private SettingDao settingDao;                          //数据库操作类
    private SwitchButton switchButton;                      //开关按钮
    private Spinner spinnerDifficulty;                      //定义选择难度的下拉框
    private Spinner spinnerSockNum;                          //定义解锁题目的下拉框
    private Spinner spinnerNewNum;                          //定义新题目的下拉框
    public ArrayAdapter<String> adapterDifficulty, adapterSockNum, adapterNewNum;        //定义下拉框的适配器
    public final static String[] difficulty = new String[]{"小学", "初中", "高中", "CET4", "CET6"};                  //选择难度下拉框里面的选项nr
    public final static String[] sockNum = new String[]{"3", "5", "7", "9"};                               //解锁题目下拉框的选项内容
    public final static String[] newNum = new String[]{"10", "20", "30", "50"};                                  //新题目下拉框的选项内容
    public final static String[] difficulty_db = new String[]{"小学", "初中", "高中", "CET4luan_2", "CET6_2"};                  //存入数据库的对应数据
    public final static String[] sockNum_db = new String[]{"3", "5", "7", "9"};                               //存入数据库的对应数据
    public final static String[] newNum_db = new String[]{"10", "20", "30", "50"};                                  //存入数据库的对应数据
    /**
     * 设置下拉框默认选项的方法
     */
    public void setSpinnerItemSelectedByValue(Spinner spinner, String value) {
        SpinnerAdapter apsAdapter = spinner.getAdapter(); //得到SpinnerAdapter对象
        int k = apsAdapter.getCount();
        for (int i = 0; i < k; i++) {
            if (value.equals(apsAdapter.getItem(i).toString())) {
                spinner.setSelection(i, true);// 默认选中项
            }
        }
    }
    //将数据库中获取的转换为下拉框中对应的字符串
    private String getCorrectItem(String value,String[] a,String[] a_db){
        for(int i=0;i<a.length;i++){
            if (value.equals(a_db[i])){
                return a[i];
            }
        }
        return null;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        settingDao=new SettingDao(SettingActivity.this);
        //获得Ui控件
        switchButton= findViewById(R.id.switch_btn);           //开关按钮绑定id
        spinnerDifficulty =findViewById(R.id.spinner_difficulty);       //选择难度下拉框绑定id
        spinnerSockNum = findViewById(R.id.spinner_all_number);        //解锁题目下拉框绑定id
        spinnerNewNum = findViewById(R.id.spinner_new_number);            //新题目下拉框绑定id


        //设置难度监听器
        //NiceSpinner niceSpinner = (NiceSpinner) findViewById(R.id.spinner_difficulty);
        adapterDifficulty = new ArrayAdapter<String>(SettingActivity.this,
                android.R.layout.simple_selectable_list_item, difficulty);              //初始化选择难度下拉框的适配器
        spinnerDifficulty.setAdapter(adapterDifficulty);                            //给选择难度下拉框设置适配器
        String d=getCorrectItem(settingDao.getDifficulty(),difficulty,difficulty_db);

        setSpinnerItemSelectedByValue(spinnerDifficulty, d);      //定义选择难度下拉框的默认选项
        this.spinnerDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {             //设置选择难度的下拉框的监听事件
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String msg = difficulty_db[position];                         //获取到选择的内容
                settingDao.updateDifficulty(msg);                                                            //保存
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        //设置锁屏解锁题目监听
        adapterSockNum = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_selectable_list_item, sockNum);
        spinnerSockNum.setAdapter(adapterSockNum);
        String c=getCorrectItem(String.valueOf(settingDao.getSockNum()),sockNum,sockNum_db);
        setSpinnerItemSelectedByValue(spinnerSockNum, c);
        spinnerSockNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String msg = sockNum_db[position];
                int i = Integer.parseInt(msg);
                settingDao.updateSockNum(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //每天新学单词设置监听器
        adapterNewNum = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_selectable_list_item, newNum);
        spinnerNewNum.setAdapter(adapterNewNum);
        String n=getCorrectItem(String.valueOf(settingDao.getNewNum()),newNum,newNum_db);
        setSpinnerItemSelectedByValue(spinnerNewNum, n);
        spinnerNewNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String msg =newNum_db[position];
                int i= Integer.parseInt(msg);
                settingDao.updateNewNum(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //为是否开启锁屏设置监听器
        if (settingDao.getSock()==1) {
            switchButton.setChecked(true);
        } else {
            switchButton.setChecked(false);
        }
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    settingDao.updateSock(1);
                    Toast.makeText(SettingActivity.this,"锁屏背单词已开启",Toast.LENGTH_SHORT).show();
                }

                else
                    settingDao.updateSock(0);
                    Toast.makeText(SettingActivity.this,"锁屏背单词已关闭",Toast.LENGTH_SHORT).show();
            }

        });

    }
}
