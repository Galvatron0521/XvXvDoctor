package cn.com.xxdoctor.activity.tag;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.DoctorTagListBean;
import cn.com.xxdoctor.bean.ResultBean;
import cn.com.xxdoctor.bean.TagBean;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;
import cn.com.xxdoctor.utils.ToastUtils;

/**
 * Created by chong.han on 2018/8/23.
 */

public class SelectPatientTagActivity extends DoctorBaseActivity {

    private TextView title_name;
    private ImageView title_back;
    private TextView title_right_tv;
    private List<TagBean> mTopData;
    private List<TagBean> mBottomData;
    private String tag;
    private Button tag_save;
    private String user_id;
    private String labeIds;
    private String patientID;
    public static String CHANGE_TAG_SUCCESS = "change_tag_success";
    private TagFlowLayout id_flowlayout_top;
    private TagFlowLayout id_flowlayout_bottom;
    private TagAdapter<TagBean> top_adapter;
    private TagAdapter<TagBean> bottomAdapter;
    private TextView tag_bottom_hint_tv;
    private TextView tag_top_hint_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);
        tag = getIntent().getStringExtra("tag");
        labeIds = getIntent().getStringExtra("labeIds");
        patientID = getIntent().getStringExtra("patientID");
        EventBus.getDefault().register(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        tag_top_hint_tv = (TextView) findViewById(R.id.tag_top_hint_tv);
        tag_bottom_hint_tv = (TextView) findViewById(R.id.tag_bottom_hint_tv);
        tag_save = (Button) findViewById(R.id.tag_save);

        title_back.setVisibility(View.VISIBLE);
        title_name.setText("患者标签");
        tag_save.setVisibility(View.VISIBLE);
        title_right_tv.setText("管理");
        title_right_tv.setVisibility(View.VISIBLE);

        id_flowlayout_top = (TagFlowLayout) findViewById(R.id.id_flowlayout_top);
        id_flowlayout_bottom = (TagFlowLayout) findViewById(R.id.id_flowlayout_bottom);
    }

    @Override
    public void initData() {
        mTopData = new ArrayList<>();
        mBottomData = new ArrayList<>();
        user_id = DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, "");
        requestTagList();
    }

    private void setAdapter() {

        top_adapter = new TagAdapter<TagBean>(mTopData) {
            @Override
            public View getView(FlowLayout parent, int position, TagBean tagBean) {
                TextView tv = (TextView) View.inflate(mContext, R.layout.tag_flowlayout_tv, null);
                tv.setText(tagBean.lableName);
                return tv;
            }
        };
        id_flowlayout_top.setAdapter(top_adapter);

        bottomAdapter = new TagAdapter<TagBean>(mBottomData) {
            @Override
            public View getView(FlowLayout parent, int position, TagBean tagBean) {
                TextView tv = (TextView) View.inflate(mContext, R.layout.tag_flowlayout_tv, null);
                tv.setText(tagBean.lableName);
                return tv;
            }
        };
        id_flowlayout_bottom.setAdapter(bottomAdapter);
    }

    private void requestTagList() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("userID", user_id);
        DoctorNetService.requestDoctorTagList(Constants.SELECT_DOCTOR_LABLE_LIST, map, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();

                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        DoctorTagListBean doctorTagListBean = (DoctorTagListBean) info;
                        mTopData.clear();
                        mBottomData.clear();
                        for (TagBean bean : doctorTagListBean.list) {
                            if (tag.contains(bean.lableName)) {
                                mTopData.add(bean);
                            } else {
                                mBottomData.add(bean);
                            }

                        }
                        showHintTv();
                        setAdapter();
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
                startActivity(new Intent(mContext, TagManageActivity.class));
            }
        });

        id_flowlayout_bottom.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                TagBean bean = mBottomData.get(position);
                mBottomData.remove(position);
                mTopData.add(bean);
                bottomAdapter.notifyDataChanged();
                top_adapter.notifyDataChanged();

                showHintTv();
                return false;
            }
        });
        id_flowlayout_top.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                TagBean bean = mTopData.get(position);
                mTopData.remove(position);
                mBottomData.add(bean);
                bottomAdapter.notifyDataChanged();
                top_adapter.notifyDataChanged();
                showHintTv();
                return false;
            }
        });
        tag_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(labeIds)) {
                    List<String> oldTag = new ArrayList<>();
                    String[] labeids = labeIds.split(",");
                    for (String labeid : labeids) {
                        oldTag.add(labeid);
                    }
                    requestDeleteTag(oldTag);
                } else {
                    showWaitDialog();
                    requestAddTag();
                }
            }
        });
    }

    private void showHintTv() {
        if (mBottomData.size() == 0) {
            tag_bottom_hint_tv.setVisibility(View.VISIBLE);
        } else {
            tag_bottom_hint_tv.setVisibility(View.GONE);
        }
        if (mTopData.size() == 0) {
            tag_top_hint_tv.setVisibility(View.VISIBLE);
        } else {
            tag_top_hint_tv.setVisibility(View.GONE);
        }
    }

    /**
     * 删除当前用户已有标签
     * patientlableIDs":["4028801e6541f52e016541f52e530000"],
     * "userID":"1",
     * "mobileType":"2"
     *
     * @param oldTag
     */
    private void requestDeleteTag(List<String> oldTag) {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("patientlableIDs", oldTag);
        map.put("userID", user_id);
        map.put("mobileType", Constants.MOBILE_TYPE);
        DoctorNetService.requestAddOrChange(Constants.DELETE_LABLE, map, new
                NetWorkRequestInterface() {


                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                        ToastUtils.showMessage(mContext, "未知错误，请重试");
                    }

                    @Override
                    public void onNext(Object info) {
                        requestAddTag();
                    }
                });
    }

    private void requestAddTag() {
        /**
         * "lablelist":["4028801e65408c4c0165408c4c510000"],
         "patientID":40,
         "mobileType":"2",
         "userID":1
         */
        List<String> lables = new ArrayList<>();
        for (TagBean mData : mTopData) {
            lables.add(mData.id);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("lablelist", lables);
        map.put("userID", user_id);
        map.put("patientID", patientID);
        map.put("mobileType", Constants.MOBILE_TYPE);
        DoctorNetService.requestAddOrChange(Constants.INSERT_LABLE, map, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                        ToastUtils.showMessage(mContext, "修改失败，请重试");
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        ResultBean bean = (ResultBean) info;
                        ToastUtils.showMessage(mContext, bean.data);
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put(Constants.EVENTBUS_TYEPE, CHANGE_TAG_SUCCESS);
                        EventBus.getDefault().post(map1);
                        finish();
                    }
                });
    }

    @Subscribe
    public void onEventMainThread(Map<String, Object> map) {
        String type = (String) map.get(Constants.EVENTBUS_TYEPE);
        if (type.equals(TagManageActivity.TAG_HAS_CHANGE)) {
            requestTagList();
        }
    }
}
