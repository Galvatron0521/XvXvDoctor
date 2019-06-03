package cn.com.xxdoctor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.bean.DoctorListBean;

/**
 * Created by chong.han on 2018/8/20.
 */

public class DoctorListAdapter extends BaseAdapter {
    private Context mContext;
    private List<DoctorListBean.ListBean> mDatas;

    public DoctorListAdapter(Context mContext, List<DoctorListBean.ListBean> mDatas) {
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
            view = View.inflate(mContext, R.layout.item_patient_layout, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        DoctorListBean.ListBean listBean = mDatas.get(i);
        viewHolder.item_patient_logo.setText(listBean.Name.substring(0, 1));
        viewHolder.item_patient_name.setText(listBean.Name);
        viewHolder.item_patient_time.setText("");
        viewHolder.item_patient_phone.setText(listBean.Mobile);
        return view;
    }

    public static class ViewHolder {
        public View rootView;
        public TextView item_patient_logo;
        public TextView item_patient_name;
        public TextView item_patient_time;
        public TextView item_patient_phone;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.item_patient_logo = (TextView) rootView.findViewById(R.id.item_patient_logo);
            this.item_patient_name = (TextView) rootView.findViewById(R.id.item_patient_name);
            this.item_patient_time = (TextView) rootView.findViewById(R.id.item_patient_time);
            this.item_patient_phone = (TextView) rootView.findViewById(R.id.item_patient_phone);
        }

    }
}
