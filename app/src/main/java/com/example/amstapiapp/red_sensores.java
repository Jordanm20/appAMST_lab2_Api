package com.example.amstapiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class red_sensores extends AppCompatActivity {

    private RequestQueue mQueue;
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_sensores);
        mQueue = Volley.newRequestQueue(this);

        Intent login = getIntent();
        this.token = (String) login.getExtras().get("token");
        revisarSensores();

        Button enviarBtn = findViewById(R.id.enviarBtn);
        enviarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarNuevosValores();
            }
        });
    }

    private void revisarSensores() {
        final TextView tempValue = findViewById(R.id.tempVal);
        final TextView pesoValue = findViewById(R.id.pesoVal);
        final TextView humedadValue = findViewById(R.id.humedadVal);

        String url_temp = "https://amst-lab-api.herokuapp.com/api/sensores/1";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url_temp, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tempValue.setText(response.getString("temperatura") + " °C");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(red_sensores.this, "Error al obtener el valor de temperatura", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "JWT " + token);
                return params;
            }
        };
        mQueue.add(request);

        String url_humedad = "https://amst-lab-api.herokuapp.com/api/sensores/2";
        JsonObjectRequest request_humedad = new JsonObjectRequest(
                Request.Method.GET, url_humedad, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            humedadValue.setText(response.getString("humedad") + " %");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(red_sensores.this, "Error al obtener el valor de humedad", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "JWT " + token);
                return params;
            }
        };
        mQueue.add(request_humedad);

        String url_peso = "https://amst-lab-api.herokuapp.com/api/sensores/3";
        JsonObjectRequest request_peso = new JsonObjectRequest(
                Request.Method.GET, url_peso, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pesoValue.setText(response.getString("peso") + " g");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(red_sensores.this, "Error al obtener el valor de peso", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "JWT " + token);
                return params;
            }
        };
        mQueue.add(request_peso);
    }

    private void enviarNuevosValores() {
        EditText humedadInput = findViewById(R.id.humedadInput);
        EditText pesoInput = findViewById(R.id.pesoInput);
        EditText temperaturaInput = findViewById(R.id.temperaturaInput);

        String humedadStr = humedadInput.getText().toString();
        String pesoStr = pesoInput.getText().toString();
        String temperaturaStr = temperaturaInput.getText().toString();

        if (humedadStr.isEmpty() || pesoStr.isEmpty() || temperaturaStr.isEmpty()) {
            Toast.makeText(this, "Debe ingresar todos los valores", Toast.LENGTH_SHORT).show();
            return;
        }

        double humedad = Double.parseDouble(humedadStr);
        double peso = Double.parseDouble(pesoStr);
        double temperatura = Double.parseDouble(temperaturaStr);

        String url = "https://amst-lab-api.herokuapp.com/api/sensores/";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("humedad", humedad);
            jsonObject.put("peso", peso);
            jsonObject.put("temperatura", temperatura);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(red_sensores.this, "Valores enviados correctamente", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(red_sensores.this, "Error al enviar los valores", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "JWT " + token);
                return params;
            }
        };

        mQueue.add(request);
    }
}
