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

import java.util.concurrent.TimeUnit;

import cn.nbhope.rxjavapractice.R;
import cn.nbhope.rxjavapractice.utils.L;
import rx.Observable;
import rx.functions.Func1;

/**
 * @Description GroupBy
 * Created by EthanCo on 2016/6/30.
 */
public class GroupBy extends Fragment {

    private ViewDataBinding binding;
    private TextView tvInfo;

    public static Fragment newInstance() {
        GroupBy fragment = new GroupBy();
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

        /**
         * groupBy操作符是对源Observable产生的结果进行分组，形成一个类型为GroupedObservable的结果集，
         * GroupedObservable中存在一个方法为getKey()，可以通过该方法获取结果集的Key值（类似于HashMap的key)。
         值得注意的是，由于结果集中的GroupedObservable是把分组结果缓存起来，如果对每一个GroupedObservable不进行处理
         （既不订阅执行也不对其进行别的操作符运算），就有可能出现内存泄露。因此，如果你对某个GroupedObservable不进行处理，最好是对其使用操作符take(0)处理。
         */
        Observable.interval(1, TimeUnit.SECONDS)
                .take(10)
                .groupBy(new Func1<Long, String>() {
                    @Override
                    public String call(Long value) {
                        return String.valueOf(value % 3);
                    }
                })
                .subscribe(result -> {
                            result.subscribe(value -> {
                                L.i("key:" + result.getKey() + " value:" + value);
                            }, throwable -> {
                            }, () -> {
                            });
                        },
                        throwable -> {
                        },
                        () -> {
                        });
    }
}
