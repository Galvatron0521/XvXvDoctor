package cn.com.xxdoctor.bean;

import java.io.Serializable;
import java.util.List;


public class MyLiangBiaoInfoListBean implements Serializable {

    public List<ListBean> list;


    public static class ListBean implements Serializable {

        public ParentModuleBean parentModule;
        public List<FildlistBean> fildlist;


        public static class ParentModuleBean implements Serializable {

            public int moduleID;
            public String moduleCode;
            public String moduleName;
            public String parentModule;
            public int orderNum;
            public int isShared;
            public String createUser;

        }

        public static class FildlistBean implements Serializable {

            public String id;
            public String moduleCode;
            public String displayName;
            public String fieldType;
            public int isNewline;
            public String scores;
            public String suffixName;
            public String fieldName;
            public String fieldControlName;
            public List<ChildrensBeanX> childrens;


            public static class ChildrensBeanX implements Serializable {

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

                public static class ChildrensBean implements Serializable {

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
}
