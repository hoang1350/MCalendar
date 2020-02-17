package com.luvina.democalendar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.luvina.democalendar.R;

/**
 * Class handling AppDescriptionActivity
 */
public class AppDescriptionActivity extends AppCompatActivity {
    // Control of the activity
    private TextView textBack;

    /**
     * Create AppDescriptionActivity
     *
     * @param savedInstanceState: Bundle contains the most recent data
     * @author HoangNN
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_description);
        textBack = findViewById(R.id.back);
        textBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
