package cl.json.intents;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;

/**
 * Created by raymond on 9/12/2016.
 */

public abstract class BaseIntentType {
    protected final ReactApplicationContext reactContext;

    public BaseIntentType(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
    }

    abstract public void handleIntent(ReadableMap options);
}
