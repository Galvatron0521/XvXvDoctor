package cn.com.xxdoctor.utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import cn.com.xxdoctor.base.BaseReturnBean;
import cn.com.xxdoctor.base.DoctorBaseAppliction;
import cn.com.xxdoctor.bean.PdfReturnBean;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Wubj 创建于 2017/3/13 0010.
 */
public class OkHttpUtils {

    private static final String TAG = OkHttpUtils.class.getSimpleName();

    /**
     * 针对RXJava专用同步网络请求 post
     *
     * @param url
     * @param params
     */
    public static BaseReturnBean initHttp(String url, Map<String, Object> params) {
        final String sendJson = JSONUtil.parseMapToJson(params);
        LogUtil.i(TAG, "params>>>>" + params);
        LogUtil.i(TAG, "url>>>>" + url);
        if (sendJson == null) {
            return null;
        }
        MediaType JSON = MediaType.parse("application/octet-stream; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, sendJson);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = DoctorBaseAppliction.sOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                LogUtil.i(TAG, "url :" + url + "\nparams :" + sendJson + "\n返回结果 :" + result);
                BaseReturnBean bean = optBaseReturnBean(result);
                LogUtil.d("returnBean.code>>" + bean.code);
                LogUtil.d("returnBean.desc>>" + bean.desc);
                return bean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d(TAG, "wubaojie>>>initHttp: 解密失败");
        }
        return null;
    }

    /**
     * 针对RXJava专用同步网络请求 get
     *
     * @param url
     * @param params
     */
    public static BaseReturnBean initGetHttp(String url, Map<String, Object> params) {
        final String sendJson = JSONUtil.parseMapToJson(params);
        url = url + sendJson;
        if (sendJson == null) {
            return null;
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = DoctorBaseAppliction.sOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                LogUtil.i(TAG, "url :" + url + "\nparams :" + sendJson + "\n返回结果 :" + result);
                BaseReturnBean bean = optBaseReturnBean(result);
                LogUtil.d("returnBean.code>>" + bean.code);
                LogUtil.d("returnBean.desc>>" + bean.desc);
                return bean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 针对RXJava专用同步网络请求 get
     *
     * @param url
     * @param params
     */
    public static PdfReturnBean initPdfGetHttp(String url, Map<String, Object> params) {
        final String sendJson = JSONUtil.parseMapToJson(params);
        url = url + sendJson;
        if (sendJson == null) {
            return null;
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = DoctorBaseAppliction.sOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                LogUtil.i(TAG, "url :" + url + "\nparams :" + sendJson + "\n返回结果 :" + result);
                PdfReturnBean bean = JSONUtil.parseJsonToBean(result,PdfReturnBean.class);
                return bean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 针对RXJava专用同步网络请求 get
     *
     * @param url
     */
    public static BaseReturnBean initGetHttpOnlyByUrl(String url) {
        MediaType JSON = MediaType.parse("application/octet-stream; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, "");
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = DoctorBaseAppliction.sOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                BaseReturnBean bean = optBaseReturnBean(result);
                LogUtil.d("returnBean.code>>" + bean.code);
                LogUtil.d("returnBean.desc>>" + bean.desc);
                return bean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param key_value 参数键值对, key是参数名,value是参数内容, 原先string就直接放进去, 原先是结构体就转成string...
     */
    public static BaseReturnBean initPostRequest(String url, Map<String, Object> key_value) {
        if (key_value == null || key_value.isEmpty()) {
            throw new IllegalArgumentException("param is null");
        }
        RequestBody requestBodyPost = null;
        if (key_value.size() == 3) {
            requestBodyPost = new FormBody.Builder()
                    .add("data", (String) key_value.get("data"))
                    .add("act", (String) key_value.get("act"))
                    .add("filter", (String) key_value.get("filter"))
                    .build();
        } else {
            requestBodyPost = new FormBody.Builder()
                    .add("data", (String) key_value.get("data"))
                    .add("act", (String) key_value.get("act"))
                    .build();
        }
        Request request = new Request.Builder()
                .url(url)
                .post(requestBodyPost)
                .build();
        try {
            Response response = DoctorBaseAppliction.sOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                LogUtil.i(TAG, "url :" + url + "\nparams :" + requestBodyPost + "\n返回结果 :" +
                        result);
                BaseReturnBean bean = optBaseReturnBean(result);
                LogUtil.d("returnBean.code>>" + bean.code);
                LogUtil.d("returnBean.desc>>" + bean.desc);
                return bean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BaseReturnBean optBaseReturnBean(String result) {
        final BaseReturnBean returnBean = new BaseReturnBean();
        try {
            JSONObject proDataJSON = new JSONObject(result);
            returnBean.code = proDataJSON.optString("status");
            returnBean.desc = "";
            String data = "";
            if (proDataJSON.has("data")) {
                data = proDataJSON.optString("data");
            } else if (proDataJSON.has("msgList")) {
                data = proDataJSON.optString("msgList");
            } else if (proDataJSON.has("msg")) {
                data = proDataJSON.optString("msg");
            }
            returnBean.data = data;
            LogUtil.d(TAG, "returnBean.data>>" + data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnBean;
    }


    public static String initUpLoad(String url, RequestBody requestBody) {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        LogUtil.e("wubj---->>>:网路请求地址:" + url);
        try {
            Response response = DoctorBaseAppliction.sOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
