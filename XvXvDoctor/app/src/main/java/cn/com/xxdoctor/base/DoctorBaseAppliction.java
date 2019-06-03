package cn.com.xxdoctor.base;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.util.concurrent.TimeUnit;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.utils.PicassoImageLoader;
import cn.com.xxdoctor.utils.PicassoPauseOnScrollListener;
import cn.com.xxdoctor.utils.SPUtil;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.jpush.im.android.api.JMessageClient;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by chong.han on 2018/8/21.
 */

public class DoctorBaseAppliction extends Application {
    public static boolean isTestState = true;
    public static OkHttpClient sOkHttpClient;
    public static DoctorBaseAppliction mInstance;
    static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
    public static SPUtil spUtil;
    public static String JMAppKey = "53ae66c14ab0234bbd26367f";
    public static FunctionConfig functionConfig;
    public static FunctionConfig functionConfigMax;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        File cacheDir = new File(DoctorBaseAppliction.mInstance.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
        sOkHttpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(1, TimeUnit.MINUTES) //设置连接超时
                .readTimeout(1, TimeUnit.MINUTES) //设置读取超时
                .writeTimeout(1, TimeUnit.MINUTES) //设置写入超时
                .cache(cache)
                .build();

        spUtil = new SPUtil(this, "fileName");
        initPhoto();
        initMaxPhoto();
        initImageLoader(mInstance);
        //初始化极光   是否开启消息漫游
        JMessageClient.setDebugMode(true);
        JMessageClient.init(mInstance, false);


    }

    private void initPhoto() {
        //配置功能
        functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(false)
                .setEnableEdit(false)
                .setEnableCrop(false)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .setMutiSelectMaxSize(1)
                .build();
        //配置imageloader
        ImageLoader imageloader = new PicassoImageLoader();

        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(getResources().getColor(R.color.main_color))
                .setFabNornalColor(getResources().getColor(R.color.main_color))
                .setCheckSelectedColor(getResources().getColor(R.color.main_color))
                .setFabPressedColor(getResources().getColor(R.color.main_color))
                //设置Floating按钮Pressed状态颜色
                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(mInstance, imageloader, theme)
                .setPauseOnScrollListener(new PicassoPauseOnScrollListener(false, true))
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);
    }

    private void initMaxPhoto() {
        //配置功能
        functionConfigMax = new FunctionConfig.Builder()
                .setEnableCamera(false)
                .setEnableEdit(false)
                .setEnableCrop(false)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .setMutiSelectMaxSize(9)
                .build();
        //配置imageloader
        ImageLoader imageloader = new PicassoImageLoader();

        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(getResources().getColor(R.color.main_color))
                .setFabNornalColor(getResources().getColor(R.color.main_color))
                .setCheckSelectedColor(getResources().getColor(R.color.main_color))
                .setFabPressedColor(getResources().getColor(R.color.main_color))
                //设置Floating按钮Pressed状态颜色
                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(mInstance, imageloader, theme)
                .setPauseOnScrollListener(new PicassoPauseOnScrollListener(false, true))
                .setFunctionConfig(functionConfigMax)
                .build();
        GalleryFinal.init(coreConfig);
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config.build());
    }

}
