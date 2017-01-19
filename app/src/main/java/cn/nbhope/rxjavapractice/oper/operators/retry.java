package cn.nbhope.rxjavapractice.oper.operators;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import cn.nbhope.rxjavapractice.R;
import cn.nbhope.rxjavapractice.utils.L;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 如果出现错误，将重新尝试 (尝试次数最多为指定次数)
 */
public class retry extends Fragment {

    private ViewDataBinding binding;
    private TextView tvInfo;

    public static Fragment newInstance() {
        retry fragment = new retry();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_text, container, false);
        tvInfo = (TextView) binding.getRoot().findViewById(R.id.tvInfo);
        return binding.getRoot();
    }

    int i = 0;

    @Override
    public void onStart() {
        super.onStart();

        Observable.create((Subscriber<? super String> s) -> {
            System.out.println("subscribing");
            s.onError(new RuntimeException("always fails"));
        }).retryWhen(attempts -> {
            return attempts.zipWith(Observable.range(1, 3), (n, i) -> i).flatMap(i -> {
                System.out.println("delay retry by " + i + " second(s)");
                return Observable.timer(i, TimeUnit.SECONDS);
            });
        }).toBlocking().forEach(System.out::println);

        Observable.just("1000")
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        i++;
                        if (i <= 3) {
                            L.w("call throw exception: " + i);
                            throw new IllegalStateException("error" + i);
                        }
                    }
                })
                .retry(5) //最多尝试5次

                //这个和上面的那个相同
//                .retry(new Func2<Integer, Throwable, Boolean>() {
//                    @Override
//                    public Boolean call(Integer integer, Throwable throwable) {
//                        if (integer < 5) {
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    }
//                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        L.i("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e(e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        L.i("onNext");
                    }
                });
    }
}
