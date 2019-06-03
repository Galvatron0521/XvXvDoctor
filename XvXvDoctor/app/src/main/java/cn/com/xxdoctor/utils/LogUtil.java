package cn.com.xxdoctor.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.xxdoctor.base.DoctorBaseAppliction;

public class LogUtil {
    private static final boolean DEBUG = DoctorBaseAppliction.isTestState;

    private static final int MAX_LENGTH = 3000;

    public static void showLog(String str) {
        str = str.trim();
        int index = 0;
        int maxLength = 4000;
        String finalString;
        while (index < str.length()) {
            if (str.length() <= index + maxLength) {
                finalString = str.substring(index);

            } else {
                finalString = str.substring(index, maxLength);
            }

            index += maxLength;

            d(finalString.trim());
        }
    }

    public static void d(Object object, String msg) {
        if (DEBUG) {
            Log.d(object.getClass().getSimpleName(), msg);
        }
    }

    public static void e(Object object, String msg) {
        if (DEBUG) {
            Log.e(object.getClass().getSimpleName(), msg);
        }
    }

    public static void v(String TAG, String msg) {
        if (DEBUG) {
            Log.v(TAG, msg);
        }
    }

    public static void i(String TAG, String msg) {
        if (DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (DEBUG) {
            Log.v(_FILE_(), getLineMethod() + "-->" + msg);
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            Log.d(_FILE_(), getLineMethod() + "-->" + msg);
        }
    }

    public static void i(String msg) {
        if (DEBUG) {
            Log.i(_FILE_(), getLineMethod() + "-->" + msg);
        }
    }

    public static void d(String TAG, String msg) {
        if (DEBUG) {
            if (msg.length() > MAX_LENGTH) {
                int chunkCount = msg.length() / MAX_LENGTH;
                for (int i = 0; i <= chunkCount; i++) {
                    int max = MAX_LENGTH * (i + 1);
                    if (max >= msg.length()) {
                        Log.d(TAG + "-" + i, msg.substring(MAX_LENGTH * i));
                    } else {
                        Log.d(TAG + "-" + i, msg.substring(MAX_LENGTH * i, max));
                    }
                }
            } else {
                Log.d(TAG, msg);
            }
        }
    }

    public static void e(String msg) {
        if (DEBUG) {
            Log.e(_FILE_(), getLineMethod() + msg);
        }
    }

    public static void e(String TAG, String msg) {
        if (DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static String getFileLineMethod() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        StringBuffer toStringBuffer = new StringBuffer("[")
                .append(traceElement.getFileName()).append(" | ")
                .append(traceElement.getLineNumber()).append(" | ")
                .append(traceElement.getMethodName()).append("]");
        return toStringBuffer.toString();
    }

    public static String getLineMethod() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        StringBuffer toStringBuffer = new StringBuffer("[")
                .append(traceElement.getLineNumber()).append(" | ")
                .append(traceElement.getMethodName()).append("]");
        return toStringBuffer.toString();
    }

    public static String _FILE_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        return traceElement.getFileName();
    }

    public static String _FUNC_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getMethodName();
    }

    public static int _LINE_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getLineNumber();
    }

    public static String _TIME_() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(now);
    }
}
