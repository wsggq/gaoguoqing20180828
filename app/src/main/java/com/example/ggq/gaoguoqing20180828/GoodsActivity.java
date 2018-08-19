package com.example.ggq.gaoguoqing20180828;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.ggq.gaoguoqing20180828.adapter.GoodsAdapter;
import com.example.ggq.gaoguoqing20180828.bean.InfoBean;
import com.example.ggq.gaoguoqing20180828.di.IContract;
import com.example.ggq.gaoguoqing20180828.di.IPresenterImpl;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsActivity extends AppCompatActivity implements IContract.IView {


    @BindView(R.id.xecycler_view)
    XRecyclerView xecyclerView;
    @BindView(R.id.btn_show_map)
    Button btnShowMap;
    private Context context;
    private IContract.IPresenter iPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        ButterKnife.bind(this);
        context = GoodsActivity.this;

        iPresenter = new IPresenterImpl();
        iPresenter.attachView(this);
        iPresenter.requestInfo();
    }

    @Override
    public void loading() {

    }

    @Override
    public void showData(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                InfoBean infoBean = gson.fromJson(msg, InfoBean.class);
                final List<InfoBean.DataBean> data = infoBean.getData();
                xecyclerView.setLayoutManager(new LinearLayoutManager(GoodsActivity.this));
                GoodsAdapter adapter = new GoodsAdapter(GoodsActivity.this, data);
                xecyclerView.setLoadingMoreEnabled(true);
                xecyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new GoodsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int layoutPosition) {
                        data.remove(layoutPosition);
                        xecyclerView.notifyItemRemoved(data,layoutPosition);
                    }
                });
                adapter.setOnItemLongClickListener(new GoodsAdapter.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(int layoutPosition) {
                        data.add(data.get(layoutPosition));
                        xecyclerView.notifyItemInserted(data, layoutPosition);
                    }
                });
                //xecyclerView.setLoadingListener(this);
            }
        });
    }

    @Override
    public void completed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iPresenter.detachView(this);
    }

    @OnClick(R.id.btn_show_map)
    public void onViewClicked() {
        startActivity(new Intent(GoodsActivity.this, MapActivity.class));
    }
}
