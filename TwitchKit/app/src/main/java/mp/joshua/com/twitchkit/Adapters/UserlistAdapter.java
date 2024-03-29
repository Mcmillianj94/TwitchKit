package mp.joshua.com.twitchkit.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import mp.joshua.com.twitchkit.Activities.ProfileActivity;
import mp.joshua.com.twitchkit.DataProviders.ConstantsLibrary;
import mp.joshua.com.twitchkit.R;


public class UserlistAdapter extends BaseExpandableListAdapter {

    public ArrayList<String> groupItem;

    public ArrayList<Object> childitem;

    public LayoutInflater minflater;
    public Activity activity;
    public Context mContext;

    public UserlistAdapter(Context context, ArrayList<String> grList, ArrayList<Object> childItem) {
        mContext = context;
        groupItem = grList;
        childitem = childItem;
    }

    public void setInflater(LayoutInflater mInflater, Activity act) {
        this.minflater = mInflater;
        activity = act;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        List tempChild = ((List) childitem.get(groupPosition));
        TextView text = null;

        final ParseUser currentChild = (ParseUser)tempChild.get(childPosition);

        if (convertView == null) {
            convertView = minflater.inflate(R.layout.tableview_child_userlist, null);
        }
        text = (TextView) convertView.findViewById(R.id.textView1);
        text.setText(currentChild.getUsername());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra(ConstantsLibrary.EXTRA_PARSEUSER_ID, currentChild.getObjectId());
                intent.putExtra(ConstantsLibrary.EXTRA_FOLLOWING_STATUS, groupPosition);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return ((List) childitem.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return groupItem.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.tableview_group_userlist, null);
        }
        ((CheckedTextView) convertView).setText(groupItem.get(groupPosition));
        ((CheckedTextView) convertView).setChecked(isExpanded);
        ((ExpandableListView)parent).expandGroup(groupPosition);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
