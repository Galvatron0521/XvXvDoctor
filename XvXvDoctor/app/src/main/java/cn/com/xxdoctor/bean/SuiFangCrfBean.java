package cn.com.xxdoctor.bean;

import java.util.List;

/**
 * Created by chong.han on 2018/11/8.
 */

public class SuiFangCrfBean {

    public String optionTag;
    public List<BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean> fildlist;
    public List<FieldContentsListBean> fieldContentsList;


//    public static class FildlistBean implements Serializable {
//
//        public String id;
//        public String moduleCode;
//        public String displayName;
//        public String fieldType;
//        public String isNewline;
//        public String scores;
//        public String suffixName;
//        public String fieldName;
//        public String fieldControlName;
//        public List<ChildrensBeanXXX> childrens;
//
//        @Override
//        public String toString() {
//            return "FildlistBean{" +
//                    "id='" + id + '\'' +
//                    ", moduleCode='" + moduleCode + '\'' +
//                    ", displayName='" + displayName + '\'' +
//                    ", fieldType='" + fieldType + '\'' +
//                    ", isNewline='" + isNewline + '\'' +
//                    ", scores='" + scores + '\'' +
//                    ", suffixName='" + suffixName + '\'' +
//                    ", fieldName='" + fieldName + '\'' +
//                    ", fieldControlName='" + fieldControlName + '\'' +
//                    ", childrens=" + childrens +
//                    '}';
//        }
//
//        public static class ChildrensBeanXXX implements Serializable {
//
//            public String id;
//            public String moduleCode;
//            public String displayName;
//            public String fieldType;
//            public String isNewline;
//            public String scores;
//            public String suffixName;
//            public String fieldName;
//            public String fieldControlName;
//            public List<ChildrensBeanXX> childrens;
//            public boolean isCheck; // 是否选中
//            public String content; // 录入内容
//
//            @Override
//            public String toString() {
//                return "ChildrensBeanXXX{" +
//                        "id='" + id + '\'' +
//                        ", moduleCode='" + moduleCode + '\'' +
//                        ", displayName='" + displayName + '\'' +
//                        ", fieldType='" + fieldType + '\'' +
//                        ", isNewline='" + isNewline + '\'' +
//                        ", scores='" + scores + '\'' +
//                        ", suffixName='" + suffixName + '\'' +
//                        ", fieldName='" + fieldName + '\'' +
//                        ", fieldControlName='" + fieldControlName + '\'' +
//                        ", childrens=" + childrens +
//                        ", isCheck=" + isCheck +
//                        ", content='" + content + '\'' +
//                        '}';
//            }
//
//            public static class ChildrensBeanXX implements Serializable {
//                //冠心病  三级
//                public String id;
//                public String moduleCode;
//                public String displayName;
//                public String fieldType;
//                public String isNewline;
//                public String scores;
//                public String suffixName;
//                public String fieldName;
//                public String fieldControlName;
//                public List<ChildrensBeanX> childrens;
//                public boolean isCheck; // 是否选中
//                public String content; // 录入内容
//
//                @Override
//                public String toString() {
//                    return "ChildrensBeanXX{" +
//                            "id='" + id + '\'' +
//                            ", moduleCode='" + moduleCode + '\'' +
//                            ", displayName='" + displayName + '\'' +
//                            ", fieldType='" + fieldType + '\'' +
//                            ", isNewline='" + isNewline + '\'' +
//                            ", scores='" + scores + '\'' +
//                            ", suffixName='" + suffixName + '\'' +
//                            ", fieldName='" + fieldName + '\'' +
//                            ", fieldControlName='" + fieldControlName + '\'' +
//                            ", childrens=" + childrens +
//                            ", isCheck=" + isCheck +
//                            ", content='" + content + '\'' +
//                            '}';
//                }
//
//                public static class ChildrensBeanX implements Serializable {
//                    // 亚诊断  四级
//                    public String id;
//                    public String moduleCode;
//                    public String displayName;
//                    public String fieldType;
//                    public String isNewline;
//                    public String scores;
//                    public String suffixName;
//                    public String fieldName;
//                    public String fieldControlName;
//                    public List<ChildrensBean> childrens;
//                    public boolean isCheck; // 是否选中
//                    public String content; // 录入内容
//
//                    @Override
//                    public String toString() {
//                        return "ChildrensBeanX{" +
//                                "id='" + id + '\'' +
//                                ", moduleCode='" + moduleCode + '\'' +
//                                ", displayName='" + displayName + '\'' +
//                                ", fieldType='" + fieldType + '\'' +
//                                ", isNewline='" + isNewline + '\'' +
//                                ", scores='" + scores + '\'' +
//                                ", suffixName='" + suffixName + '\'' +
//                                ", fieldName='" + fieldName + '\'' +
//                                ", fieldControlName='" + fieldControlName + '\'' +
//                                ", childrens=" + childrens +
//                                ", isCheck=" + isCheck +
//                                ", content='" + content + '\'' +
//                                '}';
//                    }
//
//                    public static class ChildrensBean implements Serializable {
//                        // 不稳定性心绞痛 五级
//                        public String id;
//                        public String moduleCode;
//                        public String displayName;
//                        public String fieldType;
//                        public String isNewline;
//                        public String scores;
//                        public String suffixName;
//                        public String fieldName;
//                        public String fieldControlName;
//                        public List<?> childrens;
//
//                        public boolean isCheck; // 是否选中
//                        public String content; // 录入内容
//
//                        @Override
//                        public String toString() {
//                            return "ChildrensBean{" +
//                                    "id='" + id + '\'' +
//                                    ", moduleCode='" + moduleCode + '\'' +
//                                    ", displayName='" + displayName + '\'' +
//                                    ", fieldType='" + fieldType + '\'' +
//                                    ", isNewline='" + isNewline + '\'' +
//                                    ", scores='" + scores + '\'' +
//                                    ", suffixName='" + suffixName + '\'' +
//                                    ", fieldName='" + fieldName + '\'' +
//                                    ", fieldControlName='" + fieldControlName + '\'' +
//                                    ", childrens=" + childrens +
//                                    ", isCheck=" + isCheck +
//                                    ", content='" + content + '\'' +
//                                    '}';
//                        }
//                    }
//                }
//            }
//        }
//    }

    public static class FieldContentsListBean {

        public int fieldRecordID;
        public int patientID;
        public String moduleCode;
        public long crfDate;
        public String scores;
        public String recordFlag;
        public int followRecordId;
        public String fieldID;
        public String contents;
    }
}
