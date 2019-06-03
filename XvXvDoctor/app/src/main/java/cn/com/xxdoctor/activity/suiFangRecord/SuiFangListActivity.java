package cn.com.xxdoctor.activity.suiFangRecord;

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
import cn.com.xxdoctor.adapter.SuiFangListAdapter;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.bean.PatientSuiFangListBean;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;

public class SuiFangListActivity extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private ListView suifang_listview;
    private SpringView suifang_spring;
    private TextView suifang_add;
    private SuiFangListAdapter suiFangListAdapter;
    private String patientID;
    private int pageCount = 10;
    private int pageNo = 0;
    private List<PatientSuiFangListBean.ListBean> mDatas;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sui_fang_list);
        EventBus.getDefault().register(this);
        patientID = getIntent().getStringExtra("patientID");
        name =  getIntent().getStringExtra("name");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        suifang_listview = (ListView) findViewById(R.id.suifang_listview);
        suifang_spring = (SpringView) findViewById(R.id.suifang_spring);
        suifang_add = (TextView) findViewById(R.id.suifang_add);

        title_back.setVisibility(View.VISIBLE);
        title_name.setText("随访记录");

        suifang_spring.setHeader(new DefaultHeader(mContext));
        suifang_spring.setFooter(new DefaultFooter(mContext));
    }

    @Override
    public void initData() {
        mDatas = new ArrayList<>();
        suiFangListAdapter = new SuiFangListAdapter(mContext, mDatas);
        suifang_listview.setAdapter(suiFangListAdapter);
        requestSuiFangList();
    }

    /**
     * patientID":40,
     * "pageNo":"0",
     * "pageCount":"8"
     */
    private void requestSuiFangList() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("patientID", patientID);
        map.put("pageNo", pageNo + "");
        map.put("pageCount", pageCount + "");
        DoctorNetService.requestPatientSuiFangList(Constants.SELECT_FOLLOW_PATIENT_NEW_LIST, map,
                new
                        NetWorkRequestInterface() {

                            @Override
                            public void onError(Throwable throwable) {
                                hideWaitDialog();
                            }

                            @Override
                            public void onNext(Object info) {
                                hideWaitDialog();
                                /**
                                 name:姓名
                                 followName:随访方案名称
                                 patientID:患者id
                                 followID": 81,
                                 followType:随访方式
                                 startTime:开始时间
                                 descript:描述
                                 endTime:结束时间
                                 endReason:原因
                                 followMan:终止人
                                 */
                                PatientSuiFangListBean bean = (PatientSuiFangListBean) info;
                                mDatas.addAll(bean.list);
                                suiFangListAdapter.notifyDataSetChanged();
                                suifang_spring.onFinishFreshAndLoad();
//                        if (bean.list.size() < pageCount) {
//                            pageNo = pageNo - pageCount;
//                        }
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
        suifang_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddSuiFangJiluActivity.class);
                intent.putExtra("isAdd", true);
                intent.putExtra("name", name);
                intent.putExtra("patientID", patientID);
                startActivity(intent);
            }
        });
        suifang_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = mDatas.get(i).id;
                Intent intent = new Intent(mContext, AddSuiFangJiluActivity.class);
                intent.putExtra("isAdd", false);
                intent.putExtra("patientID", patientID);
                intent.putExtra("followRecordId", id);
                intent.putExtra("PatientSuiFangListBean", mDatas.get(i));
                startActivity(intent);
            }
        });
        suifang_spring.setListener(new SpringView.OnFreshListener() {
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
        if (type.equals(AddSuiFangJiluActivity.ADD_SUIFANG_INFO_SUCCESS)) {
            mDatas.clear();
            pageNo = 0;
            requestSuiFangList();
        }
    }
}
