package com.dhbw.unilife;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // Die onCreate Funktion der MainActivity wird als erstes beim starten der app aufgerufen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Layout-Datei zuweisen
    }
}