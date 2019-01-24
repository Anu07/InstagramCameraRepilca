package com.desmond.squarecamera.galleryView;

import android.os.Environment;

public class FilePaths {
    //"storage/emulated/0"



    public static String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
    public String OTHERS = ROOT_DIR + "/Pictures";
    public String PICTURES = ROOT_DIR + "/Pictures";
    public String CAMERA = ROOT_DIR + "/DCIM/camera";
    public String STORIES = ROOT_DIR + "/Stories";

}
