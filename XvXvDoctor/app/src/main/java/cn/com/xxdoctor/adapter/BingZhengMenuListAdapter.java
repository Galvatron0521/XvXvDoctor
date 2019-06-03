package cn.com.xxdoctor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.bean.BianZhengBean;

/**
 * Created by chong.han on 2018/8/31.
 */

public class BingZhengMenuListAdapter extends BaseAdapter {
    private List<BianZhengBean.ModuleListBean.ChildlistBean> mDatas;
    private Context mContext;

    public BingZhengMenuListAdapter(List<BianZhengBean.ModuleListBean.ChildlistBean> mDatas,
                                    Context mContext) {
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
        ViewHolder viewHolder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_bianzheng_menu_layout, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.menu_name_tv.setText(mDatas.get(i).tagModule.moduleName);
        return view;
    }

    public static class ViewHolder {
        public View rootView;
        public TextView menu_name_tv;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.menu_name_tv = (TextView) rootView.findViewById(R.id.menu_name_tv);
        }

    }
}
