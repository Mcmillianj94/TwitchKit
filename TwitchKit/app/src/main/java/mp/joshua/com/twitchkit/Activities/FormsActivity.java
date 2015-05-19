package mp.joshua.com.twitchkit.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.ArrayList;
import java.util.Locale;

import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;
import mp.joshua.com.twitchkit.DataProviders.DataPostOffice;
import mp.joshua.com.twitchkit.DataProviders.ParseSingleton;
import mp.joshua.com.twitchkit.Fragments.MainGiveawayFragment;
import mp.joshua.com.twitchkit.Fragments.MainPollFragment;
import mp.joshua.com.twitchkit.Fragments.SupportPageFragment;
import mp.joshua.com.twitchkit.R;

public class FormsActivity extends ActionBarActivity {

    ParseSingleton parseSingleton;
    private static DataPostOffice mDataPostOffice;
    SectionsPagerAdapter mSectionsPagerAdapter;
    public ViewPager mViewPager;

    ArrayList<Fragment> formActivityFragList = null;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        parseSingleton = ParseSingleton.getInstance(FormsActivity.this);
        mDataPostOffice = DataPostOffice.getInstance(FormsActivity.this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);
        tabs.setIndicatorHeight(10);
        tabs.setIndicatorColor(getResources().getColor(R.color.dirtyWhite));
        tabs.setScrollOffset(20);
        tabs.setShouldExpand(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forms, menu);
        parseSingleton.reloadOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        parseSingleton.reloadOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_logout){
            parseSingleton.logUserOut(FormsActivity.this);

        }else if (id == R.id.action_login){
            ParseLoginBuilder builder = new ParseLoginBuilder(FormsActivity.this);
            startActivityForResult(builder.build(),0);
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return MainGiveawayFragment.newInstance(ConstantsLibrary.ARG_ACTIVITY_FORMS);
                case 1:
                    return MainPollFragment.newInstance(ConstantsLibrary.ARG_ACTIVITY_FORMS);
                case 2:
                    return SupportPageFragment.newInstance(ConstantsLibrary.ARG_ACTIVITY_FORMS, ParseUser.getCurrentUser().getObjectId());
            }
            return new Fragment();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }
}
