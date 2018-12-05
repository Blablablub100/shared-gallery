package com.example.blablablub100.gemeinsameerinnerungen.Sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.dropbox.core.v2.DbxClientV2;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DropboxQuickSyncTask extends AsyncTask<String, Void, Void> {

    List<String> filesToUpload;
    List<String> filesToDownload;
    DbxClientV2 client;
    Context context;
    TextView progress;

    public DropboxQuickSyncTask(List<String> filesToUpload
            , List<String> filesToDownload
            , DbxClientV2 client
            , Context context
            , TextView progress) {
        this.filesToUpload = filesToUpload;
        this.filesToDownload = filesToDownload;
        this.client = client;
        this.context = context;
        this.progress = progress;
    }


    @Override
    protected Void doInBackground(String... strings) {
        DropboxDownloadTask ddt = null;
        if (filesToDownload.size() > 0) {
            ddt = new DropboxDownloadTask(client, filesToDownload, context, progress, 0);
        }

        DropboxUploadTask dut =
                new DropboxUploadTask(client, filesToUpload, context, progress, 0, ddt);
        dut.execute();
        return null;
    }
}
