package com.example.tarun.unmevents;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<Event> {

    private static class ViewHolder {
        TextView name;
        TextView date;
    }

    public CustomListAdapter(Activity context, List<Event> events) {
        super(context, R.layout.mylist, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Event event = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.mylist, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.item);
            viewHolder.date = (TextView) convertView.findViewById(R.id.textView1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.name.setText(event.getSummary());
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String d = "";
        if(event.getStartTime()!=null) {
            d = df.format(event.getStartTime());
        }
        viewHolder.date.setText(d);
        // Return the completed view to render on screen
        return convertView;
    }
}