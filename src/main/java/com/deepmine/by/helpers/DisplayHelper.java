package com.deepmine.by.helpers;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DisplayHelper {

	protected static Context mContext;
	protected static DisplayMetrics mMetrics = new DisplayMetrics();
	protected static DisplayHelper mInstance = null;
	
	protected DisplayHelper() {}
	
	public void init(Context context)
	{
		mContext = context;
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
	}
	
	
	public static DisplayHelper getInstance()
	{
		if(mInstance == null);
			mInstance =  new DisplayHelper();
			
		return mInstance;		
	}
	
	public DisplayMetrics getMetrics(){
		return mMetrics;
	}
	
	
}
