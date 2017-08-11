package com.goockr.nakedeyeguard.data.Model;

/**
 * Created by JJT-ssd on 2017/3/9.
 * 治疗数据模型
 */

public class TreatmentModel {

    private String treatmentId;//本次治疗的数据号码
    public String getTreatmentId() {return treatmentId;}
    public void setTreatmentId(String treatmentId) {this.treatmentId = treatmentId;}

    private String treatmentUserId;
    public String getTreatmentUserId() {return treatmentUserId;}
    public void setTreatmentUserId(String treatmentUserId) {this.treatmentUserId = treatmentUserId;}

    private String treatmentRemainTime;//固定为1200000,代表治疗时间为20分钟
    public String getTreatmentRemainTime() {return treatmentRemainTime;}
    public void setTreatmentRemainTime(String treatmentRemainTime) {this.treatmentRemainTime = treatmentRemainTime;}

    private String treatmentPauseTime;//暂停时间,此时与开始时间一致
    public String getTreatmentPauseTime() {return treatmentPauseTime;}
    public void setTreatmentPauseTime(String treatmentPauseTime) {this.treatmentPauseTime = treatmentPauseTime;}

//    private String treatmentStartTime;//开始时间
//    public String getTreatmentStartTime() {return treatmentStartTime;}
//    public void setTreatmentStartTime(String treatmentStartTime) {this.treatmentStartTime = treatmentStartTime;}

    private String treatmentState;
    public String getTreatmentState() {return treatmentState;}
    public void setTreatmentState(String treatmentState) {this.treatmentState = treatmentState;}

    private String treatmentTimes;
    public String getTreatmentTimes() {return treatmentTimes;}
    public void setTreatmentTimes(String treatmentTimes) {this.treatmentTimes = treatmentTimes;}

}