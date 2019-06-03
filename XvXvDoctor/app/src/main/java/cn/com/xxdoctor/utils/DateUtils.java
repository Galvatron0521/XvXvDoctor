package cn.com.xxdoctor.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间工具类
 * <p>
 * create time 2013-12-16 version 1.0.0.0 class DateUtils.java
 *
 * @author mawen
 */
public class DateUtils {
    /**
     * 格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
    /**
     * 格式：yyyyMMddHHmmss
     */
    public static final String FORMAT_2 = "yyyyMMddHHmmss";

    /**
     * 格式：yyyy-MM-dd HH:mm
     */
    public static final String FORMAT_3 = "yyyy-MM-dd HH:mm";

    /**
     * 格式：yyyyMMdd
     */
    public static final String FORMAT_4 = "yyyy/MM/dd";

    /**
     * 格式：yyyy-MM-dd
     */
    public static final String FORMAT_5 = "yyyy-MM-dd";

    /**
     * 格式：HH:mm:ss
     */
    public static final String FORMAT_6 = "HH:mm:ss";

    /**
     * 格式：HH:mm
     */
    public static final String FORMAT_7 = "HH:mm";

    /**
     * 格式：MM-dd
     */
    public static final String FORMAT_8 = "MM-dd";

    /**
     * 格式：MM-dd HH:mm
     */
    public static final String FORMAT_9 = "MM-dd HH:mm";

    /**
     * 格式 MM月dd日
     */
    public static final String FORMAT_10 = "MM月dd日";

    /**
     * 格式 MM月dd日
     */
    public static final String FORMAT_11 = "yyyy/MM/dd HH:mm";


    public static final String FORMAT_12 = "MM/dd";

    public static final String FORMAT_13 = "yyyy/MM/dd";

    public static final String FORMAT_14 = "HHmmss";

    public static final String FORMAT_15 = "yyyyMMdd";
    public static final String FORMAT_17 = "yyyyMMddHHmmss";
    public static final String FORMAT_16 = "MM/dd HH:mm";

    public static final String WORK_SIGN = "yyyy年MM月dd日 HH:mm:ss EEEE";

    /**
     * timeMillis转换为指定格式的时间
     *
     * @param timeMillis
     * @param format     日期格式 为空指定则采用默认格式yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String timeMillisToDateTimeString(long timeMillis,
                                                    String format) {
        format = (format == null || format.length() <= 0) ? FORMAT_1 : format;
        return new SimpleDateFormat(format).format(new Date(timeMillis));
    }

    /**
     * date类型转换为String类型
     *
     * @param data
     * @param format 日期格式 为空指定则采用默认格式yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String dateToString(Date data, String format) {
        format = (format == null || format.length() <= 0) ? FORMAT_1 : format;
        return new SimpleDateFormat(format).format(data);
    }

    /**
     * string类型转换为date类型
     *
     * @param datetimeString
     * @param format         日期格式 为空指定则采用默认格式yyyy-MM-dd HH:mm:ss
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String datetimeString, String format)
            throws ParseException {
        format = (format == null || format.length() <= 0) ? FORMAT_1 : format;
        return new SimpleDateFormat(format).parse(datetimeString);
    }

    public static Date parseStringToDate(String strDate, String pattern) {
        if (TextUtils.isEmpty(strDate)) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getHostTime() {
        // long DBMSGSAVETIME = 1000 * 60 * 60 * 24 * 15;
        // String format = "yyyyMMddHHmmss";
        // SimpleDateFormat sdf = new SimpleDateFormat(format);
        // try {
        // return sdf.format(new Date(System.currentTimeMillis()
        // - DBMSGSAVETIME));
        // } catch (Exception e) {
        // return "";
        // }
        return dateToString(addDate(new Date(), -15), FORMAT_2);
    }

    /**
     * 日期字符串由指定格式转换成目标格式
     *
     * @param datetimeString
     * @param srcFormat      日期格式 为空指定则采用默认格式yyyy-MM-dd HH:mm:ss
     * @param destFormat     日期格式 为空指定则采用默认格式yyyy-MM-dd HH:mm:ss
     * @return
     * @throws ParseException
     */
    public static String datetimeStringToDatetimeString(String srcFormat,
                                                        String datetimeString, String destFormat) throws

