package com.app.reservapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    EditText from, to;
    Spinner nbrPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        nbrPlace = findViewById(R.id.spinner2);
    }

    public void go(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("from", from.getText().toString());
        intent.putExtra("to", to.getText().toString());
        intent.putExtra("number", Integer.parseInt(nbrPlace.getSelectedItem().toString()));
        startActivity(intent);
    }
}