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
    private Fragment fragment;

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
        fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        bottomNavigationView = findViewById(R.id.navigation);
        // Set ItemSelectedListener for bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    // When the user click tab Home
                    case R.id.nav_home:
                        if (fragment instanceof HomeFragment) {
                            break;
                        }
                        fragment = new HomeFragment();
                        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
                        break;
                    // When the user click tab About
                    case R.id.nav_about:
                        if (fragment instanceof AboutFragment) {
                            break;
                        }
                        fragment = new AboutFragment();
                        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
                        break;
                    default:
                        //DO NOTHING
                        break;
                }
                return true;
            }
        });
    }
}
