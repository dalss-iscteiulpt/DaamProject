package com.example.diogo.petsearcher;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Admin on 5/26/2017.
 */

public class PicassoClient {

    public  static  void downloadimg(Context c, String url, ImageView img)
    {
        if (url!=null && url.length()>0)
        {
            Picasso.with(c).load(url).placeholder(R.drawable.img_001).into(img);

        }else
        {
            Picasso.with(c).load(R.drawable.img_001).into(img);
        }
    }


}