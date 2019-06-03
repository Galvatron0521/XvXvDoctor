package cn.com.xxdoctor.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.bean.BingZhengListBean;
import cn.com.xxdoctor.utils.HcUtils;
import cn.com.xxdoctor.view.TouchImageView;
import cn.finalteam.galleryfinal.widget.HorizontalListView;

/**
 * Created by chong.han on 2018/8/28.
 */

public class BingZhangListAdapter extends BaseAdapter {
    private Context mContext;
    private Activity activity;
    private List<BingZhengListBean.ListBean> mDatas;
    private Map<Integer, ItemBingChengManageImageAdapter> map;
    private PopupWindow pop;

    public BingZhangListAdapter(Context mContext, List<BingZhengListBean.ListBean> mDatas,
                                Activity activity) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.activity = activity;
        map = new HashMap<>();
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
            view = View.inflate(mContext, R.layout.item_bingzhang_list, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final BingZhengListBean.ListBean listBean = mDatas.get(i);
        // 0 复诊 1 首诊 2 无
        String beizhu = "";
        if (listBean.recordFlag.equals("0")) {
            beizhu = "复诊";
        } else if (listBean.recordFlag.equals("1")) {
            beizhu = "首诊";
        } else if (listBean.recordFlag.equals("2")) {
            beizhu = "无";
        }
        viewHolder.item_bingzheng_beizhu_tv.setText("备注：" + beizhu);
        if (!TextUtils.isEmpty(listBean.fieldRecordDate)) {
            viewHolder.item_bingzheng_time_tv.setText("时间：" + HcUtils.getData(Long.parseLong
                    (listBean.fieldRecordDate)));
        }
        viewHolder.item_bingzheng_no_tv.setText("编号：" + listBean.fieldRecordSign);
        if (listBean.imagelist.size() == 0) {
            viewHolder.item_bingzheng_hlist.setVisibility(View.GONE);
        } else {
            viewHolder.item_bingzheng_hlist.setVisibility(View.VISIBLE);
            ItemBingChengManageImageAdapter itemBingChengManageImageAdapter = new
                    ItemBingChengManageImageAdapter(listBean.imagelist);
            viewHolder.item_bingzheng_hlist.setAdapter(itemBingChengManageImageAdapter);
            map.put(i, itemBingChengManageImageAdapter);
            viewHolder.item_bingzheng_hlist.setOnItemClickListener(new AdapterView
                    .OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int childPosition,
                                        long l) {
                    showPop(Constants.IP + listBean.imagelist.get(childPosition).fileUrl);
                }
            });
        }
        viewHolder.item_bingzheng_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClickListener.onDeleteClick(i);
            }
        });
        viewHolder.item_bingzheng_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(i);
            }
        });
        return view;
    }

    public void showPop(String url) {
        TouchImageView contentView = new TouchImageView(mContext);
        pop = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 产生背景变暗效果
        Picasso.with(mContext).load(url).into(contentView);
        WindowManager.LayoutParams lp = activity.getWindow()
                .getAttributes();
        lp.alpha = 0.4f;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
        pop.setTouchable(true);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOutsideTouchable(true);
        pop.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        pop.update();
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                activity.getWindow().setAttributes(lp);
            }
        });

    }

    public class ItemBingChengManageImageAdapter extends BaseAdapter {
        private List<BingZhengListBean.ImageList> mDatas;

        public ItemBingChengManageImageAdapter(List<BingZhengListBean.ImageList> mDatas) {
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
            if (view == null) {
                view = View.inflate(mContext, R.layout.item_bingzheng_list_image, null);
            }
            ImageView imageView = view.findViewById(R.id.item_iv_photo);
            BingZhengListBean.ImageList imageList = mDatas.get(i);
            ImageLoader.getInstance().displayImage(Constants.IP + imageList.fileUrl, imageView);
            return view;
        }
    }
    private OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public static class ViewHolder {
        public View rootView;
        public ImageView item_bingzheng_list_iv;
        public TextView item_bingzheng_no_tv;
        public TextView item_bingzheng_time_tv;
        public TextView item_bingzheng_beizhu_tv;
        public HorizontalListView item_bingzheng_hlist;
        public Button item_bingzheng_delete;
        public LinearLayout item_bingzheng_container;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.item_bingzheng_list_iv = (ImageView) rootView.findViewById(R.id
                    .item_bingzheng_list_iv);
            this.item_bingzheng_no_tv = (TextView) rootView.findViewById(R.id.item_bingzheng_no_tv);
            this.item_bingzheng_time_tv = (TextView) rootView.findViewById(R.id
                    .item_bingzheng_time_tv);
            this.item_bingzheng_beizhu_tv = (TextView) rootView.findViewById(R.id
                    .item_bingzheng_beizhu_tv);
            this.item_bingzheng_hlist = (HorizontalListView) rootView.findViewById(R.id
                    .item_bingzheng_hlist);
            this.item_bingzheng_delete = (Button) rootView.findViewById(R.id.item_bingzheng_delete);
            this.item_bingzheng_container = (LinearLayout) rootView.findViewById(R.id.item_bingzheng_container);
        }

    }

}
