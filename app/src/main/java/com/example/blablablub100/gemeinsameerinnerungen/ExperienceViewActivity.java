package com.example.blablablub100.gemeinsameerinnerungen;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.blablablub100.gemeinsameerinnerungen.ExperienceParser.Config;
import com.example.blablablub100.gemeinsameerinnerungen.ExperienceParser.ExperienceReader;
import com.example.blablablub100.gemeinsameerinnerungen.experienceLogic.Experience;
import com.example.blablablub100.gemeinsameerinnerungen.experienceLogic.Memory;

import java.util.ArrayList;
import java.util.List;

public class ExperienceViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    // Linear Layout Manager or GridLayoutManager possible too
    private StaggeredGridLayoutManager layoutManager;
    private List<Memory> memories;
    private MemoryAdapter memoryAdapter;
    private Experience currExp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_view);

        FloatingActionButton fab = findViewById(R.id.fab_add_memory);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent addMem = new Intent(getBaseContext(), AddMemoryActivity.class);
                addMem.putExtra("path", getIntent().getStringExtra("path"));
                startActivity(addMem);
            }
        });

        currExp = Config.experiences.getExperience(
                getIntent().getIntExtra("exp_location", 0));

        ExperienceReader.readMemories(currExp);


        recyclerView = findViewById(R.id.recycler_view);

        // set Layout Manager
        layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        // Limiting the size
        recyclerView.setHasFixedSize(true);

        init();
    }

    private void init() {
        memories = new ArrayList<Memory>();
        memoryAdapter = new MemoryAdapter(ExperienceViewActivity.this);
        recyclerView.setAdapter(memoryAdapter);

        memoryAdapter.setMemories(currExp.getMemories());
        // Adding date to the list
        /*callsList.add(new Call("John","9:30 AM"));
        callsList.add(new Call("Rob","9:40 AM"));
        callsList.add(new Call("Peter","9:45 AM"));
        callsList.add(new Call("Jack","9:50 AM"));
        callsList.add(new Call("Bob","9:55 AM"));
        callsList.add(new Call("Sandy","10:00 AM"));
        callsList.add(new Call("Kate","10:05 AM"));
        callsList.add(new Call("Daniel","10:10 AM"));
        callsList.add(new Call("Roger","10:15 AM"));
        callsList.add(new Call("Sid","10:20 AM"));
        callsList.add(new Call("Kora","10:25 AM"));
        callsList.add(new Call("Nick","10:30 AM"));
        callsList.add(new Call("Rose","10:35 AM"));
        callsList.add(new Call("Mia","10:40 AM"));
        callsList.add(new Call("Scott","10:45 AM"));
        // Set items to adapter
        callsAdapter.setCallsFeed(callsList);
        callsAdapter.notifyDataSetChanged();
        */
        memoryAdapter.notifyDataSetChanged();


    }
}
