package mp.joshua.com.twitchkit.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.loopj.android.image.SmartImageView;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Locale;

import mp.joshua.com.twitchkit.CoverView;
import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;
import mp.joshua.com.twitchkit.DataProviders.DataPostOffice;
import mp.joshua.com.twitchkit.DataProviders.ParseSingleton;
import mp.joshua.com.twitchkit.R;

public class ProfileFragment extends Fragment {

    ParseSingleton mParseSigleton;
    DataPostOffice mDataPostOffice;
    CoverView mCoverView;
    ParseUser profileOwner;

    RelativeLayout viewHolder;
    SmartImageView profilePicture;
    TextView usernameTextView;
    TextView bioTextView;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    private String ownerID;

    public static ProfileFragment newInstance(String id){
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ConstantsLibrary.ARG_PARSEUSER_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mParseSigleton = ParseSingleton.getInstance(getActivity());
        mDataPostOffice = DataPostOffice.getInstance(getActivity());
        ownerID = getArguments().getString(ConstantsLibrary.ARG_PARSEUSER_ID);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantsLibrary.ACTION_PROFILE_OWNER_RETRIEVED);
        intentFilter.addAction(ConstantsLibrary.ACTION_PROFILE_OWNER_FOLLOWED);
        getActivity().registerReceiver(profileCallbackReciever,intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(profileCallbackReciever);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCoverView.removeCoverView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile,container,false);

        viewHolder = (RelativeLayout)v.findViewById(R.id.relative_profileFrag_viewHolder);
        profilePicture = (SmartImageView)v.findViewById(R.id.smartImage_profileFrag_profilePic);
        usernameTextView = (TextView)v.findViewById(R.id.textView_profileFrag_username);
        bioTextView = (TextView)v.findViewById(R.id.textView_profileFrag_bio);
        mViewPager = (ViewPager) v.findViewById(R.id.pager);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);
        tabs.setIndicatorHeight(10);
        tabs.setIndicatorColor(getResources().getColor(R.color.dirtyWhite));
        tabs.setScrollOffset(20);
        tabs.setShouldExpand(true);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (ParseUser.getCurrentUser() != null){
            inflater.inflate(R.menu.menu_frag_profile,menu);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (profileOwner != null){
            populateToolBar(menu);
            Log.d("Test", "this junk work");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_not_followed){
            //Add profileOwner to current users following list
            mParseSigleton.followUser(ParseUser.getCurrentUser(),profileOwner.getObjectId());
        }else if (itemId == R.id.action_followed){
            mParseSigleton.unFollowUser(ParseUser.getCurrentUser(),profileOwner.getObjectId());
        }

        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCoverView = new CoverView(getActivity(),viewHolder);
        setupFragment();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return TwitterFragment.newInstance();
                case 1:
                    return MainGiveawayFragment.newInstance(ConstantsLibrary.ARG_ACTIVITY_PROFILE);
                case 2:
                    return MainPollFragment.newInstance(ConstantsLibrary.ARG_ACTIVITY_PROFILE);
            }
            return null;
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
                    return getString(R.string.profile_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.profile_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.profile_section3).toUpperCase(l);
            }
            return null;
        }
    }

    BroadcastReceiver profileCallbackReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionId = intent.getAction();

            switch (actionId){
                case ConstantsLibrary.ACTION_PROFILE_OWNER_RETRIEVED:
                    profileOwner = mDataPostOffice.getProfileOwner();
                    populateUI();
                    break;
                case ConstantsLibrary.ACTION_PROFILE_OWNER_FOLLOWED:
                    Toast.makeText(getActivity(),"Followed that mutha",Toast.LENGTH_SHORT).show();
                    //TODO: Update toolbar from follow to unfollow
                    break;
                case ConstantsLibrary.ACTION_PROFILE_QUERY_ERROR:
                    mCoverView.removeCoverView();
                    mCoverView.createInstructionCover(ConstantsLibrary.CONST_QUERY_ERROR_MESSAGE,null);
                    break;
            }
        }
    };

    public void setupFragment(){
        //Display loading coverView
        mCoverView.createLoadingCover();

        //Start data retrieval
        mParseSigleton.getUser(ownerID);
    }

    public void populateUI(){

        getActivity().invalidateOptionsMenu();

        //Get profile image - Set smartImageViewUrl to image url
        ParseFile image = profileOwner.getParseFile("profileImage");
        if (image != null){
            profilePicture.setImageUrl(image.getUrl());
        }

        //Set usernameTexView
        usernameTextView.setText(profileOwner.getUsername());

        //Set bio
        String bio = profileOwner.getString("bio");
        if (bio != null){
            bioTextView.setText(bio);
        }

        //Remove coverView once UI elements are successfully populated
        mCoverView.removeCoverView();
    }

    private void populateToolBar(Menu menu){
        MenuItem followItem = menu.findItem(R.id.action_not_followed);
        MenuItem unFollowItem = menu.findItem(R.id.action_followed);
        ArrayList followingList = null;
        if(ParseUser.getCurrentUser() != null){
            followingList = (ArrayList)ParseUser.getCurrentUser().getList("following");
            if (followingList != null){
                if (followingList.contains(profileOwner.getObjectId())){
                    followItem.setVisible(false);
                    unFollowItem.setVisible(true);
                }else {
                    followItem.setVisible(true);
                    unFollowItem.setVisible(false);
                };
            }else {
                mParseSigleton.editPorfile(
                        ParseUser.getCurrentUser(),
                        "following",
                        new ArrayList<>()
                );
            }
        }
    }
}
