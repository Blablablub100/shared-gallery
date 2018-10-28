package com.example.blablablub100.gemeinsameerinnerungen.ExperienceParser;

import com.example.blablablub100.gemeinsameerinnerungen.experienceLogic.Experience;
import com.example.blablablub100.gemeinsameerinnerungen.experienceLogic.Memory;
import com.example.blablablub100.gemeinsameerinnerungen.util.FileUtil;
import com.example.blablablub100.gemeinsameerinnerungen.util.ReadWriter;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExperienceReader {

    public static List<Experience> readExperiences() {
        List<Experience> experiences = new ArrayList<Experience>();
        File[] files = FileUtil.listeDirectoryFiles();
        if (files == null || files.length == 0) return experiences;
        for (File file: files) {
            if (file.isDirectory()) {
                experiences.add(generateExperience(file));
            }
        }
        return experiences;
    }

    private static Experience generateExperience(File f) {
        String dirname = f.getAbsolutePath().substring(
                f.getAbsolutePath().lastIndexOf("/")
        );
        dirname = dirname.replaceAll("/", "");
        String[] ExperienceData = dirname.split("_");
        try {
            Date startdate = Config.df.parse(ExperienceData[0]);
            Date enddate = Config.df.parse(ExperienceData[1]);
            String name = ExperienceData[2];
            String description = ReadWriter.readFile(f.getAbsolutePath()+"/description");
            return new Experience(startdate, enddate, name, description);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static void readMemories(Experience exp) {
        List<Memory> memories = new ArrayList<Memory>();
        File dir = new File(FileUtil.getBasedir() + "/" + Config.generateFolderName(exp));
        File[] memoryFiles = dir.listFiles();
        if (memoryFiles == null) return;
        for (File file: memoryFiles) {
            if (!file.getAbsolutePath().endsWith("description.txt")) {
                Memory mem = Config.generateMemoryData(file, Config.generateDescriptionFile(file));
                if (mem != null) {
                    memories.add(mem);
                }
            }
        }
        exp.setMemories(memories);
    }


}
