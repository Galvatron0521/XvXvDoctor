// Generated code from Butter Knife. Do not modify!
package cn.com.xxdoctor.chat.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CommonScanActivity$$ViewBinder<T extends cn.com.xxdoctor.chat.activity.CommonScanActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131231567, "field 'scan_image'");
    target.scan_image = finder.castView(view, 2131231567, "field 'scan_image'");
    view = finder.findRequiredView(source, 2131230828, "field 'authorize_return'");
    target.authorize_return = finder.castView(view, 2131230828, "field 'authorize_return'");
    view = finder.findRequiredView(source, 2131230937, "field 'title'");
    target.title = finder.castView(view, 2131230937, "field 'title'");
    view = finder.findRequiredView(source, 2131231566, "field 'scan_hint'");
    target.scan_hint = finder.castView(view, 2131231566, "field 'scan_hint'");
    view = finder.findRequiredView(source, 2131231759, "field 'tv_scan_result'");
    target.tv_scan_result = finder.castView(view, 2131231759, "field 'tv_scan_result'");
  }

  @Override public void unbind(T target) {
    target.scan_image = null;
    target.authorize_return = null;
    target.title = null;
    target.scan_hint = null;
    target.tv_scan_result = null;
  }
}
