package com.example.diogo.petsearcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ListPetDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ArrayList<SpottedAnimal> animalList;
    protected ListView petlist;
    protected FirebaseClient animalListHook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pet_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        animalListHook = new FirebaseGetList();
//        animalListHook.update(this,animalList);

//        SpottedAnimal testPet = new SpottedAnimal();
//        testPet.setRealLocation("Test test test test location");
//        testPet.setCommentaries("commmmmmmmmmmmmmmmmmm fsfdfdsfdsfs sfdfdsfdsfsd sfdfsfsdfdsfs");
//        testPet.setPrimaryC("Green");
//        testPet.setSecodnaryC("Blue");
//        testPet.setEmail("ddfddsfdsf@fdfdsfdsf.com");
//        testPet.setPhoneNr("982323213123");
//        testPet.setPictureID("property_image_1");
//        testPet.setSpotttedDate("22 Jan 2017");
//        testPet.setSpottedHour("23:00");
//
//        animalList.add(testPet);
//        animalList.add(testPet);
//        animalList.add(testPet);
//        animalList.add(testPet);
//        animalList.add(testPet);


//        ArrayAdapter<SpottedAnimal> adapter = new ListPetDrawer.propertyArrayAdapter(this, 0, animalList);

        petlist = (ListView) findViewById(R.id.customListView);
        animalListHook= new FirebaseClient(this,petlist,null);
        animalListHook.refreshdata();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_pet_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Map) {
            Intent intent = new Intent(ListPetDrawer.this, MainMapDrawer.class);
            startActivity(intent);

        } else if (id == R.id.nav_list) {

        } else if (id == R.id.nav_filters) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    class propertyArrayAdapter extends ArrayAdapter<SpottedAnimal> {
//
//        private Context context;
//        private List<SpottedAnimal> animalList;
//
//        protected DatabaseReference mDatabase;
//        private StorageReference mStorageRef;
//
//        //constructor, call on creation
//        public propertyArrayAdapter(Context context, int resource, ArrayList<SpottedAnimal> objects) {
//            super(context, resource, objects);
//
//            this.context = context;
//            this.animalList = objects;
//
//            mDatabase = FirebaseDatabase.getInstance().getReference();
//            mStorageRef = FirebaseStorage.getInstance().getReference();
//
//        }
//
//        //called when rendering the list
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            //get the property we are displaying
//            SpottedAnimal animal = animalList.get(position);
//
//            //get the inflater and inflate the XML layout for each item
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//
//            //conditionally inflate either standard or special template
//            View view = inflater.inflate(R.layout.array_format, null);
//
//
//            TextView description = (TextView) view.findViewById(R.id.description);
//            TextView typeBreedTxt = (TextView) view.findViewById(R.id.desc1txt);
//            TextView dateTime = (TextView) view.findViewById(R.id.dateTxt);
//            ImageView image = (ImageView) view.findViewById(R.id.image);
//
//            String typeBreed = animal.getType()+" "+animal.getBreed();
//            String descriptiontxt = "Primary Color: "+animal.getPrimaryC()+"\n"
//                    +"Secondary Color: "+animal.getSecodnaryC()+"\n"
//                    +"Gender: "+animal.getGender();
//            String dateTimetxt = animal.getSpotttedDate()+" "+animal.getSpottedHour();
//
//            description.setText(descriptiontxt);
//            typeBreedTxt.setText(typeBreed);
//            dateTime.setText(dateTimetxt);
//            //get the image associated with this property
//            int imageID = context.getResources().getIdentifier(String.valueOf(animal.getPictureID()), "drawable", context.getPackageName());
//            image.setImageResource(imageID);
//
//            return view;
//        }
//    }
}
