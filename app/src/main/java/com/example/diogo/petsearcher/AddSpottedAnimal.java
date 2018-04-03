package com.example.diogo.petsearcher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;


public class AddSpottedAnimal extends AppCompatActivity {

    protected TextView typeTxt;
    protected TextView dateTimeTxt;
    protected TextView locTxt;
    protected ImageView pic;
    protected Button locBtn;

    protected boolean typeFirstClick;
    protected String realLoc;
    protected Context mContext;

    private static final int CAMERA_PIC_REQUEST = 1;
    int PLACE_PICKER_REQUEST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spotted_animal);
        mContext = this;
        startFields();

    }

    protected void startFields() {
        typeFirstClick = true;
        typeTxt = (TextView) findViewById(R.id.typetxt);
        typeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeFirstClick) {
                    ((TextView) view).setText("");
                    ((TextView) view).setTextColor(0xFF000000);
                    typeFirstClick = false;
                }
            }
        });

        pic = (ImageView) findViewById(R.id.imageView3);
        pic.setImageResource(R.drawable.camera_icon);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_PIC_REQUEST);

            }
        });

        dateTimeTxt = (TextView) findViewById(R.id.dateTime);
        dateTimeTxt.setText(DateFormat.getDateTimeInstance().format(new Date()));

        LatLng loc = (LatLng) getIntent().getParcelableExtra("location");
        locTxt = (TextView) findViewById(R.id.locationTxt);
        locTxt.setText(getRealLocation(loc.longitude, loc.latitude));

        locBtn = (Button) findViewById(R.id.locationBtn);
        locBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            try {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                pic.setImageBitmap(thumbnail);
            } catch (NullPointerException exp){
                return;
            }
        } else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {

            }
        }

    }


    protected String getRealLocation(double longitude, double latitude) {
        String realLocation ="";
        Geocoder geocoder = new Geocoder(AddSpottedAnimal.this);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude,
                                                 longitude,
                                                1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses != null && addresses.size() > 0 ){
            Address address = addresses.get(0);
            if (address.getThoroughfare() == null){
                realLocation = address.getPremises()+", "+address.getPostalCode()+" "+address.getAdminArea()+", "+address.getCountryName();
            } else {
                realLocation = address.getThoroughfare();
            }
        }
        return realLocation;
    }

}
