package cn.com.xxdoctor.network;

import android.text.TextUtils;

import java.util.Map;

import cn.com.xxdoctor.base.BaseReturnBean;
import cn.com.xxdoctor.bean.BaoGaoInfoBean;
import cn.com.xxdoctor.bean.BianZhengBean;
import cn.com.xxdoctor.bean.BianZhengDetailBean;
import cn.com.xxdoctor.bean.BingZhengListBean;
import cn.com.xxdoctor.bean.ContentBean;
import cn.com.xxdoctor.bean.DoctorListBean;
import cn.com.xxdoctor.bean.DoctorTagListBean;
import cn.com.xxdoctor.bean.FangAnInfoBean;
import cn.com.xxdoctor.bean.InitSubjectBean;
import cn.com.xxdoctor.bean.LiangBiaoInfoBean;
import cn.com.xxdoctor.bean.MyLiangBiaoInfoListBean;
import cn.com.xxdoctor.bean.MyPlanCRFListBean;
import cn.com.xxdoctor.bean.MyPlanListBean;
import cn.com.xxdoctor.bean.MySubjectDetailBean;
import cn.com.xxdoctor.bean.MySubjectListBean;
import cn.com.xxdoctor.bean.MySuggestionListBean;
import cn.com.xxdoctor.bean.NextSubjectBean;
import cn.com.xxdoctor.bean.PatientInfoBean;
import cn.com.xxdoctor.bean.PatientListBean;
import cn.com.xxdoctor.bean.PatientSuiFangListBean;
import cn.com.xxdoctor.bean.PdfReturnBean;
import cn.com.xxdoctor.bean.PicListBean;
import cn.com.xxdoctor.bean.ResultBean;
import cn.com.xxdoctor.bean.SuiFangCrfBean;
import cn.com.xxdoctor.bean.SuiFangFangAnBean;
import cn.com.xxdoctor.bean.SuiFangTypeBean;
import cn.com.xxdoctor.bean.UserInfoBean;
import cn.com.xxdoctor.utils.JSONUtil;
import cn.com.xxdoctor.utils.LogUtil;
import cn.com.xxdoctor.utils.OkHttpUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chong.han on 2018/4/27.
 */

