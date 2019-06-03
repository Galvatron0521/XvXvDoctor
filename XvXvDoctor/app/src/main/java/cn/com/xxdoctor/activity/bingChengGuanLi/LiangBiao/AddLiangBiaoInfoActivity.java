package cn.com.xxdoctor.activity.bingChengGuanLi.LiangBiao;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.bean.LiangBiaoInfoBean;
import cn.com.xxdoctor.utils.HcUtils;
import cn.com.xxdoctor.utils.LogUtil;
import cn.com.xxdoctor.view.WarpLinearLayout;

/**
 * 用于展示 三个条目点击之后的 详情
 */
public class AddLiangBiaoInfoActivity extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private LinearLayout liangbiao_container;
    private int position;
    private String patientID;
    private String recordFlag;
    private LiangBiaoInfoBean.ModuleListBean moduleListBean;
    Map<String, RadioButton> radios = new HashMap<>();
    public static String ADD_LIANG_BIAN_FINISH = "Add_liang_bian_finish";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moduleListBean = (LiangBiaoInfoBean.ModuleListBean) getIntent()
                .getSerializableExtra("info");
        position = getIntent().getIntExtra("position", -1);
        for (LiangBiaoInfoBean.ModuleListBean.FildlistBean fildlistBean : moduleListBean.fildlist) {
            for (LiangBiaoInfoBean.ModuleListBean.FildlistBean.ChildrensBeanX children :
                    fildlistBean.childrens) {
                for (LiangBiaoInfoBean.ModuleListBean.FildlistBean.ChildrensBeanX.ChildrensBean
                        childrensBean : children.childrens) {
                    if (fildlistBean.isCheck) {
                        LogUtil.e(childrensBean.displayName + "--" + childrensBean.id);
                    }
                }
            }
        }
        setContentView(R.layout.activity_add_liang_biao_info);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        liangbiao_container = (LinearLayout) findViewById(R.id.liangbiao_container);

        title_back.setVisibility(View.VISIBLE);
        title_name.setText(moduleListBean.parentModule.moduleName);
    }

    @Override
    public void initData() {
        //添加 右侧视图  一级
        for (LiangBiaoInfoBean.ModuleListBean.FildlistBean fildlistBean : moduleListBean.fildlist) {
            // title,textarea,checkbox,radio,text,select
            liangbiao_container.addView(getTitle(fildlistBean.fieldName));
            //添加 右侧视图  二级
            for (final LiangBiaoInfoBean.ModuleListBean.FildlistBean.ChildrensBeanX children :
                    fildlistBean.childrens) {
                liangbiao_container.addView(getFuTitle(children.displayName));
                //添加  右侧视图 三级
                addThreeLevelView(children);
            }
        }
    }

    @Override
    public void initListener() {
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * 添加右侧三级视图
     *
     * @param children
     */
    private void addThreeLevelView(final LiangBiaoInfoBean.ModuleListBean.FildlistBean
            .ChildrensBeanX children) {
        WarpLinearLayout fixGridLayout = getLinearContainer();
        liangbiao_container.addView(fixGridLayout);
        for (final LiangBiaoInfoBean.ModuleListBean.FildlistBean.ChildrensBeanX.ChildrensBean
                childrensBeanXX : children
                .childrens) {
            if ("radio".equals(children.fieldType)) {
                final RadioButton radioButton = getRadioButton(childrensBeanXX.displayName,
                        childrensBeanXX.isCheck);
                fixGridLayout.addView(radioButton);
                radios.put(childrensBeanXX.id, radioButton);
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String oldId = "";
                        for (LiangBiaoInfoBean.ModuleListBean.FildlistBean.ChildrensBeanX
                                .ChildrensBean beanXX : children.childrens) {
                            if (beanXX.isCheck) oldId = beanXX.id;
                            radios.get(beanXX.id).setChecked(false);
                            beanXX.isCheck = false;
                        }
                        radioButton.setChecked(true);
                        childrensBeanXX.isCheck = true;
                    }
                });
            }
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
        textView.setTextSize(16);
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
        textView.setTextSize(14);
        textView.setTextColor(Color.parseColor("#55000000"));
        textView.setText(title);
        textView.setPadding(10, 2, 10, 0);
        return textView;
    }

    /**
     * 获取单选
     *
     * @return
     */
    public RadioButton getRadioButton(String displayName, boolean isCheck) {
        RadioButton radioButton = new RadioButton(mContext);
        radioButton.setChecked(isCheck);
        radioButton.setText(displayName);
        radioButton.setTextSize(12);
        return radioButton;
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

    @Override
    public void onBackPressed() {
        Map<String, Object> map = new HashMap<>();
        map.put("position", position);
        map.put(Constants.EVENTBUS_TYEPE, ADD_LIANG_BIAN_FINISH);
        map.put(Constants.EVENTBUS_VALUE, moduleListBean);
        EventBus.getDefault().post(map);
        finish();
    }
}
