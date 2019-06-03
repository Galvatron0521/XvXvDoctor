package cn.com.xxdoctor.bean;

import java.util.List;

/**
 * Created by chong.han on 2018/9/4.
 */

public class DoctorListBean {


    public int pageCount;
    public int totalCount;
    public int pageNo;
    public List<ListBean> list;

    public static class ListBean {

        public int userID;
        public int HospitalID;
        public String LoginName;
        public String Name;
        public int Sex;
        public String Mobile;
        public String IDCard;
        public String Address;

    }
}
