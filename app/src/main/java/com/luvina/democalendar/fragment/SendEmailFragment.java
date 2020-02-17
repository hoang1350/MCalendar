package com.luvina.democalendar.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.luvina.democalendar.R;
import com.luvina.democalendar.model.EventModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Class handling SendEmailFragment
 */
public class SendEmailFragment extends Fragment {
    // Request permission send email
    private static final int REQUEST_SEND_EMAIL = 101;
    // Declare view controls of the fragment
    private EditText editEmail;
    private Button btnOK;

    /**
     * Create fragment send email
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
        View view = inflater.inflate(R.layout.fragment_email, container, false);
        initView(view);
        addEvent();
        return view;
    }

    /**
     * Handle request from app
     *
     * @param requestCode:  request code from app
     * @param permissions:  array contains the permissions
     * @param grantResults: response from user
     * @author HoangNN
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_SEND_EMAIL) {
            // If the user accept the WRITE PERMISSION
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Call attachFileAndSend()
                attachFileAndSend(editEmail.getText().toString().trim());
            } else {
                Toast.makeText(getActivity(), "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * setOnClickListener for button OK
     *
     * @author HoangNN
     */
    private void addEvent() {
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSendEmail();
            }
        });
    }

    /**
     * Handle action click OK
     *
     * @author HoangNN
     */
    private void doSendEmail() {
        String email = editEmail.getText().toString().trim();
        // Check if the email is valid
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // If permission isn't granted yet, then request the WRITE PERMISSION
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_SEND_EMAIL);
                // if permission is granted, call attachFileAndSend()
            } else {
                attachFileAndSend(email);
            }
            // if the email is invalid
        } else {
            Toast.makeText(getActivity(), "Email is invalid!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Write data to file and attach file to email
     *
     * @param email: email address
     * @throws IOException
     * @author HoangNN
     */
    private void attachFileAndSend(String email) {
        File file = null;
        try {
            File root = Environment.getExternalStorageDirectory();
            if (root.canWrite()) {
                // Create a directory "Event"
                File dir = new File(root.getAbsolutePath() + "/Event");
                dir.mkdirs();
                // Create a file in directory "Event"
                file = new File(dir, "Event.csv");
                // Initialize a FileOutputStream
                FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                // Get list event in database
                List<EventModel> listEvent = HomeFragment.eventDao.getListEvent();
                // Loop the list and write each event to file
                for (EventModel event : listEvent) {
                    String line = event.getId() + " / " + event.getName() + " / " + event.getStartDate() + " / " + event.getEndDate() +
                            " / " + event.getNote() + " / " + event.getNotify() + " / " + event.getImage() + "\n";
                    fileOutputStream.write(line.getBytes());
                }
                fileOutputStream.close();
                // Initialize an intent to send email with an attach file
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Events");
                Uri uri = null;
                // If SDK version >= 24
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", file);
                } else {
                    uri = Uri.fromFile(file);
                }
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setType("text/csv");
                startActivity(intent);
            }
        } catch (IOException e) {
            Toast.makeText(getActivity(), "Failed to send email!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Initialize controls and objects
     *
     * @param view: a view
     * @author HoangNN
     */
    private void initView(View view) {
        editEmail = view.findViewById(R.id.editEmail);
        btnOK = view.findViewById(R.id.btnOK);
    }
}
