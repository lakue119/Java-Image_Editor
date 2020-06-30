package com.lakue.feelingdiary.Items;

import com.lakue.feelingdiary.Base.BaseItem;

public class ItemContent extends BaseItem {
    int content = 0;

    public ItemContent(int content){
        this.content = content;
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ItemContent{" +
                "content=" + content +
                '}';
    }
}