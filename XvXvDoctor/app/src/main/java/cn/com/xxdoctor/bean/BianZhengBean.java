package cn.com.xxdoctor.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chong.han on 2018/8/24.
 */

public class BianZhengBean implements Serializable {

    public List<ModuleListBean> moduleList;
    public List<RelationListBean> relationList;

    public static class ModuleListBean implements Serializable{

        public ParentModuleBean parentModule; // 量表的三个title
        public List<ChildlistBean> childlist;

        @Override
        public String toString() {
            return "ModuleListBean{" +
                    "parentModule=" + parentModule +
                    ", childlist=" + childlist +
                    '}';
        }

        public static class ParentModuleBean implements Serializable{

            public String moduleName;
            public String moduleCode;
            public String parentModule;
            public String orderNum;

            @Override
            public String toString() {
                return "ParentModuleBean{" +
                        "moduleName='" + moduleName + '\'' +
                        ", moduleCode='" + moduleCode + '\'' +
                        ", parentModule='" + parentModule + '\'' +
                        ", orderNum=" + orderNum +
                        '}';
            }
        }

        public static class ChildlistBean implements Serializable{
            public TagModuleBean tagModule;
            public List<FildlistBean> fildlist;

            public static class TagModuleBean implements Serializable{
                public boolean isSelect;
                public String moduleName;
                public String moduleCode;
                public String parentModule;
                public String orderNum;

                @Override
                public String toString() {
                    return "TagModuleBean{" +
                            "moduleName='" + moduleName + '\'' +
                            ", moduleCode='" + moduleCode + '\'' +
                            ", parentModule='" + parentModule + '\'' +
                            ", orderNum=" + orderNum +
                            '}';
                }
            }

            public static class FildlistBean implements Serializable{

                public String id;
                public String moduleCode;
                public String displayName;
                public String fieldType;
                public String isNewline;
                public String scores;
                public String suffixName;
                public String fieldName;
                public String fieldControlName;
                public List<ChildrensBeanXXX> childrens;

                @Override
                public String toString() {
                    return "FildlistBean{" +
                            "id='" + id + '\'' +
                            ", moduleCode='" + moduleCode + '\'' +
                            ", displayName='" + displayName + '\'' +
                            ", fieldType='" + fieldType + '\'' +
                            ", isNewline='" + isNewline + '\'' +
                            ", scores='" + scores + '\'' +
                            ", suffixName='" + suffixName + '\'' +
                            ", fieldName='" + fieldName + '\'' +
                            ", fieldControlName='" + fieldControlName + '\'' +
                            ", childrens=" + childrens +
                            '}';
                }

                public static class ChildrensBeanXXX implements Serializable{

                    public String id;
                    public String moduleCode;
                    public String displayName;
                    public String fieldType;
                    public String isNewline;
                    public String scores;
                    public String suffixName;
                    public String fieldName;
                    public String fieldControlName;
                    public List<ChildrensBeanXX> childrens;
                    public boolean isCheck; // 是否选中
                    public String content; // 录入内容

                    @Override
                    public String toString() {
                        return "ChildrensBeanXXX{" +
                                "id='" + id + '\'' +
                                ", moduleCode='" + moduleCode + '\'' +
                                ", displayName='" + displayName + '\'' +
                                ", fieldType='" + fieldType + '\'' +
                                ", isNewline='" + isNewline + '\'' +
                                ", scores='" + scores + '\'' +
                                ", suffixName='" + suffixName + '\'' +
                                ", fieldName='" + fieldName + '\'' +
                                ", fieldControlName='" + fieldControlName + '\'' +
                                ", childrens=" + childrens +
                                ", isCheck=" + isCheck +
                                ", content='" + content + '\'' +
                                '}';
                    }

                    public static class ChildrensBeanXX implements Serializable{
                        //冠心病  三级
                        public String id;
                        public String moduleCode;
                        public String displayName;
                        public String fieldType;
                        public String isNewline;
                        public String scores;
                        public String suffixName;
                        public String fieldName;
                        public String fieldControlName;
                        public List<ChildrensBeanX> childrens;
                        public boolean isCheck; // 是否选中
                        public String content; // 录入内容

                        @Override
                        public String toString() {
                            return "ChildrensBeanXX{" +
                                    "id='" + id + '\'' +
                                    ", moduleCode='" + moduleCode + '\'' +
                                    ", displayName='" + displayName + '\'' +
                                    ", fieldType='" + fieldType + '\'' +
                                    ", isNewline='" + isNewline + '\'' +
                                    ", scores='" + scores + '\'' +
                                    ", suffixName='" + suffixName + '\'' +
                                    ", fieldName='" + fieldName + '\'' +
                                    ", fieldControlName='" + fieldControlName + '\'' +
                                    ", childrens=" + childrens +
                                    ", isCheck=" + isCheck +
                                    ", content='" + content + '\'' +
                                    '}';
                        }

                        public static class ChildrensBeanX implements Serializable{
                            // 亚诊断  四级
                            public String id;
                            public String moduleCode;
                            public String displayName;
                            public String fieldType;
                            public String isNewline;
                            public String scores;
                            public String suffixName;
                            public String fieldName;
                            public String fieldControlName;
                            public List<ChildrensBean> childrens;
                            public boolean isCheck; // 是否选中
                            public String content; // 录入内容

                            @Override
                            public String toString() {
                                return "ChildrensBeanX{" +
                                        "id='" + id + '\'' +
                                        ", moduleCode='" + moduleCode + '\'' +
                                        ", displayName='" + displayName + '\'' +
                                        ", fieldType='" + fieldType + '\'' +
                                        ", isNewline='" + isNewline + '\'' +
                                        ", scores='" + scores + '\'' +
                                        ", suffixName='" + suffixName + '\'' +
                                        ", fieldName='" + fieldName + '\'' +
                                        ", fieldControlName='" + fieldControlName + '\'' +
                                        ", childrens=" + childrens +
                                        ", isCheck=" + isCheck +
                                        ", content='" + content + '\'' +
                                        '}';
                            }

                            public static class ChildrensBean implements Serializable{
                                // 不稳定性心绞痛 五级
                                public String id;
                                public String moduleCode;
                                public String displayName;
                                public String fieldType;
                                public String isNewline;
                                public String scores;
                                public String suffixName;
                                public String fieldName;
                                public String fieldControlName;
                                public List<?> childrens;

                                public boolean isCheck; // 是否选中
                                public String content; // 录入内容

                                @Override
                                public String toString() {
                                    return "ChildrensBean{" +
                                            "id='" + id + '\'' +
                                            ", moduleCode='" + moduleCode + '\'' +
                                            ", displayName='" + displayName + '\'' +
                                            ", fieldType='" + fieldType + '\'' +
                                            ", isNewline='" + isNewline + '\'' +
                                            ", scores='" + scores + '\'' +
                                            ", suffixName='" + suffixName + '\'' +
                                            ", fieldName='" + fieldName + '\'' +
                                            ", fieldControlName='" + fieldControlName + '\'' +
                                            ", childrens=" + childrens +
                                            ", isCheck=" + isCheck +
                                            ", content='" + content + '\'' +
                                            '}';
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public String toString() {
                return "ChildlistBean{" +
                        "tagModule=" + tagModule +
                        ", fildlist=" + fildlist +
                        '}';
            }
        }
    }

    public static class RelationListBean implements Serializable{
        public String id;
        public String moduleCode;
        public String fieldID;
    }
}
