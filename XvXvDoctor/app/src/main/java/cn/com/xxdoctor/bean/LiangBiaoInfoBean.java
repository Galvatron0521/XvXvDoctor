package cn.com.xxdoctor.bean;

import java.io.Serializable;
import java.util.List;


public class LiangBiaoInfoBean implements Serializable {

    public List<ModuleListBean> moduleList;


    public static class ModuleListBean implements Serializable {

        public ParentModuleBean parentModule;
        public List<FildlistBean> fildlist;


        public static class ParentModuleBean implements Serializable {

            public String moduleName;
            public String moduleCode;
            public String parentModule;
            public int orderNum;

        }

        public static class FildlistBean implements Serializable {
            //一级
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
            public boolean isCheck; // 是否选中
            public String content; // 录入内容

            public static class ChildrensBeanX implements Serializable {
                //二级
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
                public boolean isCheck; // 是否选中
                public String content; // 录入内容

                public static class ChildrensBean implements Serializable {
                    // 三级
                    public boolean isCheck; // 是否选中
                    public String content; // 录入内容
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
