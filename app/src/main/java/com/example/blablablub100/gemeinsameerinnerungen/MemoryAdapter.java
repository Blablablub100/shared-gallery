package com.example.blablablub100.gemeinsameerinnerungen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blablablub100.gemeinsameerinnerungen.ExperienceParser.Config;
import com.example.blablablub100.gemeinsameerinnerungen.experienceLogic.Memory;
import com.example.blablablub100.gemeinsameerinnerungen.util.ReadWriter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MemoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public class ViewHolderText extends RecyclerView.ViewHolder {

        private TextView memoryNameTextView,memoryTextTextView;
        private Memory memory;
        private Context context;

        private final View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MemoryTextViewActivity.class);
                intent.putExtra("filepath", memory.getFile().getAbsolutePath());
                context.startActivity(intent);
            }
        };


        public ViewHolderText(View itemView) {
            super(itemView);
            // Initiate view
            memoryNameTextView = (TextView) itemView.findViewById(R.id.textView_memory_text_name);
            memoryTextTextView = (TextView) itemView.findViewById(R.id.textView_memory_text_text);
            itemView.setOnClickListener(myListener);
        }


        public void showMemoryDetails(Memory memory, Context context){
            // Attach values for each item
            String memoryName = memory.getName();
            String memoryText = "";
            if (memory.getFile().getName().endsWith(".txt")) {
                memoryText = ReadWriter.readFile(memory.getFile().getAbsolutePath());
            } else {
                memoryText = "noText";
            }
            memoryNameTextView.setText(memoryName);
            memoryTextTextView.setText(memoryText);
            this.memory = memory;
            this.context = context;
        }
    }

    public class ViewHolderPic extends RecyclerView.ViewHolder {

        private TextView memoryNameTextView, memoryDescTextView;
        private ImageView memoryPicImageView;

        public ViewHolderPic(View itemView) {
            super(itemView);
            memoryNameTextView = (TextView) itemView.findViewById(R.id.textView_memory_pic_name);
            memoryDescTextView = (TextView) itemView.findViewById(R.id.textView_memory_pic_decription);
            memoryPicImageView = (ImageView) itemView.findViewById(R.id.imageView_memory_pic_pic);
        }

        public void showMemoryDetails(Memory memory) {
            String memoryName = memory.getName();
            String memoryDescription = memory.getDescription();
            memoryNameTextView.setText(memoryName);
            memoryDescTextView.setText(memoryDescription);
            Picasso.with(context).load(memory.getFile()).centerCrop().resize(200, 300).into(memoryPicImageView);
        }

    }

    public class ViewHolderVid extends RecyclerView.ViewHolder {

        private TextView memoryNameTextView, memoryDescTextView;
        private ImageView memoryThumbImageView;

        public ViewHolderVid(View itemView) {
            super(itemView);
            memoryNameTextView = (TextView) itemView.findViewById(R.id.textView_memory_vid_name);
            memoryDescTextView = (TextView) itemView.findViewById(R.id.textView_memory_vid_decription);
            memoryThumbImageView = (ImageView) itemView.findViewById(R.id.imageView_memory_vid_thumb);
        }

        public void showMemoryDetails(Memory memory) {
            String memoryName = memory.getName();
            String memoryDescription = memory.getDescription();
            memoryNameTextView.setText(memoryName);
            memoryDescTextView.setText(memoryDescription);
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(memory.getFile().getAbsolutePath(),
                    MediaStore.Video.Thumbnails.MINI_KIND);
            memoryThumbImageView.setImageBitmap(bitmap);
        }

    }


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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout;
        View v;
        switch (viewType) {
            case 0:
                layout = R.layout.memory_text_layout;
                v = LayoutInflater
                        .from(parent.getContext())
                        .inflate(layout, parent, false);
                return new ViewHolderText(v);
            case 1:
                layout = R.layout.memory_pic_layout;
                v = LayoutInflater
                        .from(parent.getContext())
                        .inflate(layout, parent, false);
                return new ViewHolderPic(v);
            case 2:
                layout = R.layout.memory_vid_layout;
                v = LayoutInflater
                        .from(parent.getContext())
                        .inflate(layout, parent, false);
                return new ViewHolderVid(v);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return memories.get(position).getType();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        Memory memory = memories.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                ((ViewHolderText) holder).showMemoryDetails(memory, context);
                break;
            case 1:
                ((ViewHolderPic) holder).showMemoryDetails(memory);
                break;
            case 2:
                ((ViewHolderVid) holder).showMemoryDetails(memory);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return memories.size();
    }







}

