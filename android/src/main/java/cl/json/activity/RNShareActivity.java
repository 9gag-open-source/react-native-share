package cl.json.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by raymond on 6/12/2016.
 */

public class RNShareActivity extends AppCompatActivity {
    private static final String TAG = "RNShareActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        Log.d(TAG, "onCreate: " + getIntent().getExtras());
        Toast.makeText(this, "Downloading " + getIntent().getStringExtra(Intent.EXTRA_TEXT) + " subj=" + getIntent().getStringExtra(Intent.EXTRA_SUBJECT), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode + ", data=" + data);
    }
}
