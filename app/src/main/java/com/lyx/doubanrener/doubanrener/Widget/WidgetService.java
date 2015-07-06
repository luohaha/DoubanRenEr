package com.lyx.doubanrener.doubanrener.Widget;


import android.content.Intent;

import android.widget.RemoteViewsService;



/**
 * Created by root on 15-7-4.
 */
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyWidgetFactory(this.getApplicationContext(), intent);
    }



}
