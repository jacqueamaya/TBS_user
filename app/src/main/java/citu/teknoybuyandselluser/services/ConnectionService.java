package citu.teknoybuyandselluser.services;

import android.app.IntentService;
import android.content.Intent;

/**
 ** Created by jack on 6/02/16.
 */
public abstract class ConnectionService extends IntentService {

    public ConnectionService(String name) {
        super(name);
    }

    protected void notifySuccess(String className, String responseBody){
        Intent intent = new Intent(className);
        intent.putExtra("result", 1);
        intent.putExtra("response", "" + responseBody);
        sendBroadcast(intent);
    }

    protected void notifyFailure(String className, String responseBody){
        Intent intent = new Intent(className);
        intent.putExtra("result", -1);
        intent.putExtra("response", "" + responseBody);
        sendBroadcast(intent);
    }
}
