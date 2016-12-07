package cl.json.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raymond on 7/12/2016.
 */

public final class ShareUtil {
    private static final String TAG = "ShareUtil";
    public static final String FACEBOOK_PACKAGE = "com.facebook.katana";
    public static final String FACEBOOK_MESSENGER_PACKAGE = "com.facebook.orca";
    public static final String TWITTER_PACKAGE = "com.twitter.android";
    public static final String GPLUS_PACKAGE = "com.google.android.apps.plus";
    public static final String GTALK_PACKAGE = "com.google.android.talk";
    public static final String WHATSAPP_PACKAGE = "com.whatsapp";
    public static final String PINTEREST_PACKAGE = "com.pinterest";
    public static final String TUMBLR_PACKAGE = "com.tumblr";
    public static final String LINE_PACKAGE = "jp.naver.line.android";
    public static final String CHROME_PACKAGE = "com.android.chrome";

    public static String packageNameToShortName(String key){
        if (key != null && key.contains(":")) {
            key = key.split(":")[0];
        }
        if (key != null && key.contains("/")) {
            key = key.split("/")[0];
        }
        String method = null;
        if (ShareUtil.FACEBOOK_PACKAGE.equals(key)) {
            method = "facebook";
        } else if (ShareUtil.FACEBOOK_MESSENGER_PACKAGE.equals(key)) {
            method = "fbm";
        } else if (ShareUtil.TWITTER_PACKAGE.equals(key)) {
            method = "twitter";
        } else if (ShareUtil.PINTEREST_PACKAGE.equals(key)) {
            method = "pinterest";
        } else if (ShareUtil.GPLUS_PACKAGE.equals(key)) {
            method = "gplus";
        } else if (ShareUtil.TUMBLR_PACKAGE.equals(key)) {
            method = "tumblr";
        } else if (ShareUtil.WHATSAPP_PACKAGE.equals(key)) {
            method = "whatsapp";
        } else if (ShareUtil.LINE_PACKAGE.equals(key)) {
            method = "line";
        } else if (ShareUtil.CHROME_PACKAGE.equals(key)) {
            method = "chrome";
        }

        return method;
    }

    public static Intent shareImage(String chooserTitle, Uri imageUri, String messageSubject, String messageBody){
        Intent shareIntent = getShareImageIntent(imageUri, messageSubject,  messageBody);
        return Intent.createChooser(shareIntent, chooserTitle);
    }
    public static Intent shareText(String chooserTitle, String messageSubject, String messageBody) {
        Intent shareIntent = getShareTextIntent(messageSubject, messageBody);
        return Intent.createChooser(shareIntent, chooserTitle);
    }

    public static Intent getOpenInChromeIntent(String url){
        Intent i = new Intent("android.intent.action.VIEW");
        i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
        //i.addCategory("android.intent.category.LAUNCHER");
        i.setData(Uri.parse(url));

        return i;
    }

    public static Intent getShareImageIntent(Uri imageUri, String messageSubject, String messageBody){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, messageSubject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, messageBody);
        shareIntent.setType("image/*");
        return shareIntent;
    }

    public static Intent getShareTextIntent(String messageSubject, String messageBody){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, messageSubject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, messageBody);
        shareIntent.setType("text/plain");
        return shareIntent;
    }

    public static Intent getSendToMailIntent(Uri imageUri, String messageSubject, String messageBody){
        Intent shareIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        if (imageUri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        }
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, messageSubject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, messageBody);
        return shareIntent;
    }

    // 2014-01-28 not working for some sms app (no text attached)
    public static Intent getSendToSmsIntent(Uri imageUri, String messageBody){
        Intent shareIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
        if (imageUri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        }

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, messageBody);
        shareIntent.putExtra(Intent.EXTRA_TEXT, messageBody);
        shareIntent.putExtra("sms_body", messageBody);
        return shareIntent;
    }


    public static Intent chooserAndSkipPackageIntent(PackageManager pm, Intent srcIntent, String title, String skipPackagePrefix) {
        List<ResolveInfo> activities = pm.queryIntentActivities(srcIntent, 0);
        List<Intent> targetIntents = new ArrayList<Intent>();

        for (ResolveInfo currentInfo : activities) {
            String packageName = currentInfo.activityInfo.packageName;
            if (packageName != null && !packageName.startsWith(skipPackagePrefix)) {
                Intent targetIntent = new Intent(srcIntent);
                targetIntent.setPackage(packageName);
                targetIntent.setClassName(packageName, currentInfo.activityInfo.name);

                targetIntents.add(targetIntent);
            }
        }
        Intent chooserIntent = Intent.createChooser(targetIntents.remove(targetIntents.size() - 1), title);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetIntents.toArray(new Parcelable[]{}));

        return chooserIntent;
    }



    // 2014-01-28 not used
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list == null) return false;
        return list.size() > 0;
    }
}
