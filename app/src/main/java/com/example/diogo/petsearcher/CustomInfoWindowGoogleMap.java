package com.example.diogo.petsearcher;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

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
        String data = marker.getTitle();
        String[] parsedData = data.split("&&");
        marker.setSnippet(parsedData[0]);

        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.marker_content, null);
        TextView race_txt = view.findViewById(R.id.racetxt);
        TextView primary_txt = view.findViewById(R.id.primaryTxt);
        TextView date_txt = view.findViewById(R.id.dateTxt);
        TextView hourTxt = view.findViewById(R.id.hourTxt);
        ImageView img_view = view.findViewById(R.id.symbolImg);
        img_view.setImageResource(R.drawable.arrow_icon);
        ImageView thumbPic = view.findViewById(R.id.markerImageView);

        race_txt.setText(parsedData[4]);
        primary_txt.setText(parsedData[1]);
        date_txt.setText(parsedData[2]);
        hourTxt.setText((parsedData[3]));
//        PicassoClient.downloadimg(context,parsedData[5],thumbPic);
        Picasso.with(context).load(parsedData[5]).placeholder(R.drawable.img_001).into(thumbPic,new MarkerCallback(marker));

        return view;
    }

    // Callback is an interface from Picasso:
    static class MarkerCallback implements Callback
    {
        Marker marker = null;

        MarkerCallback(Marker marker)
        {
            this.marker = marker;
        }

        @Override
        public void onError() {}

        @Override
        public void onSuccess()
        {
            if (marker == null)
            {
                return;
            }

            if (!marker.isInfoWindowShown())
            {
                return;
            }

            // If Info Window is showing, then refresh it's contents:

            marker.hideInfoWindow(); // Calling only showInfoWindow() throws an error
            marker.showInfoWindow();
        }
    }
}