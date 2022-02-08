package com.example.appdelclima;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link searchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class searchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public String key = "dec0ab6d6f7fed077cbcb4df58b2680f", nombre;
    ArrayList<Clima> listDatos;
    AdapterBusqueda adapter;
    RecyclerView recyclerView;
    Button botonBuscar;
    EditText buscador;

    public searchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment searchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static searchFragment newInstance(String param1, String param2) {
        searchFragment fragment = new searchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = vista.findViewById(R.id.recyclerViewBusqueda);
        botonBuscar = vista.findViewById(R.id.botonBuscar);
        buscador = vista.findViewById(R.id.buscador);
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buscador.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"El buscador esta vacio", Toast.LENGTH_LONG).show();
                } else {
                    obtenerCoordenadas(buscador.getText().toString());
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return vista;
    }

    private void obtenerCoordenadas(String dato) {
        boolean resultado;
        try {
            String[] partes = dato.split(",");
            Integer.parseInt(partes[0]);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }
        // Obtener las Coordenadas mediante el nombre o zipcode de algun lugar
        if(!resultado){
            // Coordenadas por Nombre
            String tempUrl = "https://api.openweathermap.org/geo/1.0/direct?q="+dato+"&limit=1&appid="+key;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonResponse = new JSONArray(response);
                        JSONObject jsonObject = jsonResponse.getJSONObject(0);
                        nombre = jsonObject.getString(ListadoJSON.NAME);
                        Double lat =jsonObject.getDouble(ListadoJSON.LAT);
                        Double lon = jsonObject.getDouble(ListadoJSON.LON);
                        showDailyForecast(lat,lon);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(),error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        } else {
            // Coordenadas por ZIP CODE
            String tempUrl = "https://api.openweathermap.org/geo/1.0/zip?zip="+dato+"&appid="+key;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        nombre = jsonObject.getString(ListadoJSON.NAME);
                        Double lat =jsonObject.getDouble(ListadoJSON.LAT);
                        Double lon = jsonObject.getDouble(ListadoJSON.LON);
                        showDailyForecast(lat,lon);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(),error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }
    }

    private void showDailyForecast(Double lat, Double lon) {
        String tempUrl = "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&units=metric&appid="+key;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    listDatos = new ArrayList<>();
                    JSONObject jsonResponse = new JSONObject(response);
                    String timezone =jsonResponse.getString(ListadoJSON.TIMEZONE);
                    JSONArray daily = jsonResponse.getJSONArray(ListadoJSON.DAILY);

                    for (int i = 0; i < 6; i++) {
                        JSONObject jsonObject = daily.getJSONObject(i);
                        Long date = jsonObject.getLong(ListadoJSON.DT)*1000;
                        Locale spanish = new Locale("ES", "ES");
                        DateFormat dateFormat = new SimpleDateFormat("EEEE", spanish);
                        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
                        String temp = jsonObject.getJSONObject(ListadoJSON.TEMP).getInt(ListadoJSON.DAY)+"°C";
                        listDatos.add(new Clima(nombre,dateFormat.format(date), temp));
                    }
                    adapter = new AdapterBusqueda(listDatos);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}