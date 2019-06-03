package cn.com.xxdoctor.bean;

/**
 * Created by chong.han on 2018/8/23.
 */

public class FangAnInfoBean {

    public String optionTag;
    public FollowBean follow;

    public static class FollowBean {

        public String id;
        public String moduleCode;
        public String followName;
        public String followDescript;
        public String followMaxNum;
        public String followMaxYear;
        public String followMaxMonth;
        public String followMaxDay;
        public String followDayNum;
        public String createUser;
        public long createTime;
        public String updateUser;
        public String updateTime;
        public String delFlag;
        public String remark;
    }
}
