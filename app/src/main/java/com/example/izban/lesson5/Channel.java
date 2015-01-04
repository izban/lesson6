package com.example.izban.lesson5;

/**
 * Created by izban on 03.01.15.
 */
public class Channel {
    String link;

    Channel() {}
    Channel(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return link;
    }
}
