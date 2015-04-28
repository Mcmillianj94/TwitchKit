package mp.joshua.com.twitchkit.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;
import mp.joshua.com.twitchkit.Fragments.LoginFragment;
import mp.joshua.com.twitchkit.Fragments.SignupFragment;
import mp.joshua.com.twitchkit.R;

public class LoginActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String requestedFragment = getIntent().getExtras().getString(ConstantsLibrary.EXTRA_ACTIVITY_INTENTSENDER);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (requestedFragment.equals(ConstantsLibrary.EXTRA_FRAGMENT_LOGIN)){
            fragmentTransaction.replace(R.id.container, LoginFragment.newInstance());

        }else if (requestedFragment.equals(ConstantsLibrary.EXTRA_FRAGMENT_SIGNUP)){
            fragmentTransaction.replace(R.id.container, SignupFragment.newInstance());
        }
        fragmentTransaction.commit();
    }
}
