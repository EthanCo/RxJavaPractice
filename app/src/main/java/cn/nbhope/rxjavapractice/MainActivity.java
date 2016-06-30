package cn.nbhope.rxjavapractice;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import cn.nbhope.rxjavapractice.create_oper.Just;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_main);

        changeFragment(new Just(),"just",R.id.layout_container);
    }
}
