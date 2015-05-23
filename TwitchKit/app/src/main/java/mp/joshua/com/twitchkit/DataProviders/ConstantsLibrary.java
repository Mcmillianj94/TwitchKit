package mp.joshua.com.twitchkit.DataProviders;

public class ConstantsLibrary {

    public static final String ARG_CURRENT_ACTIVITY = "currentActivity";
    public static final String ARG_ACTIVITY_PROFILE = "profileActivity";
    public static final String ARG_ACTIVITY_FORMS = "formsActivity";
    public static final String ARG_PARSEUSER_ID = "userIDarg";

    public static final String EXTRA_ACTIVITY_INTENTSENDER = "IntentSender";
    public static final String EXTRA_FRAGMENT_LOGIN = "loginFragment";
    public static final String EXTRA_PARSEUSER_ID = "parseUserId";
    public static final String EXTRA_ERROR_TYPE = "queryError";
    public static final String EXTRA_ERRORTYPE_QUERY = "queryError";
    public static final String EXTRA_ERRORTYPE_NULL = "nullError";
    public static final String EXTRA_FOLLOWING_STATUS = "followingStatus";

    public static final String ACTION_GET_USERS = "actionGetUsers";
    public static final String ACTION_SEARCH_USERS = "actionSearchUsers";

    public static final String ACTION_LOGGED_OUT = "actionLoggedOut";

    public static final String ACTION_GET_SUPPORTLINKS = "actionGetSupportLinks";

    public static final String ACTION_PROFILE_QUERY_ERROR = "profileQueryError";

    public static final String ACTION_GIVEAWAY_CREATED = "giveawayCreated";
    public static final String ACTION_GIVEAWAY_ALREADYACTIVE = "giveawayIsActive";
    public static final String ACTION_GIVEAWAY_RETRIEVED = "giveawayRetrieved";
    public static final String ACTION_GIVEAWAY_COMPLETED = "giveawayDeleted";
    public static final String ACTION_GIVEAWAY_DATAERROR = "giveawayDataError";
    public static final String ACTION_GIVEAWAY_ENTERED = "giveawayEntered";
    public static final String ACTION_GIVEAWAY_WINNERS = " giveaway";

    public static final String ACTION_POLL_CREATED = "pollCreated";
    public static final String ACTION_POLL_RETRIEVED = "pollRetrieved";
    public static final String ACTION_POLL_USERVOTED = "userVoted";
    public static final String ACTION_POLL_DELETED = "pollDeleted";
    public static final String ACTION_POLL_DATAERROR = "pollDataError";

    public static final String ACTION_SETTING_CHANGED = "settingChanged";

    public static final String ACTION_PROFILE_OWNER_RETRIEVED = "profileOwnerRetrieved";
    public static final String ACTION_PROFILE_OWNER_FOLLOWED = "profileOwnerFollowed";

    public static final int CONST_POLL_NULL_MESSAGE = 10;
    public static final int CONST_GIVEAWAY_NULL_MESSAGE = 20;
    public static final int CONST_USERLIST_MESSAGE = 30;
    public static final int CONST_QUERY_ERROR_MESSAGE = 40;
    public static final int CONST_TWITTERFEED_NULL_MESSAGE = 50;
    public static final int CONST_USER_NULL_MESSAGE = 60;

    public static final String TAG_SETTINGS_BIO = "bioChangeAlert";
    public static final String TAG_SETTINGS_TWITTERNAME = "twitterChangeAlert";
    public static final String TAG_SETTINGS_PROFILEPIC = "profilrPicChangeAlert";

    public static final int RESULT_GALLERY = 105;
    public static final int RESULT_TWITTERLINK = 110;
}
