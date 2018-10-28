package com.example.blablablub100.gemeinsameerinnerungen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blablablub100.gemeinsameerinnerungen.ExperienceParser.Config;
import com.example.blablablub100.gemeinsameerinnerungen.experienceLogic.Memory;
import com.example.blablablub100.gemeinsameerinnerungen.util.ReadWriter;

import java.util.ArrayList;
import java.util.List;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {

    private List<Memory> memories = new ArrayList<Memory>();
    private Context context;

    public MemoryAdapter(Context context) {
        this.context = context;
    }

    public void setMemories(List<Memory> memories) {
        this.memories = memories;
    }

    @NonNull
    @Override
    public MemoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        int layout = R.layout.memory_text_layout;
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoryAdapter.ViewHolder holder, int position) {
        Memory memory = memories.get(position);
        holder.showMemoryDetails(memory);
    }

    @Override
    public int getItemCount() {
        return memories.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView memoryNameTextView,memoryTextTextView,memoryDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initiate view
            memoryNameTextView = (TextView) itemView.findViewById(R.id.textView_memory_text_name);
            memoryDateTextView = (TextView) itemView.findViewById(R.id.textView_memory_text_date);
            memoryTextTextView = (TextView) itemView.findViewById(R.id.textView_memory_text_text);
        }


        public void showMemoryDetails(Memory memory){
            // Attach values for each item
            String memoryDate =  Config.printdf.format(memory.getDate());
            String memoryName = memory.getName();
            String memoryText = "";
            if (memory.getFile().getName().endsWith(".txt")) {
                memoryText = ReadWriter.readFile(memory.getFile().getAbsolutePath());
            } else {
                memoryText = "noText";
            }
            memoryNameTextView.setText(memoryName);
            memoryDateTextView.setText(memoryDate);
            memoryTextTextView.setText(memoryText);
        }
    }


}

