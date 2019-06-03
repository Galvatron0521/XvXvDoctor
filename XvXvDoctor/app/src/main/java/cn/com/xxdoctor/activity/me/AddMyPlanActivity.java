package cn.com.xxdoctor.activity.me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.adapter.AddMyPlanListAdapter;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.CardBean;
import cn.com.xxdoctor.bean.ContentBean;
import cn.com.xxdoctor.bean.MyPlanListBean;
import cn.com.xxdoctor.bean.ResultBean;
import cn.com.xxdoctor.chat.utils.ToastUtil;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;
import cn.com.xxdoctor.utils.ToastUtils;
import cn.com.xxdoctor.view.MyListView;

public class AddMyPlanActivity extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private EditText my_plan_name;
    private TextView my_subject_renqun_zhenduan;
    private TextView my_plan_jizhun_time;
    private MyListView my_plan_task_list;
    private Switch my_plan_switch;
    private ImageView my_plan_add_task;
    private List<MyPlanListBean.FollowlistBean.ProjectlistBean> mDatas;
    private AddMyPlanListAdapter addMyPlanListAdapter;
    private MyPlanListBean.FollowlistBean bean;
    private boolean isAdd;
    private OptionsPickerView pvOptions;
    private List<CardBean> items1;
    private List<List<CardBean>> items2;
    private List<List<List<CardBean>>> items3;
    private MyPlanListBean.FollowlistBean.ProjectlistBean currentSelectBean;
    private int currentSelectPosition;
    public static String CHANG_FOLLOW_SUCCESS = "Chang_Follow_Success";
    private String followprojectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_plan);
        EventBus.getDefault().register(this);
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        followprojectID = getIntent().getStringExtra("followprojectID");
        bean = (MyPlanListBean.FollowlistBean) getIntent().getSerializableExtra("info");
        initView();
        initData();
        initListener();
    }

    @Override
    public void initData() {
        mDatas = new ArrayList<>();
        mCardItem = new ArrayList<>();
        addMyPlanListAdapter = new AddMyPlanListAdapter(mDatas, mContext);
        my_plan_task_list.setAdapter(addMyPlanListAdapter);

        if (isAdd) {
            title_right_tv.setText("保存");
            bean = new MyPlanListBean.FollowlistBean();
            MyPlanListBean.FollowlistBean.ProjectlistBean projectlistBean = new MyPlanListBean
                    .FollowlistBean.ProjectlistBean();
            projectlistBean.project = new MyPlanListBean.FollowlistBean.ProjectlistBean
                    .ProjectBean();
            projectlistBean.project.scope = "0";
            mDatas.add(projectlistBean);
        } else {
            title_right_tv.setText("删除");
            my_plan_name.setText(bean.planName);
            my_plan_jizhun_time.setText("1".equals(bean.baseDate) ? "首诊日期" : "其他");
            mDatas.addAll(bean.projectlist);
            if (bean.projectlist != null) {
                if (bean.projectlist.size() > 0) {
                    if (bean.projectlist.get(0).project.optionlist != null && bean.projectlist
                            .get(0).project.optionlist.size() > 0) {
                        for (MyPlanListBean.FollowlistBean.ProjectlistBean projectlistBean :
                                bean.projectlist) {
                            for (MyPlanListBean.FollowlistBean.ProjectlistBean.ProjectBean
                                    .OptionlistBean
                                    optionlistBean : projectlistBean.project.optionlist) {
                                if (optionlistBean.taskOption.equals("0")) {
                                    projectlistBean.project.task_name = "量化指标";
                                } else {
                                    projectlistBean.project.task_name2 = "量表";
                                }
                            }
                        }
                    }
                }
            }
            addMyPlanListAdapter.notifyDataSetChanged();
            changeViewState();
        }
        initPickerData();
    }

    /**
     * 改变view状态为不可编辑
     */
    private void changeViewState() {
        my_plan_name.setFocusable(false);
        my_plan_jizhun_time.setEnabled(false);
        my_plan_switch.setEnabled(false);
    }

    private void initPickerData() {
        items1 = new ArrayList<>();
        List<CardBean> tempItems2 = new ArrayList<>();
        items2 = new ArrayList<>();
        items3 = new ArrayList<>();
        List<CardBean> tempItems3 = new ArrayList<>();
        List<List<CardBean>> tempItems33 = new ArrayList<>();
        items1.add(new CardBean("前", "前"));
        items1.add(new CardBean("后", "后"));
        for (int i = 0; i <= 30; i++) {
            tempItems2.add(new CardBean((i) + "", (i) + ""));
        }
        items2.add(tempItems2);
        items2.add(tempItems2);

        tempItems3.add(new CardBean("天", "天"));
        tempItems3.add(new CardBean("周", "周"));
        tempItems3.add(new CardBean("月", "月"));
        for (int i = 0; i <= 30; i++) {
            tempItems33.add(tempItems3);
            items3.add(tempItems33);
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
                if (isAdd) {
                    submit();
                } else {
                    showAffirmDialog("提示", "确认删除改随访方案？", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            delectProject();
                        }
                    });
                }
            }
        });
        addMyPlanListAdapter.setOnButtonClick(new AddMyPlanListAdapter.onButtonClick() {
            @Override
            public void onButtonClick(int position, int type) {
                //         * @param type     0 删除 1 添加任务 2 时间选择
                currentSelectBean = mDatas.get(position);
                currentSelectPosition = position;
                switch (type) {
                    case 0:
                        if (!isAdd) return;
                        mDatas.remove(position);
                        addMyPlanListAdapter.notifyDataSetChanged();
                        break;
                    case 1: //0  量化指标
                        if (!isAdd) {
                            if (!TextUtils.isEmpty(currentSelectBean.project.task_name)) {
                                Intent intent = new Intent(mContext, SelectMyPlanListActivity
                                        .class);
                                intent.putExtra("taskOption", "0");
                                List<MyPlanListBean.FollowlistBean.ProjectlistBean.ProjectBean
                                        .OptionlistBean> tempList = new ArrayList<>();
                                for (MyPlanListBean.FollowlistBean.ProjectlistBean.ProjectBean
                                        .OptionlistBean optionlistBean : currentSelectBean
                                        .project.optionlist) {
                                    if (optionlistBean.taskOption.equals("0")){
                                        tempList.add(optionlistBean);
                                    }
                                }
                                intent.putExtra("info", (Serializable) tempList);
                                intent.putExtra("position", currentSelectPosition);
                                intent.putExtra("followprojectID", followprojectID);
                                startActivity(intent);
                            }else {
                                ToastUtils.showMessage(mContext,"无量化指标任务项目");
                            }
                        } else {
                            if (TextUtils.isEmpty(currentSelectBean.project.beforeOrAfter) ||
                                    TextUtils
                                            .isEmpty(currentSelectBean.project.unit)) {
                                ToastUtil.shortToast(mContext, "请选择任务执行时间");
                                return;
                            }
                            MyPlanListBean.FollowlistBean.ProjectlistBean projectlistBean =
                                    mDatas.get(currentSelectPosition);
                            projectlistBean.project.task_name = "量化指标";
                            Intent intent = new Intent(mContext, SelectMyPlanListActivity.class);
                            intent.putExtra("taskOption", "0");
                            intent.putExtra("info", (Serializable) projectlistBean.project
                                    .optionlist);
                            intent.putExtra("position", currentSelectPosition);
                            startActivity(intent);
                            addMyPlanListAdapter.notifyDataSetChanged();
                        }
                        break;
                    case 2:
                        if (!isAdd) return;
                        if (TextUtils.isEmpty(my_plan_jizhun_time.getText().toString().trim())) {
                            ToastUtil.shortToast(mContext, "请选择随访基准时间");
                            return;
                        }
                        initOptionPicker(new TextView(mContext));
                        pvOptions.setPicker(items1, items2, items3);
                        pvOptions.show();
                        break;
                    case 3: //1  量表
                        if (!isAdd) {
                            if (!TextUtils.isEmpty(currentSelectBean.project.task_name2)) {
                                Intent intent = new Intent(mContext, SelectMyPlanListActivity
                                        .class);
                                intent.putExtra("taskOption", "1");
                                List<MyPlanListBean.FollowlistBean.ProjectlistBean.ProjectBean
                                        .OptionlistBean> tempList = new ArrayList<>();
                                for (MyPlanListBean.FollowlistBean.ProjectlistBean.ProjectBean
                                        .OptionlistBean optionlistBean : currentSelectBean
                                        .project.optionlist) {
                                    if (optionlistBean.taskOption.equals("1")){
                                        tempList.add(optionlistBean);
                                    }
                                }
                                intent.putExtra("info", (Serializable) tempList);
                                intent.putExtra("position", currentSelectPosition);
                                intent.putExtra("followprojectID", followprojectID);
                                startActivity(intent);
                            }else {
                                ToastUtils.showMessage(mContext,"无量表任务项目");
                            }
                        } else {
                            if (TextUtils.isEmpty(currentSelectBean.project.beforeOrAfter) ||
                                    TextUtils
                                            .isEmpty(currentSelectBean.project.unit)) {
                                ToastUtil.shortToast(mContext, "请选择任务执行时间");
                                return;
                            }
                            MyPlanListBean.FollowlistBean.ProjectlistBean projectlistBean =
                                    mDatas.get(currentSelectPosition);
                            projectlistBean.project.task_name2 = "量表";
                            Intent intent = new Intent(mContext, SelectMyPlanListActivity.class);
                            intent.putExtra("taskOption", "1");
                            intent.putExtra("info", (Serializable) projectlistBean.project
                                    .optionlist);
                            intent.putExtra("position", currentSelectPosition);
                            startActivity(intent);
                            addMyPlanListAdapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
        });
        my_plan_jizhun_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initOptionPicker(my_plan_jizhun_time);
//                if (mCardItem.size() == 0) {
                requestContent();
//                } else {
//                    pvOptions.setPicker(mCardItem);
//                    pvOptions.show();
//            }
            }
        });
        my_plan_add_task.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                MyPlanListBean.FollowlistBean.ProjectlistBean projectlistBean = new MyPlanListBean
                        .FollowlistBean.ProjectlistBean();
                projectlistBean.project = new MyPlanListBean.FollowlistBean.ProjectlistBean
                        .ProjectBean();
                projectlistBean.project.scope = "0";
                mDatas.add(projectlistBean);
                addMyPlanListAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 删除方案
     * "followPlanID":"4028801e65eaba910165eaba91840000","mobileType":"2","userID":1
     */
    private void delectProject() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("followPlanID", followprojectID);
        map.put("mobileType", Constants.MOBILE_TYPE);
        map.put("userID", DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, ""));
        DoctorNetService.requestAddOrChange(Constants.DELETE_FOLLOW_PLAN, map, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                        ToastUtil.shortToast(mContext, "删除失败，请重试");
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        ResultBean bean = (ResultBean) info;
                        ToastUtil.shortToast(mContext, bean.data);
                        Map<String, Object> map = new HashMap<>();
                        map.put(Constants.EVENTBUS_TYEPE, CHANG_FOLLOW_SUCCESS);
                        EventBus.getDefault().post(map);
                        finish();
                    }
                });
    }

    /**
     * 请求数据字典
     */
    private void requestContent() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("typeCode", "followBaseDate");
        DoctorNetService.requestContent(Constants.SELECT_CONTENT, map, new
                NetWorkRequestInterface() {


                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        ContentBean contentBean = (ContentBean) info;
                        mCardItem.clear();
                        if (contentBean.selectList != null && contentBean.selectList.size() > 0) {
                            for (ContentBean.SelectListBean mBean : contentBean.selectList) {
                                CardBean bean = new CardBean(mBean.typeDetailCode, mBean
                                        .typeDetailName);
                                mCardItem.add(bean);
                            }
                            pvOptions.setPicker(mCardItem);
                            pvOptions.show();
                        }
                    }
                });
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        my_plan_name = (EditText) findViewById(R.id.my_plan_name);
        my_subject_renqun_zhenduan = (TextView) findViewById(R.id.my_subject_renqun_zhenduan);
        my_plan_jizhun_time = (TextView) findViewById(R.id.my_plan_jizhun_time);
        my_plan_task_list = (MyListView) findViewById(R.id.my_plan_task_list);
        my_plan_switch = (Switch) findViewById(R.id.my_plan_switch);
        my_plan_add_task = (ImageView) findViewById(R.id.my_plan_add_task);

        title_back.setVisibility(View.VISIBLE);
        title_right_tv.setVisibility(View.VISIBLE);
        title_name.setText("新建随访方案");
    }

    private void submit() {
        // validate
        String name = my_plan_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入随访方案名称", Toast.LENGTH_SHORT).show();
            return;
        }
        String jizhun_time = my_plan_jizhun_time.getText().toString().trim();
        if (TextUtils.isEmpty(jizhun_time)) {
            Toast.makeText(this, "请选择随访基准时间", Toast.LENGTH_SHORT).show();
            return;
        }
        bean.planName = name;
        bean.projectlist = new ArrayList<>();
        bean.projectlist.addAll(mDatas);
        for (int i = 0; i < bean.projectlist.size(); i++) {
            MyPlanListBean.FollowlistBean.ProjectlistBean projectlistBean = bean.projectlist.get(i);
            projectlistBean.project.taskNum = (i + 1) + "";
        }
        requestUpData();
    }

    /**
     * 提交数据
     */
    private void requestUpData() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("follow", bean);
        map.put("mobileType", Constants.MOBILE_TYPE);
        map.put("userID", DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, ""));
        map.put("createUser", DoctorBaseAppliction.spUtil.getString(Constants.Name, ""));
        DoctorNetService.requestAddOrChange(Constants.INSERT_FOLLOW_PLAN, map, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                        ToastUtil.shortToast(mContext, "保存失败，请重试");
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        ResultBean bean = (ResultBean) info;
                        ToastUtil.shortToast(mContext, bean.data);

                        Map<String, Object> map = new HashMap<>();
                        map.put(Constants.EVENTBUS_TYEPE, CHANG_FOLLOW_SUCCESS);
                        EventBus.getDefault().post(map);
                        finish();
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
                if (my_plan_jizhun_time == view) {
                    String tx = mCardItem.get(options1).cardItemName;
                    String id = mCardItem.get(options1).cardItemId;
                    bean.baseDate = id;
                    view.setText(tx);
                } else if (title_right_tv == view) {
                    MyPlanListBean.FollowlistBean.ProjectlistBean projectlistBean = mDatas.get
                            (currentSelectPosition);
                    projectlistBean.project.task_name = mCardItem.get(options1)
                            .cardItemName;
                    Intent intent = new Intent(mContext, SelectMyPlanListActivity.class);
                    intent.putExtra("taskOption", mCardItem.get(options1).cardItemId);
                    intent.putExtra("info", (Serializable) projectlistBean.project.optionlist);
                    intent.putExtra("position", currentSelectPosition);
                    startActivity(intent);
                    addMyPlanListAdapter.notifyDataSetChanged();
                } else {
                    mDatas.get(currentSelectPosition).project.unit = items3.get(0).get(0).get
                            (options3)
                            .cardItemName;
                    mDatas.get(currentSelectPosition).project.amount = items2.get(0).get(options2)
                            .cardItemName;
                    mDatas.get(currentSelectPosition).project.beforeOrAfter = items1.get(options1)
                            .cardItemName;
                    addMyPlanListAdapter.notifyDataSetChanged();
                }
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

    @Subscribe
    public void onEventMainThread(Map<String, Object> map) {
        String type = (String) map.get(Constants.EVENTBUS_TYEPE);
        if (type.equals(SelectMyPlanListActivity.MY_PLAN_SELECT)) {
            String result = (String) map.get(Constants.EVENTBUS_VALUE);
            int position = (int) map.get("position");
            String taskOption = (String) map.get("taskOption");
            MyPlanListBean.FollowlistBean.ProjectlistBean projectlistBean = mDatas.get(position);
            if (projectlistBean.project.optionlist == null) {
                projectlistBean.project.optionlist = new ArrayList<>();
            }
            if (!TextUtils.isEmpty(result)) {
                String[] split = result.split(",");
                for (String s : split) {
                    if (!projectlistBean.project.optionlist.contains(s)) {
                        projectlistBean.project.optionlist.add(new MyPlanListBean.FollowlistBean
                                .ProjectlistBean.ProjectBean.OptionlistBean(taskOption, s));
                    }
                }
            }
            mDatas.remove(position);
            mDatas.add(position, projectlistBean);
            addMyPlanListAdapter.notifyDataSetChanged();
        }
    }
}
