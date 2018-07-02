package com.xjhsk.exampad.model.bean;

/**
 * 作者：weidingqiang on 2018/1/26 19:06
 * 邮箱：dqwei@iflytek.com
 */

public class AnswerVO {

    private String answer;

    private String problemNO;

    public AnswerVO(String answer,String problemNO){
        this.answer = answer;
        this.problemNO = problemNO;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getProblemNO() {
        return problemNO;
    }

    public void setProblemNO(String problemNO) {
        this.problemNO = problemNO;
    }
}
