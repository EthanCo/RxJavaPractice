package cn.nbhope.rxjavapractice.base;

import android.app.Application;

import cn.nbhope.rxjavapractice.utils.T;

/**
 * @Description Application
 * Created by EthanCo on 2016/6/30.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        T.init(this);
    }
}
