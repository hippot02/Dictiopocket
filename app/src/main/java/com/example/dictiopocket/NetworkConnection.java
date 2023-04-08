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

public class NetworkConnection extends ConnectivityManager.NetworkCallback {

    private Context context;
    private ConnectivityManager connectivityManager;

    private Boolean wifi;

    public NetworkConnection(Context context) {
        this.context = context;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifi = true;
    }

    public void register() {
        if (connectivityManager != null) {
            connectivityManager.registerDefaultNetworkCallback(this);
        }
    }

    public void unregister() {
        if (connectivityManager != null) {
            connectivityManager.unregisterNetworkCallback(this);
        }
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        checkWifiConnection();
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        checkWifiConnection();
    }

    @Override
    public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
    }

    private void checkWifiConnection() {
        if (isConnectedToWifi()) {
            Toast.makeText(context, "Connected to Wi-Fi", Toast.LENGTH_SHORT).show();
            if(wifi == false) {
                Intent i = new Intent(context, HomeActivity.class);
                context.startActivity(i);
            }
        } else {
            wifi = false;
            Toast.makeText(context, "Not connected to Wi-Fi", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, WifiActivity.class);
            context.startActivity(i);
        }
    }

    public boolean isConnectedToWifi() {
        if (connectivityManager != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))) {
                return true;
            }
        }
        return false;
    }

    public void setWifi(Boolean wifi) {
        this.wifi = wifi;
    }
}