public class DoctorNetService {
    /**
     * 用户登录
     *
     * @param param
     */
    public static void requestLogin(final String url, final Map<String, Object> param, final
    NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<UserInfoBean>() {
            @Override
            public void call(Subscriber<? super UserInfoBean> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                UserInfoBean userInfoBean = JSONUtil.parseJsonToBean(bean.data, UserInfoBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(userInfoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<UserInfoBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(UserInfoBean info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 请求患者列表
     *
     * @param param
     */
    public static void requestPatientList(final String url, final Map<String, Object> param, final
    NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                PatientListBean patientListBean = JSONUtil.parseJsonToBean(bean.data,
                        PatientListBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(patientListBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 请求患者详细信息
     *
     * @param param
     */
    public static void requestPatientDetail(final String url, final Map<String, Object> param, final
    NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                PatientInfoBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        PatientInfoBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 修改或者添加数据  只返回状态的
     *
     * @param param
     */
    public static void requestAddOrChange(final String url, final Map<String, Object> param, final
    NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                ResultBean infoBean = JSONUtil.parseJsonToBean(bean.data, ResultBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 添加数据需要返回错误信息的
     *
     * @param param
     */
    public static void requestAddOrChangeCallErrorInfo(final String url, final Map<String,
            Object> param, final
                                                       NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                subscriber.onNext(bean);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 查询医生标签列表
     *
     * @param param
     */
    public static void requestDoctorTagList(final String url, final Map<String, Object> param, final
    NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                DoctorTagListBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        DoctorTagListBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 查询患者随访记录
     *
     * @param param
     */
    public static void requestPatientSuiFangList(final String url, final Map<String, Object>
            param, final
                                                 NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                PatientSuiFangListBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        PatientSuiFangListBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 查询患者随访记录
     *
     * @param param
     */
    public static void requestMyPlanList(final String url, final Map<String, Object>
            param, final
                                         NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                MyPlanListBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        MyPlanListBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 查询随访方式
     *
     * @param param
     */
    public static void requestFangAnType(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                SuiFangTypeBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        SuiFangTypeBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 查询随访方式
     *
     * @param param
     */
    public static void requestFangAnList(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                SuiFangFangAnBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        SuiFangFangAnBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 获取方案信息
     *
     * @param param
     */
    public static void requestFangAnInfo(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                FangAnInfoBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        FangAnInfoBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 获取辩证数据
     *
     * @param param
     */
    public static void requestFildMoudleList(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                BianZhengBean infoBean = JSONUtil.parseJsonToBean(bean.data, BianZhengBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 获取两边数据
     *
     * @param param
     */
    public static void requestLiangBiaoMoudleList(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                LiangBiaoInfoBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        LiangBiaoInfoBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 获取病症管理列表
     *
     * @param param
     */
    public static void requestBingZhengList(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                BingZhengListBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        BingZhengListBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 获取病症管理列表
     *
     * @param param
     */
    public static void requestBingZhengDetail(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                BianZhengDetailBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        BianZhengDetailBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 获取病症管理列表
     *
     * @param param
     */
    public static void requestSuiFCrfDetail(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                SuiFangCrfBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        SuiFangCrfBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 请求病理报告
     *
     * @param param
     */
    public static void requestFieldRecordListForm(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                BaoGaoInfoBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        BaoGaoInfoBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 请求医生列表
     *
     * @param param
     */
    public static void requestDoctorList(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                DoctorListBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        DoctorListBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 根据URL修改或者添加数据  只返回状态的
     */
    public static void requestAddOrChangeByUrl(final String url, final Map<String, Object>
            param, final
                                               NetWorkRequestInterface requestInterface) {
        LogUtil.e("URL -- >> " + url);
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initPostRequest(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                ResultBean infoBean = JSONUtil.parseJsonToBean(bean.data, ResultBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 请求图片列表
     */
    public static void requestPicList(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                PicListBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        PicListBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 请求我的量表列表
     */
    public static void requestMyLiangBiaoList(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                MyLiangBiaoInfoListBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        MyLiangBiaoInfoListBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 请求病理报告PDF
     */
    public static void requestGetPdf(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                PdfReturnBean bean = OkHttpUtils.initPdfGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                if (isSuccess(bean.status)) {
                    subscriber.onNext(bean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 请求 我的课题列表
     */
    public static void requestMySubjectList(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                MySubjectListBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        MySubjectListBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 查询数据字典
     */
    public static void requestContent(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                ContentBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        ContentBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 查询我的课题当中设置入选人群初始化条件
     */
    public static void requestRuXuanList(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                InitSubjectBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        InitSubjectBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 查询我的课题详情
     */
    public static void requestSubjectDetail(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                MySubjectDetailBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        MySubjectDetailBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 查询我的课题条件
     */
    public static void requestNextList(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                NextSubjectBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        NextSubjectBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 查询我的意见列表
     */
    public static void requestSuggestionList(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                MySuggestionListBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        MySuggestionListBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    /**
     * 查询 我的随访方案 关联  任务项目
     * selectFollowCRFListNew
     */
    public static void requestFollowCRFList(final String url, final Map<String, Object>
            param, final NetWorkRequestInterface requestInterface) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                BaseReturnBean bean = OkHttpUtils.initGetHttp(url, param);
                LogUtil.e("hcccccc---->>>:" + bean);
                MyPlanCRFListBean infoBean = JSONUtil.parseJsonToBean(bean.data,
                        MyPlanCRFListBean.class);
                if (isSuccess(bean.code)) {
                    subscriber.onNext(infoBean);
                } else {
                    subscriber.onError(new Exception());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                requestInterface.onError(throwable);
            }

            @Override
            public void onNext(Object info) {
                requestInterface.onNext(info);
            }
        });
    }

    public static boolean isSuccess(String result) {
        if (!TextUtils.isEmpty(result)) {
            return result.endsWith("0000") || "0".equals(result);
        } else {
            return false;
        }
    }
}