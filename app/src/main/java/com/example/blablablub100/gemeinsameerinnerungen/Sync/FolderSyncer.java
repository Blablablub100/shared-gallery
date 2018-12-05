package com.example.blablablub100.gemeinsameerinnerungen.Sync;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderBuilder;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.example.blablablub100.gemeinsameerinnerungen.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FolderSyncer {

    private static final String ACCESS_TOKEN =
       "zi";

    static DbxRequestConfig config =
            DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
    static DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

    private static List<String> dbxFiles;
    private static List<String> localFiles;
    private static List<File> localFilesPath;


    public static void initDropbox(TextView dbxNameTextView, TextView dbxGallerySize) {
        new DropboxShowAccountTask(dbxNameTextView, client).execute();
        new DropboxListFilesTask(client, dbxGallerySize).execute();
    }

    public static void fullSync(Context context, TextView progress) {
        // TODO FULLSYNCPROCES
        // TODO =============
        // TODO download all txts in temp folder
        // TODO compare every txt with local corresponding txt
        // TODO if txt does not exist in local -> copy to local
        // TODO if txt in local is not in temp -> upload txt
        // TODO if txt in temp equals local -> do nothing
        // TODO if txt in temp does not match local -> CONFLICT
        //
        // TODO delete temp
        // TODO QUICKSYNC


    }


    // only checks for new files etc no content checking
    public static void quickSync(final Context context, final TextView progress) {

        progress.setText("listing local files");

        localFilesPath = FileUtil.listAllFiles(
                FileUtil.getBasedir().getAbsolutePath(), new ArrayList<File>());

        localFiles = new ArrayList<>();

        for (int i = 0; i < localFilesPath.size(); i++) {
            String tmp = localFilesPath.get(i).getAbsolutePath()
                    .replace(FileUtil.getBasedir().getAbsolutePath(), "");
            localFiles.add(tmp);
        }

        // files to upload:
        progress.setText("searching files to upload");
        List<String> filesToUpload = new ArrayList<>();
        for (int i = 0; i < localFiles.size(); i++) {
            String tmp = localFiles.get(i);
            if (!containsCaseInsensitive(tmp, dbxFiles)) {
                filesToUpload.add(tmp);
            }
        }

        // files to download
        progress.setText("searching files to download");
        List<String> filesToDownload = new ArrayList<>();
        for (int i = 0; i < dbxFiles.size(); i++) {
            String tmp = dbxFiles.get(i);
            if (!containsCaseInsensitive(tmp, localFiles)) {
                filesToDownload.add(tmp);
            }
        }


        new DropboxQuickSyncTask
                (filesToUpload, filesToDownload, client, context, progress).execute();

    }


    private static boolean containsCaseInsensitive(String strToCompare, List<String>list) {
        for(String str:list) {
            if(str.equalsIgnoreCase(strToCompare)) {
                return(true);
            }
        }
        return(false);
    }


    protected static void setDbxFiles(List<String> dbx_Files) {
        dbxFiles = dbx_Files;
    }


    private static void uploadFiles(DbxClientV2 dbxClient
            , List<String> localFiles, Context context, TextView prog) {
        new DropboxUploadTask(dbxClient, localFiles, context, prog, 0).execute();
    }

    private  static void downloadFiles(DbxClientV2 dbxClient
            , List<String> dbxFiles, Context context, TextView prog) {
        new DropboxDownloadTask(dbxClient, localFiles, context, prog, 0).execute();
    }
}
