package com.example.samir_shop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    CircleImageView profileImage;

    EditText Name,Email,Password;

    TextView ImageUrl;

    Button signUp,uploadImage;

    public Uri uri;

    String url;

    DatabaseReference databaseReference;

    StorageReference storageReference;

    private FirebaseAuth mAuth;

    public static final int imageRequest=1;

    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        databaseReference= FirebaseDatabase.getInstance().getReference("User").child("EmailUser");

        storageReference= FirebaseStorage.getInstance().getReference("ProfileImage");

profileImage=findViewById(R.id.ProfileImage);

Name=(EditText)findViewById(R.id.UserName);

Email=(EditText)findViewById(R.id.UserEmail);

Password=(EditText)findViewById(R.id.UserEmailPassword);

ImageUrl=(TextView)findViewById(R.id.imageUrl);

signUp=(Button)findViewById(R.id.SignUp);

uploadImage=(Button)findViewById(R.id.UploadImage);

signUp.setOnClickListener(this);
uploadImage.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

if( v.getId()==R.id.SignUp ){

if( Name.getText().toString().trim().length()>=1 && Email.getText().toString().trim().length()>=1 && Password.getText().toString().trim().length()>=1 && ImageUrl.getText().toString().trim().length()>=1  ){

    mAuth.createUserWithEmailAndPassword(Email.getText().toString().trim(),Password.getText().toString().trim())
    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {

            if( task.isSuccessful() ){

                Toast.makeText( getApplicationContext(),"Email authetication succesfull",Toast.LENGTH_LONG ).show();

FirebaseUser firebaseUser=mAuth.getCurrentUser();

String uid=firebaseUser.getUid().toString();

                SignUp_Mail_Helper_Class signUp_mail_helper_class=new SignUp_Mail_Helper_Class(
                        Name.getText().toString().trim(),
                        Email.getText().toString().trim(),
                        uid,
                        ImageUrl.getText().toString().trim()
                );

                databaseReference.child(uid).setValue(signUp_mail_helper_class).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if( task.isSuccessful() ){

                            Toast.makeText(getApplicationContext(),"Realtime storage successfull",Toast.LENGTH_LONG).show();

                        }else{

                            Toast.makeText( getApplicationContext(),"Storage failure",Toast.LENGTH_LONG ).show();

                        }

                    }
                });

            }else{

                Toast.makeText(getApplicationContext(),"authentication failed",Toast.LENGTH_LONG).show();

            }

        }
    });

}else{

    Toast.makeText(getApplicationContext(),"The empty information is not allowed",Toast.LENGTH_LONG).show();

}


}else if( v.getId()==R.id.UploadImage ){

if( Name.getText().toString().trim().length()>=1 ){

imageChooser();

}else{

    Toast.makeText(getApplicationContext(),"Please enter your image name",Toast.LENGTH_LONG).show();

}

}

    }

    private void imageChooser() {

        Intent intent=new Intent();

        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent,imageRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode==imageRequest && resultCode==RESULT_OK && data!=null && data.getData()!=null ){

            uri=data.getData();

            Picasso.get().load(uri).into(profileImage);

            saveData();

        }

    }

    public String getExtension(Uri url){

        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();

return  mimeTypeMap.getExtensionFromMimeType( contentResolver.getType(url) );

    }

    private void saveData() {

        String ImageName=Name.getText().toString().trim();

if( ImageName.isEmpty() ){

    Toast.makeText(getApplicationContext(),"Please enter the image name",Toast.LENGTH_LONG).show();

    return;

}else{

final StorageReference mountainsRef=storageReference.child( Name.getText().toString().trim()+"."+getExtension(uri) );

mountainsRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

Toast.makeText(getApplicationContext(),"The image is stored successfully",Toast.LENGTH_LONG).show();

        Task<Uri> task=taskSnapshot.getStorage().getDownloadUrl();

        while( !task.isSuccessful() );//{

            uri=task.getResult();

        //}

        url=uri.toString();

        ImageUrl.setText(url);

    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {

        Toast.makeText(getApplicationContext(),""+e.toString(),Toast.LENGTH_LONG).show();

    }
});

}

    }

}