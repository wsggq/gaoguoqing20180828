package com.example.ggq.gaoguoqing20180828.di;

import com.example.ggq.gaoguoqing20180828.common.Api;
import com.example.ggq.gaoguoqing20180828.utils.OKHttpUtil;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class IModelImpl implements IContract.IModel{
    @Override
    public void requestData(final onCallbackListener onCallbackListener) {
        OKHttpUtil.getInstance().get(Api.SEARCHPRODUCTS_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onCallbackListener.onCallback(e.getMessage().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                onCallbackListener.onCallback(string);
            }
        });
    }
}
