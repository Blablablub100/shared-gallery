package com.example.blablablub100.gemeinsameerinnerungen.experienceLogic;

import android.support.annotation.NonNull;

import com.example.blablablub100.gemeinsameerinnerungen.ExperienceParser.Config;
import com.example.blablablub100.gemeinsameerinnerungen.ExperienceParser.ExperienceWriter;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Experience implements Comparable<Experience> {
    private Date startDate;
    private Date endDate;
    private String name;
    private String description;
    private List<Memory> memories;


    public Experience(Date startDate, Date endDate, String name, String description) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.description = description;
    }

    public void write() {
        ExperienceWriter.writeExperience(this);
    }

    public void addMemory (String name, Date timestamp, File content, String description) {
        memories.add(new Memory(name, timestamp, content, description));
        Collections.sort(memories);
    }

    public void addMemory(Memory memory) {
        memories.add(memory);
        Collections.sort(memories);
    }

    public void setMemories(List<Memory> memories) {
        this.memories = memories;
        Collections.sort(memories);
    }

    @Override
    public int compareTo(@NonNull Experience e) {
        if (e.startDate.before(startDate)) {
            return -1;
        }
        return 1;
    }

    //used for ListView
    public String toString() {
        return (name
                + "\n"
                + Config.printdf.format(startDate)
                + " - "
                + Config.printdf.format(endDate));
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Memory> getMemories() {return memories;}
}
