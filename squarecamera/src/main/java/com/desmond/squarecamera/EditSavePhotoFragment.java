package com.desmond.squarecamera;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.desmond.squarecamera.galleryView.HorizontalImageAdapter;
import com.desmond.squarecamera.galleryView.ItemOffsetDecoration;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailCallback;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Fragment to put filters on image
 */
public class EditSavePhotoFragment extends Fragment implements ThumbnailCallback {

    public static final String TAG = EditSavePhotoFragment.class.getSimpleName();
    public static final String BITMAP_KEY = "bitmap_byte_array";
    public static final String ROTATION_KEY = "rotation";
    public static final String IMAGE_INFO = "image_info";
    public static final String PATHS = "images_paths";

    private static final int REQUEST_STORAGE = 1;
    private RecyclerView thumbListView;
    Bitmap bitmap;
    ImageView photoImageView;
    private Bitmap mBitmap;
    private RecyclerView horizontalView;
    private View view;
    private HorizontalImageAdapter mAdapter;
    private  ArrayList<Bitmap> mOriginalMaps= new ArrayList<>();
    private ArrayList<Bitmap> mBitmaps = new ArrayList<>();
    ArrayList<String> path;
    private TextView mNextView;
    private ImageView shareClose;

    public static Fragment newInstance(byte[] bitmapByteArray, int rotation,
                                       @NonNull ImageParameters parameters) {
        Fragment fragment = new EditSavePhotoFragment();

        Bundle args = new Bundle();
        args.putByteArray(BITMAP_KEY, bitmapByteArray);
        args.putInt(ROTATION_KEY, rotation);
        args.putParcelable(IMAGE_INFO, parameters);

        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new EditSavePhotoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public EditSavePhotoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.squarecamera__fragment_edit_save_photo, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        photoImageView = view.findViewById(R.id.place_holder_imageview);
        horizontalView = view.findViewById(R.id.imgViewList);
        final View topView = view.findViewById(R.id.topView);

        thumbListView = view.findViewById(R.id.thumbnails);
        mNextView = view.findViewById(R.id.tvNext);
        shareClose=view.findViewById(R.id.ivCloseShare);
        mNextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO mOriginalMaps contain filtered images , use them to save images
               /* getFragmentManager()
                        .beginTransaction()
                        .replace(
                                R.id.fragment_container,
                                EditSavePhotoFragment.newInstance(data),
                                EditSavePhotoFragment.TAG)
                        .addToBackStack(null)
                        .commit();*/
            }
        });

        shareClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the gallery fragment.");
                getActivity().finish();
            }
        });

        if (getArguments().containsKey(ROTATION_KEY)) {
            photoImageView.setVisibility(View.VISIBLE);
            horizontalView.setVisibility(View.GONE);
            int rotation = getArguments().getInt(ROTATION_KEY);
            Log.i(TAG, "onViewCreated: " + rotation);
            byte[] data = getArguments().getByteArray(BITMAP_KEY);
            ImageParameters imageParameters = getArguments().getParcelable(IMAGE_INFO);
            if (imageParameters == null) {
                return;
            }

            imageParameters.mIsPortrait =
                    getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

            if (imageParameters.mIsPortrait) {
                topView.getLayoutParams().height = imageParameters.mCoverHeight;
            } else {
                topView.getLayoutParams().width = imageParameters.mCoverWidth;
            }
            rotatePicture(rotation, data, photoImageView);
        } else {
            photoImageView.setVisibility(View.GONE);
            horizontalView.setVisibility(View.VISIBLE);
            path = new ArrayList<>(getArguments().getStringArrayList(PATHS));
            addImageViewsList(path);
        }
        initHorizontalList();
    }

    private void addImageViewsList(ArrayList<String> path) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontalView.setLayoutManager(mLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.squarecamera__item_offset);
        horizontalView.addItemDecoration(itemDecoration);
        mLayoutManager.scrollToPositionWithOffset(0, 20);
        setUpFilterAdapter(path);
        setImage(path.get(0));
    }

    private void setUpFilterAdapter(ArrayList<String> path) {
        mOriginalMaps=getBitmapList(path);
        mAdapter = new HorizontalImageAdapter(mOriginalMaps);
        horizontalView.setAdapter(mAdapter);
    }

    private ArrayList<Bitmap> getBitmapList(ArrayList<String> path) {
        mBitmaps=new ArrayList<>();
        for (int i = 0; i < path.size(); i++) {
            mBitmaps.add(setImage(path.get(i)));
        }
        return mBitmaps;
    }


    private Bitmap setImage(String s) {
        File mFile = new File(s);
        if (mFile.exists()) {
            bitmap = BitmapFactory.decodeFile(mFile.getAbsolutePath());
            Bitmap.createScaledBitmap(bitmap, 400, 600, false);
        }
        return bitmap;
    }

    //New methods
    private void initHorizontalList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        thumbListView.setLayoutManager(layoutManager);
        thumbListView.setHasFixedSize(true);
        bindDataToAdapter();
    }


    private void rotatePicture(int rotation, byte[] data, ImageView photoImageView) {

        bitmap = ImageUtility.decodeSampledBitmapFromByte(getActivity(), data);
//        Log.d(TAG, "original bitmap width " + bitmap.getWidth() + " height " + bitmap.getHeight());
        if (rotation != 0) {
            Bitmap oldBitmap = bitmap;

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);

            bitmap = Bitmap.createBitmap(
                    oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, false
            );

            oldBitmap.recycle();
        }
        photoImageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false));
