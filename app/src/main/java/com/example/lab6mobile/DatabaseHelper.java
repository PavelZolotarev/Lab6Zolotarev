package com.example.lab6mobile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "reminder_db";
    private static final int DATABASE_VERSION = 8;
    private Context context;

    // Название таблицы и колонки
    public static final String TABLE_REMINDERS = "reminders";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REMINDERS_TABLE = "CREATE TABLE " + TABLE_REMINDERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_TEXT + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_TIME + " INTEGER"
                + ")";
        db.execSQL(CREATE_REMINDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        onCreate(db);
    }

    // Метод для добавления напоминания
    public long addReminder(String title, String text, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_TEXT, text);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);

        long id = db.insert(TABLE_REMINDERS, null, values); // ID присваивается автоматически
        db.close();

        //setReminder(id, title, text, date, time);

        // Логируем ID для отладки
        Log.d("DatabaseHelper", "Reminder added with ID: " + id);

        return id; // Возвращаем сгенерированный ID
    }

    // Метод для получения всех напоминаний
    public ArrayList<Reminder> getAllReminders() {
        ArrayList<Reminder> reminders = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_REMINDERS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));

                String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                String text = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));

                reminders.add(new Reminder(id, title, text, date, time));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return reminders;
    }

    public void deleteReminder(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Логируем ID, чтобы убедиться, что он передается правильно
        Log.d("DatabaseHelper", "Deleting reminder with ID: " + id);

        // Удаляем напоминание по ID
        int rowsDeleted = db.delete(TABLE_REMINDERS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

        // Логируем количество удаленных строк
        Log.d("DatabaseHelper", "Rows deleted: " + rowsDeleted);

        db.close();
    }
}