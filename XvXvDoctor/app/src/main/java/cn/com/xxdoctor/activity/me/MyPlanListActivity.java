package cn.com.xxdoctor.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.activity.suiFangRecord.AddSuiFangJiluActivity;
import cn.com.xxdoctor.activity.tag.TagManageActivity;
import cn.com.xxdoctor.adapter.MyPlanListAdapter;
import cn.com.xxdoctor.adapter.SuiFangListAdapter;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.MyPlanListBean;
import cn.com.xxdoctor.bean.PatientInfoBean;
import cn.com.xxdoctor.bean.PatientSuiFangListBean;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;

public class MyPlanListActivity extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private ListView my_plan_listview;
    private SpringView my_plan_spring;
    private TextView add_my_plan;
    private MyPlanListAdapter suiFangListAdapter;
    private String patientID;
    private int pageCount = 10;
    private int pageNo = 0;
    private List<MyPlanListBean.FollowlistBean> mDatas;
    private PatientInfoBean infoBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plan_list);
        EventBus.getDefault().register(this);
        patientID = getIntent().getStringExtra("patientID");
        infoBean = (PatientInfoBean) getIntent().getSerializableExtra("info");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);

        my_plan_listview = (ListView) findViewById(R.id.my_plan_listview);
        my_plan_spring = (SpringView) findViewById(R.id.my_plan_spring);
        add_my_plan = (TextView) findViewById(R.id.add_my_plan);

        title_back.setVisibility(View.VISIBLE);
        title_name.setText("我的随访方案");

        my_plan_spring.setHeader(new DefaultHeader(mContext));
        my_plan_spring.setFooter(new DefaultFooter(mContext));


    }

    @Override
    public void initData() {
        mDatas = new ArrayList<>();
        suiFangListAdapter = new MyPlanListAdapter(mContext, mDatas);
        my_plan_listview.setAdapter(suiFangListAdapter);
        requestSuiFangList();
    }

    /**
     * patientID":40,
     * "pageNo":"0",
     * "pageCount":"8"
     */
    private void requestSuiFangList() {
        showWaitDialog();
//        "pageNo":"0", "pageCount":"8","userID":
        Map<String, Object> map = new HashMap<>();
//        map.put("userID", DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, ""));
//        map.put("userID", DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, ""));
        map.put("pageNo", pageNo + "");
        map.put("pageCount", pageCount + "");
        DoctorNetService.requestMyPlanList(Constants.SELECT_FOLLOW_PLAN_LIST, map,
                new NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        MyPlanListBean bean = (MyPlanListBean) info;
                        mDatas.addAll(bean.followlist);
                        suiFangListAdapter.notifyDataSetChanged();
                        my_plan_spring.onFinishFreshAndLoad();
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
        add_my_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddMyPlanActivity.class);
                startActivity(intent);
            }
        });
        my_plan_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyPlanListBean.FollowlistBean bean = mDatas.get(i);
                MyPlanListBean.FollowlistBean followlistBean = new MyPlanListBean.FollowlistBean();
                followlistBean.planName = bean.planName;
                followlistBean.baseDate = bean.baseDate;
                followlistBean.projectlist = new ArrayList<>();
                for (MyPlanListBean.FollowlistBean.ProjectlistBean projectlistBean : bean
                        .projectlist) {
                    MyPlanListBean.FollowlistBean.ProjectlistBean projectBean = new
                            MyPlanListBean.FollowlistBean.ProjectlistBean();
                    projectBean.project = new MyPlanListBean.FollowlistBean.ProjectlistBean
                            .ProjectBean();
                    projectBean.project.taskNum = projectlistBean.taskNum;
                    projectBean.project.beforeOrAfter = projectlistBean.beforeOrAfter;
                    projectBean.project.scope = projectlistBean.scope;
                    projectBean.project.amount = projectlistBean.amount;
                    projectBean.project.unit = projectlistBean.unit;
                    projectBean.project.followprojectID = projectlistBean.followprojectID;
                    projectBean.project.optionlist = new ArrayList<>();
                    for (MyPlanListBean.FollowlistBean.ProjectlistBean.OptionlistBean
                            optionlistBean : projectlistBean.optionlist) {
                        MyPlanListBean.FollowlistBean.ProjectlistBean.ProjectBean.OptionlistBean
                                bean1 = new MyPlanListBean.FollowlistBean.ProjectlistBean
                                .ProjectBean.OptionlistBean(optionlistBean.taskOption,
                                optionlistBean.optionModuleCodes);
                        projectBean.project.optionlist.add(bean1);
                    }
                    followlistBean.projectlist.add(projectBean);
                }

                Intent intent = new Intent(mContext, AddMyPlanActivity.class);
                intent.putExtra("isAdd", false);
                intent.putExtra("followprojectID", bean.id);
                intent.putExtra("info", followlistBean);
                startActivity(intent);
            }
        });
        my_plan_spring.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                mDatas.clear();
                pageNo = 0;
                requestSuiFangList();
            }

            @Override
            public void onLoadmore() {
                pageNo = pageNo + pageCount;
                requestSuiFangList();
            }
        });
    }

    @Subscribe
    public void onEventMainThread(Map<String, Object> map) {
        String type = (String) map.get(Constants.EVENTBUS_TYEPE);
        if (type.equals(AddMyPlanActivity.CHANG_FOLLOW_SUCCESS)) {
            pageNo = 0;
            mDatas.clear();
            requestSuiFangList();
        }
    }
}
