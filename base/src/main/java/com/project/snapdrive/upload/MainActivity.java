package com.project.snapdrive.upload;
import com.project.snapdrive.R;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.project.snapdrive.upload.fragment.CameraFragment;

public class MainActivity extends Activity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    static final int PERMISSION_REQUEST_CAMERA = 1001;

    static final String LOG_TAG = "SNAP_DRIVE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, CameraFragment.newInstance())
                    .commit();
        }


//        //Instant App CAN'T have Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
//        if (!hasPermission(Manifest.permission.CAMERA)) {
//            Log.i(LOG_TAG, "We don't have Camera Permission");
//            askPermission(PERMISSION_REQUEST_CAMERA, Manifest.permission.CAMERA);
//        } else {
//            Log.i(LOG_TAG, "We have Camera Permission");
//            dispatchTakePictureIntent();
//        }
    }

    private boolean hasPermission(String... permissions) {

        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(this,
                    permission);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }

        return true;
    }


    private void askPermission(int requestCode, String... permissions) {

        ActivityCompat.requestPermissions(this, permissions, requestCode);

//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                permission)) {
//            // Show an explanation to the user *asynchronously* -- don't block
//            // this thread waiting for the user's response! After the user
//            // sees the explanation, try again to request the permission.
//        } else {
//            // No explanation needed, we can request the permission.
//            ActivityCompat.requestPermissions(this,
//                    new String[]{permission}, requestCode);
//        }

    }

    private void dispatchTakePictureIntent() {
        Log.i(LOG_TAG, "Open Camera");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        /*
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) { //first activity component that can handle the intent
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Log.i(LOG_TAG, "No Activity can handle this intent");
        }*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mImageView.setImageBitmap(imageBitmap);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                boolean permissionGranted = true;
                if (grantResults.length > 0){
                    for (int i=0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            permissionGranted = permissionGranted && true;
                        } else {
                            Log.i(LOG_TAG, "Permission Denied - " + permissions[i]);
                            permissionGranted = false;
                        }
                    }
                    permissionGranted = false;
                } else {
                    permissionGranted = false;
                }

                if (permissionGranted) {
                    Log.i(LOG_TAG, "Camera Permission Granted");
                    dispatchTakePictureIntent();
                } else {
                    Log.i(LOG_TAG, "Camera Permission Denied");
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CAMERA)) {
                        //Show permission explanation dialog...
                        AlertDialog.Builder builder
                                = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                        builder.setTitle("Camera Access Denied")
                                .setMessage("Please provide Camera access to capture photo")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO
                            }
                        })
                        .setNegativeButton("Don't need Camera", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                    } else {
                        //Never ask again selected, or device policy prohibits the app from having that permission.
                        //So, disable that feature, or fall back to another situation...
                    }
                }

                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
