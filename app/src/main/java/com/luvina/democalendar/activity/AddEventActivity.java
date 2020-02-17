package com.luvina.democalendar.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.luvina.democalendar.fragment.DatePickerFragment;
import com.luvina.democalendar.R;
import com.luvina.democalendar.fragment.HomeFragment;
import com.luvina.democalendar.logic.EventLogic;
import com.luvina.democalendar.model.EventModel;
import com.luvina.democalendar.receiver.NotificationReceiver;
import com.luvina.democalendar.utils.Common;
import com.luvina.democalendar.utils.Constant;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Class handling Add event screen
 */
public class AddEventActivity extends AppCompatActivity {
    // Request code for camera
    private static final int REQUEST_CODE_CAPTURE = 0;
    // Request code for gallery
    private static final int REQUEST_CODE_PICK = 1;
    private static final String TAG = "AddEventActivity";
    // Action: add/edit or detail
    private static String action = "";
    // Declare view controls of the activity
    private TextView title;
    private EditText editTitle, editStartDate, editEndDate, editNote;
    private Button btnCancel, btnAddEdit;
    private Spinner spinnerStartHour, spinnerStartMinute, spinnerEndHour, spinnerEndMinute;
    private ImageView calendarStart, calendarEnd, imageChoose;
    private Switch switchNotify;
    // Object EventLogic
    private EventLogic eventLogic;
    // Intent to get data
    private Intent intent;
    // Event's id
    private int eventId;
    // Event's notify
    private boolean eventNotify;

