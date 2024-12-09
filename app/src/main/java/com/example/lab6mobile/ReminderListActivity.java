package com.example.lab6mobile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReminderListActivity extends AppCompatActivity {
    private ListView lvReminders;
    private ArrayList<String> remindersList;
    private DatabaseHelper dbHelper;
    private ArrayList<Reminder> reminderList;  // Список напоминаний
    private ArrayAdapter<String> adapter;  // Адаптер для отображения списка

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list);

        lvReminders = findViewById(R.id.lvReminders);
        dbHelper = new DatabaseHelper(this);

        // Загружаем список напоминаний из базы данных
        reminderList = dbHelper.getAllReminders(); // Получаем все напоминания из базы данных
        remindersList = new ArrayList<>();

        // Формируем список строк для отображения в ListView
        for (Reminder reminder : reminderList) {
            String reminderText = "Заголовок: " + reminder.getTitle() + "\n" +
                    "Описание: " + reminder.getText() + "\n" +
                    "Дата: " + reminder.getDate() + "\n" +
                    "Время: " + reminder.getTime() + "\n";
            remindersList.add(reminderText);
        }

        // Создаем адаптер для отображения напоминаний в ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, remindersList);
        lvReminders.setAdapter(adapter);

        // Обработчик нажатия на элемент списка
        lvReminders.setOnItemClickListener((parent, view, position, id) -> {
            // Получаем выбранное напоминание
            Reminder selectedReminder = reminderList.get(position);

            // Создаем диалог подтверждения удаления
            new AlertDialog.Builder(ReminderListActivity.this)
                    .setTitle("Удалить напоминание")
                    .setMessage("Вы уверены, что хотите удалить это напоминание?")
                    .setPositiveButton("Да", (dialog, which) -> {
                        // Удаляем напоминание из базы данных
                        dbHelper.deleteReminder(selectedReminder.getId()); // Удаляем по правильному ID

                        // Обновляем список
                        reminderList.remove(position);  // Удаляем напоминание из списка объектов
                        remindersList.remove(position); // Удаляем строку из списка строк
                        adapter.notifyDataSetChanged(); // Обновляем адаптер

                        // Показываем сообщение об удалении
                        Toast.makeText(ReminderListActivity.this, "Напоминание удалено", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
            scheduleNotification(selectedReminder);
        });
    }

    // Метод для добавления нового напоминания
    private void scheduleNotification(Reminder reminder) {
        // Разбиваем дату и время на компоненты
        String[] dateParts = reminder.getDate().split("-");
        String[] timeParts = reminder.getTime().split(":");

        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]) - 1; // Месяцы начинаются с 0
        int day = Integer.parseInt(dateParts[2]);

        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        // Устанавливаем календарь на указанную дату и время
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, 0);

        // Создаем Intent для уведомления
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("title", reminder.getTitle());
        intent.putExtra("text", reminder.getText());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) reminder.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Планируем AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Напоминание установлено", Toast.LENGTH_SHORT).show();
        }
    }
}