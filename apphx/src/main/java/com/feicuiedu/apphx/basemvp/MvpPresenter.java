package com.feicuiedu.apphx.basemvp;


import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

/**
 * Presenter是视图和模型之间的中间人。它通过视图接口({@link MvpView})来控制视图的行为。
 * <p/>
 * 模型层的数据变化通过事件总线({@link EventBus})传递给Presenter。
 *
 * @param <V> 该Presenter所关联的视图类型。
 */
public abstract class MvpPresenter<V extends MvpView> {

    private V view;

    /**
     * Presenter创建的回调。
     * <p/>
     * 在Activity或Fragment的onCreate()方法中调用。
     */
    public final void onCreate() {
        EventBus.getDefault().register(this);
    }

    /**
     * Presenter和视图关联。
     * <p/>
     * 在Activity的onCreate()中调用。
     * <p/>
     * 在Fragment的onViewCreated()或onActivityCreated()方法中调用。
     */
    public final void attachView(V view) {
        this.view = view;
    }


    /**
     * Presenter和视图解除关联。
     * <p/>
     * 在Activity的onDestroy()中调用。
     * <p/>
     * 在Fragment的onViewDestroyed()中调用。
     */
    public final void detachView() {
        // 使用Null Object Pattern，避免子类使用getView()方法时频繁检查null值。
        this.view = getNullObject();
    }

    /**
     * Presenter销毁的回调。
     * <p/>
     * 在Activity或Fragment的onDestroy()方法中调用。
     */
    public final void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    protected final V getView() {
        return view;
    }

    protected abstract @NonNull V getNullObject();
}
