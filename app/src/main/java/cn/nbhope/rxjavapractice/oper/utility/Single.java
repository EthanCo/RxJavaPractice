package cn.nbhope.rxjavapractice.oper.utility;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.nbhope.rxjavapractice.R;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * @Description Scan
 * Created by EthanCo on 2016/6/30.
 */
public class Single extends Fragment {

    private ViewDataBinding binding;
    private TextView tvInfo;

    public static Fragment newInstance() {
        Single fragment = new Single();
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
         *  single 操作符也与 first 类似，但是如果原始Observable在完成之前不是正好发射一次数据，它会抛出一个 NoSuchElementException 。
         */
        Observable.just(1,2,3,4,5,6,7,8)
                .single(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        //取大于10的第一个数字
                        return integer>10;
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onNext(Integer item) {
                        System.out.println("Next: " + item);
                    }

                    @Override
                    public void onError(Throwable error) {
                        System.err.println("Error: " + error.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("Sequence complete.");
                    }
                });
    }
}
