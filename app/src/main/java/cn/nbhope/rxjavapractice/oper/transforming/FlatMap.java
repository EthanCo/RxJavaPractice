package cn.nbhope.rxjavapractice.oper.transforming;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.nbhope.rxjavapractice.R;
import cn.nbhope.rxjavapractice.utils.L;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Description Just
 * Created by EthanCo on 2016/6/30.
 */
public class FlatMap extends Fragment {

    private ViewDataBinding binding;
    private TextView tvInfo;

    public static Fragment newInstance() {
        FlatMap fragment = new FlatMap();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_text, container, false);
        tvInfo = (TextView) binding.getRoot().findViewById(R.id.tvInfo);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
//        String[] arr = new String[]{"1", "2", "3", "4", "5"};
        List<String> arr = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            arr.add(String.valueOf(i));
        }
        Observable.just(arr)
                .flatMap(new Func1<List<String>, Observable<?>>() {
                    @Override
                    public Observable<?> call(List<String> strings) {
                        return Observable.from(strings);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(response -> {
                            L.i("flatMap:" + response.toString());
                        },
                        throwable -> {
                        },
                        () -> {
                        });


        /**
         * http://www.jianshu.com/p/6d16805537ef
         *
         * flatMap()操作符使用你提供的原本会被原始Observable发送的事件，来创建一个新的Observable。
         * 而且这个操作符，返回的是一个自身发送事件并合并结果的Observable。
         * 可以用于任何由原始Observable发送出的事件，发送合并后的结果。记住，
         * flatMap()可能交错的发送事件，最终结果的顺序可能并是不原始Observable发送时的顺序。
         * 为了防止交错的发生，可以使用与之类似的concatMap()操作符。
         */

//        Observable.just(arr)
//                .flatMapIterable(new Func1<String[], Iterable<?>>() {
//                    @Override
//                    public Iterable<?> call(String[] strings) {
//                        return null;
//                    }
//                }).observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(response -> {
//                            L.i(response.toString());
//                        },
//                        throwable -> {
//                        },
//                        () -> {
//                        });

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Observable.just(arr)
                .concatMap(new Func1<List<String>, Observable<?>>() {
                    @Override
                    public Observable<?> call(List<String> strings) {
                        return Observable.from(strings);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(response -> {
                            L.i("concatMap" + response.toString());
                        },
                        throwable -> {
                        },
                        () -> {
                        });
    }
}
