package com.example.blablablub100.gemeinsameerinnerungen.experienceLogic;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.Date;

public class Memory implements Comparable<Memory> {

    private String name;
    private Date timestamp;
    private File content; //can be sound, vid, text, pic
    private String description;

    public Memory(String name, Date timestamp, File content, String description) {
        this.name = name;
        this.timestamp = timestamp;
        this.description = description;
        this.content = content;
    }

    @Override
    public int compareTo(@NonNull Memory e) {
        if (e.timestamp.before(timestamp)) {
            return -1;
        }
        return 1;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return timestamp;
    }

    public File getFile() {
        return content;
    }

    public String getDescription() {
        return description;
    }
}
