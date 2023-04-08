package com.example.dictiopocket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WifiActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_error);
        NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.register();

        if(networkConnection.isConnectedToWifi() == true) {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        }
        if(networkConnection.isConnectedToWifi() == false) {
            networkConnection.setWifi(false);
        }
    }

}
