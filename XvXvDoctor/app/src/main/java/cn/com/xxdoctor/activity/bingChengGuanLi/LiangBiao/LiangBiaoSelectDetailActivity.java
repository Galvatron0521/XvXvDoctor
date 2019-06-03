package cn.com.xxdoctor.activity.bingChengGuanLi.LiangBiao;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.activity.bingChengGuanLi.BianZheng.AddBianZhengLunZhiActivity;
import cn.com.xxdoctor.adapter.LiangBiaoListAdapter;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.AddBingZhangBean;
import cn.com.xxdoctor.bean.BianZhengDetailBean;
import cn.com.xxdoctor.bean.LiangBiaoInfoBean;
import cn.com.xxdoctor.bean.ResultBean;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;
import cn.com.xxdoctor.utils.JSONUtil;
import cn.com.xxdoctor.utils.LogUtil;
import cn.com.xxdoctor.utils.ToastUtils;

/**
 * 编辑走这个！！！ 结束之后不需要传递 fieldRecordSign 回去
 */
public class LiangBiaoSelectDetailActivity extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private ListView liangbiao_list;
    private List<LiangBiaoInfoBean.ModuleListBean.ParentModuleBean> mDatas;
    private LiangBiaoListAdapter liangBiaoListAdapter;
    private String recordFlag;
    private String patientID;
    private LiangBiaoInfoBean bianZhengBean;
    private AddBingZhangBean addBingZhangBean;
    private BianZhengDetailBean bianZhengDetailBean;
    private String fieldRecordSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recordFlag = getIntent().getStringExtra("recordFlag");
        patientID = getIntent().getStringExtra("patientID");
        fieldRecordSign = getIntent().getStringExtra("fieldRecordSign");
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_liang_biao_fen_xi);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        liangbiao_list = (ListView) findViewById(R.id.liangbiao_list);
        title_back.setVisibility(View.VISIBLE);
        title_name.setText("量表分析");
        title_right_tv.setVisibility(View.VISIBLE);
        title_right_tv.setText("提交");
    }

    @Override
    public void initData() {
        mDatas = new ArrayList<>();
        liangBiaoListAdapter = new LiangBiaoListAdapter(mDatas, mContext);
        liangbiao_list.setAdapter(liangBiaoListAdapter);
        requestDetial();
    }

    @Override
    public void initListener() {
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        liangbiao_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LiangBiaoInfoBean.ModuleListBean moduleListBean = bianZhengBean.moduleList.get(i);
                Intent intent = new Intent(mContext, AddLiangBiaoInfoActivity.class);
                intent.putExtra("info", moduleListBean);
                intent.putExtra("patientID", patientID);
                intent.putExtra("recordFlag", recordFlag);
                intent.putExtra("position", i);
                startActivity(intent);
            }
        });
        title_right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    /**
     * 查询详情
     */
    private void requestDetial() {
        showWaitDialog();
        //patientID":40,"fieldRecordSign":"4028801e64b55ac50164b55ac5560000"
        Map<String, Object> map = new HashMap<>();
        map.put("patientID", patientID);
        map.put("fieldRecordSign", fieldRecordSign);
        DoctorNetService.requestBingZhengDetail(Constants.SELECT_FIELD_CONTENTS_LIST, map, new
                NetWorkRequestInterface() {
                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        bianZhengDetailBean = (BianZhengDetailBean) info;
                        //获取辩证数据
                        requestFildModule();
                    }
                });
    }

    private void requestFildModule() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("isCRF", "1");
        map.put("recordFlag", recordFlag);
        DoctorNetService.requestLiangBiaoMoudleList(Constants.SELECT_FILD_MODULE_LIST, map, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        bianZhengBean = (LiangBiaoInfoBean) info;
                        for (LiangBiaoInfoBean.ModuleListBean moduleListBean : bianZhengBean
                                .moduleList) {
                            mDatas.add(moduleListBean.parentModule);
                        }
                        LogUtil.e(mDatas.size() + "");
                        liangBiaoListAdapter.notifyDataSetChanged();
                        setDetailData();
                    }
                });
    }

    /**
     * 设置好数据
     */
    private void setDetailData() {
        int i = 1;
        for (LiangBiaoInfoBean.ModuleListBean childlistBean : bianZhengBean.moduleList) {
            for (LiangBiaoInfoBean.ModuleListBean.FildlistBean fildlistBean :
                    childlistBean.fildlist) {
                for (LiangBiaoInfoBean.ModuleListBean.FildlistBean.ChildrensBeanX
                        children : fildlistBean.childrens) {
                    for (LiangBiaoInfoBean.ModuleListBean.FildlistBean.ChildrensBeanX
                            .ChildrensBean childrensBean : children.childrens) {
                        for (BianZhengDetailBean.FieldContentsListBean fieldContentsListBean :
                                bianZhengDetailBean.fieldContentsList) {
                            if (fieldContentsListBean.fieldID.equals(childrensBean.id)) {
                                LogUtil.e(i++ + "");
                                childrensBean.content = fieldContentsListBean.contents;
                                childrensBean.isCheck = true;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 提交数据
     */
    private void submit() {
        addBingZhangBean = new AddBingZhangBean();
        addBingZhangBean.filterObj = new ArrayList<>();
        for (LiangBiaoInfoBean.ModuleListBean childlistBean : bianZhengBean.moduleList) {
            AddBingZhangBean.FilterObjBean filterObjBean = new
                    AddBingZhangBean.FilterObjBean();
            filterObjBean.moduleCode = childlistBean.parentModule.moduleCode;
            filterObjBean.fieldContentsList = new ArrayList<>();
            for (LiangBiaoInfoBean.ModuleListBean.FildlistBean fildlistBean
                    : childlistBean.fildlist) {
                for (LiangBiaoInfoBean.ModuleListBean.FildlistBean.ChildrensBeanX children :
                        fildlistBean.childrens) {
                    for (LiangBiaoInfoBean.ModuleListBean.FildlistBean.ChildrensBeanX
                            .ChildrensBean childrensBeanXX :
                            children.childrens) {
                        if (!TextUtils.isEmpty(childrensBeanXX.content) ||
                                childrensBeanXX.isCheck) {
                            // 三级数据
                            AddBingZhangBean.FilterObjBean.FieldContentsListBean
                                    fieldContentsListBeanXX = new AddBingZhangBean.FilterObjBean
                                    .FieldContentsListBean();
                            fieldContentsListBeanXX.contents = childrensBeanXX
                                    .content;
                            fieldContentsListBeanXX.fieldID = childrensBeanXX.id;
                            filterObjBean.fieldContentsList.add(fieldContentsListBeanXX);
                            LogUtil.e("三级数据 ---" + fieldContentsListBeanXX.contents + " --- " +
                                    fieldContentsListBeanXX.fieldID);
                        }
                    }
                }
            }
            if (filterObjBean.fieldContentsList.size() > 0)
                addBingZhangBean.filterObj.add(filterObjBean);
        }
        if (addBingZhangBean.filterObj.size() == 0) {
            ToastUtils.showMessage(mContext, "请添加病症信息");
            return;
        }
        LogUtil.e(addBingZhangBean.toString());
        List<AddBingZhangBean.FilterObjBean> insertList = new ArrayList<>(); // 新增
        List<AddBingZhangBean.FilterObjBean> upDataList = new ArrayList<>(); // 修改

        for (BianZhengDetailBean.FieldContentsListBean fieldContentsListBean :
                bianZhengDetailBean.fieldContentsList) {
            for (AddBingZhangBean.FilterObjBean filterObjBean : addBingZhangBean.filterObj) {
                if (fieldContentsListBean.moduleCode.equals(filterObjBean.moduleCode)) {
                    filterObjBean.fieldRecordID = fieldContentsListBean.fieldRecordID;
                    upDataList.add(filterObjBean);
                }
            }
        }
        for (AddBingZhangBean.FilterObjBean filterObjBean : addBingZhangBean.filterObj) {
            if (!upDataList.contains(filterObjBean)) {
                insertList.add(filterObjBean);
            }
        }

        if (insertList.size() > 0) {
            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("filterObj", insertList);
            requestUpData("insert", insertMap);
        }
        if (upDataList.size() > 0) {
            Map<String, Object> upDataMap = new HashMap<>();
            upDataMap.put("filterObj", upDataList);
            requestUpData("update", upDataMap);
        }
    }

    /**
     * 上传数据
     */
    private void requestUpData(String optionTag, Map<String, Object> map1) {
        showWaitDialog();
        //{"patientID":40,"diseasesID":"", "fieldRecordSign":"","mobileType":"2",
        // "optionTag":"insert","recordFlag":"0"}
        //final String sendJson = JSONUtil.parseMapToJson(params);
        Map<String, Object> map = new HashMap<>();
        map.put("patientID", patientID);
        map.put("userID", DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, ""));
        map.put("fieldRecordSign", fieldRecordSign);
        map.put("mobileType", Constants.MOBILE_TYPE);
        map.put("optionTag", optionTag);
        map.put("recordFlag", recordFlag);
        String sendJson = JSONUtil.parseMapToJson(map);
        String sendJson2 = JSONUtil.parseMapToJson(map1);
        Map<String, Object> map3 = new HashMap<>();
        map3.put("act", "insertOrUpdateFieldContents");
        map3.put("data", sendJson);
        map3.put("filter", sendJson2);
        String Url = Constants.Host.replace("?", "");
        DoctorNetService.requestAddOrChangeByUrl(Url, map3, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        ResultBean resultBean = (ResultBean) info;
                        ToastUtils.showMessage(mContext, resultBean.data);
                        Map<String, Object> map = new HashMap<>();
                        map.put(Constants.EVENTBUS_TYEPE, AddBianZhengLunZhiActivity
                                .UPDATA_BINGZHENG_SUCCESS);
                        EventBus.getDefault().post(map);
                        finish();
                    }
                });
    }

    @Subscribe
    public void onEventMainThread(Map<String, Object> map) {
        String type = (String) map.get(Constants.EVENTBUS_TYEPE);
        if (type.equals(AddLiangBiaoInfoActivity.ADD_LIANG_BIAN_FINISH)) {
            int position = (int) map.get("position");
            LiangBiaoInfoBean.ModuleListBean bean = (LiangBiaoInfoBean.ModuleListBean) map.get
                    (Constants.EVENTBUS_VALUE);
            bianZhengBean.moduleList.remove(position);
            bianZhengBean.moduleList.add(position, bean);
        }
    }
}
