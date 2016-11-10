package com.gaowenyun.gift.ui.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.bean.DailyRvBean;
import com.gaowenyun.gift.utils.GiftOnRvItemClick;
import com.gaowenyun.gift.utils.ScreenSizeUtil;


import java.util.List;

/**
 *
 * 榜单-每日推荐界面适配器
 */
public class DailyRvAdapter extends RecyclerView.Adapter<DailyRvAdapter.ViewHolder> {
    private static final int TYPE_ITEM = 1, TYPE_HEAD = 0;
    private String imgUrl;
    private Context context;
    private List<DailyRvBean.DataBean.ItemsBean> datas;
    private GiftOnRvItemClick giftOnRvItemClick;

    public void setGiftOnRvItemClick(GiftOnRvItemClick giftOnRvItemClick) {
        this.giftOnRvItemClick = giftOnRvItemClick;
    }


    public DailyRvAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<DailyRvBean.DataBean.ItemsBean> datas, String imgUrl) {
        this.datas = datas;
        this.imgUrl = imgUrl;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int type = TYPE_ITEM;
        if (position == 0) {
            type = TYPE_HEAD;
        }
        return type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case TYPE_ITEM:
                view = LayoutInflater.from(context).inflate(R.layout.item_daily_rv, parent, false);
                break;
            case TYPE_HEAD:
                view = LayoutInflater.from(context).inflate(R.layout.item_daily_head, parent, false);
                break;
        }
        return new ViewHolder(view);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEAD ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ITEM:
                final DailyRvBean.DataBean.ItemsBean bean = datas.get(position - 1);
                Glide.with(context).load(bean.getCover_image_url()).thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.dailyIv);
                if (bean.getShort_description() != "") {
                    holder.dailyName.setText(bean.getName());
                    holder.dailyDescription.setText(bean.getShort_description());
                    holder.dailyPrice.setText("￥ " + subZeroAndDot(bean.getPrice()));
                } else {
                    holder.dailyDescription.setText(bean.getName());// 当dailyDescryption为"" 设置到dailyName的位置上显示
                    holder.dailyName.setText("");
                    holder.dailyPrice.setText("￥ " + subZeroAndDot(bean.getPrice()));
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (giftOnRvItemClick != null) {
                            int p = holder.getLayoutPosition();
                            giftOnRvItemClick.onRvItemClickListener(p, bean);
                        }
                    }
                });
                break;
            case TYPE_HEAD:
                // 0的position在这被用了
                ViewGroup.LayoutParams params = holder.dailyHeadIv.getLayoutParams();
                params.width = ScreenSizeUtil.getScreenWidth(context);
                params.height = ScreenSizeUtil.getScreenHeight(context) * 14 / 48;
                holder.dailyHeadIv.setLayoutParams(params);
                Glide.with(context).load(imgUrl).thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.dailyHeadIv);
                break;
        }
    }

    /**
     * 去掉多余的0 和 小数点
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是"."则去掉
        }
        return s;
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size() + 1;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView dailyIv;
        ImageView dailyHeadIv;
        TextView dailyName, dailyDescription, dailyPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            dailyIv = (ImageView) itemView.findViewById(R.id.daily_item_iv);
            dailyDescription = (TextView) itemView.findViewById(R.id.daily_item_short_description);
            dailyName = (TextView) itemView.findViewById(R.id.daily_item_name);
            dailyPrice = (TextView) itemView.findViewById(R.id.daily_item_price);
            dailyHeadIv = (ImageView) itemView.findViewById(R.id.daily_header_iv);
        }
    }
}
