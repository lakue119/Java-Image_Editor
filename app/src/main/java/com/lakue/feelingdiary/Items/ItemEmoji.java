package com.lakue.feelingdiary.Items;

import com.lakue.feelingdiary.Base.BaseItem;

public class ItemEmoji extends BaseItem {
    int emoji = 0;

    public ItemEmoji(int emoji){
        this.emoji = emoji;
    }

    public int getEmoji() {
        return emoji;
    }

    public void setEmoji(int emoji) {
        this.emoji = emoji;
    }

    @Override
    public String toString() {
        return "itemEmoji{" +
                "emoji=" + emoji +
                '}';
    }
}
