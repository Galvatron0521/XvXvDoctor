package cn.com.xxdoctor.activity.tag;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mylhyl.circledialog.view.listener.OnInputClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.adapter.TagListAdapter;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.DoctorTagListBean;
import cn.com.xxdoctor.bean.ResultBean;
import cn.com.xxdoctor.bean.TagBean;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;
import cn.com.xxdoctor.utils.ToastUtils;

public class TagManageActivity extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private ListView tad_manage_listview;
    private TagListAdapter tagListAdapter;
    private List<TagBean> mDatas;
    public static String TAG_HAS_CHANGE = "tag_has_change";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_manage);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        tad_manage_listview = (ListView) findViewById(R.id.tad_manage_listview);

        title_back.setVisibility(View.VISIBLE);
        title_name.setText("标签分类");
        title_right_tv.setText("添加");
        title_right_tv.setVisibility(View.VISIBLE);

    }

    @Override
    public void initData() {
        mDatas = new ArrayList<>();
        tagListAdapter = new TagListAdapter(mContext, mDatas);
        tad_manage_listview.setAdapter(tagListAdapter);
        requestTagList();
    }

    private void requestTagList() {
        showWaitDialog();
        final Map<String, Object> map = new HashMap<>();
        map.put("userID", DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, ""));
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
                        mDatas.clear();
                        mDatas.addAll(doctorTagListBean.list);
                        tagListAdapter.notifyDataSetChanged();
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put(Constants.EVENTBUS_TYEPE, TAG_HAS_CHANGE);
                        EventBus.getDefault().post(map1);
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
                showInputDialog("请输入标签名称", new OnInputClickListener() {
                    @Override
                    public void onClick(String text, View v) {
                        if (TextUtils.isEmpty(text)) {
                            ToastUtils.showMessage(mContext, "标签名称不能为空");
                            return;
                        }
                        requestAddTag(text);
                    }
                });
            }
        });
        tagListAdapter.setOnButtonClickListener(new TagListAdapter.onButtonClickListener() {
            @Override
            public void onClick(int type, int position) {
                final TagBean bean = mDatas.get(position);
                if (type == tagListAdapter.ON_DELETE_CLICK) {
                    showAffirmDialog("提示", "确认删除该标签？", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestDeleteTag(bean.id);
                        }
                    });
                } else {
                    showInputDialog("请输入标签名称", new OnInputClickListener() {
                        @Override
                        public void onClick(String text, View v) {
                            if (TextUtils.isEmpty(text)) {
                                ToastUtils.showMessage(mContext, "标签名称不能为空");
                                return;
                            }
                            requestUpDataTag(bean.id, text);
                        }
                    });
                }
            }
        });
    }

    /**
     * 更新标签
     *
     * @param id
     * @param text
     */
    private void requestUpDataTag(String id, String text) {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("lableName", text);
        map.put("mobileType", Constants.MOBILE_TYPE);
        DoctorNetService.requestAddOrChange(Constants.UPDATE_DOCTOR_LABLE, map, new
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
                        requestTagList();
                    }
                });
    }

    /**
     * 删除标签
     *
     * @param id
     */
    private void requestDeleteTag(String id) {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("mobileType", Constants.MOBILE_TYPE);
        map.put("userID", DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, ""));
        DoctorNetService.requestAddOrChange(Constants.DELETE_DOCTOR_LABLE, map, new
                NetWorkRequestInterface() {
                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                        ToastUtils.showMessage(mContext, "删除失败，请重试");
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        ResultBean bean = (ResultBean) info;
                        ToastUtils.showMessage(mContext, bean.data);
                        requestTagList();
                    }
                });
    }

    /**
     * 添加标签
     *
     * @param text
     */
    private void requestAddTag(String text) {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("lableName", text);
        map.put("userID", DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, ""));
        map.put("createUser", DoctorBaseAppliction.spUtil.getString(Constants.Name, ""));
        map.put("mobileType", Constants.MOBILE_TYPE);
        DoctorNetService.requestAddOrChange(Constants.INSERT_DOCTOR_LABLE, map, new
                NetWorkRequestInterface() {
                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                        ToastUtils.showMessage(mContext, "添加失败，请重试");
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        ResultBean bean = (ResultBean) info;
                        ToastUtils.showMessage(mContext, bean.data);
                        requestTagList();
                    }
                });
    }

}
