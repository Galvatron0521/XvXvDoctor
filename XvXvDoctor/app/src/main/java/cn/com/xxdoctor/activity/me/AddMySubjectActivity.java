package cn.com.xxdoctor.activity.me;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.activity.tag.TagManageActivity;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.CardBean;
import cn.com.xxdoctor.bean.InitSubjectBean;
import cn.com.xxdoctor.bean.MySubjectDetailBean;
import cn.com.xxdoctor.bean.NextSubjectBean;
import cn.com.xxdoctor.bean.SubjectConditionBean;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;
import cn.com.xxdoctor.utils.DateUtils;
import cn.com.xxdoctor.utils.HcUtils;
import cn.com.xxdoctor.utils.LogUtil;
import cn.com.xxdoctor.view.MyListView;
import cn.com.xxdoctor.view.WarpLinearLayout;

public class AddMySubjectActivity extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private EditText my_subject_name;
    private TextView my_subject_renqun_zhenduan;
    private LinearLayout my_subject_renqun_ll;
    private LinearLayout subject_tiaojian_ll;
    //    private TextView my_subject_renqun_bingzheng;
    private MyListView my_subject_tiaojian_list;
    private TextView my_subject_fang_an;
    private TextView my_subject_shouzhen_start_time;
    private TextView my_subject_shouzhen_end_time;
    private TextView my_subject_tiaojian_add;
    //    private WarpLinearLayout ru_xuan_container;
    private int index;
    private OptionsPickerView pvOptions;
    private InitSubjectBean initSubjectBean;
    private String fieldName;
    private String moduleCode;
    private String followPlanID = "";
    private SubjectConditionBean subjectConditionBean;
    private WarpLinearLayout ru_xuan_container;
    private boolean isAdd;
    private String id;
    private MySubjectDetailBean mySubjectDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_subject);
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        id = getIntent().getStringExtra("id");
        initView();
        initTimePicker();
        initData();
        initListener();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        my_subject_name = (EditText) findViewById(R.id.my_subject_name);
        my_subject_renqun_zhenduan = (TextView) findViewById(R.id.my_subject_renqun_zhenduan);
        my_subject_renqun_ll = (LinearLayout) findViewById(R.id.my_subject_renqun_ll);
        subject_tiaojian_ll = (LinearLayout) findViewById(R.id.subject_tiaojian_ll);
