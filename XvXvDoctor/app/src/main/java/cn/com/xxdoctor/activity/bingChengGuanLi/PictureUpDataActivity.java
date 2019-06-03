package cn.com.xxdoctor.activity.bingChengGuanLi;

import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.activity.bingChengGuanLi.BianZheng.AddBianZhengLunZhiActivity;
import cn.com.xxdoctor.adapter.PicPhotoListAdapter;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.PicListBean;
import cn.com.xxdoctor.bean.PicUpLoadResultBean;
import cn.com.xxdoctor.bean.ResultBean;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;
import cn.com.xxdoctor.utils.HcUtils;
import cn.com.xxdoctor.utils.JSONUtil;
import cn.com.xxdoctor.utils.LogUtil;
import cn.com.xxdoctor.utils.OkHttpUtils;
import cn.com.xxdoctor.utils.ToastUtils;
import cn.com.xxdoctor.view.TouchImageView;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PictureUpDataActivity extends DoctorBaseActivity {
    public OkHttpClient client;
    private Button submit_pic;
    private Button submit;
    private final int REQUEST_CODE_GALLERY = 1001;
    private List<PhotoInfo> mPhotoList;
    private PicPhotoListAdapter mChoosePhotoListAdapter;
    private GridView lv_photo;
    private String patientID;
    private String fieldRecordSign;
    private PicListBean picListBean;
    private List<PicListBean.ListBean> mTempList;
    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private PopupWindow pop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fieldRecordSign = getIntent().getStringExtra("fieldRecordSign");
        patientID = getIntent().getStringExtra("patientID");
        setContentView(R.layout.activity_picture_up_data);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);
        }

        initView();
        initData();
        initListener();
    }

    private void initView() {
        submit_pic = (Button) findViewById(R.id.submit_pic);
        submit = (Button) findViewById(R.id.submit);
        lv_photo = (GridView) findViewById(R.id.lv_photo);
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        title_back.setVisibility(View.VISIBLE);
        title_name.setText("图片上传");
    }

    @Override
    public void initData() {
        client = new OkHttpClient();
        mPhotoList = new ArrayList<>();
        mTempList = new ArrayList<>();
        mChoosePhotoListAdapter = new PicPhotoListAdapter(this, mPhotoList, mContext);
        lv_photo.setAdapter(mChoosePhotoListAdapter);
        requestPicList();
    }

    /**
     * 请求图片列表
     */
    private void requestPicList() {
        showWaitDialog();

        Map<String, Object> map = new HashMap<>();
        map.put("patientID", patientID);
        map.put("pageNo", "0");
        map.put("pageCount", "100");
        map.put("fieldRecordSign", fieldRecordSign);
        DoctorNetService.requestPicList(Constants.SELECT_FILE_SIGN_LIST_NEW, map, new
                NetWorkRequestInterface() {


                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        picListBean = (PicListBean) info;
                        for (PicListBean.ListBean listBean : picListBean.list) {
                            PhotoInfo photoInfo = new PhotoInfo();
                            photoInfo.setPhotoPath(Constants.IP + listBean.fileUrl);
                            mPhotoList.add(photoInfo);
                            mTempList.add(listBean);
                        }
                        mChoosePhotoListAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void initListener() {
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        submit_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, DoctorBaseAppliction
                        .functionConfigMax, mOnHanlderResultCallback);
            }
        });
        mChoosePhotoListAdapter.setOnDelectClickListener(new PicPhotoListAdapter
                .onDelectClickListener() {
            @Override
            public void onDelectClick(final int position) {
                showAffirmDialog("提示", "确认删除？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestDeletePic(position);
                    }
                });
            }
        });
        lv_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showPop(Constants.IP + mTempList.get(i).fileUrl);
            }
        });
    }

    public void showPop(String url) {
        TouchImageView contentView = new TouchImageView(mContext);
        pop = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 产生背景变暗效果
        Picasso.with(mContext).load(url).into(contentView);
        WindowManager.LayoutParams lp = this.getWindow()
                .getAttributes();
        lp.alpha = 0.4f;
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        this.getWindow().setAttributes(lp);
        pop.setTouchable(true);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOutsideTouchable(true);
        pop.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        pop.update();
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = PictureUpDataActivity.this.getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                PictureUpDataActivity.this.getWindow().addFlags(WindowManager.LayoutParams
                        .FLAG_DIM_BEHIND);
                PictureUpDataActivity.this.getWindow().setAttributes(lp);
            }
        });

    }

    /**
     * 删除照片
     *
     * @param position
     */
    private void requestDeletePic(final int position) {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("id", mTempList.get(position).patientimageID);
        DoctorNetService.requestAddOrChange(Constants.DELETE_IMAGE, map, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        ResultBean bean = (ResultBean) info;
                        ToastUtils.showMessage(mContext, bean.data);
                        mPhotoList.remove(position);
                        mTempList.remove(position);
                        mChoosePhotoListAdapter.notifyDataSetChanged();
                        Map<String, Object> map = new HashMap<>();
                        map.put(Constants.EVENTBUS_TYEPE, AddBianZhengLunZhiActivity
                                .UPDATA_BINGZHENG_SUCCESS);
                        EventBus.getDefault().post(map);
                    }
                });
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal
            .OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
