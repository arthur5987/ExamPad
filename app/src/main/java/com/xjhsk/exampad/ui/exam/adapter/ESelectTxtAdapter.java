package com.xjhsk.exampad.ui.exam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xjhsk.exampad.R;
import com.xjhsk.exampad.base.RxBus;
import com.xjhsk.exampad.model.bean.PaperActionOption;
import com.xjhsk.exampad.model.event.SelectEvent;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;

/**
 * 作者：weidingqiang on 2018/1/19 21:37
 * 邮箱：dqwei@iflytek.com
 */

public class ESelectTxtAdapter extends RecyclerView.Adapter<ESelectTxtAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<PaperActionOption> paperActionOptions;

    public ArrayList<PaperActionOption> getPaperActionOptions() {
        return paperActionOptions;
    }

    public ESelectTxtAdapter(Context context, ArrayList<PaperActionOption> paperActionOptions){
        this.context = context;
        this.paperActionOptions = paperActionOptions;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.e_select_txt_item, null);

        return new ESelectTxtAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        PaperActionOption bean = paperActionOptions.get(position);

        holder.option_tv.setText(bean.getKey()+"."+bean.getContent());

        if(bean.isSelect()){
            holder.option_img.setImageResource(R.drawable.icon_e_checkbox_select);
        }
        else {
            holder.option_img.setImageResource(R.drawable.icon_e_checkbox_normal);
        }

        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxBus.getDefault().post(new SelectEvent(SelectEvent.SELECT_ITEM,position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return paperActionOptions.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView option_tv;

        private ImageView option_img;

        private AutoLinearLayout root_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            option_tv = (TextView) itemView.findViewById(R.id.option_tv);
            option_img = (ImageView) itemView.findViewById(R.id.option_img);
            root_layout = (AutoLinearLayout) itemView.findViewById(R.id.root_layout);
        }
    }
}
