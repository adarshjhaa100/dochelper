package com.example.ayush.afinal;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.content.Context;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class PatientReport extends AppCompatActivity {
    PatientRecord rec;
    public final String TAG = "hi";
    ListView listview;
    ListAdapter adapter;
    String[] date;
    String[] docname;
    String[] image;
    Button b1,b2;


    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final int ACTION_TAKE_PHOTO_S = 2;
    private static final int ACTION_TAKE_VIDEO = 3;

    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";

    private Bitmap mImageBitmap;

    private static final String VIDEO_STORAGE_KEY = "viewvideo";
    private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";

    private Uri mVideoUri;

    private String mCurrentPhotoPath;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;


    /* Photo album for this application */
    private String getAlbumName() {
        return getString(R.string.album_name);
    }


    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */


		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */


		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;

        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */

        mVideoUri = null;


    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        switch (actionCode) {
            case ACTION_TAKE_PHOTO_B:
                File f = null;

                try {
                    f = setUpPhotoFile();
                    mCurrentPhotoPath = f.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                } catch (IOException e) {
                    e.printStackTrace();
                    f = null;
                    mCurrentPhotoPath = null;
                }
                break;

            default:
                break;
        } // switch

        startActivityForResult(takePictureIntent, actionCode);
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);
    }

    private void handleSmallCameraPhoto(Intent intent) {
        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap) extras.get("data");

        mVideoUri = null;


    }

    private void handleBigCameraPhoto() {

        if (mCurrentPhotoPath != null) {
            setPic();
            galleryAddPic();
            mCurrentPhotoPath = null;
        }

    }

    private void handleCameraVideo(Intent intent) {
        mVideoUri = intent.getData();

        mImageBitmap = null;


    }

    Button.OnClickListener mTakePicOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
                }
            };

    Button.OnClickListener mTakePicSOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
                }
            };

    Button.OnClickListener mTakeVidOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakeVideoIntent();
                }
            };

    ArrayList<PatientRecord> GetReports(String patid) {
        final ArrayList<PatientRecord> patrec = new ArrayList<PatientRecord>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Report").child(patid);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    rec = singleSnapshot.getValue(PatientRecord.class);
                    patrec.add(rec);
                    Log.d(TAG,rec.Date + "ASD");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        myRef.addValueEventListener(postListener);
        return patrec;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent ii = new Intent();
        ii = getIntent();
        b1 = findViewById(R.id.btn2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PatientReport.this,PatientStart.class));
            }
        });

        b2 = findViewById(R.id.btn1);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PatientReport.this,Main2Activity.class));
            }
        });
        ArrayList<PatientRecord> record = GetReports(ii.getExtras().getString("Patid"));
        docname = new String[]{"a8976", "a8465", "a7484", "a8978", "a7485", "a7845", "a9625", "a8753", "a8756", "a7845", "a8798"};
        date = new String[]{"16/12/2017","13/10/2018","20/01/2018","13/10/2018","18/06/2018","16/10/2018","14/05/2017","18/08/2018","15/07/2018","11/12/2018","12/11/2018"};

        image = new String[]{"https://firebasestorage.googleapis.com/v0/b/hackeam18.appspot.com/o/5c49122c-7296-4105-b1ac-066c71d0b477.jpg?alt=media&token=ab06dfff-7a0f-4c87-87f6-9fe3201531cd",
                "https://firebasestorage.googleapis.com/v0/b/hackeam18.appspot.com/o/article-2101832-11C11A01000005DC-876_224x386.jpg?alt=media&token=60a8464d-0216-43dc-96e2-133392f7583a",
                "https://firebasestorage.googleapis.com/v0/b/hackeam18.appspot.com/o/c07f680d-0ba5-4b1f-8e2b-1cf02503dd7f.jpg?alt=media&token=17771f57-94bf-407e-a904-e2c9869e79ad",
                "https://firebasestorage.googleapis.com/v0/b/hackeam18.appspot.com/o/c15c136d-2447-4c33-a717-8659cdcf5bc9.jpg?alt=media&token=77ff4e25-a620-4b6a-90c2-9ac5e693e592",
                "https://firebasestorage.googleapis.com/v0/b/hackeam18.appspot.com/o/cd420960-3f80-4f7e-82e3-3683c6c05408.jpg?alt=media&token=272520b5-b1c5-4359-b9c1-1249bd0eae3a",
                "https://firebasestorage.googleapis.com/v0/b/hackeam18.appspot.com/o/doc-prescription.jpg?alt=media&token=3a743037-528f-4794-b71f-eb2d8a1b2c6d",
                "https://firebasestorage.googleapis.com/v0/b/hackeam18.appspot.com/o/dr-sukumarmukherjee3.jpg?alt=media&token=4468535c-a8f0-479d-807d-1dd12674856a",
                "https://firebasestorage.googleapis.com/v0/b/hackeam18.appspot.com/o/eminent_doctor2.png?alt=media&token=918a5c3d-9d50-4adc-abb1-730d15e86fd0",
                "https://firebasestorage.googleapis.com/v0/b/hackeam18.appspot.com/o/medical-3.jpg?alt=media&token=77855f93-1aa6-40f7-9431-04044584362f",
                "https://firebasestorage.googleapis.com/v0/b/hackeam18.appspot.com/o/medical-prescription-drugs_www%5B1%5D.txt2pic.com.jpg?alt=media&token=3ccc6394-f2b2-4409-8226-03d1dbab41dd",
                "https://firebasestorage.googleapis.com/v0/b/hackeam18.appspot.com/o/RITCHIE%2C%20HAROLD%20SCRIPT%20FIG_%201.jpg?alt=media&token=34ee80f0-a5d1-4e06-811f-105211bf38c1"};


        listview = (ListView) findViewById(R.id.listview);
        adapter = new com.example.ayush.afinal.ListAdapter(this, docname, image, date);

        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(listview.getContext(), ItemView.class);
                // Pass all data version
                i.putExtra("docname", docname);

                // Pass all data image
                i.putExtra("image", image);
                // Pass a single position

                i.putExtra("position", position);
                i.putExtra("date", date);
                startActivity(i);
            }

        });

        mImageBitmap = null;
        mVideoUri = null;

        Button picBtn = (Button) findViewById(R.id.btnIntend);
        setBtnListenerOrDisable(
                picBtn,
                mTakePicOnClickListener,
                MediaStore.ACTION_IMAGE_CAPTURE
        );

        Button picSBtn = (Button) findViewById(R.id.btnIntendS);
        setBtnListenerOrDisable(
                picSBtn,
                mTakePicSOnClickListener,
                MediaStore.ACTION_IMAGE_CAPTURE
        );

        Button vidBtn = (Button) findViewById(R.id.btnIntendV);
        setBtnListenerOrDisable(
                vidBtn,
                mTakeVidOnClickListener,
                MediaStore.ACTION_VIDEO_CAPTURE
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_TAKE_PHOTO_B: {
                if (resultCode == RESULT_OK) {
                    handleBigCameraPhoto();
                }
                break;
            } // ACTION_TAKE_PHOTO_B

            case ACTION_TAKE_PHOTO_S: {
                if (resultCode == RESULT_OK) {
                    handleSmallCameraPhoto(data);
                }
                break;
            } // ACTION_TAKE_PHOTO_S

            case ACTION_TAKE_VIDEO: {
                if (resultCode == RESULT_OK) {
                    handleCameraVideo(data);
                }
                break;
            } // ACTION_TAKE_VIDEO
        } // switch
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
        outState.putParcelable(VIDEO_STORAGE_KEY, mVideoUri);
        outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
        outState.putBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY, (mVideoUri != null) );
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
        mVideoUri = savedInstanceState.getParcelable(VIDEO_STORAGE_KEY);



    }
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void setBtnListenerOrDisable(
            Button btn,
            Button.OnClickListener onClickListener,
            String intentName
    ) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(onClickListener);
        } else {
            btn.setText(
                    getText(R.string.cannot).toString() + " " + btn.getText());
            btn.setClickable(false);
        }
    }

    public void SetDate(String Date)
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String docid = mAuth.getCurrentUser().getEmail().split("@")[0];
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference docref = db.getReference("DoctorSchedule");
        Query q = docref.child(docid);

    }

}
