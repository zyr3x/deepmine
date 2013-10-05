package com.deepmine.by.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Гость on 05.10.13.
 */
public class Playlist {
    public ArrayList<DataTitle> playlist;

    public ArrayList<HashMap<String, Object>> getSimpleAdapterList()
    {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        if(playlist!=null)
        for (int i = 0; i < playlist.size(); i++) {
            HashMap<String, Object> temp = new HashMap<String, Object>();
            temp.put("artist", playlist.get(i).artist);
            temp.put("track", playlist.get(i).track);
            temp.put("cover", playlist.get(i).cover);
            list.add(temp);
        }
        return list;
    }
}