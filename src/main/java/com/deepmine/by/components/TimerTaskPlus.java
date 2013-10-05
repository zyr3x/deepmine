package com.deepmine.by.components;

import android.os.Handler;

import java.util.TimerTask;

/**
 * Created by Гость on 05.10.13.
 */
public  abstract class TimerTaskPlus extends TimerTask {
    public final Handler handler = new Handler();
}
