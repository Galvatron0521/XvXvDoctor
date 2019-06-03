package cn.com.xxdoctor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.bean.PatientListBean;
import cn.com.xxdoctor.utils.DateUtils;

/**
 * Created by chong.han on 2018/8/20.
 */

public class PatientListAdapter extends BaseAdapter {
    private Context mContext;
    private Boolean isFriendList;
    private List<PatientListBean.ListBean> mDatas;

    public PatientListAdapter(Context mContext, List<PatientListBean.ListBean> mDatas, boolean
            isFriendList) {
        this.mContext = mContext;
        this.isFriendList = isFriendList;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_patient_layout_new, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final PatientListBean.ListBean listBean = mDatas.get(i);
        viewHolder.item_patient_logo.setText(listBean.name.substring(0, 1));
        viewHolder.item_patient_name.setText(listBean.name);
        viewHolder.item_patient_time.setText(DateUtils.dateToString(new Date(listBean.createTime)
                , DateUtils.FORMAT_5));
        viewHolder.item_patient_phone.setText(listBean.mobile);
        viewHolder.item_patient_fuzhen.setVisibility(isFriendList ? View.GONE : View.VISIBLE);
        viewHolder.item_patient_suifang.setVisibility(isFriendList ? View.GONE : View.VISIBLE);
        //recordFlag:1首诊 0复诊,根据返回值,如果为0,说明该用户未添加首诊
        if (!isFriendList) {
            if (listBean.recordFlag.equals("1")) {
                viewHolder.item_patient_fuzhen.setTextColor(Color.WHITE);
                viewHolder.item_patient_fuzhen.setBackground(mContext.getDrawable(R.drawable
                        .item_patient_has_shouzhen_fuzhen_bg));
                viewHolder.item_patient_suifang.setTextColor(Color.WHITE);
                viewHolder.item_patient_suifang.setBackground(mContext.getDrawable(R.drawable
                        .item_patient_has_shouzhen_suifang_bg));
            } else {
                viewHolder.item_patient_fuzhen.setTextColor(Color.parseColor("#33000000"));
                viewHolder.item_patient_fuzhen.setBackground(mContext.getDrawable(R.drawable
                        .item_patient_no_shouzhen));
                viewHolder.item_patient_suifang.setTextColor(Color.parseColor("#33000000"));
                viewHolder.item_patient_suifang.setBackground(mContext.getDrawable(R.drawable
                        .item_patient_no_shouzhen));
            }
            viewHolder.item_patient_suifang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listBean.recordFlag.equals("1")) {
                        onViewClickLisener.onClick(i, 2);
                    } else {
                        return;
                    }

                }
            });
            viewHolder.item_patient_fuzhen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listBean.recordFlag.equals("1")) {
                        onViewClickLisener.onClick(i, 1);
                    } else {
                        return;
                    }
                }
            });
            viewHolder.item_patient_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onViewClickLisener.onClick(i, 0);
                }
            });
            viewHolder.item_patient_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onViewClickLisener.onClick(i, 0);
                }
            });
        }
        return view;
    }

    private OnViewClickLisener onViewClickLisener;

    public void setOnViewClickLisener(OnViewClickLisener onViewClickLisener) {
        this.onViewClickLisener = onViewClickLisener;
    }

    public interface OnViewClickLisener {
        /**
         * @param position 点击索引
         * @param type     类型 0 头像  1 复诊  2 随访
         */
        void onClick(int position, int type);
    }

    public static class ViewHolder {
        public View rootView;
        public TextView item_patient_logo;
        public TextView item_patient_name;
        public TextView item_patient_phone;
        public TextView item_patient_time;
        public TextView item_patient_suifang;
        public TextView item_patient_fuzhen;
        public RelativeLayout item_patient_rl;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.item_patient_logo = (TextView) rootView.findViewById(R.id.item_patient_logo);
            this.item_patient_name = (TextView) rootView.findViewById(R.id.item_patient_name);
            this.item_patient_phone = (TextView) rootView.findViewById(R.id.item_patient_phone);
            this.item_patient_time = (TextView) rootView.findViewById(R.id.item_patient_time);
            this.item_patient_suifang = (TextView) rootView.findViewById(R.id.item_patient_suifang);
            this.item_patient_fuzhen = (TextView) rootView.findViewById(R.id.item_patient_fuzhen);
            this.item_patient_rl = (RelativeLayout) rootView.findViewById(R.id.item_patient_rl);
        }

    }
}
