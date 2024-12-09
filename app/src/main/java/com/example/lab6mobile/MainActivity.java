package com.example.lab6mobile;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText etTitle, etText;
    private Button btnSave;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTitle = findViewById(R.id.etTitle);
        etText = findViewById(R.id.etText);
        EditText etDate = findViewById(R.id.etDate);
        etDate.setOnClickListener(v -> {
            // Получаем текущее значение даты
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Создаем DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year1, month1, dayOfMonth) -> {
                        // Форматируем выбранную дату в строку
                        String date = String.format("%02d-%02d-%04d", dayOfMonth, month1 + 1, year1);
                        etDate.setText(date);  // Устанавливаем выбранную дату в EditText
                    },
                    year, month, day);

            datePickerDialog.show();
        });
        EditText etTime = findViewById(R.id.etTime);
        etTime.setOnClickListener(v -> {
            // Получаем текущее время
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minute = Calendar.getInstance().get(Calendar.MINUTE);

            // Создаем TimePickerDialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (view, hourOfDay, minute1) -> {
                        // Форматируем выбранное время в строку HH:mm
                        String time = String.format("%02d:%02d", hourOfDay, minute1);
                        etTime.setText(time);  // Устанавливаем выбранное время в EditText
                    },
                    hour, minute, true);

            timePickerDialog.show();
        });

        btnSave = findViewById(R.id.btnSave);

        dbHelper = new DatabaseHelper(this);

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String text = etText.getText().toString();
            String dateString = etDate.getText().toString();
            String timeString = etTime.getText().toString();

            if (title.isEmpty() || text.isEmpty() || dateString.isEmpty() || timeString.isEmpty()) {
                Toast.makeText(MainActivity.this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            } else {
                // Сохраняем напоминание в базу данных
                long result = dbHelper.addReminder(title, text, dateString, timeString);

                if (result != -1) {
                    Toast.makeText(MainActivity.this, "Напоминание сохранено", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Ошибка при сохранении", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnViewReminders = findViewById(R.id.btnViewReminders);
        btnViewReminders.setOnClickListener(v -> {
            // Открываем активити со списком напоминаний
            Intent intent = new Intent(MainActivity.this, ReminderListActivity.class);
            startActivity(intent);
        });
    }
}