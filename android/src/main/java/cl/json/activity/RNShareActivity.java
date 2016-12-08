package cl.json.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

/**
 * Created by raymond on 6/12/2016.
 */

public class RNShareActivity extends ReactActivity {
    private static final String TAG = "RNShareActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();

        String type = getIntent().getType();
//        Log.d(TAG, "onCreate: " + getIntent().getExtras());
        Toast.makeText(this, "Downloading " + getIntent().getStringExtra(Intent.EXTRA_TEXT) + " subj=" + getIntent().getStringExtra(Intent.EXTRA_SUBJECT), Toast.LENGTH_SHORT).show();

        ReactContext context = getReactNativeHost().getReactInstanceManager().getCurrentReactContext();

        if (context != null) {
            WritableMap writableMap = Arguments.createMap();
            writableMap.putString("fileUrl", "http://www.example.com");

            context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("onDownloadTriggered", writableMap);
        }

        finish();
    }
}
