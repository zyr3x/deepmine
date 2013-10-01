package com.deepmine.by.adapters;

import java.util.ArrayList;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;

public class MultiSimpleAdapters extends BaseAdapter{

	protected ArrayList<SimpleAdapter> adapters =new ArrayList<SimpleAdapter>();
	protected ArrayList<View> views =new ArrayList<View>();
	
	public void add(SimpleAdapter adapter)
	{
		adapters.add(adapter);
	}
	
	public void addAll(ArrayList<SimpleAdapter> adapterList)
	{
		for (SimpleAdapter simpleAdapter : adapterList) {
			adapters.add(simpleAdapter);
		}
		
	}
	
	@Override
	public int getCount() {
		return adapters.size();
	}

	@Override
	public Object getItem(int index) {
		return adapters.get(index).getItem(0);
	}

	@Override
	    public long getItemId(int position) {
	        return position;
	    }

	@Override
	public View getView(int index, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(view == null || index >= (views.size()-1) || adapters.size() != 0)
		{
			view = adapters.get(index).getView(0, null, parent);
			views.add(index,view);
		}
		else
			view = views.get(index);
		
		return view;
	}

}
