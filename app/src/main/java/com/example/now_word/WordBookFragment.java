package com.example.now_word;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

//单词本页面
public class WordBookFragment extends Fragment {
    private ViewPager pager;
    private FragmentAdapter fragmentAdapter;
    private List<TabFragment> fragmentList;
    private TabLayout tabLayout;
    private List<String> mTitles;
    private String [] title={"易错单词","标记单词","已学单词","完成单词","全部单词"};
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.word_book_fragment,container,false);
        pager=view.findViewById(R.id.page);
        tabLayout=view.findViewById(R.id.tab_layout);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        fragmentList=new ArrayList<>();
        mTitles=new ArrayList<>();
        for(int i=0;i<title.length;i++){
            mTitles.add(title[i]);
            fragmentList.add(new TabFragment(title[i]));
        }

        fragmentAdapter=new FragmentAdapter(getActivity().getSupportFragmentManager(),fragmentList,mTitles);
        pager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(pager);//与ViewPage建立关系
    }
}
