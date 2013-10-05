package com.deepmine.by.adapters;

import java.net.MalformedURLException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.androidquery.AQuery;
import com.deepmine.by.helpers.Constants;
import com.deepmine.by.helpers.ImageThreadLoader;

public class ImageAdapter extends BaseAdapter implements Constants {
    private Context mContext;
    ImageThreadLoader imageThreadLoader = new ImageThreadLoader();
    
    private ArrayList<String> list = new ArrayList<String>();
    public ImageAdapter(Context c,ArrayList<String> arrayList) {
        mContext = c;
    	list = arrayList;
    }
    
 		public int getCount() {
 			try
 			{
 				return this.list.size();
 			}
 			catch (Exception e) {
 				return 0;
			}
 		}

 		public Object getItem(int position) {
 			return position;
 		}

 		public long getItemId(int position) {
 			return position;
 		}

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setPadding(10, 10, 10, 10);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
       
        } else {
            imageView = (ImageView) convertView;
        }
        try {
            imageThreadLoader.loadImage(list.get(position).toString(),new ImageThreadLoader.ImageLoadedListener() {
                @Override
                public void imageLoaded(Bitmap imageBitmap) {
                    imageView.setImageBitmap(imageBitmap);
                }
            });
        } catch (MalformedURLException e) {
            Log.d(MAIN_TAG, "Error image load:" + e.getMessage());
        }
  
        return imageView;
    }

}
