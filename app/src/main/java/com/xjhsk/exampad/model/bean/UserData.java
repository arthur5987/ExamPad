package com.xjhsk.exampad.model.bean;

/**
 * 作者：weidingqiang on 2017/12/20 14:53
 * 邮箱：dqwei@iflytek.com
 */

public class UserData {


    /**
     * exam_no : 10001
     * exam_stu_name : 爱新觉罗.努尔哈赤
     * exan_stu_sex : 1
     * exam_level : 1
     * exam_pic :
     * exam_batch: 1,  //参加考试的批次
     * paper_name: "xhk_1_001.hsk"
     */

    private String exam_no;
    private String exam_stu_name;
    private int exam_stu_sex;
    private int exam_level;
    private long time_stamp;
    private int exam_batch;
    private String paper_name;


    public String getExam_no() {
        return exam_no;
    }

    public void setExam_no(String exam_no) {
        this.exam_no = exam_no;
    }

    public String getExam_stu_name() {
        return exam_stu_name;
    }

    public void setExam_stu_name(String exam_stu_name) {
        this.exam_stu_name = exam_stu_name;
    }

    public int getExam_stu_sex() {
        return exam_stu_sex;
    }

    public void setExam_stu_sex(int exam_stu_sex) {
        this.exam_stu_sex = exam_stu_sex;
    }

    public int getExam_level() {
        return exam_level;
    }

    public void setExam_level(int exam_level) {
        this.exam_level = exam_level;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    public int getExam_batch() {
        return exam_batch;
    }

    public void setExam_batch(int exam_batch) {
        this.exam_batch = exam_batch;
    }

    public String getPaper_name() {
        return paper_name;
    }

    public void setPaper_name(String paper_name) {
        this.paper_name = paper_name;
    }
}
