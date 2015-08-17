package com.example.vietnguyen.controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vietnguyen.core.database.DBHelper;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.myapplication.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormFragment extends MyFragment {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    private ImageView preview;
    private EditText title;
    private EditText description;
    private TextView previewText;
    private Bitmap photo;
    private DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment, retrieve ui elements
        View view = inflater.inflate(R.layout.fragment_form, container, false);
        preview = (ImageView) view.findViewById(R.id.imageView);
        previewText = (TextView) view.findViewById(R.id.previewText);
        title = (EditText) view.findViewById(R.id.titleText);
        description = (EditText) view.findViewById(R.id.descriptionText);
        Button btnSave = (Button) view.findViewById(R.id.btnSave);

        //setup event listener
        setOnClickPreview(preview);
        setOnClickSave(btnSave);

        return view;
    }

    private void setOnClickPreview(View preview) {
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final CharSequence[] items = {
                    getString(R.string.take_photo),
                    getString(R.string.take_video),
                    getString(R.string.cancel)
                };
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int option) {
                        switch (option) {
                            case 0: //take photo
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                                break;
                            case 1:
                                break;
                            default:
                                return;
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

    private void setOnClickSave(Button btnSave) {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleText = title.getText().toString();
                String descriptionText = description.getText().toString();
                if (photo != null && !titleText.equals("") && !descriptionText.equals("")) {
                    File sd = Environment.getExternalStorageDirectory();
                    String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png";
                    saveBitmapPhoto(photo, sd, fileName);
                    //save image information to database, update form screen
                    dbHelper.insertImage(titleText, descriptionText, sd + "/" + fileName);
                    updateUI();
                } else {
                    Log.e("######", "Todo: make a toast to remind user take a photo");
                    Toast.makeText(getActivity(), getString(R.string.remind),
                        Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void saveBitmapPhoto(Bitmap bitmapPhoto, File sd, String fileName) {
        File dest = new File(sd, fileName);
        try {
            FileOutputStream out = new FileOutputStream(dest);
            bitmapPhoto.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUI() {
        photo = null;
        previewText.setVisibility(View.VISIBLE);
        preview.setImageDrawable(null);
        title.setText("");
        description.setText("");
    }

    /*
    After taking a photo
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FormFragment.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                ImageView preview = (ImageView) getView().findViewById(R.id.imageView);
                photo = (Bitmap) data.getExtras().get("data");
                preview.setImageBitmap(photo);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("#######", "Capture image: result canceled");
            } else {
                Log.e("#######", "Capture image: result undefined");
            }
        }
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediafileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
