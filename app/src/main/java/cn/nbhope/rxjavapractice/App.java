package cn.nbhope.rxjavapractice;

import android.app.Application;

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
