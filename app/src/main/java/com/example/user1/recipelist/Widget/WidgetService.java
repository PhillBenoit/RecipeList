package com.example.user1.recipelist.Widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * WidgetService is the {@link RemoteViewsService} that will return our RemoteViewsFactory
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("TESTBUG","Service");
        return new WidgetDataProvider(this, intent);
    }
}
