package com.example.appdelclima;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CiudadesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CiudadesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public String key = "dec0ab6d6f7fed077cbcb4df58b2680f";
    protected static final String FILE_NAME = "ciudades.txt";
    ArrayList<Ciudad> listDatos = new ArrayList<>();
    AdapterCiudades adapter;
    RecyclerView recyclerView;
    FloatingActionButton agregar;

    public CiudadesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CiudadesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CiudadesFragment newInstance(String param1, String param2) {
        CiudadesFragment fragment = new CiudadesFragment();
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
        View vista = inflater.inflate(R.layout.fragment_ciudades, container, false);
        leerArchivo();
        recyclerView = vista.findViewById(R.id.recyclerViewCiudad);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        agregar = vista.findViewById(R.id.btnAgregar);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarCiudad();
            }
        });
        adapter = new AdapterCiudades(listDatos);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrincipalFragment principalFragment = new PrincipalFragment(listDatos.get(recyclerView.getChildAdapterPosition(view)).getNombre());
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layoutCiudades, principalFragment).addToBackStack(null).commit();
            }
        });
        return vista;
    }

    private void agregarCiudad() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_agregar, null);
        builder.setView(view);
        final AlertDialog dialogo = builder.create();
        dialogo.show();
        dialogo.setCanceledOnTouchOutside(false);
        EditText nombre = view.findViewById(R.id.editNombre);
        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });
        Button btnAceptar = view.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean resultado;
                String dato = nombre.getText().toString();
                try {
                    String[] partes = dato.split(",");
                    Integer.parseInt(partes[0]);
                    resultado = true;
                } catch (NumberFormatException excepcion) {
                    resultado = false;
                }
                if(resultado){
                    String tempUrl = "https://api.openweathermap.org/geo/1.0/zip?zip="+dato+"&appid="+key;
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String nombre = jsonObject.getString(ListadoJSON.NAME)+"\n";
                                guardarFichero(nombre);
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
                    String nom = nombre.getText().toString()+"\n";
                    guardarFichero(nom);
                }
                dialogo.dismiss();
            }
        });
    }

    public void leerArchivo() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = getActivity().openFileInput(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineaTexto;
            while ((lineaTexto = bufferedReader.readLine()) != null) {
                listDatos.add(new Ciudad(lineaTexto));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void guardarFichero(String nombre) {
        Log.d("Nombre", nombre);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = getActivity().openFileOutput(FILE_NAME, Context.MODE_APPEND);
            fileOutputStream.write(nombre.getBytes());
            CiudadesFragment ciudadesFragment = new CiudadesFragment();
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.layoutCiudades, ciudadesFragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}