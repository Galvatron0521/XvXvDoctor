package cn.com.xxdoctor.bean;

import java.util.List;

/**
 * Created by chong.han on 2018/8/27.
 */

public class AddBingZhangBean {


    public List<FilterObjBean> filterObj;

    @Override
    public String toString() {
        return "AddBingZhangBean{" +
                "filterObj=" + filterObj +
                '}';
    }

    public static class FilterObjBean {

        public String moduleCode;
        public int fieldRecordID;
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
}
