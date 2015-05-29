package mp.joshua.com.twitchkit.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.parse.ParseUser;

import mp.joshua.com.twitchkit.CoverView;
import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;
import mp.joshua.com.twitchkit.DataProviders.DataPostOffice;
import mp.joshua.com.twitchkit.R;


public class TwitterFragment extends Fragment {

    WebView twitterWebView;
    RelativeLayout viewHolder;
    ParseUser profileOwner;
    DataPostOffice mDatapostOffice;
    CoverView mCoverView;

    public static TwitterFragment newInstance(){
        TwitterFragment fragment = new TwitterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatapostOffice = DataPostOffice.getInstance(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ConstantsLibrary.ACTION_PROFILE_OWNER_RETRIEVED);
        getActivity().registerReceiver(twitterFragCallbackReciever,intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(twitterFragCallbackReciever);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCoverView.removeCoverView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_twitterfeed,container,false);
        viewHolder = (RelativeLayout)v.findViewById(R.id.relative_twitterFrag_viewHolder);
        twitterWebView = (WebView)v.findViewById(R.id.WebView_twitterFeedFrag_feed);
        mCoverView = new CoverView(getActivity(),viewHolder);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(profileOwner != null){
            populateUI();
        }
    }

    BroadcastReceiver twitterFragCallbackReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionId = intent.getAction();

            switch (actionId){
                case ConstantsLibrary.ACTION_PROFILE_OWNER_RETRIEVED:
                    profileOwner = mDatapostOffice.getProfileOwner();
                    populateUI();
                    break;
            }
        }
    };

    private void populateUI() {
        mCoverView.createLoadingCover();
        //get Twitter name
        String twitterUsername = profileOwner.getString("twitterName");

        if (twitterUsername != null){
            twitterWebView.setWebViewClient(new WebViewClient());
            twitterWebView.loadUrl("https://twitter.com/" + twitterUsername);
        }else {
            CoverView coverView = new CoverView(getActivity(),viewHolder);
            coverView.createInstructionCover(ConstantsLibrary.CONST_TWITTERFEED_NULL_MESSAGE,null);
        }
        mCoverView.removeCoverView();
    }
}
