package cn.com.xxdoctor.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.bean.MyPlanListBean;
import cn.com.xxdoctor.bean.PatientSuiFangListBean;
import cn.com.xxdoctor.view.MyListView;

/**
 * Created by chong.han on 2018/9/29.
 */

public class SuiFangTaskListAdapter extends BaseAdapter {
    private List<PatientSuiFangListBean.ListBean.ProjectlistBean> mDatas;
    private Context mContext;

    public SuiFangTaskListAdapter(List<PatientSuiFangListBean.ListBean.ProjectlistBean> mDatas,
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
        ViewHolder viewholder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_my_plan_layout, null);
            viewholder = new ViewHolder(view);
            view.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) view.getTag();
        }
        PatientSuiFangListBean.ListBean.ProjectlistBean projectlistBean = mDatas.get(i);
        viewholder.item_plan_add_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick.onButtonClick(i, 1);
            }
        });
        if (projectlistBean.optionlist != null && projectlistBean.optionlist.size() > 0)
            if (projectlistBean.optionlist.get(0).taskOption.equals("0")) {
                viewholder.item_plan_add_project.setText("量化指标");
            } else {
                viewholder.item_plan_add_project.setText("量表");
            }
        viewholder.item_plan_task_name.setText("第" + (i + 1) + "次任务");
        if (TextUtils.isEmpty(projectlistBean.beforeOrAfter) || TextUtils.isEmpty(projectlistBean
                .unit)) {
            viewholder.item_plan_time.setText("请选择");
        } else {

            viewholder.item_plan_time.setText("" + projectlistBean.beforeOrAfter + projectlistBean
                    .amount + projectlistBean.unit);

        }
        return view;
    }

    private onButtonClick onButtonClick;

    public void setOnButtonClick(onButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    public interface onButtonClick {
        /**
         * @param position index
         * @param type     0 删除 1 添加任务 2 时间选择  3 添加任务
         */
        void onButtonClick(int position, int type);
    }

    public static class ViewHolder {
        public View rootView;
        public ImageView item_plan_statu_iv;
        public TextView item_plan_task_name;
        public TextView item_plan_time;
        public MyListView item_plan_project_list;
        public TextView item_plan_add_project;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.item_plan_statu_iv = (ImageView) rootView.findViewById(R.id.item_plan_statu_iv);
            this.item_plan_task_name = (TextView) rootView.findViewById(R.id.item_plan_task_name);
            this.item_plan_time = (TextView) rootView.findViewById(R.id.item_plan_time);
            this.item_plan_project_list = (MyListView) rootView.findViewById(R.id
                    .item_plan_project_list);
            this.item_plan_add_project = (TextView) rootView.findViewById(R.id
                    .item_plan_add_project);
        }

    }
}