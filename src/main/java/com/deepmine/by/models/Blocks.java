package com.deepmine.by.models;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Blocks {

    protected ArrayList<BlockItem> _ArrayCatalog = new ArrayList<BlockItem>();


    public Blocks()
    {

    }

    public Blocks(JSONArray objects)
    {
        ArrayList<BlockItem> arrayCatalog = new ArrayList<BlockItem>();
        if (objects.length() > 0)
            for (int i = 0; i < objects.length(); i++)
                try {
                    arrayCatalog.add(i, parse(objects.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        _ArrayCatalog = arrayCatalog;
    }

    public int size() {
        return _ArrayCatalog.size();
    }

    public BlockItem get(int index)
    {
        return _ArrayCatalog.get(index);
    }

    public BlockItem parse(JSONObject object) throws JSONException {
        BlockItem model = new BlockItem();
        if (object != null)
        {
            model.setId(getValue(object,"id"));
            model.setImage(getValue(object,"bg"));
            model.setUrl(getValue(object,"url"));
            model.setTitle(getValue(object,"title"));
        }
        return model;
    }

    private String getValue(JSONObject object, String name)
    {
        try {
            return object.getString(name);
        } catch (JSONException e) {
            return "";
        }
    }

    private void  parseJSONArray(JSONArray objects)
    {
        ArrayList<BlockItem> arrayCatalog = new ArrayList<BlockItem>();
        if (objects.length() > 0)
            for (int i = 0; i < objects.length(); i++)
                try {
                    arrayCatalog.add(i, parse(objects.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        _ArrayCatalog = arrayCatalog;
    }

    public ArrayList<BlockItem> getArray(JSONArray objects){
        parseJSONArray(objects);
        return _ArrayCatalog;
    }

    public ArrayList<HashMap<String, Object>> getList() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

        for (int i = 0; i < _ArrayCatalog.size(); i++) {
            HashMap<String, Object> temp = new HashMap<String, Object>();
            temp.put("ess_id", _ArrayCatalog.get(i).getId());
            temp.put("bg", _ArrayCatalog.get(i).getImage());
            temp.put("title", _ArrayCatalog.get(i).getTitle());
            temp.put("url", _ArrayCatalog.get(i).getUrl());
            list.add(temp);
        }

        return list;
    }

    public ArrayList<HashMap<String, Object>> getListItem(int i)
    {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> temp = new HashMap<String, Object>();
        temp.put("ess_id", _ArrayCatalog.get(i).getId());
        temp.put("bg", _ArrayCatalog.get(i).image);
        temp.put("title", _ArrayCatalog.get(i).title);
        temp.put("url", _ArrayCatalog.get(i).url);
        list.add(temp);
        return list;
    }

}
