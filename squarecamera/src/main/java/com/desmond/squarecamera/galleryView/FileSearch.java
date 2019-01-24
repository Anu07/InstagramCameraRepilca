package com.desmond.squarecamera.galleryView;

import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class FileSearch {
    private static final String TAG = FileSearch.class.getName();
    private static String[] extArray= {".jpg",".mp4",".3gpp",".3gp",".jpeg",".png",".bmp"};
    /**
     * Search a directory and return a list of all **directories** contained inside
     *
     * @param directory
     * @return
     */
    public static ArrayList<String> getDirectoryPaths(String directory) {
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listfiles = file.listFiles();
        if (listfiles != null)
            for (int i = 0; i < listfiles.length; i++) {
                if (listfiles[i].isDirectory()) {
                    pathArray.add(listfiles[i].getAbsolutePath());
                }
            }
        return pathArray;
    }

    /**
     * Search a directory and return a list of all **files** contained inside
     *
     * @param directory
     * @return
     */
    public static ArrayList<String> getFilePaths(Albums directory) {
        ArrayList<String> pathArray = new ArrayList<>();
        File[] listfiles = null;
        Log.i(TAG, "getFilePaths: "+directory.getImagePath());
        File file = new File(new File(directory.getImagePath()).getParent());
        if (file.isDirectory()) {
            listfiles = file.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String f) {

                    for (String ext:extArray) {
                        if(f.endsWith(ext))
                            return true;
                    }
                    return false;
                }
            });
        }
        if (listfiles != null)
            for (int i = 0; i < listfiles.length; i++) {
                File f=listfiles[i];
                for (String ext:extArray) {
                    if(f.getName().endsWith(ext))
                        pathArray.add(listfiles[i].getAbsolutePath());
                }
            }
        Log.i(TAG, "getFilePaths: "+pathArray.size());
        return pathArray;
    }
}
