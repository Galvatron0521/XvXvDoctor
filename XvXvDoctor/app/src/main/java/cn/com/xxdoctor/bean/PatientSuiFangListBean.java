package cn.com.xxdoctor.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chong.han on 2018/8/23.
 */

public class PatientSuiFangListBean implements Serializable {

    public int pageCount;
    public int totalCount;
    public int pageNo;
    public List<ListBean> list;

    public static class ListBean implements Serializable {

        public String id;
        public String patientID;
        public String followID;
        public String followType;
        public String name;
        public String followName;
        public String followTypeName;
        public String descript;
        public String createUser;
        public long createTime;
        public List<ProjectlistBean> projectlist;
//
        public static class ProjectlistBean implements Serializable {

            public String followprojectID;
            public String taskNum;
            public String beforeOrAfter;
            public int amount;
            public String unit;
            public String scope;
            public List<OptionlistBean> optionlist;

            public static class OptionlistBean implements Serializable {

                public String followoptionID;
                public String taskOption; //0  量化指标  1  量表
                public String optionModuleCodes;
                public String moduleName;

            }
        }
    }
}