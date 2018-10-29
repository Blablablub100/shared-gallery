package com.example.blablablub100.gemeinsameerinnerungen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blablablub100.gemeinsameerinnerungen.ExperienceParser.Config;
import com.example.blablablub100.gemeinsameerinnerungen.experienceLogic.Memory;
import com.example.blablablub100.gemeinsameerinnerungen.util.ReadWriter;

import java.io.File;

public class MemoryTextViewActivity extends AppCompatActivity {

    EditText memoryText;
    TextView memoryName;
    TextView memoryDate;
    Button saveButton;
    Memory memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_text_view);
        memoryText = findViewById(R.id.editText_memory_text_view_text);
        memoryName = (TextView) findViewById(R.id.textView_memory_text_view_name);
        memoryDate = (TextView) findViewById(R.id.textView_memory_text_view_date);
        final String filepath = getIntent().getStringExtra("filepath");
        saveButton = findViewById(R.id.button_memory_text_view_save);

        memory = Config.generateMemoryData(new File(filepath)
                , Config.generateDescriptionFile(new File(filepath)));
        memoryText.setText(ReadWriter.readFile(memory.getFile().getAbsolutePath()));
        memoryName.setText(memory.getName());
        memoryDate.setText(Config.printdf.format(memory.getDate()));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = memoryText.getText().toString();
                ReadWriter.writeFile(filepath, text);
                Toast.makeText(getBaseContext(), "Gespeeeeeichert", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
