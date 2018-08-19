package com.example.ggq.gaoguoqing20180828.di;

public interface IContract {
    public interface IView{
        void loading();
        void showData(String msg);
        void completed();
    }
    public interface IPresenter<IView>{
        void attachView(IView iView);
        void detachView(IView iView);
        void requestInfo();
    }
    public interface IModel{
        void requestData(onCallbackListener onCallbackListener);
        public interface onCallbackListener{
            void onCallback(String msg);
        }
    }
}
