package com.xjhsk.exampad.widget.login;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import com.weidingqiang.rxfiflibrary2.app.AppConstants;
import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.R;
import com.xjhsk.exampad.api.Constants;
import com.xjhsk.exampad.app.AppContext;
import com.xjhsk.exampad.base.RxBus;
import com.xjhsk.exampad.model.bean.UserData;
import com.xjhsk.exampad.model.event.LoginEvent;
import com.xjhsk.exampad.model.http.api.ExamApis;
import com.xjhsk.exampad.model.http.download.ApiHttpManager;
import com.xjhsk.exampad.model.http.download.DownloadFileInterface;
import com.xjhsk.exampad.picasso.CircleTransform;
import com.xjhsk.exampad.ui.testsound.fragment.RecordFragment;
import com.xjhsk.exampad.utils.CompressUtils;
import com.xjhsk.exampad.utils.GlideCircleTransform;
import com.xjhsk.exampad.utils.Sha1Util;
import com.xjhsk.exampad.utils.XhkCrypto;
import com.zhy.autolayout.AutoLinearLayout;

import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.util.UUID;

/**
 * 作者：weidingqiang on 2018/1/13 10:10
 * 邮箱：dqwei@iflytek.com
 * 考生信息
 */

public class LoginUserInfoView extends AutoLinearLayout implements View.OnClickListener{

    private static final String TAG = LoginUserInfoView.class.getSimpleName();

    //头像
    private ImageView head_img;
    //名字
    private TextView name_tv;
    //性别
    private TextView sex_tv;
    //准考证号
    private TextView code_tv;
    //级别
    private TextView grade_tv;
    //确定
    private ImageView sure_img;
    //取消
    private ImageView cancel_img;

    private String headurl;

    private AutoLinearLayout control_button_layout;

    public LoginUserInfoView(Context context) {
        this(context,null);
    }

    public LoginUserInfoView(Context context, AttributeSet attrs) {
        super(context,attrs);
        initView();
    }

    private void initView(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.login_user_info_view, this, true);

        control_button_layout = (AutoLinearLayout) this.findViewById(R.id.control_button_layout);

        head_img = (ImageView) this.findViewById(R.id.head_img);

        name_tv = (TextView) this.findViewById(R.id.name_tv);
        sex_tv = (TextView) this.findViewById(R.id.sex_tv);
        code_tv = (TextView) this.findViewById(R.id.code_tv);
        grade_tv = (TextView) this.findViewById(R.id.grade_tv);

        sure_img = (ImageView) this.findViewById(R.id.sure_img);
        sure_img.setOnClickListener(this);
        cancel_img = (ImageView) this.findViewById(R.id.cancel_img);
        cancel_img.setOnClickListener(this);

//        headurl = AppConstants.PIC_DOWNLOAD_PATH+ UUID.randomUUID().toString() + ".jpg";
    }

    public void setData(UserData userData){

//        control_button_layout.setVisibility(INVISIBLE);

        name_tv.setText("姓名 : "+userData.getExam_stu_name());

        if(EmptyUtils.isEmpty(userData.getExam_stu_sex())){
            sex_tv.setVisibility(GONE);
        }
        else {
            if(userData.getExam_stu_sex() == 1){
                sex_tv.setText("性别 : 男");
            }else {
                sex_tv.setText("性别 : 女");
            }
            sex_tv.setVisibility(VISIBLE);
        }

        code_tv.setText("考号 : "+userData.getExam_no());

        if(EmptyUtils.isEmpty(userData.getExam_level())){
            grade_tv.setVisibility(GONE);
        }
        else {
            switch (userData.getExam_level())
            {
                case 1:
                    grade_tv.setText("级别 : 一级");
                    break;
                case 2:
                    grade_tv.setText("级别 : 二级");
                    break;
                case 3:
                    grade_tv.setText("级别 : 三级");
                    break;
                case 4:
                    grade_tv.setText("级别 : 四级");
                    break;
            }
            grade_tv.setVisibility(VISIBLE);
        }

        head_img.setImageBitmap(null);
        Picasso.with(getContext())
                .load(getImageUrlForPicasso())
                .transform(new CircleTransform(getContext()))
                .into(head_img);

    }

    public String getImageUrlForPicasso() {
        return "encrypt_"+"http://"+AppContext.getInstance().getHostIp()+":8083/xhk/res/examstu/"
                +AppContext.getInstance().getUserVO().getExam_batch()+"/"+AppContext.getInstance().getUserVO().getExam_no()+".jpg";
    }

    @Override
    public void onClick(View view) {
        //发送事件
        if(view.getId() == R.id.sure_img){
            RxBus.getDefault().post(new LoginEvent(LoginEvent.LOGIN_SURE));
        }
        else {
            RxBus.getDefault().post(new LoginEvent(LoginEvent.LOGIN_CANCEL));
        }
    }
}
