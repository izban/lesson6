package com.example.izban.lesson5;

/**
 * Created by izban on 03.01.15.
 */
public class Channel {
    String title;
    String link;

    Channel() {}
    Channel(String title, String link) {
        this.title = title;
        this.link = link;
    }

    @Override
    public String toString() {
        return title;
    }
}
