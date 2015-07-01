package com.example.n33r.addcontactlocal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    Dialog dialog;
    Spinner spinner;
    ImageView contactimage;
    private int imagerequest = 1;
    private int camerarequest = 2;
    int rotate = 45;

    //String imageDecode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (Spinner) findViewById(R.id.groupselect);
        contactimage = (ImageView) findViewById(R.id.profile_image);
        contactimage.setImageResource(R.drawable.bolod);
        Spinnermethod();
        contactimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(MainActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.customdialogaddpicture, null, false);
                dialog.setContentView(view);
                dialog.setTitle("Photo Option");
                Button camerabtn = (Button) dialog.findViewById(R.id.CameraButton);
                Button gallerybtn = (Button) dialog.findViewById(R.id.GalerryButton);
                Button removebtn = (Button) dialog.findViewById(R.id.removeButton);
                if (HasCamera() == true) {
                    camerabtn.setVisibility(View.VISIBLE);
                    Log.d("dd", "Dukche re : " + HasCamera());
                } else
                    camerabtn.setVisibility(View.INVISIBLE);
                camerabtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(camera, camerarequest);
                        dialog.dismiss();
                    }
                });

                gallerybtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent imageintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(imageintent, imagerequest);
                        dialog.dismiss();
                    }
                });

                if (contactimage.getTag() != null && contactimage.getTag().toString().equals("check")) {
                    removebtn.setVisibility(View.INVISIBLE);
                } else {
                    removebtn.setVisibility(View.VISIBLE);
                    contactimage.setTag("check");

                }
                removebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contactimage.setImageResource(R.drawable.bolod);
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

    }

    //spinner
    public void Spinnermethod() {
        String[] spinnertext = getResources().getStringArray(R.array.spinnertext);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < spinnertext.length; i++) {
            list.add(spinnertext[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(adapter);
    }

    private boolean HasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {

                    Uri uri = data.getData();
                    Log.d("galary", "" + RESULT_OK);

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        //contactimage.setRotation(Float.parseFloat("-90"));
                        contactimage.setImageBitmap(bitmap);
                        contactimage.setTag("failed");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                break;
            }
            case 2: {
                if (resultCode == RESULT_OK && data != null && data != null) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    contactimage.setImageBitmap(bitmap);
                    contactimage.setTag("cameraimage");
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                break;
            }

        }
        /*try {
            if (requestCode == imagerequest && null!=data && resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String[] filepath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, filepath, null, null, null);
                cursor.moveToFirst();
                int colomindex = cursor.getColumnIndex(filepath[0]);
                imageDecode = cursor.getString(colomindex);
                cursor.close();
                contactimage.setImageBitmap(BitmapFactory.decodeFile(imageDecode));
            } else {
                Toast.makeText(getApplicationContext(), "having problem", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "something problem", Toast.LENGTH_SHORT).show();
        }*/

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
        if (id == R.id.rotateimage) {
            contactimage.setRotation(Float.parseFloat(String.valueOf(rotate)));
            rotate+=45;
            if(rotate==360)
            {
                rotate=0;
            }

            return true;
        }

        return super.

                onOptionsItemSelected(item);
    }
}
