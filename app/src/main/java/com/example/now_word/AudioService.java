package com.example.now_word;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

public class AudioService{

    private MediaPlayer mp;
    private String query;
    private Context context;

    AudioService(Context context){
        this.context=context;
        System.out.println("音频初始化");
        }

        //更新音频，换单词,0英音,1美音
    public void updateMP(String word,int type){
        Uri location;
        switch (type){
            case 0:
                location = Uri.parse("http://dict.youdao.com/dictvoice?type=0&audio=" + word);
                break;
            case 1:
                location = Uri.parse("http://dict.youdao.com/dictvoice?type=1&audio=" + word);
                break;
            case 2:
                location = Uri.parse("http://qqlykm.cn/api/txt/apiz.php?text="+word+"&spd=4");
                break;
            default:
                location = Uri.parse("http://dict.youdao.com/dictvoice?type=0&audio=" + word);
                break;


        }

        mp = MediaPlayer.create(context,location);
        System.out.println("更换单词音频");
        mp.start();
    }

    //再播放音频
    public void play(){
    mp.start();
    }


    }

