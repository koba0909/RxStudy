package com.testcode.btyisu.rxstudy.example;

import android.annotation.SuppressLint;
import android.util.Log;

import io.reactivex.Observable;

public class test {
    @SuppressLint("CheckResult")
    public void test(){
        String[] balls = {"1", "2", "3", "5"};
        Observable<String> source = Observable.fromArray(balls)
                .map(ball -> ball + "<>");
        source.subscribe(System.out::println);
    }
}
