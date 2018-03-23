package com.example.diogo.petsearcher;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button addMarkerBtn;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mContext=this;




        addMarkerBtn = findViewById(R.id.button5);
        addMarkerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mContext, "Please enable Location services.", Toast.LENGTH_SHORT).show();
                    return;
                }
                final Location getLastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                //addMarker(getLastLocation.getLatitude(), getLastLocation.getLongitude());
                Intent addAnimal = new Intent(MainMap.this, AddSpottedAnimal.class);
                addAnimal.putExtra("location", getLastLocation);
                startActivity(addAnimal);
            }
        });

        Button testBtn = findViewById(R.id.testbtn);
        testBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMap.this, AnimalList.class);
                startActivity(intent);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please enable Location services.", Toast.LENGTH_SHORT).show();
            return;
        }
        mMap.setMyLocationEnabled(true);
        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(customInfoWindow);
        centerMapOnLocation();
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                return infoWindowAdapter(marker);
//            }
//        });

    }

    public void centerMapOnLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        final Location getLastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(1));
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            public void onMapLoaded() {
                try {
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                            .target(new LatLng(getLastLocation.getLatitude(), getLastLocation.getLongitude()))
                            .zoom(18)
                            .build()));
                } catch(Exception e){
                    Toast.makeText(mContext, "Please enable Location services.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void addMarker(Double lat, Double log){
        LatLng location = new LatLng(lat, log);
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

    }

//    public void drawMarker(Double lat, Double log) {
//        LatLng position = new LatLng(lat, log);
//
//        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
//        Bitmap bmp = Bitmap.createBitmap(80, 80, conf);
//        Canvas canvas1 = new Canvas(bmp);
//
//// paint defines the text color, stroke width and size
//        Paint color = new Paint();
//        color.setTextSize(35);
//        color.setColor(Color.BLACK);
//
//
//        // modify canvas
//        canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
//                R.drawable.image_001), 0,0, color);
//        canvas1.drawText("User Name!", 30, 40, color);
//
//// add marker to Map
//        mMap.addMarker(new MarkerOptions()
//                .position(position)
//                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
//                // Specifies the anchor to be at a particular point in the marker image.
//                .anchor(0.5f, 1));
//    }


//    public boolean infoWindowAdapter(Marker arg0){
//        final Dialog d = new Dialog(MainMap.this);
//        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        d.setTitle("Select");
//        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//        d.setContentView(R.layout.marker_content);
//        ImageView eivPhoto = (ImageView)d.findViewById(R.id.infocontent_iv_image);
//        final LatLng l = arg0.getPosition();
//        TextView tvName = (TextView)d.findViewById(R.id.infocontent_tv_name);
//        tvName.setText("Text1");
//
//        TextView tvType = (TextView)d.findViewById(R.id.infocontent_tv_type);
//        tvType.setText("Text1");
//
//        TextView tvDesc = (TextView)d.findViewById(R.id.infocontent_tv_desc);
//        tvDesc.setText("Text1");
//
//        TextView tvAddr = (TextView)d.findViewById(R.id.infocontent_tv_addr);
//        tvAddr.setText("Text1");
//
//        d.show();
//        return true;
//    }



}
