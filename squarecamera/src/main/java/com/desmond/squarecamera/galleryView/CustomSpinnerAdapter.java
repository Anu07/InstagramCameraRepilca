package com.desmond.squarecamera.galleryView;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.desmond.squarecamera.R;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends ArrayAdapter<Albums> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final ArrayList<Albums> items;
    private final int mResource;

    public CustomSpinnerAdapter(@NonNull Context context, @LayoutRes int resource,
                              @NonNull ArrayList<Albums> objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);
        TextView offTypeTv = view.findViewById(R.id.textSpinner);
        Albums offerData = items.get(position);
        offTypeTv.setText(offerData.getFolderNames());
        return view;
    }
}
