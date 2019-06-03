package cn.com.xxdoctor.activity.patientCenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.activity.bingChengGuanLi.BingChengManageListActivity;
import cn.com.xxdoctor.activity.suiFangRecord.SuiFangListActivity;
import cn.com.xxdoctor.activity.tag.SelectPatientTagActivity;
import cn.com.xxdoctor.activity.tag.TagManageActivity;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.PatientInfoBean;
import cn.com.xxdoctor.bean.ResultBean;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;
import cn.com.xxdoctor.utils.ToastUtils;

public class PatientInfoActivity extends DoctorBaseActivity {

    private ImageView patient_title_back;
    private TextView patient_title_delete;
    private TextView patient_info_logo;
    private TextView patient_info_name;
    private TextView patient_info_sex;
    private TextView patient_info_age;
    private LinearLayout patient_info_ll;
    private TextView patient_info_tag;
    private CardView patient_info_base_message;
    private CardView patient_info_guanli;
    private CardView patient_info_fangan;
    private CardView patient_info_tonghua;
    private String patientID;
    private PatientInfoBean patientInfoBean;
    public static String DELETE_PATIENT_SUCCESS = "delete_patient_success";
    private String user_id;
    private StringBuilder labeIds = new StringBuilder(); // 用于患者标签删除
    private String phone;
    private String create_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_patient_info);
        patientID = getIntent().getStringExtra("patientID");
        create_time = getIntent().getStringExtra("create_time");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        patient_title_back = (ImageView) findViewById(R.id.patient_title_back);
        patient_title_delete = (TextView) findViewById(R.id.patient_title_delete);
        patient_info_logo = (TextView) findViewById(R.id.patient_info_logo);
        patient_info_name = (TextView) findViewById(R.id.patient_info_name);
        patient_info_sex = (TextView) findViewById(R.id.patient_info_sex);
        patient_info_age = (TextView) findViewById(R.id.patient_info_age);
        patient_info_ll = (LinearLayout) findViewById(R.id.patient_info_ll);
        patient_info_tag = (TextView) findViewById(R.id.patient_info_tag);
        patient_info_base_message = (CardView) findViewById(R.id.patient_info_base_message);
        patient_info_guanli = (CardView) findViewById(R.id.patient_info_guanli);
        patient_info_fangan = (CardView) findViewById(R.id.patient_info_fangan);
        patient_info_tonghua = (CardView) findViewById(R.id.patient_info_tonghua);
    }

    @Override
    public void initData() {
        user_id = DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, "");
        phone = DoctorBaseAppliction.spUtil.getString(Constants.MOBILE, "");
        requestPatientInfo();
    }

    private void requestPatientInfo() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("patientID", patientID);
        map.put("userID", DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, ""));
        DoctorNetService.requestPatientDetail(Constants.GET_PATIENT_DETAILS, map, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                        ToastUtils.showMessage(mContext, "请求患者详情失败");
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        patientInfoBean = (PatientInfoBean) info;
                        patient_info_logo.setText(patientInfoBean.patientDetails.name.substring
                                (0, 1));
                        patient_info_name.setText(patientInfoBean.patientDetails.name);
                        patient_info_sex.setText(patientInfoBean.patientDetails.sex);
                        patient_info_age.setText(patientInfoBean.patientDetails.age);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < patientInfoBean.patientDetails.lablelist.size(); i++) {
                            if (i == patientInfoBean.patientDetails.lablelist.size() - 1) {
                                sb.append(patientInfoBean.patientDetails.lablelist.get(i)
                                        .lableName);
                                labeIds.append(patientInfoBean.patientDetails.lablelist.get(i)
                                        .lableID);
                            } else {
                                sb.append(patientInfoBean.patientDetails.lablelist.get(i)
                                        .lableName + ",");
                                labeIds.append(patientInfoBean.patientDetails.lablelist.get(i)
                                        .lableID + ",");
                            }
                        }
                        patient_info_tag.setText(TextUtils.isEmpty(sb.toString()) ? "未设置标签" : sb
                                .toString());
                    }
                });
    }

    @Override
    public void initListener() {
        patient_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        patient_title_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAffirmDialog("提示", "确认移除该患者？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestDelete();
                    }
                });
            }
        });
        patient_info_base_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddPatientInfoActivity.class);
                intent.putExtra("isAdd", false);
                intent.putExtra("patientID", patientID);
                intent.putExtra("info", patientInfoBean);
                startActivity(intent);
            }
        });
        patient_info_guanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BingChengManageListActivity.class);
                intent.putExtra("patientID", patientID);
                intent.putExtra("info", patientInfoBean);
                intent.putExtra("create_time", create_time);
                startActivity(intent);
            }
        });
        patient_info_fangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SuiFangListActivity.class);
                intent.putExtra("patientID", patientID);
                intent.putExtra("name", patientInfoBean.patientDetails.name);
                startActivity(intent);
            }
        });
        patient_info_tonghua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showMessage(mContext, "未获取到患者手机号");
                    return;
                }
                showSelect();
            }
        });
        //标签
        patient_info_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SelectPatientTagActivity.class);
                intent.putExtra("tag", patient_info_tag.getText().toString());
                intent.putExtra("labeIds", labeIds.toString());
                intent.putExtra("patientID", patientID);
                startActivity(intent);
            }
        });
    }

    private void requestDelete() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("id", patientID);
        map.put("userID", user_id);
        map.put("mobileType", Constants.MOBILE_TYPE);
        DoctorNetService.requestAddOrChange(Constants.DELETE_PATIENT, map, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                        ToastUtils.showMessage(mContext, "移除失败，请重试");
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        ResultBean bean = (ResultBean) info;
                        ToastUtils.showMessage(mContext, bean.data);
                        Map<String, Object> map = new HashMap<>();
                        map.put(Constants.EVENTBUS_TYEPE, DELETE_PATIENT_SUCCESS);
                        EventBus.getDefault().post(map);
                        finish();
                    }
                });
    }

    /**
     * 拨打电话 发送短信
     */
    public void showSelect() {
        ActionSheet.createBuilder(mContext, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("拨打电话", "发送短信")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        if (index == 0) {
                            callPhone();
                        } else {
                            sendMessage();
                        }
                    }
                })
                .show();
    }

    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + patientInfoBean.patientDetails.mobile);
        intent.setData(data);
        startActivity(intent);
    }

    public void sendMessage() {
        Intent intent = new Intent();                        //创建 Intent 实例
        intent.setAction(Intent.ACTION_SENDTO);             //设置动作为发送短信
        intent.setData(Uri.parse("smsto:" + patientInfoBean.patientDetails.mobile));
        //设置发送的号码
        intent.putExtra("sms_body", "");              //设置发送的内容
        startActivity(intent);
    }

    @Subscribe
    public void onEventMainThread(Map<String, Object> map) {
        String type = (String) map.get(Constants.EVENTBUS_TYEPE);
        if (type.equals(SelectPatientTagActivity.CHANGE_TAG_SUCCESS) || type.equals
                (TagManageActivity.TAG_HAS_CHANGE)) {
            requestPatientInfo();
        } else if (type.equals(AddPatientInfoActivity.ADD_PATIENT_SUCCESS)) {
            requestPatientInfo();
        }
    }
}
