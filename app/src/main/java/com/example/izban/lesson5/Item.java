package com.example.izban.lesson5;

/**
 * Created by izban on 20.10.14.
 */
public class Item {
    String link;
    String title;
    String description;
    String channel;
    Long time;

    Item() {}
    Item(String link, String title, String description, String channel, Long time) {
        this.link = link;
        this.title = title;
        this.description = description;
        this.channel = channel;
        this.time = time;
    }

    public String toString() {
        return title + "\n" + description;
    }
}
