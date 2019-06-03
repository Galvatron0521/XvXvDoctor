package cn.com.xxdoctor.bean;

import java.util.List;

/**
 * Created by chong.han on 2018/8/28.
 */

public class BingZhengListBean {

    public List<ListBean> list;
    public List<DisplayNameList> displayNameList;

    public static class ListBean {

        public String fieldRecordSign;
        public String fieldRecordDate;
        public String recordFlag;
        public List<ImageList> imagelist;
    }

    public static class DisplayNameList {

        public String displayName;

    }

    public static class ImageList {

        public String fileUrl;
        public String patientimageID;

    }
}
