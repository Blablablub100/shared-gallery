package com.example.blablablub100.gemeinsameerinnerungen.ExperienceParser;

import com.example.blablablub100.gemeinsameerinnerungen.experienceLogic.Experience;
import com.example.blablablub100.gemeinsameerinnerungen.experienceLogic.Experiences;
import com.example.blablablub100.gemeinsameerinnerungen.experienceLogic.Memory;
import com.example.blablablub100.gemeinsameerinnerungen.util.FileUtil;
import com.example.blablablub100.gemeinsameerinnerungen.util.ReadWriter;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Config {

    public static Experiences experiences = null;

    public final static DateFormat df = new SimpleDateFormat("yyyyMMdd~HHmmss");
    public final static DateFormat printdf = new SimpleDateFormat("dd.MM.yyyy");
    public final static DateFormat camdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

    protected static String generateFolderName(Experience e) {
        String res = "";
        res = res + Config.df.format(e.getStartDate());
        res = res + "_";
        res = res + Config.df.format(e.getEndDate());
        res = res + "_";
        res = res + e.getName();
        return res;
    }

    public static void initExperiences() {
        experiences = new Experiences();
    }

    public static String generateFullPath(Experience e) {
        return FileUtil.getBasedir() + "/" + generateFolderName(e) + "/";
    }

    protected static Memory generateMemoryData(File content, File description) {
        String string = content.getName();
        string = string.replaceAll("/", "");
        String[] data = string.split("_");
        String descr = null;
        if (description.exists()) {
            descr = ReadWriter.readFile(description.getAbsolutePath());
        }
        Date date = null;
        try {
            date = df.parse(data[0]);
            return new Memory(data[1], date, content, descr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static File generateDescriptionFile(File file) {
        String string = file.getAbsolutePath();
        string = string.substring(0, string.lastIndexOf("."));
        string = string + "_description.txt";
        return new File(string);
    }
}
