package cn.com.xxdoctor.base;

import android.support.v4.app.Fragment;

import com.android.tu.loadingdialog.LoadingDailog;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by chong.han on 2018/8/20.
 */

public abstract class DoctorBaseFragment extends Fragment {

    public abstract void initData();

    private LoadingDailog.Builder loadBuilder;
    private LoadingDailog dialog;

    public abstract void initListener();

    public void showWaitDialog() {
        loadBuilder = new LoadingDailog.Builder(getContext())
                .setShowMessage(false)
                .setCancelable(true)
                .setCancelOutside(true);
        dialog = loadBuilder.create();
        dialog.show();
    }

    public void hideWaitDialog() {
        if (dialog != null) {
            dialog.cancel();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
