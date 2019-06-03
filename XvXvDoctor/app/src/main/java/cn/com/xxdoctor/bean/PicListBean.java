package cn.com.xxdoctor.bean;

import java.util.List;

/**
 * Created by chong.han on 2018/9/6.
 */

public class PicListBean {
    public int pageCount;
    public int totalCount;
    public int pageNo;
    public List<ListBean> list;
    public static class ListBean {
        public String fileUrl;
        public String patientimageID;
    }
}
