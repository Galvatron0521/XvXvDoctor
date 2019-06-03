package cn.com.xxdoctor.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.adapter.MyLiangBiaoListAdapter;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.MyLiangBiaoInfoListBean;
import cn.com.xxdoctor.bean.PicUpLoadResultBean;
import cn.com.xxdoctor.chat.utils.ToastUtil;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;
import cn.com.xxdoctor.utils.HcUtils;
import cn.com.xxdoctor.utils.JSONUtil;
import cn.com.xxdoctor.utils.LogUtil;
import cn.com.xxdoctor.utils.OkHttpUtils;
import cn.com.xxdoctor.utils.ToastUtils;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyLiangBiaoListActivity extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private ListView my_liangbiao_list;
    private MyLiangBiaoInfoListBean infoBean;
    private List<MyLiangBiaoInfoListBean.ListBean> mDatas;
    private MyLiangBiaoListAdapter myLiangBiaoListAdapter;
    private List<PhotoInfo> mPhotoList;
    private final int REQUEST_CODE_GALLERY = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_liang_biao_list);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        my_liangbiao_list = (ListView) findViewById(R.id.my_liangbiao_list);
        title_back.setVisibility(View.VISIBLE);
        title_name.setText("我的量表");
        title_right_tv.setVisibility(View.VISIBLE);
        title_right_tv.setText("上传量表");
    }

    @Override
    public void initData() {
        mDatas = new ArrayList<>();
        mPhotoList = new ArrayList<>();
        myLiangBiaoListAdapter = new MyLiangBiaoListAdapter(mDatas, mContext);
        my_liangbiao_list.setAdapter(myLiangBiaoListAdapter);
        requestData();
    }

    @Override
    public void initListener() {
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title_right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, DoctorBaseAppliction
                        .functionConfig, mOnHanlderResultCallback);
            }
        });
        my_liangbiao_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyLiangBiaoInfoListBean.ListBean listBean = mDatas.get(i);
                Intent intent = new Intent(mContext, MyLiangBiaoDetailActivity.class);
                intent.putExtra("title", listBean.parentModule.moduleName);
                intent.putExtra("info", listBean);
                startActivity(intent);
            }
        });
        myLiangBiaoListAdapter.setOnButtonClick(new MyLiangBiaoListAdapter.onButtonClick() {
            @Override
            public void onButtonClick(final int position) {
                int isShared = mDatas.get(position).parentModule.isShared;
                String isShare = "";
                String message = "";
                // 1 已共享 0 未共享
                if (isShared == 1) {
                    isShare = "1";
                    message = "确认取消共享该量表？";
                } else {
                    isShare = "0";
                    message = "确认共享该量表？";
                }
                final String finalIsShare = isShare;
                showAffirmDialog("提示", message, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestChangeStatu(finalIsShare, mDatas.get(position).parentModule
                                .moduleCode);
                    }
                });
            }
        });
    }

    /**
     * 请求量表列表
     */
    private void requestData() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("userID", DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, ""));
        DoctorNetService.requestMyLiangBiaoList(Constants.SELECT_SCALE_LIST, map, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        mDatas.clear();
                        infoBean = (MyLiangBiaoInfoListBean) info;
                        mDatas.addAll(infoBean.list);
                        myLiangBiaoListAdapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * 修改量表分享装填
     * isShared":0, "moduleCode":"SP020113"
     */
    private void requestChangeStatu(String isShared, String moduleCode) {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("isShared", isShared);
        map.put("moduleCode", moduleCode);
        DoctorNetService.requestAddOrChange(Constants.UPDATA_SCALE_IS_SHARED, map, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                        ToastUtil.shortToast(mContext, "修改量表共享状态失败，请重试");
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        ToastUtil.shortToast(mContext, "修改量表共享状态成功");
                        requestData();
                    }
                });
    }
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal
            .OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                for (PhotoInfo photoInfo : resultList) {
                    upLoadPic(new File(photoInfo.getPhotoPath()));
                }
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };
    public void upLoadPic(final File file) {
        showWaitDialog();
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                //http://111.17.215.37:8023/form/scaleFileUpload
                // .html?mobileType=2&userID=1&isShared=1;
                LogUtil.e("hanchong---->>>:图片地址" + file.getPath() + "图片大小" + file.length());
                if (!HcUtils.isFileExist(Constants.tempPath))
                    HcUtils.createDir(Constants.tempPath);
                String targetPath = Constants.tempPath + file.getName();
                String compressImage = HcUtils.compressImage(file.getPath(), targetPath, 50);
                File compressedPic = new File(compressImage);
                LogUtil.e("hanchong---->>>:图片地址" + compressedPic
                        .getPath() + "压缩后图片大小" + compressedPic.length());
                String urlStr = Constants.SCALE_FILE_UPLOAD +
                        "&userID=" + DoctorBaseAppliction.spUtil.getString
                        (Constants.USER_ID, "")
                        + "&mobileType=" + Constants.MOBILE_TYPE
                        + "&isShared=" + "1";
                try {
                    LogUtil.e(file.getAbsoluteFile() + "");
                    byte[] content;
                    content = HcUtils.readStream(file);
                    MultipartBody.Builder builder = new MultipartBody.Builder();
                    builder.setType(MultipartBody.FORM).addFormDataPart("file", file.getName
                            (), RequestBody.create(MediaType.parse
                            ("application/octet-stream"), content));

                    RequestBody requestBody = builder.build();

                    String result = OkHttpUtils.initUpLoad(urlStr, requestBody);
                    LogUtil.e("上传图片 -- " + result);
                    final PicUpLoadResultBean picUpLoadResultBean = JSONUtil.parseJsonToBean
                            (result, PicUpLoadResultBean.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(picUpLoadResultBean.fileUrl)) {
                               ToastUtil.shortToast(mContext,"上传成功");
                                hideWaitDialog();
                                requestData();
                            } else {
                                ToastUtils.showMessage(mContext, "上传失败请重试");
                            }
                        }
                    });
                    subscriber.onNext(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
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
