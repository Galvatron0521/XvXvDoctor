package cn.com.xxdoctor.network;

/**
 * Created by chong.han on 2018/5/2.
 */

public interface NetWorkRequestInterface {
    void onError(Throwable throwable);

    void onNext(Object info);
}
