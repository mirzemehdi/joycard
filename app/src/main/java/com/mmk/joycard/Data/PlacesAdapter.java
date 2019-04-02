package com.mmk.joycard.Data;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mmk.joycard.CardScanActivity;
import com.mmk.joycard.Model.Place;
import com.mmk.joycard.PlacesActivity;
import com.mmk.joycard.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {



    private Context context;
    private List<Place> places;


    public PlacesAdapter(Context context, List<Place> places) {
        this.context = context;
        this.places = places;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.place_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.placeTitle.setText(places.get(position).getPlaceTitle());

        int imageResource=Integer.parseInt(places.get(position).getImgUrl());
        holder.placeImg.setImageResource(imageResource);

    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView placeImg;
        private TextView placeTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            placeImg=(CircleImageView)itemView.findViewById(R.id.placeRowImage);
            placeTitle=(TextView)itemView.findViewById(R.id.placeRowTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CardScanActivity.class);
                    context.startActivity(intent);
                }
            });
        }



    }
}
