package cn.com.xxdoctor.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.UserInfoBean;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;
import cn.com.xxdoctor.utils.LogUtil;
import cn.com.xxdoctor.utils.ToastUtils;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class SplashActivity extends DoctorBaseActivity {
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                skipLoginOrMain();

            }
        }, 1 * 1000);
    }

    private void skipLoginOrMain() {
//        DoctorBaseAppliction.spUtil.putString("account", account);
//        DoctorBaseAppliction.spUtil.putString("pwd", pwd);

        String account = DoctorBaseAppliction.spUtil.getString("account", "");
        String pwd = DoctorBaseAppliction.spUtil.getString("pwd", "");

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(pwd)) {
            requestLogin(account, pwd);
        } else {
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        }
    }

    private void requestLogin(final String account, final String pwd) {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("username", account);
        map.put("password", pwd);
        DoctorNetService.requestLogin(Constants.USER_LOGIN, map, new NetWorkRequestInterface() {
            @Override
            public void onError(Throwable throwable) {
                hideWaitDialog();
                DoctorBaseAppliction.spUtil.clear();
                ToastUtils.showMessage(mContext, "登录信息已过期，请重新登录");
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
            }

            @Override
            public void onNext(Object info) {

                UserInfoBean userInfoBean = (UserInfoBean) info;
                saveUserInfo(userInfoBean);
                DoctorBaseAppliction.spUtil.putString("account", account);
                DoctorBaseAppliction.spUtil.putString("pwd", pwd);
                hideWaitDialog();
                JMessageClient.login(account, "123456", new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        LogUtil.e("i " + i + "----" + s);
                        if (i == 0) {
                            ToastUtils.showMessage(mContext, "欢迎回来");
                            startActivity(new Intent(mContext, MainActivity.class));
                            finish();
                        } else {
                            ToastUtils.showMessage(mContext, "登录信息已过期，请重新登录");
                            startActivity(new Intent(mContext, LoginActivity.class));
                        }
                    }
                });

            }
        });
    }

    /**
     * 保存用户信息
     *
     * @param userInfoBean
     */
    private void saveUserInfo(UserInfoBean userInfoBean) {
        DoctorBaseAppliction.spUtil.putString(Constants.USER_ID, userInfoBean.user.UserID);
        DoctorBaseAppliction.spUtil.putString(Constants.HOSPITAL_ID, userInfoBean.user.HospitalID);
        DoctorBaseAppliction.spUtil.putString(Constants.LOGIN_NAME, userInfoBean.user.LoginName);
        DoctorBaseAppliction.spUtil.putString(Constants.Name, userInfoBean.user.Name);
        DoctorBaseAppliction.spUtil.putString(Constants.SEX, userInfoBean.user.Sex);
        DoctorBaseAppliction.spUtil.putString(Constants.NATIONAL, userInfoBean.user.National);
        DoctorBaseAppliction.spUtil.putString(Constants.BRITHDAY, userInfoBean.user.Brithday);
        DoctorBaseAppliction.spUtil.putString(Constants.MOBILE, userInfoBean.user.Mobile);
        DoctorBaseAppliction.spUtil.putString(Constants.ID_CARD, userInfoBean.user.IDCard);
        DoctorBaseAppliction.spUtil.putString(Constants.PHOTO_ID, userInfoBean.user.PhotoID);
        DoctorBaseAppliction.spUtil.putString(Constants.PHOTO_URL, userInfoBean.user.PhotoUrl);
        DoctorBaseAppliction.spUtil.putString(Constants.ADDRESS, userInfoBean.user.Address);
        DoctorBaseAppliction.spUtil.putString(Constants.FILEURL, userInfoBean.user.FileUrl);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
