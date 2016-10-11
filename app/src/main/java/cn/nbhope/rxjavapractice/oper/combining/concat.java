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

import cn.nbhope.rxjavapractice.R;
import cn.nbhope.rxjavapractice.utils.L;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * @Description concat 不交错的发射两个或多个Observable的发射物
 * https://github.com/mcxiaoke/RxDocs/blob/master/operators/Mathematical.md#Concat
 * Created by EthanCo on 2016/10/11.
 */

public class concat extends Fragment {

    private ViewDataBinding binding;
    private TextView tvInfo;

    public static Fragment newInstance() {
        concat fragment = new concat();
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


        //抛出异常，在concat中仍然会调用onError，
//        Observable<Long> observable1 = Observable.just(null)
//                .map(new Func1<Object, Long>() {
//                    @Override
//                    public Long call(Object o) {
//                        return Long.valueOf(Integer.valueOf(String.valueOf(o)));
//                    }
//                });

        Observable<Long> observable1 = Observable.just(null)
                .map(new Func1<Object, Long>() {
                    @Override
                    public Long call(Object o) {
                        return -1L;
                    }
                })
                .filter(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        return aLong>0;
                    }
                });


        Observable<Long> observable2 = Observable.just(23L);

        Observable.concat(observable1, observable2)
                .first()
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        L.i("Z-Sequence complete.");
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i("Z-Error: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Long aLong) {
                        L.i("Z-Next:" + aLong);
                    }
                });
    }
}