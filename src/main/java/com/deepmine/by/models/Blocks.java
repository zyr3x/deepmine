package com.deepmine.by.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Blocks {

    public ArrayList<BlockItem> data;

    public ArrayList<HashMap<String, Object>> getList() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        if(data!=null)
        for (BlockItem item : data ) {
            if(item!=null)
            {
                HashMap<String, Object> temp = new HashMap<String, Object>();
                temp.put("bg", item.bg);
                temp.put("title", item.title);
                temp.put("url",item.url);
                list.add(temp);
            }
        }

        return list;
    }

}
