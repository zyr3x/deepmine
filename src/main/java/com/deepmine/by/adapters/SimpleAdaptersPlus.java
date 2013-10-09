package com.deepmine.by.adapters;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleAdaptersPlus extends SimpleAdapter{

    ArrayList<HashMap<String, Object>> data;

    public SimpleAdaptersPlus(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.data = ( ArrayList<HashMap<String, Object>> )data;
    }

    public void updateData(List<? extends Map<String, ?>> data)
    {
        this.data = ( ArrayList<HashMap<String, Object>> )data;
        this.notifyDataSetChanged();
    }


}
