package com.example.izban.lesson5;

/**
 * Created by izban on 20.10.14.
 */
public class Item {
    String link;
    String title;
    String description;

    Item() {}
    Item(String link, String title, String description) {
        this.link = link;
        this.title = title;
        this.description = description;
    }

    public String toString() {
        return title + "\n" + description;
    }
}
