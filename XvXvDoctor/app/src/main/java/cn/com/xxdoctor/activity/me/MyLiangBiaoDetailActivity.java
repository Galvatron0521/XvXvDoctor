package cn.com.xxdoctor.activity.me;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.Serializable;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.bean.BianZhengDetailBean;
import cn.com.xxdoctor.bean.LiangBiaoInfoBean;
import cn.com.xxdoctor.bean.MyLiangBiaoInfoListBean;
import cn.com.xxdoctor.utils.HcUtils;
import cn.com.xxdoctor.utils.LogUtil;
import cn.com.xxdoctor.view.WarpLinearLayout;

public class MyLiangBiaoDetailActivity extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private LinearLayout liangbiao_container;
    private String title;
    private MyLiangBiaoInfoListBean.ListBean info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_liang_biao_detail);
        title = getIntent().getStringExtra("title");
        info = (MyLiangBiaoInfoListBean.ListBean) getIntent().getSerializableExtra("info");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        liangbiao_container = (LinearLayout) findViewById(R.id.my_liangbiao_container);
        title_back.setVisibility(View.VISIBLE);
        title_name.setText(title);
    }

    @Override
    public void initData() {
        //添加 右侧视图  一级
        for (MyLiangBiaoInfoListBean.ListBean.FildlistBean fildlistBean : info.fildlist) {
            // title,textarea,checkbox,radio,text,select
            liangbiao_container.addView(getTitle(fildlistBean.fieldName));
            //添加 右侧视图  二级
            for (final MyLiangBiaoInfoListBean.ListBean.FildlistBean.ChildrensBeanX children :
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
                finish();
            }
        });
    }

    /**
     * 添加右侧三级视图
     *
     * @param children
     */
    private void addThreeLevelView(final MyLiangBiaoInfoListBean.ListBean.FildlistBean
            .ChildrensBeanX children) {
        WarpLinearLayout fixGridLayout = getLinearContainer();
        liangbiao_container.addView(fixGridLayout);
        for (final MyLiangBiaoInfoListBean.ListBean.FildlistBean.ChildrensBeanX.ChildrensBean
                childrensBeanXX : children
                .childrens) {
            if ("radio".equals(children.fieldType)) {
                final RadioButton radioButton = getRadioButton(childrensBeanXX.displayName,
                        false);
                fixGridLayout.addView(radioButton);
//                radios.put(childrensBeanXX.id, radioButton);
//                radioButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        String oldId = "";
//                        for (LiangBiaoInfoBean.ModuleListBean.FildlistBean.ChildrensBeanX
//                                .ChildrensBean beanXX : children.childrens) {
//                            if (beanXX.isCheck) oldId = beanXX.id;
//                            radios.get(beanXX.id).setChecked(false);
//                            beanXX.isCheck = false;
//                        }
//                        radioButton.setChecked(true);
//                        childrensBeanXX.isCheck = true;
//                    }
//                });
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
        radioButton.setClickable(false);
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
}
