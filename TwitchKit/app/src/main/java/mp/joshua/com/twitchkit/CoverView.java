package mp.joshua.com.twitchkit;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;

public class CoverView {

    Activity mActivity;
    RelativeLayout coverViewHolder;
    RelativeLayout background;
    RelativeLayout.LayoutParams params;

   public CoverView(Activity context, RelativeLayout v){
       mActivity = context;
       coverViewHolder = v;
       background = new RelativeLayout(mActivity);
       background.setLayoutParams(new LinearLayout.LayoutParams(
               LinearLayout.LayoutParams.MATCH_PARENT,
               LinearLayout.LayoutParams.MATCH_PARENT));
       background.setBackgroundResource(R.color.dirtyWhite);

       params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
       params.addRule(RelativeLayout.CENTER_IN_PARENT);
   }

    public void createLoadingCover(){

        ProgressBar progressBar = new ProgressBar(mActivity, null, android.R.attr.progressBarStyleLarge);
        progressBar.setBackgroundColor(mActivity.getResources().getColor(R.color.dirtyWhite));
        progressBar.setLayoutParams(params);
        background.addView(progressBar);

        coverpage(background);
    }

    public void createInstructionCover(int messageCode, String currentActivity){

        String message = "";

        switch (messageCode){
            case ConstantsLibrary.CONST_USERLIST_MESSAGE:
                message = "Sign in to follow streamers. Streamers being followed will show here";
                break;

            case ConstantsLibrary.CONST_GIVEAWAY_NULL_MESSAGE:
                if (currentActivity.equals(ConstantsLibrary.ARG_ACTIVITY_FORMS)){
                    message = "You do not have an active giveaway at this time. Tap the + at the top to create a new one";
                }else if (currentActivity.equals(ConstantsLibrary.ARG_ACTIVITY_PROFILE)){
                    message = "There is no active giveaway at this time";
                }
                break;


            case ConstantsLibrary.CONST_POLL_NULL_MESSAGE:
                if (currentActivity.equals(ConstantsLibrary.ARG_ACTIVITY_FORMS)){
                    message = "You do not have an active poll at this time. Tap the + at the top to create a new one";
                }else if (currentActivity.equals(ConstantsLibrary.ARG_ACTIVITY_PROFILE)){
                    message = "There is no active poll at this time";
                }
                break;

            case ConstantsLibrary.CONST_QUERY_ERROR_MESSAGE:
                message = "Data could not be retrieved at this time";
                break;

            case ConstantsLibrary.CONST_TWITTERFEED_NULL_MESSAGE:
                message = "User doesn't have a twitter linked";
                break;

            case ConstantsLibrary.CONST_USER_NULL_MESSAGE:
                message = "You must be signed in to access this content";
                break;
        }

        TextView instructionView = new TextView(mActivity);
        instructionView.setLayoutParams(params);
        instructionView.setPadding(16, 0, 16, 0);
        instructionView.setTextSize(19);
        instructionView.setGravity(Gravity.CENTER);
        instructionView.setText(message);

        background.addView(instructionView);
        coverpage(background);
    }

    public void coverpage(View coverView){
        coverViewHolder.addView(coverView);
    }

    public void removeCoverView(){
        background.removeAllViews();
        coverViewHolder.removeView(background);
    }
}
