package com.luvina.democalendar.logic;

import com.luvina.democalendar.fragment.HomeFragment;
import com.luvina.democalendar.model.EventModel;
import com.luvina.democalendar.utils.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Class handling logic Validate
 */
public class EventLogic {

    /**
     * Validate input data from user
     *
     * @param event: event that have to be validated
     * @return: return a list error
     * @author HoangNN
     */
    public List<String> validate(EventModel event) {
        List<String> listError = new ArrayList<>();
        // Get all the data that have to be validated
        String name = event.getName();
        String startDate = event.getStartDate();
        String endDate = event.getEndDate();
        // If the name is empty
        if (name.isEmpty()) {
            listError.add("Please input event name.");
        }
        // If start time <= current time
        if (!Common.compareToCurrentTime(startDate)) {
            listError.add("Start time must be greater than current time.");
        }
        // If start time >= end time
        if (!Common.compareStartEnd(startDate, endDate)) {
            listError.add("End time must be greater than start time.");
        }
        return listError;
    }

    /**
     * Check if a given event is conflict with other events
     *
     * @param event: object event
     * @return: return true if conflict, false if not
     * @author HoangNN
     */
    public boolean checkTimeEvent(EventModel event) {
        return HomeFragment.eventDao.checkTimeEvent(event);
    }
}
