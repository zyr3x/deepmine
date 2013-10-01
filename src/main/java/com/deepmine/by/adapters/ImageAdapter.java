package com.deepmine.by.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.androidquery.AQuery;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private AQuery aq;
    
    private ArrayList<String> list = new ArrayList<String>();
    public ImageAdapter(Context c,ArrayList<String> arrayList) {
        mContext = c;
    	aq = new AQuery(mContext);
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
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setPadding(10, 10, 10, 10);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
       
        } else {
            imageView = (ImageView) convertView;
        }
    	aq.id(imageView).image(list.get(position).toString(),false, true);
  
        return imageView;
    }

    // references to our images
  
}
