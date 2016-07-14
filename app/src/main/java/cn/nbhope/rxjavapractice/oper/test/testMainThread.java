package cn.nbhope.rxjavapractice.oper.test;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.nbhope.rxjavapractice.R;
import cn.nbhope.rxjavapractice.utils.L;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 在just中的方法，执行的时候是否在子线程 - 实验证明，不在 T T
 */
public class TestMainThread extends Fragment {

    private ViewDataBinding binding;
    private TextView tvInfo;

    public static Fragment newInstance() {
        TestMainThread fragment = new TestMainThread();
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

        /*Observable.just(1, "hello world", true, 200L, 0.23f)
                .ofType(Float.class)
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onNext(Object item) {
                        L.i("Next: " + item);
                    }

                    @Override
                    public void onError(Throwable error) {
                        L.e("Error: " + error.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        L.i("Sequence complete.");
                    }
                });*/

        Observable.just(getValue())
                .doOnNext(s -> {
                    L.i("是否是主线程2:" + String.valueOf(isMainThread()));
                })
                .map(s1 -> Integer.valueOf(s1))
                .doOnNext(s -> {
                    L.i("是否是主线程2.5:" + String.valueOf(isMainThread()));
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(response -> {
                            L.i("是否是主线程3:" + String.valueOf(isMainThread()));
                        },
                        throwable -> {
                        },
                        () -> {
                            L.i("是否是主线程4:" + String.valueOf(isMainThread()));
                        });
    }

    public String getValue() {
        L.i("是否是主线程:" + String.valueOf(isMainThread()));

        return "1233333";
    }

    public boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
