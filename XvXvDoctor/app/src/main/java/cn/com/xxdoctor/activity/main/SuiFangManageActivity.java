package cn.com.xxdoctor.activity.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.adapter.SuiFangManageAdapter;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.fragment.DaiSuiFangJiLuFragment;
import cn.com.xxdoctor.fragment.SuiFangJiLuFragment;

public class SuiFangManageActivity extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private TabLayout suifang_manege_tab;
    private ViewPager suifang_manege_viewpager;
    private SuiFangManageAdapter suiFangManageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sui_fang_manage);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        suifang_manege_tab = (TabLayout) findViewById(R.id.suifang_manege_tab);
        suifang_manege_viewpager = (ViewPager) findViewById(R.id.suifang_manege_viewpager);
        title_back.setVisibility(View.VISIBLE);
        title_name.setText("我的随访");
    }

    @Override
    public void initData() {
        suiFangManageAdapter = new SuiFangManageAdapter(getSupportFragmentManager());
        suifang_manege_viewpager.setAdapter(suiFangManageAdapter);
        List<Fragment> list = new ArrayList<>();
        list.add(new SuiFangJiLuFragment());
        list.add(new DaiSuiFangJiLuFragment());
        suiFangManageAdapter.setList(list);
        suifang_manege_tab.setupWithViewPager(suifang_manege_viewpager);//将TabLayout和ViewPager关联起来。

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


}
