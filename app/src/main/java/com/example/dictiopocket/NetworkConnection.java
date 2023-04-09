package com.example.dictiopocket;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.logging.Handler;

/*
Cette classe vérifie l'état de la connexion réseau.
Elle hérite de la classe "ConnectivityManager.NetworkCallback"
pour surveiller les modifications de l'état de la connectivité réseau
 */
public class NetworkConnection extends ConnectivityManager.NetworkCallback {

    private Context context;
    private ConnectivityManager connectivityManager;

    private Boolean wifi;

    /*
    Le constructeur de la classe prend un objet "Context" en paramètre,
    qui est utilisé pour initialiser les variables de la classe.
     */
    public NetworkConnection(Context context) {
        this.context = context;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifi = true;
    }
    /*
    La méthode register() est appelée pour enregistrer l'objet "NetworkCallback"
     */
    public void register() {
        if (connectivityManager != null) {
            connectivityManager.registerDefaultNetworkCallback(this);
        }
    }
    /*
    La méthode unregister() est appelée pour retirer l'objet "NetworkCallback"
     */
    public void unregister() {
        if (connectivityManager != null) {
            connectivityManager.unregisterNetworkCallback(this);
        }
    }

    /*
    La méthode onAvailable() est appelée lorsque la connexion réseau est disponible,
    et fait appelle à la methode checkWifiConnected()
     */
    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        checkWifiConnection();
    }

    /*
    La méthode onLost() est appelée lorsque la connexion réseau est perdu,
    et fait appelle à la methode checkWifiConnected()
     */
    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        checkWifiConnection();
    }

    /*
    La méthode checkWifiConnection() vérifie si l'appareil est connecté
    à un réseau Wi-Fi en appelant la méthode isConnectedToWifi().
    Si la connexion est établie, un message de notification est affiché à l'utilisateur,
    puis il est redirigé vers l'activité d'accueil de l'application.
    Si la connexion n'est pas établie, un message d'erreur est affiché à l'utilisateur,
    puis il est redirigé vers une activité d'erreur de la connexion Wi-Fi
     */
    private void checkWifiConnection() {
        if (isConnectedToWifi()) {
            if (wifi == false) {
                Toast.makeText(context, "Connecté au WIFI", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, HomeActivity.class);
                context.startActivity(i);
            }
        } else {
            wifi = false;
            Toast.makeText(context, "Problème de connexion", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, WifiActivity.class);
            context.startActivity(i);
        }
    }

    /*
    La méthode isConnectedToWifi() utilise la classe ConnectivityManager()
    pour obtenir les informations sur la connectivité réseau actuelle.
    Si l'appareil est connecté à un réseau Wi-Fi ou cellulaire,
    la méthode retourne "true". Sinon, elle retourne "false"
     */
    public boolean isConnectedToWifi() {
        if (connectivityManager != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))) {
                return true;
            }
        }
        return false;
    }

    /*
    La méthode setWifi() est utilisée pour modifier la variable "wifi"
     */
    public void setWifi(Boolean wifi) {
        this.wifi = wifi;
    }
}
