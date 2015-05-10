package mp.joshua.com.twitchkit.DataProviders;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import mp.joshua.com.twitchkit.R;

public class ParseSingleton {

    private static ParseSingleton instance = null;
    private static DataPostOffice mDataPostOffice;
    private static Context mContext;
    private boolean didSave;

    protected ParseSingleton() {
        // Exists only to defeat instantiation.
    }
    public static ParseSingleton getInstance(Context context) {
        mContext = context;
        mDataPostOffice = DataPostOffice.getInstance(context);
        if(instance == null) {
            instance = new ParseSingleton();
        }
        return instance;
    }

    public void logUserOut(Context context){
        ParseUser.logOut();

        AlertDialog.Builder logoutAlert = new AlertDialog.Builder(context)
                .setTitle("Logout Successful")
                .setMessage("You have been successfully logged out")
                .setPositiveButton("ok", null);
        logoutAlert.show();
    }

    public void getAllStreamerList(){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> scoreList, ParseException e) {
                if (e == null) {
                    ArrayList<ParseUser> tempArray = new ArrayList<ParseUser>();
                    for (ParseUser poTemp : scoreList) {
                        tempArray.add(poTemp);
                    }
                    mDataPostOffice.setParseUserArrayList(tempArray);

                    Intent userlistIntent = new Intent(ConstantsLibrary.ACTION_GET_USERS);
                    mContext.sendBroadcast(userlistIntent);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getFollowing(){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
    }

    public void reloadOptionsMenu(Menu menu){

        if (ParseUser.getCurrentUser() == null){
            MenuItem menuItem = menu.findItem(R.id.action_login);
            menuItem.setVisible(true);
            MenuItem menuItem2 = menu.findItem(R.id.action_logout);
            menuItem2.setVisible(false);

        }else if(ParseUser.getCurrentUser() != null){
            MenuItem menuItem = menu.findItem(R.id.action_logout);
            menuItem.setVisible(true);
            MenuItem menuItem2 = menu.findItem(R.id.action_login);
            menuItem2.setVisible(false);
        }
    }

    public boolean createSupportLink(String title, String link, String image){
        ArrayList <String> testStrings = new ArrayList<>();
        testStrings.add(title);
        testStrings.add(link);
        testStrings.add(image);

        for (String s : testStrings){
            if (s == null){
                return false;
            }
        }

        ParseObject supportLink = new ParseObject("SupportLinks");
        supportLink.put("owner", ParseUser.getCurrentUser().getObjectId());
        supportLink.put("title", title);
        supportLink.put("link",link);
        supportLink.put("image",image);
        supportLink.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    didSave = true;
                }else{
                    didSave = false;
                    e.printStackTrace();
                }
            }
        });
        return didSave;
    }

    public void getSuportLinkList(String ownerId){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("SupportLinks");
        query.whereEqualTo("owner", ownerId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    ArrayList<Object> tempArray = new ArrayList<Object>();
                    for (ParseObject poTemp : scoreList) {
                        tempArray.add(poTemp);
                    }
                    mDataPostOffice.setSupportLinkArrayList(tempArray);

                    Intent userlistIntent = new Intent(ConstantsLibrary.ACTION_GET_SUPPORTLINKS);
                    mContext.sendBroadcast(userlistIntent);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void deleteSupportLink(String objectID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("SupportLinks");
        query.getInBackground(objectID, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.deleteInBackground();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void editSupportLink(String objectID, final String title, final String link, final String image){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("SupportLinks");
        query.getInBackground(objectID, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.put("title", title);
                    object.put("link", link);
                    object.put("image", image);
                    object.saveInBackground();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void createGiveaway(String ownerId, String title, String message){
        ParseObject giveaway = new ParseObject("Giveaway");
        giveaway.put("owner", ownerId);
        giveaway.put("title",title);
        giveaway.put("message",message);
        giveaway.put("active", true);
        giveaway.put("participants", new ArrayList<>());
        giveaway.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e ==  null){
                    Intent giveawayCreatedIntent = new Intent(ConstantsLibrary.ACTION_GIVEAWAY_CREATED);
                    mContext.sendBroadcast(giveawayCreatedIntent);
                }else{
                    e.printStackTrace();
                }
            }
        });
    }

    public void getGiveaway(String ownerId){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Giveaway");
        query.whereEqualTo("owner", ownerId);
        Toast.makeText(mContext,ownerId,Toast.LENGTH_SHORT).show();
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    if (list.size() > 0){
                        mDataPostOffice.setGiveawayObject(list.get(0));
                        Intent giveawayRetrievedIntent = new Intent(ConstantsLibrary.ACTION_GIVEAWAY_RETRIEVED);
                        mContext.sendBroadcast(giveawayRetrievedIntent);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addGiveawayEntry(ParseObject giveawayObject, String ownerID){
        ArrayList<Object> arrayList = null;
        arrayList = (ArrayList<Object>)giveawayObject.getList("participants");

        boolean hasEntered = arrayList.contains(ownerID);

        if (hasEntered){
            Toast.makeText(mContext,"Entry Not Submitted Bihh", Toast.LENGTH_SHORT).show();

        }else if (!hasEntered){
            arrayList.add(ownerID);
            giveawayObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Toast.makeText(mContext,"Entry Submitted Bihh", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void createPoll(String ownerID, String title,ArrayList<String> optionsList){
        ParseObject pollObject = new ParseObject("Poll");
        pollObject.put("owner",ownerID);
        pollObject.put("title",title);
        pollObject.put("options",optionsList);
        pollObject.put("votes", new ArrayList<>());
        pollObject.put("participants", new ArrayList<>());
        pollObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Intent intent = new Intent(ConstantsLibrary.ACTION_POLL_CREATED);
                    mContext.sendBroadcast(intent);
                }else {

                }
            }
        });
    }

    public void getPoll(String ownerId){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Poll");
        query.whereEqualTo("owner", ownerId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    mDataPostOffice.setPollObject(list.get(0));
                    Intent intent = new Intent(ConstantsLibrary.ACTION_POLL_RETRIEVED);
                    mContext.sendBroadcast(intent);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void submitPollVote(String userID,ParseObject pollObject,int vote){
        Log.d("GritzTest","" + vote);
        ArrayList<Object> voteList = null;
        ArrayList<Object> participantList = null;
        voteList = (ArrayList<Object>)pollObject.getList("votes");
        participantList = (ArrayList<Object>)pollObject.getList("participants");

        boolean hasEntered = participantList.contains(userID);

        if (hasEntered){
            Toast.makeText(mContext,"NOPE", Toast.LENGTH_SHORT).show();

        }else if (!hasEntered){
            voteList.add(vote);
            participantList.add(userID);
            pollObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        Intent intent = new Intent(ConstantsLibrary.ACTION_POLL_USERVOTED);
                        mContext.sendBroadcast(intent);
                    }else{
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
