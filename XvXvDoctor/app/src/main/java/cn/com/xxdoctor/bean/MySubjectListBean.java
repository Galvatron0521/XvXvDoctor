package cn.com.xxdoctor.bean;

import java.util.List;

/**
 * Created by chong.han on 2018/10/12.
 */

public class MySubjectListBean {

    /**
     * pageCount : 10
     * totalCount : 3
     * pageNo : 0
     * list : [{"id":16,"subjectName":"课题名称1","followPlanID":"","startTime":1539273600000,
     * "endTime":1539273600000},{"id":17,"subjectName":"课题1","followPlanID":"",
     * "startTime":1539273600000,"endTime":1539273600000},{"id":18,"subjectName":"课题2",
     * "followPlanID":"81","startTime":1539273600000,"endTime":1539273600000}]
     */

    public int pageCount;
    public int totalCount;
    public int pageNo;
    public List<ListBean> list;


    public static class ListBean {
        /**
         * id : 16
         * subjectName : 课题名称1
         * followPlanID :
         * startTime : 1539273600000
         * endTime : 1539273600000
         */

        public String id;
        public String subjectName;
        public String followPlanID;
        public long startTime;
        public long endTime;

    }
}
