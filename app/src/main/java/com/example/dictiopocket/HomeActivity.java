package com.example.dictiopocket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

/*
Cette classe est l'activité qui se charge en premier avec des informations sur un pays.
Cette clase contient plusieurs methodes pour gerer les interactions
de l'utilisateur (boutons), de requêter une API de pays,
de télécharger des images,de vérifier la connectivité réseau,
et de gérer l'affichage des données sur l'écran
 */
public class HomeActivity extends AppCompatActivity {

    private String name, area, population, capital, mapUrl;
    private WebView webView;
    private NetworkConnection networkConnection;

    /*
    Cette méthode est appelée lors de la création de l'activité et
    permet de définir le contenu de l'écran en associant le fichier
    activity_home.xml à l'activité. Et appelle d'autres methodes
    pour verifier la connexion Internet ou d'afficher ou d'afficher
    le contenu d'un pays
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        gestionToolbar(); // Permet de gérer la toolbar

        networkConnection = new NetworkConnection(this);
        networkConnection.register();

        if (networkConnection.isConnectedToWifi() == true) {
            webView = findViewById(R.id.mapView);

            RequestTask rq = new RequestTask();
            rq.execute();
        } else {
            Intent i = new Intent(this, WifiActivity.class);
            startActivity(i);
        }

    }

    /*
    Cette méthode est appelée lorsqu'un utilisateur clique sur un
    élément de l'interface utilisateur (bouton)
     */
    public void onClick(View v) {

        if (v.getId() == R.id.mapBtn) {
            webView.loadUrl(mapUrl);
        }

        if (v.getId() == R.id.nouveauPays) {
            RequestTask rq = new RequestTask();
            rq.execute();
        }
    }

    /*
    Cette méthode génère un nombre aléatoire compris entre min et max.
    Elle permet par la suite de choisir un pays aléatoire parmis les 250
    possible lors de la requête
     */
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


    /*
    Cette classe interne hérite de la classe AsyncTask  permet de réaliser une requête HTTP
    sur une API(restcountries) et de décoder les données JSON renvoyées par
    l'API et de les afficher sur l'ecran
     */
    private class RequestTask extends AsyncTask<Void, Void, String> {

        /*
        Cette méthode execute la méthode requete et retourne le resultat obtenu
         */
        protected String doInBackground(Void... voids) {
            String response = requete();
            return response;
        }

        /*
        Cette méthode effectue une requete HTTP sur L'API (restcountries)
        et retourne le rasultat JSON en une chaîne de caractères
         */
        private String requete() {
            String response = "";
            try {
                HttpURLConnection connection = null;
                URL url = new URL("https://restcountries.com/v3.1/all");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new
                        InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String ligne = bufferedReader.readLine();
                while (ligne != null) {
                    response += ligne;
                    ligne = bufferedReader.readLine();
                }

            } catch (UnsupportedEncodingException e) {
                response = "problème d'encodage";
            } catch (MalformedURLException e) {
                response = "problème d'URL ";
            } catch (IOException e) {
                response = "problème de connexion ";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        /*
        Cette méthode decode un JSONARRAY reçu en parametre
         */
        private String decodeJSA(JSONArray jso) throws Exception {
            String response = "";
            int m = getRandomNumber(0, 249);

            JSONArray jsonArray = new JSONArray(jso.toString());
            JSONObject jsoPays = jsonArray.getJSONObject(m);
            JSONObject jsoTranslations = jsoPays.getJSONObject("translations");
            JSONObject jsoFra = jsoTranslations.getJSONObject("fra");
            name = jsoFra.getString("common");
            area = jsoPays.getString("area");
            population = jsoPays.getString("population");
            JSONArray data = jsoPays.getJSONArray("capital");
            capital = data.getString(0);
            //capital = jsoPays.getString("capital");
            JSONObject jsoFlag = jsoPays.getJSONObject("flags");
            String flagURL = jsoFlag.getString("png");
            JSONObject jsoMap = jsoPays.getJSONObject("maps");
            mapUrl = jsoMap.getString("googleMaps");
            response = flagURL;
            return response;
        }

        /*
        Cette méthode est appelée après l'exécution de la méthode doInBackground(),
        elle appelle la methode decodeJSA() pour decoder la reponse de l'API
        et permet d'afficher les données du pays sur l'écran
         */
        protected void onPostExecute(String result) {
            JSONArray jsa;
            try {
                jsa = new JSONArray(result);
                new DownloadImageTask((ImageView) findViewById(R.id.drapeauPays)).execute(decodeJSA(jsa));
                DecimalFormat formatter = new DecimalFormat("#,###,###,###");
                TextView nomPays = findViewById(R.id.nomPays);
                TextView areaPays = findViewById(R.id.areaPays);
                TextView populationPays = findViewById(R.id.populationPays);
                TextView capitalPays = findViewById(R.id.capitalPays);
                TextView areaP = findViewById(R.id.area);
                TextView populationP = findViewById(R.id.population);
                TextView capitalP = findViewById(R.id.capital);
                if (name.length() > 16) {
                    nomPays.setTextSize(30);
                } else {
                    nomPays.setTextSize(50);
                }
                nomPays.setText(name);
                areaP.setText("Surface du pays :");
                areaPays.setText(formatter.format(Integer.parseInt(area)) + " km²");
                populationP.setText("Population :");
                populationPays.setText(formatter.format(Integer.parseInt(population)));
                capitalP.setText("Capitale du pays :");
                capitalPays.setText(capital);
            } catch (Exception e) {

            }
        }
    }

    /*
    Cette classe interne hérite de la classe AsyncTask
    et permet de télécharger une image à partir d'une URL donnée
     */
    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        /*
        Cette méthode permet de télécharge l'image (drapeau du pays) en appelant
        la méthode downloadImage()
         */
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        /*
        Cette méthode est appelée après l'exécution de la méthode doInBackground()
        et permet d'afficher l'image téléchargée sur l'écran.
         */
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);

        }
    }

    /*
    Cette méthode permet de gérer la barre d'action de l'application
     */
    private void gestionToolbar() {
        ImageView home_icon = findViewById(R.id.home_icon);
        Button tbDevinPaysButton = findViewById(R.id.tbDevinPaysButton);
        Button tbQuizzButton = findViewById(R.id.tbQuizzButton);
        home_icon.setOnClickListener(view -> {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        });
        tbDevinPaysButton.setOnClickListener(view -> {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        });

        tbQuizzButton.setOnClickListener(view -> {
            Intent i = new Intent(this, QuizzActivity.class);
            startActivity(i);
        });

    }
}
