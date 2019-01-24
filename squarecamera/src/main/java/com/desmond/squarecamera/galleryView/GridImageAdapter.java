package com.desmond.squarecamera.galleryView;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.desmond.squarecamera.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.desmond.squarecamera.galleryView.GalleryFragment.TAG;

/**
 * GridAdapter for gallery images
 */

public class GridImageAdapter extends RecyclerView.Adapter<GridImageAdapter.GridViewHolder> {
    private final ItemClickListener mListener;
    private String mAppend;
    private ArrayList<GalleryImage> mImgURLs;
    private boolean isClicked;
    private Context mContext;
    private ArrayList<Integer> mTempList=new ArrayList<>();
    public boolean selectAll=false;

    public GridImageAdapter(ItemClickListener listener, int layoutResource, String append, ArrayList<GalleryImage> imgURLs) {
        mListener = listener;
        mAppend = append;
        mImgURLs = imgURLs;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext=viewGroup.getContext();
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.squarecamera__image_gallery_item, viewGroup, false);

        return new GridViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GridViewHolder gridViewHolder, final int position) {
        final String imgURL = mImgURLs.get(position).getImgPath();

        Log.e("getView", "" + imgURL);
        assert imgURL != null;
        refreshView(gridViewHolder, position);
        if(imgURL.endsWith(".3gpp") || imgURL.endsWith(".mp4") || imgURL.endsWith(".3gp")){
            gridViewHolder.mProgressBar.setVisibility(View.GONE);
            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(imgURL , MediaStore.Video.Thumbnails.MICRO_KIND);
            gridViewHolder.imgGrid.setImageBitmap(bMap);
            gridViewHolder.mDuration.setVisibility(View.VISIBLE);
            gridViewHolder.mDuration.setText(getVideoDuration(new File(imgURL)));
        }else{
            Picasso.get()
                    .load(Uri.fromFile(new File(imgURL)))
                    .fit()
                    .error(R.drawable.ic_close_black_24dp)
                    .into(gridViewHolder.imgGrid, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (gridViewHolder.mProgressBar != null) {
                                gridViewHolder.mProgressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            if (gridViewHolder.mProgressBar != null) {
                                gridViewHolder.mProgressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }


        gridViewHolder.mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position, imgURL);
              //To handle multi selection on single click
                if ((mTempList.size()<10 && mTempList.size()>0 && !mImgURLs.get(position).isSelected) || selectAll) {
                    mImgURLs.get(position).isSelected = true;
                    mListener.onLongItemClick(position, imgURL);
                    gridViewHolder.mParentView.setBackground(mContext.getResources().getDrawable(R.drawable.squarecamera__empty_rect));
                    gridViewHolder.mCount.setVisibility(View.VISIBLE);
                    mTempList.add(position);
                    Log.i(TAG, "onClick: "+mTempList.size());
                    mImgURLs.get(position).selectedPositionCount=mTempList.size();
                    gridViewHolder.mCount.setText(""+mTempList.size());
                }
            }
        });
        gridViewHolder.mParentView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mTempList.size()<10 || (mTempList.size()==10 && mImgURLs.get(position).isSelected)){
                    if (mImgURLs.get(position).isSelected) {
                        mImgURLs.get(position).isSelected = false;
                        gridViewHolder.mParentView.setBackground(null);
                        mListener.onLongItemClick(position, imgURL);
                        gridViewHolder.mCount.setVisibility(View.GONE);
                        mImgURLs.get(position).selectedPositionCount=0;     //to nullify count
                        refreshCountPositionList(position);
                    } else {
                        mImgURLs.get(position).isSelected = true;
                        mListener.onLongItemClick(position, imgURL);
                        gridViewHolder.mParentView.setBackground(mContext.getResources().getDrawable(R.drawable.squarecamera__empty_rect));
                        gridViewHolder.mCount.setVisibility(View.VISIBLE);
                        mTempList.add(position);
                        mImgURLs.get(position).selectedPositionCount=mTempList.size();  //add image selected count
                        gridViewHolder.mCount.setText(""+mTempList.size());
                    }
                }else{
                    Toast.makeText(mContext, R.string.squarecamera__limit_exceeded,Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });
    }

    private String getVideoDuration(File file) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(mContext, Uri.fromFile(file));
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMillisec = Long.parseLong(time );
        retriever.release();
        return String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(timeInMillisec),
                TimeUnit.MILLISECONDS.toSeconds(timeInMillisec) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMillisec)));
    }

    private void refreshCountPositionList(int position) {
        mTempList.remove(mTempList.indexOf(position));
        Log.i(TAG, "refreshCountPositionList: "+mTempList);
    }

    private void refreshView(@NonNull GridViewHolder gridViewHolder, int position) {
        if(!mImgURLs.get(position).isSelected){
            gridViewHolder.mParentView.setBackground(null);
            gridViewHolder.mCount.setVisibility(View.GONE);
        }else{
            gridViewHolder.mParentView.setBackground(mContext.getResources().getDrawable(R.drawable.squarecamera__empty_rect));
            gridViewHolder.mCount.setVisibility(View.VISIBLE);
            Log.i(TAG, "refreshView: "+position+"!!!"+mTempList.indexOf(position));
            gridViewHolder.mCount.setText(""+(mTempList.indexOf(position)+1));
        }
    }

    @Override
    public int getItemCount() {
        return mImgURLs.size();
    }


    public class GridViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgGrid;
        private final ProgressBar mProgressBar;
        FrameLayout mParentView;
        TextView mCount,mDuration;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            mParentView = itemView.findViewById(R.id.itemLayoutView);
            imgGrid = itemView.findViewById(R.id.gridImageView);
            mProgressBar = itemView.findViewById(R.id.gridImageProgressbar);
            mCount = itemView.findViewById(R.id.count);
            mDuration = itemView.findViewById(R.id.duration);
        }
    }

}
