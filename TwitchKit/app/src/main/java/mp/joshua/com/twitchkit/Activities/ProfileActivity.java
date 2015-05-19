package mp.joshua.com.twitchkit.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ui.ParseLoginBuilder;

import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;
import mp.joshua.com.twitchkit.DataProviders.ParseSingleton;
import mp.joshua.com.twitchkit.Fragments.SupportPageFragment;
import mp.joshua.com.twitchkit.R;


public class ProfileActivity extends ActionBarActivity {

    public static String parseUserID;

    private ParseSingleton mParseSingleton;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mParseSingleton = ParseSingleton.getInstance(ProfileActivity.this);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        parseUserID = getIntent().getStringExtra(ConstantsLibrary.EXTRA_PARSEUSER_ID);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, SupportPageFragment.newInstance(ConstantsLibrary.ARG_ACTIVITY_PROFILE,parseUserID))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activities,menu);
        mParseSingleton.reloadOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mParseSingleton.reloadOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_login){
            ParseLoginBuilder builder = new ParseLoginBuilder(ProfileActivity.this);
            startActivityForResult(builder.build(),0);
        }else if (id == R.id.action_logout){
            mParseSingleton.logUserOut(ProfileActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }
}