package mp.joshua.com.twitchkit.Activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.ArrayList;

import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;
import mp.joshua.com.twitchkit.DataProviders.DataPostOffice;
import mp.joshua.com.twitchkit.DataProviders.ParseSingleton;
import mp.joshua.com.twitchkit.Fragments.UserListFragment;
import mp.joshua.com.twitchkit.R;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    ParseSingleton parseSingleton;
    DataPostOffice mDataPostOffice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parseSingleton = ParseSingleton.getInstance(MainActivity.this);
        mDataPostOffice = DataPostOffice.getInstance(MainActivity.this);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, UserListFragment.newInstance())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        parseSingleton.reloadOptionsMenu(menu);
        toolBarSearch(menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        parseSingleton.reloadOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int actionId = item.getItemId();

        switch (actionId) {
            case R.id.action_settings:
                if(ParseUser.getCurrentUser() != null){
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }else {
                    parseSingleton.showLoginNotification(MainActivity.this);
                }
                break;
            case R.id.action_login:
                ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
                startActivityForResult(builder.build(), 0);
                break;
            case R.id.action_logout:
                parseSingleton.logUserOut(MainActivity.this);
                Intent loggedOutIntent = new Intent(ConstantsLibrary.ACTION_LOGGED_OUT);
                sendBroadcast(loggedOutIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toolBarSearch(Menu menu){
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setOnQueryTextListener(onQueryTextListener);
        }
    }

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            ArrayList<ParseUser> parseUserArrayList = mDataPostOffice.getParseUserArrayList();
            ArrayList<ParseUser> tempArray =  new ArrayList<>();

            if (parseUserArrayList != null){
                for (ParseUser parseUser : parseUserArrayList){
                    String username = parseUser.getUsername();
                    if (username.contains(s)){
                        Log.d("Test",username);
                        tempArray.add(parseUser);
                    }
                }
                Log.d("Test","" + tempArray.size());
                mDataPostOffice.setParseUserSearchArrayList(tempArray);
                Intent searchUserIntent = new Intent(ConstantsLibrary.ACTION_SEARCH_USERS);
                sendBroadcast(searchUserIntent);
            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };
}
