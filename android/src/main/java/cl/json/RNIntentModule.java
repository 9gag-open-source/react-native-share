package cl.json;

import android.content.ActivityNotFoundException;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;

import cl.json.intents.BaseIntentType;
import cl.json.intents.ViewImageIntent;

/**
 * Created by raymond on 9/12/2016.
 */
@ReactModule(name = RNIntentModule.CLASS_NAME)
public class RNIntentModule extends ReactContextBaseJavaModule {
    public static final String CLASS_NAME = "RNIntentAndroid";

    private static final String DEFAULT_INTENT_KEY = "default";
    private static final String TAG = "RNIntentAndroid";

    private final ArrayMap<String, BaseIntentType> supportedIntents = new ArrayMap<>();

    public RNIntentModule(ReactApplicationContext reactContext) {
        super(reactContext);
        supportedIntents.put(DEFAULT_INTENT_KEY, new ViewImageIntent(reactContext));
        supportedIntents.put("image/*", new ViewImageIntent(reactContext));
    }

    @Override
    public String getName() {
        return CLASS_NAME;
    }

    @ReactMethod
    public void handleIntent(ReadableMap options, @Nullable Callback failureCallback, @Nullable Callback successCallback) {
        try {
            String intentKeyType = DEFAULT_INTENT_KEY;
            if (options.hasKey("type")) {
                intentKeyType = options.getString("type");
            };

            BaseIntentType baseIntent = supportedIntents.get(intentKeyType);
            baseIntent.handleIntent(options);
            successCallback.invoke("OK");
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "handleIntent: ", e);
            failureCallback.invoke("ERROR");
        }
    }
}
