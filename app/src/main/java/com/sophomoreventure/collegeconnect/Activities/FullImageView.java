package com.sophomoreventure.collegeconnect.Activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;
import com.sophomoreventure.collegeconnect.R;

/**
 * Created by Vikas Kumar on 12-02-2016.
 */

public class FullImageView extends AppCompatActivity {

    String UrlImage;
    private VolleySingleton mVolleySingleton;
    private ImageLoader mImageLoader;
    private RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.full_image_view);
        ImageView imageView = (ImageView) findViewById(R.id.fullImageView);

        UrlImage = getIntent().getStringExtra("Image");
        mVolleySingleton = VolleySingleton.getInstance(this);
        requestQueue = mVolleySingleton.getRequestQueue();
        mImageLoader = mVolleySingleton.getImageLoader();
        if(false){
            loadImages(UrlImage,imageView);
            Log.i("expose", "image Url" + UrlImage);
        }
    }

    private void loadImages(String urlThumbnail,final ImageView holder) {
        if (true) {
            mImageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.setImageBitmap(response.getBitmap());
                }
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
