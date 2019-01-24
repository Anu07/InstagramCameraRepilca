package com.desmond.squarecamera;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.desmond.squarecamera.galleryView.FilePaths;
import com.desmond.squarecamera.galleryView.GalleryFragment;


public class CameraActivity extends AppCompatActivity {

    public static final String TAG = CameraActivity.class.getSimpleName();

    TextView mGalleryTab,mPhotoTab,mVideoTab;
    private LinearLayout mBottomTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.squarecamera__CameraFullScreenTheme);
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.squarecamera__activity_camera);
        mBottomTab=findViewById(R.id.bottomOptionsview);
        mPhotoTab=findViewById(R.id.photoTab);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, CameraFragment.newInstance(), CameraFragment.TAG)
                    .commit();
        }

        mGalleryTab=findViewById(R.id.galleryTab);
        mGalleryTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GalleryFragment galleryFrag= new GalleryFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,galleryFrag, GalleryFragment.TAG)
                        .commit();
            }
        });
        mPhotoTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, CameraFragment.newInstance(), CameraFragment.TAG)
                        .commit();
            }
        });
    }


    public void returnPhotoUri(Uri uri) {
        Intent data = new Intent();
        data.setData(uri);

        if (getParent() == null) {
            setResult(RESULT_OK, data);
        } else {
            getParent().setResult(RESULT_OK, data);
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onCancel(View view) {
        getSupportFragmentManager().popBackStack();
    }
}
