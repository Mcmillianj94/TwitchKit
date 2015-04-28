package mp.joshua.com.twitchkit.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;
import mp.joshua.com.twitchkit.R;

public class MainGiveawayFragment extends android.support.v4.app.Fragment{


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
        recievedArguments = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_giveaway,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String currentActivity = recievedArguments.getString(ConstantsLibrary.ARG_CURRENT_ACTIVITY);

        LinearLayout participantsHolder = (LinearLayout)getView().findViewById(R.id.linearLayout_mainGiveaway_participants);
        EditText emailInput = (EditText)getView().findViewById(R.id.EditText_mainGiveaway_email);
        EditText usernameInput = (EditText)getView().findViewById(R.id.EditText_mainGiveaway_userName);
        Button submitButton = (Button)getView().findViewById(R.id.Button_mainGiveaway_submit);
        Button pickWinnerButton = (Button)getView().findViewById(R.id.Button_mainGiveaway_pickWinner);

        if (currentActivity.equals(ConstantsLibrary.ARG_ACTIVITY_PROFILE)){
            participantsHolder.setVisibility(View.GONE);
            pickWinnerButton.setVisibility(View.GONE);

        }else if (currentActivity.equals(ConstantsLibrary.ARG_ACTIVITY_FORMS)){
            emailInput.setVisibility(View.GONE);
            usernameInput.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);
        }
    }
}
