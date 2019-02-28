package com.espfullstack.wedoo.pojo;

public class UploadImages {
    private String mImageUrl;

    public UploadImages(){

    }

    public UploadImages(String imageUrl){
        mImageUrl = imageUrl;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
