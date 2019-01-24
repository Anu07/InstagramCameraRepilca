package com.desmond.squarecamera.galleryView;




public class GalleryImage {

    String imgPath;
    boolean isSelected=false;
    int selectedPositionCount;

    public GalleryImage(String imgPath) {
        this.imgPath = imgPath;
    }


    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
