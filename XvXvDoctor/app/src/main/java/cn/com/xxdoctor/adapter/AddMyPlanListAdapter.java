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
import cn.com.xxdoctor.view.MyListView;

/**
 * Created by chong.han on 2018/9/29.
 */

public class AddMyPlanListAdapter extends BaseAdapter {
    private List<MyPlanListBean.FollowlistBean.ProjectlistBean> mDatas;
    private Context mContext;

    public AddMyPlanListAdapter(List<MyPlanListBean.FollowlistBean.ProjectlistBean> mDatas,
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
        MyPlanListBean.FollowlistBean.ProjectlistBean projectlistBean = mDatas.get(i);
        viewholder.item_plan_statu_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == 0)
                    return;
                onButtonClick.onButtonClick(i, 0);
            }
        });
        viewholder.item_plan_add_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick.onButtonClick(i, 1);
            }
        });
        viewholder.item_plan_add_project_liangbiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick.onButtonClick(i, 3);
            }
        });
        viewholder.item_plan_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick.onButtonClick(i, 2);
            }
        });
        if (i == 0) {
            viewholder.item_plan_statu_iv.setImageResource(R.drawable.blue_circle);
        } else {
            viewholder.item_plan_statu_iv.setImageResource(R.drawable.tingzhen_delete);
        }
        if (TextUtils.isEmpty(projectlistBean.project.task_name)) {
            viewholder.item_plan_add_project.setText("无量化指标任务项目");
        } else {
            viewholder.item_plan_add_project.setText(projectlistBean.project.task_name);
        }
        if (TextUtils.isEmpty(projectlistBean.project.task_name2)) {
            viewholder.item_plan_add_project_liangbiao.setText("无量表任务项目");
        } else {
            viewholder.item_plan_add_project_liangbiao.setText(projectlistBean.project.task_name2);
        }
//        viewholder.item_plan_task_name.setText("第" + projectlistBean.taskNum + "次任务");
        viewholder.item_plan_task_name.setText("第" + (i + 1) + "次任务");
        if (TextUtils.isEmpty(projectlistBean.project.beforeOrAfter) || TextUtils.isEmpty(projectlistBean
                .project.unit)) {
            viewholder.item_plan_time.setText("请选择");
        } else {

            viewholder.item_plan_time.setText("" + projectlistBean.project.beforeOrAfter + projectlistBean
                    .project.amount + projectlistBean.project.unit);

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
         * @param type     0 删除 1 添加量化指标任务 2 时间选择  3 添加量表任务
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
        public TextView item_plan_add_project_liangbiao;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.item_plan_statu_iv = (ImageView) rootView.findViewById(R.id.item_plan_statu_iv);
            this.item_plan_task_name = (TextView) rootView.findViewById(R.id.item_plan_task_name);
            this.item_plan_time = (TextView) rootView.findViewById(R.id.item_plan_time);
            this.item_plan_project_list = (MyListView) rootView.findViewById(R.id
                    .item_plan_project_list);
            this.item_plan_add_project = (TextView) rootView.findViewById(R.id
                    .item_plan_add_project);
            this.item_plan_add_project_liangbiao = (TextView) rootView.findViewById(R.id
                    .item_plan_add_project_liangbiao);
        }

    }
}