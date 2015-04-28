package mp.joshua.com.twitchkit.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import mp.joshua.com.twitchkit.Adapters.UserlistAdapter;
import mp.joshua.com.twitchkit.R;

public class UserListFragment extends Fragment {

    ExpandableListView expandList;
    ArrayList<String> groupItem = new ArrayList<String>();
    ArrayList<Object> childItem = new ArrayList<Object>();

    public static UserListFragment newInstance(){
        UserListFragment fragment = new UserListFragment();
        return fragment;
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
        setChildGroupData();

        expandList.setDividerHeight(0);
        expandList.setGroupIndicator(Drawable.createFromPath(String.valueOf(R.drawable.ic_launcher)));
        expandList.setBackgroundColor(getResources().getColor(R.color.dirtyWhite));
        expandList.setClickable(true);

        UserlistAdapter mNewAdapter = new UserlistAdapter(getActivity(),groupItem, childItem);
        mNewAdapter.setInflater(
                (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE),getActivity());

        expandList.setAdapter(mNewAdapter);
    }

    public void setGroupData() {
        groupItem.add("Following");
        groupItem.add("All Users");
    }

    public void setChildGroupData() {
        /**
         * Add Data For TecthNology
         */
        ArrayList<String> child = new ArrayList<String>();
        child.add("BigGritz904");
        child.add("LovedPvP");
        child.add("Paluco");
        childItem.add(child);

        /**
         * Add Data For Mobile
         */
        child = new ArrayList<String>();
        child.add("Android");
        child.add("Window Mobile");
        child.add("iPHone");
        child.add("Blackberry");
        child.add("HTC");
        child.add("Apple");
        child.add("Samsung");
        child.add("Nokia");
        child.add("Contact Us");
        child.add("About Us");
        child.add("Location");
        child.add("Root Cause");
        childItem.add(child);
    }
}
