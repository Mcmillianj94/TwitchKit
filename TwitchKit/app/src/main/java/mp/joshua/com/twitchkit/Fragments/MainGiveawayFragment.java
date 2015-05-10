package mp.joshua.com.twitchkit.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;
import java.util.Random;

import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;
import mp.joshua.com.twitchkit.DataProviders.DataPostOffice;
import mp.joshua.com.twitchkit.DataProviders.ParseSingleton;
import mp.joshua.com.twitchkit.R;

public class MainGiveawayFragment extends android.support.v4.app.Fragment{

    private ParseSingleton mParseSingleton;
    private DataPostOffice mDataPostOffice;
    String currentActivity;

    private TextView mTitleTextView;
    private TextView mMessageTextView;
    private TextView mParticipantCount;
    private Button submitButton;
    private Button pickWinnerButton;
    private LinearLayout participantsHolder;

    private ParseObject giveawayObject;
    private Bundle recievedArguments;

    public static MainGiveawayFragment newInstance(String currentActivity){
        MainGiveawayFragment fragment = new MainGiveawayFragment();
        Bundle args = new Bundle();
        args.putString(ConstantsLibrary.ARG_CURRENT_ACTIVITY,currentActivity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        recievedArguments = getArguments();
        currentActivity = recievedArguments.getString(ConstantsLibrary.ARG_CURRENT_ACTIVITY);
        mParseSingleton = ParseSingleton.getInstance(getActivity());
        mDataPostOffice = DataPostOffice.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_giveaway,container,false);
        participantsHolder = (LinearLayout)getView().findViewById(R.id.linearLayout_mainGiveaway_participants);
        mTitleTextView = (TextView)getView().findViewById(R.id.textView_giveaway_title);
        mMessageTextView = (TextView)getView().findViewById(R.id.textView_giveaway_message);
        mParticipantCount = (TextView)getView().findViewById(R.id.textView_giveaway_participantCount);
        submitButton = (Button)getView().findViewById(R.id.Button_mainGiveaway_submit);
        pickWinnerButton = (Button)getView().findViewById(R.id.Button_mainGiveaway_pickWinner);
        return v;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_frag_profile,menu);

        if (currentActivity.equals(ConstantsLibrary.ARG_ACTIVITY_PROFILE)){
            MenuItem addItem = menu.findItem(R.id.action_add);
            addItem.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add){
            final AlertDialog.Builder adBuilder = new AlertDialog.Builder(getActivity())
                    .setTitle("Create Giveaway")
                    .setView(R.layout.alert_create_giveaway)
                    .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Dialog d = (Dialog)dialog;

                            TextView titleTextView = (TextView)d.findViewById(R.id.editText_createGiveaway_title);
                            TextView messageTextView = (TextView)d.findViewById(R.id.editText_createGiveaway_message);

                            mParseSingleton.createGiveaway(
                                    ParseUser.getCurrentUser().getObjectId(),
                                    titleTextView.getText().toString(),
                                    messageTextView.getText().toString());
                        }
                    })
                    .setNegativeButton("Cancel",null);
            adBuilder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentSetUp();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            if(viewId == R.id.Button_mainGiveaway_submit){
                mParseSingleton.addGiveawayEntry(giveawayObject,ParseUser.getCurrentUser().getObjectId());

            }else if (viewId == R.id.Button_mainGiveaway_pickWinner){
                Random rand = new Random();
                List tempList = giveawayObject.getList("participants");

                //Randomize a number and pick the user at that index
                int n = rand.nextInt(tempList.size());
                Toast.makeText(getActivity(),"winner " + n,Toast.LENGTH_SHORT).show();
            }
        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionId = intent.getAction();

            if (actionId.equals(ConstantsLibrary.ACTION_GIVEAWAY_CREATED)){
                startGiveawayRetreival(ParseUser.getCurrentUser().getObjectId());

            }else if (actionId.equals(ConstantsLibrary.ACTION_GIVEAWAY_RETRIEVED)){
                giveawayObject = mDataPostOffice.getGiveawayObject();
                populateUI();
            }
        }
    };

    private void startGiveawayRetreival(String ownerID){
        mParseSingleton.getGiveaway(ownerID);
    }

    private void fragmentSetUp(){
        if (currentActivity.equals(ConstantsLibrary.ARG_ACTIVITY_PROFILE)){
            String parseUserID = getActivity().getIntent().getStringExtra(ConstantsLibrary.EXTRA_PARSEUSER_ID);
            mParseSingleton.getGiveaway(parseUserID);
            participantsHolder.setVisibility(View.GONE);
            pickWinnerButton.setVisibility(View.GONE);
            submitButton.setOnClickListener(onClickListener);

        }else if (currentActivity.equals(ConstantsLibrary.ARG_ACTIVITY_FORMS)){
            mParseSingleton.getGiveaway(ParseUser.getCurrentUser().getObjectId());
            submitButton.setVisibility(View.GONE);
            pickWinnerButton.setOnClickListener(onClickListener);
        }
    }

    private void populateUI(){
        if (giveawayObject != null){
            mTitleTextView.setText(giveawayObject.getString("title"));
            mMessageTextView.setText(giveawayObject.getString("message"));
            mParticipantCount.setText("" + giveawayObject.getList("participants").size());
        }
    }
}
