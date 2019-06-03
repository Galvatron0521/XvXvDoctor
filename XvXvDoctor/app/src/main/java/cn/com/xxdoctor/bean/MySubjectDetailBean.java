package cn.com.xxdoctor.bean;

import java.util.List;

/**
 * Created by chong.han on 2018/11/9.
 */

public class MySubjectDetailBean {

    public SubjectMapAndroidBean subjectMapAndroid;
    public SubjectMapBean subjectMap;

    public static class SubjectMapAndroidBean {

        public int id;
        public String subjectName;
        public String followPlanID;
        public String planName;
        public long startTime;
        public long endTime;
        public int userID;
        public List<TiaojianBean> tiaojian;

        public static class TiaojianBean {
            public List<ChildBean> child;

            public static class ChildBean {
                /**
                 * id : SP13
                 * condition :
                 * moduleCode : SP13
                 * fieldName :
                 * displayName : 西医诊断
                 */

                public String id;
                public String condition;
                public String moduleCode;
                public String fieldName;
                public String displayName;

            }
        }
    }

    public static class SubjectMapBean {

        public int id;
        public String subjectName;
        public String followPlanID;
        public String planName;
        public long startTime;
        public long endTime;
        public int userID;
        public List<SelectListBean> selectList;

        public static class SelectListBean {

            public String id;
            public ChildBeanXXXX child;
            public String moduleCode;
            public String fieldName;
            public String displayName;


            public static class ChildBeanXXXX {

                public String id;
                public ChildBeanXXX child;
                public String moduleCode;
                public String fieldName;
                public String displayName;

                public static class ChildBeanXXX {

                    public String id;
                    public ChildBeanXX child;
                    public String moduleCode;
                    public String fieldName;
                    public String displayName;

                    public static class ChildBeanXX {

                        public String id;
                        public ChildBeanX child;
                        public String moduleCode;
                        public String fieldName;
                        public String displayName;

                        public static class ChildBeanX {

                            public String id;
                            public String condition;
                            public String moduleCode;
                            public String fieldName;
                            public String displayName;

                        }
                    }
                }
            }
        }
    }
}
