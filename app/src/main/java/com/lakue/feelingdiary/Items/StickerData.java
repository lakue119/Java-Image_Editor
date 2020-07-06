package com.lakue.feelingdiary.Items;

import android.graphics.Matrix;
import android.graphics.drawable.Drawable;

import com.lakue.feelingdiary.Base.BaseItem;
import com.lakue.feelingdiary.Point;

import java.util.Arrays;

public class StickerData extends BaseItem {
    int idx = 0;
    int position = 0;
    int width = 0;
    int height = 0;
    Drawable d = null;

    float[] matrixValues = new float[9];
    float[] unrotatedWrapperCorner = new float[8];
    float[] unrotatedPoint = new float[2];
    float[] boundPoints = new float[8];
    float[] mappedBounds = new float[8];
    Matrix matrix = new Matrix();

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

    public float[] getMatrixValues() {
        return matrixValues;
    }

    public void setMatrixValues(float[] matrixValues) {
        this.matrixValues = matrixValues;
    }

    public float[] getUnrotatedWrapperCorner() {
        return unrotatedWrapperCorner;
    }

    public void setUnrotatedWrapperCorner(float[] unrotatedWrapperCorner) {
        this.unrotatedWrapperCorner = unrotatedWrapperCorner;
    }

    public float[] getUnrotatedPoint() {
        return unrotatedPoint;
    }

    public void setUnrotatedPoint(float[] unrotatedPoint) {
        this.unrotatedPoint = unrotatedPoint;
    }

    public float[] getBoundPoints() {
        return boundPoints;
    }

    public void setBoundPoints(float[] boundPoints) {
        this.boundPoints = boundPoints;
    }

    public float[] getMappedBounds() {
        return mappedBounds;
    }

    public void setMappedBounds(float[] mappedBounds) {
        this.mappedBounds = mappedBounds;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public String toString() {
        return "StickerData{" +
                "idx=" + idx +
                ", position=" + position +
                ", width=" + width +
                ", height=" + height +
                ", d=" + d +
                ", \nmatrixValues=" + Arrays.toString(matrixValues) +
                ", \nunrotatedWrapperCorner=" + Arrays.toString(unrotatedWrapperCorner) +
                ", \nunrotatedPoint=" + Arrays.toString(unrotatedPoint) +
                ", \nboundPoints=" + Arrays.toString(boundPoints) +
                ", \nmappedBounds=" + Arrays.toString(mappedBounds) +
                ", \nmatrix=" + matrix +
                '}' + "\n\n\n\n";
    }
}
