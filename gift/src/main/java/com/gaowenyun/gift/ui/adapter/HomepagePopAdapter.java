package com.gaowenyun.gift.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.gaowenyun.gift.R;
import com.gaowenyun.gift.utils.HomepagePopRvItemClick;

import java.util.List;

/**
 *
 * 首页-PopWindow-RecyclerView适配器
 */
public class HomepagePopAdapter extends RecyclerView.Adapter<HomepagePopAdapter.MyViewHolder> {
    private List<String> datas;
    private Context context;
    private HomepagePopRvItemClick popRvItemClick;
    private int selectIndex;

    public void setIndex(int index) {
        selectIndex = index;
    }

    public void setPopRvItemClick(HomepagePopRvItemClick popRvItemClick) {
        this.popRvItemClick = popRvItemClick;
    }

    public HomepagePopAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_popwindow, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.popTv.setText(datas.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popRvItemClick != null && datas.get(position) != null) {
                    int p = holder.getLayoutPosition();
                    String string = datas.get(p);
                    popRvItemClick.OnPopRvItemClickListener(p, string);
                }
            }
        });
        if (position == selectIndex) {
            holder.popTv.setTextColor(Color.parseColor("#ED2D44"));
            holder.popView.setBackgroundColor(Color.parseColor("#ED2D44"));
        }else {
            holder.popTv.setTextColor(Color.BLACK);
            holder.popView.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return datas != null && datas.size() > 0 ? datas.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView popTv;
        private View popView;

        public MyViewHolder(View itemView) {
            super(itemView);
            popTv = (TextView) itemView.findViewById(R.id.item_pop_tv);
            popView = itemView.findViewById(R.id.item_pop_view);
        }
    }
}
