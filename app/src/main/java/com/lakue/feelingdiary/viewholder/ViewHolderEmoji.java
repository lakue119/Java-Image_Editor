package com.lakue.feelingdiary.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.lakue.feelingdiary.Base.BaseItem;
import com.lakue.feelingdiary.Base.BaseViewHolder;
import com.lakue.feelingdiary.Items.ItemEmoji;
import com.lakue.feelingdiary.R;
import com.lakue.feelingdiary.listener.OnItemClickListener;

public class ViewHolderEmoji extends BaseViewHolder {

    ImageView iv_emoji;
    ItemEmoji itemEmoji;

    OnItemClickListener onItemClickListener;

    public ViewHolderEmoji(@NonNull View itemView) {
        super(itemView);
        iv_emoji = itemView.findViewById(R.id.iv_emoji);
        iv_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v, itemEmoji.getEmoji());
                }
            }
        });
    }

    public void onBind(BaseItem item, int position){
        itemEmoji = (ItemEmoji)item;
        Glide.with(itemView.getContext()).load(itemEmoji.getEmoji()).into(iv_emoji);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
