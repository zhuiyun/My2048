package com.gaowenyun.gift.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.bean.DailyRvBean;
import com.gaowenyun.gift.utils.GiftOnRvItemClick;
import com.gaowenyun.gift.utils.ScreenSizeUtil;


import java.util.List;

/**
 *
 * 榜单-TOP100页面适配器(后面俩页复用)
 */
public class Top100RvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 1, TYPE_HEAD = 0;
    private Context context;
    private List<DailyRvBean.DataBean.ItemsBean> datas;
    private String imgUrl;
    private GiftOnRvItemClick giftOnRvItemClick;

    public void setGiftOnRvItemClick(GiftOnRvItemClick giftOnRvItemClick) {
        this.giftOnRvItemClick = giftOnRvItemClick;
    }

    public Top100RvAdapter(Context context) {
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                View view = LayoutInflater.from(context).inflate(R.layout.item_daily_head, parent, false);
                HolderOneImg holderOneImg = new HolderOneImg(view);
                return holderOneImg;
            case TYPE_ITEM:
                View viewList = LayoutInflater.from(context).inflate(R.layout.item_top100_rv, parent, false);
                HolderList holderList = new HolderList(viewList);
            return holderList;
        }
        return null;
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
                    // 个人观点(从位置0跨列到1)
                    return getItemViewType(position) == TYPE_HEAD ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_ITEM:
                final HolderList holderList = (HolderList) holder;
                final DailyRvBean.DataBean.ItemsBean bean = datas.get(position - 1);
                /**
                 * 设置TOP加数字斜体, 数字相对放大
                 */
                SpannableString span = new SpannableString("TOP" + bean.getOrder());
                span.setSpan(new StyleSpan(Typeface.ITALIC), 0, String.valueOf(bean.getOrder()).length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new RelativeSizeSpan(1.2f), 3, String.valueOf(bean.getOrder()).length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                /**
                 * 前三设置标签不同
                 */
                holderList.topName.setText(bean.getName());
                holderList.topDescription.setText(bean.getShort_description());
                holderList.topPrice.setText("￥ " + subZeroAndDot(bean.getPrice()));
                Glide.with(context).load(bean.getCover_image_url()).into(holderList.topIv);

                if (position > 0 && position < 4) {
                    holderList.topTv1.setText(span);
                    holderList.topTv2.setVisibility(View.GONE);
                }
                if (position >= 4) {
                    holderList.topTv2.setText(span);
                    holderList.topTv1.setVisibility(View.GONE);
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
                HolderOneImg holderOneImg = (HolderOneImg) holder;
                ViewGroup.LayoutParams params = holderOneImg.coverImageView.getLayoutParams();
                params.width = ScreenSizeUtil.getScreenWidth(context);
                params.height = ScreenSizeUtil.getScreenHeight(context) * 7 / 24;
                holderOneImg.coverImageView.setLayoutParams(params);
                Glide.with(context).load(imgUrl).into(holderOneImg.coverImageView);
                break;
        }

    }

    // 去掉多余的0 和 小数点

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


    class HolderOneImg extends RecyclerView.ViewHolder {
        ImageView coverImageView;

        public HolderOneImg(View itemView) {
            super(itemView);
            coverImageView = (ImageView) itemView.findViewById(R.id.daily_header_iv);
        }
    }


    class HolderList extends RecyclerView.ViewHolder {
        ImageView topIv;
        TextView topName, topDescription, topPrice, topTv1, topTv2;

        public HolderList(View itemView) {
            super(itemView);
            topIv = (ImageView) itemView.findViewById(R.id.top_item_iv);
            topDescription = (TextView) itemView.findViewById(R.id.top_item_short_description);
            topName = (TextView) itemView.findViewById(R.id.top_item_name);
            topPrice = (TextView) itemView.findViewById(R.id.top_item_price);
            topTv1 = (TextView) itemView.findViewById(R.id.top_item_tv1);
            topTv2 = (TextView) itemView.findViewById(R.id.top_item_tv2);
        }
    }
}
