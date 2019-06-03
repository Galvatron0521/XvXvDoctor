package cn.com.xxdoctor.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.activity.main.LoginActivity;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.base.DoctorBaseFragment;
import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by chong.han on 2018/8/16.
 */

public class MyInfoFragment extends DoctorBaseFragment {


    private RoundedImageView my_info_logo_iv;
    private TextView my_info_name;
    private TextView my_info_phone;
    private LinearLayout my_info_setting_ll;
    private TextView my_info_shengfen_tv;
    private LinearLayout my_info_shengfen_ll;
    private TextView my_info_yiyuan_tv;
    private LinearLayout my_info_yiyuan_ll;
    private TextView my_info_keshi_tv;
    private LinearLayout my_info_keshi_ll;
    private TextView my_info_zhicheng_tv;
    private LinearLayout my_info_zhicheng_ll;
    private LinearLayout my_info_code_ll;
    private Button log_out;
    private LinearLayout my_info_liangbiao_ll;
    private LinearLayout my_info_kecheng_ll;
    private LinearLayout my_info_suifang_fangan_ll;
    private LinearLayout my_info_jiben_shezhi_ll;
    private LinearLayout my_info_tingzhen_ll;
    private LinearLayout my_info_wode_zhanghu_ll;
    private LinearLayout my_info_yijian_ll;

    public static MyInfoFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        MyInfoFragment fragment = new MyInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.my_info_fragment, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {

        my_info_logo_iv = (RoundedImageView) view.findViewById(R.id.my_info_logo_iv);
        my_info_name = (TextView) view.findViewById(R.id.my_info_name);
        my_info_phone = (TextView) view.findViewById(R.id.my_info_phone);
        my_info_setting_ll = (LinearLayout) view.findViewById(R.id.my_info_setting_ll);
        my_info_shengfen_tv = (TextView) view.findViewById(R.id.my_info_shengfen_tv);
        my_info_shengfen_ll = (LinearLayout) view.findViewById(R.id.my_info_shengfen_ll);
        my_info_yiyuan_tv = (TextView) view.findViewById(R.id.my_info_yiyuan_tv);
        my_info_yiyuan_ll = (LinearLayout) view.findViewById(R.id.my_info_yiyuan_ll);
        my_info_keshi_tv = (TextView) view.findViewById(R.id.my_info_keshi_tv);
        my_info_keshi_ll = (LinearLayout) view.findViewById(R.id.my_info_keshi_ll);
        my_info_zhicheng_tv = (TextView) view.findViewById(R.id.my_info_zhicheng_tv);
        my_info_zhicheng_ll = (LinearLayout) view.findViewById(R.id.my_info_zhicheng_ll);
        my_info_code_ll = (LinearLayout) view.findViewById(R.id.my_info_code_ll);
        log_out = (Button) view.findViewById(R.id.log_out);

        my_info_liangbiao_ll = (LinearLayout) view.findViewById(R.id.my_info_liangbiao_ll);
        my_info_kecheng_ll = (LinearLayout) view.findViewById(R.id.my_info_kecheng_ll);
        my_info_suifang_fangan_ll = (LinearLayout) view.findViewById(R.id
                .my_info_suifang_fangan_ll);
        my_info_jiben_shezhi_ll = (LinearLayout) view.findViewById(R.id.my_info_jiben_shezhi_ll);
        my_info_tingzhen_ll = (LinearLayout) view.findViewById(R.id.my_info_tingzhen_ll);
        my_info_yijian_ll = (LinearLayout) view.findViewById(R.id.my_info_yijian_ll);
        my_info_wode_zhanghu_ll = (LinearLayout) view.findViewById(R.id.my_info_wode_zhanghu_ll);
    }

    @Override
    public void initData() {
        my_info_name.setText(DoctorBaseAppliction.spUtil.getString(Constants.Name, ""));
        my_info_phone.setText(DoctorBaseAppliction.spUtil.getString(Constants.MOBILE, ""));
    }

    @Override
    public void initListener() {
        //退出登录
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoctorBaseAppliction.spUtil.clear();
                JMessageClient.logout();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
        //我的二维码
        my_info_code_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CodeActivity.class));
            }
        });
        //停诊
        my_info_tingzhen_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), TingZhenActivity.class));
            }
        });
        //我的量表
        my_info_liangbiao_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MyLiangBiaoListActivity.class));
            }
        });
        //我的课题
        my_info_kecheng_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MySubjectListActivity.class));
            }
        });
        //我的随访方案
        my_info_suifang_fangan_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MyPlanListActivity.class));
            }
        });
        //我的意见
        my_info_yijian_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MySuggestionAct.class));
            }
        });
    }
}
