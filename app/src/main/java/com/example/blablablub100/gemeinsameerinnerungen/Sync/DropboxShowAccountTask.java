package com.example.blablablub100.gemeinsameerinnerungen.Sync;

import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderBuilder;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DropboxShowAccountTask extends AsyncTask<String, Void, String> {

    private TextView t;
    DbxClientV2 client;

    public DropboxShowAccountTask(TextView t, DbxClientV2 client) {
        this.t = t;
        this.client = client;
    }


    @Override
    protected String doInBackground(String[] objects) {


        // just prints out my name to console for testing
        String dbxname;
        try {
            FullAccount account = client.users().getCurrentAccount();
             dbxname = account.getName().getDisplayName();
        } catch (DbxException e) {
            dbxname = ((e.getMessage()));
        }

        return dbxname;
    }


    protected void onPostExecute(String temp) {
        t.setText("Connection to account \""+temp+"\" established");
    }
}
