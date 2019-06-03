package cn.com.xxdoctor.activity.suiFangFangAn;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.adapter.SuiFangFangAnListAdapter;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.bean.SuiFangFangAnBean;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;
import cn.com.xxdoctor.utils.ToastUtils;

public class SuiFangFangAnActivity extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private ListView suifang_fangan_listview;
    private SpringView suifang_fangan_springview;
    private int pageCount = 10;
    private int pageNo = 0;
    private List<SuiFangFangAnBean.ListBean> mDatas;
    private SuiFangFangAnListAdapter suiFangFangAnListAdapter;
    public static String SELECT_FANGAN_NAME = "select_fangan_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sui_fang_fang_an);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        suifang_fangan_listview = (ListView) findViewById(R.id.suifang_fangan_listview);
        suifang_fangan_springview = (SpringView) findViewById(R.id.suifang_fangan_springview);
        title_name.setText("随访方案");
        title_back.setVisibility(View.VISIBLE);


        suifang_fangan_springview.setHeader(new DefaultHeader(mContext));
        suifang_fangan_springview.setFooter(new DefaultFooter(mContext));
    }

    @Override
    public void initData() {
        mDatas = new ArrayList<>();
        suiFangFangAnListAdapter = new SuiFangFangAnListAdapter
                (mContext, mDatas);
        suifang_fangan_listview.setAdapter(suiFangFangAnListAdapter);
        requestFangAnList();
    }

    private void requestFangAnList() {
        showWaitDialog();
        /**
         *  "name":"",
         "pageNo":"0",
         "pageCount":"8"
         */
        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", pageNo);
        map.put("pageCount", pageCount);
        DoctorNetService.requestFangAnList(Constants.FOLLOW_LIST, map, new
                NetWorkRequestInterface() {


                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                        ToastUtils.showMessage(mContext, "查询失败");
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        suifang_fangan_springview.onFinishFreshAndLoad();
                        SuiFangFangAnBean suiFangFangAnBean = (SuiFangFangAnBean) info;
                        mDatas.addAll(suiFangFangAnBean.list);
                        suiFangFangAnListAdapter.notifyDataSetChanged();
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
        suifang_fangan_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SuiFangFangAnBean.ListBean listBean = mDatas.get(i);
                Map<String, Object> map = new HashMap<>();
                map.put(Constants.EVENTBUS_TYEPE, SELECT_FANGAN_NAME);
                map.put(Constants.EVENTBUS_VALUE, listBean);
                EventBus.getDefault().post(map);
                finish();
            }
        });
        suifang_fangan_springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                mDatas.clear();
                pageNo = 0;
                requestFangAnList();
            }

            @Override
            public void onLoadmore() {
                pageNo = pageNo + pageCount;
                requestFangAnList();
            }
        });
    }

}
