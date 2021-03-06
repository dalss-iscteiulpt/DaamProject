package com.example.diogo.petsearcher;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.marker_content, null);

        TextView race_txt = view.findViewById(R.id.racetxt);
        TextView primary_txt = view.findViewById(R.id.dateTxt);
        TextView secondary_txt = view.findViewById(R.id.secondarytxt);
        ImageView img_view = view.findViewById(R.id.symbolImg);
        img_view.setImageResource(R.drawable.arrow_icon);

//        TextView name_tv = view.findViewById(R.id.tv_title);
//        TextView details_tv = view.findViewById(R.id.tv_subtitle);
//        ImageView img = view.findViewById(R.id.pic);

//        TextView hotel_tv = view.findViewById(R.id.hotels);
//        TextView food_tv = view.findViewById(R.id.food);
//        TextView transport_tv = view.findViewById(R.id.transport);

//        name_tv.setText(marker.getTitle());
//        details_tv.setText(marker.getSnippet());

//        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
//
//        int imageId = context.getResources().getIdentifier(infoWindowData.getImage().toLowerCase(),
//                "drawable", context.getPackageName());
//        img.setImageResource(imageId);
//
//        name_tv.setText("Hotel : excellent hotels available");
//        details_tv.setText("Food : all types of restaurants available");
//        transport_tv.setText("Reach the site by bus, car and train.");

        return view;
    }


}