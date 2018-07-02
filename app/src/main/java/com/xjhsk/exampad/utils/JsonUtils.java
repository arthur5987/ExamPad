package com.xjhsk.exampad.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.alibaba.fastjson.JSON;
import com.xjhsk.exampad.model.bean.PagerInfo;
import com.xjhsk.exampad.model.bean.PaperAction;
import com.xjhsk.exampad.model.bean.PaperActionOption;
import com.xjhsk.exampad.model.bean.PaperSection;
import com.xjhsk.exampad.model.bean.PaperSectionHeader;
import com.xjhsk.exampad.model.bean.PaperVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：weidingqiang on 2017/11/2 10:43
 * 邮箱：dqwei@iflytek.com
 */

public class JsonUtils {

    public static PaperVO analyzeJson(JSONObject jsondata) throws JSONException {

        PaperVO paperVO = new PaperVO();

        //paperinfo
        PagerInfo pagerInfo = JSON.parseObject(jsondata.getString("pagerInfo"),PagerInfo.class);
        paperVO.setPagerInfo(pagerInfo);

        ArrayList<PaperSection> paperSections = new ArrayList<PaperSection>();

        JSONArray jsonArray = jsondata.getJSONArray("pagerArray");

        for (int i=0;i<jsonArray.length();i++){
            JSONObject jsonsection = jsonArray.getJSONObject(i);

            PaperSection paperSection = new PaperSection();

            //解析header
            PaperSectionHeader paperSectionHeader = JSON.parseObject(jsonsection.getString("paperHeader"),PaperSectionHeader.class);
            paperSection.setPaperSectionHeader(paperSectionHeader);

            ArrayList<PaperAction> paperActions = new ArrayList<PaperAction>();

            JSONArray jsonActions = jsonsection.getJSONArray("actions");
            for (int j=0;j<jsonActions.length();j++){

                JSONObject jsonAction = jsonActions.getJSONObject(j);

                PaperAction paperAction = JSON.parseObject(jsonActions.getString(j),PaperAction.class);

                //包含options要重新处理
                if(jsonAction.has("options")){
                    List<PaperActionOption> paperActionOptions = JSON.parseArray(jsonAction.getString("options"), PaperActionOption.class);
                    paperAction.setPaperActionOptions((ArrayList<PaperActionOption>)paperActionOptions);
                }

                paperActions.add(paperAction);
            }

            paperSection.setPaperActions(paperActions);

            paperSections.add(paperSection);
        }

        paperVO.setPaperSections(paperSections);

        return paperVO;
    }

    //将传入的is一行一行解析读取出来出来
    public static String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is,"utf-8");//GB2312
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();//把读取的数据返回
    }

    public static String readJsonResult(String fileName,Context context) throws IOException{
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        //获取assets资源管理器
        AssetManager assetManager = context.getAssets();
        //通过管理器打开文件并读取
        BufferedReader bf = new BufferedReader(new InputStreamReader(
                assetManager.open(fileName)));
        String line;
        while ((line = bf.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
