package cn.com.xxdoctor.activity.bingChengGuanLi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import cn.com.xxdoctor.activity.bingChengGuanLi.BianZheng.AddBianZhengLunZhiActivity;
import cn.com.xxdoctor.adapter.BingZhangListAdapter;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.BingZhengListBean;
import cn.com.xxdoctor.bean.PatientInfoBean;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;

public class BingChengManageListActivity extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private TextView bingcheng_add;
    private ListView bingcheng_listview;
    private SpringView bingcheng_springview;
    private String patientID;
    private int pageCount = 10;
    private int pageNo = 0;
    private List<BingZhengListBean.ListBean> mDatas;
    private BingZhangListAdapter bingZhangListAdapter;
    private PatientInfoBean infoBean;
    private String create_time;
    private TextView bingcheng_zhuyao_zhenduan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_cheng_manage);
        patientID = getIntent().getStringExtra("patientID");
        create_time = getIntent().getStringExtra("create_time");
        infoBean = (PatientInfoBean) getIntent().getSerializableExtra("info");
        EventBus.getDefault().register(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        bingcheng_add = (TextView) findViewById(R.id.bingcheng_add);
        bingcheng_zhuyao_zhenduan = (TextView) findViewById(R.id.bingcheng_zhuyao_zhenduan);
        bingcheng_listview = (ListView) findViewById(R.id.bingcheng_listview);
        bingcheng_springview = (SpringView) findViewById(R.id.bingcheng_springview);

        title_back.setVisibility(View.VISIBLE);
        title_name.setText("病程管理");

        bingcheng_springview.setHeader(new DefaultHeader(mContext));
        bingcheng_springview.setFooter(new DefaultFooter(mContext));

    }

    @Override
    public void initData() {
        mDatas = new ArrayList<>();
        bingZhangListAdapter = new BingZhangListAdapter(mContext, mDatas, this);
        bingcheng_listview.setAdapter(bingZhangListAdapter);
        requestBingZhengList();
    }

    /**
     * 请求病症列表
     */
    private void requestBingZhengList() {
        showWaitDialog();
        //"patientID":40,"pageNo":"0","pageCount":"8"
        Map<String, Object> map = new HashMap<>();
        map.put("patientID", patientID);
        map.put("pageNo", pageNo + "");
        map.put("pageCount", pageCount + "");
        DoctorNetService.requestBingZhengList(Constants.SELECT_FIELD_RECORD_SIGN_LIST, map, new
                NetWorkRequestInterface() {


                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        BingZhengListBean bingZhengListBean = (BingZhengListBean) info;
                        mDatas.addAll(bingZhengListBean.list);
                        if (bingZhengListBean.displayNameList.size() == 0) {
                            bingcheng_zhuyao_zhenduan.setText("暂无主要诊断");
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < bingZhengListBean.displayNameList.size(); i++) {
                                if (i == bingZhengListBean.displayNameList.size() - 1) {
                                    stringBuilder.append(bingZhengListBean.displayNameList.get(i)
                                            .displayName);
                                } else {
                                    stringBuilder.append(bingZhengListBean.displayNameList.get(i)
                                            .displayName + ",");
                                }
                            }
                            bingcheng_zhuyao_zhenduan.setText(stringBuilder.toString());
                        }

                        bingZhangListAdapter.notifyDataSetChanged();
                        bingcheng_springview.onFinishFreshAndLoad();
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
        bingcheng_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showSelect();
                Intent intent = new Intent(mContext, BingChengSelectActivity.class);
                intent.putExtra("patientID", patientID);
                if (mDatas.size() == 0) {
                    intent.putExtra("recordFlag", "1");
                } else {
                    intent.putExtra("recordFlag", "0");
                }
                startActivity(intent);
            }
        });
        bingZhangListAdapter.setOnItemClickListener(new BingZhangListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, BingChengSelectActivity.class);
                intent.putExtra("patientID", patientID);
                intent.putExtra("recordFlag", mDatas.get(position).recordFlag);
                intent.putExtra("sicknessTime", mDatas.get(position).fieldRecordDate);
                intent.putExtra("fieldRecordSign", mDatas.get(position).fieldRecordSign);
                startActivity(intent);
            }
        });
        bingcheng_springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                mDatas.clear();
                pageNo = 0;
                requestBingZhengList();
            }

            @Override
            public void onLoadmore() {
                pageNo = pageNo + pageCount;
                requestBingZhengList();
            }
        });
        bingZhangListAdapter.setOnDeleteClickListener(new BingZhangListAdapter
                .OnDeleteClickListener() {
            @Override
            public void onDeleteClick(final int position) {
                showAffirmDialog("提示", "确认删除该病程？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestDelete(position);
                    }
                });
            }
        });
    }

    /**
     * 删除病程
     *
     * @param position
     */
    private void requestDelete(int position) {
        showWaitDialog();
        Map<String,Object> map = new HashMap();
        map.put("fieldRecordSign", mDatas.get(position).fieldRecordSign);
        map.put("userID", DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, ""));
        map.put("mobileType", Constants.MOBILE_TYPE);
        DoctorNetService.requestAddOrChange(Constants.DELETE_COURSE_OF_DISEASE, map, new
                NetWorkRequestInterface() {
                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        pageNo = 0;
                        mDatas.clear();
                        requestBingZhengList();
                    }
                });
    }


    @Subscribe
    public void onEventMainThread(Map<String, Object> map) {
        String type = (String) map.get(Constants.EVENTBUS_TYEPE);
        if (AddBianZhengLunZhiActivity.UPDATA_BINGZHENG_SUCCESS.equals(type)) {
            pageNo = 0;
            mDatas.clear();
            requestBingZhengList();
        }
    }
}
