package cn.nbhope.rxjavapractice.oper.filtering;

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

/**
 * @Description Scan
 * <p>
 * join操作符把类似于combineLatest操作符，也是两个Observable产生的结果进行合并，合并的结果组成一个新的Observable，
 * 但是join操作符可以控制每个Observable产生结果的生命周期，在每个结果的生命周期内，可以与另一个Observable产生的结果按照一定的规则进行合并
 * <p>
 * Created by EthanCo on 2016/6/30.
 */
public class ofType extends Fragment {

    private ViewDataBinding binding;
    private TextView tvInfo;

    public static Fragment newInstance() {
        ofType fragment = new ofType();
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

        Observable.just(1, "hello world", true, 200L, 0.23f)
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
                });
    }
}
