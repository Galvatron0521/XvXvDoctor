package cn.com.xxdoctor.activity.bingChengGuanLi.BianZheng;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.adapter.BingZhengMenuListAdapter;
import cn.com.xxdoctor.adapter.MyExpandableListViewAdapter;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.AddBingZhangBean;
import cn.com.xxdoctor.bean.BianZhengBean;
import cn.com.xxdoctor.bean.BianZhengDetailBean;
import cn.com.xxdoctor.bean.PicUpLoadResultBean;
import cn.com.xxdoctor.bean.ResultBean;
import cn.com.xxdoctor.chat.utils.ToastUtil;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;
import cn.com.xxdoctor.utils.DateUtils;
import cn.com.xxdoctor.utils.HcUtils;
import cn.com.xxdoctor.utils.JSONUtil;
import cn.com.xxdoctor.utils.LogUtil;
import cn.com.xxdoctor.utils.OkHttpUtils;
import cn.com.xxdoctor.utils.ToastUtils;
import cn.com.xxdoctor.view.WarpLinearLayout;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BianZhengLunDetailZhiActivity extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private ExpandableListView binazheng_list;
    private List<BianZhengBean.ModuleListBean.ParentModuleBean> parentList;
    private List<List<BianZhengBean.ModuleListBean.ChildlistBean>> childlists;
    private MyExpandableListViewAdapter myExpandableListViewAdapter;
    private LinearLayout bingzheng_container;
    private Map<String, List<View>> fourLevelMap = new HashMap<>();
    private Map<String, List<View>> fiveLevelMap = new HashMap<>();
    Map<String, RadioButton> radios = new HashMap<>();
    private String patientID;
    private AddBingZhangBean addBingZhangBean;
    public static String UPDATA_BINGZHENG_SUCCESS = "updata_bingzheng_success";
    private String recordFlag;
    private String fieldRecordSign;
    private String sicknessTime;
    private BianZhengDetailBean bianZhengDetailBean;
    private List<BianZhengBean.ModuleListBean.ChildlistBean> navChildLists;
    private ImageButton menu_button;
    private ListView menu_list;
    private boolean isShowMenuList = false;
    private BingZhengMenuListAdapter navAdapter;
    private BianZhengBean bianZhengBean;
    private final int REQUEST_CODE_GALLERY = 1001;
    private int currentGroupPosition;
    private StringBuilder sb;
    private EditText currentAloneBigEdit;//当前创建的edit
    private ImageView bingzheng_arrow;
    private boolean menuIsHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bian_zheng_lun_zhi);
        recordFlag = getIntent().getStringExtra("recordFlag");
        fieldRecordSign = getIntent().getStringExtra("fieldRecordSign");
        sicknessTime = getIntent().getStringExtra("sicknessTime");
        patientID = getIntent().getStringExtra("patientID");
        initTimePicker();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        binazheng_list = (ExpandableListView) findViewById(R.id.binazheng_list);
        bingzheng_container = (LinearLayout) findViewById(R.id.bingzheng_container);
        menu_button = (ImageButton) findViewById(R.id.menu_button);
        bingzheng_arrow = (ImageView) findViewById(R.id.bingzheng_arrow);
        menu_list = (ListView) findViewById(R.id.menu_list);
        title_back.setVisibility(View.VISIBLE);
        title_name.setText("辨证论治");
        title_right_tv.setText("提交");
        title_right_tv.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        parentList = new ArrayList<>();
        childlists = new ArrayList<>();
        navChildLists = new ArrayList<>();
        navAdapter = new BingZhengMenuListAdapter(navChildLists, mContext);
        menu_list.setAdapter(navAdapter);
        // 获取详情数据
        requestDetial();
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
        map.put("isCRF", "0");
        map.put("recordFlag", recordFlag);
        DoctorNetService.requestFildMoudleList(Constants.SELECT_FILD_MODULE_LIST, map, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        bianZhengBean = (BianZhengBean) info;
                        for (BianZhengBean.ModuleListBean moduleListBean : bianZhengBean
                                .moduleList) {
                            parentList.add(moduleListBean.parentModule);
                            List<BianZhengBean.ModuleListBean.ChildlistBean>
                                    tagModuleBeans = new ArrayList<>();
                            for (BianZhengBean.ModuleListBean.ChildlistBean childlistBean :
                                    moduleListBean.childlist) {
                                tagModuleBeans.add(childlistBean);
                            }
                            childlists.add(tagModuleBeans);
                        }
                        myExpandableListViewAdapter = new MyExpandableListViewAdapter(mContext,
                                parentList, childlists);
                        binazheng_list.setAdapter(myExpandableListViewAdapter);
                        setDetailData();
                        changeRightView(childlists.get(0).get(0));
                    }
                });
    }

    /**
     * 设置好数据
     */
    private void setDetailData() {
        for (BianZhengDetailBean.FieldContentsListBean fieldContentsListBean :
                bianZhengDetailBean.fieldContentsList) {
            for (List<BianZhengBean.ModuleListBean.ChildlistBean> childlist : childlists) {
                for (BianZhengBean.ModuleListBean.ChildlistBean childlistBean : childlist) {
                    for (BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean fildlistBean :
                            childlistBean.fildlist) {
                        for (BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
                                .ChildrensBeanXXX
                                children : fildlistBean.childrens) {
                            if (fieldContentsListBean.fieldID.equals(children.id)) {
                                children.content = fieldContentsListBean.contents;
                                children.isCheck = true;
                            }
                            for (BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
                                    .ChildrensBeanXXX.ChildrensBeanXX childrensBeanXX : children
                                    .childrens) {
                                if (fieldContentsListBean.fieldID.equals(childrensBeanXX.id)) {
                                    childrensBeanXX.content = fieldContentsListBean.contents;
                                    childrensBeanXX.isCheck = true;
                                }
                                for (BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
                                        .ChildrensBeanXXX.ChildrensBeanXX.ChildrensBeanX
                                        childrensBeanX : childrensBeanXX.childrens) {
                                    if (fieldContentsListBean.fieldID.equals(childrensBeanX.id)) {
                                        childrensBeanX.content = fieldContentsListBean.contents;
                                        childrensBeanX.isCheck = true;
                                    }
                                    for (BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
                                            .ChildrensBeanXXX.ChildrensBeanXX.ChildrensBeanX
                                            .ChildrensBean bean : childrensBeanX.childrens) {
                                        if (fieldContentsListBean.fieldID.equals(bean.id)) {
                                            bean.content = fieldContentsListBean.contents;
                                            bean.isCheck = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void initListener() {
        //返回
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 关联选项
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowMenuList) {
                    menu_list.setVisibility(View.GONE);
                } else {
                    menu_list.setVisibility(View.VISIBLE);
                }
                isShowMenuList = !isShowMenuList;
            }
        });
        bingzheng_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuIsHide) {
                    binazheng_list.setVisibility(View.VISIBLE);
                    bingzheng_arrow.setImageResource(R.drawable.hide_left_arrow);
                } else {
                    binazheng_list.setVisibility(View.GONE);
                    bingzheng_arrow.setImageResource(R.drawable.hide_right_arrow);
                }
                menuIsHide = !menuIsHide;
            }
        });
        menu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                BianZhengBean.ModuleListBean.ChildlistBean childlistBean = navChildLists.get
                        (position);
                int groupPositionn = 0;
                for (int groupPosition = 0; groupPosition < childlists.size(); groupPosition++) {
                    List<BianZhengBean.ModuleListBean.ChildlistBean> childlistBeans = childlists
                            .get(groupPosition);
                    for (int childPosition = 0; childPosition < childlistBeans.size();
                         childPosition++) {

                        BianZhengBean.ModuleListBean.ChildlistBean childlistBean1 =
                                childlistBeans.get(childPosition);
                        if (childlistBean1.tagModule.moduleCode.equals(childlistBean.tagModule
                                .moduleCode)) {
                            childlistBean1.tagModule.isSelect = true;
                            groupPositionn = groupPosition;
                            changeRightView(childlistBean1);
                        } else {
                            childlistBean1.tagModule.isSelect = false;
                        }
                    }
                }
                binazheng_list.collapseGroup(groupPositionn);
                binazheng_list.expandGroup(groupPositionn);
            }
        });
        // 子类条目点击事件
        binazheng_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int
                    groupPositon, int childPosition, long l) {
                currentGroupPosition = groupPositon;
                HcUtils.hideKeyboard(BianZhengLunDetailZhiActivity.this);
                changeSelectItemStatu(groupPositon, childPosition);
                final BianZhengBean.ModuleListBean.ChildlistBean childlistBean =
                        childlists.get(groupPositon).get(childPosition);
                changeRightView(childlistBean);

                return false;
            }
        });
        //重写OnGroupClickListener，实现当展开时，ExpandableListView不自动滚动  忘了有啥用了。。。
        binazheng_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()

        {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition,
                                        long id) {
                HcUtils.hideKeyboard(BianZhengLunDetailZhiActivity.this);
                if (childlists.get(groupPosition).size() == 1) {
                    currentGroupPosition = groupPosition;
                    changeSelectItemStatu(groupPosition, -1);
                    bingzheng_container.removeAllViews();
                    final BianZhengBean.ModuleListBean.ChildlistBean childlistBean =
                            childlists.get(groupPosition).get(0);
                    //添加 右侧视图  一级
                    for (BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean fildlistBean :
                            childlistBean.fildlist) {
                        // title,textarea,checkbox,radio,text,select
                        bingzheng_container.addView(getTitle(fildlistBean.fieldName));
                        //添加 右侧视图  二级
                        for (final BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
                                .ChildrensBeanXXX children : fildlistBean.childrens) {
                            addSecondLevelView(children);
                            //添加  右侧视图 三级
                            WarpLinearLayout warpLinearLayout = getLinearContainer();
                            bingzheng_container.addView(warpLinearLayout);
                            addThreeLevelView(children,warpLinearLayout);
                            //添加右侧 四级 五级视图
                            for (final BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
                                    .ChildrensBeanXXX.ChildrensBeanXX childrensBeanXX : children
                                    .childrens) {
                                if (children.fieldType.equals("checkbox") || children.fieldType
                                        .equals("radio")) {
                                    if (childrensBeanXX.isCheck && childrensBeanXX.childrens.size
                                            () > 0) {
                                        addFiveLevelView(childrensBeanXX, warpLinearLayout);
                                    }
                                }
                            }
                        }
                    }
                }

                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                } else {
                    //第二个参数false表示展开时是否触发默认滚动动画
                    parent.expandGroup(groupPosition, true);
                }
                return true;
            }
        });
        // 提交
        title_right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    /**
     * 根据左侧选择替换右侧界面
     *
     * @param childlistBean
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void changeRightView(BianZhengBean.ModuleListBean.ChildlistBean childlistBean) {
        bingzheng_container.removeAllViews();
        //添加 右侧视图  一级
        for (BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean fildlistBean :
                childlistBean.fildlist) {
            // title,textarea,checkbox,radio,text,select
            bingzheng_container.addView(getTitle(fildlistBean.fieldName));
            //添加 右侧视图  二级
            for (final BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
                    .ChildrensBeanXXX children : fildlistBean.childrens) {
                addSecondLevelView(children);
                //添加  右侧视图 三级
                //添加  右侧视图 三级
                WarpLinearLayout warpLinearLayout = getLinearContainer();
                bingzheng_container.addView(warpLinearLayout);
                addThreeLevelView(children, warpLinearLayout);
                //添加右侧 四级 五级视图
                for (final BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
                        .ChildrensBeanXXX.ChildrensBeanXX childrensBeanXX : children
                        .childrens) {
                    if (children.fieldType.equals("checkbox")) {
                        if (childrensBeanXX.isCheck) {
                            addFiveLevelView(childrensBeanXX, warpLinearLayout);
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
        for (List<BianZhengBean.ModuleListBean.ChildlistBean> childlist : childlists) {
            for (BianZhengBean.ModuleListBean.ChildlistBean childlistBean : childlist) {

                AddBingZhangBean.FilterObjBean filterObjBean = new
                        AddBingZhangBean.FilterObjBean();
                filterObjBean.moduleCode = childlistBean.tagModule.moduleCode;
                filterObjBean.fieldContentsList = new ArrayList<>();
                for (BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean fildlistBean
                        : childlistBean.fildlist) {
                    for (BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
                            .ChildrensBeanXXX children : fildlistBean.childrens) {

                        if (!TextUtils.isEmpty(children.content) || children.isCheck) {
                            // 二级数据
                            LogUtil.e("二级数据  --" + children.content + " --- " + children.id);
                            AddBingZhangBean.FilterObjBean.FieldContentsListBean
                                    fieldContentsListBean
                                    = new AddBingZhangBean.FilterObjBean
                                    .FieldContentsListBean();
                            if (TextUtils.isEmpty(children.content)) {
                                fieldContentsListBean.contents = children.displayName;
                            } else {
                                fieldContentsListBean.contents = children.content;
                            }

                            fieldContentsListBean.fieldID = children.id;
                            filterObjBean.fieldContentsList.add(fieldContentsListBean);
                        } else {
                            for (BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
                                    .ChildrensBeanXXX.ChildrensBeanXX childrensBeanXX :
                                    children.childrens) {
                                if (!TextUtils.isEmpty(childrensBeanXX.content) ||
                                        childrensBeanXX.isCheck) {
                                    // 三级数据
                                    AddBingZhangBean.FilterObjBean.FieldContentsListBean
                                            fieldContentsListBeanXX
                                            = new AddBingZhangBean.FilterObjBean
                                            .FieldContentsListBean();

                                    if (childrensBeanXX.isCheck) {
                                        fieldContentsListBeanXX.contents = childrensBeanXX
                                                .displayName;
                                    } else {
                                        fieldContentsListBeanXX.contents = childrensBeanXX.content;
                                    }

                                    fieldContentsListBeanXX.fieldID = childrensBeanXX.id;
                                    filterObjBean.fieldContentsList.add(fieldContentsListBeanXX);
                                    LogUtil.e("三级数据 ---" + fieldContentsListBeanXX.contents + " " +
                                            "--- " + fieldContentsListBeanXX.fieldID);
                                    for (BianZhengBean.ModuleListBean.ChildlistBean
                                            .FildlistBean
                                            .ChildrensBeanXXX.ChildrensBeanXX.ChildrensBeanX
                                            childrensBeanX : childrensBeanXX.childrens) {
                                        if (!TextUtils.isEmpty(childrensBeanX.content) ||
                                                childrensBeanX.isCheck) {
                                            AddBingZhangBean.FilterObjBean
                                                    .FieldContentsListBean
                                                    fieldContentsListBeanX
                                                    = new AddBingZhangBean.FilterObjBean
                                                    .FieldContentsListBean();

                                            if (childrensBeanX.isCheck) {
                                                fieldContentsListBeanX.contents = childrensBeanX
                                                        .displayName;
                                            } else {
                                                fieldContentsListBeanX.contents = childrensBeanX
                                                        .content;
                                            }
                                            fieldContentsListBeanX.fieldID =
                                                    childrensBeanX.id;
                                            filterObjBean.fieldContentsList.add
                                                    (fieldContentsListBeanX);
                                            LogUtil.e("四级数据 --- " + fieldContentsListBeanX
                                                    .contents + " " + "--- " +
                                                    fieldContentsListBeanX.fieldID);
                                        }
                                        for (BianZhengBean.ModuleListBean.ChildlistBean
                                                .FildlistBean.ChildrensBeanXXX
                                                .ChildrensBeanXX
                                                .ChildrensBeanX.ChildrensBean
                                                childrensBean :
                                                childrensBeanX.childrens) {
                                            if (!TextUtils.isEmpty(childrensBean
                                                    .content) || childrensBean.isCheck) {
                                                AddBingZhangBean.FilterObjBean
                                                        .FieldContentsListBean
                                                        fieldContentsListBeans
                                                        = new AddBingZhangBean
                                                        .FilterObjBean.FieldContentsListBean();
                                                if (childrensBean.isCheck) {
                                                    fieldContentsListBeans.contents = childrensBean
                                                            .displayName;
                                                } else {
                                                    fieldContentsListBeans.contents = childrensBean
                                                            .content;
                                                }
                                                fieldContentsListBeans.fieldID =
                                                        childrensBean.id;
                                                filterObjBean.fieldContentsList.add
                                                        (fieldContentsListBeans);
                                                LogUtil.e("五级数据  --- " + childrensBean
                                                        .displayName + "--- " +
                                                        fieldContentsListBeans.fieldID);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (filterObjBean.fieldContentsList.size() > 0)
                    addBingZhangBean.filterObj.add(filterObjBean);
            }
        }
        boolean guanXinBing = true;
        boolean gaoxueya = true;
        boolean gaoxuezhi = true;
        boolean xinzang = true;
        boolean xinlvshichang = true;

        if (addBingZhangBean.filterObj.size() == 0) {
            ToastUtils.showMessage(mContext, "请添加病症信息");
            return;
        } else {
            for (AddBingZhangBean.FilterObjBean filterObjBean : addBingZhangBean.filterObj) {
                for (AddBingZhangBean.FilterObjBean.FieldContentsListBean fieldContentsListBean :
                        filterObjBean.fieldContentsList) {

                    //!111
                    if (fieldContentsListBean.fieldID.equals("4028801e6420461f01642048f6cc0003")) {
                        guanXinBing = false;
                    }
                    if (filterObjBean.moduleCode.equals("SP020139") || filterObjBean.moduleCode
                            .equals("SP020140")) {
                        guanXinBing = true;
                    }
                    //2222
                    if (fieldContentsListBean.fieldID.equals("4028801e64205fce0164207e24e30009")) {
                        gaoxueya = false;
                    }
                    if (fieldContentsListBean.fieldID.equals("402880e6641bf1be01641bf66c8a0005")) {
                        gaoxueya = true;
                    }
                    //3333
                    if (fieldContentsListBean.fieldID.equals("4028801e6420fa89016420fa89d30000")) {
                        gaoxuezhi = false;
                    }
                    if (filterObjBean.moduleCode.equals("SP020125")) {
                        gaoxuezhi = true;
                    }
                    //444444
                    if (fieldContentsListBean.fieldID.equals("4028801e6420fa89016421027a090002") ||
                            fieldContentsListBean.fieldID.equals
                                    ("4028801e6420fa890164210a5e410003")) {
                        xinzang = false;
                    }
                    if (filterObjBean.moduleCode.equals("SP020137")) {
                        xinzang = true;
                    }
                    //5555
                    if (fieldContentsListBean.fieldID.equals("4028801e6420fa890164210b90530004")) {
                        xinlvshichang = false;
                    }
                    if (filterObjBean.moduleCode.equals("SP020135") || filterObjBean.moduleCode
                            .equals("SP020134")) {
                        xinlvshichang = true;
                    }
                }
            }
        }

        /**
         * 4028801e6420461f01642048f6cc0003  冠心病   1 2
         * SP020139  冠脉CTA
         * SP020140  冠脉造影
         *
         * 4028801e64205fce0164207e24e30009  原发性高血压病 1 1
         * 402880e6641bf1be01641bf66c8a0005  体格检查血压
         *
         * 4028801e6420fa89016420fa89d30000  高脂血症 1 1
         * SP020125  化学检验血脂
         *
         * 4028801e6420fa89016421027a090002  扩张性心肌病 2 1
         * 4028801e6420fa890164210a5e410003  心脏瓣膜病
         * SP020137  心脏声超
         *
         * 4028801e6420fa890164210b90530004  心律失常 1 2
         * SP020134  心电图
         * SP020135  Holter

         */
        if (!guanXinBing) {
            ToastUtil.shortToast(mContext, "请填写冠脉CTA或冠脉造影");
            return;
        } else if (!gaoxueya) {
            ToastUtil.shortToast(mContext, "请填写体格检查-血压");
            return;
        } else if (!gaoxuezhi) {
            ToastUtil.shortToast(mContext, "请填写化学检验-血脂");
            return;
        } else if (!xinzang) {
            ToastUtil.shortToast(mContext, "请填写心脏声超");
            return;
        } else if (!xinlvshichang) {
            ToastUtil.shortToast(mContext, "请填写心电图或Holter");
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
     * insert添加
     * update修改
     */
    private void requestUpData(String optionTag, Map<String, Object> map1) {
        showWaitDialog();
        //{"patientID":40,"diseasesID":"", "fieldRecordSign":"","mobileType":"2",
        // "optionTag":"insert","recordFlag":"0"}
        //final String sendJson = JSONUtil.parseMapToJson(params);
        Map<String, Object> map = new HashMap<>();
        map.put("patientID", patientID);
        map.put("fieldRecordSign", fieldRecordSign);
        map.put("sicknessTime", sicknessTime);
        map.put("mobileType", Constants.MOBILE_TYPE);
        map.put("userID", DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, ""));
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
                        map.put(Constants.EVENTBUS_TYEPE, UPDATA_BINGZHENG_SUCCESS);
                        EventBus.getDefault().post(map);
                        finish();
                    }
                });
    }

    /**
     * 添加 右侧二级视图
     * SP020146  主症    4028801e6579432901657944df0a0001
     * SP020147  伴随症状  4028801e6579432901657947e1290003
     * SP020148  兼症  4028801e657943290165797421160005
     *
     * @param children
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addSecondLevelView(final BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
            .ChildrensBeanXXX children) {
        if (!"text".equals(children.fieldType)) {
            bingzheng_container.addView(getFuTitle(children.displayName));
            if ("textarea".equals(children.fieldType)) {
                EditText aloneBigEdit = getAloneBigEdit(children.content);
                if (children.id.equals("4028801e654a8ec201654a8f05780001")) {
                    addPicBt(children, aloneBigEdit);
                }
                if ("4028801e6579432901657944df0a0001".equals(children.id) ||
                        "4028801e6579432901657947e1290003".equals(children.id) ||
                        "4028801e657943290165797421160005".equals(children.id)) {
                    currentAloneBigEdit = aloneBigEdit;
                    sb = new StringBuilder();
                    sb.append(currentAloneBigEdit.getText().toString());
                    bingzheng_container.addView(currentAloneBigEdit);
                    currentAloneBigEdit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int
                                i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int
                                i2) {
                            children.content = charSequence.toString();
                            sb.append(children.content.replace(sb.toString(), ""));
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                } else {
                    bingzheng_container.addView(aloneBigEdit);
                    aloneBigEdit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int
                                i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int
                                i2) {
                            children.content = charSequence.toString();
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }

            } else if ("date".equals(children.fieldType)) {
                addDateSelect(children);
            }
        } else if ("date".equals(children.fieldType)) {
            addDateSelect(children);
        } else {
            WarpLinearLayout editViews = getEditView(children.displayName, children
                    .suffixName, children.content);
            bingzheng_container.addView(editViews);
            EditText editText = (EditText) editViews.getChildAt(1);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i,
                                              int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int
                        i1, int i2) {
                    children.content = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    private void addDateSelect(final BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
            .ChildrensBeanXXX children) {
        final LinearLayout dateView = getDateView(children.displayName, TextUtils.isEmpty
                (children.content) ? "点击请选择" :
                children.content);
        bingzheng_container.addView(dateView);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show(dateView);
            }
        });
        ((TextView) dateView.getChildAt(1)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int
                    i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                children.content = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 上传图片按钮 中药处方
     *
     * @param children
     * @param aloneBigEdit
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addPicBt(final BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
            .ChildrensBeanXXX children, final EditText aloneBigEdit) {
        TextView picButton = getPicSelectButton("图片上传");
        bingzheng_container.addView(picButton);
        picButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, DoctorBaseAppliction
                        .functionConfig, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo>
                            resultList) {
                        if (resultList != null) {
                            for (PhotoInfo photoInfo : resultList) {
                                upLoadPic(new File(photoInfo.getPhotoPath()),
                                        children, aloneBigEdit);
                            }
                        }
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {
                        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 添加右侧三级视图
     *
     * @param children
     * @param warpLinearLayout
     */
    private void addThreeLevelView(final BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
            .ChildrensBeanXXX children, final WarpLinearLayout warpLinearLayout) {
        for (final BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
                .ChildrensBeanXXX.ChildrensBeanXX childrensBeanXX : children
                .childrens) {
            if (children.fieldType.equals("checkbox")) {
                CheckBox checkbox = getCheckbox(childrensBeanXX.displayName,
                        childrensBeanXX.isCheck);
                warpLinearLayout.addView(checkbox);
                addThreeMenuList(childrensBeanXX);
                checkbox.setTag(childrensBeanXX.id);
                checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        childrensBeanXX.isCheck = !childrensBeanXX.isCheck;
                        if (childrensBeanXX.isCheck) {
                            if (childrensBeanXX.childrens.size() > 0) {
                                // 右侧视图 四级
                                addFiveLevelView(childrensBeanXX, warpLinearLayout);
                            }
                        } else {
                            List<View> fourViews = fourLevelMap.get
                                    (childrensBeanXX.id);
                            if (fourViews != null && fourViews.size() > 0) {
                                for (View fourView : fourViews) {
                                    warpLinearLayout.removeView(fourView);
                                }
                                List<View> fiveViews = fiveLevelMap.get
                                        (childrensBeanXX.id);
                                for (View fiveView : fiveViews) {
                                    warpLinearLayout.removeView(fiveView);
                                }
                            }

                        }
                        /**
                         * SP020146  主症
                         * SP020147  伴随症状
                         * SP020148  兼症
                         */
                        if ("SP020146".equals(childrensBeanXX.moduleCode) ||
                                "SP020147".equals(childrensBeanXX.moduleCode) ||
                                "SP020148".equals(childrensBeanXX.moduleCode)) {
                            if (childrensBeanXX.isCheck) {
                                sb.append(childrensBeanXX.displayName + " ");
                            } else {
                                String replace = sb.toString().replace(childrensBeanXX
                                        .displayName + " ", "");
                                sb = new StringBuilder();
                                sb.append(replace);
                            }
                            currentAloneBigEdit.setText(sb.toString());
                        }
                        addThreeMenuList(childrensBeanXX);
                    }
                });
                // title,textarea,checkbox,radio,text,select
            } else if ("radio".equals(children.fieldType)) {
                final RadioButton radioButton = getRadioButton(childrensBeanXX.displayName,
                        childrensBeanXX.isCheck);
                warpLinearLayout.addView(radioButton);
                radios.put(childrensBeanXX.id, radioButton);
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String oldId = "";
                        for (BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
                                .ChildrensBeanXXX.ChildrensBeanXX beanXX : children.childrens) {
                            if (beanXX.isCheck) oldId = beanXX.id;
                            radios.get(beanXX.id).setChecked(false);
                            beanXX.isCheck = false;
                        }
                        List<View> fourViews = fourLevelMap.get(oldId);
                        if (fourViews != null && fourViews.size() > 0) {
                            for (View fourView : fourViews) {
                                warpLinearLayout.removeView(fourView);
                            }
                            List<View> fiveViews = fiveLevelMap.get(oldId);
                            for (View fiveView : fiveViews) {
                                warpLinearLayout.removeView(fiveView);
                            }
                        }
                        radioButton.setChecked(true);
                        childrensBeanXX.isCheck = true;
                        if (childrensBeanXX.childrens.size() > 0) {
                            addFiveLevelView(childrensBeanXX,warpLinearLayout);
                        }
                    }
                });
            } else if ("date".equals(children.fieldType)) {
                addDateSelect(children);
            }
        }

    }

    /**
     * 根据三级数据添加 menubutton list
     *
     * @param childrensBeanXX
     */
    private void addThreeMenuList(BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
                                          .ChildrensBeanXXX.ChildrensBeanXX childrensBeanXX) {
        for (BianZhengBean.RelationListBean relationListBean : bianZhengBean
                .relationList) {
            if (childrensBeanXX.id.equals(relationListBean.fieldID)) {
                for (List<BianZhengBean.ModuleListBean.ChildlistBean>
                        childlist : childlists) {
                    for (BianZhengBean.ModuleListBean.ChildlistBean
                            childlistBean : childlist) {
                        if (childlistBean.tagModule.moduleCode.equals
                                (relationListBean.moduleCode)) {
                            if (childrensBeanXX.isCheck) {
                                if (!navChildLists.contains(childlistBean))
                                    navChildLists.add(childlistBean);
                            } else {
                                navChildLists.remove(childlistBean);
                            }
                        }
                    }
                }
            }
            if (navChildLists.size() > 0) {
                menu_button.setVisibility(View.VISIBLE);
            } else {
                isShowMenuList = false;
                menu_button.setVisibility(View.GONE);
                menu_list.setVisibility(View.GONE);
            }
            navAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 添加右侧 四级 五级视图
     *
     * @param childrensBeanXX
     * @param warpLinearLayout
     */
    private void addFiveLevelView(BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
                                          .ChildrensBeanXXX.ChildrensBeanXX childrensBeanXX,
                                  WarpLinearLayout warpLinearLayout) {
        List<View> fourList = new ArrayList<>();
        List<View> fiveList = new ArrayList<>();
        for (final BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean.ChildrensBeanXXX
                .ChildrensBeanXX.ChildrensBeanX childrensBeanX : childrensBeanXX.childrens) {
            TextView fourTitle = getTitle(childrensBeanXX.displayName + "-" + childrensBeanX
                    .displayName);
            warpLinearLayout.addView(fourTitle);
            fourList.add(fourTitle);
            addProgressLay(fourList, childrensBeanX);
            for (final BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean.ChildrensBeanXXX
                    .ChildrensBeanXX.ChildrensBeanX.ChildrensBean childrensBean : childrensBeanX
                    .childrens) {
                if (childrensBeanX.fieldType.equals("checkbox")) {
                    LogUtil.e(childrensBean.displayName + " - - - - " + childrensBean
                            .isCheck);
                    CheckBox fiveCheckBox = getCheckbox(childrensBean.displayName, childrensBean
                            .isCheck);
                    warpLinearLayout.addView(fiveCheckBox);
                    addFiveMenuList(childrensBean);
                    fiveCheckBox.setTag(childrensBean);
                    fiveList.add(fiveCheckBox);
                    fiveCheckBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            childrensBean.isCheck = !childrensBean.isCheck;
                            addFiveMenuList(childrensBean);
                        }
                    });
                } else if ("textarea".equals(childrensBean.fieldType)) {
                    EditText aloneBigEdit = getAloneBigEdit(childrensBean.content);
                    warpLinearLayout.addView(aloneBigEdit);
                    aloneBigEdit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int
                                i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int
                                i2) {
                            childrensBean.content = charSequence.toString();
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                } else if ("radio".equals(childrensBeanX.fieldType)) {
                    final RadioButton radioButton = getRadioButton(childrensBean.displayName,
                            childrensBean.isCheck);
                    warpLinearLayout.addView(radioButton);
                    radioButton.setTag(childrensBean.id);
                    radios.put(childrensBean.id, radioButton);
                    fiveList.add(radioButton);
                    radioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for (BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
                                    .ChildrensBeanXXX.ChildrensBeanXX.ChildrensBeanX.ChildrensBean
                                    childrensBeanXx : childrensBeanX.childrens) {
                                radios.get(childrensBeanXx.id).setChecked(false);
                                childrensBeanXx.isCheck = false;
                            }
                            radioButton.setChecked(true);
                            childrensBean.isCheck = true;
                        }
                    });
                }
            }
            fourLevelMap.put(childrensBeanXX.id, fourList);
            fiveLevelMap.put(childrensBeanXX.id, fiveList);
        }
    }

    /**
     * 添加进度条
     *
     * @param fourList
     * @param childrensBeanX
     */
    private void addProgressLay(List<View> fourList, final BianZhengBean.ModuleListBean
            .ChildlistBean.FildlistBean.ChildrensBeanXXX.ChildrensBeanXX.ChildrensBeanX
            childrensBeanX) {
        if ("score".equals(childrensBeanX.fieldType)) {
            final LinearLayout linearLayout = new LinearLayout(mContext);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
            SeekBar progressBar = getProgressBar(TextUtils.isEmpty
                    (childrensBeanX.content) ? "0" : childrensBeanX.content);
            final TextView normalTv = getNormalTv(TextUtils.isEmpty(childrensBeanX.content)
                    ? "0" : childrensBeanX.content);
            linearLayout.addView(progressBar);
            linearLayout.addView(normalTv);
            bingzheng_container.addView(linearLayout);
            progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    normalTv.setText(progress + "");
                    childrensBeanX.content = progress + "";
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            fourList.add(linearLayout);
        }
    }

    /**
     * 根据五级数据添加menubutton list
     *
     * @param childrensBean
     */
    private void addFiveMenuList(BianZhengBean.ModuleListBean.ChildlistBean.FildlistBean
                                         .ChildrensBeanXXX.ChildrensBeanXX.ChildrensBeanX
                                         .ChildrensBean childrensBean) {
        for (BianZhengBean.RelationListBean relationListBean : bianZhengBean
                .relationList) {
            if (childrensBean.id.equals(relationListBean.fieldID)) {
                for (List<BianZhengBean.ModuleListBean.ChildlistBean>
                        childlist : childlists) {
                    for (BianZhengBean.ModuleListBean.ChildlistBean
                            childlistBean : childlist) {
                        if (childlistBean.tagModule.moduleCode.equals
                                (relationListBean.moduleCode)) {
                            if (childrensBean.isCheck) {
                                if (!navChildLists.contains(childlistBean))
                                    navChildLists.add(childlistBean);
                            } else {
                                navChildLists.remove(childlistBean);
                            }
                        }
                    }
                }
            }
            if (navChildLists.size() > 0) {
                menu_button.setVisibility(View.VISIBLE);
            } else {
                isShowMenuList = false;
                menu_button.setVisibility(View.GONE);
                menu_list.setVisibility(View.GONE);
            }
            navAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 上传图片
     *
     * @param file
     * @param children     当前的bean
     * @param aloneBigEdit 当前的输入框
     */
    public void upLoadPic(final File file, final BianZhengBean.ModuleListBean.ChildlistBean
            .FildlistBean.ChildrensBeanXXX children, final EditText aloneBigEdit) {
        showWaitDialog();
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                String urlStr = Constants.FILE_UP_LOAD_APP_OCR +
                        "&userID=" + DoctorBaseAppliction.spUtil.getString
                        (Constants.USER_ID, "")
                        + "&modules=ocrImage"
                        + "&mobileType=" + Constants.MOBILE_TYPE;
                try {
                    LogUtil.e(file.getAbsoluteFile() + "");
                    byte[] content;
                    content = HcUtils.readStream(file);
                    MultipartBody.Builder builder = new MultipartBody.Builder();
                    builder.setType(MultipartBody.FORM).addFormDataPart("file", file.getName
                            (), RequestBody.create(MediaType.parse
                            ("application/octet-stream"), content));

                    RequestBody requestBody = builder.build();

                    String result = OkHttpUtils.initUpLoad(urlStr, requestBody);
                    LogUtil.e("上传图片药方 -- " + result);
                    final PicUpLoadResultBean picUpLoadResultBean = JSONUtil.parseJsonToBean
                            (result, PicUpLoadResultBean.class);
                    if (!TextUtils.isEmpty(picUpLoadResultBean.contents)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                children.content = picUpLoadResultBean.contents;
                                aloneBigEdit.setText(picUpLoadResultBean.contents);
                                binazheng_list.collapseGroup(currentGroupPosition);
                                binazheng_list.expandGroup(currentGroupPosition);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showMessage(mContext, "解析失败，请重试");
                            }
                        });
                    }
                    subscriber.onNext(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).

                subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object result) {
                        hideWaitDialog();
                    }
                });
    }

    /**
     * 修改 选中条目样式
     *
     * @param groupPositon
     * @param childPosition 如果等于 -1  说明是 父类
     */
    private void changeSelectItemStatu(int groupPositon, int childPosition) {
        for (List<BianZhengBean.ModuleListBean.ChildlistBean> childlist : childlists) {
            for (BianZhengBean.ModuleListBean.ChildlistBean childlistBean : childlist) {
                childlistBean.tagModule.isSelect = false;
            }
        }
        if (childPosition != -1) {
            childlists.get(groupPositon).get(childPosition).tagModule.isSelect = true;
            binazheng_list.collapseGroup(groupPositon);
            binazheng_list.expandGroup(groupPositon);
        }
    }

    /**
     * 获取一级标题
     *
     * @param title
     * @return
     */
    public TextView getTitle(String title) {
        TextView textView = new TextView(mContext);
        textView.setTextSize(18);
        textView.setTextColor(Color.BLACK);
        textView.setText(title);
        textView.setPadding(10, 2, 10, 0);
        textView.setBackgroundColor(Color.parseColor("#44000000"));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(HcUtils.getScreenWidth
                (mContext), LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        return textView;
    }

    /**
     * 获取二级标题
     *
     * @param title
     * @return
     */
    public TextView getFuTitle(String title) {
        TextView textView = new TextView(mContext);
        textView.setTextSize(16);
        textView.setTextColor(Color.parseColor("#55000000"));
        textView.setText(title);
        textView.setPadding(10, 2, 10, 0);
        return textView;
    }

    /**
     * 获取checkbox
     *
     * @param title
     * @param isCheck
     * @return
     */
    public CheckBox getCheckbox(String title, boolean isCheck) {
        CheckBox checkBox = new CheckBox(mContext);
        checkBox.setText(title);
        checkBox.setPadding(10, 2, 10, 0);
        checkBox.setChecked(isCheck);
        return checkBox;
    }


    /**
     * 获取输入框样式
     * suffixName  单位   displayName 名称
     *
     * @param name
     * @param displayName 名称
     * @param suffixName  单位
     * @param name        录入内容
     * @return
     */
    public WarpLinearLayout getEditView(String displayName, String suffixName, String name) {
        WarpLinearLayout linearContainer = getLinearContainer();
        linearContainer.addView(getNormalTv(displayName + ""));
        linearContainer.addView(getNormalEdit(name));
        linearContainer.addView(getNormalTv(suffixName + ""));
        return linearContainer;
    }
    /**
     * 获取选择样式
     * suffixName  单位   displayName 名称
     *
     * @param name
     * @param displayName 名称
     * @param name        录入内容
     * @return
     */
    public LinearLayout getDateView(String displayName, String name) {

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(getNormalTv(displayName + ""));
        TextView normalTv = getNormalTv(name + "");
        if (name.equals("点击请选择")) {
            normalTv.setTextColor(Color.parseColor("#0aa2ed"));
        }
        linearLayout.addView(normalTv);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        return linearLayout;
    }

    /**
     * 获取展示的 TextView
     *
     * @param title
     * @return
     */
    public TextView getNormalTv(String title) {
        TextView textView = new TextView(mContext);
        textView.setTextSize(16);
        textView.setTextColor(Color.parseColor("#99000000"));
        textView.setText(title);
        textView.setPadding(10, 10, 10, 10);
        return textView;
    }

    /**
     * 获取展示的 TextView
     *
     * @param title
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TextView getPicSelectButton(String title) {
        TextView textView = new TextView(mContext);
        textView.setTextSize(16);
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setText(title);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundDrawable(getDrawable(R.drawable.search_bt_bg));
        textView.setPadding(10, 10, 10, 10);
        int m = HcUtils.dp2px(mContext, 8);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(HcUtils.getScreenWidth
                (mContext) / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, m, m, m);
        textView.setLayoutParams(params);
        return textView;
    }

    /**
     * 获取展示的 Edit
     *
     * @return
     */
    public EditText getNormalEdit(String content) {
        EditText editText = new EditText(mContext);
        editText.setTextSize(16);
        editText.setHint("            ");
        editText.setText(content);
        editText.setSingleLine(true);
        editText.setTextColor(Color.parseColor("#99000000"));
        editText.setBackgroundResource(R.drawable.fangan_bg);
        editText.setPadding(16, 2, 16, 0);
        return editText;
    }

    /**
     * 获取单独的大录入框
     *
     * @return
     */
    public EditText getAloneBigEdit(String content) {
        EditText editText = new EditText(mContext);
        editText.setTextSize(16);
        editText.setHint("            ");
        editText.setText(content);
        editText.setSingleLine(false);
        editText.setTextColor(Color.parseColor("#99000000"));
        editText.setBackgroundResource(R.drawable.fangan_bg);
        int m = HcUtils.dp2px(mContext, 8);
        editText.setPadding(m, m, m, m);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(HcUtils.getScreenWidth
                (mContext) / 3 * 2, HcUtils.dp2px(mContext, 68));
        params.setMargins(10, m, m, m);
        editText.setLayoutParams(params);
        return editText;
    }

    /**
     * 单选按钮
     *
     * @return
     */
    public RadioButton getRadioButton(String displayName, boolean isCheck) {
        RadioButton radioButton = new RadioButton(mContext);
        radioButton.setChecked(isCheck);
        radioButton.setText(displayName);
        radioButton.setTextSize(16);
        return radioButton;
    }

    /**
     * 获取单选
     *
     * @return
     */
    public SeekBar getProgressBar(String progress) {
        SeekBar progressBar = new SeekBar(mContext);
        progressBar.setMax(10);
        progressBar.setProgress(Integer.parseInt(progress));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(HcUtils.getScreenWidth
                (mContext) / 5 * 3, HcUtils.dp2px(mContext, 42));
        int m = HcUtils.dp2px(mContext, 8);
        params.setMargins(10, m, 0, m);
        progressBar.setLayoutParams(params);
        return progressBar;
    }
    /**
     * 获取Linaer
     *
     * @return
     */
    public WarpLinearLayout getLinearContainer() {
        WarpLinearLayout fixGridLayout = new WarpLinearLayout(mContext);
        fixGridLayout.setGrivate(1);
        return fixGridLayout;
    }
    /**
     * 初始化时间弹框
     */
    private void initTimePicker() {
        String yearTime = HcUtils.getYearTime(new Date(System.currentTimeMillis()));
        String monthTime = HcUtils.getMonthTime(new Date(System.currentTimeMillis()));
        String dayTime = HcUtils.getDayTime(new Date(System.currentTimeMillis()));
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1988, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2030, 0, 1);
        pvTime = new TimePickerView.Builder(this, new TimePickerView
                .OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date, View v) {
                String found_date = DateUtils.dateToString(date, DateUtils.FORMAT_5);
                TextView tv = (TextView) ((LinearLayout) v).getChildAt(1);
                tv.setText(found_date);
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setCancelColor(getResources().getColor(R.color.main_color))
                .setSubmitColor(getResources().getColor(R.color.main_color))
                .setRangDate(startDate, endDate)
                .setBackgroundId(0x66000000) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
    }
}
