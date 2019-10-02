package br.edu.ceunsp.buscacep;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView cepText = (TextView) findViewById(R.id.cep);
        TextView logradouroText = (TextView) findViewById(R.id.logradouro);
        TextView bairroText = (TextView) findViewById(R.id.bairro);
        TextView localidadeText = (TextView) findViewById(R.id.localidade);
        TextView ufText = (TextView) findViewById(R.id.uf);

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String response = makeRequest("https://viacep.com.br/ws/13334360/json");
        try {
            JSONObject json = new JSONObject(response);
            String cep = json.getString("cep");
            String logradouro = json.getString("logradouro");
            String bairro = json.getString("bairro");
            String localidade = json.getString("localidade");
            String uf = json.getString("uf");
            cepText.setText(cep);
            logradouroText.setText(logradouro);
            bairroText.setText(bairro);
            localidadeText.setText(localidade);
            ufText.setText(uf);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String makeRequest(String urlAddress) {
        HttpURLConnection con = null;
        URL url = null;
        String response = null;
        try {
            url = new URL(urlAddress);
            con = (HttpURLConnection) url.openConnection();
            response = readStream(con.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
        }
        return response;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }

}
