package com.example.lab6mobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");

        // Создаем уведомление
        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.sendNotification(title, text);
    }
}