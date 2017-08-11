package com.goockr.nakedeyeguard.data.Model;

import java.util.List;

/**
 * Created by CPF on 2017/6/29.
 */

public class UserDetailBean {
    /**
     * name : 110
     * left_times : 15
     * head_image : https://maverickhunteraxel.leanapp.cnhttps://maverickhunteraxel.leanapp.cn/images/no_image.png
     * treatment : {"number":5,"type":"C"}
     * vision : {"before_right":"5.0/1.0","before_left":"5.0/1.0","naked_right":"5.0/1.0","naked_left":"5.0/1.0","before_gls_right":"5.0/1.0","before_gls_left":"5.0/1.0","glasses_right":"5.0/1.0","glasses_left":"5.0/1.0","programme":[1,1,1]}
     */

    private String name;
    private int left_times;
    private String head_image;
    private TreatmentBean treatment;
    private VisionBean vision;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLeft_times() {
        return left_times;
    }

    public void setLeft_times(int left_times) {
        this.left_times = left_times;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public TreatmentBean getTreatment() {
        return treatment;
    }

    public void setTreatment(TreatmentBean treatment) {
        this.treatment = treatment;
    }

    public VisionBean getVision() {
        return vision;
    }

    public void setVision(VisionBean vision) {
        this.vision = vision;
    }

    public static class TreatmentBean {
        /**
         * number : 5
         * type : C
         */

        private int number;
        private String type;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class VisionBean {
        /**
         * before_right : 5.0/1.0
         * before_left : 5.0/1.0
         * naked_right : 5.0/1.0
         * naked_left : 5.0/1.0
         * before_gls_right : 5.0/1.0
         * before_gls_left : 5.0/1.0
         * glasses_right : 5.0/1.0
         * glasses_left : 5.0/1.0
         * programme : [1,1,1]
         */

        private String before_right;
        private String before_left;
        private String naked_right;
        private String naked_left;
        private String before_gls_right;
        private String before_gls_left;
        private String glasses_right;
        private String glasses_left;
        private List<Integer> programme;

        public String getBefore_right() {
            return before_right;
        }

        public void setBefore_right(String before_right) {
            this.before_right = before_right;
        }

        public String getBefore_left() {
            return before_left;
        }

        public void setBefore_left(String before_left) {
            this.before_left = before_left;
        }

        public String getNaked_right() {
            return naked_right;
        }

        public void setNaked_right(String naked_right) {
            this.naked_right = naked_right;
        }

        public String getNaked_left() {
            return naked_left;
        }

        public void setNaked_left(String naked_left) {
            this.naked_left = naked_left;
        }

        public String getBefore_gls_right() {
            return before_gls_right;
        }

        public void setBefore_gls_right(String before_gls_right) {
            this.before_gls_right = before_gls_right;
        }

        public String getBefore_gls_left() {
            return before_gls_left;
        }

        public void setBefore_gls_left(String before_gls_left) {
            this.before_gls_left = before_gls_left;
        }

        public String getGlasses_right() {
            return glasses_right;
        }

        public void setGlasses_right(String glasses_right) {
            this.glasses_right = glasses_right;
        }

        public String getGlasses_left() {
            return glasses_left;
        }

        public void setGlasses_left(String glasses_left) {
            this.glasses_left = glasses_left;
        }

        public List<Integer> getProgramme() {
            return programme;
        }

        public void setProgramme(List<Integer> programme) {
            this.programme = programme;
        }
    }


}
