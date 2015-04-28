package mp.joshua.com.twitchkit.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mp.joshua.com.twitchkit.R;

public class CreateGiveawayFragment extends android.support.v4.app.Fragment{

    public static CreateGiveawayFragment newInstance(){
        CreateGiveawayFragment fragment = new CreateGiveawayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_giveaway,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button submitButton = (Button)getView().findViewById(R.id.button_createGiveaway_create);
    }
}
