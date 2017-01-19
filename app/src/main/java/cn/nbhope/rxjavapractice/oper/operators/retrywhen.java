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
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * http://blog.csdn.net/jdsjlzx/article/details/51722365
 *
 * retryWhen和retry类似，区别是，retryWhen将onError中的Throwable传递给一个函数，这个函数产生另一个Observable，
 * retryWhen观察它的结果再决定是不是要重新订阅原始的Observable。如果这个Observable发射了一项数据，它就重新订阅，
 * 如果这个Observable发射的是onError通知，它就将这个通知传递给观察者然后终止。retryWhen默认在trampoline调度器上执行，你可以通过参数指定其它的调度器。
 * Created by EthanCo on 2016/6/30.
 */
public class retrywhen extends Fragment {

    private ViewDataBinding binding;
    private TextView tvInfo;

    public static Fragment newInstance() {
        retrywhen fragment = new retrywhen();
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
    int errorTime = 0;

    @Override
    public void onStart() {
        super.onStart();

//        Observable.create((Subscriber<? super String> s) -> {
//            System.out.println("subscribing");
//            s.onError(new RuntimeException("always fails"));
//        }).retryWhen(attempts -> {
//            return attempts.zipWith(Observable.range(1, 3), (n, i) -> i).flatMap(i -> {
//                System.out.println("delay retry by " + i + " second(s)");
//                return Observable.timer(i, TimeUnit.SECONDS);
//            });
//        }).toBlocking().forEach(System.out::println);

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
//                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() { //每2秒重新尝试一次，最多尝试5次
//                    @Override
//                    public Observable<?> call(Observable<? extends Throwable> observable) {
//                        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
//                            @Override
//                            public Observable<?> call(Throwable throwable) {
//                                if (++errorTime <= 5) {
//                                    return Observable.timer(2000, TimeUnit.MILLISECONDS);
//                                } else {
//                                    return Observable.error(throwable);
//                                }
//                            }
//                        });
//                    }
//                })
                .retryWhen(new RetryWithDelay(5, 2000)) //每两秒尝试一次，最多尝试5次
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

    class RetryWithDelay implements
            Func1<Observable<? extends Throwable>, Observable<?>> {

        private final int maxRetries;
        private final int retryDelayMillis;
        private int retryCount;

        public RetryWithDelay(int maxRetries, int retryDelayMillis) {
            this.maxRetries = maxRetries;
            this.retryDelayMillis = retryDelayMillis;
        }

        @Override
        public Observable<?> call(Observable<? extends Throwable> attempts) {
            return attempts
                    .flatMap(new Func1<Throwable, Observable<?>>() {
                        @Override
                        public Observable<?> call(Throwable throwable) {
                            if (++retryCount <= maxRetries) {
                                // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                                L.i("get error, it will try after " + retryDelayMillis
                                        + " millisecond, retry count " + retryCount);
                                return Observable.timer(retryDelayMillis,
                                        TimeUnit.MILLISECONDS);
                            }
                            // Max retries hit. Just pass the error along.
                            return Observable.error(throwable);
                        }
                    });
        }
    }
}
