package cn.com.xxdoctor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.com.xxdoctor.R;

/**
 * Created by chong.han on 2018/9/28.
 */

public class AddSubjectListAdapter extends BaseAdapter {
    private List<String> mDatas;
    private Context mContext;

    public AddSubjectListAdapter(List<String> mDatas, Context mContext) {
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
            view = View.inflate(mContext, R.layout.item_my_subject_layout, null);
            viewholder = new ViewHolder(view);
            view.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) view.getTag();
        }
        viewholder.item_my_subject_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick.onButtonClick(i);
            }
        });
        return view;
    }

    private onButtonClick onButtonClick;

    public void setOnButtonClick(AddSubjectListAdapter.onButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    public interface onButtonClick {
        void onButtonClick(int position);
    }

    public static class ViewHolder {
        public View rootView;
        public ImageView item_my_subject_delete;
        public TextView item_my_subject_first;
        public TextView item_my_subject_second;
        public TextView item_my_subject_three;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.item_my_subject_delete = (ImageView) rootView.findViewById(R.id
                    .item_my_subject_delete);
            this.item_my_subject_first = (TextView) rootView.findViewById(R.id
                    .item_my_subject_first);
            this.item_my_subject_second = (TextView) rootView.findViewById(R.id
                    .item_my_subject_second);
            this.item_my_subject_three = (TextView) rootView.findViewById(R.id
                    .item_my_subject_three);
        }

    }
}
