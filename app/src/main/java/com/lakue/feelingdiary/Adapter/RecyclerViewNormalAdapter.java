package com.lakue.feelingdiary.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lakue.feelingdiary.Base.BaseItem;
import com.lakue.feelingdiary.Base.BaseViewHolder;
import com.lakue.feelingdiary.R;
import com.lakue.feelingdiary.listener.OnItemClickListener;
import com.lakue.feelingdiary.type.RecyclerViewType;
import com.lakue.feelingdiary.viewholder.ViewHolderContent;
import com.lakue.feelingdiary.viewholder.ViewHolderEmoji;

import java.util.ArrayList;

public class RecyclerViewNormalAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    RecyclerViewType type;
    ArrayList<BaseItem> items = new ArrayList<>();

    OnItemClickListener onItemClickListener;

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
        } else  if(type == RecyclerViewType.CONTENT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content,parent,false);
            return new ViewHolderContent(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if(holder instanceof ViewHolderEmoji){
            ViewHolderEmoji viewHolderEmoji = (ViewHolderEmoji)holder;
            viewHolderEmoji.onBind(items.get(position),position);
            viewHolderEmoji.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int item) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClick(view, item);
                    }
                }
            });
        } else  if(holder instanceof ViewHolderContent){
            ViewHolderContent viewHolderContent = (ViewHolderContent)holder;
            viewHolderContent.onBind(items.get(position),position);
            viewHolderContent.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int item) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClick(view, item);
                    }
                }
            });
        }


    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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
