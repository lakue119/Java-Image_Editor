package com.lakue.feelingdiary.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lakue.feelingdiary.Base.BaseItem;
import com.lakue.feelingdiary.Base.BaseViewHolder;
import com.lakue.feelingdiary.R;
import com.lakue.feelingdiary.listener.OnEmojiClickListener;
import com.lakue.feelingdiary.type.RecyclerViewType;
import com.lakue.feelingdiary.viewholder.ViewHolderEmoji;

import java.util.ArrayList;

public class RecyclerViewNormalAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    RecyclerViewType type;
    ArrayList<BaseItem> items = new ArrayList<>();

    OnEmojiClickListener onEmojiClickListener;

    public RecyclerViewNormalAdapter(RecyclerViewType type){
        this.type = type;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(type == RecyclerViewType.EMOJI){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emoji,parent,false);
            return new ViewHolderEmoji(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if(holder instanceof ViewHolderEmoji){
            ViewHolderEmoji viewHolderEmoji = (ViewHolderEmoji)holder;
            viewHolderEmoji.onBind(items.get(position),position);
            viewHolderEmoji.setOnEmojiClickListener(new OnEmojiClickListener() {
                @Override
                public void onEmojiClick(int emoji) {
                    if(onEmojiClickListener != null){
                        onEmojiClickListener.onEmojiClick(emoji);
                    }
                }
            });
        }

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setOnEmojiClickListener(OnEmojiClickListener onEmojiClickListener) {
        this.onEmojiClickListener = onEmojiClickListener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(BaseItem data) {
        // 외부에서 item을 추가시킬 함수입니다.
        items.add(data);
        notifyItemChanged(items.size()-1);
    }

}
