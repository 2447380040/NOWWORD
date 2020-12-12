package com.example.now_word;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dao.RecordDao;
import com.example.model.Record;

import org.w3c.dom.Text;

import java.io.IOException;

//首页
public class HomeFragment extends Fragment {
    private RecordDao recordDao;
    private Button startstudyword;

    private TextView needreviewwordText;
    private TextView neednewwordText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.home_fragment,null);
        //获得Ui控件
        startstudyword=(Button)view.findViewById(R.id.startstudyword);

        neednewwordText=(TextView)view.findViewById(R.id.new_text);
        needreviewwordText=(TextView)view.findViewById(R.id.review_mastered);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //更新页面上还需要背和复习的单词数量
        recordDao=new RecordDao(getContext());
        Record record = recordDao.addOrGet();
        int needreviewwordCount=record.getNeed_RepeatNum()-record.getRepeatNum();
        int neednewwordCount=record.getNeed_FirstNum()-record.getFirstNum();
        if (neednewwordCount<0){neednewwordCount=0;}
        neednewwordText.setText(String.valueOf(neednewwordCount));
        needreviewwordText.setText(String.valueOf(needreviewwordCount));

        //设置页面上的按钮监听


        startstudyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "点击按钮",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getView().getContext(), MainStudyActivity.class);
                startActivity(intent);
            }
        });

        super.onActivityCreated(savedInstanceState);
    }
}
