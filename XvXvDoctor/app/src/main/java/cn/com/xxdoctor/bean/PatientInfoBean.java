package cn.com.xxdoctor.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chong.han on 2018/8/22.
 */

public class PatientInfoBean implements Serializable{


    /**
     * patientDetails : {"num":"PDJH180119","name":"丁建红","sex":"女","age":46,"national":"",
     * "brithday":"","mobile":"12164385700","idCard":"","address":"","lablelist":[]}
     */

    public PatientDetailsBean patientDetails;


    public static class PatientDetailsBean  implements Serializable{
        public String num;
        public String name;
        public String sex;
        public String age;
        public String national;
        public String brithday;
        public String mobile;
        public String idCard;
        public String mobile2;
        public String address;
        public List<TagBean> lablelist;
    }
}
