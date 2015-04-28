package mp.joshua.com.twitchkit.Fragments;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import mp.joshua.com.twitchkit.Adapters.SupportPageAdapter;
import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;
import mp.joshua.com.twitchkit.R;

public class SupportPageFragment extends android.support.v4.app.Fragment{

    ExpandableListView expandList;
    ArrayList<String> groupItem = new ArrayList<String>();
    ArrayList<Object> childItem = new ArrayList<Object>();
    FragmentManager fragmentManager;
    Bundle recievedArgs;
    Button continueButton;

    public static SupportPageFragment newInstance(String activityRequesting){
        SupportPageFragment supportPageFragment = new SupportPageFragment();
        Bundle args = new Bundle();
        args.putString(ConstantsLibrary.ARG_CURRENT_ACTIVITY, activityRequesting);
        supportPageFragment.setArguments(args);
        return supportPageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        fragmentManager = getFragmentManager();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_supportpage,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (recievedArgs.getString(ConstantsLibrary.ARG_CURRENT_ACTIVITY).equals(ConstantsLibrary.ARG_ACTIVITY_FORMS)){
            if (item.getItemId() == R.id.action_add){
                setAdapterFunc();
            }
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_supportpage,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recievedArgs = getArguments();

        setGroupData();
        setChildGroupData();

        expandList = (ExpandableListView)getActivity().findViewById(R.id.supportPage_expandList);
        expandList.setDividerHeight(0);
        expandList.setGroupIndicator(Drawable.createFromPath(String.valueOf(R.drawable.ic_launcher)));
        expandList.setClickable(true);

        if (recievedArgs.getString(ConstantsLibrary.ARG_CURRENT_ACTIVITY).equals(ConstantsLibrary.ARG_ACTIVITY_PROFILE)){
            setAdapterFunc();
            continueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container,ProfileFragment.newInstance())
                            .commit();
                }
            });
        }else if(recievedArgs.getString(ConstantsLibrary.ARG_CURRENT_ACTIVITY).equals(ConstantsLibrary.ARG_ACTIVITY_FORMS)){
            setAdapterFunc();
            continueButton.setVisibility(View.GONE);
        }
    }


    public void setAdapterFunc(){
        continueButton = (Button)getView().findViewById(R.id.supportFrag_continueToProfile);
        SupportPageAdapter mNewAdapter = new SupportPageAdapter(getActivity(),groupItem, childItem);
        mNewAdapter.setInflater(
                (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE),getActivity());

        expandList.setBackgroundColor(getResources().getColor(R.color.dirtyWhite));
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
