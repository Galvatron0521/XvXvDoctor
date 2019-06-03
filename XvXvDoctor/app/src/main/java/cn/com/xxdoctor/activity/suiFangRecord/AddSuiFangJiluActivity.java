package cn.com.xxdoctor.activity.suiFangRecord;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.activity.patientCenter.PatientListActivity;
import cn.com.xxdoctor.activity.suiFangFangAn.SuiFangFangAnActivity;
import cn.com.xxdoctor.adapter.SuiFangTaskListAdapter;
import cn.com.xxdoctor.base.BaseReturnBean;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.CardBean;
import cn.com.xxdoctor.bean.PatientListBean;
import cn.com.xxdoctor.bean.PatientSuiFangListBean;
import cn.com.xxdoctor.bean.ResultBean;
import cn.com.xxdoctor.bean.SuiFangFangAnBean;
import cn.com.xxdoctor.bean.SuiFangTypeBean;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;
import cn.com.xxdoctor.utils.DateUtils;
import cn.com.xxdoctor.utils.HcUtils;
import cn.com.xxdoctor.utils.JSONUtil;
import cn.com.xxdoctor.utils.ToastUtils;
import cn.com.xxdoctor.view.MyListView;

public class AddSuiFangJiluActivity extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private TextView add_suif_patient_name;
    private TextView add_suif_fangan_name;
    private TextView add_suif_fangan_type;
    private TextView add_suif_start_time;
    private TextView add_suif_chuangjian_name;
    private TextView add_suif_chuangjian_time;
    private EditText add_suif_detial;
    private TextView add_suif_end_time;
    private EditText add_suif_jieshu_detail;
    private boolean isAdd;
    private LinearLayout add_suif_jieshu_detail_ll;
    private LinearLayout add_suif_end_time_ll;
    private OptionsPickerView pvOptions;
    private String patientID;
    private String suifang_type_id;
    public static String ADD_SUIFANG_INFO_SUCCESS = "add_suifang_info_success";
    private SuiFangFangAnBean.ListBean listBean;
    private PatientSuiFangListBean.ListBean patientSuiFangListBean;
    private boolean isOver;
    private boolean isDoctorCreate; // 判断是患者入口新增 还是医生入口新增
    private LinearLayout add_suif_chuangjian_time_ll;
    private String suiFangType;
    private MyListView add_suif_task_list;
    private SuiFangTaskListAdapter taskAdapter;
    private List<PatientSuiFangListBean.ListBean.ProjectlistBean> mDatas;
    private String followRecordId;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_add_sui_fang_info);
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        patientID = getIntent().getStringExtra("patientID");
        followRecordId = getIntent().getStringExtra("followRecordId");
        if (isAdd) {
            name = getIntent().getStringExtra("name");
            if (TextUtils.isEmpty(name)) {
                isDoctorCreate = true;
            }
        } else {
            patientSuiFangListBean = (PatientSuiFangListBean.ListBean) getIntent()
                    .getSerializableExtra("PatientSuiFangListBean");
        }
        initTimePicker();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        add_suif_patient_name = (TextView) findViewById(R.id.add_suif_patient_name);
        add_suif_fangan_name = (TextView) findViewById(R.id.add_suif_fangan_name);
        add_suif_fangan_type = (TextView) findViewById(R.id.add_suif_fangan_type);
        add_suif_start_time = (TextView) findViewById(R.id.add_suif_start_time);
        add_suif_chuangjian_name = (TextView) findViewById(R.id.add_suif_chuangjian_name);
        add_suif_chuangjian_time = (TextView) findViewById(R.id.add_suif_chuangjian_time);
        add_suif_chuangjian_time_ll = (LinearLayout) findViewById(R.id.add_suif_chuangjian_time_ll);
        add_suif_detial = (EditText) findViewById(R.id.add_suif_detial);
        add_suif_end_time = (TextView) findViewById(R.id.add_suif_end_time);
        add_suif_jieshu_detail = (EditText) findViewById(R.id.add_suif_jieshu_detail);
        add_suif_jieshu_detail_ll = (LinearLayout) findViewById(R.id.add_suif_jieshu_detail_ll);
        add_suif_end_time_ll = (LinearLayout) findViewById(R.id.add_suif_end_time_ll);
        add_suif_task_list = (MyListView) findViewById(R.id.add_suif_task_list);

        title_back.setVisibility(View.VISIBLE);
        title_name.setText("添加随访记录");
        if (isDoctorCreate) {
            add_suif_patient_name.setHint("请选择患者");
        }
        if (!isAdd) {
            add_suif_detial.setEnabled(false);
            add_suif_fangan_name.setEnabled(false);
            add_suif_fangan_type.setEnabled(false);
            add_suif_start_time.setEnabled(false);
            add_suif_chuangjian_time.setEnabled(false);
            add_suif_detial.setEnabled(false);
            /**
             *
             */
            add_suif_chuangjian_time_ll.setVisibility(View.VISIBLE);
            add_suif_patient_name.setText(patientSuiFangListBean.name);
            add_suif_fangan_name.setText(patientSuiFangListBean.followName);
            if (patientSuiFangListBean.followType.equals("1")) {
                suiFangType = "门诊随访";
            } else if (patientSuiFangListBean.followType.equals("2")) {
                suiFangType = "住院随访";
            } else if (patientSuiFangListBean.followType.equals("3")) {
                suiFangType = "电话随访";
            } else { //4
                suiFangType = "其他方式";
            }
            add_suif_fangan_type.setText(suiFangType);
            add_suif_chuangjian_name.setText(patientSuiFangListBean.createUser);
            add_suif_chuangjian_time.setText(HcUtils.getData(patientSuiFangListBean.createTime));
            add_suif_detial.setText(patientSuiFangListBean.descript);
            title_right_tv.setVisibility(View.VISIBLE);
            title_right_tv.setText("删除");
        } else {
            title_right_tv.setVisibility(View.VISIBLE);
            title_right_tv.setText("保存");
            add_suif_patient_name.setText(name);
            add_suif_chuangjian_name.setText(DoctorBaseAppliction.spUtil.getString(Constants
                    .Name, ""));
        }
    }

    @Override
    public void initData() {
        mCardItem = new ArrayList<>();
        mDatas = new ArrayList<>();
        if (!isAdd) {
            if (patientSuiFangListBean.projectlist != null) {
                mDatas.addAll(patientSuiFangListBean.projectlist);
            }
            taskAdapter = new SuiFangTaskListAdapter(mDatas, mContext);
            add_suif_task_list.setAdapter(taskAdapter);
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
        title_right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("删除".equals(title_right_tv.getText().toString())) {
                    showAffirmDialog("提示", "确定删除改掉随访记录？", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestDelete();
                        }
                    });
                } else {
                    submit();
                }
            }
        });
        add_suif_patient_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDoctorCreate) {
                    Intent intent = new Intent(mContext, PatientListActivity.class);
                    startActivity(intent);
                }
            }
        });
        add_suif_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HcUtils.hideKeyboard(AddSuiFangJiluActivity.this);
                pvTime.show(add_suif_start_time);
            }
        });
        add_suif_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HcUtils.hideKeyboard(AddSuiFangJiluActivity.this);
                pvTime.show(add_suif_start_time);
            }
        });
        add_suif_chuangjian_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HcUtils.hideKeyboard(AddSuiFangJiluActivity.this);
                pvTime.show(add_suif_chuangjian_time);
            }
        });
        add_suif_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HcUtils.hideKeyboard(AddSuiFangJiluActivity.this);
                pvTime.show(add_suif_end_time);
            }
        });
        add_suif_fangan_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initOptionPicker(add_suif_fangan_type);
                if (mCardItem.size() == 0) {
                    requestSuiFangType();
                } else {
                    pvOptions.setPicker(mCardItem);
                    pvOptions.show();
                }
            }
        });
        add_suif_fangan_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SuiFangFangAnActivity.class);
                startActivity(intent);
            }
        });
        if (!isAdd) {
            taskAdapter.setOnButtonClick(new SuiFangTaskListAdapter.onButtonClick() {
                @Override
                public void onButtonClick(int position, int type) {
                    PatientSuiFangListBean.ListBean.ProjectlistBean projectlistBean = mDatas.get
                            (position);
                    Intent intent = new Intent(mContext, SelectMySuiFangPlanListActivity.class);
                    intent.putExtra("taskOption", projectlistBean.optionlist.get(0).taskOption);
                    intent.putExtra("taskNum", position + 1);
                    intent.putExtra("info", (Serializable) projectlistBean.optionlist);
                    intent.putExtra("position", position);
                    intent.putExtra("patientID", patientID);
                    intent.putExtra("followRecordId", followRecordId);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * 删除随访记录
     */
    private void requestDelete() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("mobileType", Constants.MOBILE_TYPE);
        map.put("id", patientSuiFangListBean.id);
        DoctorNetService.requestAddOrChange(Constants.DELETE_FOLLOW_RECORD, map, new
                NetWorkRequestInterface() {


                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        Map<String, Object> map = new HashMap<>();
                        map.put(Constants.EVENTBUS_TYEPE, ADD_SUIFANG_INFO_SUCCESS);
                        EventBus.getDefault().post(map);
                        finish();
                    }
                });
    }

    private void showSelectDialog() {
        ActionSheet.createBuilder(mContext, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("终止")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        title_right_tv.setText("保存");
                        add_suif_end_time.setVisibility(View.VISIBLE);
                        add_suif_jieshu_detail_ll.setVisibility(View.VISIBLE);
                        add_suif_jieshu_detail.setVisibility(View.VISIBLE);
                        add_suif_end_time_ll.setVisibility(View.VISIBLE);
                    }
                })
                .show();
    }

    /**
     * 查询随访方式
     */
    private void requestSuiFangType() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        DoctorNetService.requestFangAnType(Constants.FOLLOW_TYPE_LIST, map, new
                NetWorkRequestInterface() {
                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                        ToastUtils.showMessage(mContext, "请求失败，请重试");
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        SuiFangTypeBean suiFangTypeBean = (SuiFangTypeBean) info;
                        mCardItem.clear();
                        for (SuiFangTypeBean.FollowVisitListBean followVisitListBean :
                                suiFangTypeBean.followVisitList) {
                            mCardItem.add(new CardBean(followVisitListBean.typeDetailCode,
                                    followVisitListBean.typeDetailName));
                        }
                        pvOptions.setPicker(mCardItem);
                        pvOptions.show();
                    }
                });
    }


    private void submit() {
        String patient_name = add_suif_patient_name.getText().toString().trim();
        String fangan_name = add_suif_fangan_name.getText().toString().trim();
        String suifang_type = add_suif_fangan_type.getText().toString().trim();
        String start_time = add_suif_start_time.getText().toString().trim();
        String chuangjian_time = add_suif_chuangjian_time.getText().toString().trim();
        if (isDoctorCreate) {
            if (TextUtils.isEmpty(patient_name)) {
                Toast.makeText(this, "请选择患者姓名", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (TextUtils.isEmpty(fangan_name)) {
            Toast.makeText(this, "请选择方案名称", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(suifang_type)) {
            Toast.makeText(this, "请选择随访方式", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (TextUtils.isEmpty(start_time)) {
//            Toast.makeText(this, "请选择开始时间", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(chuangjian_time)) {
//            Toast.makeText(this, "请选择创建时间", Toast.LENGTH_SHORT).show();
//            return;
//        }

        String detail = add_suif_detial.getText().toString().trim();
        if (TextUtils.isEmpty(detail)) {
            Toast.makeText(this, "请输入随访描述", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (isOver) {
////            String end_time = add_suif_end_time.getText().toString().trim();
////            if (TextUtils.isEmpty(end_time)) {
////                Toast.makeText(this, "请选择结束时间", Toast.LENGTH_SHORT).show();
////                return;
////            }
////            String endReson = add_suif_jieshu_detail.getText().toString().trim();
////            if (TextUtils.isEmpty(endReson)) {
////                Toast.makeText(this, "请输入结束原因", Toast.LENGTH_SHORT).show();
////                return;
////            }
//            /**
//             "mobileType":"2",
//             "id":"5",
//             "UserID":1,
//             "endTime":"2018-08-01 10:02:01",
//             "endReason":"12312",
//             "followMan":"123123"
//             }
//             */
////            Map<String, Object> map = new HashMap<>();
////            map.put("mobileType", Constants.MOBILE_TYPE);
////            map.put("id", patientSuiFangListBean.id);
////            map.put("endTime", end_time);
////            map.put("endReason", endReson);
////            map.put("userID", DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, ""));
////            map.put("followMan", DoctorBaseAppliction.spUtil.getString(Constants.Name, ""));
////            // 终止
////            requestStop(map);
//        } else {
        //提交

        Map<String, Object> map = new HashMap<>();
        map.put("patientID", patientID);
        map.put("mobileType", Constants.MOBILE_TYPE);
        map.put("followID", listBean.id + ""); // 方案ID
        map.put("followType", suifang_type_id); // 方式ID
        map.put("descript", detail);
        map.put("createUser", add_suif_chuangjian_name.getText().toString());
        requestAddSuiFang(map);
//        }

    }

    /**
     * 终止记录
     *
     * @param map
     */
    private void requestStop(Map<String, Object> map) {
        DoctorNetService.requestAddOrChangeCallErrorInfo(Constants.STOP_FOLLOW_RECORD, map, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                        ToastUtils.showMessage(mContext, "终止失败，请重试");
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();

                        BaseReturnBean bean = (BaseReturnBean) info;
                        if (DoctorNetService.isSuccess(bean.code)) {
                            ResultBean infoBean = JSONUtil.parseJsonToBean(bean.data, ResultBean
                                    .class);
                            Map<String, Object> map = new HashMap<>();
                            map.put(Constants.EVENTBUS_TYEPE, ADD_SUIFANG_INFO_SUCCESS);
                            EventBus.getDefault().post(map);
                            finish();
                        } else {
                            ToastUtils.showMessage(mContext, bean.data);
                        }
                    }
                });
    }

    /**
     * 添加随访
     *
     * @param map
     */
    private void requestAddSuiFang(Map<String, Object> map) {
        showWaitDialog();
        DoctorNetService.requestAddOrChangeCallErrorInfo(Constants.SAVE_FOLLOW_INFORMATION, map, new
                NetWorkRequestInterface() {
                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                        ToastUtils.showMessage(mContext, "添加失败，请重试");
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        BaseReturnBean bean = (BaseReturnBean) info;
                        if (DoctorNetService.isSuccess(bean.code)) {
                            ResultBean infoBean = JSONUtil.parseJsonToBean(bean.data, ResultBean
                                    .class);
                            Map<String, Object> map = new HashMap<>();
                            map.put(Constants.EVENTBUS_TYEPE, ADD_SUIFANG_INFO_SUCCESS);
                            EventBus.getDefault().post(map);
                            finish();
                        } else {
                            ToastUtils.showMessage(mContext, bean.data);
                        }
                    }
                });
    }

    /**
     * 初始化弹窗
     *
     * @param view 点击的哪个view
     */
    private List<CardBean> mCardItem;

    private void initOptionPicker(final TextView view) {//条件选择器初始化
        pvOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView
                .OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String tx = mCardItem.get(options1).cardItemName;
                suifang_type_id = mCardItem.get(options1).cardItemId;
                view.setText(tx);
            }
        })
                .setContentTextSize(20)//设置滚轮文字大小
                .setSelectOptions(0, 1)//默认选中项
                .setBgColor(Color.WHITE)
                .setCancelColor(getResources().getColor(R.color.main_color))
                .setSubmitColor(getResources().getColor(R.color.main_color))
                .setLineSpacingMultiplier(2.0f)//设置两横线之间的间隔倍数
                .setBackgroundId(0x66000000) //设置外部遮罩颜色
                .build();
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
        endDate.set(Integer.parseInt(yearTime), Integer.parseInt(monthTime) - 1, Integer
                .parseInt(dayTime));
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

    @Subscribe
    public void onEventMainThread(Map<String, Object> map) {
        String type = (String) map.get(Constants.EVENTBUS_TYEPE);
        if (type.equals(SuiFangFangAnActivity.SELECT_FANGAN_NAME)) {
            listBean = (SuiFangFangAnBean.ListBean) map.get(Constants
                    .EVENTBUS_VALUE);
            add_suif_fangan_name.setText(listBean.followName);
        } else if (type.equals(PatientListActivity.SELECT_PATIENT_INFO)) {
            PatientListBean.ListBean listBean = (PatientListBean.ListBean) map.get(Constants
                    .EVENTBUS_VALUE);
            patientID = listBean.id;
            add_suif_patient_name.setText(listBean.name);
        }
    }
}
