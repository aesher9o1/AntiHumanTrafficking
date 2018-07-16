package aesher.milan.milan_root;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.*;

import java.io.IOException;
import java.util.UUID;


import aesher.milan.milan_root.constants.BaseActivity;
import aesher.milan.milan_root.constants.LostChild;

public class MissingChild extends BaseActivity {
    private Button btnChoose, btnUpload;
    private ImageView imageView;
    private int detailCheck = 0;
    private Uri filePath;
    long time;
    FirebaseStorage storage;
    StorageReference storageReference;
    String downloadUrl = new String();
    private DatabaseReference mDatabase;


    private final int PICK_IMAGE_REQUEST = 71;


    Uri imageUri;
    String locationImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        goFullScreen();
        setContentView(R.layout.activity_missing_child);

        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imageView = (ImageView) findViewById(R.id.imgView);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            time= System.currentTimeMillis();
            StorageReference ref = storageReference.child("images/"+ Long.toString(time));

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            LinearLayout details = findViewById(R.id.linearLayout);
                            details.setVisibility(View.VISIBLE);
                            Toast.makeText(MissingChild.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MissingChild.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });

        }
    }

    public void registerComplaint(View view){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        EditText firstName = findViewById(R.id.first_name);
        EditText lastName = findViewById(R.id.last_name);
        EditText age = findViewById(R.id.age);
        EditText complexion = findViewById(R.id.complexion);
        EditText id1 = findViewById(R.id.id1);
        EditText id2 = findViewById(R.id.id2);
        EditText height = findViewById(R.id.height);
        EditText weight = findViewById(R.id.weight);
        EditText extra = findViewById(R.id.extraDetails);
        EditText contact = findViewById(R.id.contact);
        EditText incharge = findViewById(R.id.incharge);

        String first_name = firstName.getText().toString();
        String last_name = lastName.getText().toString();
        String age_string = age.getText().toString();
        String complexion_string = complexion.getText().toString();
        String id1_string = id1.getText().toString();
        String id2_string = id2.getText().toString();
        String height_string = height.getText().toString();
        String weight_string = weight.getText().toString();
        String extra_string = extra.getText().toString();
        String contact_string = contact.getText().toString();
        String incharge_string = incharge.getText().toString();

        if(first_name.isEmpty()){
            Toast.makeText(MissingChild.this,"enter first name",Toast.LENGTH_SHORT).show();

        }
        else if(age_string.isEmpty()){
            Toast.makeText(MissingChild.this,"enter age, if not known add approximate",Toast.LENGTH_SHORT).show();

        }
        else if (complexion_string.isEmpty()){
            Toast.makeText(MissingChild.this,"enter complexion",Toast.LENGTH_SHORT).show();

        }
        else if (height_string.isEmpty()){

            Toast.makeText(MissingChild.this,"enter height",Toast.LENGTH_SHORT).show();

        }
        else if (contact_string.isEmpty()){
            Toast.makeText(MissingChild.this,"enter Contact information",Toast.LENGTH_SHORT).show();

        }
        else if (incharge_string.isEmpty()){
            Toast.makeText(MissingChild.this,"enter Policeman incharge",Toast.LENGTH_SHORT).show();

        }
        else {

            try {
                storageReference.child("images/" + Long.toString(time)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'

                        downloadUrl = uri.toString();
                        Toast.makeText(MissingChild.this, "URL Saved", Toast.LENGTH_SHORT).show();
/// The string(file link) that you need
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
                LostChild lostChild = new LostChild(first_name, last_name, age_string, height_string, weight_string,
                        id1_string, id2_string, extra_string, contact_string, downloadUrl, complexion_string, incharge_string);
                if(!downloadUrl.isEmpty()){
                mDatabase.child("complaints").child(Long.toString(time)).setValue(lostChild);
                Toast.makeText(MissingChild.this, "Complaint Registered", Toast.LENGTH_SHORT).show();
                firstName.setText("");
                lastName.setText("");
                age.setText("");
                complexion.setText("");
                id1.setText("");
                id2.setText("");
                height.setText("");
                weight.setText("");
                extra.setText("");
                contact.setText("");
                incharge.setText("");
                }
                else
                {
                    Toast.makeText(MissingChild.this, "Photo URL Error, Try Pressing Again", Toast.LENGTH_SHORT).show();

                }


            } catch (Exception exception) {
                Toast.makeText(MissingChild.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();

            }

        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
