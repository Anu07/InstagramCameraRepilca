package com.desmond.squarecamera.galleryView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.desmond.squarecamera.EditSavePhotoFragment;
import com.desmond.squarecamera.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Adapter to display multiple images in horizontal view
 */


public class HorizontalImageAdapter extends RecyclerView.Adapter<HorizontalImageAdapter.ImageViewHolder> {
    private Context mContext;
    ArrayList<Bitmap>  mPaths;

    public HorizontalImageAdapter( ArrayList<Bitmap> mPaths) {
        this.mPaths = mPaths;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext=viewGroup.getContext();
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.squarecamera__item_horizontal_img, viewGroup, false);

        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, final int i) {
        imageViewHolder.mImg.setImageBitmap(mPaths.get(i));
    }

    @Override
    public int getItemCount() {
        return mPaths.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView mImg;
        LinearLayout mParent;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            mParent=itemView.findViewById(R.id.horizontalParent);
            mImg= itemView.findViewById(R.id.place_holder_imageview);
        }
    }

}
