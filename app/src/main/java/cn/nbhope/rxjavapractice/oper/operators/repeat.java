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

import cn.nbhope.rxjavapractice.R;
import cn.nbhope.rxjavapractice.utils.L;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * http://blog.csdn.net/axuanqq/article/details/50695737
 * <p>
 * repeat
 */
public class repeat extends Fragment {

    private ViewDataBinding binding;
    private TextView tvInfo;

    public static Fragment newInstance() {
        repeat fragment = new repeat();
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


        Observable.range(1, 5)
                .repeat(3) //重复执行3次 observable -> onNext ，最后执行onCompleted()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        L.i("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e(e.getMessage());
                    }

                    @Override
                    public void onNext(Integer i) {
                        L.i("onNext:" + i);
                    }
                });
    }
}
