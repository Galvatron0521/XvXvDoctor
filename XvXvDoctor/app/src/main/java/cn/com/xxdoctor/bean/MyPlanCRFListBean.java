package cn.com.xxdoctor.bean;

import java.util.List;

/**
 * Created by chong.han on 2018/10/11.
 */

public class MyPlanCRFListBean {

    public int pageCount;
    public int pageNo;
    public List<CrflistBean> crflist;
    public List<ScaleListBean> scaleList;


    public static class CrflistBean {

        public String moduleName;
        public String moduleCode;
        public boolean isCheck;
        public List<CrfBean> crf;

        public CrflistBean(String moduleName, String moduleCode, boolean isCheck) {
            this.moduleName = moduleName;
            this.moduleCode = moduleCode;
            this.isCheck = isCheck;
        }

        public static class CrfBean {

            public String id;
            public String moduleCode;
            public String displayName;
            public String fieldType;
            public int isNewline;
            public String scores;
            public String suffixName;
            public String fieldName;
            public String fieldControlName;
            public List<ChildrensBean> childrens;

            public static class ChildrensBean {

                public String id;
                public String moduleCode;
                public String displayName;
                public String fieldType;
                public int isNewline;
                public String scores;
                public String suffixName;
                public String fieldName;
                public String fieldControlName;
                public List<?> childrens;
            }
        }
    }
    public static class ScaleListBean {

        public String moduleName;
        public String moduleCode;
        public boolean isCheck;
        public List<ScaleBean> crf;


        public static class ScaleBean {

            public String id;
            public String moduleCode;
            public String displayName;
            public String fieldType;
            public int isNewline;
            public String scores;
            public String suffixName;
            public String fieldName;
            public String fieldControlName;
            public List<ChildrensBean> childrens;

            public static class ChildrensBean {

                public String id;
                public String moduleCode;
                public String displayName;
                public String fieldType;
                public int isNewline;
                public String scores;
                public String suffixName;
                public String fieldName;
                public String fieldControlName;
                public List<?> childrens;
            }
        }
    }
}
