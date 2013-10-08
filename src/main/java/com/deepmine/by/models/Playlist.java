package com.deepmine.by.models;

import android.util.Log;

import com.deepmine.by.helpers.Constants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Гость on 05.10.13.
 */
public class Playlist implements Constants {
    public ArrayList<DataTitle> playlist;
    public DataTitle activeDataTitle;
    private int active_id = 0;
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
                temp.put("active", item.active);
                list.add(temp);
            }
        }
        return list;
    }

    public DataTitle getActive()
    {
        if (activeDataTitle == null)
            return  new DataTitle();
        else
            return activeDataTitle;
    }

    public int getActiveId()
    {
        return active_id;
    }

    public void setActive(DataTitle dataTitle)
    {
        activeDataTitle = dataTitle;

        for (int i=0;  i< playlist.size(); i++ )
        {
            if(playlist.get(i).title.equals(dataTitle.title))
            {
                playlist.get(i).active = "1";
                active_id = 1;
            }
            else
                playlist.get(i).active = "0";
        }
    }
}