package com.example.appdelclima;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showSelectedFragment(new PrincipalFragment());
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.botonNavegacion);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()== R.id.principal){
                    showSelectedFragment(new PrincipalFragment());
                }
                if(item.getItemId()== R.id.ciudadesTop){
                    showSelectedFragment(new CiudadesFragment());
                }
                if(item.getItemId()== R.id.busqueda){
                    showSelectedFragment(new SearchFragment());
                }
                return true;
            }
        });
    }


    private void showSelectedFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }
}