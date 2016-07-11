package cn.nbhope.rxjavapractice.oper.combining;

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
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @Description Scan
 *
 * join操作符把类似于combineLatest操作符，也是两个Observable产生的结果进行合并，合并的结果组成一个新的Observable，
 * 但是join操作符可以控制每个Observable产生结果的生命周期，在每个结果的生命周期内，可以与另一个Observable产生的结果按照一定的规则进行合并
 *
 * Created by EthanCo on 2016/6/30.
 */
public class Join extends Fragment {

    private ViewDataBinding binding;
    private TextView tvInfo;

    public static Fragment newInstance() {
        Join fragment = new Join();
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

        //产生0,5,10,15,20数列
        Observable<Long> observable1 = Observable.timer(0, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        long value = aLong * 5;
                        L.i("Z-o1:" + value);
                        return value;
                    }
                }).take(5);

        //产生0,10,20,30,40数列
        Observable<Long> observable2 = Observable.timer(500, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        long value = aLong * 10;
                        L.i("Z-o2:" + value);
                        return value;
                    }
                }).take(5);

        observable1.join(observable2, new Func1<Long, Observable<Long>>() {
            @Override
            public Observable<Long> call(Long aLong) {
                //使Observable延迟600毫秒执行
                return Observable.just(aLong).delay(600, TimeUnit.MILLISECONDS);
            }
        }, new Func1<Long, Observable<Long>>() {
            @Override
            public Observable<Long> call(Long aLong) {
                //使Observable延迟600毫秒执行
                return Observable.just(aLong).delay(600, TimeUnit.MILLISECONDS);
            }
        }, new Func2<Long, Long, Long>() {
            @Override
            public Long call(Long aLong, Long aLong2) {
                return aLong + aLong2;
            }
        }).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                L.i("Z-Sequence complete.");
            }

            @Override
            public void onError(Throwable e) {
                L.i("Z-" + "Error: " + e.getMessage());
            }

            @Override
            public void onNext(Long aLong) {
                L.i("Z-onNext:" + aLong);
            }
        });
    }
}
