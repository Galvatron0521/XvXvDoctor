package cn.com.xxdoctor.bean;

import java.util.List;

/**
 * Created by chong.han on 2018/10/15.
 * <p>
 * 我的课题当中设置入选人群初始化条件
 */

public class InitSubjectBean {


    public List<ModuleListBean> moduleList;
    public List<PlanListBean> planList;


    public static class ModuleListBean {
        /**
         * moduleName : 西医诊断
         * moduleCode : SP13
         * parentModule : SP0
         * orderNum : 1
         * recordFlag : 1
         * isHaveChild : 1
         */

        public String moduleName;
        public String moduleCode;
        public String parentModule;
        public int orderNum;
        public String recordFlag;
        public String isHaveChild; // 是不是有子集  1 有 0 没有

    }

    public static class PlanListBean {
        /**
         * id : 4028e47b66661e590166662177dd0007
         * planName : CBA季后赛
         */

        public String id;
        public String planName;
    }
}
