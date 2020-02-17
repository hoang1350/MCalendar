package com.luvina.democalendar.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.luvina.democalendar.R;
import com.luvina.democalendar.fragment.AboutFragment;
import com.luvina.democalendar.fragment.HomeFragment;

/**
 * Class handling MainActivity
 */
public class MainActivity extends AppCompatActivity {
    // Bottom navigation of the activity
    private BottomNavigationView bottomNavigationView;

    /**
     * Create MainActivity graphics
     *
     * @param savedInstanceState: Bundle contains the most recent data
     * @author HoangNN
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Default: display Home screen
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.getMenu().getItem(0).setEnabled(false);
        // Set ItemSelectedListener for bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = null;
                switch (item.getItemId()) {
                    // When the user click tab Home
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        bottomNavigationView.getMenu().getItem(0).setEnabled(false);
                        bottomNavigationView.getMenu().getItem(1).setEnabled(true);
                        break;
                    // When the user click tab About
                    case R.id.nav_about:
                        fragment = new AboutFragment();
                        bottomNavigationView.getMenu().getItem(0).setEnabled(true);
                        bottomNavigationView.getMenu().getItem(1).setEnabled(false);
                        break;
                    default:
                        //DO NOTHING
                        break;
                }
                fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
                return true;
            }
        });
    }
}
