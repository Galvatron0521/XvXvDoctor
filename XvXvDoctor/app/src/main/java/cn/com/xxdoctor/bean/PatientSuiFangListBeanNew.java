package cn.com.xxdoctor.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chong.han on 2018/8/23.
 */

public class PatientSuiFangListBeanNew implements Serializable {


    public int pageCount;
    public int totalCount;
    public int pageNo;
    public List<ListBean> list;


    public static class ListBean {

        public String id;
        public int patientID;
        public String followID;
        public String followType;
        public long startTime;
        public String descript;
        public int endFlag;
        public String endTime;
        public String endReason;
        public String followMan;

    }
}
