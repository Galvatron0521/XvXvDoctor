package cn.com.xxdoctor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.bean.LiangBiaoInfoBean;

/**
 * Created by chong.han on 2018/8/30.
 */

public class LiangBiaoListAdapter extends BaseAdapter {
    private List<LiangBiaoInfoBean.ModuleListBean.ParentModuleBean> mDatas;
    private Context mContext;

    public LiangBiaoListAdapter(List<LiangBiaoInfoBean.ModuleListBean.ParentModuleBean> mDatas,
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
        if (view == null) {
            view = View.inflate(mContext, R.layout.bianzheng_item_text, null);
        }
        TextView item_name = view.findViewById(R.id.item_bianzheng_name);
        LiangBiaoInfoBean.ModuleListBean.ParentModuleBean parentModuleBean = mDatas.get(i);
        item_name.setText(parentModuleBean.moduleName);
        item_name.setTextSize(14);
        item_name.setGravity(Gravity.CENTER);
        item_name.setBackgroundColor(Color.parseColor("#33000000"));
        return view;
    }
}
