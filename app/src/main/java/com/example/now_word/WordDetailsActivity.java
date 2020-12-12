package com.example.now_word;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dao.WordDao;
import com.example.model.Word;

public class WordDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView headWord,usphone,ukphone,tranCn,tranEn,sentences,phrase,synos;
    public ImageView play_voice_uk,play_voice_us;
    public int wordindex;
    public Word word;
    public WordDao wordDao;
    public AudioService audioService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_details);
        init();
    }
    public void init(){
        wordDao=new WordDao(this);

        headWord=findViewById(R.id.headWord);
        headWord.setOnClickListener(this);
        usphone=findViewById(R.id.usphone_text);
        usphone.setOnClickListener(this);
        play_voice_uk=findViewById(R.id.play_vioce_uk);
        play_voice_uk.setOnClickListener(this);
        ukphone=findViewById(R.id.ukphone_text);
        ukphone.setOnClickListener(this);
        play_voice_us=findViewById(R.id.play_vioce_us);
        play_voice_us.setOnClickListener(this);
        tranCn=findViewById(R.id.tranCN);
        tranCn.setOnClickListener(this);

        tranEn=findViewById(R.id.tranEn);
        tranEn.setOnClickListener(this);
        sentences=findViewById(R.id.sentences);
        sentences.setOnClickListener(this);
        phrase=findViewById(R.id.phrases);
        phrase.setOnClickListener(this);
        synos=findViewById(R.id.synos);
        synos.setOnClickListener(this);
        audioService=new AudioService(this);
        Intent intent=getIntent();
        wordindex= intent.getIntExtra("word",1);
        //System.out.println(wordindex);
        //Toast.makeText(WordDetails.this,String.valueOf(wordindex),Toast.LENGTH_SHORT).show();
        word=wordDao.find(wordindex);
        headWord.setText(word.getHeadWord());
        ukphone.setText(word.getUkphone());
        usphone.setText(word.getUkphone());
        tranCn.setText(word.getTranCN());
        tranEn.setText(word.getTranEN());
        sentences.setText(word.getSentences());
        phrase.setText(word.getPhrases());
        synos.setText(word.getSyno());





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.headWord:
            case R.id.play_vioce_uk:
            case R.id.ukphone_text:
                audioService.updateMP(word.getHeadWord(),0);
                break;
            case R.id.play_vioce_us:
            case R.id.usphone_text:
                audioService.updateMP(word.getHeadWord(),1);
                break;
            case R.id.tranCN:
                audioService.updateMP(word.getTranCN(),2);
                break;
            case R.id.tranEn:
                audioService.updateMP(word.getTranEN(),1);
                break;
            //case R.id.phrases:
                //audioService.updateMP(word.getPhrases(),0);
                //break;
            case R.id.sentences:
                audioService.updateMP(word.getSentences(),1);
                break;
            //case R.id.synos:
                //audioService.updateMP(word.getSyno(),1);
                //break;
        }

    }
}