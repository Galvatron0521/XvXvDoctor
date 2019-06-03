package cn.com.xxdoctor.bean;

import java.util.List;

/**
 * Created by chong.han on 2018/10/16.
 */

public class SubjectConditionBean {

    public String subjectName;
    public String followPlanID;
    public String startTime;
    public String mobileType;
    public String endTime;
    public String userID;
    public List<SelectListBean> selectList;

    public static class SelectListBean {

        public String condition;
        public String fieldID;

        public SelectListBean(String condition, String fieldID) {
            this.condition = condition;
            this.fieldID = fieldID;
        }

        @Override
        public String toString() {
            return "SelectListBean{" +
                    "condition='" + condition + '\'' +
                    ", fieldID='" + fieldID + '\'' +
                    '}';
        }
    }
}
