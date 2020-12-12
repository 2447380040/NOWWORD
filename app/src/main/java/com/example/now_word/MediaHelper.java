package com.example.now_word;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;



public class MediaHelper {

    public static MediaPlayer mediaPlayer;
    // 有道英音发音
    public static final String YOU_DAO_VOICE_EN = "https://dict.youdao.com/dictvoice?type=1&audio=";

    // 有道美音发音
    public static final String YOU_DAO_VOICE_USA = "https://dict.youdao.com/dictvoice?type=0&audio=";

    // 英文发音
    public static final int ENGLISH_VOICE = 1;
    // 美国发音
    public static final int AMERICA_VOICE = 0;
    public Context mContext;

    // 默认
    public static final int DEFAULT_VOICE = ENGLISH_VOICE;

    private static final String TAG = "MediaHelper";
    MediaHelper(Context context){
        this.mContext=context;
    }

    public void play(int type, String wordName) {
        if (mediaPlayer != null) {
            releasePlayer();
            mediaPlayer = new MediaPlayer();
        } else
            mediaPlayer = new MediaPlayer();
        try {
            if (ENGLISH_VOICE == type)
                mediaPlayer.setDataSource(YOU_DAO_VOICE_EN + wordName);
            else
                mediaPlayer.setDataSource(YOU_DAO_VOICE_USA + wordName);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(String wordName) {

        play(DEFAULT_VOICE, wordName);
    }

    public  void playInternetSource(String address) {
        if (mediaPlayer != null) {
            releasePlayer();
            mediaPlayer = new MediaPlayer();
        } else
            mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(address);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void releasePlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void playLocalFile(int sourceAddress) {
        if (mediaPlayer != null) {
            releasePlayer();
            mediaPlayer = new MediaPlayer();
        } else
            mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor fileDescriptor = mContext.getResources().openRawResourceFd(sourceAddress);
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playLocalFileRepeat(final int sourceAddress) {
        if (mediaPlayer != null) {
            releasePlayer();
            mediaPlayer = new MediaPlayer();
        } else
            mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor fileDescriptor = mContext.getResources().openRawResourceFd(sourceAddress);
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playLocalFileRepeat(sourceAddress);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
