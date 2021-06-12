package com.example.samir_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PhoneOrEmailChooserActivity extends AppCompatActivity implements View.OnClickListener {

    Button email,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_or_email_chooser);

        email=(Button)findViewById(R.id.SignInEmail);
        phone=(Button)findViewById(R.id.SignInPhone);

        email.setOnClickListener(this);
        phone.setOnClickListener(this);

        email.setOnClickListener(this);
        phone.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if( v.getId()==R.id.SignInEmail ){

            Intent intent=new Intent( PhoneOrEmailChooserActivity.this,chatHomeActivity.class );

            startActivity(intent);

        }else if( v.getId()==R.id.SignInPhone ){

Intent intent=new Intent(PhoneOrEmailChooserActivity.this,chatHomePhoneActivity.class);

startActivity(intent);

        }

    }
}