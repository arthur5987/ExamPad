package com.xjhsk.exampad.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xjhsk.exampad.R;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * 作者：weidingqiang on 2018/1/21 18:07
 * 邮箱：dqwei@iflytek.com
 */

public class ETextView extends AutoLinearLayout {

    private TextView textview;

    public ETextView(Context context) {
        this(context,null);
    }

    public ETextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView();
    }

    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.e_text_view_layout, this, true);

        textview = (TextView) this.findViewById(R.id.textview);
    }

    public void setData(String contain){
        textview.setText(contain);
    }
}
