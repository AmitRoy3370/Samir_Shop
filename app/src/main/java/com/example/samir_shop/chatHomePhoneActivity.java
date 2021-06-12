package com.example.samir_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatHomePhoneActivity extends AppCompatActivity implements View.OnClickListener {

    EditText userName,phone,password;

    TextView ImageUrl,signUp;

    CircleImageView circleImageView;

    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_home_phone);

        userName=(EditText)findViewById(R.id.UserName);
        phone=(EditText)findViewById(R.id.UserPhone);
        password=(EditText)findViewById(R.id.UserPhonePassword);

        circleImageView=findViewById(R.id.profileImage);

        ImageUrl=(TextView)findViewById(R.id.imageUrl);
        signUp=(TextView)findViewById(R.id.SignUp);

        signIn=(Button)findViewById(R.id.SignIn);

        signIn.setOnClickListener(this);

        signUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

if( v.getId()==R.id.SignIn ){



}else if( v.getId()==R.id.SignUp ){

    Intent intent=new Intent(chatHomePhoneActivity.this,SignUpPhoneActivity.class);

    startActivity(intent);

}

    }

}