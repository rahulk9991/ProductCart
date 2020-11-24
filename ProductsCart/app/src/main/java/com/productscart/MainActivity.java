package com.productscart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ImageView iv_image;
    EditText tv_name,tv_price,tv_quantity,tv_description;
    Button btn_submit;
    FirebaseFirestore db;

    private ProgressDialog loadingBar;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private StorageReference ProductImagesRef;
    String downloadImageUrl;
    String name,price,quantity,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Add Product");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        loadingBar = new ProgressDialog(MainActivity.this);
        db = FirebaseFirestore.getInstance();

        iv_image=(ImageView)findViewById(R.id.iv_image);
        tv_name=(EditText)findViewById(R.id.tv_name);
        tv_price=(EditText)findViewById(R.id.tv_price);
        tv_quantity=(EditText)findViewById(R.id.tv_quantity);
        tv_description=(EditText)findViewById(R.id.tv_description);

        btn_submit=(Button)findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OpenGallery();
            }
        });
    }
    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            iv_image.setImageURI(ImageUri);
        }
    }

    private void ValidateProductData()
    {
        name = tv_name.getText().toString();
        price = tv_price.getText().toString();
        quantity = tv_quantity.getText().toString();
        description = tv_description.getText().toString();

        if (ImageUri == null)
        {
            Toast.makeText(MainActivity.this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(price))
        {
            Toast.makeText(this, "Please enter price...", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(quantity))
        {
            Toast.makeText(this, "Please enter quantity..", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(description))
        {
            Toast.makeText(this, "Please write description...", Toast.LENGTH_SHORT).show();
        }


        else
        {
            StoreProductInformation();
        }
    }
    private void StoreProductInformation()
    {
        loadingBar.setTitle("Create Account");
        loadingBar.setMessage("Please wait, while we are Creating ...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        StorageReference str = FirebaseStorage.getInstance().getReference();

        StorageReference filePath = str.child("Photos").child(ImageUri.getLastPathSegment());


        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(MainActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(MainActivity.this, "Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            //Toast.makeText(RegistrationActivity.this, "Profile image Url Successfully...", Toast.LENGTH_SHORT).show();

                            ValidateDetails();
                        }
                    }
                });
            }
        });
    }
    private void ValidateDetails() {

        HashMap<String, Object> userdataMap = new HashMap<>();
        userdataMap.put("name", name);
        userdataMap.put("price", price);
        userdataMap.put("quantity", quantity);
        userdataMap.put("description", description);
        userdataMap.put("Image", downloadImageUrl);

        db.collection("Products")
                .add(userdataMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, "Product Added Succussfully.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Firestore Database issue", Toast.LENGTH_SHORT).show();

                    }
                });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}