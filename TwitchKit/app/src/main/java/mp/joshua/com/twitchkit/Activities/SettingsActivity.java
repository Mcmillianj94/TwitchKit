package mp.joshua.com.twitchkit.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import mp.joshua.com.twitchkit.R;


public class SettingsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Log.d("setting","settingsAct");

    }
}
