package com.luvina.democalendar.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.SpinnerAdapter;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Class contains utility method of the project
 */
public class Common {
    private static final String TAG = "Common";

    /**
     * Convert date to date String format: yyyy-MM-dd
     *
     * @param date: the date to be converted
     * @return: a date String format: yyyy-MM-dd
     * @author HoangNN
     */
    public static String convertDateToStr(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    /**
     * Convert date String format: yyyy-MM-dd HH:mm to a String format: HH:mm
     *
     * @param dateTimeStr: a String to be converted
     * @return: a String format: HH:mm
     * @author HoangNN
     */
    public static String getTimeFromDate(String dateTimeStr) {
        String[] arrDateTime = dateTimeStr.split(" ");
        return arrDateTime[1];
    }

    /**
     * Convert a String format: yyyy-MM-dd HH:mm to a String format yyyy/MM/dd
     *
     * @param dateTimeStr: a String to be converted
     * @return: return a String format yyyy/MM/dd
     * @author HoangNN
     */
    public static String convertToDate(String dateTimeStr) {
        String[] arrDateTime = dateTimeStr.split(" ");
        return arrDateTime[0].replace("-", "/");
    }

    /**
     * Get the current year
     *
     * @return: return the current year
     * @author HoangNN
     */
    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * Get the current month
     *
     * @return: return the current month
     * @author HoangNN
     */
    public static int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    /**
     * Get the current day
     *
     * @return: return the current day
     * @author HoangNN
     */
    public static int getPresentDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get list hour: 0-23h
     *
     * @return: list hour
     * @author HoangNN
     */
    public static List<String> getListHour() {
        List<String> listHour = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            listHour.add(i + "h");
        }
        return listHour;
    }

    /**
     * Get list minute 0-59m
     *
     * @return: list minute
     * @author HoangNN
     */
    public static List<String> getListMinute() {
        List<String> listMinute = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            listMinute.add(i + "m");
        }
        return listMinute;
    }

    /**
     * Get current hour
     *
     * @return: current hour
     * @author HoangNN
     */
    public static int getCurrentHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Get current minute
     *
     * @return: current minute
     * @author HoangNN
     */
    public static int getCurrentMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    /**
     * Split date String format: yyyy/MM/dd to a list contains [year, month, day]
     *
     * @param dateStr: date String to be split
     * @return: a list contains [year, month, day]
     * @author HoangNN
     */
    public static List<Integer> splitDate(String dateStr) {
        List<Integer> listDate = new ArrayList<>();
        String[] arrDateStr = dateStr.split("/");
        for (int i = 0; i < arrDateStr.length; i++) {
            listDate.add(Integer.parseInt(arrDateStr[i]));
        }
        return listDate;
    }

    /**
     * Convert given year, month, dayOfMonth to a date String format: yyyy/MM/dd
     *
     * @param year:       năm
     * @param month:      tháng
     * @param dayOfMonth: ngày
     * @throws ParseException
     * @return: trả về chuỗi String dạng yyyy/MM/dd
     * @author HoangNN
     */
    public static String convertToDate(int year, int month, int dayOfMonth) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateStr = "";
        try {
            Date date = simpleDateFormat.parse(year + "/" + month + "/" + dayOfMonth);
            dateStr = simpleDateFormat.format(date);
        } catch (ParseException e) {
            Log.d(TAG, "Common" + " convertToDate" + e.getMessage());
        }
        return dateStr;
    }

    /**
     * Convert a date String format: yyyy/MM/dd HH:mm to yyyy-MM-dd HH:mm
     *
     * @param timeToConvert: time to be converted
     * @throws ParseException
     * @return: string format: yyyy-MM-dd HH:mm
     * @author HoangNN
     */
    public static String convertToDateTimeStr(String timeToConvert) {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateTimeStr = "";
        try {
            Date date = simpleDateFormat1.parse(timeToConvert);
            dateTimeStr = simpleDateFormat2.format(date);
        } catch (ParseException e) {
            Log.d(TAG, "Common" + " convertToDateTimeStr" + e.getMessage());
        }
        return dateTimeStr;
    }

    /**
     * Compare a given time to current time
     *
     * @param startDate: time to compare
     * @throws ParseException
     * @return: true if given time > current time, false if not
     * @author HoangNN
     */
    public static boolean compareToCurrentTime(String startDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        boolean check = false;
        try {
            Date date = simpleDateFormat.parse(startDate);
            if (date.after(Calendar.getInstance().getTime())) {
                check = true;
            }
        } catch (ParseException e) {
            Log.d(TAG, "Common" + " compareToCurrentTime" + e.getMessage());
        }
        return check;
    }

    /**
     * Compare 2 given time format: yyyy-MM-dd HH:mm
     *
     * @param startDate: start time
     * @param endDate:   end time
     * @throws ParseException
     * @return: true if endDate > startDate, false if not
     * @author HoangNN
     */
    public static boolean compareStartEnd(String startDate, String endDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        boolean check = false;
        try {
            if (simpleDateFormat.parse(endDate).after(simpleDateFormat.parse(startDate))) {
                check = true;
            }
        } catch (ParseException e) {
            Log.d(TAG, "Common" + " compareStartEnd" + e.getMessage());
        }
        return check;
    }

    /**
     * Get notify int
     *
     * @param checked
     * @return: 1 if checked = true, 0 if checked = false
     * @author HoangNN
     */
    public static int getNotifyInt(boolean checked) {
        return checked ? 1 : 0;
    }

    /**
     * Convert date String format: yyyy-MM-dd HH:mm to hour
     *
     * @param dateTimeStr: a String to be converted
     * @return: hour
     * @author HoangNN
     */
    public static int getHourFromDate(String dateTimeStr) {
        String[] arrDateStr = dateTimeStr.split(" ");
        String[] arrHourMinute = arrDateStr[1].split(":");
        return Integer.parseInt(arrHourMinute[0]);
    }

    /**
     * Convert date String format: yyyy-MM-dd HH:mm to minute
     *
     * @param dateTimeStr: a String to be converted
     * @return: minute
     * @author HoangNN
     */
    public static int getMinuteFromDate(String dateTimeStr) {
        String[] arrDateStr = dateTimeStr.split(" ");
        String[] arrHourMinute = arrDateStr[1].split(":");
        return Integer.parseInt(arrHourMinute[1]);
    }

    /**
     * Get Notify boolean
     *
     * @param notify: 0 or 1
     * @return: true if notify = 1, false if notify = 0
     * @author HoangNN
     */
    public static boolean getNotifyBoolean(int notify) {
        return notify == 1 ? true : false;
    }

    /**
     * Convert date String format yyyy-MM-dd HH:mm to millisecond
     *
     * @param startDate: a String to be converted
     * @throws ParseException
     * @return: millisecond
     * @author HoangNN
     */
    public static long convertToMillis(String startDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long timeInMillis = 0;
        try {
            Date date = simpleDateFormat.parse(startDate);
            timeInMillis = date.getTime();
        } catch (ParseException e) {
            Log.d(TAG, "Common" + " getNotifyBoolean" + e.getMessage());
        }
        return timeInMillis;
    }

}
