package cn.com.xxdoctor.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.bean.SuiFangFangAnBean;

/**
 * Created by chong.han on 2018/8/23.
 */

public class SuiFangFangAnListAdapter extends BaseAdapter {
    private Context mContext;
    private List<SuiFangFangAnBean.ListBean> mDatas;

    public SuiFangFangAnListAdapter(Context mContext, List<SuiFangFangAnBean.ListBean> mDatas) {
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
            view = View.inflate(mContext, R.layout.item_suifang_fangan_layout, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        SuiFangFangAnBean.ListBean listBean = mDatas.get(i);
        viewHolder.item_suifang_fangan_name.setText("随访方案名称：" + listBean.followName);
        viewHolder.item_suifang_fangan_miaoshu.setText("随访方案描述：" + (TextUtils.isEmpty(listBean
                .followDescript) ? "暂无描述信息" : listBean.followDescript));
        return view;
    }


    public static class ViewHolder {
        public View rootView;
        public TextView item_suifang_fangan_name;
        public TextView item_suifang_fangan_miaoshu;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.item_suifang_fangan_name = (TextView) rootView.findViewById(R.id
                    .item_suifang_fangan_name);
            this.item_suifang_fangan_miaoshu = (TextView) rootView.findViewById(R.id
                    .item_suifang_fangan_miaoshu);
        }

    }
}