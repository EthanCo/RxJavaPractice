package cn.nbhope.rxjavapractice.oper.block;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import cn.nbhope.rxjavapractice.R;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * toBlicking.single() 将Observable转为普通类型
 */
public class toBlocking extends Fragment {

    private ViewDataBinding binding;
    private TextView tvInfo;

    public static Fragment newInstance() {
        toBlocking fragment = new toBlocking();
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
    }

    @Override
    public void onResume() {
        super.onResume();

        String result = Observable.just("159")
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                //.delay(60, TimeUnit.SECONDS)
                .toBlocking().single(); //不要在UI线程执行，会阻塞，无论是否是Schedulers.io()

        Toast.makeText(getActivity(), "result:" + result, Toast.LENGTH_SHORT).show();
    }
}
