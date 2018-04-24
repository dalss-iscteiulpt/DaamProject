package com.example.diogo.petsearcher;

/**
 * Created by Diogo on 17/04/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
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


    public  FirebaseClient(Context c, ListView listView, GoogleMap mapView)
    {
        this.c= c;
        this.listView= listView;
        this.mapView = mapView;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();


    }


    public  void refreshdata()
    {
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

    public void onCLickList(){

    }

    public void getupdates(DataSnapshot dataSnapshot){

        animalList.clear();

        for(DataSnapshot ds :dataSnapshot.getChildren()){
            final  DataSnapshot dataSnap = ds;
            String id = dataSnap.child("id").getValue(String.class);
            mStorageRef.child("picture_"+id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    if (listView != null) {
                        SpottedAnimal pet = dataSnap.getValue(SpottedAnimal.class);
                        pet.setPictureID(uri.toString());
                        animalList.add(pet);
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
                            Toast.makeText(c, "No data", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (mapView != null){
                        SpottedAnimal pet = dataSnap.getValue(SpottedAnimal.class);
                        pet.setPictureID(uri.toString());
                        animalList.add(pet);
                        if (animalList.size() > 0) {
                            String[] coords = pet.getCoordLocation().split(",");
                            String data = pet.id+"&&"+pet.getPrimaryC()+"&&"+pet.getSpotttedDate()+
                                    "&&"+pet.getSpottedHour()+"&&"+pet.getBreed();
                            addMarker(Double.parseDouble(coords[1]),Double.parseDouble(coords[0]),mapView,data);
                        } else {
                            Toast.makeText(c, "No data", Toast.LENGTH_SHORT).show();
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