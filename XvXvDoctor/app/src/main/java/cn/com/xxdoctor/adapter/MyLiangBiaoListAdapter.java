package cn.com.xxdoctor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.MyLiangBiaoInfoListBean;

/**
 * Created by chong.han on 2018/9/27.
 */

public class MyLiangBiaoListAdapter extends BaseAdapter {
    private List<MyLiangBiaoInfoListBean.ListBean> mDatas;
    private Context mContext;

    public MyLiangBiaoListAdapter(List<MyLiangBiaoInfoListBean.ListBean> mDatas,
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.my_liangbiao_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        MyLiangBiaoInfoListBean.ListBean listBean = mDatas.get(i);
        viewHolder.my_liangbiao_item_name.setText(listBean.parentModule.moduleName);
        viewHolder.my_liangbiao_item_id.setText("量表ID：" + listBean.parentModule.moduleID);
        viewHolder.my_liangbiao_item_no.setText("量表编号：" + listBean.parentModule.moduleCode);
        //-----1 - 已共享; 0 - 未共享
        if (listBean.parentModule.createUser.equals(DoctorBaseAppliction.spUtil.getString
                (Constants.USER_ID,
                ""))) {
            viewHolder.my_liangbiao_item_share.setVisibility(View.VISIBLE);
            if (listBean.parentModule.isShared == 1) {
                viewHolder.my_liangbiao_item_share.setText("取消共享");
                viewHolder.my_liangbiao_item_share.setBackgroundResource(R.color.juhuang);
            } else {
                viewHolder.my_liangbiao_item_share.setText("共享");
                viewHolder.my_liangbiao_item_share.setBackgroundResource(R.color.main_color);
            }
        } else {
            viewHolder.my_liangbiao_item_share.setVisibility(View.GONE);
        }
        viewHolder.my_liangbiao_item_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick.onButtonClick(i);
            }
        });
        return view;
    }

    public static class ViewHolder {
        public View rootView;
        public TextView my_liangbiao_item_name;
        public TextView my_liangbiao_item_id;
        public TextView my_liangbiao_item_no;
        public Button my_liangbiao_item_share;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.my_liangbiao_item_name = (TextView) rootView.findViewById(R.id
                    .my_liangbiao_item_name);
            this.my_liangbiao_item_id = (TextView) rootView.findViewById(R.id.my_liangbiao_item_id);
            this.my_liangbiao_item_no = (TextView) rootView.findViewById(R.id.my_liangbiao_item_no);
            this.my_liangbiao_item_share = (Button) rootView.findViewById(R.id
                    .my_liangbiao_item_share);
        }

    }

    private onButtonClick onButtonClick;

    public void setOnButtonClick(MyLiangBiaoListAdapter.onButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    public interface onButtonClick {
        void onButtonClick(int position);
    }
}
