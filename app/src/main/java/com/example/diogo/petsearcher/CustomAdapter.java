package com.example.diogo.petsearcher;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Diogo on 17/04/2018.
 */

public class CustomAdapter extends BaseAdapter {

    Context c;
    ArrayList<SpottedAnimal> animalList;
    LayoutInflater inflater;


    public CustomAdapter(Context c, ArrayList<SpottedAnimal> animalList) {
        this.c = c;
        this.animalList = animalList;
    }

    @Override
    public int getCount() {
        return animalList.size();
    }

    @Override
    public Object getItem(int i) {
        return animalList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup viewGroup) {

        SpottedAnimal animal = animalList.get(i);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        //conditionally inflate either standard or special template
        View view = inflater.inflate(R.layout.array_format, null);


        TextView description = (TextView) view.findViewById(R.id.description);
        TextView typeBreedTxt = (TextView) view.findViewById(R.id.desc1txt);
        TextView dateTime = (TextView) view.findViewById(R.id.primaryTxt);
        ImageView image = (ImageView) view.findViewById(R.id.image);

        String typeBreed = animal.getType()+" "+animal.getBreed();
        String descriptiontxt = "Primary Color: "+animal.getPrimaryC()+"\n"
                +"Secondary Color: "+animal.getSecodnaryC()+"\n"
                +"Gender: "+animal.getGender();
        String dateTimetxt = animal.getSpotttedDate()+" "+animal.getSpottedHour();

        description.setText(descriptiontxt);
        typeBreedTxt.setText(typeBreed);
        dateTime.setText(dateTimetxt);
        //get the image associated with this property
//        int imageID = context.getResources().getIdentifier(String.valueOf(animal.getPictureID()), "drawable", context.getPackageName());
//        image.setImageResource(imageID);
        PicassoClient.downloadimg(c,animalList.get(i).getPictureID(),image);

        return view;

//        if (inflater== null)
//        {
//            inflater=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        } if(convertview==null)
//        {
//            convertview= inflater.inflate(R.layout.listview_layout,viewGroup,false);
//
//        }
//
//        MyHolder holder= new MyHolder(convertview);
//        holder.nameTxt.setText(dogies.get(i).getName());
//        PicassoClient.downloadimg(c,dogies.get(i).getUrl(),holder.img);
//
//
//
//        return convertview;
    }

}


