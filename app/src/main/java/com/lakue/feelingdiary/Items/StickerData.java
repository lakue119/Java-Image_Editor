package com.lakue.feelingdiary.Items;

import android.graphics.Matrix;
import android.graphics.drawable.Drawable;

import com.lakue.feelingdiary.Base.BaseItem;
import com.lakue.feelingdiary.Point;

public class StickerData extends BaseItem {
    int idx = 0;
    int position = 0;

    int width = 0;
    int height = 0;
//    Point point = new Point();
    Matrix matrix = new Matrix();
    Drawable d = null;

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public Drawable getD() {
        return d;
    }

    public void setD(Drawable d) {
        this.d = d;
    }

    @Override
    public String toString() {
        return "StickerData{" +
                "idx=" + idx +
                ", position=" + position +
                ", width=" + width +
                ", height=" + height +
                ", matrix=" + matrix +
                ", d=" + d +
                '}';
    }
}
