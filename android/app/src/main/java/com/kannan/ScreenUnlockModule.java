package com.kannan;


import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;


/**
 * Created by #kannanpvm007 on  31/05/23.
 */
public class ScreenUnlockModule  extends ReactContextBaseJavaModule implements  ActivityEventListener {

    private static final String MODULE_NAME = "ScreenUnlockModule";
    private final ReactApplicationContext reactContext;
    private final int REQUEST_CONFIRM_CREDENTIALS = 123;
    private Callback unlockCallback;
private String TAG="ScreenUnlockModule";

    private final ActivityEventListener activityEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            if (requestCode == REQUEST_CONFIRM_CREDENTIALS) {
                if (resultCode == android.app.Activity.RESULT_OK) {
                    System.out.println( "onActivityResult:-------> unlocked");
                    System.out.println("onActivityResult:-------> unlocked");
                    unlockCallback.invoke(true);
                } else {
                    System.out.println( "onActivityResult:-------> fail to unlock");

                    unlockCallback.invoke(false);
                }
            }
        }
    };
    public ScreenUnlockModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
//        this.reactContext.addActivityEventListener(activityEventListener);
        reactContext.addActivityEventListener(this);
        System.out.println( "ScreenUnlockModule:------------------------------> ");
    }

    @NonNull
    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void unlockApp(Callback callback) {
        KeyguardManager keyguardManager = (KeyguardManager) reactContext.getSystemService(Context.KEYGUARD_SERVICE);

        if (keyguardManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (keyguardManager.isDeviceSecure()) {
                    unlockCallback = callback;
                    Intent intent = keyguardManager.createConfirmDeviceCredentialIntent("hello", "un lock react-native");
                    if (intent != null) {
                        reactContext.startActivityForResult(intent, REQUEST_CONFIRM_CREDENTIALS, null);

                    } else {
                        callback.invoke(false);
                    }
                } else {
                    callback.invoke(false);
                }
            } else {
                if (keyguardManager.isKeyguardSecure()) {
                    callback.invoke(true);
                } else {
                    callback.invoke(false);
                }
            }
        } else {
            callback.invoke(false);
        }
    }


    @Override
    public void onActivityResult(Activity activity, int i, int i1, @Nullable Intent intent) {
        System.out.println("onActivityResult------------------------------->i"+i);
        System.out.println("onActivityResult------------------------------->i1"+i1);
        if (REQUEST_CONFIRM_CREDENTIALS == i) {
            System.out.println("onActivityResult---------->");
            boolean isUnlocked = i1 == getCurrentActivity().RESULT_OK;
            System.out.println("onActivityResult---------->"+isUnlocked);
            if (unlockCallback != null) {
                unlockCallback.invoke(isUnlocked);
                unlockCallback = null;
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}