//        my_subject_renqun_bingzheng = (TextView) findViewById(R.id.my_subject_renqun_bingzheng);
        my_subject_tiaojian_list = (MyListView) findViewById(R.id.my_subject_tiaojian_list);
        my_subject_fang_an = (TextView) findViewById(R.id.my_subject_fang_an);
        my_subject_shouzhen_start_time = (TextView) findViewById(R.id
                .my_subject_shouzhen_start_time);
        my_subject_shouzhen_end_time = (TextView) findViewById(R.id.my_subject_shouzhen_end_time);
        my_subject_tiaojian_add = (TextView) findViewById(R.id.my_subject_tiaojian_add);

        title_back.setVisibility(View.VISIBLE);
        title_name.setText("课题设置");
        title_right_tv.setVisibility(View.VISIBLE);
        title_right_tv.setText("保存");

        String found_date = DateUtils.dateToString(new Date(System.currentTimeMillis()),
                DateUtils.FORMAT_5);
        my_subject_shouzhen_start_time.setText(found_date);
        my_subject_shouzhen_end_time.setText(found_date);
    }

    @Override
    public void initData() {
        subjectConditionBean = new SubjectConditionBean();
        subjectConditionBean.selectList = new ArrayList<>();
        mCardItem = new ArrayList<>();
        ru_xuan_container = getLinearContainer();
        my_subject_renqun_ll.addView(ru_xuan_container);
        requestRuXuanList();
        if (isAdd) {
            //初始化入选人群容器
            addRuXuanItem(ru_xuan_container);
        } else {
            requestDeital();
        }
    }

    private void requestDeital() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("mobileType", Constants.MOBILE_TYPE);
        DoctorNetService.requestSubjectDetail(Constants.SELECT_SUBJECT_FIELD_LIST, map, new
                NetWorkRequestInterface() {
                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        mySubjectDetailBean = (MySubjectDetailBean) info;
                        my_subject_name.setText(mySubjectDetailBean.subjectMap.subjectName);
                        my_subject_fang_an.setText(mySubjectDetailBean.subjectMap.planName);
                        my_subject_shouzhen_start_time.setText(DateUtils.dateToString(new Date
                                (mySubjectDetailBean.subjectMap.startTime), DateUtils.FORMAT_5));
                        my_subject_shouzhen_end_time.setText(DateUtils.dateToString(new Date
                                (mySubjectDetailBean.subjectMap.endTime), DateUtils.FORMAT_5));
                        followPlanID = mySubjectDetailBean.subjectMapAndroid.followPlanID;
                        for (int i = 0; i < mySubjectDetailBean.subjectMapAndroid.tiaojian.size()
                                ; i++) {
                            MySubjectDetailBean.SubjectMapAndroidBean.TiaojianBean selectListBean =
                                    mySubjectDetailBean.subjectMapAndroid.tiaojian.get(i);
                            if (i == 0) {
                                addRenQunView(selectListBean);
                            } else {
                                WarpLinearLayout tiaojian_container = getLinearContainer();
                                final ImageView imageView = new ImageView(mContext);
                                imageView.setImageResource(R.drawable.tingzhen_delete);

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                                        (ViewGroup
                                                .LayoutParams
                                                .WRAP_CONTENT, HcUtils.dp2px(mContext, 42));
                                int m1 = HcUtils.dp2px(mContext, 8);
                                params.setMargins(m1, m1, m1, m1);
                                imageView.setLayoutParams(params);
                                imageView.setScaleType(ImageView.ScaleType.CENTER);
                                tiaojian_container.addView(imageView);

                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        WarpLinearLayout parent = (WarpLinearLayout) imageView
                                                .getParent();
                                        int index = ((LinearLayout) parent.getParent())
                                                .indexOfChild(parent);
                                        subject_tiaojian_ll.removeView(parent);
                                    }
                                });
                                subject_tiaojian_ll.addView(tiaojian_container);
                                addTiaoJianView(selectListBean, tiaojian_container);
                            }
                        }
                    }
                });

    }

    /**
     * 详情增加条件
     *
     * @param selectListBean
     * @param tiaojian_container
     */
    private void addTiaoJianView(MySubjectDetailBean.SubjectMapAndroidBean.TiaojianBean
                                         selectListBean, WarpLinearLayout tiaojian_container) {
        for (int i = 0; i < selectListBean.child.size(); i++) {
            MySubjectDetailBean.SubjectMapAndroidBean.TiaojianBean.ChildBean childBean =
                    selectListBean.child.get(i);
            tiaojian_container.setTag(childBean.id);
            if (i == 0) {
                final TextView tv = getTextViewItem(childBean.condition);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initOptionPicker(tv);
                        mCardItem.clear();
                        mCardItem.add(new CardBean("and", "and"));
                        mCardItem.add(new CardBean("or", "or"));
                        mCardItem.add(new CardBean("not", "not"));
                        pvOptions.setPicker(mCardItem);
                        pvOptions.show();
                    }
                });
                tiaojian_container.addView(tv);
            }
            final TextView tv = getTextViewItem(childBean.displayName);
            requestNextList(childBean.moduleCode, childBean.fieldName, tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    index = ((ViewGroup) tv.getParent()).indexOfChild(tv);
                    initOptionPicker(tv);
                    if (index == 1) {
                        mCardItem.clear();
                        mCardItem.add(new CardBean("and", "and"));
                        mCardItem.add(new CardBean("or", "or"));
                        mCardItem.add(new CardBean("not", "not"));
                        pvOptions.setPicker(mCardItem);
                        pvOptions.show();
                    } else if (index == 2) {
                        mCardItem.clear();
                        for (InitSubjectBean.ModuleListBean moduleListBean : initSubjectBean
                                .moduleList) {
                            mCardItem.add(new CardBean(moduleListBean.moduleCode, moduleListBean
                                    .moduleName, "", moduleListBean.isHaveChild, ""));
                        }
                        pvOptions.setPicker(mCardItem);
                        pvOptions.show();
                    } else {
                        WarpLinearLayout parent = (WarpLinearLayout) tv.getParent();
                        TextView text = (TextView) parent.getChildAt(index - 1);
                        NextSubjectBean nextSubjectBean = (NextSubjectBean) text.getTag();
                        mCardItem.clear();
                        for (NextSubjectBean.FieldchildlistBean fieldchildlistBean :
                                nextSubjectBean
                                        .fieldchildlist) {
                            mCardItem.add(new CardBean(fieldchildlistBean.moduleCode,
                                    fieldchildlistBean
                                            .displayName, fieldchildlistBean.id,
                                    fieldchildlistBean
                                            .isHaveChild, fieldchildlistBean.fieldName));
                        }
                        if (mCardItem.size() > 0) {
                            pvOptions.setPicker(mCardItem);
                            pvOptions.show();
                        }
                    }
                }
            });
            tiaojian_container.addView(tv);
        }
    }

    /**
     * 详情增加人群
     *
     * @param selectListBean
     */

    private void addRenQunView(MySubjectDetailBean.SubjectMapAndroidBean.TiaojianBean
                                       selectListBean) {
        for (MySubjectDetailBean.SubjectMapAndroidBean.TiaojianBean.ChildBean childBean :
                selectListBean.child) {
            ru_xuan_container.setTag(childBean.id);
            final TextView tv = getTextViewItem(childBean.displayName);
            requestNextList(childBean.moduleCode, childBean.fieldName, tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    index = ((ViewGroup) tv.getParent()).indexOfChild(tv);
                    initOptionPicker(tv);
                    if (index == 0) {
                        mCardItem.clear();
                        for (InitSubjectBean.ModuleListBean moduleListBean : initSubjectBean
                                .moduleList) {
                            mCardItem.add(new CardBean(moduleListBean.moduleCode, moduleListBean
                                    .moduleName, "", moduleListBean.isHaveChild, ""));
                        }
                        pvOptions.setPicker(mCardItem);
                        pvOptions.show();
                    } else {
                        WarpLinearLayout parent = (WarpLinearLayout) tv.getParent();
                        TextView text = (TextView) parent.getChildAt(index - 1);
                        NextSubjectBean nextSubjectBean = (NextSubjectBean) text.getTag();
                        mCardItem.clear();
                        for (NextSubjectBean.FieldchildlistBean fieldchildlistBean : nextSubjectBean
                                .fieldchildlist) {
                            mCardItem.add(new CardBean(fieldchildlistBean.moduleCode,
                                    fieldchildlistBean
                                            .displayName, fieldchildlistBean.id, fieldchildlistBean
                                    .isHaveChild, fieldchildlistBean.fieldName));
                        }
                        if (mCardItem.size() > 0) {
                            pvOptions.setPicker(mCardItem);
                            pvOptions.show();
                        }
                    }
                }
            });
            ru_xuan_container.addView(tv);
        }
    }

    //{"id":17,"mobileType":"2"}
    private void requestRuXuanList() {
//        showWaitDialog();
        DoctorNetService.requestRuXuanList(Constants.SET_MY_SUBJECT_OPTION, new HashMap<String,
                Object>(), new NetWorkRequestInterface() {
            @Override
            public void onError(Throwable throwable) {
//                hideWaitDialog();
            }

            @Override
            public void onNext(Object info) {
//                hideWaitDialog();
                initSubjectBean = (InitSubjectBean) info;
            }
        });
    }

    private void addRuXuanItem(ViewGroup layout) {
        final TextView tv = getTextViewItem("请选择");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = ((ViewGroup) tv.getParent()).indexOfChild(tv);
                initOptionPicker(tv);
                if (index == 0) {
                    mCardItem.clear();
                    for (InitSubjectBean.ModuleListBean moduleListBean : initSubjectBean
                            .moduleList) {
                        mCardItem.add(new CardBean(moduleListBean.moduleCode, moduleListBean
                                .moduleName, "", moduleListBean.isHaveChild, ""));
                    }
                    pvOptions.setPicker(mCardItem);
                    pvOptions.show();
                } else {
//                    requestNextList(moduleCode, fieldName, view);
                    WarpLinearLayout parent = (WarpLinearLayout) tv.getParent();
                    TextView text = (TextView) parent.getChildAt(index - 1);
                    NextSubjectBean nextSubjectBean = (NextSubjectBean) text.getTag();
                    mCardItem.clear();
                    for (NextSubjectBean.FieldchildlistBean fieldchildlistBean : nextSubjectBean
                            .fieldchildlist) {
                        mCardItem.add(new CardBean(fieldchildlistBean.moduleCode,
                                fieldchildlistBean.displayName, fieldchildlistBean.id,
                                fieldchildlistBean.isHaveChild, fieldchildlistBean.fieldName));
                    }
                    if (mCardItem.size() > 0) {
                        pvOptions.setPicker(mCardItem);
                        pvOptions.show();
                    }
                }
            }
        });
        layout.addView(tv);
    }

    private void addSubjectTiaoJianItem(final ViewGroup layout) {
        final TextView tv = getTextViewItem("请选择");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = ((ViewGroup) tv.getParent()).indexOfChild(tv);
                initOptionPicker(tv);
                if (index == 1) {
                    mCardItem.clear();
                    mCardItem.add(new CardBean("and", "and"));
                    mCardItem.add(new CardBean("or", "or"));
                    mCardItem.add(new CardBean("not", "not"));
                    pvOptions.setPicker(mCardItem);
                    pvOptions.show();
                } else if (index == 2) {
                    mCardItem.clear();
                    for (InitSubjectBean.ModuleListBean moduleListBean : initSubjectBean
                            .moduleList) {
                        mCardItem.add(new CardBean(moduleListBean.moduleCode, moduleListBean
                                .moduleName, "", moduleListBean.isHaveChild, ""));
                    }
                    pvOptions.setPicker(mCardItem);
                    pvOptions.show();
                } else {
//                    requestNextList(moduleCode, fieldName, view);
                    WarpLinearLayout parent = (WarpLinearLayout) tv.getParent();
                    TextView text = (TextView) parent.getChildAt(index - 1);
                    NextSubjectBean nextSubjectBean = (NextSubjectBean) text.getTag();
                    mCardItem.clear();
                    for (NextSubjectBean.FieldchildlistBean fieldchildlistBean : nextSubjectBean
                            .fieldchildlist) {
                        mCardItem.add(new CardBean(fieldchildlistBean.moduleCode,
                                fieldchildlistBean
                                        .displayName, fieldchildlistBean.id, fieldchildlistBean
                                .isHaveChild, fieldchildlistBean.fieldName));
                    }
                    if (mCardItem.size() > 0) {
                        pvOptions.setPicker(mCardItem);
                        pvOptions.show();
                    }
                }
            }
        });
        layout.addView(tv);
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
                submit();
            }
        });
        my_subject_shouzhen_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show(my_subject_shouzhen_start_time);
            }
        });
        my_subject_shouzhen_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show(my_subject_shouzhen_end_time);
            }
        });
        my_subject_tiaojian_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mDatas.add("111");
