package cn.com.xxdoctor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.bean.PatientSuiFangListBean;

/**
 * Created by chong.han on 2018/8/20.
 */

public class SuiFangListAdapter extends BaseAdapter {
    private Context mContext;
    private List<PatientSuiFangListBean.ListBean> mDatas;

    public SuiFangListAdapter(Context mContext, List<PatientSuiFangListBean.ListBean> mDatas) {
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
            view = View.inflate(mContext, R.layout.item_suifang_layout, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        PatientSuiFangListBean.ListBean listBean = mDatas.get(i);

        String suiFangType = "";
        if (listBean.followType.equals("1")) {
            suiFangType = "门诊随访";
        } else if (listBean.followType.equals("2")) {
            suiFangType = "住院随访";
        } else if (listBean.followType.equals("3")) {
            suiFangType = "电话随访";
        } else { //4
            suiFangType = "其他方式";
        }
        viewHolder.item_suifang_person.setText("随访人员：" + listBean.createUser);
        viewHolder.item_suifang_time.setText("患者姓名：" + listBean.name);
        viewHolder.item_suifang_type.setText("随访方式：" + suiFangType);
        //endFlag  0 进行中  1 结束
//        if (listBean.endFlag.equals("0")) {
        viewHolder.item_suifang_statu_iv.setImageResource(R.drawable.doting);
//        } else {
//            viewHolder.item_suifang_statu_iv.setImageResource(R.drawable.dot);
//        }
        return view;
    }

    public static class ViewHolder {
        public View rootView;
        public ImageView item_suifang_statu_iv;
        public TextView item_suifang_time;
        public TextView item_suifang_type;
        public TextView item_suifang_person;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.item_suifang_statu_iv = (ImageView) rootView.findViewById(R.id
                    .item_suifang_statu_iv);
            this.item_suifang_time = (TextView) rootView.findViewById(R.id.item_suifang_time);
            this.item_suifang_type = (TextView) rootView.findViewById(R.id.item_suifang_type);
            this.item_suifang_person = (TextView) rootView.findViewById(R.id.item_suifang_person);
        }

    }
}
