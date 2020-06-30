package com.lakue.feelingdiary.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.lakue.feelingdiary.Base.BaseItem;
import com.lakue.feelingdiary.Base.BaseViewHolder;
import com.lakue.feelingdiary.Items.ItemContent;
import com.lakue.feelingdiary.Items.ItemEmoji;
import com.lakue.feelingdiary.R;
import com.lakue.feelingdiary.listener.OnItemClickListener;

public class ViewHolderContent  extends BaseViewHolder {

    ImageView iv_content;
    ItemContent itemContent;

    OnItemClickListener onItemClickListener;

    public ViewHolderContent(@NonNull View itemView) {
        super(itemView);
        iv_content = itemView.findViewById(R.id.iv_content);
        iv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v, itemContent.getContent());
                }
            }
        });
    }

    public void onBind(BaseItem item, int position){
        itemContent = (ItemContent)item;
        Glide.with(itemView.getContext()).load(itemContent.getContent()).into(iv_content);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
