package com.desmond.squarecamera.galleryView;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.desmond.squarecamera.CameraFragment;
import com.desmond.squarecamera.EditSavePhotoFragment;
import com.desmond.squarecamera.ImageParameters;
import com.desmond.squarecamera.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;

import static com.desmond.squarecamera.EditSavePhotoFragment.PATHS;

/**
 * This fragment shows gallery content in grid and has feature of multiselection
 */


public class GalleryFragment extends Fragment implements ItemClickListener{


    //constants
    private static final int NUM_GRID_COLUMNS = 3;
    public static final String TAG = GalleryFragment.class.getName();

    //widgets
    private RecyclerView gridView;
    private ImageView galleryImage;
    private ProgressBar mProgressBar;
    private Spinner directorySpinner;
    CustomSpinnerAdapter mSpinnerAdapter;

    //vars
    private ArrayList<String> directories;
    private String mAppend = "file:/";
    private String mSelectedImage;
    private GridImageAdapter mAdapter;
    private ArrayList<String> mMultipleImg=new ArrayList<>();
    private ArrayList<GalleryImage> imgURLs=new ArrayList<>();
    private TextView mNextView;
    private FloatingActionButton collectionBttn;
    private ArrayList<GalleryImage> imgURLs1;
    private VideoView videoView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.squarecamera__fragment_gallery, container, false);
        galleryImage = view.findViewById(R.id.galleryImageView);
        videoView= view.findViewById(R.id.videoView);
        collectionBttn=view.findViewById(R.id.multiSelectButton);
        gridView = view.findViewById(R.id.gridView);
        directorySpinner = view.findViewById(R.id.spinnerDirectory);
        mProgressBar = view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        directories = new ArrayList<>();
        ImageView shareClose = view.findViewById(R.id.ivCloseShare);
        shareClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mNextView = view.findViewById(R.id.tvNext);
        mNextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: "+mMultipleImg.size());
                Bundle data = new Bundle();
                if(mMultipleImg.size()==0){
                    mMultipleImg.add(0,imgURLs.get(0).getImgPath());
                }
                data.putStringArrayList(PATHS,mMultipleImg);
                getFragmentManager()
                        .beginTransaction()
                        .replace(
                                R.id.fragment_container,
                                EditSavePhotoFragment.newInstance(data),
                                EditSavePhotoFragment.TAG)
                        .addToBackStack(null)
                        .commit();
            }
        });
        collectionBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.selectAll=true;        //Update adapter variable for selecting multiple imgs
            }
        });
        setupGridView();
        init();
        return view;
    }


    private void init() {
        final ArrayList<Albums> directoryNames = new ArrayList<>();
        ArrayList<Albums> imageFolders = new ImageFolder().getAllShownImagesPath(getActivity());
        for (int i = 0; i < imageFolders.size(); i++) {
            if(imageFolders.get(i).getImgCount()>0)     //to remove folders with zero images
                        directoryNames.add(imageFolders.get(i));
        }

        mSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), R.layout.squarecamera__custom_spinner_item,directoryNames);
        directorySpinner.setAdapter(mSpinnerAdapter);
        directorySpinner.setSelection(0);
        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                imgURLs=new ArrayList<>();
                fetchImagesList(directoryNames.get(position));
                setupGridView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setupGridView() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),NUM_GRID_COLUMNS);
        gridView.setLayoutManager(mLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.squarecamera__item_offset);
        gridView.addItemDecoration(itemDecoration);
        mAdapter = new GridImageAdapter(this, R.layout.squarecamera__image_gallery_item, mAppend, imgURLs);
        gridView.setAdapter(mAdapter);
        if (imgURLs.size() > 0)
            try {
                mSelectedImage = imgURLs.get(0).getImgPath();
                setImage(mSelectedImage, mAppend);
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e(TAG, "setupGridView: ArrayIndexOutOfBoundsException: " + e.getMessage());
            }
    }

    /**
     * To fetch files with extensions .jpg,.mp4,.3gpp,.3gp,.jpeg,.png,.bmp
     * @param selectedDirectory
     */

    private void fetchImagesList(Albums selectedDirectory) {
        for (String path: FileSearch.getFilePaths(selectedDirectory)) {
            imgURLs.add(new GalleryImage(path));
        }
    }


    private void setImage(String imgURL, String append) {
        Log.d(TAG, "setImage: setting image");
        Log.e(TAG, "" + append);
        Log.e(TAG, "" + append);
        Picasso.get().
                load(Uri.fromFile(new File(imgURL)))
                .into(galleryImage);
    }



    @Override
    public void onItemClick(int position, String imgPath) {
        if(imgPath.endsWith(".3gp")||imgPath.endsWith(".3gpp")||imgPath.endsWith(".mp4")){
            galleryImage.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            videoView.setMediaController(new MediaController(getActivity()));
            videoView.setVideoURI(Uri.fromFile(new File(imgPath)));
            videoView.requestFocus();
            videoView.start();
        }else{
            videoView.setVisibility(View.GONE);
            galleryImage.setVisibility(View.VISIBLE);
        }

        mSelectedImage = imgPath;
        setImage(imgPath, mAppend);
    }

    @Override
    public void onLongItemClick(int position, String imgPath) {
        if(imgURLs.get(position).isSelected){
            if(!mMultipleImg.contains(imgPath))
                    mMultipleImg.add(imgPath);
        }else{
            if(mMultipleImg.contains(imgPath)){
                mMultipleImg.remove(imgPath);
                imgURLs1=new ArrayList<>(imgURLs);
                imgURLs.clear();
                imgURLs.addAll(imgURLs1);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMultipleImg.clear();
    }
}

