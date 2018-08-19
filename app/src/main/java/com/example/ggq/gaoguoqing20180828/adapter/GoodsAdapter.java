package com.example.ggq.gaoguoqing20180828.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ggq.gaoguoqing20180828.R;
import com.example.ggq.gaoguoqing20180828.bean.InfoBean;
import com.squareup.picasso.Picasso;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder>{
    private Context context;
    private List<InfoBean.DataBean> list;

    public GoodsAdapter(Context context, List<InfoBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] images = list.get(position).getImages().split("\\|");
        Picasso.get().load(images[0]).into(holder.iv_item_icon);
        holder.tv_showInfo.setText(list.get(position).getCreatetime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView iv_item_icon;
        private TextView tv_showInfo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            iv_item_icon = itemView.findViewById(R.id.iv_item_icon);
            tv_showInfo = itemView.findViewById(R.id.tv_showInfo);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getLayoutPosition()-1);
        }

        @Override
        public boolean onLongClick(View v) {
            onItemLongClickListener.onItemLongClick(getLayoutPosition());
            return true;
        }
    }
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int layoutPosition);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(int layoutPosition);
    }
}
