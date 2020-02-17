package com.luvina.democalendar.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.luvina.democalendar.R;
import com.luvina.democalendar.utils.Common;
import com.luvina.democalendar.utils.Constant;

import java.util.Calendar;
import java.util.List;

/**
 * Class handling action pick date
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static final String TAG = "DatePickerFragment";
    // Intent to get data
    private static Intent intent;

    /**
     * Create a datePickerDialog when the user click the icon calendar
     *
     * @param savedInstanceState: Bundle contains the most recent data
     * @return: return a DatePickerDialog to display
     * @author HoangNN
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Get the activity contains this fragment
        Activity activity = getActivity();
        intent = activity.getIntent();
        List<Integer> listDate = null;
        // Check if the user pick start date or end date
        String action = intent.getStringExtra(Constant.ACTION_PICK_DATE);
        // If the user clicks the icon start date
        if (Constant.PICK_START.equals(action)) {
            // get the year, month, date from textField start date and save it to a listDate
            EditText editStartDate = activity.findViewById(R.id.startDate);
            listDate = Common.splitDate(editStartDate.getText().toString());
            // If the user clicks the icon end date
        } else if (Constant.PICK_END.equals(action)) {
            // get the year, month, date from textField end date and save it to a listDate
            EditText editEndDate = activity.findViewById(R.id.endDate);
            listDate = Common.splitDate(editEndDate.getText().toString());
        }
        int index = 0;
        // Get the year, month, day from the listDate
        int year = listDate.get(index++);
        int month = listDate.get(index++);
        int day = listDate.get(index++);
        // Initialize a DatePickerDialog with the default selectedDate that displays on text field
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, this, year, month - 1, day);
        // set min date: cannot choose the date < current date
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        return datePickerDialog;
    }

    /**
     * Handle when the user pick a particular date on calendar
     *
     * @param view:       DatePicker
     * @param year:       year selected
     * @param month:      month selected
     * @param dayOfMonth: day selected
     * @author HoangNN
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Get the activity contains this fragment
        Activity activity = getActivity();
        // Check if the user pick start date or end date
        String action = intent.getStringExtra(Constant.ACTION_PICK_DATE);
        // If the user clicks the icon start date
        if (Constant.PICK_START.equals(action)) {
            // set the date selected to text field startDate
            EditText editStartDate = activity.findViewById(R.id.startDate);
            editStartDate.setText(Common.convertToDate(year, month + 1, dayOfMonth));
            // If the user clicks the icon end date
        } else if (Constant.PICK_END.equals(action)) {
            // set the date selected to text field endDate
            EditText editEndDate = activity.findViewById(R.id.endDate);
            editEndDate.setText(Common.convertToDate(year, month + 1, dayOfMonth));
        }
    }
}