            ParseException {
        return dateToString(stringToDate(datetimeString, srcFormat), destFormat);
    }

    /**
     * 获取当前时间
     *
     * @param format 为空，则采用默认格式：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurrentDatetimeString(String format) {
        format = (format == null || format.trim().length() <= 0) ? "yyyy-MM-dd HH:mm:ss"
                : format;
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * 消息列表的显示时间描述
     * 当日信息      15:21
     * 当日零点前24小时内     昨天
     * 当日零点前24小时外     8/21
     * 跨年信息     15/8/6
     */
    public static String getMessageList(String datetimeString, String format) {

        long day = 1000 * 60 * 60 * 24;
        long hour = 1000 * 60 * 60;
        long minutes = 1000 * 60;
        long current = System.currentTimeMillis();//当前时间毫秒数
        long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        //今天零点零分零秒的毫秒数
        long twelve = zero + 24 * 60 * 60 * 1000 - 1;//今天23点59分59秒的毫秒数
        long yesterday = System.currentTimeMillis() - 24 * 60 * 60 * 1000;//昨天的这一时间的毫秒数
        long yearTime = getCurrYearFirst().getTime();

        try {
            Date date = stringToDate(datetimeString, format);
            long targetTime = date.getTime();

            if (targetTime > zero) {
                //当天时间
                return dateToString(date, FORMAT_7);
            } else if (targetTime < zero && targetTime > zero - day) {
                //昨天时间
                return "昨天";
            } else if (targetTime > yearTime) {
                //当年时间
                return dateToString(date, FORMAT_12);
            } else {
                return dateToString(date, FORMAT_13);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datetimeString;
    }

    public static String getMessageList(long timeMillis, String format) {

        long day = 1000 * 60 * 60 * 24;
        long hour = 1000 * 60 * 60;
        long minutes = 1000 * 60;
        long current = System.currentTimeMillis();//当前时间毫秒数
        long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        //今天零点零分零秒的毫秒数
        long twelve = zero + 24 * 60 * 60 * 1000 - 1;//今天23点59分59秒的毫秒数
        long yesterday = System.currentTimeMillis() - 24 * 60 * 60 * 1000;//昨天的这一时间的毫秒数
        long yearTime = getCurrYearFirst().getTime();

        Date date = new Date(timeMillis);
        long targetTime = date.getTime();

        if (targetTime > zero) {
            //当天时间
            return dateToString(date, FORMAT_7);
        } else if (targetTime < zero && targetTime > zero - day) {
            //昨天时间
            return "昨天";
        } else if (targetTime > yearTime) {
            //当年时间
            return dateToString(date, FORMAT_12);
        } else {
            return dateToString(date, FORMAT_13);
        }
    }

    public static String getQacQuestionList(String datetimeString, String format) {

        long day = 1000 * 60 * 60 * 24;
        long hour = 1000 * 60 * 60;
        long minutes = 1000 * 60;
        long current = System.currentTimeMillis();//当前时间毫秒数
        long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        //今天零点零分零秒的毫秒数
        long twelve = zero + 24 * 60 * 60 * 1000 - 1;//今天23点59分59秒的毫秒数
        long yesterday = System.currentTimeMillis() - 24 * 60 * 60 * 1000;//昨天的这一时间的毫秒数
        long yearTime = getCurrYearFirst().getTime();

        try {
            Date date = stringToDate(datetimeString, format);
            long targetTime = date.getTime();

            if (targetTime > zero) {
                //当天时间
                return dateToString(date, FORMAT_7);
            } else if (targetTime < zero && targetTime > zero - day) {
                //昨天时间
                return "昨天 " + dateToString(date, FORMAT_7);
            } else if (targetTime > yearTime) {
                //当年时间
                return dateToString(date, FORMAT_12) + " " + dateToString(date, FORMAT_7);
            } else {
                String year = dateToString(date, FORMAT_13);
                return year.substring(2, year.length()) + " " + dateToString(date, FORMAT_7);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datetimeString;
    }


    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取当年的第一天
     *
     * @return
     */
    public static Date getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取指定时间与当前时间差的描述
     *
     * @param datetimeString
     * @param format
     * @return
     */
    public static String getDatetimeDesc(String datetimeString, String format) {
        long day = 1000 * 60 * 60 * 24;
        long hour = 1000 * 60 * 60;
        long minutes = 1000 * 60;
        try {
            Date date = stringToDate(datetimeString, format);
            long times = new Date().getTime() - date.getTime();
            if (times / day > 0) {
                if ((times / day) >= 14) {
                    return "2周前";
                } else {
                    return (times / day) + "天前";
                }
            } else if (times / hour > 0) {
                return (times / hour) + "小时前";
            } else {
                return (times / minutes) + "分钟前";
            }
        } catch (ParseException e) {
            return datetimeString;
        }
    }

    /**
     * 获取指定天数后的日期
     *
     * @param date
     * @param days 负数为指定日期前，正为指定天数后
     * @return
     */
    public static Date addDate(Date date, int days) {
        if (date == null)
            date = new Date();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.DAY_OF_YEAR, days);
        return rightNow.getTime();
    }

    public static String StringToString(String destFormat, String time) {
        destFormat = (destFormat == null ? "yyyy-MM-dd HH:mm:ss" : destFormat);
        try {
            return dateToString(stringToDate(time, "yyyyMMddHHmmss"),
                    destFormat);
        } catch (Exception e) {
            SimpleDateFormat sdf = new SimpleDateFormat(destFormat);
            return sdf.format(new Date(System.currentTimeMillis()));
        }
    }

    /**
     * 获取指定时间与当前时间差的描述
     *
     * @param datetimeString
     * @return
     */
    public static String getChatDatetimeDesc(String datetimeString) {
        if (datetimeString == null) {
            return "";
        }
        try {
            Calendar curCal = Calendar.getInstance();

            Date date = stringToDate(datetimeString, FORMAT_1);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            if (curCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
                if (curCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
                        && curCal.get(Calendar.DAY_OF_MONTH) == cal
                        .get(Calendar.DAY_OF_MONTH)) {// 今天
                    return dateToString(stringToDate(datetimeString, FORMAT_1),
                            FORMAT_7);
                }

                curCal.add(Calendar.DATE, -1);// 昨天
                if (curCal.get(Calendar.DAY_OF_YEAR) == cal
                        .get(Calendar.DAY_OF_YEAR)) {
                    return "昨天 "
                            + dateToString(
                            stringToDate(datetimeString, FORMAT_1),
                            FORMAT_7);
                }

                return dateToString(stringToDate(datetimeString, FORMAT_1),
                        FORMAT_9);
            } else {
                return dateToString(stringToDate(datetimeString, FORMAT_1),
                        FORMAT_1);
            }
        } catch (ParseException e) {
            return "刚刚";
        }
    }

    /**
     * 计算两个日期差（毫秒）
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return 相差毫秒数
     */
    public static long diffTwoDate(Date date1, Date date2) {
        long l1 = date1.getTime();
        long l2 = date2.getTime();
        return (l1 - l2);
    }

    /**
     * 计算两个日期差（天）
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return 相差天数
     */
    public static int diffTwoDateDay(Date date1, Date date2) {
        long l1 = date1.getTime();
        long l2 = date2.getTime();
        int diff = Integer.parseInt("" + (l1 - l2) / 3600 / 24 / 1000);
        return diff;
    }

    public static String getNowDateTime() {
        return new SimpleDateFormat(FORMAT_1).format(new Date(System.currentTimeMillis()));
    }

    public static String getNowDate() {
        return new SimpleDateFormat(FORMAT_15).format(new Date(System.currentTimeMillis()));
    }
    public static String getNowSSDate() {
        return new SimpleDateFormat(FORMAT_17).format(new Date(System.currentTimeMillis()));
    }
}
