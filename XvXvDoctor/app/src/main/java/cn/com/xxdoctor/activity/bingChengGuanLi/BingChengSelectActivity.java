package cn.com.xxdoctor.activity.bingChengGuanLi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.bigkoo.pickerview.TimePickerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.activity.bingChengGuanLi.BianZheng.AddBianZhengLunZhiActivity;
import cn.com.xxdoctor.activity.bingChengGuanLi.BianZheng.BianZhengLunDetailZhiActivity;
import cn.com.xxdoctor.activity.bingChengGuanLi.LiangBiao.LiangBiaoSelectActivity;
import cn.com.xxdoctor.activity.bingChengGuanLi.LiangBiao.LiangBiaoSelectDetailActivity;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.chat.utils.ToastUtil;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;
import cn.com.xxdoctor.utils.DateUtils;
import cn.com.xxdoctor.utils.HcUtils;
import cn.com.xxdoctor.utils.ToastUtils;

/**
 * Created by chong.han on 2018/8/23.
 */

public class BingChengSelectActivity extends DoctorBaseActivity {
    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private RelativeLayout bingcheng_first;
    private RelativeLayout bingcheng_second;
    private RelativeLayout bingcheng_three;
    private String patientID;
    private String recordFlag;
    private String fieldRecordSign;
    private RelativeLayout bingcheng_four;
    private TextView bingcheng_jiuzhen_time;
    private String sicknessTime;
    private TextView bingcheng_jiuzhen_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bingcheng_layout);
        EventBus.getDefault().register(this);
        patientID = getIntent().getStringExtra("patientID");
        recordFlag = getIntent().getStringExtra("recordFlag");
        sicknessTime = getIntent().getStringExtra("sicknessTime");
        fieldRecordSign = getIntent().getStringExtra("fieldRecordSign");
        initView();
        initTimePicker();
        initData();
        initListener();
    }

    @Override
    public void initData() {
        if (TextUtils.isEmpty(sicknessTime)) {
            bingcheng_jiuzhen_time.setText(HcUtils.getData(System.currentTimeMillis()));
        } else {
            bingcheng_jiuzhen_time.setText(HcUtils.getData(Long.parseLong(sicknessTime)));
        }
    }

    @Override
    public void initListener() {
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bingcheng_jiuzhen_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show(bingcheng_jiuzhen_time);
            }
        });
        bingcheng_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if (TextUtils.isEmpty(fieldRecordSign)) {
                    intent = new Intent(mContext, AddBianZhengLunZhiActivity.class);
                } else {
                    intent = new Intent(mContext, BianZhengLunDetailZhiActivity.class);
                    intent.putExtra("fieldRecordSign", fieldRecordSign);
                }
                intent.putExtra("sicknessTime", bingcheng_jiuzhen_time.getText().toString());
                intent.putExtra("patientID", patientID);
                intent.putExtra("recordFlag", recordFlag);
                startActivity(intent);
            }
        });
        bingcheng_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(fieldRecordSign)) {
                    ToastUtils.showMessage(mContext, "请先进行辨证论治");
                    return;
                }
                Intent intent = new Intent(mContext, PictureUpDataActivity.class);
                intent.putExtra("patientID", patientID);
                intent.putExtra("fieldRecordSign", fieldRecordSign);
                startActivity(intent);
            }
        });
        bingcheng_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = null;
                if (TextUtils.isEmpty(fieldRecordSign)) {
                    intent = new Intent(mContext, LiangBiaoSelectActivity.class);
                } else {
                    intent = new Intent(mContext, LiangBiaoSelectDetailActivity.class);
                    intent.putExtra("fieldRecordSign", fieldRecordSign);
                }
                intent.putExtra("patientID", patientID);
                intent.putExtra("recordFlag", recordFlag);
                startActivity(intent);
            }
        });
        bingcheng_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(fieldRecordSign)) {
                    ToastUtils.showMessage(mContext, "请先进行辨证论治");
                    return;
                }
                Intent intent = new Intent(mContext, BingChengBaoGaoActivity.class);
                intent.putExtra("patientID", patientID);
                intent.putExtra("fieldRecordSign", fieldRecordSign);
                startActivity(intent);
            }
        });
        bingcheng_jiuzhen_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(fieldRecordSign)) {
                    ToastUtil.shortToast(mContext, "首次添加无法修改就诊方式");
                    return;
                }
                showSelect();
            }
        });
    }

    /**
     * 选择首诊 或者 复诊
     */
    public void showSelect() {
        ActionSheet.createBuilder(mContext, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("首诊", "复诊")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        if (index == 0) {
                            recordFlag = "1";
                        } else {
                            recordFlag = "0";
                        }

                        requestChangeType(recordFlag);
                    }
                })
                .show();
    }

    /**
     * 修改就诊方式
     */
    private void requestChangeType(final String recordFlag) {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("fieldRecordSign", fieldRecordSign);
        map.put("recordFlag", recordFlag);
        map.put("mobileType", Constants.MOBILE_TYPE);
        DoctorNetService.requestAddOrChange(Constants.UPDATE_FRIST_OR_DOUBLE_DIAGNOSIS, map, new
                NetWorkRequestInterface() {
                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        title_name.setText(recordFlag.equals("1")?"首诊":"复诊");
                        bingcheng_jiuzhen_type.setText(recordFlag.equals("1")?"首诊":"复诊");
                        Map<String, Object> map = new HashMap<>();
                        map.put(Constants.EVENTBUS_TYEPE, AddBianZhengLunZhiActivity
                                .UPDATA_BINGZHENG_SUCCESS);
                        EventBus.getDefault().post(map);
                    }
                });
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        bingcheng_jiuzhen_time = (TextView) findViewById(R.id.bingcheng_jiuzhen_time);
        bingcheng_jiuzhen_type = (TextView) findViewById(R.id.bingcheng_jiuzhen_type);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        bingcheng_first = (RelativeLayout) findViewById(R.id.bingcheng_first);
        bingcheng_second = (RelativeLayout) findViewById(R.id.bingcheng_second);
        bingcheng_three = (RelativeLayout) findViewById(R.id.bingcheng_three);
        bingcheng_four = (RelativeLayout) findViewById(R.id.bingcheng_four);
        title_back.setVisibility(View.VISIBLE);
        if (recordFlag.equals("1")) {
            title_name.setText("首诊");
            bingcheng_jiuzhen_type.setText("首诊");
        } else {
            title_name.setText("复诊");
            bingcheng_jiuzhen_type.setText("复诊");
        }

    }

    @Subscribe
    public void onEventMainThread(Map<String, Object> map) {
        String type = (String) map.get(Constants.EVENTBUS_TYEPE);
        if (AddBianZhengLunZhiActivity.UPDATA_BINGZHENG_SUCCESS.equals(type)) {
            if (TextUtils.isEmpty(fieldRecordSign))
                fieldRecordSign = (String) map.get(Constants.EVENTBUS_VALUE);
        }
    }

    /**
     * 初始化时间弹框
     */
    private void initTimePicker() {
        String yearTime = HcUtils.getYearTime(new Date(System.currentTimeMillis()));
        String monthTime = HcUtils.getMonthTime(new Date(System.currentTimeMillis()));
        String dayTime = HcUtils.getDayTime(new Date(System.currentTimeMillis()));
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1980, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2030, 0, 1);
        pvTime = new TimePickerView.Builder(this, new TimePickerView
                .OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date, View v) {
                String found_date = DateUtils.dateToString(date, DateUtils.FORMAT_5);
                ((TextView) v).setText(found_date);
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setCancelColor(getResources().getColor(R.color.main_color))
                .setSubmitColor(getResources().getColor(R.color.main_color))
                .setRangDate(startDate, endDate)
                .setBackgroundId(0x66000000) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
    }
}
