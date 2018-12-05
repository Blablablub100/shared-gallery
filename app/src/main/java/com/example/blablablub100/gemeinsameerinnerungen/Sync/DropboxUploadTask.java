package com.example.blablablub100.gemeinsameerinnerungen.Sync;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.files.WriteMode;
import com.example.blablablub100.gemeinsameerinnerungen.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class DropboxUploadTask extends AsyncTask<String, Void, String> {

    DbxClientV2 client;
    List<String> localFiles;
    Context context;
    TextView progress;
    int index;
    AsyncTask<String, Void, String> nextTask;

    public DropboxUploadTask(DbxClientV2 client
            , List<String> localFiles
            , Context ctx
            , TextView progress
            , int index) {
        this.client = client;
        this.localFiles = localFiles;
        this.context = context;
        this.progress = progress;
        this.index = index;
        nextTask = null;
    }

    public DropboxUploadTask(DbxClientV2 client
            , List<String> localFiles
            , Context ctx
            , TextView progress
            , int index
            , AsyncTask<String, Void, String> nextTask) {
        this.client = client;
        this.localFiles = localFiles;
        this.context = context;
        this.progress = progress;
        this.index = index;
        this.nextTask = nextTask;
    }

    @Override
    protected String doInBackground(String[] objects) {

        if (localFiles.size() < 1) return "";

        File localFile = new File(FileUtil.getBasedir().getAbsolutePath() + localFiles.get(index));
        String dropboxPath = localFiles.get(index);


        String str = index + "/ "+ localFiles.size() + " [UPLOAD] " + localFile;
        try (InputStream in = new FileInputStream(localFile)) {

            FileMetadata metadata = client.files().uploadBuilder(dropboxPath)
                    .withMode(WriteMode.ADD)
                    .withClientModified(new Date(localFile.lastModified()))
                    .uploadAndFinish(in);

            System.out.println(metadata.toStringMultiline());
        } catch (UploadErrorException ex) {
            str = ("Error uploading: " + ex.getMessage());
        } catch (DbxException ex) {
            str = ("Error uploading: " + ex.getMessage());
        } catch (IOException ex) {
            str = ("Error reading from file \"" + localFile + "\": " + ex.getMessage());
        }
        return str;
    }

    protected void onPostExecute(String str) {
        progress.setText(str);
        if (index+1 < localFiles.size()) {
            progress.setText((index+1) + "/ "+ localFiles.size() + " [UPLOAD] " + localFiles.get(index+1));
            new DropboxUploadTask(client, localFiles, context, progress, index + 1, nextTask).execute();
        } else {
            progress.setText("DONE UPLOADING");
            if (nextTask != null) {
                nextTask.execute();
            }
        }
    }
}
