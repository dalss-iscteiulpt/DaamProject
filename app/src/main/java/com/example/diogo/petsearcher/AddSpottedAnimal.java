package com.example.diogo.petsearcher;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class AddSpottedAnimal extends AppCompatActivity {

    protected TextView typeTxt;
    protected TextView breedTxt;
    protected TextView primTxt;
    protected TextView secTxt;
    protected Spinner genderSpinner;
    protected Spinner sizeSpinner;
    protected TextView phoneTxt;
    protected TextView emailTxt;
    protected TextView dateTimeTxt;
    protected TextView locTxt;
    protected ImageView pic;
    protected Button confirmBtn;


    protected boolean typeFirstClick;
    protected boolean breedFirstClick;
    protected boolean primFirstClick;
    protected boolean secFirstClick;
    protected boolean phoneFirstClick;
    protected boolean emailFirstClick;

    protected String coordloc;
    private String mCurrentPhotoPath;  // File path to the last image captured
    String realLocation;
    protected Context mContext;
    protected SpottedAnimal animal;
    private Uri mImageUri = null;

    private static final int CAMERA_PIC_REQUEST = 1;
    int PLACE_PICKER_REQUEST = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 101;

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
        sizeSpinner = (Spinner) findViewById(R.id.sizeSpinner);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);

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
                != PackageManager.PERMISSION_GRANTED) {
            askForCameraPermissions();
        } else {
            if (ContextCompat.checkSelfPermission(AddSpottedAnimal.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                askForStoragePermissions();
            } else {
                setViewerOfImages();
            }
        }

        dateTimeTxt = (TextView) findViewById(R.id.dateTime);
        dateTimeTxt.setText(DateFormat.getDateTimeInstance().format(new Date()));

        LatLng loc = (LatLng) getIntent().getParcelableExtra("location");
        locTxt = (TextView) findViewById(R.id.locationTxt);
        locTxt.setText(getRealLocation(loc.longitude, loc.latitude));

        confirmBtn = (Button)findViewById(R.id.confBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNewAnimal();

            }
        });
    }

    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format( new Date());
        String imageFileName = "JPEG_" + timeStamp + "_" ;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    public void setViewerOfImages() {
        pic = (ImageView) findViewById(R.id.imageView3);
        pic.setImageResource(R.drawable.camera_icon);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    Uri photoURI = null;
                    try {
                        photoURI = FileProvider.getUriForFile(
                                mContext,
                                BuildConfig.APPLICATION_ID + ".provider",
                                createImageFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(intent, CAMERA_PIC_REQUEST);

                }
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
            case STORAGE_PERMISSION_REQUEST_CODE:
                if (isPermissionGranted(permissions, grantResults, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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

    public void askForStoragePermissions(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle("Storage permessions needed")
                    .setMessage("you need to allow this permission!")
                    .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddSpottedAnimal.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    STORAGE_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_REQUEST_CODE);

        }
    }

    private void askForCameraPermissions() {

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
                        }
                    })
                    .show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);

        }
    }

    protected void setAnimalID(){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        animal.setSpotttedDate(dateFormat.format(date));
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        animal.setSpottedHour(dateFormat.format(date));
        String id= animal.getSpottedHour().replace(":","")+
                animal.getSpotttedDate().replace("/","");
        animal.setId(id);
    }



    protected void insertNewAnimal(){
        animal = new SpottedAnimal();
        setAnimalID();
        animal.setSize(sizeSpinner.getSelectedItem().toString());
        animal.setType(typeTxt.getText().toString());
        animal.setBreed(breedTxt.getText().toString());
        animal.setPrimaryC(primTxt.getText().toString());
        animal.setSecodnaryC(secTxt.getText().toString());
        animal.setGender(genderSpinner.getSelectedItem().toString());
        animal.setPhoneNr(phoneTxt.getText().toString());
        animal.setEmail(emailTxt.getText().toString());
        animal.setCoordLocation(coordloc);
        animal.setRealLocation(realLocation);
        animal.setCommentaries("");
        String id = animal.getId();
        animal.setPictureIDThumb("picture_Thumb"+id);
        animal.setPictureIDMain("picture_Main"+id);
        sendToFirebase(20,animal.getPictureIDMain());
        sendToFirebase(8,animal.getPictureIDThumb());
        mDatabase.child(animal.getId()).setValue(animal);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            try {
                mImageUri = Uri.parse(mCurrentPhotoPath);
                pic.setImageURI(null);
                pic.setImageURI(mImageUri);

            } catch (NullPointerException exp) {
                exp.printStackTrace();
            }
        } else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {

            }
        }

    }



    public void sendToFirebase(int sizePercent, String id){

        InputStream ims = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ims = getContentResolver().openInputStream(mImageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmapPic = BitmapFactory.decodeStream(ims);
        int sizeX = (int) (sizePercent*0.01*bitmapPic.getWidth());
        int sizeY = (int) (sizePercent*0.01*bitmapPic.getHeight());
        Log.d("SizeX", String.valueOf(sizeX));
        Log.d("SizeY", String.valueOf(sizeY));
        Bitmap imageBitmap = Bitmap.createScaledBitmap(bitmapPic, sizeX, sizeY, false);
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        byte[] data = baos.toByteArray();
        mStorageRef.child(id).putBytes(data);
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