    /**
     * Create AddEventActivity graphics and events
     *
     * @param savedInstanceState: Bundle contains the most recent data
     * @author HoangNN
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        initView();
        getValueSpinner();
        addEvents();
        getDefaultValue();
    }

    /**
     * Handle action pick image from camera or gallery
     *
     * @param requestCode: request code from user (=0 if user picks image from camera, =1 if user picks image from gallery)
     * @param resultCode:  result code
     * @param data:        Intent
     * @throws FileNotFoundException: File Not Found
     * @author HoangNN
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                // If the request code = 0
                if (requestCode == REQUEST_CODE_CAPTURE) {
                    // Get the image from intent
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    // Set the image on image view
                    imageChoose.setImageBitmap(bitmap);
                    // If the request code = 1
                } else if (requestCode == REQUEST_CODE_PICK) {
                    // Get the image uri
                    Uri uri = data.getData();
                    // Open input stream with the uri
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    // Decode the input stream
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    // Set the image on image view
                    imageChoose.setImageBitmap(bitmap);
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        } catch (FileNotFoundException e) {
            Log.d(TAG, getClass().getName() + " onActivityResult" + e.getMessage());
        }
    }

    /**
     * Get data for spinners
     *
     * @author HoangNN
     */
    private void getValueSpinner() {
        List<String> listHour = Common.getListHour();
        List<String> listMinute = Common.getListMinute();
        ArrayAdapter<String> adapterHour = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listHour);
        ArrayAdapter<String> adapterMinute = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listMinute);
        spinnerStartHour.setAdapter(adapterHour);
        spinnerStartMinute.setAdapter(adapterMinute);
        spinnerEndHour.setAdapter(adapterHour);
        spinnerEndMinute.setAdapter(adapterMinute);
    }

    /**
     * Get default value for the screen
     *
     * @author HoangNN
     */
    private void getDefaultValue() {
        // Get action from intent
        intent = getIntent();
        action = intent.getStringExtra(Constant.ACTION);
        // Check which the action is
        switch (action) {
            // Case Add: click button Add event from Home screen
            case Constant.ADD:
                // Display default value for spinners
                int currentHour = Common.getCurrentHour();
                int currentMinute = Common.getCurrentMinute();
                spinnerStartHour.setSelection(currentHour > 22 ? (currentHour + 1 - 24) : (currentHour + 1));
                spinnerStartMinute.setSelection(currentMinute);
                spinnerEndHour.setSelection(currentHour > 20 ? (currentHour + 3 - 24) : (currentHour + 3));
                spinnerEndMinute.setSelection(currentMinute);
                // Display default value for text field startDate and endDate
                int year = Common.getCurrentYear();
                int month = Common.getCurrentMonth();
                int day = Common.getPresentDay();
                editStartDate.setText(Common.convertToDate(year, month + 1, day));
                editEndDate.setText(Common.convertToDate(year, month + 1, day));
                break;
            // Case Edit, Detail: click a particular item on list view from Home screen
            case Constant.EDIT:
            case Constant.DETAIL:
                // Call fillData()
                fillData();
                // Case edit
                if (Constant.EDIT.equals(action)) {
                    btnAddEdit.setText("Update Event");
                    title.setText("Edit Event");
                    // Case detail
                } else if (Constant.DETAIL.equals(action)) {
                    btnAddEdit.setVisibility(View.INVISIBLE);
                    title.setText("Detail");
                }
                break;
            default:
                //DO NOTHING
                break;
        }
    }

    /**
     * Fill data case Add and Edit
     *
     * @author HoangNN
     */
    private void fillData() {
        // Get event from intent
        EventModel event = (EventModel) intent.getParcelableExtra(Constant.EVENT);
        // Get eventId and eventNotify and set to variables
        eventId = event.getId();
        eventNotify = Common.getNotifyBoolean(event.getNotify());
        // Set data for each view
        editTitle.setText(event.getName());
        String startDate = event.getStartDate();
        String endDate = event.getEndDate();
        spinnerStartHour.setSelection(Common.convertToHour(startDate));
        spinnerStartMinute.setSelection(Common.convertToMinute(startDate));
        editStartDate.setText(Common.convertToDate(startDate));
        spinnerEndHour.setSelection(Common.convertToHour(endDate));
        spinnerEndMinute.setSelection(Common.convertToMinute(endDate));
        editEndDate.setText(Common.convertToDate(endDate));
        switchNotify.setChecked(Common.getNotifyBoolean(event.getNotify()));
        editNote.setText(event.getNote());
        // Decode byte[] -> bitmap
        byte[] image = event.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        imageChoose.setImageBitmap(bitmap);
    }

    /**
     * Add events for buttons, date picker and image
     *
     * @author HoangNN
     */
    private void addEvents() {
        // Add event for buttons
        addButtonEvent();
        // Add event date picker
        addDatePickEvent();
        // Add event for image
        addImagePickEvent();
    }

    /**
     * setOnClickListener for image pick
     *
     * @author HoangNN
     */
    private void addImagePickEvent() {
        // setOnClickListener for image
        imageChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize an alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
                // Set title, items and listener for dialog
                builder.setTitle(R.string.pick_image)
                        .setItems(R.array.array_image, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // if user picks image from camera
                                if (which == REQUEST_CODE_CAPTURE) {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, REQUEST_CODE_CAPTURE);
                                    // if user picks image from gallery
                                } else if (which == REQUEST_CODE_PICK) {
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityForResult(intent, REQUEST_CODE_PICK);
                                }
                            }
                        }).show();
            }
        });
    }

    /**
     * set onClickListener for image calendar
     *
     * @author HoangNN
     */
    private void addDatePickEvent() {
        // Initialize object OnClickListener
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDatePicker(v);
            }
        };
        calendarStart.setOnClickListener(onClickListener);
        calendarEnd.setOnClickListener(onClickListener);
    }

    /**
     * Handle action pick startDate or andDate
     *
     * @param v: View
     * @author HoangNN
     */
    private void doDatePicker(View v) {
        int action = v.getId();
        // If user picks startDate
        if (action == R.id.calendarStart) {
            getIntent().putExtra(Constant.ACTION_PICK_DATE, Constant.PICK_START);
            // Nếu user picks endDate
        } else if (action == R.id.calendarEnd) {
            getIntent().putExtra(Constant.ACTION_PICK_DATE, Constant.PICK_END);
        }
        // Display a datePickerFragment to choose date from
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), DatePickerFragment.TAG);
    }

    /**
     * setOnClickListener for buttons
     *
     * @author HoangNN
     */
    private void addButtonEvent() {
        // Initialize object OnClickListener
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doButton(v);
            }
        };
        // setOnClickListener for each button
        btnCancel.setOnClickListener(onClickListener);
        btnAddEdit.setOnClickListener(onClickListener);
    }

    /**
     * Handle action click for each button
     *
     * @param v: View
     * @author HoangNN
     */
    private void doButton(View v) {
        switch (v.getId()) {
            // If the user clicks button cancel
            case R.id.btnCancel:
                onBackPressed();
                break;
            // If the user clicks button Add event
            case R.id.btnAddEdit:
                EventModel event = new EventModel();
                event.setName(editTitle.getText().toString().trim());
                event.setStartDate(Common.convertToDateTimeStr(editStartDate.getText().toString() + " " + spinnerStartHour.getSelectedItemPosition() + ":" + spinnerStartMinute.getSelectedItemPosition()));
                event.setEndDate(Common.convertToDateTimeStr(editEndDate.getText().toString() + " " + spinnerEndHour.getSelectedItemPosition() + ":" + spinnerEndMinute.getSelectedItemPosition()));
                // Validate the input data from user
                List<String> listError = eventLogic.validate(event);
                // If invalid
                if (!listError.isEmpty()) {
                    Toast.makeText(AddEventActivity.this, "Error: " + listError.get(0), Toast.LENGTH_SHORT).show();
                    // If valid
                } else {
                    // Case Add
                    if (Constant.ADD.equals(action)) {
                        // Call createEvent()
                        createEvent(event);
                        // Case Edit
                    } else if (Constant.EDIT.equals(action)) {
                        // Call updateEvent()
                        updateEvent(event);
                    }
                }
                break;
            default:
                //DO NOTHING
                break;
        }
    }

    /**
     * Handle action update event
     *
     * @param event: object EventModel
     * @author HoangNN
     */
    private void updateEvent(EventModel event) {
        // Set eventId
        event.setId(eventId);
        // Check if the time is conflict
        boolean isConflict = eventLogic.checkTimeEvent(event);
        // If not conflict
        if (!isConflict) {
            // Call updateEventToDatabase()
            updateEventToDatabase(event);
            // If conflict
        } else {
            // Initialize an alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
            builder.setTitle("You're about to update an event that conflicts to other events");
            builder.setMessage("Are you sure you want to update this event?");
            // setPositiveButton
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Initialize an event and set value for it
                    EventModel event = new EventModel();
                    event.setId(eventId);
                    event.setName(editTitle.getText().toString().trim());
                    event.setStartDate(Common.convertToDateTimeStr(editStartDate.getText().toString() + " " + spinnerStartHour.getSelectedItemPosition() + ":" + spinnerStartMinute.getSelectedItemPosition()));
                    event.setEndDate(Common.convertToDateTimeStr(editEndDate.getText().toString() + " " + spinnerEndHour.getSelectedItemPosition() + ":" + spinnerEndMinute.getSelectedItemPosition()));
                    event.setNote(editNote.getText().toString());
                    event.setNotify(Common.getNotifyInt(switchNotify.isChecked()));
                    // Convert imageview -> byte[]
                    byte[] image = Common.convertToImageByteArray((BitmapDrawable) imageChoose.getDrawable());
                    event.setImage(image);
                    // Update event to DB
                    boolean isSucceeded = HomeFragment.eventDao.updateEvent(event);
                    // If succeeded
                    if (isSucceeded) {
                        Toast.makeText(AddEventActivity.this, "Update successfully", Toast.LENGTH_LONG).show();
                        // set notification for event
                        setNotificationForUpdatedEvent(event);
                        // If not succeeded
                    } else {
                        Toast.makeText(AddEventActivity.this, "Update failed", Toast.LENGTH_LONG).show();
                    }
                    // Change the activity to Main activity
                    startActivity(new Intent(AddEventActivity.this, MainActivity.class));
                    finish();
                }
            });
            // setNegativeButton
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    /**
     * Update event to database
     *
     * @param event: object EventModel
     * @author HoangNN
     */
    private void updateEventToDatabase(EventModel event) {
        event.setNote(editNote.getText().toString());
        event.setNotify(Common.getNotifyInt(switchNotify.isChecked()));
        // Convert imageview -> byte[]
        byte[] image = Common.convertToImageByteArray((BitmapDrawable) imageChoose.getDrawable());
        event.setImage(image);
        // Update event to DB
        boolean isSucceeded = HomeFragment.eventDao.updateEvent(event);
        // If succeeded
        if (isSucceeded) {
            Toast.makeText(AddEventActivity.this, "Update successfully", Toast.LENGTH_LONG).show();
            // set notification for event
            setNotificationForUpdatedEvent(event);
            // If not succeeded
        } else {
            Toast.makeText(AddEventActivity.this, "Update failed", Toast.LENGTH_LONG).show();
        }
        // Change the activity to Main activity
        startActivity(new Intent(AddEventActivity.this, MainActivity.class));
        finish();
    }

    /**
     * Set notification for updated event
     *
     * @param event: object EventModel
     * @author HoangNN
     */
    private void setNotificationForUpdatedEvent(EventModel event) {
        // Check if the switch is checked
        boolean notifyCheck = switchNotify.isChecked();
        // If the event had notification and the switch is checked
        if (eventNotify && notifyCheck) {
            // Remove the old notification
            removeNotification(eventId);
            // Add a new notification for event
            addNotification(eventId, event.getStartDate(), event.getEndDate(), event.getName());
            // If the event had notification and the switch is unchecked
        } else if (eventNotify && !notifyCheck) {
            // Remove the old notification
            removeNotification(eventId);
            // If the event didn't have notification and the switch is checked
        } else if (!eventNotify && notifyCheck) {
            // Add a new notification for event
            addNotification(eventId, event.getStartDate(), event.getEndDate(), event.getName());
        }
    }

    /**
     * Handle action add event
     *
     * @param event: object EventModel
     * @author HoangNN
     */
    private void createEvent(EventModel event) {
        // Check if the time is conflict
        boolean isConflict = eventLogic.checkTimeEvent(event);
        // If not conflict
        if (!isConflict) {
            // Call addEventToDatabase()
            addEventToDatabase(event);
            // If conflict
        } else {
            // Initialize an alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
            builder.setTitle("You're about to create an event that conflicts to other events");
            builder.setMessage("Are you sure you want to create this event?");
            // setPositiveButton
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Initialize an event and set value for it
                    EventModel event = new EventModel();
                    event.setName(editTitle.getText().toString().trim());
                    event.setStartDate(Common.convertToDateTimeStr(editStartDate.getText().toString() + " " + spinnerStartHour.getSelectedItemPosition() + ":" + spinnerStartMinute.getSelectedItemPosition()));
                    event.setEndDate(Common.convertToDateTimeStr(editEndDate.getText().toString() + " " + spinnerEndHour.getSelectedItemPosition() + ":" + spinnerEndMinute.getSelectedItemPosition()));
                    event.setNote(editNote.getText().toString());
                    event.setNotify(Common.getNotifyInt(switchNotify.isChecked()));
                    // Convert imageview -> byte[]
                    byte[] image = Common.convertToImageByteArray((BitmapDrawable) imageChoose.getDrawable());
                    event.setImage(image);
                    // Add event to database
                    int id = HomeFragment.eventDao.addEvent(event);
                    // If id > 0
                    if (id > 0) {
                        Toast.makeText(AddEventActivity.this, "Create successfully", Toast.LENGTH_LONG).show();
                        // If switch is checked
                        if (switchNotify.isChecked()) {
                            // add notification for the event
                            addNotification(id, event.getStartDate(), event.getEndDate(), event.getName());
                        }
                        // If id < 0
                    } else {
                        Toast.makeText(AddEventActivity.this, "Create failed", Toast.LENGTH_LONG).show();
                    }
                    // Change the activity to Main activity
                    startActivity(new Intent(AddEventActivity.this, MainActivity.class));
                    finish();
                }
            });
            // setNegativeButton
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    /**
     * Add event to database
     *
     * @param event: object EventModel
     * @author HoangNN
     */
    private void addEventToDatabase(EventModel event) {
        event.setNote(editNote.getText().toString());
        event.setNotify(Common.getNotifyInt(switchNotify.isChecked()));
        // Convert imageview -> byte[]
        byte[] image = Common.convertToImageByteArray((BitmapDrawable) imageChoose.getDrawable());
        event.setImage(image);
        // Add event to database
        int id = HomeFragment.eventDao.addEvent(event);
        // If id > 0
        if (id > 0) {
            Toast.makeText(AddEventActivity.this, "Create successfully", Toast.LENGTH_LONG).show();
            // If switch is checked
            if (switchNotify.isChecked()) {
                // add notification for the event
                addNotification(id, event.getStartDate(), event.getEndDate(), event.getName());
            }
            // If id < 0
        } else {
            Toast.makeText(AddEventActivity.this, "Create failed", Toast.LENGTH_LONG).show();
        }
        // Change the activity to Main activity
        startActivity(new Intent(AddEventActivity.this, MainActivity.class));
        finish();
    }

    /**
     * Add notification for a particular event
     *
     * @param id:        event's id
     * @param startDate: event's start time
     * @param endDate:   event's end time
     * @param eventName: event's name
     * @author HoangNN
     */
    private void addNotification(int id, String startDate, String endDate, String eventName) {
        // get object AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // Initialize an intent and put data on it
        Intent intent = new Intent(AddEventActivity.this, NotificationReceiver.class);
        intent.putExtra(Constant.EVENT_NAME, eventName);
        intent.putExtra(Constant.START_TIME, startDate);
        intent.putExtra(Constant.END_TIME, endDate);
        // Initialize a pendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddEventActivity.this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Set time for alarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, Common.convertToMillis(startDate), pendingIntent);
    }

    /**
     * Remove notification of a particular event
     *
     * @param id: event's id
     * @author HoangNN
     */
    private void removeNotification(int id) {
        // get object AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // Initialize an intent
        Intent intent = new Intent(AddEventActivity.this, NotificationReceiver.class);
        // Initialize a pendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddEventActivity.this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Remove notification that set before
        alarmManager.cancel(pendingIntent);
    }

    /**
     * Initialize controls and objects
     *
     * @author HoangNN
     */
    private void initView() {
        title = findViewById(R.id.titleContent);
        editTitle = findViewById(R.id.editTitle);
        editStartDate = findViewById(R.id.startDate);
        editEndDate = findViewById(R.id.endDate);
        editNote = findViewById(R.id.note);
        btnCancel = findViewById(R.id.btnCancel);
        btnAddEdit = findViewById(R.id.btnAddEdit);
        spinnerStartHour = findViewById(R.id.startHour);
        spinnerStartMinute = findViewById(R.id.startMinute);
        spinnerEndHour = findViewById(R.id.endHour);
        spinnerEndMinute = findViewById(R.id.endMinute);
        calendarStart = findViewById(R.id.calendarStart);
        calendarEnd = findViewById(R.id.calendarEnd);
        imageChoose = findViewById(R.id.imageChoose);
        switchNotify = findViewById(R.id.notify);
        eventLogic = new EventLogic();
    }
}
