package com.example.lab6mobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class ReminderAdapter extends ArrayAdapter<Reminder> {

    public ReminderAdapter(Context context, List<Reminder> reminders) {
        super(context, 0, reminders);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        Reminder reminder = getItem(position);

        TextView titleView = convertView.findViewById(android.R.id.text1);
        TextView dateView = convertView.findViewById(android.R.id.text2);

        titleView.setText(reminder.getTitle());
        dateView.setText(reminder.getDate());

        return convertView;
    }
}