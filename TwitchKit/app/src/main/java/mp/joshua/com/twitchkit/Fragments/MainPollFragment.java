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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;
import mp.joshua.com.twitchkit.DataProviders.DataPostOffice;
import mp.joshua.com.twitchkit.DataProviders.ParseSingleton;
import mp.joshua.com.twitchkit.R;

public class MainPollFragment extends Fragment {

    private ParseSingleton mParseSingleton;
    private DataPostOffice mDataPostoffice;
    private String mCurrentActivity;
    private ParseObject mPollObject;

    private TextView pollTitleView;
    private RadioGroup mRadioGroup;
    private Button voteButton;
    private Button deleteButton;

    public static MainPollFragment newInstance(String currentActivity){
        MainPollFragment fragment = new MainPollFragment();
        Bundle args = new Bundle();
        args.putString(ConstantsLibrary.ARG_CURRENT_ACTIVITY, currentActivity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mCurrentActivity = getArguments().getString(ConstantsLibrary.ARG_CURRENT_ACTIVITY);
        mParseSingleton = ParseSingleton.getInstance(getActivity());
        mDataPostoffice = DataPostOffice.getInstance(getActivity());

    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantsLibrary.ACTION_POLL_CREATED);
        intentFilter.addAction(ConstantsLibrary.ACTION_POLL_RETRIEVED);
        intentFilter.addAction(ConstantsLibrary.ACTION_POLL_USERVOTED);
        getActivity().registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_poll,container,false);
        pollTitleView = (TextView)v.findViewById(R.id.textView_poll_title);
        mRadioGroup = (RadioGroup)v.findViewById(R.id.radioGroup_poll);
        voteButton = (Button)v.findViewById(R.id.button_mainPoll_vote);
        deleteButton = (Button)v.findViewById(R.id.button_mainPoll_delete);

        voteButton.setOnClickListener(voteClickListener);
        deleteButton.setOnClickListener(deleteClickListener);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_frag_profile,menu);

        if (mCurrentActivity.equals(ConstantsLibrary.ARG_ACTIVITY_PROFILE)){
            MenuItem addItem = menu.findItem(R.id.action_add);
            addItem.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add){
            final AlertDialog.Builder adBuilder = new AlertDialog.Builder(getActivity())
                    .setTitle("Create Poll")
                    .setView(R.layout.alert_create_poll)
                    .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getAlertViewData(dialog);
                        }
                    })
                    .setNegativeButton("Cancel",null);
            adBuilder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionId = intent.getAction();

            if (actionId.equals(ConstantsLibrary.ACTION_POLL_CREATED)){
                //Start query for CURRENT users Poll
                mParseSingleton.getPoll(ParseUser.getCurrentUser().getObjectId());

            }else if (actionId.equals(ConstantsLibrary.ACTION_POLL_RETRIEVED)){
                //Get the poll from DataPostOffice, Call populateUI function
                mPollObject = mDataPostoffice.getPollObject();
                populateUI();

            }else if (actionId.equals(ConstantsLibrary.ACTION_POLL_USERVOTED)){
                //Start query for SELECTED users Poll
                mParseSingleton.getPoll(getActivity().getIntent().getStringExtra(ConstantsLibrary.EXTRA_PARSEUSER_ID));
                Toast.makeText(getActivity(),"poll voted",Toast.LENGTH_SHORT).show();
                voteButton.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentSetup();
    }

    View.OnClickListener voteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int selectedButton = mRadioGroup.getCheckedRadioButtonId();
            //int voteNumber = 1;

            //if(voteNumber !=  0){
            Log.d("GritzTest", "" + selectedButton);
                mParseSingleton.submitPollVote(
                        ParseUser.getCurrentUser().getObjectId(),
                        mPollObject,
                        selectedButton);
            //}
        }
    };

    View.OnClickListener deleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Todo: create method in parse singleton to delete poll object
        }
    };

    private void populateUI(){
        mRadioGroup.removeAllViews();

        //If user has voted remove vote button
        if (mPollObject.getList("participants").contains(ParseUser.getCurrentUser().getObjectId())){
            voteButton.setVisibility(View.GONE);
        }

        //Get arrayListsm from PollObject
        ArrayList pollOptionsArrayList = (ArrayList)mPollObject.getList("options");
        ArrayList votesArrayList = (ArrayList)mPollObject.getList("votes");

        //Set polls title
        pollTitleView.setText(mPollObject.getString("title"));

        //Create Radio buttons and progress bars
        for (int i = 0; i < pollOptionsArrayList.size(); i++){

            //Radio Button
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setId(i + 1);
            radioButton.setText((String)pollOptionsArrayList.get(i));

            //Progress Bar
            ProgressBar progressBar = (ProgressBar)LayoutInflater.from(getActivity()).inflate(R.layout.view_poll_progressbar,null);
            progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT));
            progressBar.setTag(i + 1);

            //Set progress bars data
            progressBar.setMax(votesArrayList.size());
            int j = 0;
            for (Object object: votesArrayList){
                if (object == (i + 1)){
                    j++;
                }
            }
            progressBar.setProgress(j);

            mRadioGroup.addView(radioButton);
            mRadioGroup.addView(progressBar);
        }
    }

    private void fragmentSetup(){
        if (mCurrentActivity.equals(ConstantsLibrary.ARG_ACTIVITY_FORMS)){
            //Get current users poll
            mParseSingleton.getPoll(ParseUser.getCurrentUser().getObjectId());
            //Hide vote button
            voteButton.setVisibility(View.GONE);

        }else if (mCurrentActivity.equals(ConstantsLibrary.ARG_ACTIVITY_PROFILE)){
            //Get selected users poll
            String parseUserID = getActivity().getIntent().getStringExtra(ConstantsLibrary.EXTRA_PARSEUSER_ID);
            mParseSingleton.getPoll(parseUserID);

            //Hide delete button
            deleteButton.setVisibility(View.GONE);
        }
    }

    private void getAlertViewData(DialogInterface dialog){
        Dialog d = (Dialog)dialog;

        TextView titleTextView = (TextView)d.findViewById(R.id.editText_alert_title);
        TextView optOneTextView = (TextView)d.findViewById(R.id.editText_alert_optOne);
        TextView optTwoTextView = (TextView)d.findViewById(R.id.editText_alert_optTwo);
        TextView optThreeTextView = (TextView)d.findViewById(R.id.editText_alert_optThree);
        TextView optFourTextView = (TextView)d.findViewById(R.id.editText_alert_optFour);
        TextView optFiveTextView = (TextView)d.findViewById(R.id.editText_alert_optFive);

        ArrayList<String> optionsArray = new ArrayList<String>();
        optionsArray.add(optOneTextView.getText().toString());
        optionsArray.add(optTwoTextView.getText().toString());
        optionsArray.add(optThreeTextView.getText().toString());
        optionsArray.add(optFourTextView.getText().toString());
        optionsArray.add(optFiveTextView.getText().toString());

        mParseSingleton.createPoll(
                ParseUser.getCurrentUser().getObjectId(),
                titleTextView.getText().toString(),
                optionsArray
        );
    }
}
