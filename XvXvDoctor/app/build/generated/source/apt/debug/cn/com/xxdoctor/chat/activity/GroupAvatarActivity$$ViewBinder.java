// Generated code from Butter Knife. Do not modify!
package cn.com.xxdoctor.chat.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GroupAvatarActivity$$ViewBinder<T extends cn.com.xxdoctor.chat.activity.GroupAvatarActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131231287, "field 'llBack'");
    target.llBack = finder.castView(view, 2131231287, "field 'llBack'");
    view = finder.findRequiredView(source, 2131231214, "field 'ivSave'");
    target.ivSave = finder.castView(view, 2131231214, "field 'ivSave'");
    view = finder.findRequiredView(source, 2131231204, "field 'ivGroupAvatar'");
    target.ivGroupAvatar = finder.castView(view, 2131231204, "field 'ivGroupAvatar'");
  }

  @Override public void unbind(T target) {
    target.llBack = null;
    target.ivSave = null;
    target.ivGroupAvatar = null;
  }
}
