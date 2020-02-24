package com.luvina.democalendar.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luvina.democalendar.R;
import com.luvina.democalendar.activity.AddEventActivity;
import com.luvina.democalendar.activity.CurrentDateDecorator;
import com.luvina.democalendar.adapter.EventRecyclerAdapter;
import com.luvina.democalendar.dao.EventDao;
import com.luvina.democalendar.model.EventModel;
import com.luvina.democalendar.utils.Common;
import com.luvina.democalendar.utils.Constant;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Class handling Home screen
 */
public class HomeFragment extends Fragment implements EventRecyclerAdapter.OnItemClickedListener, View.OnClickListener {
    // Declare the object to access to DB
    private EventDao eventDao;
    // Declare view controls of the fragment
    private EventRecyclerAdapter eventRecyclerAdapter;
    private RecyclerView recyclerView;
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        addEvents();
        actionToday();
        return view;
    }

    @Override
    public void onItemClicked(int position) {
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
     * Initialize controls and objects
     *
     * @author HoangNN
     */
    private void init(View view) {
        btnToday = view.findViewById(R.id.btnToday);
        btnAdd = view.findViewById(R.id.btnAdd);
        calendarView = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.recyclerV);
        eventDao = EventDao.getInstance(getActivity());
        listEvent = new ArrayList<>();
        eventRecyclerAdapter = new EventRecyclerAdapter(this, listEvent);
        recyclerView.setAdapter(eventRecyclerAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * Load data for the selected date and display on list view
     *
     * @param date: the selected date
     * @author HoangNN
     */
    private void getListEvent(Date date) {
        // Convert date to String
        String dateStr = Common.convertDateToStr(date);
        // Get list event from Database
        listEvent = eventDao.getListEvent(dateStr);
        // Display on list view
        if (eventRecyclerAdapter == null) {
            eventRecyclerAdapter = new EventRecyclerAdapter(this, listEvent);
            recyclerView.setAdapter(eventRecyclerAdapter);
        } else {
            eventRecyclerAdapter.setListEvent(listEvent);
            eventRecyclerAdapter.notifyDataSetChanged();
        }
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
    }

    /**
     * setOnClickListener for buttons
     *
     * @author HoangNN
     */
    private void addButtonEvent() {
        // setOnClickListener for each button
        btnToday.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
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
        getListEvent(dateSelected.getDate());
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
            public void onMonthChanged(MaterialCalendarView widget, final CalendarDay date) {
                // If the month contains the date selected before
                if (date.getYear() == dateSelected.getYear() && date.getMonth() == dateSelected.getMonth()) {
                    // Display list event for date selected
                    getListEvent(dateSelected.getDate());
                    // If the month doesn't contain the date selected before
                } else {
                    // Clear list view
                    listEvent.clear();
                    eventRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
        // setOnDateChangedListener
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull final CalendarDay date, boolean selected) {
                dateSelected = date;
                getListEvent(date.getDate());
            }
        });
        calendarView.addDecorator(new CurrentDateDecorator(getActivity()));
    }
}
