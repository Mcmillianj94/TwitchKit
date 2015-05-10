package mp.joshua.com.twitchkit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;

import mp.joshua.com.twitchkit.R;

public class SupportPageAdapter extends BaseAdapter {

    public static final long CONST_ID = 0x0101;
    public ArrayList<Object> supportLinksArray;
    public Context mContext;

    public SupportPageAdapter (Context context, ArrayList<Object> arrayList) {
        mContext = context;
        supportLinksArray = arrayList;
    }

    @Override
    public int getCount() {
        return supportLinksArray.size();
    }

    @Override
    public Object getItem(int position) {
        return supportLinksArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return CONST_ID + position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.tableview_group_supportpage,parent,false);
        }
        ParseObject current = (ParseObject)supportLinksArray.get(position);

        convertView.setTag(current.get("link"));
        ((TextView)convertView.findViewById(R.id.tableView_GroupTitle_SupportPage)).setText(current.getString("title"));
        return convertView;
    }
}
