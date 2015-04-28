package mp.joshua.com.twitchkit.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mp.joshua.com.twitchkit.R;


public class TwitterFragment extends Fragment {

    public static TwitterFragment newInstance(){
        TwitterFragment fragment = new TwitterFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_twitterfeed,container,false);
    }
}
