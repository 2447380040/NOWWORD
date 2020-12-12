package com.example.now_word;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
//复习页面
public class ReviewFragment extends Fragment {
    private Button flagwordstudy;
    private Button dangerwordstudy;
    private Button finishwordstudy;
    private Button dictatewordstudy;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.review_fragment,null);
        //获得Ui控件
        flagwordstudy=view.findViewById(R.id.flagwordstudy);
        dangerwordstudy=view.findViewById(R.id.dangerwordstudy);
        finishwordstudy=view.findViewById(R.id.finishwordstudy);
        dictatewordstudy=view.findViewById(R.id.dictate_study);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        flagwordstudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getView().getContext(), FlagWordStudyActivity.class);
                startActivity(intent);
            }
        });
        dangerwordstudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getView().getContext(), DangerWordStudyActivity.class);
                startActivity(intent);
            }
        });
        finishwordstudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getView().getContext(),FinishWordStudyActivity.class);
                startActivity(intent);
            }
        });
        dictatewordstudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getView().getContext(),DictateWordStudyActivity.class);
                startActivity(intent);
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

}
