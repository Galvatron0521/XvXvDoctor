package cn.com.xxdoctor.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Administrator on 2015/11/25.
 */
public class HcUtils {

    public static String TYPE = "type";
    public static String VALUE = "value";
    public static String VALUE2 = "value2";

    public static String USER_SIGN_PATH = Environment.getExternalStorageDirectory() + File
            .separator + "nqkj" + File
            .separator + "userSign" + File.separator;

    /**
     * 获取网络状态
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static boolean isMoblieNetwork(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        }
        return false;
    }

    /**
     * 隐藏输入框
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            View currentFocus = activity.getCurrentFocus();
            if (currentFocus != null) {
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager
                        .HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 获取所有指定类型的子控件
     *
     * @param T
     * @return
     */
    private List<View> getAllChildViews(Activity act, Class<?> T) {
        View view = act.getWindow().getDecorView();
        return getAllChildViews(view, T);
    }

    private List<View> getAllChildViews(View parent, Class<?> T) {
        List<View> allchildren = new ArrayList<>();
        if (parent instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) parent;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                if (viewchild.getClass().equals(T)) {
                    allchildren.add(viewchild);
                }
                allchildren.addAll(getAllChildViews(viewchild, T));
            }
        }
        return allchildren;
    }

    /**
     * 获取时间
     *
     * @param date
     * @return
     */
    public static String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    /**
     * 获取时间
     *
     * @param date
     * @return
     */
    public static String getYearMonthTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        return format.format(date);
    }

    /**
     * 获取时间
     * /***
     *
     * @param date
     * @return
     */
    public static String getYearTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(date);
    }

    /**
     * 获取时间
     * /***
     *
     * @param date
     * @return
     */
    public static String getMonthTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("M");
        return format.format(date);
    }

    /**
     * 获取时间
     * /***
     *
     * @param date
     * @return
     */
    public static String getDayTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("d");
        return format.format(date);
    }

    /**
     * 获取时间编号
     *
     * @param date
     * @return
     */
    public static String getNumTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyMM");
        return format.format(date);
    }

    public static byte[] readStream(InputStream inStream) {
        byte[] buffer = new byte[1024];
        int len = -1;
        byte[] data = null;
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            data = outStream.toByteArray();
            outStream.close();
            inStream.close();
        } catch (Exception e) {
        }
        return data;
    }


    /**
     * @param @param  base64String
     * @param @return 设定文件
     * @return Bitmap    返回类型
     * @throws
     * @Title: base64ToBitmap
     * @Description: TODO(base64l转换为Bitmap)
     */
    public static Bitmap base64ToBitmap(String base64String) {

        byte[] decode = Base64.decode(base64String, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(decode, 0, decode.length);
    }

    /**
     * 压缩图片
     *
     * @param filePath
     * @param targetPath
     * @param quality
     * @param needRotate
     * @return
     */
    public static String compressImage(String filePath, String targetPath, int quality, boolean
            needRotate) {
        Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片
        if (needRotate) {
            int degree = readPictureDegree(filePath);//获取相片拍摄角度
            if (degree != 0) {//旋转照片角度，防止头像横着显示
                bm = rotateBitmap(bm, degree);
            }
        }
        File outputFile = new File(targetPath);
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                //outputFile.createNewFile();
            } else {
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } catch (Exception e) {
            return filePath;
        }
        return outputFile.getPath();
    }

    public static byte[] compressImage(String filePath, int quality) {
        Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片
        return getBitmapByte(bm, quality);
    }

    public static byte[] getBitmapByte(Bitmap bitmap, int quality) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //参数1转换类型，参数2压缩质量，参数3字节流资源
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public static byte[] readStream(File file) throws Exception {
        FileInputStream fs = new FileInputStream(file);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while (-1 != (len = fs.read(buffer))) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        fs.close();
        return outStream.toByteArray();
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options);
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 1280, 720);
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 获取照片角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转照片
     *
     * @param bitmap
     * @param degrees
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degrees);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m,
                    true);
            return bitmap;
        }
        return null;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int
            reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static boolean checkZhongXvJingYingCode(String str) {
        String pattern = "[\u4e00-\u9fa5]{1}[a-zA-Z_0-9]{7}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        boolean matches = m.matches();
        return matches;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        if (sdate != null && !TextUtils.isEmpty(sdate)) {
            String today = DateUtils.dateToString(new Date(System.currentTimeMillis()),
                    "yyyy-MM-dd");
            return sdate.contains(today);
        } else {
            return false;
        }
    }


    /**
     * 判断给定字符串时间是否在12小时内
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isBeLong12hour(String sdate) {
        if (sdate != null && !TextUtils.isEmpty(sdate)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
            long time2 = 0;
            try {
                time2 = simpleDateFormat.parse(sdate).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long hTime = System.currentTimeMillis() - time2;

            if (hTime > 43200000) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    //当前月份第一天
    public static String getMonthStatr() {

        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return dateFormater.format(cal.getTime());
    }

    //当前月份最后一天
    public static String getMonthEnd() {

        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateFormater.format(cal.getTime());
    }

    //当前年第一天
    public static String getYearStatr() {
        SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return dateFormater.format(cal.getTime());
    }

    //当前年最后一天
    public static String getYearEnd() {

        SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_YEAR,
                cal.getActualMaximum(Calendar.DAY_OF_YEAR));
        return dateFormater.format(cal.getTime());
    }

    //获取一个月前的日期
    public static String getLastYearStart() {

        SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_YEAR,
                cal.getActualMaximum(Calendar.DAY_OF_YEAR));
        return dateFormater.format(cal.getTime());
    }

    //获取上月第一天
    public static String getLastMonthStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cal.getTime());
        calendar.add(Calendar.MONTH, -1);
        String monthAgo = simpleDateFormat.format(calendar.getTime());
        return monthAgo;
    }

    //获取上月最后一天
    public static String getLastMonthEnd() {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cal.getTime());
        calendar.add(Calendar.MONTH, -1);
        String monthAgo = simpleDateFormat.format(calendar.getTime());
        return monthAgo;
    }

    /**
     * dp转换成px
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转换成dp
     */
    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转换成px
     */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转换成sp
     */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int getScreenWidth(Context mContext) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return width;
    }

    public static int getScreenHigth(Context mContext) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    public static String getData(long time) {
        String s = DateUtils.dateToString(new Date(time), DateUtils.FORMAT_5);
        return s;
    }
    public static String getData(long time,String f) {
        String s = DateUtils.dateToString(new Date(time),f);
        return s;
    }

    public static boolean isFileExist(String filePath) {
        if (isBlank(filePath)) {
            return false;
        } else {
            File file = new File(filePath);
            return file.exists() && file.isFile();
        }
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }
    public static boolean createDir(String fileDir) {
        try {
            File dirFile = new File(fileDir);
            if(!dirFile.exists()) {
                dirFile.mkdirs();
            }

            return true;
        } catch (Exception var2) {
            return false;
        }
    }
    /**
     * 压缩图片
     *
     * @param filePath
     * @param targetPath
     * @param quality
     * @return
     */
    public static String compressImage(String filePath, String targetPath, int quality) {
        Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片
        int degree = readPictureDegree(filePath);//获取相片拍摄角度
        if (degree != 0) {//旋转照片角度，防止头像横着显示
            bm = rotateBitmap(bm, degree);
        }
        File outputFile = new File(targetPath);
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                //outputFile.createNewFile();
            } else {
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } catch (Exception e) {
        }
        return outputFile.getPath();
    }
}
