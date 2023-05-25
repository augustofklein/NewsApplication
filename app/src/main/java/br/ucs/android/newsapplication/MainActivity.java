package br.ucs.android.newsapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public BottomNavigationView bnvMenu;
    public FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnvMenu = (BottomNavigationView) findViewById(R.id.bnvMenu);
        fragmentManager = getSupportFragmentManager();

        bnvMenu.setOnItemSelectedListener(item -> {

            switch(item.getItemId()) {

                case R.id.nav_historico -> {

                }
                case R.id.nav_favoritos -> {

                    fragmentManager.beginTransaction()
                            .replace(R.id.fcvMain, FavoritosFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("favoritos") // name can be null
                            .commit();
                }
                case R.id.nav_principal -> {

                }
                case R.id.nav_buscar -> {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fcvMain, BuscarFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("buscar") // name can be null
                            .commit();
                }
            }
            return true;
        });

    }

}