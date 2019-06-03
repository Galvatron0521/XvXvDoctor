package cn.com.xxdoctor.bean;

import java.util.List;

/**
 * Created by chong.han on 2018/8/23.
 */

public class SuiFangTypeBean {

    public List<FollowVisitListBean> followVisitList;


    public static class FollowVisitListBean {

        public String typeDetailCode;
        public String typeCode;
        public String typeDetailName;

    }
}
