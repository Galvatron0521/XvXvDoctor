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
import cn.com.xxdoctor.activity.tag.TagManageActivity;
import cn.com.xxdoctor.adapter.MySubjectListAdapter;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.MySubjectListBean;
import cn.com.xxdoctor.bean.PatientInfoBean;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;

public class MySubjectListActivity extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private ListView my_subject_listview;
    private SpringView my_subject_spring;
    private TextView add_my_subject;
    private MySubjectListAdapter mySubjectListAdapter;
    private String patientID;
    private int pageCount = 10;
    private int pageNo = 0;
    private List<MySubjectListBean.ListBean> mDatas;
    private PatientInfoBean infoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subject_list);
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

        my_subject_listview = (ListView) findViewById(R.id.my_subject_listview);
        my_subject_spring = (SpringView) findViewById(R.id.my_subject_spring);
        add_my_subject = (TextView) findViewById(R.id.add_my_subject);

        title_back.setVisibility(View.VISIBLE);
        title_name.setText("我的课题");

        my_subject_spring.setHeader(new DefaultHeader(mContext));
        my_subject_spring.setFooter(new DefaultFooter(mContext));

    }

    @Override
    public void initData() {
        mDatas = new ArrayList<>();
        mySubjectListAdapter = new MySubjectListAdapter(mContext, mDatas);
        my_subject_listview.setAdapter(mySubjectListAdapter);
        requestMySubjectList();
    }

    /**
     * patientID":40,
     * "pageNo":"0",
     * "pageCount":"8"
     */
    private void requestMySubjectList() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("userID", DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, ""));
        map.put("pageNo", pageNo + "");
        map.put("pageCount", pageCount + "");
        DoctorNetService.requestMySubjectList(Constants.SELECT_SUBJECT_LIST, map,
                new NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();

                        MySubjectListBean bean = (MySubjectListBean) info;
                        mDatas.addAll(bean.list);
                        mySubjectListAdapter.notifyDataSetChanged();
                        my_subject_spring.onFinishFreshAndLoad();
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
        add_my_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddMySubjectActivity.class);
                intent.putExtra("isAdd", true);
                startActivity(intent);
            }
        });
        my_subject_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, AddMySubjectActivity.class);
                intent.putExtra("isAdd", false);
                intent.putExtra("id", mDatas.get(i).id);
                startActivity(intent);
            }
        });
        my_subject_spring.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                mDatas.clear();
                pageNo = 0;
                requestMySubjectList();
            }

            @Override
            public void onLoadmore() {
                pageNo = pageNo + pageCount;
                requestMySubjectList();
            }
        });
    }

    @Subscribe
    public void onEventMainThread(Map<String, Object> map) {
        String type = (String) map.get(Constants.EVENTBUS_TYEPE);
        if (type.equals(TagManageActivity.TAG_HAS_CHANGE)) {
            pageNo = 0;
            mDatas.clear();
            requestMySubjectList();
        }
    }
}
