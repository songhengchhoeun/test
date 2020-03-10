package kh.com.mysabay.sdk.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tan Phirum on 3/10/20
 * Gmail phirumtan@gmail.com
 */
public class SingletonNameViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private UserApiVM myViewModel;

    private final Map<Class<? extends ViewModel>, ViewModel> mFactory = new HashMap<>();
  //  private final Map<Class<? extends ViewModel>, Provider<ViewModel>> creators;

    public SingletonNameViewModelFactory(UserApiVM myViewModel) {
        this.myViewModel = myViewModel;
    }
/*
    public SingletonNameViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> creators) {
        this.creators = creators;
    }*/

    @NonNull
    @Override
    public <T extends ViewModel> T create(final @NonNull Class<T> modelClass) {
        mFactory.put(modelClass, myViewModel);

        if (UserApiVM.class.isAssignableFrom(modelClass)) {
            UserApiVM shareVM = null;

            if (mFactory.containsKey(modelClass)) {
                shareVM = (UserApiVM) mFactory.get(modelClass);
            } else {
                try {
                    shareVM = (UserApiVM) modelClass.getConstructor(Runnable.class).newInstance(new Runnable() {
                        @Override
                        public void run() {
                            mFactory.remove(modelClass);
                        }
                    });
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                } catch (InstantiationException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                }
                mFactory.put(modelClass, shareVM);
            }

            return (T) shareVM;
        }
        return super.create(modelClass);
    }

}