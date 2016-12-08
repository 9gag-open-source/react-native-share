package cl.json.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.OnSheetDismissedListener;
import com.flipboard.bottomsheet.commons.IntentPickerSheetView;

import java.util.ArrayList;
import java.util.List;

import cl.json.R;

/**
 * Created by raymond on 6/12/2016.
 */

public class RNShareWrapperActivity extends AppCompatActivity {
    private static final String TAG = "RNShareWrapperActivity";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final Intent sendIntent = (Intent) intent.getExtras().get(Intent.EXTRA_INTENT);

        setContentView(R.layout.activity_rn_share_wrapper);
        final BottomSheetLayout bottomSheet = (BottomSheetLayout) findViewById(R.id.rn_share_bottomsheet);

        IntentPickerSheetView intentPickerSheet = new IntentPickerSheetView(this, sendIntent, "Share with...", new IntentPickerSheetView.OnIntentPickedListener() {
            @Override
            public void onIntentPicked(IntentPickerSheetView.ActivityInfo activityInfo) {
                // TODO: 7/12/2016 Add url suffixes for metrics
                bottomSheet.dismissSheet();
                startActivity(activityInfo.getConcreteIntent(sendIntent));
            }
        });
        List<IntentPickerSheetView.ActivityInfo> custom = new ArrayList<>();
        custom.add(new IntentPickerSheetView.ActivityInfo(ContextCompat.getDrawable(this, R.drawable.ic_rn_share_download), "Download", this, RNShareActivity.class));
        intentPickerSheet.setMixins(custom);

        bottomSheet.showWithSheetView(intentPickerSheet);
        bottomSheet.addOnSheetDismissedListener(new OnSheetDismissedListener() {
            @Override
            public void onDismissed(BottomSheetLayout bottomSheetLayout) {
                finish();
            }
        });

    }
}
