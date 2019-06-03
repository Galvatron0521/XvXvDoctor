package cn.com.xxdoctor.activity.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import cn.com.xxdoctor.utils.PermissionHelper;
import cn.com.xxdoctor.utils.PermissionInterface;
import cn.com.xxdoctor.utils.ToastUtils;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class LoginActivity extends DoctorBaseActivity implements View.OnClickListener,
        PermissionInterface {

    private EditText login_account;
    private EditText login_pwd;
    private Button login_bt_login;
    private PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_login);
        permissionHelper = new PermissionHelper(this, this);
        permissionHelper.requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, 1);
        permissionHelper.requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
        initView();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    private void initView() {
        login_account = (EditText) findViewById(R.id.login_account);
        login_pwd = (EditText) findViewById(R.id.login_pwd);
        login_bt_login = (Button) findViewById(R.id.login_bt_login);

        login_bt_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_bt_login:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        String account = login_account.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String pwd = login_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        requestLogin(account, pwd);
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
                ToastUtils.showMessage(mContext, "账号或密码错误");
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
                            ToastUtils.showMessage(mContext, "登录成功");
                            startActivity(new Intent(mContext, MainActivity.class));
                            finish();
                        } else {
                            ToastUtils.showMessage(mContext, "登录失败，请重试");
                        }
                    }
                });
//                startActivity(new Intent(mContext, MainActivity.class));
//                finish();
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
    public void requestPermissionsSuccess(int callBackCode) {

    }

    @Override
    public void requestPermissionsFail(int callBackCode) {

    }
}
