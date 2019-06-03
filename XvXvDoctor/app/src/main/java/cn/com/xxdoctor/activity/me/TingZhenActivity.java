package cn.com.xxdoctor.activity.me;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.adapter.TingZhenListAdapter;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.DateBean;
import cn.com.xxdoctor.bean.ResultBean;
import cn.com.xxdoctor.chat.utils.ToastUtil;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;
import cn.com.xxdoctor.utils.DateUtils;
import cn.com.xxdoctor.utils.HcUtils;

public class TingZhenActivity extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private ListView tingzhen_list;
    private TextView tingzhen_add;
    private List<DateBean> mDatas;
    private TingZhenListAdapter tingZhenListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ting_zhen);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        tingzhen_list = (ListView) findViewById(R.id.tingzhen_list);
        tingzhen_add = (TextView) findViewById(R.id.tingzhen_add);
        title_back.setVisibility(View.VISIBLE);
        title_right_tv.setVisibility(View.VISIBLE);
        title_right_tv.setText("发布");
        title_name.setText("发布停诊");
        initTimePicker();
    }

    @Override
    public void initData() {
        mDatas = new ArrayList<>();
        tingZhenListAdapter = new TingZhenListAdapter(mDatas, mContext);
        tingzhen_list.setAdapter(tingZhenListAdapter);
    }

    @Override
    public void initListener() {
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tingzhen_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show(tingzhen_add);
            }
        });
        tingzhen_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                showAffirmDialog("提示", "您确定要删除该停诊时间吗？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatas.remove(i);
                        tingZhenListAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        title_right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestUpData();
            }
        });

    }

    /**
     * 发布停诊
     * "userID":1, "dateList":[
     * {"date":"2018-09-12"},
     * {"date":"2018-09-18"}]
     */
    private void requestUpData() {
        Map<String, Object> map = new HashMap<>();
        map.put("userID", DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, ""));
        map.put("dateList", mDatas);
        showWaitDialog();
        DoctorNetService.requestAddOrChange(Constants.SUSPEND_MEDICAL, map, new
                NetWorkRequestInterface() {
                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                        ToastUtil.shortToast(mContext, "发布停诊信息失败，请重试");
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        ResultBean bean = (ResultBean) info;
                        ToastUtil.shortToast(mContext, "发布停诊信息成功");
                        finish();
                    }
                });
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
        startDate.set(Integer.parseInt(yearTime), 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2030, 0, 1);
        pvTime = new TimePickerView.Builder(this, new TimePickerView
                .OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date, View v) {
                String found_date = DateUtils.dateToString(date, DateUtils.FORMAT_5);
                for (DateBean mData : mDatas) {
                    if (found_date.equals(mData.date)) {
                        ToastUtil.shortToast(mContext, "停诊日期已添加");
                        return;
                    }
                }
                mDatas.add(new DateBean(found_date));
                tingZhenListAdapter.notifyDataSetChanged();
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
