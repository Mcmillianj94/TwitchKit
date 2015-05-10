package mp.joshua.com.twitchkit.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import mp.joshua.com.twitchkit.Adapters.UserlistAdapter;
import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;
import mp.joshua.com.twitchkit.DataProviders.DataPostOffice;
import mp.joshua.com.twitchkit.DataProviders.ParseSingleton;
import mp.joshua.com.twitchkit.R;

public class UserListFragment extends Fragment {

    ParseSingleton mParseSingleton;
    DataPostOffice mDataPostOffice;
    ExpandableListView expandList;
    ArrayList<String> groupItem = new ArrayList<String>();
    ArrayList<Object> childItem = new ArrayList<Object>();

    public static UserListFragment newInstance(){
        UserListFragment fragment = new UserListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParseSingleton = ParseSingleton.getInstance(getActivity());
        mDataPostOffice = DataPostOffice.getInstance(getActivity());
        setChildItem();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantsLibrary.ACTION_GET_USERS);
        intentFilter.addAction(ConstantsLibrary.ACTION_SEARCH_USERS);
        getActivity().registerReceiver(mMessageReceiver,intentFilter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_expandlist,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        expandList = (ExpandableListView)getActivity().findViewById(R.id.expand_list);
        setGroupData();

        expandList.setDividerHeight(0);
        expandList.setGroupIndicator(Drawable.createFromPath(String.valueOf(R.drawable.ic_launcher)));
        expandList.setBackgroundColor(getResources().getColor(R.color.dirtyWhite));
        expandList.setClickable(true);

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            ArrayList userListArray = null;

            if (intentAction.equals(ConstantsLibrary.ACTION_GET_USERS)){
                userListArray = mDataPostOffice.getParseUserArrayList();

            }else if (intentAction.equals(ConstantsLibrary.ACTION_SEARCH_USERS)){
                userListArray = mDataPostOffice.getParseUserSearchArrayList();
                Log.d("TestFragment","" + userListArray.size());
            }

            Log.d("Test",intentAction);

            if (userListArray != null){
                childItem.clear();
                childItem.add(userListArray);
                childItem.add(userListArray);

                UserlistAdapter mNewAdapter = new UserlistAdapter(getActivity(),groupItem, childItem);
                mNewAdapter.setInflater(
                        (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE),getActivity());

                expandList.setAdapter(mNewAdapter);
            }else {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity())
                        .setMessage("Data could not be retrieved")
                        .setPositiveButton("ok",null);
                alert.show();
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mMessageReceiver);
    }

    public void setGroupData() {
        groupItem.add("Following");
        groupItem.add("All Users");
    }

    public void setChildItem(){
        mParseSingleton.getAllStreamerList();
    }
}
