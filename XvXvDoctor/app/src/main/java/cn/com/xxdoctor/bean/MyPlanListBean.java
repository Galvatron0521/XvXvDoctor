package cn.com.xxdoctor.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chong.han on 2018/9/29.
 */

public class MyPlanListBean implements Serializable {

    public int pageCount;
    public int totalCount;
    public int pageNo;
    public List<FollowlistBean> followlist;


    public static class FollowlistBean implements Serializable {
        public String id;
        public String baseDate;
        public String planName;
        public List<ProjectlistBean> projectlist;

        /**
         * follow:随访方案;
         * baseDate:取数据字典选中的typeDetailCode值;
         * planName:随访方案名称;
         * taskNum:任务次数;
         * beforeOrAfter:前 后;
         * amount:多少(天,周,月);
         * unit:单位 天,周,月;
         * scope:任务时间范围，可设定+-多少天;
         * optionlist:当前随访方案关联量表CRF的列表;
         * followoptionID:随访方案关联量表CRF表id;
         * taskOption:1 量表 0 CRF;
         * optionModuleCodes:moduleCode
         */
        public static class ProjectlistBean implements Serializable {
            /**
             * project : {"taskNum":1,"beforeOrAfter":"后","amount":3,"unit":"天","scope":"2",
             * "optionlist":[{"taskOption":"0","optionModuleCodes":"SP020141"}]}
             */

            public ProjectBean project;

            public static class ProjectBean implements Serializable{
                /**
                 * taskNum : 1
                 * beforeOrAfter : 后
                 * amount : 3
                 * unit : 天
                 * scope : 2
                 * optionlist : [{"taskOption":"0","optionModuleCodes":"SP020141"}]
                 */

                public String taskNum;
                public String beforeOrAfter;
                public String amount;
                public String unit;
                public String task_name;
                public String task_name2; //量表
                public String followprojectID;
                public String scope;
                public List<OptionlistBean> optionlist;

                public static class OptionlistBean implements Serializable{

                    public String taskOption;
                    public String optionModuleCodes;

                    public OptionlistBean(String taskOption, String optionModuleCodes) {
                        this.taskOption = taskOption;
                        this.optionModuleCodes = optionModuleCodes;
                    }
                }
            }

            public String followprojectID;
            public String taskNum;
            public String beforeOrAfter;
            public String amount;
            public String unit;
            public String scope;
            public String task_name;
            public List<OptionlistBean> optionlist;

            public static class OptionlistBean implements Serializable {
                public String followoptionID;
                public String taskOption;  //0  量化指标  1  量表
                public String optionModuleCodes;

                public OptionlistBean(String taskOption, String optionModuleCodes) {
                    this.taskOption = taskOption;
                    this.optionModuleCodes = optionModuleCodes;
                }
            }
        }
    }
}
