package com.example.blablablub100.gemeinsameerinnerungen.util;

import android.os.Environment;

import com.example.blablablub100.gemeinsameerinnerungen.ExperienceParser.Config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileUtil {

    private static String basepathName = "/shared_experiences/";
    private static File basedir = new File(Environment.getExternalStorageDirectory() + basepathName);

    public static boolean createFile(String path, String input) {
        if (!isExternalStorageWritable()) return false;
        File f = null;
        if (!path.startsWith(basedir.getAbsolutePath())) {
            f = new File(basedir + "/" + path);
        } else {
            f = new File(path);
        }
        f.getParentFile().mkdirs();
        FileWriter writer = null;
        try {
            f.createNewFile();
            writer = new FileWriter(f);
            writer.append(input);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static File[] listDirectoryFiles() {
        return basedir.listFiles();
    }

    /* Checks if external storage is available for read and write */
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    private static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getBasedir() {
        return basedir;
    }

    public static Date getDateFromCamera(String filename) {
        Date res = null;
        if (filename.length() != 19) {
            try {
                res = Config.camdf.parse(filename.substring(4, 19));
            } catch(Exception e) {

            }
        }
        return res;
    }

    public static List<File> listAllFiles(String directoryName, List<File> files) {
        File directory = new File(directoryName);
        List<File> res = new ArrayList<>();

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if (fList != null) {
            for (File file : fList) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    res.addAll(listAllFiles(file.getAbsolutePath(), res));
                }
            }
        }
        return res;
    }


}
