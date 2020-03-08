package kh.com.mysabay.sdk.base;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.test.espresso.IdlingResource;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;

import java.util.Locale;

import kh.com.mysabay.sdk.R;
import kh.com.mysabay.sdk.utils.FontUtils;
import kh.com.mysabay.sdk.utils.IdlingResourceHelper;

/**
 * Created by Tan Phirum on 3/4/20
 * Gmail phirumtan@gmail.com
 */
public abstract class BaseActivity extends AppCompatActivity implements
        /*NetworkRequestUtils.OnConnectionChangedListener,*/
        OnLocaleChangedListener {

    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);

    private boolean isNetWorkHasLost = false;

    public abstract int getLayoutId();

    protected abstract int getToolbarId();

    public abstract void initializeObjects(Bundle args);

    public abstract void assignValues();

    public abstract void addListeners();

    public abstract void onActionAfterCreated();

    protected abstract View getCoordinateLayout();

    protected Toolbar mToolbar;

    protected IdlingResourceHelper mIdlingResourceHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);
        setLanguage("km");
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        //register check network change
        /*NetworkRequestUtils networkRequestUtils = new NetworkRequestUtils(this);
        networkRequestUtils.setOnConnectionChangedListener(this);*/

        if (getToolbarId() > 0) {
            mToolbar = findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
        initializeObjects(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        assignValues();
        addListeners();
        onActionAfterCreated();
    }

    @Override
    protected void onResume() {
        super.onResume();
        localizationDelegate.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(newBase));
    }

    @Override
    public Context getApplicationContext() {
        return localizationDelegate.getApplicationContext(super.getApplicationContext());
    }

    @Override
    public Resources getResources() {
        return localizationDelegate.getResources(super.getResources());
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SpannableString newTitle = FontUtils.getEmbeddedString(this,
                    title.toString(), R.font.font_battambang_bold, false);
            super.setTitle(newTitle);
        } else
            super.setTitle(title);
    }

  /*  @Override
    public void onInternetConnected() {
        if (isNetWorkHasLost) {
            MessageUtil.displaySneakBar(getCoordinateLayout(),
                    R.string.msg_can_not_connect_internet, getString(R.string.online), view -> {
                        //send broadcast internet connection to other registered
                        try {
                            Intent intent = new Intent(NetworkRequestUtils.ACTION_REQUEST_INTERNET_RECONNECTION);
                            intent.putExtra(NetworkRequestUtils.EXT_KEY_IS_CONNECTED, true);
                            LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
            isNetWorkHasLost = false;
        }
    }

    @Override
    public void onInternetDisconnected() {
        isNetWorkHasLost = true;
        MessageUtil.displaySneakBar(getCoordinateLayout(), R.string.msg_can_not_connect_internet);
    }*/

    /**
     * call before change string language
     */
    @Override
    public void onBeforeLocaleChanged() {

    }

    /**
     * call after change string language
     */
    @Override
    public void onAfterLocaleChanged() {

    }

    /**
     * Only called from test, creates and returns a new {@link IdlingResourceHelper}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResourceHelper == null)
            mIdlingResourceHelper = new IdlingResourceHelper();
        return mIdlingResourceHelper;
    }

    public final void setLanguage(String language) {
        localizationDelegate.setLanguage(this, language);
    }

    public final void setLanguage(Locale locale) {
        localizationDelegate.setLanguage(this, locale);
    }

    public final void setDefaultLanguage(String language) {
        localizationDelegate.setDefaultLanguage(language);
    }

    public final void setDefaultLanguage(Locale locale) {
        localizationDelegate.setDefaultLanguage(locale);
    }

    public final Locale getCurrentLanguage() {
        return localizationDelegate.getLanguage(this);
    }


}
