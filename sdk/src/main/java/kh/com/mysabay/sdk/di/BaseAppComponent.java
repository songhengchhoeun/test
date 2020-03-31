package kh.com.mysabay.sdk.di;

import javax.inject.Singleton;

import dagger.Component;
import kh.com.mysabay.sdk.MySabaySDK;
import kh.com.mysabay.sdk.di.component.StoreComponent;
import kh.com.mysabay.sdk.di.component.UserComponent;
import kh.com.mysabay.sdk.webservice.ServiceGenerator;

/**
 * Created by Tan Phirum on 2019-12-24
 * Gmail phirumtan@gmail.com
 */
@Singleton
@Component(modules = {ServiceGenerator.class, ApplicationModule.class})
public interface BaseAppComponent {

    // This function exposes the LoginComponent Factory out of the graph so consumers
    // can use it to obtain new instances of LoginComponent
    UserComponent.Factory mainComponent();

    StoreComponent.Factory storeComponent();

    void inject(MySabaySDK mySabaySDK);

}