package cn.com.xxdoctor.utils;

import android.text.TextUtils;

import java.io.File;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * <p>
 * create time 2013-12-16
 * <p>
 * version 1.0.0.0 class
 * <p>
 * StringUtils.java
 *
 * @author mawen
 */
public class StringUtils {
    /**
     * 填充字符串
     *
     * @param str
     * @param n
     * @param isLeft true左填充,false 右填充
     * @param c      填充字符串
     * @return
     */
    public static String fill(String str, int n, boolean isLeft, String c) {
        c = (c == null) ? "0" : c;
        if (StringUtils.isEmpty(str))
            str = "";
        if (str.length() >= n)
            str = str.substring(0, n);

        String s = "";
        for (int i = str.length(); i < n; i++)
            s += c;
        if (isLeft)
            return s + str;
        else
            return str + s;
    }

    /**
     * 判断是字符串否为空，判断规则，字符串为null或者trim后无值
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() <= 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 手机号码去杂
     *
     * @param phoneNum
     * @return
     */
    public static String filterPhoneNum(String phoneNum) {
        if (isEmpty(phoneNum)) {
            return "";
        }
        String tempPhoneNum = phoneNum;
        if (tempPhoneNum.startsWith("+86")) {
            tempPhoneNum = tempPhoneNum.substring(3);
        } else if (tempPhoneNum.startsWith("86")) {
            tempPhoneNum = tempPhoneNum.substring(2);
        }
        // 过滤非数字的任何东西
        int size = tempPhoneNum.length();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {
            if (!(TextUtils.isEmpty(String.valueOf(tempPhoneNum.charAt(i))
                    .trim()) || !Character.isDigit(tempPhoneNum.charAt(i)))) {
                sb.append(tempPhoneNum.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * 获取jid中的name
     *
     * @param @param  XMPPAddress
     * @param @return
     * @return
     * @throws
     * @Description: TODO
     */
    public static String parseName(String XMPPAddress) {
        if (XMPPAddress == null) {
            return null;
        }
        int atIndex = XMPPAddress.lastIndexOf("@");
        if (atIndex <= 0) {
            return XMPPAddress;
        }
        return XMPPAddress.substring(0, atIndex);
    }

    /**
     * 获取jid中的server
     *
     * @param @param  XMPPAddress
     * @param @return
     * @return
     * @throws
     * @Description: TODO
     */
    public static String parseServer(String XMPPAddress) {
        if (XMPPAddress == null) {
            return null;
        }
        int atIndex = XMPPAddress.lastIndexOf("@");

        if (atIndex + 1 > XMPPAddress.length()) {
            return "";
        }
        int slashIndex = XMPPAddress.indexOf("/");
        if ((slashIndex > 0) && (slashIndex > atIndex)) {
            return XMPPAddress.substring(atIndex + 1, slashIndex);
        }

        return XMPPAddress.substring(atIndex + 1);
    }

    /**
     * Returns the XMPP address with any resource information removed. For
     * example, for the address "matt@jivesoftware.com/Smack",
     * "matt@jivesoftware.com" would be returned.
     *
     * @param XMPPAddress the XMPP address.
     * @return the bare XMPP address without resource information.
     */
    public static String parseBareAddress(String XMPPAddress) {
        if (XMPPAddress == null) {
            return null;
        }
        int slashIndex = XMPPAddress.indexOf("/");
        if (slashIndex < 0) {
            return XMPPAddress;
        } else if (slashIndex == 0) {
            return "";
        } else {
            return XMPPAddress.substring(0, slashIndex);
        }
    }

    public static boolean hasText(String text) {
        return text != null && text.length() > 0;
    }

    // 测试IP地址是否合法
    public static boolean isIp(String ipAddress) {
        Pattern pattern = Pattern
                .compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)" +
                        "\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.(" +
						"(?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    public static int isPort(String port) {
        Pattern pattern = Pattern.compile("[0-9]*");
        if (pattern.matcher(port).matches()) {
            return Integer.parseInt(port);
        } else {
            return -1;
        }
    }

    // 使用本地默认格式输出货币值
    public static String decimalFormat(String value) {
        try {

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
            return currencyFormat.format(Float.parseFloat(value));
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    // 使用本地默认格式输出货币值不包括￥
    public static String decimalFormatString(String value) {
        try {

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
            String values = currencyFormat.format(Float.parseFloat(value));
            return values.substring(1, values.length());
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    public static boolean isNull(String str) {
        if (!StringUtils.hasText(str)) {
            return true;
        }
        if (str.equalsIgnoreCase("null")) {
            return true;
        }
        return false;
    }

    /**
     * 验证银行卡号码
     *
     * @param id_number
     * @return
     */
    public static Boolean checkBankCard(String id_number) {
        String check = "([0-9]{16})";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(id_number);
        boolean isMatched = matcher.matches();

        return isMatched;
    }

    /**
     * 验证身份证号码
     *
     * @param id_number
     * @return
     */
    public static Boolean checkNID(String id_number) {
        String check = "([0-9]{17}([0-9]|X))|([0-9]{15})";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(id_number);
        boolean isMatched = matcher.matches();

        return isMatched;
    }

    /**
     * 验证邮箱地址是否正确
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    /**
     * 验证手机号码
     *
     * @param mobiles
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isNum(String number) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("[0-9]");
            Matcher m = p.matcher(number);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static String getBucketPath(String fullPath, String fileName) {
        File f = new File(fullPath);
        System.out.print(f.getParent());
        int index = fullPath.lastIndexOf(fileName);
        if (index >= 0) {
            return fullPath.substring(0, index);
        } else {
            return fullPath.substring(0, fullPath.lastIndexOf("/"));
        }

    }

    public static String getUri(String path) {
        if (TextUtils.isEmpty(path)) {
            return "file://";
        }
        if (path.startsWith("http") || path.startsWith("https"))
            return path;
        if (path.indexOf("file") >= 0) {
            return path;
        }
        return "file://" + path;
    }

    public static String HighLight(String str, String target, String... color) {
        if (str.indexOf(target) < 0)
            return str;
        if (null == target || target.equals(""))
            return str;
        // String defaultColor = "#49c6d8";//蓝色
        String defaultColor = "#f25824";// 橙色
        if (color.length > 0)
            defaultColor = color[0];
        String temp = "<font color='" + defaultColor + "'>" + target
                + "</font>";
        return str.replace(target, temp);
    }

    /**
     * 字符串是否为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(CharSequence s) {
        return s != null && s.toString().matches("\\s*");
    }

    /**
     * 判断集合是否为空
     *
     * @param coll
     * @return
     */
    public static boolean isEmpty(Collection<? extends Object> coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * 从扫描的二维码中截取出token
     *
     * @param url
     * @return
     */
    public static String getTokenFromUrl(String url) {
        String token = "";
        if (url == null) {
            return token;
        }

        int eidx = url.lastIndexOf('=');
        if (eidx > 0) {
            token = url.substring(eidx + 1);
        }

        return token;
    }

}
