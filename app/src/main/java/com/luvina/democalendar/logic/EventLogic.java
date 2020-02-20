package com.luvina.democalendar.logic;

import android.content.Context;

import com.luvina.democalendar.dao.EventDao;
import com.luvina.democalendar.model.EventModel;
import com.luvina.democalendar.utils.Common;

/**
 * Class handling logic Validate
 */
public class EventLogic {

    /**
     * Validate input data from user
     *
     * @param event: event that have to be validated
     * @return: return an error message
     * @author HoangNN
     */
    public String getErrorMessage(EventModel event) {
        String errorMessage = "";
        // Get all the data that have to be validated
        String name = event.getName();
        String startDate = event.getStartDate();
        String endDate = event.getEndDate();
        if (name.isEmpty()) {
            errorMessage = "Please input event name.";
        } else if (!Common.compareToCurrentTime(startDate)) {
            errorMessage = "Start time must be greater than current time.";
        } else if (!Common.compareStartEnd(startDate, endDate)) {
            errorMessage = "End time must be greater than start time.";
        }
        return errorMessage;
    }

    /**
     * Check if a given event is conflict with other events
     *
     * @param event: object event
     * @return: return true if conflict, false if not
     * @author HoangNN
     */
    public boolean checkTimeEvent(EventModel event, Context context) {
        return EventDao.getInstance(context).checkTimeEvent(event);
    }
}
