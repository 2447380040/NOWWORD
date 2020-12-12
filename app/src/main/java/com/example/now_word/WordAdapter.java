package com.example.now_word;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.Word;

import java.util.List;

import static android.app.PendingIntent.getActivity;
/*
单词列表的适配器
 */


public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder>{
    private List<Word> mData;
    public Context mcon;

    public WordAdapter(List<Word> data, Context con) {
        this.mData = data;
        mcon=con;
    }

    public void updateData(List<Word> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item, parent, false);
        // 实例化viewholder
        final ViewHolder viewHolder = new ViewHolder(v);
//        viewHolder.mheadWord.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                int position=viewHolder.getAdapterPosition();
//                Word word=mData.get(position);
//                //Toast.makeText(v.getContext(),"你点击了"+word.get_id(),Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(v.getContext(),WordDetails.class);
//                intent.putExtra("word",word.get_id());
//                mcon.startActivity(intent);
//            }
//        });
//        viewHolder.mtranCn.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                int position=viewHolder.getAdapterPosition();
//                System.out.println(position);
//                Word word=mData.get(position);
//                //Toast.makeText(v.getContext(),"你点击了"+word.getTranCN().toString(),Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(v.getContext(),WordDetails.class);
//                intent.putExtra("word",word.get_id());
//                mcon.startActivity(intent);
//            }
//        });
        viewHolder.cardView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int position=viewHolder.getAdapterPosition();
                System.out.println(position);
                Word word=mData.get(position);
                //Toast.makeText(v.getContext(),"你点击了"+word.getTranCN().toString(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), WordDetailsActivity.class);
                intent.putExtra("word",word.get_id());
                mcon.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 绑定数据
        holder.mheadWord.setText(mData.get(position).getHeadWord());
        holder.mtranCn.setText(mData.get(position).getTranCN());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mheadWord,mtranCn;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            mheadWord= (TextView) itemView.findViewById(R.id.word_list_item_headWord);
            mtranCn=(TextView)itemView.findViewById(R.id.word_list_item_tranCn);
            cardView=(CardView)itemView.findViewById(R.id.wordcard);
        }
    }
}