//        placeHolderImageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false));
    }

    private void savePicture() {
        requestForPermission();
    }

    private void requestForPermission() {
        RuntimePermissionActivity.startActivity(EditSavePhotoFragment.this,
                REQUEST_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK != resultCode) return;

        if (REQUEST_STORAGE == requestCode && data != null) {
            final boolean isGranted = data.getBooleanExtra(RuntimePermissionActivity.REQUESTED_PERMISSION, false);
            final View view = getView();
            if (isGranted && view != null) {
                photoImageView = view.findViewById(R.id.photo);

                Bitmap bitmap = ((BitmapDrawable) photoImageView.getDrawable()).getBitmap();
                Uri photoUri = ImageUtility.savePicture(getActivity(), bitmap);

                ((CameraActivity) getActivity()).returnPhotoUri(photoUri);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void bindDataToAdapter() {
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                Bitmap thumbImage = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);
                ThumbnailsManager.clearThumbs();
                List<Filter> filters = FilterPack.getFilterPack(getContext());

                for (Filter filter : filters) {
                    ThumbnailItem thumbnailItem = new ThumbnailItem();
                    thumbnailItem.image = thumbImage;
                    thumbnailItem.filter = filter;
                    ThumbnailsManager.addThumb(thumbnailItem);
                }

                List<ThumbnailItem> thumbs = ThumbnailsManager.processThumbs(getActivity());

                ThumbnailsAdapter adapter = new ThumbnailsAdapter(thumbs, EditSavePhotoFragment.this);
                thumbListView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }
        };
        handler.post(r);
    }

    @Override
    public void onThumbnailClick(Filter filter) {
        if (getArguments().containsKey(PATHS)) {
            setUpFilterAdapter(path);
            updateBitmaps(mOriginalMaps, filter);
        } else {
            mBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//        photoImageView.setImageBitmap(filter.processFilter(bitmap));
            photoImageView.setImageBitmap(filter.processFilter(mBitmap));
            mOriginalMaps.add(mBitmap);
        }
    }

    private void updateBitmaps(ArrayList<Bitmap> Bitmaps, Filter filter) {
        ArrayList<Bitmap> filterList = new ArrayList<>();
        Bitmap mbmp;
        for (Bitmap bmp : Bitmaps) {
            mbmp = bmp.copy(Bitmap.Config.ARGB_8888, true);
            filter.processFilter(mbmp);
            filterList.add(mbmp);
        }
        Bitmaps.clear();
        Bitmaps.addAll(filterList);
        mAdapter.notifyDataSetChanged();
        filterList.clear();
    }

}
