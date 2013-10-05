package com.deepmine.by.adapters;

import com.androidquery.AQuery;
import com.deepmine.by.helpers.Constants;
import com.deepmine.by.helpers.ImageThreadLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import java.net.MalformedURLException;


public class ItemImageBinder implements SimpleAdapter.ViewBinder, Constants
{
    private static int RES_ID;
    private static Context CONTEXT;
    ImageThreadLoader imageThreadLoader = new ImageThreadLoader();
    public ItemImageBinder init(Context context,int res_id )
    {
    	RES_ID = res_id;
    	CONTEXT = context;
    	return this;
    }
    
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