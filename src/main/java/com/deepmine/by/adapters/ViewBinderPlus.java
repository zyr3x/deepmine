package com.deepmine.by.adapters;

import com.deepmine.by.R;
import com.deepmine.by.helpers.Constants;
import com.deepmine.by.helpers.ImageThreadLoader;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import java.net.MalformedURLException;


public class ViewBinderPlus implements SimpleAdapter.ViewBinder, Constants
{

    @Override
    public boolean setViewValue(final View view, Object data, String textRepresentation) {

        if(view instanceof ImageView && view.getId() == R.id.playBtn)
        {
            if(data.toString().equals("1"))
                view.setVisibility(View.VISIBLE);
            else
                view.setVisibility(View.GONE);

            return true;
        }
        if (view instanceof ImageView && view.getId() == R.id.coverImage) {
            try {
                IMAGE_THREAD_LOADER.loadImage(data.toString(),new ImageThreadLoader.ImageLoadedListener() {
                    @Override
                    public void imageLoaded(Bitmap imageBitmap) {
                        ((ImageView) view).setImageBitmap(imageBitmap);
                    }
                });
            } catch (MalformedURLException e) {
                Log.d(MAIN_TAG, "Error image load:" + e.getMessage());
            }
            return true;
        }
        return false;
    }
}