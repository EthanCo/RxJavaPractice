package cn.nbhope.rxjavapractice.create_oper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Description 创建操作符
 * Created by EthanCo on 2016/6/27.
 */
public class CreateActivity extends AppCompatActivity {
    private static final String TAG = "Z-CreateActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.i(TAG, "onCreate JUST: ");
        Observable.just("hello")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(value -> {
                            Log.i(TAG, "onCreate : " + value);
                        },
                        throwable -> {
                        },
                        () -> {
                        });


        Log.i(TAG, "onCreate FORM: ");
        List<String> list = new ArrayList<>();
        list.add("item1");
        list.add("item2");
        list.add("item3");
        Observable.from(list)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(value -> {
                            Log.i(TAG, "onCreate : " + value);
                        },
                        throwable -> {
                        },
                        () -> {
                        });
    }
}
