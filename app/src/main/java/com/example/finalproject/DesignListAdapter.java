package com.example.finalproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DesignListAdapter extends RecyclerView.Adapter<DesignListAdapter.DesignViewHolder> {
    private static List<Design> designs;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Design design);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class DesignViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewDesign;
        public TextView textViewDescription;
        public TextView textViewDesignerName;
        public Button buttonMakeOffer;

        public DesignViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageViewDesign = itemView.findViewById(R.id.image_view_design);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewDesignerName = itemView.findViewById(R.id.text_view_designer_name);
            buttonMakeOffer = itemView.findViewById(R.id.button_make_offer);

            buttonMakeOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(designs.get(position));
                        }
                    }
                }
            });
        }
    }

    public DesignListAdapter(List<Design> designs) {
        this.designs = designs;
    }

    @NonNull
    @Override
    public DesignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_item, parent, false);
        DesignViewHolder designViewHolder = new DesignViewHolder(view, listener);
        return designViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DesignViewHolder holder, int position) {
        Design design = designs.get(position);
        holder.textViewDescription.setText(design.getDescription());
        holder.textViewDesignerName.setText(design.getDesigner().getName());
        Glide.with(holder.itemView)
                .load(design.getImageUrl())
                .into(holder.imageViewDesign);
    }

    @Override
    public int getItemCount() {
        return designs.size();
    }
}

