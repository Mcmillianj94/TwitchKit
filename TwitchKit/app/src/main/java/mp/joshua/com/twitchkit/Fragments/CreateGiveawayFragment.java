package mp.joshua.com.twitchkit.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.parse.ParseObject;

import mp.joshua.com.twitchkit.Activities.FormsActivity;
import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;
import mp.joshua.com.twitchkit.DataProviders.DataPostOffice;
import mp.joshua.com.twitchkit.DataProviders.ParseSingleton;
import mp.joshua.com.twitchkit.R;

public class CreateGiveawayFragment extends android.support.v4.app.Fragment{

    ParseSingleton mParseSingleton;
    DataPostOffice mDataPostOffice;
    ParseObject mGiveawayObject;
    ViewPager mViewPager;

    EditText titleEdit;
    EditText messageEdit;

    public static CreateGiveawayFragment newInstance(){
        CreateGiveawayFragment fragment = new CreateGiveawayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParseSingleton = ParseSingleton.getInstance(getActivity());
        mDataPostOffice = DataPostOffice.getInstance(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantsLibrary.ACTION_GIVEAWAY_CREATED);
        intentFilter.addAction(ConstantsLibrary.ACTION_GIVEAWAY_RETRIEVED);
        getActivity().registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alert_create_giveaway,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        titleEdit = (EditText)getView().findViewById(R.id.editText_createGiveaway_title);
        messageEdit = (EditText)getView().findViewById(R.id.editText_createGiveaway_message);

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionID =  intent.getAction();
            FormsActivity formsActivity = (FormsActivity)getActivity();

            /*if (actionID.equals(ConstantsLibrary.ACTION_GIVEAWAY_CREATED)){
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(, MainGiveawayFragment.newInstance(ConstantsLibrary.ARG_ACTIVITY_FORMS))
                        .commit();
            }*/
        }
    };
}
