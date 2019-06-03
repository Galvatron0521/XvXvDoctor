package cn.com.xxdoctor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

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
import cn.com.xxdoctor.activity.suiFangFangAn.AddSuiFangFangAnActivity;
import cn.com.xxdoctor.adapter.SuiFangFangAnListAdapter;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseFragment;
import cn.com.xxdoctor.bean.SuiFangFangAnBean;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;
import cn.com.xxdoctor.utils.ToastUtils;

/**
 * Created by chong.han on 2018/8/20.
 */

public class SuiFangFanganFragment extends DoctorBaseFragment {
    private ListView suifang_jilu_listview;
    private SpringView suifang_jilu_springview;
    private ImageButton suifang_jilu_add;
    private SuiFangFangAnListAdapter suiFangFangAnListAdapter;
    private List<SuiFangFangAnBean.ListBean> mDatas;
    private int pageCount = 10;
    private int pageNo = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.sufang_jilu_fragment, null);
        EventBus.getDefault().register(this);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        suifang_jilu_listview = (ListView) view.findViewById(R.id.suifang_jilu_listview);
        suifang_jilu_springview = (SpringView) view.findViewById(R.id.suifang_jilu_springview);

        suifang_jilu_springview.setHeader(new DefaultHeader(getContext()));
        suifang_jilu_springview.setFooter(new DefaultFooter(getContext()));

        suifang_jilu_add = (ImageButton) view.findViewById(R.id.suifang_jilu_add);
    }

    @Override
    public void initData() {
        mDatas = new ArrayList<>();
        suiFangFangAnListAdapter = new SuiFangFangAnListAdapter
                (getContext(), mDatas);
        suifang_jilu_listview.setAdapter(suiFangFangAnListAdapter);
        requestFangAnList();
    }

    @Override
    public void initListener() {

        //刷新
        suifang_jilu_springview.setListener(new SpringView.OnFreshListener() {
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
        //添加
        suifang_jilu_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddSuiFangFangAnActivity.class);
                intent.putExtra("isAdd", true);
                startActivity(intent);
            }
        });
        suifang_jilu_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SuiFangFangAnBean.ListBean listBean = mDatas.get(i);
                Intent intent = new Intent(getContext(), AddSuiFangFangAnActivity.class);
                intent.putExtra("isAdd", false);
                intent.putExtra("id", listBean.id);
                startActivity(intent);
            }
        });
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
                        ToastUtils.showMessage(getContext(), "查询失败");
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        suifang_jilu_springview.onFinishFreshAndLoad();
                        SuiFangFangAnBean suiFangFangAnBean = (SuiFangFangAnBean) info;
                        mDatas.addAll(suiFangFangAnBean.list);
                        suiFangFangAnListAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Subscribe
    public void onEventMainThread(Map<String, Object> map) {
        String type = (String) map.get(Constants.EVENTBUS_TYEPE);
        //新增或者删除患者刷新界面
        if (type.equals(AddSuiFangFangAnActivity.ADD_FANG_AN_INFO_SUCCESS)) {
            pageNo = 0;
            mDatas.clear();
            requestFangAnList();
        }
    }
}
