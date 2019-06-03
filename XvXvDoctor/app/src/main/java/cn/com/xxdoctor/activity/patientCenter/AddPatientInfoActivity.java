package cn.com.xxdoctor.activity.patientCenter;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.lljjcoder.citypickerview.widget.CityPicker;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.base.BaseReturnBean;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseActivity;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.CardBean;
import cn.com.xxdoctor.bean.PatientInfoBean;
import cn.com.xxdoctor.bean.ResultBean;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;
import cn.com.xxdoctor.utils.DateUtils;
import cn.com.xxdoctor.utils.HcUtils;
import cn.com.xxdoctor.utils.IDCardInfoExtractor;
import cn.com.xxdoctor.utils.JSONUtil;
import cn.com.xxdoctor.utils.ToastUtils;

public class AddPatientInfoActivity extends DoctorBaseActivity {

    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private EditText add_patient_info_name;
    private EditText add_patient_info_phone;
    private EditText add_patient_info_idno;
    private EditText add_patient_info_age;
    private TextView add_patient_info_sex;
    private EditText add_patient_info_no;
    private TextView add_patient_info_address;
    private TextView add_patient_info_shengri;
    private boolean isAdd;
    private PatientInfoBean infoBean;
    private String user_id;
    private OptionsPickerView pvOptions;
    public static String ADD_PATIENT_SUCCESS = "add_patient_success";
    private String patientID;
    private EditText add_patient_info_beiyong_phone;
    private CityPicker mCP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_info);
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        patientID = getIntent().getStringExtra("patientID");
        if (!isAdd) {
            infoBean = (PatientInfoBean) getIntent().getSerializableExtra("info");
        }
        initTimePicker();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        add_patient_info_name = (EditText) findViewById(R.id.add_patient_info_name);
        add_patient_info_phone = (EditText) findViewById(R.id.add_patient_info_phone);
        add_patient_info_idno = (EditText) findViewById(R.id.add_patient_info_idno);
        add_patient_info_age = (EditText) findViewById(R.id.add_patient_info_age);
        add_patient_info_sex = (TextView) findViewById(R.id.add_patient_info_sex);
        add_patient_info_no = (EditText) findViewById(R.id.add_patient_info_no);
        add_patient_info_address = (TextView) findViewById(R.id.add_patient_info_address);
        add_patient_info_beiyong_phone = (EditText) findViewById(R.id
                .add_patient_info_beiyong_phone);
        add_patient_info_shengri = (TextView) findViewById(R.id.add_patient_info_shengri);

        if (isAdd) {
            title_name.setText("手动添加");

        } else {
            title_name.setText("患者信息");
//            add_patient_info_name.setEnabled(false);
            add_patient_info_phone.setEnabled(false);
            add_patient_info_idno.setEnabled(false);
//            add_patient_info_beiyong_phone.setEnabled(false);
//            add_patient_info_age.setEnabled(false);
//            add_patient_info_sex.setEnabled(false);
//            add_patient_info_no.setEnabled(false);
//            add_patient_info_address.setEnabled(false);
//            add_patient_info_shengri.setEnabled(false);
        }
        title_right_tv.setText("保存");
        title_right_tv.setVisibility(View.VISIBLE);
        title_back.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {

        mCardItem = new ArrayList<>();
        user_id = DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, "");
        if (!isAdd) {
            add_patient_info_name.setHint("");
            add_patient_info_phone.setHint("");
            add_patient_info_beiyong_phone.setHint("");
            add_patient_info_idno.setHint("");
            add_patient_info_age.setHint("");
            add_patient_info_sex.setHint("");
            add_patient_info_no.setHint("");
            add_patient_info_address.setHint("");
            add_patient_info_shengri.setHint("");

            add_patient_info_name.setText(infoBean.patientDetails.name);
            add_patient_info_phone.setText(infoBean.patientDetails.mobile);
            add_patient_info_idno.setText(infoBean.patientDetails.idCard);
            add_patient_info_age.setText(infoBean.patientDetails.age);
            add_patient_info_sex.setText(infoBean.patientDetails.sex);
            add_patient_info_no.setText(infoBean.patientDetails.num);
            add_patient_info_address.setText(infoBean.patientDetails.address);
            add_patient_info_shengri.setText(infoBean.patientDetails.brithday);
            add_patient_info_beiyong_phone.setText(infoBean.patientDetails.mobile2);
        }
    }

    @Override
    public void initListener() {
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title_right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        add_patient_info_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HcUtils.hideKeyboard(AddPatientInfoActivity.this);
                initOptionPicker(add_patient_info_sex);
                showSelectDialog();
            }
        });
        add_patient_info_shengri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HcUtils.hideKeyboard(AddPatientInfoActivity.this);
                pvTime.show(add_patient_info_shengri);

            }
        });
        add_patient_info_idno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() == 18) {
                    IDCardInfoExtractor idCardInfo = new IDCardInfoExtractor
                            (charSequence.toString());
                    //从身份证号码中提取出生日期
                    String birthDay = idCardInfo.getYear() + "-" + idCardInfo.getMonth() + "-" +
                            idCardInfo.getDay();
                    //从身份证号中提取性别
                    String gender = idCardInfo.getGender();
                    add_patient_info_shengri.setText(birthDay);
                    add_patient_info_sex.setText(gender);
                    add_patient_info_age.setText(idCardInfo.getAge() + "");
                    add_patient_info_address.setText(idCardInfo.getProvince());
                } else {
                    add_patient_info_shengri.setText("");
                    add_patient_info_sex.setText("");
                    add_patient_info_age.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        add_patient_info_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mYunCityPicher();
                mCP.show();
            }
        });
    }

    private void submit() {
        // validate
        String name = add_patient_info_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入患者姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        String idno = add_patient_info_idno.getText().toString().trim();
        if (TextUtils.isEmpty(idno)) {
            Toast.makeText(this, "请输入身份证号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (idno.length() != 18) {
            Toast.makeText(this, "请输入正确的18位身份证号", Toast.LENGTH_SHORT).show();
            return;
        }
        String age = add_patient_info_age.getText().toString().trim();
        if (TextUtils.isEmpty(age)) {
            Toast.makeText(this, "请输入患者年龄", Toast.LENGTH_SHORT).show();
            return;
        }

        String sex = add_patient_info_sex.getText().toString().trim();
        if (TextUtils.isEmpty(sex)) {
            Toast.makeText(this, "请选择患者性别", Toast.LENGTH_SHORT).show();
            return;
        }

//        String no = add_patient_info_no.getText().toString().trim();
//        if (TextUtils.isEmpty(no)) {
//            Toast.makeText(this, "请输入患者编号", Toast.LENGTH_SHORT).show();
//            return;
//        }

        String address = add_patient_info_address.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "请选择患者居住地", Toast.LENGTH_SHORT).show();
            return;
        }

        String shengri = add_patient_info_shengri.getText().toString().trim();
        if (TextUtils.isEmpty(shengri)) {
            Toast.makeText(this, "请选择患者出生年月", Toast.LENGTH_SHORT).show();
            return;
        }
        String phone = add_patient_info_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入联系方式", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.length() != 11) {
            Toast.makeText(this, "请输入正确的11位手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        String beiyong_phone = add_patient_info_beiyong_phone.getText().toString().trim();
        /**
         * act=insertPatient&data={"UserID":1,"mobileType":"2",
         "optionTag":"insert",
         "moduleCode":"SP0201",
         "name":"005",
         "sex":"男","age":66,
         "mobile":"18880868898",
         "idCard":"",
         "num":"1231233",
         "address":"",
         "brithday":"2018-08-08"
         }
         */
        Map<String, Object> map = new HashMap<>();
        map.put("userID", user_id);
        map.put("mobileType", Constants.MOBILE_TYPE);
        map.put("optionTag", isAdd ? "insert" : "update");
        map.put("name", name);
        map.put("sex", sex);
        map.put("age", age);
        map.put("mobile", phone);
        map.put("idCard", idno);
        map.put("num", "");
        map.put("address", address);
        map.put("brithday", shengri);
        map.put("mobile2", beiyong_phone);
        if (isAdd) {
            map.put("hospitalID", DoctorBaseAppliction.spUtil.getString(Constants.HOSPITAL_ID, ""));
        }
        if (!isAdd) {
            map.put("id", patientID);
        }
        requestAddPatientInfo(map);
    }

    private void requestAddPatientInfo(Map<String, Object> map) {
        showWaitDialog();
        DoctorNetService.requestAddOrChangeCallErrorInfo(isAdd ? Constants.INSERT_PATIENT :
                Constants
                        .UPDATE_PATIENT, map, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                        ToastUtils.showMessage(mContext, "未知错误请重试");
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        BaseReturnBean bean = (BaseReturnBean) info;
                        if (DoctorNetService.isSuccess(bean.code)) {
                            ResultBean infoBean = JSONUtil.parseJsonToBean(bean.data, ResultBean
                                    .class);
                            ToastUtils.showMessage(mContext, infoBean.data);
                            Map<String, Object> map = new HashMap<>();
                            map.put(Constants.EVENTBUS_TYEPE, ADD_PATIENT_SUCCESS);
                            EventBus.getDefault().post(map);
                            finish();
                        } else {
                            ToastUtils.showMessage(mContext, bean.data);
                        }

                    }
                });
    }

    /**
     * 初始化时间弹框
     */
    private void initTimePicker() {
        String yearTime = HcUtils.getYearTime(new Date(System.currentTimeMillis()));
        String monthTime = HcUtils.getMonthTime(new Date(System.currentTimeMillis()));
        String dayTime = HcUtils.getDayTime(new Date(System.currentTimeMillis()));
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1980, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(Integer.parseInt(yearTime), Integer.parseInt(monthTime) - 1, Integer
                .parseInt(dayTime));
        pvTime = new TimePickerView.Builder(this, new TimePickerView
                .OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date, View v) {
                String found_date = DateUtils.dateToString(date, DateUtils.FORMAT_5);
                ((TextView) v).setText(found_date);
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setCancelColor(getResources().getColor(R.color.main_color))
                .setSubmitColor(getResources().getColor(R.color.main_color))
                .setRangDate(startDate, endDate)
                .setBackgroundId(0x66000000) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
    }

    /**
     * 展示下拉选择框
     */
    private void showSelectDialog() {
        mCardItem.clear();
        CardBean cardBean1 = new CardBean("1", "男");
        CardBean cardBean2 = new CardBean("0", "女");
        mCardItem.add(cardBean1);
        mCardItem.add(cardBean2);
        pvOptions.setPicker(mCardItem);
        pvOptions.show();
    }

    /**
     * 初始化弹窗
     *
     * @param view 点击的哪个view
     */
    private List<CardBean> mCardItem;

    private void initOptionPicker(final TextView view) {//条件选择器初始化
        pvOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView
                .OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String tx = mCardItem.get(options1).cardItemName;
                view.setText(tx);
            }
        })
                .setContentTextSize(20)//设置滚轮文字大小
                .setSelectOptions(0, 1)//默认选中项
                .setBgColor(Color.WHITE)
                .setCancelColor(getResources().getColor(R.color.main_color))
                .setSubmitColor(getResources().getColor(R.color.main_color))
                .setLineSpacingMultiplier(2.0f)//设置两横线之间的间隔倍数
                .setBackgroundId(0x66000000) //设置外部遮罩颜色
                .build();
    }

    public void mYunCityPicher() {
        mCP = new CityPicker.Builder(AddPatientInfoActivity.this)
                .textSize(20)
                //地址选择
                .title("地址选择")
                .backgroundPop(0xa0000000)
                //文字的颜色
                .titleBackgroundColor("#ffffff")
                .titleTextColor("#22A63B")
                .backgroundPop(0xa0000000)
                .confirTextColor("#22A63B")
                .cancelTextColor("#22A63B")
                .province("xx省")
                .city("xx市")
                .district("xx区")
                //滑轮文字的颜色
                .textColor(Color.parseColor("#000000"))
                //省滑轮是否循环显示
                .provinceCyclic(true)
                //市滑轮是否循环显示
                .cityCyclic(false)
                //地区（县）滑轮是否循环显示
                .districtCyclic(false)
                //滑轮显示的item个数
                .visibleItemsCount(7)
                //滑轮item间距
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();

        //监听
        mCP.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省
                String province = citySelected[0];
                //市
                String city = citySelected[1];
                //区。县。（两级联动，必须返回空）
                String district = citySelected[2];
                //邮证编码
                add_patient_info_address.setText(province + city + district);
//                mDiQuSanJiLianDong.setText(province + city + district);
            }

            @Override
            public void onCancel() {


            }
        });
    }
}
