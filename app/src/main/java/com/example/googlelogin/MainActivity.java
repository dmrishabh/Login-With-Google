package com.example.googlelogin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    SignInButton btn;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.googleBtn);

        // initialize sign in option

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("942633758424-5573115vc3ss6p8b3m1uedo541icepdq.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Initialize Sign in client

        googleSignInClient = GoogleSignIn.getClient(MainActivity.this, googleSignInOptions);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // initialize sign in intent

                Intent intent = googleSignInClient.getSignInIntent();

                // start activity for result
                startActivityForResult(intent, 100);
            }
        });

        //intitialize firebase auth

        firebaseAuth = FirebaseAuth.getInstance();
        // initialize firebase user

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        // check  condition
        if (firebaseUser != null){
            //when user is alrdy logged in redirect to progile activity
            startActivity(new Intent(MainActivity.this,
                    profile.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check condition

        if (requestCode == 100) {
            // initialize task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            // check condition
            if (signInAccountTask.isSuccessful()) {
//                When google sign in sucess full
                // intit a string
                String string = "Google Sign in successfull";
                displayToast(string);

                try {
                    //initialize signing account
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    //check condition
                    if (googleSignInAccount != null) {

                        // initialize auth credential
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        // check credrntials
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        //check condition
                                        if (task.isSuccessful()) {
                                            // redirect to profile activity
                                            startActivity(new Intent(MainActivity.this,
                                                    profile.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                            displayToast("Firebase authentication successful");
                                        }
                                        else {
                                            displayToast("Authentication Failed :"+ task.getException().getMessage());
                                        }
                                    }
                                });


                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private void displayToast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }
}