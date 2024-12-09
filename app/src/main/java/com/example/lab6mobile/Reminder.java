package com.example.lab6mobile;

public class Reminder {
    private long id;  // Убедитесь, что ID правильно инициализируется
    private String title;
    private String text;
    private String date;
    private String time;

    // Конструктор с ID
    public Reminder(long id, String title, String text, String date, String time) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.date = date;
        this.time = time;
    }

    // Геттеры
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
