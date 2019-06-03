package cn.com.xxdoctor.bean;

/**
 * Created by chong.han on 2018/8/22.
 */

public class UserInfoBean {


    /**
     * user : {"UserID":1,"HospitalID":1,"LoginName":"admin","Name":"admin","Sex":1,
     * "National":"1","Brithday":"2016-10-11","Mobile":"18854889207","IDCard":"","PhotoID":180,
     * "PhotoUrl":"","Address":""}
     */

    public UserBean user;

    public static class UserBean {

        public String UserID;
        public String HospitalID;
        public String LoginName;
        public String Name;
        public String Sex;
        public String National;
        public String Brithday;
        public String Mobile;
        public String IDCard;
        public String PhotoID;
        public String PhotoUrl;
        public String Address;
        public String FileUrl;
    }
}
