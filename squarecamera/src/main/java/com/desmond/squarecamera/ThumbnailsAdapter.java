package com.desmond.squarecamera;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.zomato.photofilters.utils.ThumbnailCallback;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

public class ThumbnailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "THUMBNAILS_ADAPTER";
    private static int lastPosition = -1;
    private ThumbnailCallback thumbnailCallback;
    private List<ThumbnailItem> dataSet;

    public ThumbnailsAdapter(List<ThumbnailItem> dataSet, ThumbnailCallback thumbnailCallback) {
        Log.v(TAG, "Thumbnails Adapter has " + dataSet.size() + " items");
        this.dataSet = dataSet;
        this.thumbnailCallback = thumbnailCallback;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.v(TAG, "On Create View Holder Called");
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.squarecamera__list_thumbnail_item, viewGroup, false);
        return new ThumbnailsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
        final ThumbnailItem thumbnailItem = dataSet.get(i);
        Log.v(TAG, "On Bind View Called"+thumbnailItem.filterName);
        ThumbnailsViewHolder thumbnailsViewHolder = (ThumbnailsViewHolder) holder;
        thumbnailsViewHolder.filterText.setText(thumbnailItem.filter.getName());
        thumbnailsViewHolder.thumbnail.setImageBitmap(thumbnailItem.image);
        thumbnailsViewHolder.thumbnail.setScaleType(ImageView.ScaleType.FIT_START);
        setAnimation(thumbnailsViewHolder.thumbnail, i);
        thumbnailsViewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastPosition != i) {
                    thumbnailCallback.onThumbnailClick(thumbnailItem.filter);
                    lastPosition = i;
                }
            }

        });
    }

    private void setAnimation(View viewToAnimate, int position) {
        {
            ViewHelper.setAlpha(viewToAnimate, .0f);
            ViewPropertyAnimator.animate(viewToAnimate).alpha(1).setDuration(250).start();
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ThumbnailsViewHolder extends RecyclerView.ViewHolder {
        private final TextView filterText;
        public ImageView thumbnail;

        public ThumbnailsViewHolder(View v) {
            super(v);
            this.thumbnail = v.findViewById(R.id.thumbnail);
            this.filterText = v.findViewById(R.id.filterTitle);
        }
    }
}

