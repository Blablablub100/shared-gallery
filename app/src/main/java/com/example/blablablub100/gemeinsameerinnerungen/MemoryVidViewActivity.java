package com.example.blablablub100.gemeinsameerinnerungen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.blablablub100.gemeinsameerinnerungen.ExperienceParser.Config;
import com.example.blablablub100.gemeinsameerinnerungen.experienceLogic.Memory;
import com.example.blablablub100.gemeinsameerinnerungen.util.ReadWriter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class MemoryVidViewActivity extends AppCompatActivity {

    EditText memoryDesc;
    TextView memoryName;
    TextView memoryDate;
    Button saveButton;
    VideoView img;
    Memory memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_vid_view);
        memoryName = findViewById(R.id.textView_memory_vid_view_name);
        memoryDate = findViewById(R.id.textView_memory_vid_view_date);
        img = findViewById(R.id.imageView_memory_vid_view_thumb);
        memoryDesc = findViewById(R.id.editText_memory_vid_view_decription);
        final String filepath = getIntent().getStringExtra("filepath");
        saveButton = findViewById(R.id.button_memory_vid_view_save);

        memory = Config.generateMemoryData(new File(filepath)
                , Config.generateDescriptionFile(new File(filepath)));
        memoryDesc.setText(ReadWriter.readFile(Config.generateDescriptionFile(memory.getFile()).getAbsolutePath()));
        memoryName.setText(memory.getName());
        memoryDate.setText(Config.printdf.format(memory.getDate()));
        img.setVideoPath(memory.getFile().getAbsolutePath());
        img.seekTo(50);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = memoryDesc.getText().toString();
                ReadWriter.writeFile(Config.generateDescriptionFile(new File(filepath)).getAbsolutePath(), text);
                Toast.makeText(getBaseContext(), "Gespeeeeeichert", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.imageView_play_vid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.imageView_play_vid).setVisibility(View.GONE);
                img.requestFocus();
                img.start();
            }
        });

        img.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                img.seekTo(0);
                findViewById(R.id.imageView_play_vid).setVisibility(View.VISIBLE);
            }
        });
    }
}
