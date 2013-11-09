package com.deepmine.by.models;
import java.util.ArrayList;

public class Events {

    public ArrayList<Event> data;

    public Event getEvent(int i)
    {
        if(data.size() > i)
            return data.get(i);
        return null;
    }
}
