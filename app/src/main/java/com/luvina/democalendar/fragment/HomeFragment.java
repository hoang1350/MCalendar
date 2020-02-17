package com.luvina.democalendar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.luvina.democalendar.R;
import com.luvina.democalendar.activity.AddEventActivity;
import com.luvina.democalendar.adapter.CustomAdapter;
import com.luvina.democalendar.dao.EventDao;
import com.luvina.democalendar.model.EventModel;
import com.luvina.democalendar.utils.Common;
import com.luvina.democalendar.utils.Constant;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Class handling Home screen
 */
public class HomeFragment extends Fragment {
    // Declare the object to access to DB
    public static EventDao eventDao;
    private View view;
    // Declare view controls of the fragment
    private ListView listViewEvent;
    private CustomAdapter customAdapter;
    private MaterialCalendarView calendarView;
    private Button btnToday, btnAdd;
    // Declare list event
    private List<EventModel> listEvent;
    // Declare CalendarDay to save the dateSelected
    private CalendarDay dateSelected;

    /**
     * Create Home screen
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
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        addEvents();
        actionToday();
        return view;
    }

    /**
     * Initialize controls and objects
     *
     * @author HoangNN
     */
    private void initView() {
        btnToday = view.findViewById(R.id.btnToday);
        btnAdd = view.findViewById(R.id.btnAdd);
        calendarView = view.findViewById(R.id.calendarView);
        listViewEvent = view.findViewById(R.id.listEvent);
        eventDao = new EventDao(getActivity());
        listEvent = new ArrayList<>();
        customAdapter = new CustomAdapter(getActivity(), R.layout.listview_event, listEvent);
    }

    /**
     * Load data for the selected date and display on list view
     *
     * @param date: the selected date
     * @author HoangNN
     */
    private void loadData(Date date) {
        // Convert date to String
        String dateStr = Common.convertDateToStr(date);
        // Get list event from Database
        listEvent = eventDao.getListEvent(dateStr);
        // Display on list view
        customAdapter = new CustomAdapter(getActivity(), R.layout.listview_event, listEvent);
        listViewEvent.setAdapter(customAdapter);
    }

    /**
     * Add events for Calendar, button and list view
     *
     * @author HoangNN
     */
    private void addEvents() {
        // Add event for calendar
        addCalendarViewEvent();
        // Add event for buttons
        addButtonEvent();
        // Add events for list view
        addListViewEvent();
    }

    /**
     * Set setOnItemClickListener for list view
     *
     * @author HoangNN
     */
    private void addListViewEvent() {
        listViewEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the recent selected item
                EventModel event = listEvent.get(position);
                // Initialize an intent
                Intent intent = new Intent(getActivity(), AddEventActivity.class);
                // Put selected event on intent
                intent.putExtra(Constant.EVENT, event);
                // If the event already occurs
                if (!Common.compareToCurrentTime(event.getStartDate())) {
                    // Set action detail on intent
                    intent.putExtra(Constant.ACTION, Constant.DETAIL);
                    // If the event hasn't occurred
                } else {
                    // Set action edit on intent
                    intent.putExtra(Constant.ACTION, Constant.EDIT);
                }
                // Change the screen to AddEventActivity
                startActivity(intent);
            }
        });
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
        btnToday.setOnClickListener(onClickListener);
        btnAdd.setOnClickListener(onClickListener);
    }

    /**
     * Handle action click for each button
     *
     * @param v: object View
     * @author HoangNN
     */
    private void doButton(View v) {
        // Check which button the user clicks
        switch (v.getId()) {
            // If the user clicks button Today
            case R.id.btnToday:
                actionToday();
                break;
            // If the user clicks button Add event
            case R.id.btnAdd:
                actionAddEvent();
                break;
            default:
                //DO NOTHING
                break;
        }
    }

    /**
     * Handle action add event
     *
     * @author HoangNN
     */
    private void actionAddEvent() {
        // Initialize an intent
        Intent intent = new Intent(getActivity(), AddEventActivity.class);
        // Set action add on intent
        intent.putExtra(Constant.ACTION, Constant.ADD);
        // Change the screen to AddEventActivity
        startActivity(intent);
    }

    /**
     * Handle action today
     *
     * @author HoangNN
     */
    private void actionToday() {
        dateSelected = CalendarDay.from(new Date());
        // Get the current year, month, day
        int year = Common.getCurrentYear();
        int month = Common.getCurrentMonth();
        int day = Common.getPresentDay();
        // Change the date selected to currentDate
        calendarView.setCurrentDate(CalendarDay.from(year, month, day));
        calendarView.setSelectedDate(CalendarDay.from(year, month, day));
        // Display list event
        loadData(dateSelected.getDate());
    }

    /**
     * Handle month change and date change event for calendar
     *
     * @author HoangNN
     */
    private void addCalendarViewEvent() {
        // setOnMonthChangedListener
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                // If the month contains the date selected before
                if (date.getYear() == dateSelected.getYear() && date.getMonth() == dateSelected.getMonth()) {
                    // Display list event for date selected
                    loadData(dateSelected.getDate());
                    // If the month doesn't contain the date selected before
                } else {
                    // Clear list view
                    listEvent.clear();
                    customAdapter.notifyDataSetChanged();
                }
            }
        });
        // setOnDateChangedListener
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                dateSelected = date;
                loadData(date.getDate());
            }
        });
    }
}
