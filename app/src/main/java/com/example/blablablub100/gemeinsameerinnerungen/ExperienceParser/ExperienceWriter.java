package com.example.blablablub100.gemeinsameerinnerungen.ExperienceParser;

import com.example.blablablub100.gemeinsameerinnerungen.experienceLogic.Experience;
import com.example.blablablub100.gemeinsameerinnerungen.util.FileUtil;
import com.example.blablablub100.gemeinsameerinnerungen.util.ReadWriter;

import java.io.File;
import java.util.Date;

public class ExperienceWriter {

    public static void writeExperience(Experience e) {
        String descriptionFile = Config.generateFolderName(e) + "/description.txt";
        FileUtil.createFile(descriptionFile, e.getDescription());
    }

    public static void writeDescription(String descr, File contentFile) {
        File descrFile = Config.generateDescriptionFile(contentFile);
        FileUtil.createFile(descrFile.getAbsolutePath(), descr);
    }

    public static void writeTextMemory(String name, Date date, String text, String path) {
        String filename = Config.df.format(date) + "_" + name + ".txt";
        FileUtil.createFile(path + filename, text);
    }



}
