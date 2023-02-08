package com.project.birthdayphotoframe.ui.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.birthdayphotoframe.EditPhotoFrameActivity;
import com.project.birthdayphotoframe.R;
import com.project.birthdayphotoframe.databinding.FragmentFavouritesBinding;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FavouritesAdaptor extends RecyclerView.Adapter<FavouritesAdaptor.viewHolder> {

    FragmentFavouritesBinding binding;
    Context context;

    ArrayList<String> list_of_Path;

    public FavouritesAdaptor(Context activity, ArrayList<String> pathDB) {
        list_of_Path = pathDB;
        context = activity;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_for_fragments, parent, false);
        viewHolder vH = new viewHolder(v);
        return vH;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.img.setVisibility(View.GONE);
        holder.rel_to_be_hide.setVisibility(View.GONE);
        String path = list_of_Path.get(position);

        try {
            InputStream inputStream = context.getAssets().open(path);
            Drawable d = Drawable.createFromStream(inputStream, null);
            Glide.with(context).load(d).into(holder.img_id);

            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        Toast.makeText(context, getfolderName(path) + getpathName(path), Toast.LENGTH_SHORT).show();
        getfolderName(path);

        holder.img_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context.getApplicationContext(), EditPhotoFrameActivity.class).putExtra("folderName", getfolderName(path)).putExtra("pathName", getpathName(path)));
            }
        });


    }

    private String getfolderName(String path) {
        String folderName = "";
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '/') {
                folderName += path.charAt(i);
                break;
            }
            folderName += path.charAt(i);
        }

        return folderName;


    }

    private String getpathName(String path) {
        String pathName = "";
        for (int i = path.indexOf('/') + 1; i < path.length(); i++) {
            pathName += path.charAt(i);
        }
        return pathName;

    }

    @Override
    public int getItemCount() {
        return list_of_Path.size();
    }


    class viewHolder extends RecyclerView.ViewHolder {
        ImageView img, img_id;
        RelativeLayout rel_to_be_hide;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            rel_to_be_hide=itemView.findViewById(R.id.rel_to_be_hide);
            img = itemView.findViewById(R.id.share_id);
            img_id = itemView.findViewById(R.id.img_id);
        }
    }
}
