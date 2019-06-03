package cn.com.xxdoctor.bean;

import java.util.List;

/**
 * Created by chong.han on 2018/9/29.
 */

public class ContentBean {

    public List<SelectListBean> selectList;

    public static class SelectListBean {
        /**
         * typeName : 随访基准时间
         * typeDetailCode : 1
         * typeCode : followBaseDate
         * typeDetailName : 首诊日期
         */

        public String typeName;
        public String typeDetailCode;
        public String typeCode;
        public String typeDetailName;
    }
}
