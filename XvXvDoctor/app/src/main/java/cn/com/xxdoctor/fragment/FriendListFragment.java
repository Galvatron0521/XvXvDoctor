package cn.com.xxdoctor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.xxdoctor.R;
import cn.com.xxdoctor.activity.patientCenter.AddPatientInfoActivity;
import cn.com.xxdoctor.activity.patientCenter.PatientInfoActivity;
import cn.com.xxdoctor.adapter.DoctorListAdapter;
import cn.com.xxdoctor.adapter.PatientListAdapter;
import cn.com.xxdoctor.base.Constants;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.base.DoctorBaseFragment;
import cn.com.xxdoctor.bean.DoctorListBean;
import cn.com.xxdoctor.bean.PatientListBean;
import cn.com.xxdoctor.bean.ResultBean;
import cn.com.xxdoctor.chat.activity.ChatActivity;
import cn.com.xxdoctor.chat.application.JGApplication;
import cn.com.xxdoctor.network.DoctorNetService;
import cn.com.xxdoctor.network.NetWorkRequestInterface;
import cn.com.xxdoctor.utils.ToastUtils;

/**
 * Created by chong.han on 2018/8/16.
 */

public class FriendListFragment extends DoctorBaseFragment {

    private boolean isTimePx; //排序方式标识 默认时间
    private int pageCount = 10;
    private int pageNo = 0;
    private String name = "";

    private int DpageCount = 10;
    private int DpageNo = 0;
    private String Dname = "";

    private String orderBy = "desc";//asc升序 desc降序
    private List<PatientListBean.ListBean> mDatas;
    private PatientListAdapter patientListAdapter;
    private List<DoctorListBean.ListBean> mDoctorDatas;
    private DoctorListAdapter doctorListAdapter;
    private String user_id;
    private ImageView title_back;
    private TextView title_name;
    private TextView title_right_tv;
    private EditText friend_search_et;
    private TextView friend_search_bt;
    private LinearLayout fragment_search_head;
    private ListView doctor_list_listview;
    private SpringView doctor_list_springview;
    private ListView patient_listview;
    private SpringView patient_springview;
    private RadioButton friend_rb_patient;
    private RadioButton friend_rb_doctor;
    private RadioGroup friend_rg;
    private boolean isCheckPatient = true; // 是否是选择的患者

    public static FriendListFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        FriendListFragment fragment = new FriendListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.friend_fragment, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {

        title_back = (ImageView) view.findViewById(R.id.title_back);
        title_name = (TextView) view.findViewById(R.id.title_name);
        title_right_tv = (TextView) view.findViewById(R.id.title_right_tv);
        friend_search_et = (EditText) view.findViewById(R.id.friend_search_et);
        friend_search_bt = (TextView) view.findViewById(R.id.friend_search_bt);
        fragment_search_head = (LinearLayout) view.findViewById(R.id.fragment_search_head);
        doctor_list_listview = (ListView) view.findViewById(R.id.doctor_list_listview);
        doctor_list_springview = (SpringView) view.findViewById(R.id.doctor_list_springview);
        patient_listview = (ListView) view.findViewById(R.id.patient_listview);
        patient_springview = (SpringView) view.findViewById(R.id.patient_springview);

        doctor_list_springview.setHeader(new DefaultHeader(getContext()));
        doctor_list_springview.setFooter(new DefaultFooter(getContext()));

        patient_springview.setHeader(new DefaultHeader(getContext()));
        patient_springview.setFooter(new DefaultFooter(getContext()));

        friend_rb_patient = (RadioButton) view.findViewById(R.id.friend_rb_patient);
        friend_rb_doctor = (RadioButton) view.findViewById(R.id.friend_rb_doctor);
        friend_rg = (RadioGroup) view.findViewById(R.id.friend_rg);
    }

    @Override
    public void initData() {
        isTimePx = true;
        mDatas = new ArrayList<>();
        mDoctorDatas = new ArrayList<>();
        user_id = DoctorBaseAppliction.spUtil.getString(Constants.USER_ID, "");
        patientListAdapter = new PatientListAdapter(getContext(), mDatas,true);
        patient_listview.setAdapter(patientListAdapter);

        doctorListAdapter = new DoctorListAdapter(getContext(), mDoctorDatas);
        doctor_list_listview.setAdapter(doctorListAdapter);
        requestPatientList();
        requestDoctorList();
    }

