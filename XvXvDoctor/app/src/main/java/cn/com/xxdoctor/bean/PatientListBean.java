package cn.com.xxdoctor.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chong.han on 2018/8/22.
 */

public class PatientListBean implements Serializable {


    public int pageCount;
    public int totalCount;
    public int pageNo;
    public List<ListBean> list;


    public static class ListBean implements Serializable {


        public String id;
        public String num;
        public String name;
        public String age;
        public String brithday;
        public String sex;
        public String mobile;
        public String address;
        public String idCard;
        public String recordFlag;
        public long createTime;
        public List<?> lablelist;

    }
}
