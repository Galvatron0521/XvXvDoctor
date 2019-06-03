package cn.com.xxdoctor.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.tu.loadingdialog.LoadingDailog;
import com.bigkoo.pickerview.TimePickerView;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.view.listener.OnInputClickListener;

import org.greenrobot.eventbus.EventBus;

import cn.com.xxdoctor.R;

/**
 * Created by chong.han on 2018/8/16.
 */

public abstract class DoctorBaseActivity extends AppCompatActivity {
    protected Context mContext = this;
    protected LoadingDailog.Builder loadBuilder;
    protected LoadingDailog dialog;
    protected TimePickerView pvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    public abstract void initData();

    public abstract void initListener();

    public void showWaitDialog() {
//        if (dialog.isShowing()) return;
        loadBuilder = new LoadingDailog.Builder(this)
                .setCancelable(true)
                .setShowMessage(false)
                .setCancelOutside(false);
        dialog = loadBuilder.create();
        dialog.show();
    }

    public void hideWaitDialog() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    @SuppressLint("ResourceAsColor")
    public void showAffirmDialog(String title, String message, View.OnClickListener
            onClickListener) {
        new CircleDialog.Builder(this)
                .setTitle(title)
                .setText(message)//内容
                .setTextColor(R.color.text_color)//内容字体色值
                .setPositive("确定", onClickListener)
                .setNegative("取消", null)
                .setWidth(0.7f)
                .setMaxHeight(0.4f)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
    }

    @SuppressLint("ResourceAsColor")
    public void showInputDialog(String hint, OnInputClickListener onInputClickListener) {
        new CircleDialog.Builder(this)
                //添加标题，参考普通对话框
                .setInputHint(hint)//提示
                .setTitle("请输入")
                .setWidth(0.7f)
                .setMaxHeight(0.4f)
                .setInputCounterColor(R.color.text_color)//最大字符数文字的颜色值
                .autoInputShowKeyboard()//自动弹出键盘
                .setPositiveInput("确定", onInputClickListener)
                .show();
    }

}
