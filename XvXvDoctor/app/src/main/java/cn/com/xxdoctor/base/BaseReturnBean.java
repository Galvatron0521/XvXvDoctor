package cn.com.xxdoctor.base;

/**
 * Created by liuchen on 2016/10/17.
 */

public class BaseReturnBean {
    public String code = "";
    public String desc = "";
    public String data = "";

    @Override
    public String toString() {
        return "BaseReturnBean{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
