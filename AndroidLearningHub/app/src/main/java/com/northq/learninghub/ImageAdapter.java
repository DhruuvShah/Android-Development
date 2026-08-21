package com.northq.learninghub;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/** Unit 2 Practical: "Display images using ImageView and GalleryView" (RecyclerView grid variant). */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final Context context;
    private final List<Uri> uris;

    public ImageAdapter(Context context, List<Uri> uris) {
        this.context = context;
        this.uris = uris;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageURI(uris.get(position));
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gridImage);
        }
    }
}
