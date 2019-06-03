package cn.com.xxdoctor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.bean.TagBean;

/**
 * Created by chong.han on 2018/8/20.
 */

public class PatientTagListAdapter extends BaseAdapter {
    private Context mContext;
    private List<TagBean> mDatas;

    public PatientTagListAdapter(Context mContext, List<TagBean> mDatas) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_patient_tag_layout, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final TagBean bean = mDatas.get(i);
        viewHolder.item_patient_tag_name.setText(bean.lableName);
        viewHolder.item_patient_tag_cb.setChecked(bean.isCheck);
        viewHolder.item_patient_tag_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bean.isCheck = !bean.isCheck;
            }
        });
        return view;
    }

    public static class ViewHolder {
        public View rootView;
        public CheckBox item_patient_tag_cb;
        public TextView item_patient_tag_name;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.item_patient_tag_cb = (CheckBox) rootView.findViewById(R.id.item_patient_tag_cb);
            this.item_patient_tag_name = (TextView) rootView.findViewById(R.id
                    .item_patient_tag_name);
        }

    }
}
