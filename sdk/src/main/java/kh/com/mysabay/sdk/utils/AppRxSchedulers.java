package kh.com.mysabay.sdk.utils;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by phirumtan on 06,May,2019.
 * phirumtan@gmail.com
 */
public class AppRxSchedulers {

    @Inject
    public AppRxSchedulers() {

    }

    public Scheduler io() {
        return Schedulers.io();
    }

    public Scheduler computation() {
        return Schedulers.computation();
    }

    public Scheduler mainThread() {
        return AndroidSchedulers.mainThread();
    }

    public Scheduler newThread() {
        return Schedulers.newThread();
    }
}
