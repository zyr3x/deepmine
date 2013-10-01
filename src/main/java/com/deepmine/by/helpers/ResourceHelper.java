package com.deepmine.by.helpers;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;


public class ResourceHelper {
	protected static Context mContext;
	protected static ResourceHelper mInstance = null;
	
	protected ResourceHelper() {}
	
	public void init(Context context)
	{
		mContext = context;
	}
	
	public static ResourceHelper getInstance()
	{
		if(mInstance == null);
			mInstance =  new ResourceHelper();
			
		return mInstance;		
	}
	
	public Resources getResources()
	{
		return mContext.getResources();
	}
	
	public int[] getIntArray(int id)
	{
		TypedArray arTypedArray = getResources().obtainTypedArray(id);
		int[] resIds = new int[arTypedArray.length()];
		 
		for (int i = 0; i < arTypedArray.length(); i++)
		    resIds[i] = arTypedArray.getResourceId(i, 0);
		
		arTypedArray.recycle();
		return resIds;
	}
}
