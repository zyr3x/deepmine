package com.deepmine.by.adapters;

import com.deepmine.by.helpers.Constants;
import com.deepmine.by.helpers.ImageThreadLoader;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import java.net.MalformedURLException;


public class ItemImageBinder implements SimpleAdapter.ViewBinder, Constants
{
    ImageThreadLoader imageThreadLoader = new ImageThreadLoader();
    @Override
    public boolean setViewValue(final View view, Object data, String textRepresentation) {
        if (view instanceof ImageView) {
            try {
                imageThreadLoader.loadImage(data.toString(),new ImageThreadLoader.ImageLoadedListener() {
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