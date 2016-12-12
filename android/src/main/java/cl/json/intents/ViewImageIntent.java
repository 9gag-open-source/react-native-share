package cl.json.intents;

import android.content.Intent;
import android.net.Uri;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;

import cl.json.social.ShareIntent;

/**
 * Created by raymond on 9/12/2016.
 */

public class ViewImageIntent extends BaseIntentType {

    public ViewImageIntent(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    public void handleIntent(ReadableMap options) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (ShareIntent.hasValidKey("url", options) && ShareIntent.hasValidKey("type", options)) {
            intent.setDataAndType(Uri.parse(options.getString("url")), options.getString("type"));
        }

        Intent chooserIntent = Intent.createChooser(intent, "View");
        reactContext.startActivity(chooserIntent);
    }
}
