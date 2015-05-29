package mp.joshua.com.twitchkit.Fragments;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

import mp.joshua.com.twitchkit.Adapters.SupportPageAdapter;
import mp.joshua.com.twitchkit.CoverView;
import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;
import mp.joshua.com.twitchkit.DataProviders.DataPostOffice;
import mp.joshua.com.twitchkit.DataProviders.ParseSingleton;
import mp.joshua.com.twitchkit.R;

public class SupportPageFragment extends android.support.v4.app.Fragment{

    ListView supportLinkListView;
    ArrayList<Object> supportLinksArray = null;

    ParseSingleton mParseSingleton;
    DataPostOffice mDataPostOffice;
    CoverView mCoverView;
    RelativeLayout mViewHolder;

    FragmentManager fragmentManager;
    private String mCurrentActivity;
    private String ownerID;

    Bundle recievedArgs;
    Button continueButton;

    public static SupportPageFragment newInstance(String activityRequesting, String id){
        SupportPageFragment supportPageFragment = new SupportPageFragment();
        Bundle args = new Bundle();
        args.putString(ConstantsLibrary.ARG_CURRENT_ACTIVITY, activityRequesting);
        args.putString(ConstantsLibrary.ARG_PARSEUSER_ID, id);
        supportPageFragment.setArguments(args);
        return supportPageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mParseSingleton = ParseSingleton.getInstance(getActivity());
        mDataPostOffice = DataPostOffice.getInstance(getActivity());
        mCurrentActivity = getArguments().getString(ConstantsLibrary.ARG_CURRENT_ACTIVITY);
        ownerID = getArguments().getString(ConstantsLibrary.ARG_PARSEUSER_ID);
        fragmentManager = getFragmentManager();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantsLibrary.ACTION_GET_SUPPORTLINKS);
        intentFilter.addAction(ConstantsLibrary.ACTION_SUPPORTLINKS_CHANGED);
        intentFilter.addAction(ConstantsLibrary.ACTION_SUPPORTLINKS_NULL_INPUT);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
        pullSupportLinks(ownerID);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCoverView.removeCoverView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_frag_forms,menu);

        if (mCurrentActivity.equals(ConstantsLibrary.ARG_ACTIVITY_PROFILE)){
            menu.findItem(R.id.action_add).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add){
            showSupportLinkForm(true,0);
        }else if (id == R.id.action_refresh){
            pullSupportLinks(ownerID);
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_supportpage,container,false);
        mViewHolder = (RelativeLayout)v.findViewById(R.id.relative_supportFrag_viewHolder);
        continueButton = (Button)v.findViewById(R.id.supportFrag_continueToProfile);
        supportLinkListView = (ListView)v.findViewById(R.id.supportPage_Listview);

        supportLinkListView.setOnItemClickListener(onItemClickListener);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCoverView = new CoverView(getActivity(),mViewHolder);
        recievedArgs = getArguments();
        fragmentSetUp();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();

            if (intentAction.equals(ConstantsLibrary.ACTION_GET_SUPPORTLINKS)){
                supportLinksArray = mDataPostOffice.getSupportLinkArrayList();

            }else if(intentAction.equals(ConstantsLibrary.ACTION_SUPPORTLINKS_CHANGED)){
                pullSupportLinks(ParseUser.getCurrentUser().getObjectId());

            }else if(intentAction.equals(ConstantsLibrary.ACTION_SUPPORTLINKS_NULL_INPUT)){
                Toast.makeText(getActivity(),"Fields Title and Url must have content", Toast.LENGTH_LONG).show();
            };


            if(supportLinksArray.size() != 0){
                SupportPageAdapter adapter = new SupportPageAdapter(getActivity(),supportLinksArray);
                supportLinkListView.setAdapter(adapter);
                mCoverView.removeCoverView();
            }else{
                mCoverView.removeCoverView();
                mCoverView.createInstructionCover(ConstantsLibrary.CONST_SUPPORTPAGE_NULL_MESSAGE,mCurrentActivity);
            }
        }
    };

    public void pullSupportLinks(String ownerID){
        mParseSingleton.getSuportLinkList(ownerID);
    }

    private void fragmentSetUp(){
        mCoverView.createLoadingCover();
        if (recievedArgs.getString(ConstantsLibrary.ARG_CURRENT_ACTIVITY).equals(ConstantsLibrary.ARG_ACTIVITY_PROFILE)){
            continueButton.setOnClickListener(continueClickListener);

        }else if(recievedArgs.getString(ConstantsLibrary.ARG_CURRENT_ACTIVITY).equals(ConstantsLibrary.ARG_ACTIVITY_FORMS)){
            supportLinkListView.setOnItemLongClickListener(onItemLongClickListener);
            continueButton.setVisibility(View.GONE);
        }
    }

    AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            showSupportLinkForm(false,position);
            return true;
        }
    };

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String url = view.getTag().toString();
            Uri uri;
            if (!url.startsWith("http://") || !url.startsWith("https://")){
                uri = Uri.parse("http://" + url);
            }else{
                uri = Uri.parse(url);
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    };

    private void showSupportLinkForm(boolean isCreating, int position){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View alertFormView =  inflater.inflate(R.layout.alert_supportlinkform,null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("New support link")
                .setView(alertFormView);

        final EditText lTitle = (EditText)alertFormView.findViewById(R.id.editText_alert_linkTitle);
        final EditText lURL = (EditText)alertFormView.findViewById(R.id.editText_alert_linkUrl);
        final EditText lPicture = (EditText)alertFormView.findViewById(R.id.editText_alert_linkPic);

        //If creating new support link...
        if (isCreating) {
            //Alert Dialog has create button.
            builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    mParseSingleton.createSupportLink(
                            lTitle.getText().toString(),
                            lURL.getText().toString(),
                            lPicture.getText().toString());
                }
            });

        //If selected previously created support link...
        }else if (!isCreating){
            //Get Parseobject associated with table cell
            final ParseObject parseObject = (ParseObject)supportLinksArray.get(position);
            //Alert Dialog has save button for edited data.
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mParseSingleton.editSupportLink(
                            parseObject.getObjectId(),
                            lTitle.getText().toString(),
                            lURL.getText().toString(),
                            lPicture.getText().toString());

                }
            });
            //Alert Dialog has delete button.
            builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mParseSingleton.deleteSupportLink(parseObject.getObjectId());
                }
            });

            //Set EditText fields to objects data
            lTitle.setText(parseObject.getString("title"));
            lURL.setText(parseObject.getString("link"));
            lPicture.setText(parseObject.getString("image"));
        }
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    View.OnClickListener continueClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,ProfileFragment.newInstance(ownerID))
                    .commit();
        }
    };
}
