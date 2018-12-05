package com.example.blablablub100.gemeinsameerinnerungen.Sync;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.example.blablablub100.gemeinsameerinnerungen.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class DropboxDownloadTask extends AsyncTask<String, Void, String> {

    DbxClientV2 client;
    List<String> dbxFiles;
    Context context;
    TextView progress;
    int index;

    public DropboxDownloadTask(DbxClientV2 client
            , List<String> dbxFiles
            , Context ctx
            , TextView progress
            , int index) {
        this.client = client;
        this.dbxFiles = dbxFiles;
        this.context = context;
        this.progress = progress;
        this.index = index;
    }

    @Override
    protected String doInBackground(String... strings) {
        String dbxFile = dbxFiles.get(index);
        String localFile = FileUtil.getBasedir() + dbxFile;

        String str = index + "/ "+ dbxFiles.size() + " [DOWNLOAD] " + dbxFile;
        try {
            //output file for download --> storage location on local system to download file
            new File(localFile).getParentFile().mkdirs();
            OutputStream downloadFile = new FileOutputStream(localFile);
            try {
                FileMetadata metadata = client.files().downloadBuilder(dbxFile)
                        .download(downloadFile);
            }
            finally {
                downloadFile.close();
            }
        } catch (DbxException e) {
            //error downloading file
            str = "Error downloading: " + e.getMessage();
        } catch (IOException e) {
            //error downloading file
            str = "Error writing to storage: " + e.getMessage();
        }
        return str;
    }


    protected void onPostExecute(String str) {
        progress.setText(str);
        if (index+1 < dbxFiles.size()) {
            progress.setText((index+1) + "/ "+ dbxFiles.size() + " [Download] " + FileUtil.getBasedir() + dbxFiles.get(index+1));
            new DropboxDownloadTask(client, dbxFiles, context, progress, index + 1).execute();
        } else {
            progress.setText("DONE DOWNLOADING");
        }
    }
}
