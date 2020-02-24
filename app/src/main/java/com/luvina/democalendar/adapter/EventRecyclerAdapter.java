package com.luvina.democalendar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luvina.democalendar.R;
import com.luvina.democalendar.model.EventModel;
import com.luvina.democalendar.utils.Common;

import java.util.List;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {
    private List<EventModel> listEvent;
    private OnItemClickedListener onItemClickedListener;

    public EventRecyclerAdapter(OnItemClickedListener listener, List<EventModel> listEvent) {
        this.listEvent = listEvent;
        this.onItemClickedListener = listener;
    }

    @NonNull
    @Override
    public EventRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_event, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventRecyclerAdapter.ViewHolder holder, int position) {
        EventModel event = listEvent.get(position);
        String eventName = event.getName();
        String eventNameTemp = "";
        if (eventName.length() > 8) {
            eventNameTemp = eventName.substring(0, 6) + "...";
        } else {
            eventNameTemp = eventName;
        }
        holder.eventName.setText(eventNameTemp);
        holder.startTime.setText(Common.getTimeFromDate(event.getStartDate()));
        holder.endTime.setText(Common.getTimeFromDate(event.getEndDate()));
    }

    @Override
    public int getItemCount() {
        return listEvent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, startTime, endTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickedListener.onItemClicked(getPosition());
                }
            });
        }
    }

    public void setListEvent(List<EventModel> listEvent) {
        this.listEvent = listEvent;
    }

    public interface OnItemClickedListener {
        void onItemClicked(int position);
    }
}
