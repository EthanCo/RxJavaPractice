package cn.nbhope.rxjavapractice.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

public abstract class BaseActivity extends AppCompatActivity {

    protected static void print(String s) {
        System.out.println("Z-" + s);
    }

    private String currFragmentTag = null;

    /**
     * 改变Fragment，不回退,缓存Fragment
     *
     * @param fragment
     * @param containerViewId
     */
    protected void changeFragment(Fragment fragment, String fragmentTag, int containerViewId) {
        changeFragment(fragment, fragmentTag, containerViewId, null);
    }

    /**
     * 改变Fragment，缓存Fragment
     *
     * @param fragment        Fragment
     * @param containerViewId containerView id
     * @param isBackStack     是否回退
     */
    protected void changeFragment(Fragment fragment, String fragmentTag, int containerViewId, String isBackStack) {
        changeFragment(fragment, fragmentTag, containerViewId, isBackStack, true);
    }

    /**
     * 改变Fragment
     *
     * @param fragment        Fragment
     * @param containerViewId containerView id
     * @param isBackStack     是否回退
     * @param useCache        是否使用缓存 hide-add
     */
    protected void changeFragment(Fragment fragment, String fragmentTag, int containerViewId, String isBackStack, boolean useCache) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transformation = fragmentManager.beginTransaction();
        if (useCache) {
            changeFragmentByCache(fragment, fragmentTag, containerViewId, fragmentManager, transformation);
        } else {
            replaceFragment(fragment, containerViewId, transformation);
        }
        if (TextUtils.isEmpty(isBackStack)) {
            transformation.disallowAddToBackStack();
        } else {
            transformation.addToBackStack(isBackStack);
        }
        transformation.commit();
    }

    private void changeFragmentByCache(Fragment fragment, String fragmentTag, int containerViewId, FragmentManager fragmentManager, FragmentTransaction transformation) {
        if (!TextUtils.isEmpty(currFragmentTag)) {
            Fragment currFragment = fragmentManager.findFragmentByTag(currFragmentTag);
            if (null != currFragment) {
                transformation.hide(currFragment);
            }
        }
        Fragment newFragment = fragmentManager.findFragmentByTag(fragmentTag);
        if (null == newFragment) {
            transformation.add(containerViewId, fragment, fragmentTag);
        } else {
            transformation.show(newFragment);
        }
        currFragmentTag = fragmentTag;
    }

    protected void replaceFragment(Fragment fragment, int containerViewId, FragmentTransaction transformation) {
        transformation.replace(containerViewId, fragment);
    }
}
