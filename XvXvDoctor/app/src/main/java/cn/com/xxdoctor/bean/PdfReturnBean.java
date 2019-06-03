package cn.com.xxdoctor.bean;

/**
 * Created by chong.han on 2018/11/21.
 */

public class PdfReturnBean {

    public String status;
    public String downLoad;

    @Override
    public String toString() {
        return "PdfReturnBean{" +
                "status='" + status + '\'' +
                ", downLoad='" + downLoad + '\'' +
                '}';
    }
}
