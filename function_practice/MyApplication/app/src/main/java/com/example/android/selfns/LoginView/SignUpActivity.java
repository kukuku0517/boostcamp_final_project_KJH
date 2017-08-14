package com.example.android.selfns.LoginView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.selfns.DailyView.MainActivity;
import com.example.android.selfns.Helper.FirebaseHelper;
import com.example.android.selfns.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.password;


public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.sign_up_id)
    EditText id;

    @BindView(R.id.sign_up_password)
    EditText pw;
    @BindView(R.id.sign_up_password_confirm)
    EditText pw_confirm;

    @BindView(R.id.sign_up_conirm)
    Button signUpConfirm;


    private FirebaseAuth mAuth;
    // ...
    private FirebaseAuth.AuthStateListener mAuthListener;
    String TAG = "firebaseauth";
    private Context context = this;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        signUpConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = id.getText().toString();
                String password = pw.getText().toString();
                String password_confirm = pw_confirm.getText().toString();

                if (password.equals(password_confirm)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(context, "SignUp Failed", Toast.LENGTH_SHORT).show();
                                    }else{

                                        FirebaseHelper.getInstance(context).setUserData();
                                    }

                                }
                            });
                } else {
                    Toast.makeText(context, "Password Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });


    } @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
