package com.example.kalyansai.noticeboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

public class EditNotices extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private static final int SELECTED_PICTURE=1;
    String valu;
    Uri ura;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notices);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        progressDialog = new ProgressDialog(this);
        Intent i =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,SELECTED_PICTURE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECTED_PICTURE) {
            switch (requestCode) {
                case SELECTED_PICTURE:
                    if (resultCode == RESULT_OK) {
                        ura = data.getData();
                        String[] projection = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(ura, projection, null, null, null);
                        cursor.moveToFirst();


                        int columnIndex = cursor.getColumnIndex(projection[0]);
                        String filepath = cursor.getString(columnIndex);
                        cursor.close();

                        Bitmap yourselectedimage = BitmapFactory.decodeFile(filepath);
                        Drawable d = new BitmapDrawable(yourselectedimage);
                    }
                    break;
                default:
                    break;
            }
        }
        UploadImage();
    }

    private void UploadImage() {
            Uri file = ura;
            if (file != null) {
                final ProgressDialog prograssdialog = new ProgressDialog(this);
                prograssdialog.setTitle("Uploading");
                prograssdialog.show();
                prograssdialog.setCancelable(false);
                prograssdialog.setCanceledOnTouchOutside(false);
                StorageReference riversRef = storageReference.child("images/" + "shdfha" +".jpg");
                riversRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                prograssdialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Image Uploaded Successfully ",Toast.LENGTH_LONG).show();
                                Intent  i = new Intent(getApplicationContext(),finalActivity.class);
                                startActivity(i);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                                // ...
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double prograss =  (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                                prograssdialog.setMessage(((int) prograss) + "% Uploaded....");
                            }
                        });
            }

    }
}
