// Generated code from Butter Knife. Do not modify!
package cn.com.xxdoctor.chat.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Person2CodeActivity$$ViewBinder<T extends cn.com.xxdoctor.chat.activity.Person2CodeActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131231287, "field 'llBack'");
    target.llBack = finder.castView(view, 2131231287, "field 'llBack'");
    view = finder.findRequiredView(source, 2131231182, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131231182, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131231767, "field 'tvUserName'");
    target.tvUserName = finder.castView(view, 2131231767, "field 'tvUserName'");
    view = finder.findRequiredView(source, 2131231195, "field 'ivErWeiMa'");
    target.ivErWeiMa = finder.castView(view, 2131231195, "field 'ivErWeiMa'");
    view = finder.findRequiredView(source, 2131231214, "field 'ivSave'");
    target.ivSave = finder.castView(view, 2131231214, "field 'ivSave'");
    view = finder.findRequiredView(source, 2131231289, "field 'llCopy'");
    target.llCopy = finder.castView(view, 2131231289, "field 'llCopy'");
  }

  @Override public void unbind(T target) {
    target.llBack = null;
    target.ivAvatar = null;
    target.tvUserName = null;
    target.ivErWeiMa = null;
    target.ivSave = null;
    target.llCopy = null;
  }
}
