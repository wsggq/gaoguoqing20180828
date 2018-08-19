package com.example.ggq.gaoguoqing20180828.di;

import java.lang.ref.WeakReference;

public class IPresenterImpl implements IContract.IPresenter<IContract.IView>{
    IContract.IView iView;
    private IContract.IModel iModel;
    private WeakReference<IContract.IView> viewWeakReference;
    private WeakReference<IContract.IModel> modelWeakReference;
    @Override
    public void attachView(IContract.IView iView) {
        this.iView = iView;
        iModel = new IModelImpl();
        viewWeakReference = new WeakReference<>(iView);
        modelWeakReference = new WeakReference<>(iModel);
    }

    @Override
    public void detachView(IContract.IView iView) {
        viewWeakReference.clear();
        modelWeakReference.clear();
    }

    @Override
    public void requestInfo() {
        iView.loading();
        iModel.requestData(new IContract.IModel.onCallbackListener() {
            @Override
            public void onCallback(String msg) {
                iView.showData(msg);
                iView.completed();
            }
        });
    }
}
