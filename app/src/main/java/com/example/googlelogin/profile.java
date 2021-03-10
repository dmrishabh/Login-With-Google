package com.example.googlelogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.media.TimedText;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profile extends AppCompatActivity {
    Button button_logOut;
    ImageView imageView;
    TextView textView;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        button_logOut = findViewById(R.id.btn_logout);
        imageView = findViewById(R.id.iv_imageView);
        textView = findViewById(R.id.tv_text);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            Glide.with(profile.this).load(firebaseUser.getPhotoUrl()).into(imageView);

            // set name on text view

            textView.setText(firebaseUser.getDisplayName());


        }
//      initialize sign in clint

        googleSignInClient = GoogleSignIn.getClient(profile.this, GoogleSignInOptions.DEFAULT_SIGN_IN);

        button_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sign out

                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // check condition

                        if (task.isSuccessful()){
                            firebaseAuth.signOut();

                            Toast.makeText(getApplicationContext(),"Logged out ",Toast.LENGTH_SHORT).show();

                            // finish activity

                            finish();

                        }
                    }
                });
            }
        });

    }
}