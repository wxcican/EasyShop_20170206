package com.fuicuiedu.xc.easyshop_20170206.model;

import android.graphics.Bitmap;


import com.fuicuiedu.xc.easyshop_20170206.commons.Bimp;

import java.io.IOException;
import java.io.Serializable;


/**
 * 图片上传时,Item布局对应的实体
 */
@SuppressWarnings("unused")
public class ImageItem implements Serializable {
    /**
     * 图片路径
     */
    public String imagePath;

    /**
     * 图片是否选中状态,默认为false
     */
    private boolean isCheck = false;
    /**
     * 根据图片路径获得BitMap
     */
    private Bitmap bitmap;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public Bitmap getBitmap() {
        if (bitmap == null) {
            try {
                bitmap = Bimp.revisionImageSize(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


}
