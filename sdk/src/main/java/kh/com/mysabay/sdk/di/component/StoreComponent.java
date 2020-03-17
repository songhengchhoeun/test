package kh.com.mysabay.sdk.di.component;

import dagger.Subcomponent;
import kh.com.mysabay.sdk.di.scopes.ActivityScope;
import kh.com.mysabay.sdk.ui.activity.StoreActivity;
import kh.com.mysabay.sdk.ui.fragment.PaymentFm;
import kh.com.mysabay.sdk.ui.fragment.ShopsFragment;

/**
 * Created by Tan Phirum on 2019-12-27
 * Gmail phirumtan@gmail.com
 */
// Classes annotated with @ActivityScope are scoped to the graph and the same
// instance of that type is provided every time the type is requested.
@ActivityScope
@Subcomponent
public interface StoreComponent {

    // Factory that is used to create instances of this subcomponent
    @Subcomponent.Factory
    interface Factory {
        StoreComponent create();
    }

    void inject(StoreActivity storeActivity);

    void inject(ShopsFragment shopsFragment);

    void inject(PaymentFm paymentFm);
}
