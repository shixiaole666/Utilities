package com.android.turntable.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.android.turntable.TodoRemoteViewsFactory;

public class TodoRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TodoRemoteViewsFactory(this.getApplicationContext());
    }
} 
