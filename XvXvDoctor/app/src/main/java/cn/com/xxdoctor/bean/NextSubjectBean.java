package cn.com.xxdoctor.bean;

import java.util.List;

/**
 * Created by chong.han on 2018/10/15.
 */

public class NextSubjectBean {

    public List<FieldchildlistBean> fieldchildlist;

    public static class FieldchildlistBean {
        /**
         * id : 292
         * moduleCode : SP1801
         * fieldName :
         * fieldControlName : SP18
         * displayName : 中药处方
         * fieldType : 1
         * isHaveChild : 1
         */

        public String id;
        public String moduleCode;
        public String fieldName;
        public String fieldControlName;
        public String displayName;
        public String fieldType;
        public String isHaveChild;
    }
}
