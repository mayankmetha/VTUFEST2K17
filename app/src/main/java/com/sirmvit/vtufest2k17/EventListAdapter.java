package com.sirmvit.vtufest2k17;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private List<EventItem> data = Collections.emptyList();

    // create constructor to init context and data sent from DetailActivity
    EventListAdapter(Context context, List<EventItem> data) {
        mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.event_card,parent,false);
        return new MyHolder(view);
    }

    // Bind data
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        EventItem current = data.get(position);
        myHolder.eventName.setText(current.name);
        myHolder.eventDay.setText("Day : " + current.day);
        myHolder.eventTime.setText("Time : "+current.time);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

class MyHolder extends RecyclerView.ViewHolder {
    TextView eventName;
    TextView eventDay;
    TextView eventTime;

    public MyHolder(View itemView) {
        super(itemView);
        eventName = (TextView) itemView.findViewById(R.id.EventTitle);
        eventDay = (TextView) itemView.findViewById(R.id.EventDay);
        eventTime = (TextView) itemView.findViewById(R.id.EventTime);
    }
}
