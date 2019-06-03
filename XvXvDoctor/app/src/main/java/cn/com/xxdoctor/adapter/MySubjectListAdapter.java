package cn.com.xxdoctor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.bean.MySubjectListBean;
import cn.com.xxdoctor.utils.DateUtils;

/**
 * Created by chong.han on 2018/8/20.
 */

public class MySubjectListAdapter extends BaseAdapter {
    private Context mContext;
    private List<MySubjectListBean.ListBean> mDatas;

    public MySubjectListAdapter(Context mContext, List<MySubjectListBean.ListBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_my_subject_list_layout, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        MySubjectListBean.ListBean listBean = mDatas.get(i);
        viewHolder.item_my_subject_name.setText(listBean.subjectName);
        viewHolder.item_my_subject_start_time.setText("开始时间：" + DateUtils.dateToString(new Date
                (listBean.startTime), DateUtils.FORMAT_5));
        viewHolder.item_my_subject_end_time.setText("结束时间：" + DateUtils.dateToString(new Date
                (listBean.endTime), DateUtils.FORMAT_5));
        viewHolder.item_my_subject_name.setText(listBean.subjectName);
        return view;
    }

    public static class ViewHolder {
        public View rootView;
        public TextView item_my_subject_name;
        public TextView item_my_subject_start_time;
        public TextView item_my_subject_end_time;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.item_my_subject_name = (TextView) rootView.findViewById(R.id.item_my_subject_name);
            this.item_my_subject_start_time = (TextView) rootView.findViewById(R.id
                    .item_my_subject_start_time);
            this.item_my_subject_end_time = (TextView) rootView.findViewById(R.id
                    .item_my_subject_end_time);
        }

    }
}
