package cn.com.xxdoctor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.bean.DateBean;

/**
 * Created by chong.han on 2018/9/27.
 */

public class TingZhenListAdapter extends BaseAdapter {
    private List<DateBean> mDatas;
    private Context mContext;

    public TingZhenListAdapter(List<DateBean> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
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
        ViewHolder viewholder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.tingzhen_list_item, null);
            viewholder = new ViewHolder(view);
            view.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) view.getTag();
        }
        viewholder.tingzhen_item_tv.setText("停诊时间：" + mDatas.get(i).date);
        return view;
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tingzhen_item_tv;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tingzhen_item_tv = (TextView) rootView.findViewById(R.id.tingzhen_item_tv);
        }

    }
}
