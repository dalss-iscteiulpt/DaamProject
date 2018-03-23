package com.example.diogo.petsearcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AnimalList extends AppCompatActivity {

    private ArrayList<SpottedAnimal> animalList = new ArrayList<>();
    protected ListView petlist;

    protected ListView mDrawerList;
    protected DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_animal_list);

        SpottedAnimal testPet = new SpottedAnimal();
        testPet.setRealLocation("Test test test test location");
        testPet.setCommentaries("commmmmmmmmmmmmmmmmmm fsfdfdsfdsfs sfdfdsfdsfsd sfdfsfsdfdsfs");
        testPet.setPrimaryC("Green");
        testPet.setSecodnaryC("Blue");
        testPet.setEmail("ddfddsfdsf@fdfdsfdsf.com");
        testPet.setPhoneNr("982323213123");
        testPet.setPicture("property_image_1");
        testPet.setSpotttedDate("22 Jan 2017");
        testPet.setSpottedHour("23:00");

        animalList.add(testPet);
        animalList.add(testPet);
        animalList.add(testPet);
        animalList.add(testPet);
        animalList.add(testPet);

        ArrayAdapter<SpottedAnimal> adapter = new propertyArrayAdapter(this, 0, animalList);

        petlist = (ListView) findViewById(R.id.customListView);
        petlist.setAdapter(adapter);

        startDrawer();



    }

    private void startDrawer(){
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void addDrawerItems() {
        String[] osArray = { "Pet List", "Pet Map" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        mDrawerLayout.closeDrawers();
                        break;
                    case 1:
                        mDrawerLayout.closeDrawers();
                        Intent goToMap = new Intent(AnimalList.this, MainMap.class);
                        startActivity(goToMap);
                        break;
                }
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    class propertyArrayAdapter extends ArrayAdapter<SpottedAnimal> {

        private Context context;
        private List<SpottedAnimal> animalList;

        //constructor, call on creation
        public propertyArrayAdapter(Context context, int resource, ArrayList<SpottedAnimal> objects) {
            super(context, resource, objects);

            this.context = context;
            this.animalList = objects;
        }

        //called when rendering the list
        public View getView(int position, View convertView, ViewGroup parent) {

            //get the property we are displaying
            SpottedAnimal animal = animalList.get(position);

            //get the inflater and inflate the XML layout for each item
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            //conditionally inflate either standard or special template
            View view = inflater.inflate(R.layout.array_format, null);


            TextView description = (TextView) view.findViewById(R.id.description);
            TextView typeBreedTxt = (TextView) view.findViewById(R.id.desc1txt);
            TextView dateTime = (TextView) view.findViewById(R.id.dateTxt);
            ImageView image = (ImageView) view.findViewById(R.id.image);

            String typeBreed = animal.getType()+" "+animal.getBreed();
            String descriptiontxt = "Primary Color: "+animal.getPrimaryC()+"\n"
                    +"Secondary Color: "+animal.getSecodnaryC()+"\n"
                    +"Pattern: "+animal.getPattern();
            String dateTimetxt = animal.getSpotttedDate()+" "+animal.getSpottedHour();

            description.setText(descriptiontxt);
            typeBreedTxt.setText(typeBreed);
            dateTime.setText(dateTimetxt);
            //get the image associated with this property
            int imageID = context.getResources().getIdentifier(String.valueOf(animal.getPicture()), "drawable", context.getPackageName());
            image.setImageResource(imageID);

            return view;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

