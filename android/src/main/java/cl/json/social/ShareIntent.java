package cl.json.social;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cl.json.R;
import cl.json.ShareFile;
import cl.json.activity.RNShareWrapperActivity;


/**
 * Created by disenodosbbcl on 23-07-16.
 */
public abstract class ShareIntent {

    protected final ReactApplicationContext reactContext;
    protected Intent intent;
    protected String chooserTitle;
    public ShareIntent(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
        this.chooserTitle = reactContext.getString(R.string.rn_share_title);
        this.setIntent(new Intent(android.content.Intent.ACTION_SEND));
        this.getIntent().setType("text/plain");
    }
    public void open(ReadableMap options) throws ActivityNotFoundException {
        extractIntent(options);
    }
    protected void extractIntent(ReadableMap options) {
        if (ShareIntent.hasValidKey("subject", options) ) {
            this.getIntent().putExtra(Intent.EXTRA_SUBJECT, options.getString("subject"));
        }

        if (ShareIntent.hasValidKey("message", options) && ShareIntent.hasValidKey("url", options)) {
            ShareFile fileShare = getFileShare(options);
            if(fileShare.isFile()) {
                Uri uriFile = fileShare.getURI();
                this.getIntent().setType(fileShare.getType());
                this.getIntent().putExtra(Intent.EXTRA_STREAM, uriFile);
                this.getIntent().putExtra(Intent.EXTRA_TEXT, options.getString("message"));
                this.getIntent().addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                this.getIntent().putExtra(Intent.EXTRA_TEXT, options.getString("message") + " " + options.getString("url"));
            }
        } else if (ShareIntent.hasValidKey("url", options)) {
            ShareFile fileShare = getFileShare(options);
            if(fileShare.isFile()) {
                Uri uriFile = fileShare.getURI();
                this.getIntent().setType(fileShare.getType());
                this.getIntent().putExtra(Intent.EXTRA_STREAM, uriFile);
                this.getIntent().addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                this.getIntent().putExtra(Intent.EXTRA_TEXT, options.getString("url"));
            }
        } else if (ShareIntent.hasValidKey("message", options) ) {
            this.getIntent().putExtra(Intent.EXTRA_TEXT, options.getString("message"));
        }
    }
    protected ShareFile getFileShare(ReadableMap options) {
        if (ShareIntent.hasValidKey("type", options)) {
            return new ShareFile(options.getString("url"), options.getString("type"), this.reactContext);
        } else {
            return new ShareFile(options.getString("url"), this.reactContext);
        }
    }
    protected static String urlEncode(String param) {
        try {
            return URLEncoder.encode( param , "UTF-8" );
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("URLEncoder.encode() failed for " + param);
        }
    }
    protected void openIntentChooser() throws ActivityNotFoundException {
        System.out.println(this.getIntent());
        System.out.println(this.getIntent().getExtras());
        Intent chooser = createChooserIntent(this.getIntent(), this.chooserTitle);
        chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        this.reactContext.startActivity(chooser);

//        Intent wrapperIntent = new Intent(this.reactContext, RNShareWrapperActivity.class);
//        wrapperIntent.putExtra(Intent.EXTRA_INTENT, this.getIntent());
//
//        this.reactContext.startActivity(wrapperIntent);
    }

    /**
     * Adding a custom download intent
     * @param intent
     * @param title
     * @return
     */
    private Intent createChooserIntent(Intent intent, String title) {
        Intent chooserIntent = Intent.createChooser(intent, title);

//        Intent downloadIntent = new Intent(RNShareIntent.ACTION_DOWNLOAD);
//        downloadIntent.setType("text/url");
//        downloadIntent.putExtra(Intent.EXTRA_TEXT, title);
//
//        Intent[] intentArray = { downloadIntent };
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        return chooserIntent;
    }

    protected boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    protected Intent getIntent(){
        return this.intent;
    }
    protected void setIntent(Intent intent) {
        this.intent = intent;
    }
    public static boolean hasValidKey(String key, ReadableMap options) {
        return options.hasKey(key) && !options.isNull(key);
    }
    protected void attemptToOpenTargetActivityDirectly() {
        if (isPackageInstalled(getPackage(), reactContext)) {
            if (this.reactContext.getCurrentActivity() != null) {
                this.reactContext.getCurrentActivity().startActivity(getIntent());
            } else {
                getIntent().setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.reactContext.startActivity(getIntent());
            }
        } else {
            this.openIntentChooser();
        }
    }
    protected abstract String getPackage();
    protected abstract String getDefaultWebLink();
    protected abstract String getPlayStoreLink();
}
