package cn.com.xxdoctor.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.bean.BianZhengBean;

/**
 * Created by chong.han on 2018/8/24.
 */

public class MyExpandableListViewAdapter implements ExpandableListAdapter {
    private Context mContext;
    private List<BianZhengBean.ModuleListBean.ParentModuleBean> parentList;
    private List<List<BianZhengBean.ModuleListBean.ChildlistBean>> childlists;

    public MyExpandableListViewAdapter(Context mContext, List<BianZhengBean.ModuleListBean
            .ParentModuleBean> parentList, List<List<BianZhengBean.ModuleListBean.ChildlistBean
            >> childlists) {
        this.mContext = mContext;
        this.parentList = parentList;
        this.childlists = childlists;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        return parentList == null ? 0 : parentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childlists == null ? 0 : childlists.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childlists.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.bianzheng_item_text, null);
        }
        TextView item_name = view.findViewById(R.id.item_bianzheng_name);
        BianZhengBean.ModuleListBean.ParentModuleBean parentModuleBean = parentList.get
                (groupPosition);
        item_name.setText(parentModuleBean.moduleName);
        item_name.setTextSize(16);
        item_name.setBackgroundColor(Color.parseColor("#22000000"));
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
            view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.bianzheng_item_text, null);
        }
        TextView item_name = view.findViewById(R.id.item_bianzheng_name);
        BianZhengBean.ModuleListBean.ChildlistBean.TagModuleBean parentModuleBean = childlists.get
                (groupPosition).get(childPosition).tagModule;
        item_name.setText(parentModuleBean.moduleName);
        item_name.setTextSize(14);
        if (childlists.get(groupPosition).size() == 1) {
            item_name.setVisibility(View.GONE);
        } else {
            item_name.setVisibility(View.VISIBLE);
            if (parentModuleBean.isSelect) {
                item_name.setBackgroundColor(Color.parseColor("#55000000"));
            } else {
                item_name.setBackgroundColor(Color.WHITE);
            }
        }
        return view;
    }

    //子item点击事件开关
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}
