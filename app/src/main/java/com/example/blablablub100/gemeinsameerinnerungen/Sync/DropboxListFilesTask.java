package com.example.blablablub100.gemeinsameerinnerungen.Sync;

import android.os.AsyncTask;
import android.widget.TextView;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderBuilder;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DropboxListFilesTask extends AsyncTask<String, Void, List<String>> {

    DbxClientV2 client;
    TextView dbxGallerySizeTextView;

    public DropboxListFilesTask(DbxClientV2 client, TextView dbxGallerySizeTextView) {
        this.client = client;
        this.dbxGallerySizeTextView = dbxGallerySizeTextView;
    }

    @Override
    protected List<String> doInBackground(String[] objects) {

        List<String> res = new ArrayList<String>();

        try {

            ListFolderBuilder listFolderBuilder = client.files().listFolderBuilder("");
            ListFolderResult result = listFolderBuilder.withRecursive(true).start();

            Logger log = Logger.getLogger("thread");
            log.setLevel(Level.INFO);

            while (true) {

                if (result != null) {
                    for (Metadata entry : result.getEntries()) {
                        if (entry instanceof FileMetadata) {
                            res.add(entry.getPathLower());
                        }
                    }

                    if (!result.getHasMore()) {
                        log.info("GET LATEST CURSOR");
                        //return result.getCursor();
                        break;
                    }

                    try {
                        result = client.files().listFolderContinue(result.getCursor());
                    } catch (DbxException e) {
                        log.info("Couldn't get listFolderContinue");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    protected void onPostExecute(List<String> temp) {
        FolderSyncer.setDbxFiles(temp);
        dbxGallerySizeTextView.setText("Connected to shared gallery with "+temp.size()+" files");
    }
}
