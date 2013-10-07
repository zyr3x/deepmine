package com.deepmine.by.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Гость on 05.10.13.
 */
public class Playlist {
    public ArrayList<DataTitle> playlist;

    public DataTitle getItem(int i)
    {
        return playlist.get(i);
    }

    public ArrayList<HashMap<String, Object>> getSimpleAdapterList()
    {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        if(playlist!=null)
        for (DataTitle item : playlist ) {
            if(item!=null)
            {
                HashMap<String, Object> temp = new HashMap<String, Object>();
                temp.put("artist", item.artist);
                temp.put("track", item.track);
                temp.put("cover", item.cover);
                temp.put("title", item.title);
                list.add(temp);
            }
        }
        return list;
    }
}