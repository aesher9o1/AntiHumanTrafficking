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
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MissingChild extends BaseActivity {



    private ImageView imageView;

    private Uri filePath;
    long time;
    private LostChild lostChild;
    FirebaseStorage storage;
    StorageReference storageReference;
    String downloadUrl ="";
    private DatabaseReference mDatabase;


    private final int PICK_IMAGE_REQUEST = 71;



    @OnClick(R.id.btnChoose)
        public void chooseImageForUpload(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @OnClick(R.id.btnUpload)
        public void uploadImagetoDB(){
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        goFullScreen();
        setContentView(R.layout.activity_missing_child);

        ButterKnife.bind(this);


        imageView = findViewById(R.id.imgView);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        makeToast("Upload the image first to lodge the complaint");

    }







    public void registerComplaint(View view){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final EditText firstName = findViewById(R.id.first_name);
        final EditText lastName = findViewById(R.id.last_name);
        final EditText age = findViewById(R.id.age);
        final EditText complexion = findViewById(R.id.complexion);
        final EditText id1 = findViewById(R.id.id1);
        final EditText id2 = findViewById(R.id.id2);
        final EditText height = findViewById(R.id.height);
        final EditText weight = findViewById(R.id.weight);
        final EditText extra = findViewById(R.id.extraDetails);
        final EditText contact = findViewById(R.id.contact);
        final EditText incharge = findViewById(R.id.incharge);

        final String first_name = firstName.getText().toString();
        final String last_name = lastName.getText().toString();
        final String age_string = age.getText().toString();
        final String complexion_string = complexion.getText().toString();
        final String id1_string = id1.getText().toString();
        final String id2_string = id2.getText().toString();
        final String height_string = height.getText().toString();
        final String weight_string = weight.getText().toString();
        final String extra_string = extra.getText().toString();
        final String contact_string = contact.getText().toString();
        final String incharge_string = incharge.getText().toString();

        if(first_name.isEmpty()){
            makeToast("Enter first name");
        }
        else if(age_string.isEmpty()){
            makeToast("Enter Approximate age");
        }
        else if (complexion_string.isEmpty()){
            makeToast("Enter complexion");
        }
        else if (height_string.isEmpty()){
            makeToast("Enter Height");
        }
        else if (contact_string.isEmpty()){
            makeToast("Enter person to contact");
        }
        else if (incharge_string.isEmpty()){
            makeToast("Enter policeman incharge");
        }
        else {


            try {
                storageReference.child("images/" + Long.toString(time)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'

                        downloadUrl = uri.toString();
                        Toast.makeText(MissingChild.this, "URL Saved", Toast.LENGTH_SHORT).show();
                        if(downloadUrl.isEmpty()){
                            Toast.makeText(MissingChild.this, "Please Press Again", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            lostChild = new LostChild(first_name, last_name, age_string, height_string, weight_string,
                                    id1_string, id2_string, extra_string, contact_string, downloadUrl, complexion_string, incharge_string);

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
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });



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
