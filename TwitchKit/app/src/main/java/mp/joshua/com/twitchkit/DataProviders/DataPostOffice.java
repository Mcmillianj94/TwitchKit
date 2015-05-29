package mp.joshua.com.twitchkit.DataProviders;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

public class DataPostOffice {
    private static DataPostOffice instance = null;
    private static Context mContext;

    private ArrayList<ParseUser> parseUserArrayList;
    private ArrayList<ParseUser> parseUserSearchArrayList;

    private ArrayList supportLinkArrayList;

    private ArrayList<Fragment> formActivityFragmentList;

    private ParseObject giveawayObject;
    private ParseObject pollObject;

    private ParseUser profileOwner;


    protected DataPostOffice() {
        // Exists only to defeat instantiation.
    }
    public static DataPostOffice getInstance(Context context) {
        mContext = context;

        if(instance == null) {
            instance = new DataPostOffice();
        }
        return instance;
    }

    //ParseUserArrayList Getter and Setter
    public void setParseUserArrayList(ArrayList<ParseUser> recievedList) {
        this.parseUserArrayList = recievedList;
    }
    public ArrayList<ParseUser> getParseUserArrayList() {
        return parseUserArrayList;
    }

    //ParseUserSearchArrayList Getter and Setter
    public void setParseUserSearchArrayList(ArrayList<ParseUser> recievedList) {
        this.parseUserSearchArrayList = recievedList;
    }
    public ArrayList<ParseUser> getParseUserSearchArrayList() {
        return parseUserSearchArrayList;
    }

    //supportLinkArrayList Getter and Setter
    public void setSupportLinkArrayList(ArrayList recievedList){
        this.supportLinkArrayList = recievedList;
    }
    public  ArrayList getSupportLinkArrayList(){
        return supportLinkArrayList;
    }

    //GiveawayObject
    public void setGiveawayObject(ParseObject object){
        this.giveawayObject = object;
    }
    public ParseObject getGiveawayObject(){
        return giveawayObject;
    }

    //GiveawayObject
    public void setPollObject(ParseObject recievedObject){
        this.pollObject = recievedObject;
    }
    public ParseObject getPollObject(){
        return pollObject;
    }

    public void setProfileOwner(ParseUser parseUser){
        profileOwner = parseUser;
    }
    public ParseUser getProfileOwner(){
        return profileOwner;
    }

}
