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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;
import java.util.Random;

import mp.joshua.com.twitchkit.CoverView;
import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;
import mp.joshua.com.twitchkit.DataProviders.DataPostOffice;
import mp.joshua.com.twitchkit.DataProviders.ParseSingleton;
import mp.joshua.com.twitchkit.R;

public class MainGiveawayFragment extends android.support.v4.app.Fragment{

    private ParseSingleton mParseSingleton;
    private DataPostOffice mDataPostOffice;
    private CoverView coverView;
    private CoverView prevWinnerCoverView;
    private String currentActivity;

    private RelativeLayout viewHolder;
    private TextView mTitleTextView;
    private TextView mMessageTextView;
    private TextView mParticipantCount;
    private Button submitButton;
    private Button pickWinnerButton;
    private Button previousWinners;
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
        viewHolder = (RelativeLayout)v.findViewById(R.id.viewHolderGive);
        participantsHolder = (LinearLayout)v.findViewById(R.id.linearLayout_mainGiveaway_participants);
        mTitleTextView = (TextView)v.findViewById(R.id.textView_giveaway_title);
        mMessageTextView = (TextView)v.findViewById(R.id.textView_giveaway_message);
        mParticipantCount = (TextView)v.findViewById(R.id.textView_giveaway_participantCount);
        submitButton = (Button)v.findViewById(R.id.Button_mainGiveaway_submit);
        pickWinnerButton = (Button)v.findViewById(R.id.Button_mainGiveaway_pickWinner);
        previousWinners = (Button)v.findViewById(R.id.Button_mainGiveaway_prevWinners);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantsLibrary.ACTION_GIVEAWAY_CREATED);
        intentFilter.addAction(ConstantsLibrary.ACTION_GIVEAWAY_RETRIEVED);
        intentFilter.addAction(ConstantsLibrary.ACTION_GIVEAWAY_COMPLETED);
        intentFilter.addAction(ConstantsLibrary.ACTION_GIVEAWAY_ENTERED);
        intentFilter.addAction(ConstantsLibrary.ACTION_GIVEAWAY_DATAERROR);
        getActivity().registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
        //remove cover view
        coverView.removeCoverView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_frag_forms,menu);

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
        coverView = new CoverView(getActivity(), viewHolder);

        if (ParseUser.getCurrentUser() != null){
            fragmentSetUp();
        }else{
            coverView.createInstructionCover(ConstantsLibrary.CONST_USER_NULL_MESSAGE,null);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();

            switch (viewId){
                case R.id.Button_mainGiveaway_submit:
                    mParseSingleton.addGiveawayEntry(giveawayObject,ParseUser.getCurrentUser());
                    break;

                case R.id.Button_mainGiveaway_pickWinner:
                    Random rand = new Random();
                    List tempList = giveawayObject.getList("participants");
                    int n = rand.nextInt(tempList.size());
                    String winnerID = (String)tempList.get(n);
                    break;

                case R.id.Button_mainGiveaway_prevWinners:
                    LayoutInflater layoutInflater = getLayoutInflater(null);
                    RelativeLayout prevWinnersLayout = (RelativeLayout)layoutInflater.inflate(R.layout.alert_giveaway_winners,null);
                    previousWinnersSetUp(prevWinnersLayout);

                    AlertDialog.Builder adBuilder = new AlertDialog.Builder(getActivity())
                            .setTitle("Previous Winners")
                            .setView(prevWinnersLayout)
                            .setNegativeButton("Close", null);
                    adBuilder.show();
                    break;
            }
        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionId = intent.getAction();

            switch (actionId){
                case ConstantsLibrary.ACTION_GIVEAWAY_CREATED:
                    startGiveawayRetreival(ParseUser.getCurrentUser().getObjectId());
                    break;

                case ConstantsLibrary.ACTION_GIVEAWAY_RETRIEVED:
                    giveawayObject = mDataPostOffice.getGiveawayObject();
                    populateUI();
                    break;

                case ConstantsLibrary.ACTION_GIVEAWAY_ENTERED:
                    Toast.makeText(getActivity(),"Entered, please check in later for winner",Toast.LENGTH_LONG).show();
                    break;

                case ConstantsLibrary.ACTION_GIVEAWAY_COMPLETED:
                    //TODO: RELOAD DATA
                    break;

                case ConstantsLibrary.ACTION_GIVEAWAY_DATAERROR:
                    coverView.removeCoverView();
                    if (intent.getExtras().getString(ConstantsLibrary.EXTRA_ERROR_TYPE).equals(ConstantsLibrary.EXTRA_ERRORTYPE_NULL)){
                        coverView.createInstructionCover(ConstantsLibrary.CONST_GIVEAWAY_NULL_MESSAGE,currentActivity);

                    }else if (intent.getExtras().getString(ConstantsLibrary.EXTRA_ERROR_TYPE).equals(ConstantsLibrary.EXTRA_ERRORTYPE_QUERY)){
                        coverView.createInstructionCover(ConstantsLibrary.CONST_QUERY_ERROR_MESSAGE,currentActivity);
                    }
                    break;
            }
        }
    };

    private void startGiveawayRetreival(String ownerID){
        mParseSingleton.getGiveaway(ownerID);
    }

    //Set up the fragment for activity
    private void fragmentSetUp(){
        //show loading view
        coverView.createLoadingCover();

        //Setup for Profile
        if (currentActivity.equals(ConstantsLibrary.ARG_ACTIVITY_PROFILE)){
            String parseUserID = getActivity().getIntent().getStringExtra(ConstantsLibrary.EXTRA_PARSEUSER_ID);
            mParseSingleton.getGiveaway(parseUserID);
            participantsHolder.setVisibility(View.GONE);
            pickWinnerButton.setVisibility(View.GONE);
            previousWinners.setVisibility(View.GONE);
            submitButton.setOnClickListener(onClickListener);

        //Setup for Form
        }else if (currentActivity.equals(ConstantsLibrary.ARG_ACTIVITY_FORMS)){
            mParseSingleton.getGiveaway(ParseUser.getCurrentUser().getObjectId());
            submitButton.setVisibility(View.GONE);
            pickWinnerButton.setOnClickListener(onClickListener);
            previousWinners.setOnClickListener(onClickListener);
        }
    }

    //Fill in data once it is recieved
    private void populateUI(){
        mTitleTextView.setText(giveawayObject.getString("title"));
        mMessageTextView.setText(giveawayObject.getString("message"));
        mParticipantCount.setText("" + giveawayObject.getList("participants").size());
        coverView.removeCoverView();
    }

    //Previous Winners layout setUP
    private void previousWinnersSetUp(RelativeLayout view){
        //Create cover for prevWinView
        prevWinnerCoverView = new CoverView(getActivity(),view);
        //show loading cover
        prevWinnerCoverView.createLoadingCover();
        View contentHolder = view.findViewById(R.id.linearLayout_prevWinAlert_contentHolder);
        for (int i=0; i < 6; i++){
            TextView textView = new TextView(getActivity());
            textView.setText("NIGGA!");
            textView.setTextSize(21);
            view.addView(contentHolder);
        }

    }
}
