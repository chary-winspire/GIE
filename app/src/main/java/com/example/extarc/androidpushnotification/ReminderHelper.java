package com.example.extarc.androidpushnotification;

public class ReminderHelper {
    private int ToDoid;
    private String Title, Description, Date, Time;

    public ReminderHelper(int ToDoid, String title, String description, String date, String time){
        this.ToDoid = ToDoid;
        this.Title = title;
        this.Description = description;
        this.Date = date;
        this.Time = time;
    }

    public int getToDoid() {
        return ToDoid;
    }

    public void setToDoid(int toDoid) {
        ToDoid = toDoid;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
