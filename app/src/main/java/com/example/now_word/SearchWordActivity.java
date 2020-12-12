package com.example.now_word;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.WrapperListAdapter;

import com.example.dao.WordBookDao;
import com.example.dao.WordDao;
import com.example.model.Word;

import java.util.Iterator;
import java.util.List;

public class SearchWordActivity extends AppCompatActivity implements TextWatcher {
    private WordDao wordDao;
    private EditText serchInput;
    private List<Word> wordList;
    private RecyclerView recyclerView;
    private String input;
    public WordAdapter wordAdapter;
    public List<Word> wordresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_word);
        init();
    }

    public void init(){
        input="h";
        wordDao=new WordDao(this);
        serchInput=findViewById(R.id.serch_input);
        RecyclerView recyclerView=findViewById(R.id.word_list_search);
        wordresult=getData();
        wordAdapter=new WordAdapter(wordresult,this);
        recyclerView.setAdapter(wordAdapter);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        serchInput.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        input=serchInput.getText().toString().trim();
        System.out.println(input);
        wordresult.clear();

        wordresult.addAll(getData());
        wordAdapter.notifyDataSetChanged();//更新结果数据





    }
    private List<Word> getData() {
        wordList=wordDao.getSerchWords(input);
        System.out.println(wordList.size());
        return wordList;
        }
}