    /**
     * 请求患者列表
     */
    private void requestPatientList() {
        Map<String, Object> map = new HashMap<>();
        /**
         * "pageNo":"0",
         "pageCount":"8",
         "userID":1,
         "name":"",
         "diseasesID":"",
         "orderBy":"desc"
         */
        map.put("pageNo", pageNo);
        map.put("pageCount", pageCount + "");
        map.put("userID", user_id);
        map.put("name", name);
        map.put("diseasesID", "");
        map.put("orderBy", orderBy);
        DoctorNetService.requestPatientList(Constants.PATIENTS_LIST, map, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                    }

                    @Override
                    public void onNext(Object info) {
                        PatientListBean patientListBean = (PatientListBean) info;
                        mDatas.addAll(patientListBean.list);
                        patientListAdapter.notifyDataSetChanged();
                        patient_springview.onFinishFreshAndLoad();
                    }
                });
    }

    /**
     * 请求医生列表
     */
    private void requestDoctorList() {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        /**
         * "pageNo":"0",
         "pageCount":"8",
         "userID":1,
         "name":"",
         "diseasesID":"",
         "orderBy":"desc"
         */
        map.put("pageNo", DpageNo);
        map.put("pageCount", DpageCount + "");
        map.put("userID", user_id);
        map.put("name", Dname);
        DoctorNetService.requestDoctorList(Constants.SELECT_JMESSAGE_FRIENDS_LIST, map, new
                NetWorkRequestInterface() {

                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        DoctorListBean patientListBean = (DoctorListBean) info;
                        mDoctorDatas.addAll(patientListBean.list);
                        doctorListAdapter.notifyDataSetChanged();
                        doctor_list_springview.onFinishFreshAndLoad();
                    }
                });
    }

    @Override
    public void initListener() {
        friend_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                friend_search_et.setText("");
                if (i == R.id.friend_rb_patient) {
                    isCheckPatient = true;
                    friend_rb_patient.setChecked(true);
                    friend_rb_doctor.setChecked(false);
                    doctor_list_springview.setVisibility(View.GONE);
                    patient_springview.setVisibility(View.VISIBLE);
                } else {
                    isCheckPatient = false;
                    friend_rb_patient.setChecked(false);
                    friend_rb_doctor.setChecked(true);
                    doctor_list_springview.setVisibility(View.VISIBLE);
                    patient_springview.setVisibility(View.GONE);
                }
            }
        });
        // 搜索按钮
        friend_search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCheckPatient) {
                    name = friend_search_et.getText().toString().trim();
                    pageNo = 0;
                    mDatas.clear();
                    requestPatientList();
                } else {
                    Dname = friend_search_et.getText().toString().trim();
                    DpageNo = 0;
                    mDoctorDatas.clear();
                    requestDoctorList();
                }

            }
        });
        //患者 list点击
        patient_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PatientListBean.ListBean listBean = mDatas.get(i);
                requestAddFriend(listBean.mobile, listBean.name);
            }
        });

        // 医生 list点击
        doctor_list_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DoctorListBean.ListBean listBean = mDoctorDatas.get(i);
                requestAddFriend(listBean.Mobile, listBean.Name);
            }
        });
        // 上拉下拉o
        patient_springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                //刷新
                pageNo = 0;
                mDatas.clear();
                requestPatientList();
            }

            @Override
            public void onLoadmore() {
                //加载更多
                pageNo = pageNo + pageCount;
                requestPatientList();
            }
        });

        // 上拉下拉o
        doctor_list_springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                //刷新
                DpageNo = 0;
                mDoctorDatas.clear();
                requestDoctorList();
            }

            @Override
            public void onLoadmore() {
                //加载更多
                DpageNo = DpageNo + DpageCount;
                requestDoctorList();
            }
        });
    }

    /**
     * 请求是否是好友  不是后台自动添加
     */
    private void requestAddFriend(final String mobile, final String username) {
        showWaitDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("username", DoctorBaseAppliction.spUtil.getString(Constants.MOBILE, ""));
        DoctorNetService.requestAddOrChange(Constants.ADD_FRIENDS, map, new
                NetWorkRequestInterface() {


                    @Override
                    public void onError(Throwable throwable) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onNext(Object info) {
                        hideWaitDialog();
                        //isFriend:0添加好友成功,1 已经是好友,2 添加错误
                        ResultBean bean = (ResultBean) info;
                        if (bean.isFriend.equals("0") || bean.isFriend.equals("1")) {
                            Intent intent = new Intent(getContext(), ChatActivity.class);
                            String title = username;
                            intent.putExtra(JGApplication.CONV_TITLE, title);
                            intent.putExtra(JGApplication.TARGET_ID, mobile);
                            intent.putExtra(JGApplication.TARGET_APP_KEY, DoctorBaseAppliction
                                    .JMAppKey);
                            startActivity(intent);
                        } else {
                            ToastUtils.showMessage(getContext(), "添加错误");
                        }
                    }
                });
    }

    @Subscribe
    public void onEventMainThread(Map<String, Object> map) {
        String type = (String) map.get(Constants.EVENTBUS_TYEPE);
        //新增或者删除患者刷新界面
        if (type.equals(AddPatientInfoActivity.ADD_PATIENT_SUCCESS) || type.equals
                (PatientInfoActivity.DELETE_PATIENT_SUCCESS)) {
            pageNo = 0;
            name = "";
            orderBy = "desc";
            mDatas.clear();
            requestPatientList();
        }
    }
}
