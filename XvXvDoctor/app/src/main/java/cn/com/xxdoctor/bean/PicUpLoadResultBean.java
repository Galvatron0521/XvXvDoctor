package cn.com.xxdoctor.bean;

import java.util.List;

/**
 * Created by chong.han on 2018/9/6.
 */

public class PicUpLoadResultBean {
    public List<ImgListBean> imgList;


    public static class ImgListBean {

        public String fieldRecordSign;
        public int fileId;
        public int fileType;
        public String fileName;
        public String patientimageID;
        public String fileUrl;

    }

    public String fieldRecordSign;
    public int fileId;
    public int fileType;
    public String uploadMsg;
    public String uploadStatus;
    public String fileName;
    public String patientimageID;
    public String fileUrl;
    public String contents;

}
