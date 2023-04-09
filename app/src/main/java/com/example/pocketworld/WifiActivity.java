package com.example.pocketworld;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dictiopocket.R;

/*
Cette classe est l'activité ou l'utilisateur est redirigé
automatiquement apres une perte de connexion ou au lancement de
l'application sans une connexion au réseau WIFI
 */
public class WifiActivity extends AppCompatActivity {

    /*
   Cette méthode est appelée lors de la création de l'activité et
   permet de définir le contenu de l'écran en associant le fichier
   activity_network_error.xml à l'activité. Et appelle d'autres methodes
   pour verifier la connexion Internet
    */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_error);
        NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.register();

        if (networkConnection.isConnectedToWifi() == true) {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        }
        if (networkConnection.isConnectedToWifi() == false) {
            networkConnection.setWifi(false);
        }
    }

}
