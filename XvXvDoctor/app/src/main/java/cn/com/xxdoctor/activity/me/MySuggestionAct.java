package cn.com.xxdoctor.activity.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mylhyl.circledialog.view.listener.OnInputClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.adapter.MySuggestionListAdapter;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.MySuggestionListBean;
import cn.com.xxdoctor.chat.utils.ToastUtil;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;

/**
 * Created by chong.han on 2018/10/16.
 */

public class MySuggestionAct extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private ListView my_suggestion_listview;
    private List<MySuggestionListBean.ListBean> mDatas;
    private MySuggestionListAdapter mySuggestionListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_suggestion);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        my_suggestion_listview = (ListView) findViewById(R.id.my_suggestion_listview);

        title_back.setVisibility(View.VISIBLE);
        title_name.setText("我的意见");
        title_right_tv.setText("添加");
        title_right_tv.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        mDatas = new ArrayList<>();
        mySuggestionListAdapter = new MySuggestionListAdapter(mContext, mDatas);
        my_suggestion_listview.setAdapter(mySuggestionListAdapter);
        requestMySuggestionList();
    }

    private void requestMySuggestionList() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("userID", DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, ""));
        DoctorNetService.requestSuggestionList(Constants.SELECT_MY_OPINIONS_LIST, map, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        MySuggestionListBean mySuggestionListBean = (MySuggestionListBean) info;
                        mDatas.clear();
                        mDatas.addAll(mySuggestionListBean.list);
                        mySuggestionListAdapter.notifyDataSetChanged();
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
        title_right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog("请输入您的意见", new OnInputClickListener() {
                    @Override
                    public void onClick(String text, View v) {
                        if (TextUtils.isEmpty(text)) {
                            ToastUtil.shortToast(mContext, "内容不能为空");
                            return;
                        }
                        requestUpData(text);
                    }
                });
            }
        });
    }

    /**
     * 新增意见
     *
     * @param text ={"myOpinion":"我的意见内容","userID":1, "mobileType":"2"
     */
    private void requestUpData(String text) {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("myOpinion", text);
        map.put("userID", DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, ""));
        map.put("mobileType", Constants.MOBILE_TYPE);
        DoctorNetService.requestAddOrChange(Constants.INSERT_MY_OPINION, map, new
                NetWorkRequestInterface() {


                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        requestMySuggestionList();
                    }
                });
    }
}
