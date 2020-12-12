package com.example.now_word;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {
    private RadioGroup bottomTableGroup; //布局文件的单选列表组组件
    private RadioButton main_home;       //单选按钮组件
    private FragmentManager fragmentManager; //管理fragment的类
    private DrawerLayout mDrawerLayout;//管理侧边栏的类



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout=findViewById(R.id.drawer_layout);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu_side);
        }
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.nav_settings:
                        Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_my:
                        Toast.makeText(MainActivity.this,"我的",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_theme:
                        Toast.makeText(MainActivity.this,"主题",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });





        //获取组件对象
        bottomTableGroup=(RadioGroup)findViewById(R.id.main_bottom_tabs);
        main_home=(RadioButton)findViewById(R.id.main_home);

        //初始化FragmentManager类
        fragmentManager=getSupportFragmentManager();

        //设置页面切换的监听
        bottomTableGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.main_home:
                        changeFragment(new HomeFragment(),true);
                        break;
                    case R.id.main_review:
                        changeFragment(new ReviewFragment(),true);
                        break;
                    case R.id.main_word_book:
                        changeFragment(new WordBookFragment(),true);
                        break;
                    default:
                        break;
                }
            }
            //切换不同的fragment
            public void changeFragment(Fragment fragment,boolean isInit){
                //开始事务
                FragmentTransaction transaction=fragmentManager.beginTransaction();
                transaction.replace(R.id.main_content,fragment);
                if(!isInit){transaction.addToBackStack(null);}
                transaction.commit();
            }
        });

        //设置默认选中的主页面
        main_home.setChecked(true);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            //打开侧边栏
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);

                break;
            //跳转到搜索界面
            case R.id.search_main:
                Intent intent=new Intent(this,SearchWordActivity.class);
                startActivity(intent);
                break;
                default:
                    Toast.makeText(MainActivity.this,"不要乱点",Toast.LENGTH_LONG);

        }
        return true;
    }

    @Override//重载顶部栏按钮
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }
}