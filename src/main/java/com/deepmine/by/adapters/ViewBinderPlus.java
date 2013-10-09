package com.deepmine.by.adapters;

import com.deepmine.by.R;
import com.deepmine.by.helpers.Constants;
import com.deepmine.by.helpers.ImageThreadLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import java.net.MalformedURLException;


public class ViewBinderPlus implements SimpleAdapter.ViewBinder, Constants
{
    ImageThreadLoader imageThreadLoader = ImageThreadLoader.getOnDiskInstance();

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
        if (view instanceof ImageView ) {

            ((ImageView) view).setImageDrawable(imageThreadLoader.loadImage(data.toString(), new ImageThreadLoader.ImageLoadedListener()
            {
                    @Override
                    public void imageLoaded(Drawable imageBitmap) {
                        ((ImageView) view).setImageDrawable(imageBitmap);
                    }
                }));

            return true;
        }
        return false;
    }
}