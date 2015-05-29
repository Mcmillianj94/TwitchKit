package mp.joshua.com.twitchkit;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by joshuamcmillian on 5/26/15.
 */
public class ParseInit extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "Dqd3nKnC4IGhqRkcsw8ylQXcQUkGq0dJMyDIJgPo", "gXnQrBFy2viRiSixadRPTmYBDYI7lqkIx6iDWzUx");
    }
}
