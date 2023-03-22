package com.example.dictiopocket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class HomeActivity extends AppCompatActivity {

    private String name, area, population, capital;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RequestTask rq = new RequestTask();
        rq.execute();
    }

    public void onClick(View v) {
        if(v.getId() == R.id.jeuDrapeau) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }
    }
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private class RequestTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... voids) {
            String response = requete();
            return response;
        }

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
            response = flagURL;
            return response;
        }
        protected void onPostExecute(String result){
            JSONArray jsa;
            try {
                jsa = new JSONArray(result);
                new DownloadImageTask((ImageView) findViewById(R.id.drapeauPays)).execute(decodeJSA(jsa));
                TextView nomPays = findViewById(R.id.nomPays);
                TextView areaPays = findViewById(R.id.areaPays);
                TextView populationPays = findViewById(R.id.populationPays);
                TextView capitalPays = findViewById(R.id.capitalPays);
                nomPays.setText(name);
                areaPays.setText(area + " km²");
                populationPays.setText(population);
                capitalPays.setText(capital);
            } catch (Exception e) {

            }
        }
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

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

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);

        }
    }

}
