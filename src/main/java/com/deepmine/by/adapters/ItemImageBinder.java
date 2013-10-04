package com.deepmine.by.adapters;

import com.androidquery.AQuery;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;




public class ItemImageBinder implements SimpleAdapter.ViewBinder
{
    private static int RES_ID;
    private static Context CONTEXT;
    private AQuery aq;
    public ItemImageBinder init(Context context,int res_id )
    {
    	RES_ID = res_id;
    	CONTEXT = context;
    	aq = new AQuery(CONTEXT);
    	return this;
    }
    
    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation) {
        if (view instanceof ImageView) {
        	aq.id(view).image(data.toString(),false, true, 0, RES_ID);
            return true;
        }
        return false;
    }
}