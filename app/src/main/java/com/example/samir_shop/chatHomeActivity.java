package com.example.samir_shop;

import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatHomeActivity extends AppCompatActivity implements View.OnClickListener {

    EditText userName,email,password;

    TextView ImageUrl,signUp;

    CircleImageView circleImageView;

    Button signIn,signInWithGoogle,signInWithFacebook;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    DatabaseReference databaseReference;

    GoogleSignInClient googleSignInClient;

    public int googleCode=1;

    public boolean go=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_home);

        firebaseAuth=FirebaseAuth.getInstance();

        firebaseUser=firebaseAuth.getCurrentUser();

databaseReference= FirebaseDatabase.getInstance().getReference("User").child("EmailUser");

if( firebaseUser.getUid().toString().length()>=1 ){

    logInWithRealTime();

}

        userName=(EditText)findViewById(R.id.UserName);
email=(EditText)findViewById(R.id.UserEmail);
password=(EditText)findViewById(R.id.UserEmailPassword) ;

circleImageView=findViewById(R.id.profileImage);

        ImageUrl=(TextView)findViewById(R.id.imageUrl);
signUp=(TextView)findViewById(R.id.SignUp);

signIn=(Button)findViewById(R.id.SignIn);
signInWithGoogle=(Button)findViewById(R.id.SignInGoogle);
signInWithFacebook=(Button)findViewById(R.id.SignInFacebook);

signIn.setOnClickListener(this);
signUp.setOnClickListener(this);
signInWithGoogle.setOnClickListener(this);
signInWithFacebook.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

if( v.getId()==R.id.SignIn ){

if( email.getText().toString().trim().length()>=1 && password.getText().toString().trim().length()>=1 && userName.getText().toString().trim().length()>=1 ){

firebaseAuth.signInWithEmailAndPassword( email.getText().toString().trim(),password.getText().toString().trim() )
.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {

        if( task.isSuccessful() ){

            Toast.makeText(getApplicationContext(),"Email authentication successfull",Toast.LENGTH_LONG).show();

 firebaseUser=firebaseAuth.getCurrentUser();

            logInWithRealTime();

        }else{

            Toast.makeText(getApplicationContext(),"Email authentication failed",Toast.LENGTH_LONG).show();

        }

    }
});

}else{

Toast.makeText(getApplicationContext(),"Empty information is not allowed",Toast.LENGTH_LONG).show();

}

}else if( v.getId()==R.id.SignUp ){

    Intent intent=new Intent( chatHomeActivity.this,SignUpActivity.class );

    startActivity(intent);

}else if( v.getId()==R.id.SignInGoogle ){

    go=true;

google();

}else if( v.getId()==R.id.SignInFacebook ){



}

    }

    private void google() {

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

       googleSignInClient= GoogleSignIn.getClient(this, gso);

       try {

           if( googleSignInClient==null ){

               throw new Exception("wrong");

           }

           Intent signInIntent = googleSignInClient.getSignInIntent();
           startActivityForResult(signInIntent, googleCode);

       }catch(Exception e){

           Toast.makeText(getApplicationContext(),"I get extension",Toast.LENGTH_LONG).show();

       }

    }

    public void logInWithRealTime(){

databaseReference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

        if( snapshot.child(firebaseUser.getUid().toString().trim()).exists() ){

       final SignUp_Mail_Helper_Class signUp_mail_helper_class=snapshot.child( firebaseUser.getUid().toString().trim() ).getValue( SignUp_Mail_Helper_Class.class );

       if( signUp_mail_helper_class.getEmail().equalsIgnoreCase( firebaseUser.getEmail() ) ){

           CountDownTimer countDownTimer=new CountDownTimer( 2000,1000 ) {
               @Override
               public void onTick(long millisUntilFinished) {

               }

               @Override
               public void onFinish() {

                   Picasso.get().load(Uri.parse( signUp_mail_helper_class.getImageUrl() )).into(circleImageView);

                   ImageUrl.setText( signUp_mail_helper_class.getImageUrl() );

               }
           };

           countDownTimer.start();

           Toast.makeText(getApplicationContext(),"The logged in successfully",Toast.LENGTH_LONG).show();

       }

        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode==googleCode ){

            Task< GoogleSignInAccount > task=GoogleSignIn.getSignedInAccountFromIntent(data);

            handleSignInResult(task);

        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {

        try{

            GoogleSignInAccount acc=task.getResult(ApiException.class);

            Toast.makeText(getApplicationContext(),"Logged in successfully",Toast.LENGTH_LONG).show();

            FirebaseGoogleAuth(acc);

        }catch (ApiException e){

            Toast.makeText(getApplicationContext(),"Sign In unsuccessfull",Toast.LENGTH_LONG).show();

        }

    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acc) {

        AuthCredential authCredential= GoogleAuthProvider.getCredential(acc.getIdToken(),null);

        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( task.isSuccessful() ){

                    Toast.makeText(getApplicationContext(),"successfull",Toast.LENGTH_LONG).show();

                    firebaseUser=firebaseAuth.getCurrentUser();

                    updateUI(firebaseUser);

                }else{

                    Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    private void updateUI(FirebaseUser firebaseUser) {

        try {

            Toast.makeText(getApplicationContext(), "name: " + firebaseUser.getDisplayName(), Toast.LENGTH_LONG).show();

            Toast.makeText(getApplicationContext(),"picture: "+firebaseUser.getPhotoUrl(),Toast.LENGTH_LONG).show();

            Toast.makeText(getApplicationContext(),"email: "+firebaseUser.getEmail(),Toast.LENGTH_LONG).show();

        }catch(Exception e){


        }

    }

}