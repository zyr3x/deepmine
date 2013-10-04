package com.deepmine.by.helpers;

import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.Transformer;
import com.google.gson.Gson;
/**
 * Created by zyr3x on 04.10.13.
 */
public class GSONTransformer implements Transformer {
    public <T> T transform(String url, Class<T> type, String encoding, byte[] data, AjaxStatus status) {
        Gson g = new Gson();
        return g.fromJson(new String(data), type);
    }
}
