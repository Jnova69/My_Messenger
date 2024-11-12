package com.example.mymessenger;

public class NewsItem {
    private String title;
    private String description;
    private String date;
    private String source;

    public NewsItem(String title, String description, String date, String source) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.source = source;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getSource() { return source; }
}
