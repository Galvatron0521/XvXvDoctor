package cn.com.xxdoctor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import cn.com.xxdoctor.activity.bingChengGuanLi.BianZheng.AddBianZhengLunZhiActivity;
import cn.com.xxdoctor.activity.bingChengGuanLi.BingChengSelectActivity;
import cn.com.xxdoctor.activity.patientCenter.AddPatientInfoActivity;
import cn.com.xxdoctor.activity.patientCenter.PatientInfoActivity;
import cn.com.xxdoctor.activity.suiFangRecord.SuiFangListActivity;
import cn.com.xxdoctor.activity.main.SuiFangManageActivity;
import cn.com.xxdoctor.activity.tag.TagManageActivity;
import cn.com.xxdoctor.adapter.PatientListAdapter;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.base.DoctorBaseFragment;
import cn.com.xxdoctor.bean.PatientInfoBean;
import cn.com.xxdoctor.bean.PatientListBean;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;

/**
 * Created by chong.han on 2018/8/16.
 */

public class PatientListFragment extends DoctorBaseFragment {

    private TextView tv;
    private LinearLayout patient_head_layout;
    private ListView patient_listview;
    private ArrayAdapter adapter;
    private TextView patient_add;
    private TextView patient_gaoji_search;
    private TextView patient_tag;
    private ImageView patient_search_iv;
    private TextView patient_search_tv;
    private LinearLayout patient_search_ll;
    private EditText patient_search_et;
    private TextView patient_search_bt;
    private SpringView patient_springview;
    private boolean isTimePx; //排序方式标识 默认时间
    private int pageCount = 10;
    private int pageNo = 0;
    private String name = "";
    private String orderBy = "desc";//asc升序 desc降序
    private List<PatientListBean.ListBean> mDatas;
    private PatientListAdapter patientListAdapter;
    private String user_id;

    public static PatientListFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        PatientListFragment fragment = new PatientListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.patient_fragment, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        patient_listview = (ListView) view.findViewById(R.id.patient_listview);
        patient_add = (TextView) view.findViewById(R.id.patient_add);
        patient_gaoji_search = (TextView) view.findViewById(R.id.patient_gaoji_search);
        patient_tag = (TextView) view.findViewById(R.id.patient_tag);
        patient_search_iv = (ImageView) view.findViewById(R.id.patient_search_iv);
        patient_search_tv = (TextView) view.findViewById(R.id.patient_search_tv);
        patient_search_ll = (LinearLayout) view.findViewById(R.id.patient_search_ll);
        patient_search_et = (EditText) view.findViewById(R.id.patient_search_et);
        patient_search_bt = (TextView) view.findViewById(R.id.patient_search_bt);
        patient_springview = (SpringView) view.findViewById(R.id.patient_springview);

        patient_springview.setHeader(new DefaultHeader(getContext()));
        patient_springview.setFooter(new DefaultFooter(getContext()));

    }

    @Override
    public void initData() {
        isTimePx = true;
        mDatas = new ArrayList<>();
        user_id = DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, "");
        patientListAdapter = new PatientListAdapter(getContext(), mDatas, false);
        patient_listview.setAdapter(patientListAdapter);
        requestPatientList();
    }

    private void requestPatientList() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        /**
         * "pageNo":"0",
         "pageCount":"8",
         "userID":1,
         "name":"",
         "diseasesID":"",
         "orderBy":"desc"
         */
        map.put("pageNo", pageNo);
        map.put("pageCount", pageCount + "");
        map.put("userID", user_id);
        map.put("name", name);
        map.put("diseasesID", "");
        map.put("orderBy", orderBy);
        DoctorNetService.requestPatientList(Constants.PATIENTS_LIST, map, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        PatientListBean patientListBean = (PatientListBean) info;
//                        if (patientListBean.list.size() < pageCount) {
//                            ToastUtils.showMessage(getContext(), "已全部加载");
//                            pageNo = pageNo - pageCount;
//                        }
                        mDatas.addAll(patientListBean.list);
                        patientListAdapter.notifyDataSetChanged();
                        patient_springview.onFinishFreshAndLoad();
                    }
                });
    }

    @Override
    public void initListener() {
        //手动添加
        patient_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddPatientInfoActivity.class));
            }
        });
        patient_gaoji_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SuiFangManageActivity.class));
            }
        });
        // 搜索按钮
        patient_search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = patient_search_et.getText().toString().trim();
                pageNo = 0;
                mDatas.clear();
                requestPatientList();
            }
        });
        //标签分类
        patient_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), TagManageActivity.class));
            }
        });
        //排序方式
        patient_search_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTimePx) {
                    patient_search_iv.setImageResource(R.drawable.px_name);
                    patient_search_tv.setText("时间排序");
                    orderBy = "asc";
                } else {
                    patient_search_iv.setImageResource(R.drawable.px_time);
                    patient_search_tv.setText("时间排序");
                    orderBy = "desc";
                }
                isTimePx = !isTimePx;
                pageNo = 0;
                mDatas.clear();
                requestPatientList();
            }
        });
//        //list点击
//        patient_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getContext(), PatientInfoActivity.class);
//                intent.putExtra("patientID", mDatas.get(i).id);
//                intent.putExtra("create_time", mDatas.get(i).createTime + "");
//                startActivity(intent);
//            }
//        });
        patientListAdapter.setOnViewClickLisener(new PatientListAdapter.OnViewClickLisener() {
            @Override
            public void onClick(int position, int type) {
                //类型 0 头像  1 复诊  2 随访
                switch (type) {
                    case 0:
                        Intent intent1 = new Intent(getContext(), PatientInfoActivity.class);
                        intent1.putExtra("patientID", mDatas.get(position).id);
                        intent1.putExtra("create_time", mDatas.get(position).createTime + "");
                        startActivity(intent1);
                        break;
                    case 1:
                        Intent intent2 = new Intent(getContext(), BingChengSelectActivity.class);
                        intent2.putExtra("patientID", mDatas.get(position).id);
                        intent2.putExtra("recordFlag", "0");
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3 = new Intent(getContext(), SuiFangListActivity.class);
                        PatientInfoBean patientInfoBean = new PatientInfoBean();
                        patientInfoBean.patientDetails = new PatientInfoBean.PatientDetailsBean();
                        patientInfoBean.patientDetails.name = mDatas.get(position).name;
                        intent3.putExtra("patientID", mDatas.get(position).id);
                        intent3.putExtra("info", patientInfoBean);
                        startActivity(intent3);
                        break;
                }
            }
        });
        // 上拉下拉o
        patient_springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                //刷新
                pageNo = 0;
                mDatas.clear();
                requestPatientList();
            }

            @Override
            public void onLoadmore() {
                //加载更多
                pageNo = pageNo + pageCount;
                requestPatientList();
            }
        });
    }

    @Subscribe
    public void onEventMainThread(Map<String, Object> map) {
        String type = (String) map.get(Constants.EVENTBUS_TYEPE);
        //新增或者删除患者刷新界面 或者首诊结束刷新 首诊按钮状态
        if (type.equals(AddPatientInfoActivity.ADD_PATIENT_SUCCESS) || type.equals
                (PatientInfoActivity.DELETE_PATIENT_SUCCESS) || AddBianZhengLunZhiActivity
                .UPDATA_BINGZHENG_SUCCESS.equals(type)) {
            pageNo = 0;
            name = "";
            orderBy = "desc";
            mDatas.clear();
            requestPatientList();
        }
    }
}
