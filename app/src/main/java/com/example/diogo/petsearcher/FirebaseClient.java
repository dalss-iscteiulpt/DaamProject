package com.example.diogo.petsearcher;

/**
 * Created by Diogo on 17/04/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by Admin on 5/26/2017.
 */

public class FirebaseClient  {

    private GoogleMap mapView =null;
    Context c;
    ListView listView = null;
    ArrayList<SpottedAnimal> animalList= new ArrayList<>();
    CustomAdapter customAdapter;
    protected DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private FilterObject filterObj;


    public  FirebaseClient(Context c, ListView listView, GoogleMap mapView)
    {
        this.c= c;
        this.listView= listView;
        this.mapView = mapView;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();


    }

    public Location getLocation(){
        LocationManager locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        return lastLocation;

    }


    public  void refreshdata(FilterObject filterObj){
        this.filterObj = filterObj;
        Query query = mDatabase.orderByChild("id");
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getupdates(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addMarker(Double lat, Double log, GoogleMap mMap, String data){
        LatLng location = new LatLng(lat, log);
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(data));
    }

    public SpottedAnimal petVerifier(SpottedAnimal pet){
        Location actualLocation = getLocation();
        String[] coords = pet.getCoordLocation().split(",");
        Location petLocation = new Location("Fire Provider");
        petLocation.setLatitude(Double.parseDouble(coords[1]));
        petLocation.setLongitude(Double.parseDouble(coords[0]));
        Double distance = actualLocation.distanceTo(petLocation)*0.001;


        if (!pet.getType().equals(filterObj.getType()) && !filterObj.getType().equals("Any")){
            pet = null;
        }
        if (pet != null && !pet.getPrimaryC().equals(filterObj.getColor()) && !filterObj.getColor().equals("Any")){
            pet = null;
        }
        if (pet != null && !pet.getGender().equals(filterObj.getGender()) && !filterObj.getGender().equals("Any")){
            pet = null;
        }
        if(pet != null && distance > filterObj.getDistance() ){
            pet = null;
        }
        if (pet != null) {
            animalList.add(pet);
        }

        return pet;
    }


    public void getupdates(DataSnapshot dataSnapshot){

        animalList.clear();
        try {
            mapView.clear();
        } catch (NullPointerException excp){
            Log.d("Erro1","NoMapView");
        }

        for(DataSnapshot ds :dataSnapshot.getChildren()){
            final  DataSnapshot dataSnap = ds;
            String id = dataSnap.child("id").getValue(String.class);
            mStorageRef.child("picture_Thumb"+id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    if (listView != null) {
                        SpottedAnimal pet = dataSnap.getValue(SpottedAnimal.class);
                        pet.setPictureIDThumb(uri.toString());
                        pet = petVerifier(pet);
                        if (animalList.size() > 0) {
                            customAdapter = new CustomAdapter(c, animalList);
                            listView.setAdapter((ListAdapter) customAdapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(c, PetViewer.class);
                                    SpottedAnimal intentPet= (SpottedAnimal) adapterView.getItemAtPosition(i);
                                    intent.putExtra("PetMarker", "None");
                                    intent.putExtra("PetList", intentPet);
                                    c.startActivity(intent);
                                }
                            });
                        } else {
                            if(pet != null) {
                                Toast.makeText(c, "No data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    if (mapView != null){
                        SpottedAnimal pet = dataSnap.getValue(SpottedAnimal.class);
                        pet.setPictureIDThumb(uri.toString());
                        pet = petVerifier(pet);
                        if (animalList.size() > 0 ) {
                            if(pet != null) {
                                String[] coords = pet.getCoordLocation().split(",");
                                String data = pet.id + "&&" + pet.getPrimaryC() + "&&" + pet.getSpotttedDate() +
                                        "&&" + pet.getSpottedHour() + "&&" + pet.getBreed() + "&&" + pet.getPictureIDThumb();
                                addMarker(Double.parseDouble(coords[1]), Double.parseDouble(coords[0]), mapView, data);
                            }
                        } else {
                            if(pet != null) {
                                Toast.makeText(c, "No data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    exception.printStackTrace();
                    //Toast.makeText(c, "Error getting URL", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}