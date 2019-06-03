/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.com.xxdoctor.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.com.xxdoctor.R;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.toolsfinal.DeviceUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/1 下午8:42
 */
public class PicPhotoListAdapter extends BaseAdapter {
    private List<PhotoInfo> mList;
    private LayoutInflater mInflater;
    private int mScreenWidth;
    private Context mContext;

    public void setCanEdit(boolean canEdit) {
        isCanEdit = canEdit;
    }

    private boolean isCanEdit = true;

    public PicPhotoListAdapter(Activity activity, List<PhotoInfo> list, Context mContext) {
        this.mList = list;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(activity);
        this.mScreenWidth = DeviceUtils.getScreenPix(activity).widthPixels;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_gf_default_photo)
                .showImageOnLoading(R.drawable.ic_gf_default_photo).build();
        View view = mInflater.inflate(R.layout.adapter_photo_list_item, null);
        ImageView iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
        ImageView iv_shanchu = (ImageView) view.findViewById(R.id.iv_shanchu);
        setHeight(view);
        PhotoInfo photoInfo = mList.get(position);
        if (photoInfo.getPhotoId() == -1) {
            iv_shanchu.setVisibility(View.GONE);
        } else {
            iv_shanchu.setVisibility(View.VISIBLE);
        }
        if (isCanEdit && photoInfo.getPhotoId() != -1) {
            iv_shanchu.setVisibility(View.VISIBLE);
        } else {
            iv_shanchu.setVisibility(View.GONE);
        }
        iv_shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelectClickListener.onDelectClick(position);
            }
        });
        if (photoInfo.getPhotoPath().startsWith("http")) {
            ImageLoader.getInstance().displayImage(photoInfo.getPhotoPath(), iv_photo, options);
        } else {
            ImageLoader.getInstance().displayImage("file://" + photoInfo.getPhotoPath(),
                    iv_photo, options);
        }
        return view;
    }

    private void setHeight(final View convertView) {
        int height = mScreenWidth / 3 - 8;
        convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, height));
    }

    private onDelectClickListener onDelectClickListener;

    public void setOnDelectClickListener(PicPhotoListAdapter.onDelectClickListener
                                                 onDelectClickListener) {
        this.onDelectClickListener = onDelectClickListener;
    }

    public interface onDelectClickListener {
        void onDelectClick(int position);
    }
}
