package com.example.blablablub100.gemeinsameerinnerungen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.blablablub100.gemeinsameerinnerungen.Sync.FolderSyncer;

public class SyncActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        final TextView dbxNameTextView = findViewById(R.id.textView_dbx_name);
        final TextView dbxGallerySiteTextView = findViewById(R.id.textView_gallery_size);

        final Button testbutton = findViewById(R.id.button_test_dbx_access);
        testbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FolderSyncer.initDropbox(dbxNameTextView, dbxGallerySiteTextView);
                testbutton.setClickable(false);
            }
        });

        dbxGallerySiteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSyncingButtons();
            }
        });
    }

    protected void enableSyncingButtons() {
        final Button quickSyncButton = findViewById(R.id.button_quick_sync);
        final Button fullSyncButton = findViewById(R.id.button_full_sync);
        final TextView progressTextView = findViewById(R.id.textView_progress);

        quickSyncButton.setVisibility(View.VISIBLE);
        fullSyncButton.setVisibility(View.VISIBLE);
        progressTextView.setVisibility(View.VISIBLE);

        quickSyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullSyncButton.setVisibility(View.GONE);
                quickSyncButton.setClickable(false);
                FolderSyncer.quickSync(getBaseContext(), progressTextView);
            }
        });




    }
}
