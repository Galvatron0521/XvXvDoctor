package cn.com.xxdoctor.activity.bingChengGuanLi;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.bean.BaoGaoInfoBean;
import cn.com.xxdoctor.bean.PdfReturnBean;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;
import cn.com.xxdoctor.utils.HcUtils;
import cn.com.xxdoctor.utils.LogUtil;

public class BingChengBaoGaoActivity extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private String fieldRecordSign;
    private LinearLayout baogao_container;
    private String patientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_cheng_bao_gao);
        patientID = getIntent().getStringExtra("patientID");
        fieldRecordSign = getIntent().getStringExtra("fieldRecordSign");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        baogao_container = (LinearLayout) findViewById(R.id.baogao_container);
        title_back.setVisibility(View.VISIBLE);
        title_name.setText("病例报告");
        title_right_tv.setVisibility(View.VISIBLE);
        title_right_tv.setText("下载");
    }

    @Override
    public void initData() {
        requestFormDetail();
    }

    /**
     * 请求病例报告详情
     */
    private void requestFormDetail() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("fieldRecordSign", fieldRecordSign);
        DoctorNetService.requestFieldRecordListForm(Constants.SELECT_FIELD_RECORD_LIST_FORM, map,
                new NetWorkRequestInterface() {


                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        BaoGaoInfoBean baoGaoInfoBean = (BaoGaoInfoBean) info;
                        changeLayout(baoGaoInfoBean);
                    }
                });
    }

    /**
     * 添加布局到界面
     *
     * @param baoGaoInfoBean
     */
    private void changeLayout(BaoGaoInfoBean baoGaoInfoBean) {
        baogao_container.removeAllViews();
        // 辅助检查：
        for (BaoGaoInfoBean.FileRecordModuleCodeListBean fileRecordBeforeListBean :
                baoGaoInfoBean.fileRecordModuleCodeList) {
            LogUtil.e(fileRecordBeforeListBean.moduleName + "");
            baogao_container.addView(getTitle(fileRecordBeforeListBean.moduleName));
            if (fileRecordBeforeListBean.list.size() == 0) {
                baogao_container.addView(getNormalTv("暂无"));
            } else {
                for (BaoGaoInfoBean.ListBean listBean : fileRecordBeforeListBean
                        .list) {
                    if (listBean.displayName.equals(listBean.contents)) {
                        if (fileRecordBeforeListBean.moduleCode.equals("SP020140") ||
                                fileRecordBeforeListBean.moduleCode.equals("SP020139")) {
                            baogao_container.addView(getNormalTv(listBean.fieldControlName
                                    + "  " + listBean.contents));
                        } else {
                            String[] split = listBean.fieldControlName.split("_");
                            baogao_container.addView(getNormalTv(split[split.length -
                                    1] + "   " + listBean.contents));
                        }
                    } else {
                        baogao_container.addView(getNormalTv(listBean.displayName
                                + "  " + listBean.contents));
                    }
                }
            }
        }
        TextView textView = new TextView(mContext);
        textView.setWidth(HcUtils.getScreenWidth(mContext));
        textView.setHeight(HcUtils.dp2px(mContext, 30));
        textView.setBackgroundColor(Color.WHITE);
        baogao_container.addView(textView);
        // 之前数据
        for (BaoGaoInfoBean.FileRecordBeforeListBean fileRecordBeforeListBean :
                baoGaoInfoBean.fileRecordBeforeList) {
            LogUtil.e(fileRecordBeforeListBean.moduleName + "");
            baogao_container.addView(getTitle(fileRecordBeforeListBean.moduleName));
            if (fileRecordBeforeListBean.list.size() == 0) {
                baogao_container.addView(getNormalTv("暂无"));
            } else {
                for (BaoGaoInfoBean.ListBean listBean : fileRecordBeforeListBean
                        .list) {
                    if (listBean.displayName.equals(listBean.contents)) {
                        String[] split = listBean.fieldControlName.split("_");
                        baogao_container.addView(getNormalTv(split[split.length -
                                1] + "   " + listBean.contents));
                    } else {
                        baogao_container.addView(getNormalTv(listBean.displayName
                                + "  " + listBean.contents));
                    }
                }
            }
        }

        TextView textView2 = new TextView(mContext);
        textView2.setWidth(HcUtils.getScreenWidth(mContext));
        textView2.setHeight(HcUtils.dp2px(mContext, 40));
        textView2.setBackgroundColor(Color.WHITE);
        baogao_container.addView(textView2);
        // 之后数据
        for (BaoGaoInfoBean.FileRecordAfterListBean fileRecordAfterListBean :
                baoGaoInfoBean.fileRecordAfterList) {
            LogUtil.e(fileRecordAfterListBean.moduleName + "");
            baogao_container.addView(getTitle(fileRecordAfterListBean.moduleName));
            if (fileRecordAfterListBean.list.size() == 0) {
                baogao_container.addView(getNormalTv("暂无"));
            } else {
                for (BaoGaoInfoBean.ListBean listBean : fileRecordAfterListBean
                        .list) {
                    if (listBean.displayName.equals(listBean.contents)) {
                        String[] split = listBean.fieldControlName.split("_");
                        baogao_container.addView(getNormalTv(split[split.length -
                                1] + "   " + listBean.contents));
                    } else {
                        baogao_container.addView(getNormalTv(listBean.displayName
                                + "  " + listBean.contents));
                    }
                }
            }
        }

        TextView textView3 = new TextView(mContext);
        textView3.setWidth(HcUtils.getScreenWidth(mContext));
        textView3.setBackgroundColor(Color.WHITE);
        textView3.setText("医生：" + baoGaoInfoBean.userName);
        textView3.setTextColor(Color.parseColor("#999999"));
        baogao_container.addView(textView3);

        TextView textView4 = new TextView(mContext);
        textView4.setWidth(HcUtils.getScreenWidth(mContext));
        textView4.setHeight(HcUtils.dp2px(mContext, 40));
        textView4.setBackgroundColor(Color.WHITE);
        baogao_container.addView(textView4);
    }

    @Override
    public void initListener() {
        // 返回
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //下载按钮
        title_right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPdfUrl();
            }
        });
    }

    /**
     * 请求病例报告详情
     */
    private void requestPdfUrl() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("patientID", patientID);
        map.put("fieldRecordSign", fieldRecordSign);
        DoctorNetService.requestGetPdf(Constants.TO_PDF, map,
                new NetWorkRequestInterface() {


                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        PdfReturnBean pdfReturnBean = (PdfReturnBean) info;
                        Uri uri = Uri.parse(pdfReturnBean.downLoad);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
    }

    /**
     * 获取一级标题
     *
     * @param title
     * @return
     */
    public TextView getTitle(String title) {
        TextView textView = new TextView(mContext);
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        textView.setText(title);
        textView.setPadding(10, 2, 10, 0);
        textView.setBackgroundColor(Color.parseColor("#44000000"));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(HcUtils.getScreenWidth
                (mContext), LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        return textView;
    }

    /**
     * 获取展示的 TextView
     *
     * @param title
     * @return
     */
    public TextView getNormalTv(String title) {
        TextView textView = new TextView(mContext);
        textView.setTextSize(14);
        textView.setTextColor(Color.parseColor("#99000000"));
        textView.setText(title);
        textView.setPadding(10, 10, 10, 10);
        return textView;
    }
}
