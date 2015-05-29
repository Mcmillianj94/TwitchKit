package mp.joshua.com.twitchkit.Fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.parse.ParseUser;

import java.util.ArrayList;

import mp.joshua.com.twitchkit.Activities.FormsActivity;
import mp.joshua.com.twitchkit.Adapters.UserlistAdapter;
import mp.joshua.com.twitchkit.CoverView;
import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;
import mp.joshua.com.twitchkit.DataProviders.DataPostOffice;
import mp.joshua.com.twitchkit.DataProviders.ParseSingleton;
import mp.joshua.com.twitchkit.R;

public class UserListFragment extends Fragment {

    ParseSingleton mParseSingleton;
    DataPostOffice mDataPostOffice;
    ExpandableListView expandList;
    RelativeLayout viewHolder;
    Button streamerToolButton;
    CoverView mCoverView;

    boolean hasPaused = false;
    ArrayList userListArray;
    ArrayList<String> groupItem = new ArrayList<String>();
    ArrayList<Object> childItem = new ArrayList<Object>();

    /*The clickListeners for child views are
    * defined in the userListAdapter in the
    * get child view method */

    public static UserListFragment newInstance(){
        UserListFragment fragment = new UserListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mParseSingleton = ParseSingleton.getInstance(getActivity());
        mDataPostOffice = DataPostOffice.getInstance(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantsLibrary.ACTION_GET_USERS);
        intentFilter.addAction(ConstantsLibrary.ACTION_SEARCH_USERS);
        intentFilter.addAction(ConstantsLibrary.ACTION_LOGGED_OUT);
        getActivity().registerReceiver(mMessageReceiver,intentFilter);
        if (hasPaused){
            setUpFragment();
            hasPaused = false;
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        getActivity().unregisterReceiver(mMessageReceiver);
        hasPaused = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCoverView.removeCoverView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_expandlist,container,false);
        viewHolder = (RelativeLayout)v.findViewById(R.id.relative_userlistFrag_viewHolder);
        expandList = (ExpandableListView)v.findViewById(R.id.expand_list);
        expandList.setDividerHeight(0);
        expandList.setGroupIndicator(Drawable.createFromPath(String.valueOf(R.drawable.ic_launcher)));
        expandList.setBackgroundColor(getResources().getColor(R.color.dirtyWhite));
        expandList.setClickable(true);

        streamerToolButton = (Button)v.findViewById(R.id.button_userList_streamerTools);
        streamerToolButton.setOnClickListener(streamerToolsClickListener);

        mCoverView = new CoverView(getActivity(),viewHolder);
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.action_refresh){
            mCoverView.removeCoverView();
            setUpFragment();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpFragment();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();

            switch (intentAction){
                case ConstantsLibrary.ACTION_GET_USERS:
                    userListArray = mDataPostOffice.getParseUserArrayList();
                    break;
                case ConstantsLibrary.ACTION_SEARCH_USERS:
                    userListArray = mDataPostOffice.getParseUserSearchArrayList();
                    break;

                case ConstantsLibrary.ACTION_LOGGED_OUT:
                    setUpFragment();
                    break;
            }

            filterUsers();
        }
    };
    private void setUpFragment(){

        mCoverView.createLoadingCover();

        //start receiving all users
        mParseSingleton.getAllStreamerList();
    }

    private void filterUsers(){
        ArrayList followingList = null;
        if(ParseUser.getCurrentUser() != null) {
            //Get current users following list
            followingList = (ArrayList) ParseUser.getCurrentUser().getList("following");
        }

        //Create temp array for following and all remaining users
        ArrayList<Object> tempFollowArray = new ArrayList<Object>();
        ArrayList<Object> tempAllUsers = new ArrayList<Object>();

        for (int i = 0; i < userListArray.size(); i++){
            String userID = ((ParseUser)userListArray.get(i)).getObjectId();

            if(followingList != null && followingList.contains(userID)) {
                tempFollowArray.add(userListArray.get(i));
            }else{
                tempAllUsers.add(userListArray.get(i));
            }
        }

        groupItem.clear();
        childItem.clear();

        //if user is not signed in remove following group and children
        if (ParseUser.getCurrentUser() != null){
            groupItem.add("Following");
            childItem.add(tempFollowArray);
        }
        groupItem.add("All Users");
        childItem.add(tempAllUsers);

        UserlistAdapter mNewAdapter = new UserlistAdapter(getActivity(),groupItem, childItem);
        mNewAdapter.setInflater(
                (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE),getActivity());

        expandList.setAdapter(mNewAdapter);
        mCoverView.removeCoverView();
    }

    View.OnClickListener streamerToolsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (ParseUser.getCurrentUser() != null){
                Intent intent = new Intent(getActivity(), FormsActivity.class);
                startActivity(intent);
            }else {
                mParseSingleton.showLoginNotification(getActivity());
            }
        }
    };
}