//                for (PhotoInfo photoInfo : resultList) {
//                    upLoadPic(new File(photoInfo.getPhotoPath()));
//                }
                upLoadPic(resultList);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    public void upLoadPic(final List<PhotoInfo> files) {
        showWaitDialog();
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                if (!HcUtils.isFileExist(Constants.tempPath))
                    HcUtils.createDir(Constants.tempPath);
                String urlStr = Constants.UPLOAD_PIC +
                        "&userID=" + DoctorBaseAppliction.spUtil.getString
                        (Constants.USER_ID, "")
                        + "&modules=imageData"
                        + "&mobileType=" + Constants.MOBILE_TYPE
                        + "&patientID=" + patientID
                        + "&filesignID="
                        + "&fieldRecordSign=" + fieldRecordSign;
                MultipartBody.Builder builder = new MultipartBody.Builder();
                MultipartBody.Builder builder1 = builder.setType(MultipartBody.FORM);
                for (PhotoInfo photoInfo : files) {
                    File file = new File(photoInfo.getPhotoPath());
                    LogUtil.e(file.getAbsoluteFile() + "");
                    byte[] content = new byte[0];
                    try {
                        content = HcUtils.readStream(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    builder1.addFormDataPart("file", file.getName(), RequestBody.create(MediaType
                            .parse("application/octet-stream"), content));
                }


                RequestBody requestBody = builder.build();

                String result = OkHttpUtils.initUpLoad(urlStr, requestBody);
                LogUtil.e("上传图片啊 -- " + result);
                final PicUpLoadResultBean picUpLoadResultBean = JSONUtil.parseJsonToBean
                        (result, PicUpLoadResultBean.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (picUpLoadResultBean.uploadStatus.equals("0")) {

                            for (PicUpLoadResultBean.ImgListBean imgListBean : picUpLoadResultBean
                                    .imgList) {
                                if (!TextUtils.isEmpty(imgListBean.fileUrl)) {
                                    PhotoInfo photoInfo = new PhotoInfo();
                                    photoInfo.setPhotoPath(Constants.IP + imgListBean.fileUrl);
                                    mPhotoList.add(photoInfo);
                                    PicListBean.ListBean listBean = new PicListBean.ListBean();
                                    listBean.fileUrl = imgListBean.fileUrl;
                                    listBean.patientimageID = imgListBean.patientimageID;
                                    mTempList.add(listBean);
                                }
                            }
                            mChoosePhotoListAdapter.notifyDataSetChanged();
                            Map<String, Object> map = new HashMap<>();
                            map.put(Constants.EVENTBUS_TYEPE, AddBianZhengLunZhiActivity
                                    .UPDATA_BINGZHENG_SUCCESS);
                            EventBus.getDefault().post(map);
                        } else {
                            ToastUtils.showMessage(mContext, "上传照片失败");
                        }
                    }
                });
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).

                subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object result) {
                        hideWaitDialog();
                    }
                });
    }
}
