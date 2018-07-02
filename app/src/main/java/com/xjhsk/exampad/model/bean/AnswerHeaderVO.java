package com.xjhsk.exampad.model.bean;

/**
 * 作者：weidingqiang on 2018/1/26 21:32
 * 邮箱：dqwei@iflytek.com
 */

public class AnswerHeaderVO {

    private String exam_no;

    private String paper_no;

    public AnswerHeaderVO(String exam_no,String paper_no){
        this.exam_no = exam_no;
        this.paper_no = paper_no;
    }

    public String getExam_no() {
        return exam_no;
    }

    public void setExam_no(String exam_no) {
        this.exam_no = exam_no;
    }

    public String getPaper_no() {
        return paper_no;
    }

    public void setPaper_no(String paper_no) {
        this.paper_no = paper_no;
    }
}
