package cn.com.xxdoctor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.bean.MyPlanCRFListBean;
import cn.com.xxdoctor.bean.TagBean;

/**
 * Created by chong.han on 2018/8/20.
 */

public class MyPlanCrfListAdapter extends BaseAdapter {
    private Context mContext;
    public int ON_EDIT_CLICK = 0;
    public int ON_DELETE_CLICK = 1;
    private List<MyPlanCRFListBean.CrflistBean> mDatas;
    private boolean isCanEdit = true;

    public MyPlanCrfListAdapter(Context mContext, List<MyPlanCRFListBean.CrflistBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    public void setCanEdit(boolean canEdit) {
        isCanEdit = canEdit;
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
            view = View.inflate(mContext, R.layout.item_my_plan_crf, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        MyPlanCRFListBean.CrflistBean bean = mDatas.get(i);
        viewHolder.item_my_plan_crf_name.setText(bean.moduleName);
        if (isCanEdit) {
            viewHolder.item_my_plan_crf_checkbox.setVisibility(View.VISIBLE);
            viewHolder.item_my_plan_crf_checkbox.setChecked(bean.isCheck);

            viewHolder.item_my_plan_crf_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onButtonClickListener.onClick(0, i);
                }
            });
        } else {
            viewHolder.item_my_plan_crf_checkbox.setVisibility(View.GONE);
        }
        return view;
    }


    private onButtonClickListener onButtonClickListener;

    public void setOnButtonClickListener(MyPlanCrfListAdapter.onButtonClickListener
                                                 onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public interface onButtonClickListener {
        void onClick(int type, int position);
    }

    public static class ViewHolder {
        public View rootView;
        public TextView item_my_plan_crf_name;
        public CheckBox item_my_plan_crf_checkbox;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.item_my_plan_crf_name = (TextView) rootView.findViewById(R.id
                    .item_my_plan_crf_name);
            this.item_my_plan_crf_checkbox = (CheckBox) rootView.findViewById(R.id
                    .item_my_plan_crf_checkbox);
        }

    }
}
