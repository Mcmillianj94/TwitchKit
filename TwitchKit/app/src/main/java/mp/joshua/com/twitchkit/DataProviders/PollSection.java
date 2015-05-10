package mp.joshua.com.twitchkit.DataProviders;

import android.widget.ProgressBar;
import android.widget.RadioButton;

/**
 * Created by joshuamcmillian on 5/8/15.
 */
public class PollSection {

    public RadioButton mRadioButton;
    public ProgressBar mProgressBar;

    public PollSection(RadioButton r, ProgressBar p){
        mRadioButton = r;
        mProgressBar = p;
    }
}
