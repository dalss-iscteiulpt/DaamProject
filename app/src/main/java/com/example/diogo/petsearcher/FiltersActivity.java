package com.example.diogo.petsearcher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class FiltersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected DiscreteSeekBar discreteSeekBar;
    protected TextView typeFTxt;
    protected TextView colorFTxt;
    protected TextView genderFTxt;
    protected Button confFBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        filtersStartFields();
    }

    public void filtersStartFields(){
        discreteSeekBar = (DiscreteSeekBar) findViewById(R.id.seekBarDiscrete);
        typeFTxt = (TextView) findViewById(R.id.typeFiltersTxt);
        typeFTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView) view).setText("");
                ((TextView) view).setTextColor(0xFF000000);
            }
        });
        colorFTxt = (TextView) findViewById(R.id.colorFiltersTxt);
        colorFTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView) view).setText("");
                ((TextView) view).setTextColor(0xFF000000);
            }
        });
        genderFTxt = (TextView) findViewById(R.id.genderFiltersTxt);
        genderFTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView) view).setText("");
                ((TextView) view).setTextColor(0xFF000000);
            }
        });

        confFBtn = (Button)findViewById(R.id.confirmFiltersButton);
        confFBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type = "Any";
                String color = "Any";
                int distance = discreteSeekBar.getProgress();
                String gender = "Any";
                if (!typeFTxt.getText().toString().equals(""))
                    type = typeFTxt.getText().toString();
                if (!colorFTxt.getText().toString().equals(""))
                    color = colorFTxt.getText().toString();
                if (!genderFTxt.getText().toString().equals(""))
                    gender = genderFTxt.getText().toString();
                FilterObject fObject = new FilterObject(type,color,distance,gender);
                Intent intent = new Intent(FiltersActivity.this, MainMapDrawer.class);
                intent.putExtra("FiltersObject",fObject);
                Log.d("Passing",fObject.toString());
                startActivity(intent);
            }
        });
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
        getMenuInflater().inflate(R.menu.filters, menu);
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
            Intent intent = new Intent(FiltersActivity.this, MainMapDrawer.class);
            startActivity(intent);
        } else if (id == R.id.nav_list) {
            Intent intent = new Intent(FiltersActivity.this, ListPetDrawer.class);
            startActivity(intent);

        } else if (id == R.id.nav_filters) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
