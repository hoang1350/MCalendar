package com.luvina.democalendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.luvina.democalendar.R;
import com.luvina.democalendar.model.EventModel;
import com.luvina.democalendar.utils.Common;

import java.util.List;

/**
 * Class customizing an adapter
 */
public class CustomAdapter extends ArrayAdapter<EventModel> {
    // Context
    private Context context;
    // List event
    private List<EventModel> listEvent;

    /**
     * Constructor of a custom adapter
     *
     * @param context:   context that displays a list view
     * @param resource:  layout of a custom adapter
     * @param listEvent: list event
     * @author HoangNN
     */
    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<EventModel> listEvent) {
        super(context, resource, listEvent);
        this.context = context;
        this.listEvent = listEvent;
    }

    /**
     * Create line data that an adapter contains
     *
     * @param position:    line position
     * @param convertView: ConvertView
     * @param parent:      ViewGroup
     * @return: a view to display
     * @author HoangNN
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        // If convertView hasn't been initialized
        if (convertView == null) {
            // Initialize a convertView
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_event, parent, false);
            // Initialize a ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.eventName = convertView.findViewById(R.id.eventName);
            viewHolder.startTime = convertView.findViewById(R.id.startTime);
            viewHolder.endTime = convertView.findViewById(R.id.endTime);
            // Save ViewHolder by tag
            convertView.setTag(viewHolder);
            // If convertView already be initialized
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Set value for each attribute of view holder
        EventModel event = listEvent.get(position);
        viewHolder.eventName.setText(event.getName());
        viewHolder.startTime.setText(Common.convertToTime(event.getStartDate()));
        viewHolder.endTime.setText(Common.convertToTime(event.getEndDate()));
        return convertView;
    }

    /**
     * A view holder contains the value that will be display on an adapter
     */
    public class ViewHolder {
        TextView eventName, startTime, endTime;
    }
}
