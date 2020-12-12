package com.example.now_word;
/*
   这里存放单词列表的碎片类
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;
import com.example.dao.WordBookDao;
import com.example.dao.WordDao;
import com.example.model.Word;

public class TabFragment extends Fragment {
    private List<String> wordList=new ArrayList<String>();

    private String mTitle;

    //这个构造方法是便于各导航同时调用一个fragment
    public TabFragment(String title){
        mTitle=title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.tab_fragment,container,false);
        RecyclerView recyclerView=view.findViewById(R.id.word_list);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new WordAdapter(getData(),getContext()));

        return view;
    }


//获取不同类型单词数据
    private List<Word> getData() {
        List<Word> data = null;
        WordBookDao wordBookDao=new WordBookDao(getContext());
        WordDao wordDao=new WordDao(getContext());
        switch(mTitle) {
            case "已学单词":
                data=wordBookDao.getLearnedWords();
                break;
            case "标记单词":
                data=wordBookDao.getFlagWords(wordBookDao.getTypeFlagCount());
                break;
            case "易错单词":
                data=wordBookDao.getHighWrongWords(wordBookDao.getTypeHighWrongCount(1),1);
                break;
            case "完成单词":
                data=wordBookDao.getTypeFinishWords(wordBookDao.getTypeFinishCount());
                break;
            case "全部单词":
                data=wordDao.getTypeWords();
                break;

        }

        return data;
    }
}
