package cn.com.xxdoctor.bean;

import java.util.List;

/**
 * Created by chong.han on 2018/8/23.
 */

public class SuiFangFangAnBean {


    public int pageCount;
    public int totalCount;
    public int pageNo;
    public List<ListBean> list;


    public static class ListBean {

        public String id;
        public String moduleCode;
        public String followName;
        public String followDescript;
    }
}
