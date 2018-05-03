package com.example.diogo.petsearcher;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class AddSpottedAnimal extends AppCompatActivity {

    protected TextView typeTxt;
    protected TextView breedTxt;
    protected TextView primTxt;
    protected TextView secTxt;
    protected TextView genderTxt;
    protected TextView sizeTxt;
    protected TextView phoneTxt;
    protected TextView emailTxt;
    protected TextView dateTimeTxt;
    protected TextView locTxt;
    protected ImageView pic;
    protected Button locBtn;
    protected Button confirmBtn;


    protected boolean typeFirstClick;
    protected boolean breedFirstClick;
    protected boolean primFirstClick;
    protected boolean secFirstClick;
    protected boolean genderFirstClick;
    protected boolean sizeFirstClick;
    protected boolean phoneFirstClick;
    protected boolean emailFirstClick;

    protected String coordloc;
    String realLocation;
    protected String dateInserted;
    protected Context mContext;
    protected SpottedAnimal animal;
    private FileProvider mImageUri = null;
    Bitmap bitmapPic;
    ByteArrayOutputStream baos;

    private static final int CAMERA_PIC_REQUEST = 1;
    int PLACE_PICKER_REQUEST = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    protected DatabaseReference mDatabase;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spotted_animal);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
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

        breedFirstClick = true;
        breedTxt = (TextView) findViewById(R.id.breedtxt);
        breedTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (breedFirstClick) {
                    ((TextView) view).setText("");
                    ((TextView) view).setTextColor(0xFF000000);
                    breedFirstClick = false;
                }
            }
        });

        primFirstClick = true;
        primTxt = (TextView) findViewById(R.id.primaryTxt);
        primTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (primFirstClick) {
                    ((TextView) view).setText("");
                    ((TextView) view).setTextColor(0xFF000000);
                    primFirstClick = false;
                }
            }
        });

        secFirstClick = true;
        secTxt = (TextView) findViewById(R.id.secondaryTxt);
        secTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (secFirstClick) {
                    ((TextView) view).setText("");
                    ((TextView) view).setTextColor(0xFF000000);
                    secFirstClick = false;
                }
            }
        });

        genderFirstClick = true;
        genderTxt = (TextView) findViewById(R.id.genderTxtAdd);
        genderTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (genderFirstClick) {
                    ((TextView) view).setText("");
                    ((TextView) view).setTextColor(0xFF000000);
                    genderFirstClick = false;
                }
            }
        });

        sizeFirstClick = true;
        sizeTxt = (TextView) findViewById(R.id.sizeTxt);
        sizeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sizeFirstClick) {
                    ((TextView) view).setText("");
                    ((TextView) view).setTextColor(0xFF000000);
                    sizeFirstClick = false;
                }
            }
        });

        phoneFirstClick = true;
        phoneTxt = (TextView) findViewById(R.id.phoneTxt);
        phoneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneFirstClick) {
                    ((TextView) view).setText("");
                    ((TextView) view).setTextColor(0xFF000000);
                    phoneFirstClick = false;
                }
            }
        });

        emailFirstClick = true;
        emailTxt = (TextView) findViewById(R.id.emailTxt);
        emailTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailFirstClick) {
                    ((TextView) view).setText("");
                    ((TextView) view).setTextColor(0xFF000000);
                    emailFirstClick = false;
                }
            }
        });

        if (ContextCompat.checkSelfPermission(AddSpottedAnimal.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            askForCameraPermissions();
        } else{
            setViewerOfImages();
        }

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

        confirmBtn = (Button)findViewById(R.id.confBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNewAnimal();

            }
        });
    }

    public void setViewerOfImages(){
        pic = (ImageView) findViewById(R.id.imageView3);
        pic.setImageResource(R.drawable.camera_icon);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (isPermissionGranted(permissions, grantResults, Manifest.permission.CAMERA)) {
                    setViewerOfImages();
                } else {
                    Toast.makeText(this, "Can not proceed! i need permission" , Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public static boolean isPermissionGranted(@NonNull String[] grantPermissions, @NonNull int[] grantResults,
                                              @NonNull String permission) {
        for (int i = 0; i < grantPermissions.length; i++) {
            if (permission.equals(grantPermissions[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }


    private void askForCameraPermissions() {

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {

            new android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle("Camera permessions needed")
                    .setMessage("you need to allow this permission!")
                    .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddSpottedAnimal.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    CAMERA_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
//                                        //Do nothing
                        }
                    })
                    .show();

            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);

            // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    protected void insertNewAnimal(){
        Date date = new Date();
        animal = new SpottedAnimal();
        animal.setType(typeTxt.getText().toString());
        animal.setBreed(breedTxt.getText().toString());
        animal.setPrimaryC(primTxt.getText().toString());
        animal.setSecodnaryC(secTxt.getText().toString());
        animal.setGender(genderTxt.getText().toString());
        animal.setSize(sizeTxt.getText().toString());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        animal.setSpotttedDate(dateFormat.format(date));
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        animal.setSpottedHour(dateFormat.format(date));
        animal.setPhoneNr(phoneTxt.getText().toString());
        animal.setEmail(emailTxt.getText().toString());
        animal.setCoordLocation(coordloc);
        animal.setRealLocation(realLocation);
        animal.setCommentaries("");
        String id= animal.getSpottedHour().replace(":","")+
                animal.getSpotttedDate().replace("/","");
        animal.setId(id);
        animal.setPictureID("picture_"+id);
        String path = "images/"+animal.getPictureID();
        byte[] data = baos.toByteArray();
        mStorageRef.child(animal.getPictureID()).putBytes(data);
        mDatabase.child(animal.getId()).setValue(animal);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST) {
            try {

                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                pic.setImageBitmap(thumbnail);
                encodeBitmap(thumbnail);

            } catch (NullPointerException exp) {
                exp.printStackTrace();
            }
        } else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {

            }
        }

    }

    public void encodeBitmap(Bitmap bitmap) {
        baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    protected String getRealLocation(double longitude, double latitude) {
        coordloc = Double.toString(longitude)+","+Double.toString(latitude);
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
