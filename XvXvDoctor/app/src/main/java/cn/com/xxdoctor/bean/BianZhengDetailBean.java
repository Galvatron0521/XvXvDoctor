package cn.com.xxdoctor.bean;

import java.util.List;

/**
 * Created by chong.han on 2018/8/28.
 */

public class BianZhengDetailBean {

    public List<FieldContentsListBean> fieldContentsList;


    public static class FieldContentsListBean {

        public int fieldRecordID;
        public String patientID;
        public String moduleCode;
        public long crfDate;
        public String scores;
        public String recordFlag;
        public String followRecordId;
        public String fieldID;
        public String contents;
    }
}
