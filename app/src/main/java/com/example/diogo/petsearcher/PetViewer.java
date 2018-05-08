package com.example.diogo.petsearcher;

import android.content.Context;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class PetViewer extends AppCompatActivity{
    protected String petId;
    protected DatabaseReference mDatabase;
    private StorageReference mStorageRef;


    protected TextView typeTxt;
    protected TextView raceTxt;
    protected TextView priTxt;
    protected TextView secTxt;
    protected TextView genderTxt;
    protected TextView sizeTxt;
    protected TextView phoneNrTxt;
    protected TextView emailTxt;
    protected ImageView petImg;
    protected MapView mapViewer;
//  protected GoogleMap mMapViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_pet_viewer);
        startActionBar();
        startFields();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        try {
            SpottedAnimal petFromList = (SpottedAnimal) getIntent().getExtras().get("PetList");
            resolveMarkerItemFirebase(petFromList.getId());
        } catch (ClassCastException exp){
            String data = (String) getIntent().getExtras().get("PetMarker");
            resolveMarkerItemFirebase(data.split("&&")[0]);
        }

    }

    public void resolveMarkerItemFirebase(final String idKey){
        Query mQuery = mDatabase.orderByChild("id").equalTo(idKey);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds :dataSnapshot.getChildren()){
                    final DataSnapshot dataSnap = ds;
                    mStorageRef.child("picture_Main"+idKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            SpottedAnimal petMarker = dataSnap.getValue(SpottedAnimal.class);
                            petMarker.setPictureIDMain(uri.toString());
                            setFields(petMarker);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void startFields(){
        typeTxt = (TextView) findViewById(R.id.typeTxtViewer);
        raceTxt = (TextView) findViewById(R.id.racetxtViewer);
        priTxt = (TextView) findViewById(R.id.primaryTxtViewer);
        secTxt = (TextView) findViewById(R.id.secTxtViewer);
        genderTxt = (TextView) findViewById(R.id.genderTxtViewer);
        sizeTxt = (TextView) findViewById(R.id.sizeTxtViewer);
        phoneNrTxt = (TextView) findViewById(R.id.phonTxtViewer);
        emailTxt = (TextView) findViewById(R.id.emailTxtViewer);
        petImg = (ImageView) findViewById(R.id.imgViewerPet);
        mapViewer = (MapView) findViewById(R.id.osmMap);
        mapViewer.setTileSource(TileSourceFactory.MAPNIK);
        mapViewer.setBuiltInZoomControls(true);
        mapViewer.setMultiTouchControls(true);


    }

    public void setFields(SpottedAnimal pet){
        addMarkerAndZoom(pet);
        typeTxt.setText(pet.getType());
        raceTxt.setText(pet.getBreed());
        priTxt.setText(pet.getPrimaryC());
        secTxt.setText(pet.getSecodnaryC());
        genderTxt.setText(pet.getGender());
        sizeTxt.setText(pet.getSize());
        phoneNrTxt.setText(pet.getPhoneNr());
        emailTxt.setText(pet.getEmail());
        PicassoClient.downloadimg(this, pet.getPictureIDMain(),petImg);
    }

    public void addMarkerAndZoom(SpottedAnimal pet){
        IMapController mapController = mapViewer.getController();
        final Double lon = Double.parseDouble(pet.getCoordLocation().split(",")[0]);
        final Double lat = Double.parseDouble(pet.getCoordLocation().split(",")[1]);
        GeoPoint startPoint = new GeoPoint(lat, lon);;

        Marker startMarker = new Marker(mapViewer);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapViewer.getOverlays().add(startMarker);

        mapController.setZoom(17.0);
        mapController.setCenter(startPoint);

    }

    public void startActionBar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }






}
