package com.example.blablablub100.gemeinsameerinnerungen.experienceLogic;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.Date;

public class Memory implements Comparable<Memory> {

    private String name;
    private Date timestamp;
    private File content; //can be sound, vid, text, pic
    private String description;
    private int fileType; // 0-TEXT, 1-PIC, 2-VID, 3-AUDIO

    public Memory(String name, Date timestamp, File content, String description) {
        this.name = name;
        this.timestamp = timestamp;
        this.description = description;
        this.content = content;
        this.fileType = calcFileType(content);
    }

    private int calcFileType(File file) {
        String ending = file.getAbsolutePath()
                .substring(file.getAbsolutePath()
                        .lastIndexOf(".")+1);
        final String[] audFileExtensions =  new String[] {"mp3"};
        final String[] picFileExtensions =  new String[] {"jpg", "png", "gif","jpeg", "gif"};
        final String[] vidFileExtensions =  new String[] {"3pg", "mp4", "mkv","webm"};
        final String[] txtFileExtensions =  new String[] {"txt"};
        for (String ex: picFileExtensions) {
            if (ending.equals(ex)) return 1;
        }
        for (String ex: txtFileExtensions) {
            if (ending.equals(ex)) return 0;
        }
        for (String ex: vidFileExtensions) {
            if (ending.equals(ex)) return 2;
        }
        for (String ex: audFileExtensions) {
            if (ending.equals(ex)) return 3;
        }
        return 4;
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

    public int getType() {
        return fileType;
    }
}
