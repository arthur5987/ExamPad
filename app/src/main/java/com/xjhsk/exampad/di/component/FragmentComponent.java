package com.xjhsk.exampad.di.component;

import android.app.Activity;

import com.xjhsk.exampad.di.module.FragmentModule;
import com.xjhsk.exampad.di.scope.FragmentScope;
import com.xjhsk.exampad.ui.exam.fragment.EEndFragment;
import com.xjhsk.exampad.ui.exam.fragment.EHintFragment;
import com.xjhsk.exampad.ui.exam.fragment.EJudgePicFragment;
import com.xjhsk.exampad.ui.exam.fragment.ERepeatFragment;
import com.xjhsk.exampad.ui.exam.fragment.ESelectPicFragment;
import com.xjhsk.exampad.ui.exam.fragment.ESelectTxtFragment;
import com.xjhsk.exampad.ui.exam.fragment.ESortFragment;
import com.xjhsk.exampad.ui.exam.fragment.EWelcomeFragment;
import com.xjhsk.exampad.ui.main.fragment.ExamFragment;
import com.xjhsk.exampad.ui.testsound.fragment.RecordFragment;
import com.xjhsk.exampad.ui.testsound.fragment.UserFragment;

import dagger.Component;

/**
 * Created by weidingqiang on 16/8/7.
 */

@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();

    void inject(UserFragment userFragment);

    void inject(RecordFragment recordFragment);

    void inject(ExamFragment examFragment);

    void inject(EWelcomeFragment eWelcomeFragment);

    void inject(EHintFragment eHintFragment);

    void inject(ERepeatFragment eRepeatFragment);


    void inject(ESelectPicFragment eSelectPicFragment);

    void inject(ESelectTxtFragment eSelectTxtFragment);

    void inject(EJudgePicFragment eJudgePicFragment);

    void inject(ESortFragment eSortFragment);

    void inject(EEndFragment eEndFragment);

}
