package mp.joshua.com.twitchkit.Activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.parse.ParseFile;
import com.parse.ParseUser;

import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;
import mp.joshua.com.twitchkit.DataProviders.ParseSingleton;
import mp.joshua.com.twitchkit.R;
import mp.joshua.com.twitchkit.Services.ImageCoverterService;


public class SettingsActivity extends ActionBarActivity {

    ParseSingleton mParseSingleton;

    LayoutInflater mLayoutInflater;
    SmartImageView profilePictureButton;
    Button bioButton;
    Button twitterButton;
    View inflatedEditView;

    byte[] imageBytes;
    Uri imageURI;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mParseSingleton = ParseSingleton.getInstance(this);

        mLayoutInflater = getLayoutInflater();
        profilePictureButton = (SmartImageView)findViewById(R.id.smartImage_settingsAct_profilePic);
        bioButton = (Button)findViewById(R.id.button_settings_bio);
        twitterButton = (Button)findViewById(R.id.button_settings_twitter);

        bioButton.setOnClickListener(showEditViewClickListener);
        twitterButton.setOnClickListener(showEditViewClickListener);

        profilePictureButton.setOnClickListener(launchActivityClickListener);

        populateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentfilter = new IntentFilter(ConstantsLibrary.ACTION_SETTING_CHANGED);
        registerReceiver(parseSingltonCallBackReciever,intentfilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(parseSingltonCallBackReciever);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            //Get image uri
            imageURI = data.getData();
            createEditView(R.id.smartImage_settingsAct_profilePic);
        }else if (resultCode == RESULT_CANCELED){

        }
    }

    BroadcastReceiver parseSingltonCallBackReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String acionID = intent.getAction();

            switch (acionID){
                case ConstantsLibrary.ACTION_SETTING_CHANGED:
                    populateUI();
                    break;
            }
        }
    };

    //Listener shows editView
    View.OnClickListener showEditViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            //Base alert view
            AlertDialog.Builder editView = new AlertDialog.Builder(SettingsActivity.this);
            editView.setPositiveButton("Save",saveClickListener);
            editView.setNegativeButton("Cancel",null);

            switch (id){
                //Create Bio Change editView
                case R.id.button_settings_bio:
                    createEditView(R.id.button_settings_bio);
                    break;

                //Create Password Change editView
                case R.id.button_settings_twitter:
                    createEditView(R.id.button_settings_twitter);
                    break;
            }
        }
    };

    //Listener launches activities
    View.OnClickListener launchActivityClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vID = v.getId();

            switch (vID){
                case R.id.smartImage_settingsAct_profilePic:
                    Intent imageintent = new Intent(Intent.ACTION_PICK);
                    imageintent.setType("image/png");
                    startActivityForResult(imageintent,ConstantsLibrary.RESULT_GALLERY);
                    break;
            }
        }
    };

    DialogInterface.OnClickListener saveClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            //Get data from inflatedEditView - send data to parseSingleton to be saved
            switch (inflatedEditView.getTag().toString()){
                ////Profile picture save
                case ConstantsLibrary.TAG_SETTINGS_PROFILEPIC:
                    Intent convertPhotoIntent = new Intent(SettingsActivity.this, ImageCoverterService.class);
                    convertPhotoIntent.setAction(ConstantsLibrary.ACTION_SETTING_COVERT_PHOTO);
                    convertPhotoIntent.setData(imageURI);
                    startService(convertPhotoIntent);
                    break;

                ////Bio save
                case ConstantsLibrary.TAG_SETTINGS_BIO:
                    EditText bioEdit = (EditText) inflatedEditView.findViewById(R.id.editText_alert_bio);
                    mParseSingleton.editPorfile(
                            ParseUser.getCurrentUser(),
                            "bio",
                            bioEdit.getText().toString()
                    );
                    break;

                ////Email Save
                case ConstantsLibrary.TAG_SETTINGS_TWITTERNAME:
                    EditText twitterNameEdit = (EditText) inflatedEditView.findViewById(R.id.editText_settingsAlert_twitterName);
                    mParseSingleton.editPorfile(
                            ParseUser.getCurrentUser(),
                            "twitterName",
                            twitterNameEdit.getText().toString()
                    );
                    break;
            }
        }
    };

    private void populateUI(){
        ParseFile profilePicture = ParseUser.getCurrentUser().getParseFile("profileImage");
        if (profilePicture != null){
            profilePictureButton.setImageUrl(profilePicture.getUrl());
        }
    }

    private void createEditView(int viewID){
        //Base alert view
        AlertDialog.Builder editView = new AlertDialog.Builder(SettingsActivity.this);
        editView.setPositiveButton("Save",saveClickListener);
        editView.setNegativeButton("Cancel",null);

        switch (viewID){
            //Called from onActivityResult to confirm image save
            case R.id.smartImage_settingsAct_profilePic:
                //inflate / globalize view
                inflatedEditView = mLayoutInflater.inflate(R.layout.alert_edit_profilepic,null);
                inflatedEditView.setTag(ConstantsLibrary.TAG_SETTINGS_PROFILEPIC);
                //Populate data
                ((ImageView)inflatedEditView.findViewById(R.id.imageView_settingsAlert_profilPicDisplay)).setImageURI(imageURI);
                //finish alert
                editView.setTitle("Confirm Photo");
                break;

            //Create Bio Change editView
            case R.id.button_settings_bio:
                //inflate / globalize view
                inflatedEditView = mLayoutInflater.inflate(R.layout.alert_edit_bio,null);
                inflatedEditView.setTag(ConstantsLibrary.TAG_SETTINGS_BIO);
                //get data
                String bioText = ParseUser.getCurrentUser().getString("bio");
                //Populate data
                if (bioText != null) {
                    ((TextView) inflatedEditView.findViewById(R.id.editText_alert_bio))
                            .setText(bioText);
                }
                //finish alert
                editView.setTitle("Change Bio");
                break;

            //Create Twitter Change InputView
            case R.id.button_settings_twitter:
                //inflate / globalize view
                inflatedEditView = mLayoutInflater.inflate(R.layout.alert_edit_twittername,null);
                inflatedEditView.setTag(ConstantsLibrary.TAG_SETTINGS_TWITTERNAME);
                //get data
                 String twitterName = ParseUser.getCurrentUser().getString("twitterName");
                //Populate data
                if (twitterName != null) {
                    ((TextView) inflatedEditView.findViewById(R.id.editText_settingsAlert_twitterName))
                            .setText(twitterName);
                }
                //finish alert
                editView.setTitle("Edit Twitter Username");
                break;
        }
        editView.setView(inflatedEditView);
        editView.show();
    }
}