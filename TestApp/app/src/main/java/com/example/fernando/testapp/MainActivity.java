package com.example.fernando.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonToast_1, buttonToast_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  Button 1
        buttonToast_1 = (Button) findViewById(R.id.btnToast_1);
        buttonToast_1.setOnClickListener(this);
        //  Button 2
        buttonToast_2 = (Button) findViewById(R.id.btnToast_2);
        buttonToast_2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnToast_1) {
            Toast.makeText(getApplicationContext(), "Hej Hej!!", Toast.LENGTH_LONG).show();
        }

        if(v.getId() == R.id.btnToast_2) {
            Toast.makeText(getApplicationContext(), "Hola Hola!!", Toast.LENGTH_LONG).show();
        }
    }
}