//                addSubjectListAdapter.notifyDataSetChanged();
                WarpLinearLayout tiaojian_container = getLinearContainer();
                final ImageView imageView = new ImageView(mContext);
                imageView.setImageResource(R.drawable.tingzhen_delete);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup
                        .LayoutParams
                        .WRAP_CONTENT, HcUtils.dp2px(mContext, 42));
                int m1 = HcUtils.dp2px(mContext, 8);
                params.setMargins(m1, m1, m1, m1);
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                tiaojian_container.addView(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        WarpLinearLayout parent = (WarpLinearLayout) imageView.getParent();
                        int index = ((LinearLayout) parent.getParent()).indexOfChild(parent);
                        subject_tiaojian_ll.removeView(parent);
                    }
                });
                subject_tiaojian_ll.addView(tiaojian_container);
                addSubjectTiaoJianItem(tiaojian_container);
            }
        });
        my_subject_fang_an.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initOptionPicker(my_subject_fang_an);
                mCardItem.clear();
                if (initSubjectBean != null) {
                    if (initSubjectBean.planList != null && initSubjectBean.planList.size() > 0) {
                        for (InitSubjectBean.PlanListBean planListBean : initSubjectBean.planList) {
                            mCardItem.add(new CardBean(planListBean.id, planListBean.planName));
                        }
                    }
                }
                pvOptions.setPicker(mCardItem);
                pvOptions.show();
            }
        });
    }

    private void submit() {
        String name = my_subject_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入课题名称", Toast.LENGTH_SHORT).show();
            return;
        }
        subjectConditionBean.subjectName = name;
        subjectConditionBean.endTime = my_subject_shouzhen_end_time.getText().toString().trim();
        subjectConditionBean.startTime = my_subject_shouzhen_start_time.getText().toString().trim();
        subjectConditionBean.followPlanID = followPlanID;
        subjectConditionBean.mobileType = Constants.MOBILE_TYPE;
        subjectConditionBean.userID = DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, "");

        String fildId1 = (String) ru_xuan_container.getTag();
        SubjectConditionBean.SelectListBean bean = new SubjectConditionBean.SelectListBean
                ("", fildId1);
        LogUtil.e("fildId --- " + fildId1);
        subjectConditionBean.selectList.add(bean);
        int childCount = subject_tiaojian_ll.getChildCount();
        for (int i = 0; i < childCount; i++) {
            WarpLinearLayout warpLinearLayout = (WarpLinearLayout) subject_tiaojian_ll.getChildAt
                    (i);
            String fildId = (String) warpLinearLayout.getTag();
            TextView tex = (TextView) warpLinearLayout.getChildAt(1);
            SubjectConditionBean.SelectListBean bean2 = new SubjectConditionBean.SelectListBean
                    (tex.getText().toString(), fildId);
            LogUtil.e("SelectListBean --- " + bean2.toString());
            subjectConditionBean.selectList.add(bean2);
        }
        if (TextUtils.isEmpty(subjectConditionBean.followPlanID)) {
            Toast.makeText(this, "请选择随访方案", Toast.LENGTH_SHORT).show();
            return;
        }
        requestUpData();
    }

    /**
     * 请求上传数据
     * act=insertSubject&data={"subjectName":"我的课题111","followPlanID":"1",
     * "startTime":"2018-09-10", "mobileType":"2",
     * "endTime":"2018-09-12","userID":1,"selectList":[
     * {"condition":"","fieldID":"2"},
     * {"condition":"and","fieldID":"3"},
     * {"condition":"or","fieldID":"1"}
     * ]
     * }
     */
    private void requestUpData() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        if (!isAdd) {
            map.put("id", mySubjectDetailBean.subjectMapAndroid.id + "");
        }
        map.put("subjectName", subjectConditionBean.subjectName);
        map.put("followPlanID", subjectConditionBean.followPlanID);
        map.put("startTime", subjectConditionBean.startTime);
        map.put("mobileType", subjectConditionBean.mobileType);
        map.put("endTime", subjectConditionBean.endTime);
        map.put("userID", subjectConditionBean.userID);
        map.put("selectList", subjectConditionBean.selectList);
        DoctorNetService.requestAddOrChange(isAdd ? Constants.INSERT_SUBJECT : Constants
                .UPDATE_SUBJECT, map, new NetWorkRequestInterface() {

            @Override
            public void onError(Throwable throwable) {
                hideWaitDialog();
            }

            @Override
            public void onNext(Object info) {
                hideWaitDialog();
                Map<String, Object> map1 = new HashMap<>();
                map1.put(Constants.EVENTBUS_TYEPE, TagManageActivity.TAG_HAS_CHANGE);
                EventBus.getDefault().post(map1);
                finish();
            }
        });
    }

    /**
     * 获取TextView
     *
     * @return
     */
    public TextView getTextViewItem(String title) {
        TextView textView = new TextView(mContext);
        int m = HcUtils.dp2px(mContext, 6);
        textView.setPadding(m, m, m, m);
        textView.setBackgroundResource(R.drawable.fangan_bg);
        Drawable drawable = getResources().getDrawable(R.drawable.subject_arrow_bottom);// 找到资源图片
        // 这一步必须要做，否则不会显示。
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 设置图片宽高
        textView.setCompoundDrawables(null, null, drawable, null);// 设置到控件中
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, HcUtils.dp2px(mContext, 42));
        int m1 = HcUtils.dp2px(mContext, 8);
        params.setMargins(m1, m1, 0, 0);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextColor(Color.BLACK);
        textView.setText(title);
        return textView;
    }

    /**
     * 获取Linaer
     *
     * @return
     */
    public WarpLinearLayout getLinearContainer() {
        WarpLinearLayout fixGridLayout = new WarpLinearLayout(mContext);
        fixGridLayout.setGrivate(1);
        return fixGridLayout;
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
                String tx = mCardItem.get(options1).cardItemName;//moduleName
                if (view == my_subject_fang_an) {
                    followPlanID = mCardItem.get(options1).cardItemId;
                } else {
                    moduleCode = mCardItem.get(options1).cardItemId;
                    fieldName = mCardItem.get(options1).fieldName;
                    String isHaveChild = mCardItem.get(options1).isHaveChild;
                    WarpLinearLayout parent = (WarpLinearLayout) view.getParent();
                    int childCount = parent.getChildCount();
                    for (int i = childCount - 1; i > index; i--) {
                        parent.removeViewAt(i);
                    }
                    parent.setTag(mCardItem.get(options1).id);

                    if ("1".equals(isHaveChild)) {
                        requestNextList(moduleCode, fieldName, view);
                        //根据选项请求下级数据
                        addRuXuanItem(parent);
                    } else if ("and".equals(tx) || "or".equals(tx) || "not".equals(tx)) {
                        addSubjectTiaoJianItem(parent);
                    } else {
                        view.setTag(mCardItem.get(options1).id);
                    }
                }
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

    private void requestNextList(String moduleCode, String fieldName, final TextView view) {
//        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("moduleCode", moduleCode);
        map.put("fieldName", fieldName);
        DoctorNetService.requestNextList(Constants.GET_SUBJECT_OPTION_CONTENTS, map, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
//                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
//                        hideWaitDialog();
                        NextSubjectBean nextSubjectBean = (NextSubjectBean) info;
                        mCardItem.clear();
                        for (NextSubjectBean.FieldchildlistBean fieldchildlistBean :
                                nextSubjectBean.fieldchildlist) {
                            mCardItem.add(new CardBean(fieldchildlistBean.moduleCode,
                                    fieldchildlistBean
                                            .displayName, fieldchildlistBean.id, fieldchildlistBean
                                    .isHaveChild, fieldchildlistBean.fieldName));
                        }
                        view.setTag(nextSubjectBean);
                        pvOptions.setPicker(mCardItem);
                        pvOptions.show();
                    }
                });
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
        startDate.set(1988, 0, 1);
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
