package kh.com.mysabay.sdk.webservice;

import android.content.Context;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;

import io.reactivex.observers.DisposableObserver;
import kh.com.mysabay.sdk.R;
import kh.com.mysabay.sdk.base.BaseView;
import kh.com.mysabay.sdk.utils.IdlingResourceHelper;
import kh.com.mysabay.sdk.utils.MessageUtil;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created by Tan Phirum on 3/2/18.
 */
public abstract class AbstractDisposableObs<T> extends DisposableObserver<T> {

    private WeakReference<BaseView> mWeakRefBaseView;
    private WeakReference<IdlingResourceHelper> mWeakRefIdlingState;
    private WeakReference<Context> mWeakRefContext;

    public AbstractDisposableObs(Context context, IdlingResourceHelper idlingResource) {
        this(context, null, idlingResource);
    }

    /**
     * @param context  if has context you don't need to handle error on baseview
     * @param baseView nullable
     * @param mHelper  can't null, convenient on unit test
     */
    public AbstractDisposableObs(Context context, BaseView baseView, IdlingResourceHelper mHelper) {
        this.mWeakRefBaseView = new WeakReference<>(baseView);
        this.mWeakRefIdlingState = new WeakReference<>(mHelper);
        this.mWeakRefContext = new WeakReference<>(context);
        if (baseView != null)
            baseView.showProgressBar(true);

        if (mHelper != null)
            mHelper.setIdleState(false);
    }

    protected abstract void onSuccess(T t);

    @Override
    public void onNext(T object) {
        releaseIdlingState();
        if (mWeakRefBaseView.get() != null) {
            BaseView view = mWeakRefBaseView.get();
            view.showProgressBar(false);
        }

        onSuccess(object);
    }

    @Override
    public void onError(Throwable e) {
        releaseIdlingState();
        if (mWeakRefBaseView.get() != null) {
            BaseView view = mWeakRefBaseView.get();
            view.showProgressBar(false);
        }

        Context context = null;
        if (mWeakRefContext != null)
            context = mWeakRefContext.get();
        if (context == null)
            return;

        if (e instanceof HttpException) {
            //ResponseBody responseBody = ((HttpException) e).response().errorBody();
            MessageUtil.displayToast(context, context.getString(R.string.msg_can_not_connect_server));
        } else if (e instanceof SocketTimeoutException) {
            MessageUtil.displayToast(context, context.getString(R.string.msg_can_not_connect_internet));
        } else if (e instanceof IOException) {
            MessageUtil.displayToast(context, context.getString(R.string.msg_can_not_connect_internet));
        } else
            MessageUtil.displayToast(context, context.getString(R.string.msg_can_not_connect_server));
    }

    @Override
    public void onComplete() {

    }

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private void releaseIdlingState() {
        IdlingResourceHelper idlingResourceHelper = mWeakRefIdlingState.get();
        if (idlingResourceHelper != null) idlingResourceHelper.setIdleState(true);
    }
}
