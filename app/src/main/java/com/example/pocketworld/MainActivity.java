package com.example.pocketworld;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.dictiopocket.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
Cette classe est l'activité du jeu devine drapeau.
Cette clase contient plusieurs methodes pour gerer les interactions
de l'utilisateur (boutons), de requêter une API de pays,
de télécharger des images,de vérifier la connectivité réseau,
et de gérer l'affichage des données sur l'écran
 */
public class MainActivity extends AppCompatActivity {
    private String pays;
    private int streak;
    private TextView streakT;
    private MainActivity activity;
    private NetworkConnection networkConnection;


    /*
   Cette méthode est appelée lors de la création de l'activité et
   permet de définir le contenu de l'écran en associant le fichier
   activity_main.xml à l'activité. Et appelle d'autres methodes
   pour verifier la connexion Internet ou d'afficher ou d'afficher
   le contenu d'un pays
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.activity = this;
        getSupportActionBar().hide();

        gestionToolbar(); // Permet de gérer les images view dans une autre classe

        networkConnection = new NetworkConnection(this);
        networkConnection.register();

        if (networkConnection.isConnectedToWifi() == true) {
            streakT = findViewById(R.id.streak);
            streakT.setText("Vous êtes en serie de " + streak + " bonnes réponses");
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
        TextView reponse = findViewById(R.id.reponse);
        if (v.getId() == R.id.confirm) {
            EditText edt = (EditText) findViewById(R.id.editFlag);
            String nomPays = edt.getText().toString();
            Intent i = new Intent(this, HomeActivity.class);
            if (nomPays.equals(pays)) {
                CustomPopup popup = new CustomPopup(activity);
                popup.setTitle("Félicitation");
                popup.setSubtitle("Vous avez trouvé la bonne réponse. Voulez vous rejouer ?");
                popup.build();
                popup.getYesButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup.dismiss();
                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setImageBitmap(null);
                        RequestTask rq = new RequestTask();
                        rq.execute();
                        streak += 1;
                        streakT.setText("Vous êtes en serie de " + streak + " bonnes réponses");
                        reponse.setText("");
                        EditText edt = findViewById(R.id.editFlag);
                        edt.setText(null);
                    }
                });
                popup.getNoButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup.dismiss();
                        startActivity(i);
                    }
                });
            } else {
                streak = 0;
                streakT.setText("Vous êtes en serie de " + streak + " bonnes réponses");
                Toast.makeText(getApplicationContext(), "Mauvaise réponse !", Toast.LENGTH_SHORT).show();
            }
        }
        if (v.getId() == R.id.again) {
            reponse.setText("");
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(null);
            streak = 0;
            streakT.setText("Vous êtes en serie de " + streak + " bonnes réponses");

            RequestTask rq = new RequestTask();
            rq.execute();
        }

        if (v.getId() == R.id.show) {
            reponse.setText(pays);
            streak = 0;
            streakT.setText("Vous êtes en serie de " + streak + " bonnes réponses");
        }

        if (v.getId() == R.id.tbDevinPaysButton) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
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
            String jsoCommon = jsoFra.getString("common");
            pays = jsoCommon;
            JSONObject jsoFlag = jsoPays.getJSONObject("flags");
            String flagURL = jsoFlag.getString("png");
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
                new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(decodeJSA(jsa));
            } catch (Exception e) {

            }
        }
    }

    /*
    Cette classe interne hérite de la classe AsyncTask
    et permet de télécharger une image à partir d'une URL donnée
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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