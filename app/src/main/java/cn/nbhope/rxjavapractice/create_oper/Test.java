package cn.nbhope.rxjavapractice.create_oper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.nbhope.rxjavapractice.BaseTest;
import rx.Observable;

/**
 * @Description Created by EthanCo on 2016/6/27.
 */
public class Test extends BaseTest {

    public static void main(String[] args) {
        print("==JUST==");
        Observable.just("hello")
                .subscribe(value -> {
                            print(value);
                        },
                        throwable -> {
                        },
                        () -> {
                        });


        print("==FROM==");
        List<String> list = new ArrayList<>();
        list.add("item1");
        list.add("item2");
        list.add("item3");
        Observable.from(list)
                .subscribe(value -> {
                            print(value);
                        },
                        throwable -> {
                        },
                        () -> {
                        });


        print("==REPEAT==");
        Observable.just("haha-").repeat(10)
                .subscribe(value -> {
                            print(value);
                        },
                        throwable -> {
                        },
                        () -> {
                        });


        //repeatWhen 不是很好理解，就是可以让订阅者多次订阅，如:第一次订阅1-5 间隔6秒后又会重新订阅一次
        print("==REPEATWHEN==");
        Observable.range(1,3)
                .repeatWhen(observable -> observable.timer(6, TimeUnit.SECONDS))
                .subscribe(value -> {
                            print(String.valueOf(value));
                        },
                        throwable -> {
                        },
                        () -> {
                        });
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
