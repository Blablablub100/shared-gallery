package com.example.blablablub100.gemeinsameerinnerungen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blablablub100.gemeinsameerinnerungen.ExperienceParser.Config;
import com.example.blablablub100.gemeinsameerinnerungen.experienceLogic.Memory;
import com.example.blablablub100.gemeinsameerinnerungen.util.ReadWriter;
import com.squareup.picasso.Picasso;

import java.io.File;

public class MemoryPicViewActivity extends AppCompatActivity {

    EditText memoryDesc;
    TextView memoryName;
    TextView memoryDate;
    Button saveButton;
    ImageView img;
    Memory memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_pic_view);
        memoryName = findViewById(R.id.textView_memory_pic_view_name);
        memoryDate = findViewById(R.id.textView_memory_pic_view_date);
        memoryDesc = findViewById(R.id.editText_memory_pic_view_decription);
        saveButton = findViewById(R.id.button_memory_pic_view_save);
        img = findViewById(R.id.imageView_memory_pic_view_pic);
        final String filepath = getIntent().getStringExtra("filepath");

        memory = Config.generateMemoryData(new File(filepath)
                , Config.generateDescriptionFile(new File(filepath)));
        memoryDesc.setText(ReadWriter.readFile(Config.generateDescriptionFile(memory.getFile()).getAbsolutePath()));
        memoryName.setText(memory.getName());
        memoryDate.setText(Config.printdf.format(memory.getDate()));
        Picasso.with(getBaseContext()).load(memory.getFile()).centerCrop().resize(1080, 1920).into(img);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = memoryDesc.getText().toString();
                ReadWriter.writeFile(Config.generateDescriptionFile(new File(filepath)).getAbsolutePath(), text);
                Toast.makeText(getBaseContext(), "Gespeeeeeichert", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
