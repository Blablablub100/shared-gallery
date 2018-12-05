package com.example.blablablub100.gemeinsameerinnerungen;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.blablablub100.gemeinsameerinnerungen.ExperienceParser.Config;
import com.example.blablablub100.gemeinsameerinnerungen.experienceLogic.Experience;
import com.example.blablablub100.gemeinsameerinnerungen.experienceLogic.Experiences;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addExp = new Intent(getBaseContext(), AddExperience.class);
                startActivity(addExp);
            }
        });


        FloatingActionButton syncFab = findViewById(R.id.fab_open_sync);
        syncFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startSync = new Intent(getBaseContext(), SyncActivity.class);
                startActivity(startSync);
            }
        });



        // for showing the list
        Config.initExperiences();
        List<Experience> exps = Config.experiences.getExperiences();
        String[] expsarr = new String[exps.size()];
        for (int i = 0; i < exps.size(); i++) {
            expsarr[i] = exps.get(i).toString();
        }
        if (expsarr.length == 0) {
            expsarr = new String[3];
            expsarr[0] = "erster Start :)";
            expsarr[1] = "Alles Gute zum 19ten,";
            expsarr[2] = "kleiner Brocken <3";
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this
                , android.R.layout.simple_list_item_1
                , expsarr);
        final ListView listView = (ListView) findViewById(R.id.experiences_dynamic);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                String str = (String) o; //As you are using Default String Adapter
                if (!Config.experiences.isEmpty()) {
                    Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();

                    Intent openExp = new Intent(getBaseContext(), ExperienceViewActivity.class);
                    openExp.putExtra("exp_location", position);
                    openExp.putExtra("path"
                            , Config.generateFullPath(Config.experiences.getExperience(position)));
                    startActivity(openExp);


                } else {
                    Toast.makeText(getBaseContext(),
                            "Duu muuuust auf das Pluuuuus drücken :b"
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
