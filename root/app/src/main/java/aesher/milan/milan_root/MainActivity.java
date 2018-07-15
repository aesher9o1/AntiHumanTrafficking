package aesher.milan.milan_root;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import aesher.milan.milan_root.constants.BaseActivity;
import aesher.milan.milan_root.constants.NewUsers;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;

public class MainActivity extends BaseActivity {


    @BindView(R.id.password)
    TextView password;

    @BindView(R.id.username)
    TextView userName;

    @BindView(R.id.privilege)
    TextView privilege;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goFullScreen();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /* Nullify any previous login instance */
        mAuth = null;

    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
    }




    public void doLogin(View v){
        if(!password.getText().toString().isEmpty()&&!userName.getText().toString().isEmpty()){

                mAuth.createUserWithEmailAndPassword(userName.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                /*Priority 1 means head, 2 means officer, 3 mean any other person having the app */

                                switch (Integer.parseInt(privilege.getText().toString())){
                                    case 1:
                                        createDatabaseEntryForOfficer("Tier1");
                                        break;
                                    case 2:
                                        createDatabaseEntryForOfficer("Tier2");
                                        break;
                                    case 3:
                                        createDatabaseEntryForOfficer("Tier3");
                                        break;
                                    default:
                                }


                            }
                        });


        }
    }

    public void missingChild(View view){
        Intent intent = new Intent(MainActivity.this, MissingChild.class);
        startActivity(intent);

    }

    private void createDatabaseEntryForOfficer(String reference){
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference(reference + "/" + mAuth.getUid());
        NewUsers users = new NewUsers("","","","");
        mDatabaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                makeToast("User has been successfully added to the database");
                mAuth = null;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                makeToast("An error has occurred");
            }
        });
    }




}
