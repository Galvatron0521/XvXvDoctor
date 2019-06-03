package cn.com.xxdoctor.bean;

import java.util.List;

/**
 * Created by chong.han on 2018/8/27.
 */

public class SuiFangAddBingZhangBean {


    public String moduleCode;
    public String fieldRecordID;
    public String scores;
    public List<FieldContentsListBean> fieldContentsList;

    @Override
    public String toString() {
        return "FilterObjBean{" +
                "moduleCode='" + moduleCode + '\'' +
                ", scores='" + scores + '\'' +
                ", fieldContentsList=" + fieldContentsList +
                '}';
    }

    public static class FieldContentsListBean {

        public String fieldID;
        public String contents;

        @Override
        public String toString() {
            return "FieldContentsListBean{" +
                    "fieldID='" + fieldID + '\'' +
                    ", contents='" + contents + '\'' +
                    '}';
        }
    }
}
