package cn.com.xxdoctor.activity.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.activity.me.MyInfoFragment;
import cn.com.xxdoctor.adapter.MainViewPagerAdapter;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.chat.activity.fragment.ConversationListFragment;
import cn.com.xxdoctor.fragment.FriendListFragment;
import cn.com.xxdoctor.fragment.PatientListFragment;

public class MainActivity extends DoctorBaseActivity {

    private ImageView mTitleBack;
    private TextView mTitleName;
    private TextView mTitleRightTv;
    private ViewPager mMainViewp;
    private BottomNavigationView mMainBnv;
    private MenuItem menuItem;
    private MainViewPagerAdapter mMainViewpAdapter;
    private String[] titles = {"消息", "患者", "好友", "我"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_main);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initData() {
        mMainViewpAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mMainViewp.setAdapter(mMainViewpAdapter);
        List<Fragment> list = new ArrayList<>();
        list.add(new ConversationListFragment());
        list.add(PatientListFragment.newInstance(""));
        list.add(FriendListFragment.newInstance("好友"));
        list.add(MyInfoFragment.newInstance(""));
        mMainViewpAdapter.setList(list);
    }

    @Override
    public void initListener() {
        mMainBnv.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mMainViewp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTitleName.setText(titles[position]);
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    mMainBnv.getMenu().getItem(0).setChecked(false);
                }
                menuItem = mMainBnv.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        mTitleBack = (ImageView) findViewById(R.id.title_back);
        mTitleName = (TextView) findViewById(R.id.title_name);
        mTitleRightTv = (TextView) findViewById(R.id.title_right_tv);
        mMainViewp = (ViewPager) findViewById(R.id.main_viewp);
        mMainBnv = (BottomNavigationView) findViewById(R.id.main_bnv);

        mTitleName.setText("消息");

    }

    private BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener = new BottomNavigationView
            .OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.menu_info:
                    mMainViewp.setCurrentItem(0);
                    mTitleName.setText("消息");
                    return true;
                case R.id.menu_patient:
                    mMainViewp.setCurrentItem(1);
                    mTitleName.setText("患者");
                    return true;
                case R.id.menu_friend:
                    mMainViewp.setCurrentItem(2);
                    mTitleName.setText("好友");
                    return true;
                case R.id.menu_me:
                    mMainViewp.setCurrentItem(3);
                    mTitleName.setText("我");
                    return true;
            }
            return false;
        }
    };
    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK://点击返回键
                long secondTime = System.currentTimeMillis();//以毫秒为单位
                if (secondTime - firstTime > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;
                } else {
                    finish();
                    System.exit(0);
                }
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }

}
