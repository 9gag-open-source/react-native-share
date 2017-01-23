package cl.json.social;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;

/**
 * Created by raymond on 20/1/2017.
 */

public class SmsShare extends SingleShareIntent {
    public SmsShare(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public void open(ReadableMap options) throws ActivityNotFoundException {
        try {
            extractIntent(options);
            this.getIntent()
                    .setAction(Intent.ACTION_SENDTO)
                    .setData(Uri.parse("smsto:"))
            ;

            if (ShareIntent.hasValidKey("message", options) && ShareIntent.hasValidKey("url", options)) {
                this.getIntent().putExtra("sms_body", options.getString("message") + " " + options.getString("url"));
                this.reactContext.startActivity(this.getIntent());
            }

        } catch (Exception exception) {
            Log.e("SmsShare", "open: ", exception);
        }
    }

    @Override
    protected String getPackage() {
        return null;
    }

    @Override
    protected String getDefaultWebLink() {
        return null;
    }

    @Override
    protected String getPlayStoreLink() {
        return null;
    }
}
