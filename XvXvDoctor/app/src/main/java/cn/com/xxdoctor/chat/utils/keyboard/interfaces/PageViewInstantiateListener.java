package cn.com.xxdoctor.chat.utils.keyboard.interfaces;

import android.view.View;
import android.view.ViewGroup;

import cn.com.xxdoctor.chat.utils.keyboard.data.PageEntity;

public interface PageViewInstantiateListener<T extends PageEntity> {

    View instantiateItem(ViewGroup container, int position, T pageEntity);
}
