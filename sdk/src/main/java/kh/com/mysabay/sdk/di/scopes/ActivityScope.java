package kh.com.mysabay.sdk.di.scopes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Tan Phirum on 2019-12-27
 * Gmail phirumtan@gmail.com
 */
// Definition of a custom scope called ActivityScope
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}
