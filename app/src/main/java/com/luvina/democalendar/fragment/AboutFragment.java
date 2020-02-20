package com.luvina.democalendar.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.luvina.democalendar.R;
import com.luvina.democalendar.activity.AppDescriptionActivity;

/**
 * Class handling About screen
 */
public class AboutFragment extends Fragment implements View.OnClickListener {
    // Declare view controls of the fragment
    private Button btnExport, btnAbout;

    /**
     * Create About screen
     *
     * @param inflater:           LayoutInflater
     * @param container:          ViewGroup
     * @param savedInstanceState: Bundle contains the most recent data
     * @return: a view to display
     * @author HoangNN
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        initView(view);
        addButtonEvent();
        return view;
    }

    /**
     * Handle action click for each button
     *
     * @param v: object View
     * @author HoangNN
     */
    @Override
    public void onClick(View v) {
        // Check which button the user clicks
        switch (v.getId()) {
            // If the user clicks button Export
            case R.id.btnExport:
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.sendEmail, new SendEmailFragment());
                fragmentTransaction.commit();
                break;
            // If the user clicks button About
            case R.id.btnDescription:
                startActivity(new Intent(getActivity(), AppDescriptionActivity.class));
                break;
            default:
                //DO NOTHING
                break;
        }
    }

    /**
     * Initialize controls and objects
     *
     * @author HoangNN
     */
    private void initView(View view) {
        btnExport = view.findViewById(R.id.btnExport);
        btnAbout = view.findViewById(R.id.btnDescription);
    }

    /**
     * setOnClickListener for buttons
     *
     * @author HoangNN
     */
    private void addButtonEvent() {
        // setOnClickListener for each button
        btnExport.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
    }
}
