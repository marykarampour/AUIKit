package com.prometheussoftware.auikit.utility;

import com.prometheussoftware.auikit.common.App;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtility {

    public static final String DateFormatServerStyle             = "YYYY-MM-dd HH:mm:ss";
    public static final String DateFormatShortStyle              = "yyyy-MM-dd";
    public static final String DateFormatWeekdayShortStyle       = "EEEE MMM dd";
    public static final String DateFormatFullStyle               = "EEEE, MMMM dd, yyyy";
    public static final String DateFormatMonthDayYearStyle       = "MMMM dd, yyyy";
    public static final String DateFormatMonthYearStyle          = "MMMM yyyy";
    public static final String DateFormatDayMonthYearStyle       = "dd MMMM yyyy";
    public static final String DateFormatDayMonthYearNumericStyle= "dd MM yyyy";
    public static final String DateFormatWeekdayDayStyle         = "EEEE dd";
    public static final String DateFormatFullTimeStyle           = "HH:mm:ss EEEE, MMMM dd, yyyy";
    public static final String DateFormatTimeStyle               = "HH:mm:ss a";
    public static final String DateFormatShortAPMStyle           = "yyyy/MM/dd hh:mm a";
    public static final String DateFormatFullAPMStyle            =  "hh:mm:ss aa EEEE, MMMM dd, yyyy";

    public static class Components {
        public long second;
        public long minute;
        public long hour;
        public long day;
    }

    public static Date midnightLocal() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    public static Date midnightGMT() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    public static int Month(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.get(Calendar.MONTH);
    }

    public static int Year(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.get(Calendar.YEAR);
    }

    public static Date endOfDay(Date day) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.HOUR_OF_DAY, 23);

        return cal.getTime();
    }

    public static Date weekStartDate(Date day) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        return calendar.getTime();
    }

    public static Date weekEndDate(Date day) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.add(Calendar.DAY_OF_YEAR, 6);

        return calendar.getTime();
    }

    public static Date weekBeforeLocal(Date day) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.add(Calendar.DAY_OF_YEAR, -7);

        return calendar.getTime();
    }

    public static Date weekBeforeGMT(Date day) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.setTime(day);
        calendar.add(Calendar.DAY_OF_YEAR, -7);

        return calendar.getTime();
    }

    public static Date weekAfterLocal(Date day) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.add(Calendar.DAY_OF_YEAR, 7);

        return calendar.getTime();
    }

    public static Date weekAfterGMT(Date day) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.setTime(day);
        calendar.add(Calendar.DAY_OF_YEAR, 7);

        return calendar.getTime();
    }

    public static int dayDifferenceWithWeekStartDate(Date day) {

        long startDate = weekStartDate(day).getTime();
        long endDate = day.getTime();

        return (int) ((startDate - endDate) / (1000 * 60 * 60 * 24));
    }

    public static Date updateDayWithValue(Date date, int days) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }

    public static Date updateMonthWithValue(Date date, int i) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, i);

        return cal.getTime();
    }

    public static Date updateYearWithValue(Date date, int i) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, i);

        return cal.getTime();
    }

    public static Date updateCalendarUnit(Date date, int CalendarUnit, int i) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(CalendarUnit, i);

        return cal.getTime();
    }

    public static int valueForCalendarUnit(int CalendarUnit, Date day) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        int calendarUnit = cal.get(CalendarUnit);

        return calendarUnit;
    }

    public static String[] daysOfWeek() {

        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] shortWeekdays = dfs.getShortWeekdays();

        return shortWeekdays;
    }

    public static String[] monthsOfYear() {

        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] shortMonths = dfs.getShortMonths();

        return shortMonths;

    }

    public static String dateStringWithFormat(Date currentDate, String dateFormat) {

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String formattedDate = formatter.format(currentDate);

        return formattedDate;
    }


    public static int daysBetweenFromDate(Date start, Date end) {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(start);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(end);
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);

        return (int) ((calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / (24 * 60 * 60 * 1000));

    }

    public static Date expiryWithLength(int numberOfSeconds, Date date) {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.add(Calendar.SECOND, numberOfSeconds);
        Date valid_until = calendar1.getTime();

        return !(valid_until.equals(date)) ? null : date;
    }

    public static Date expiryWithlength(int seconds) {

        Calendar cal = Calendar.getInstance();
        long currentDate = cal.getTimeInMillis();
        cal.add(Calendar.SECOND, seconds);

        long valid_until = cal.getTimeInMillis();
        Date valid_Date = cal.getTime();

        return (valid_until <= currentDate) ? null : valid_Date;
    }

    public static Boolean isExpiredWithLength(int numberOfSeconds, Date date) {
        return ((expiryWithLength(numberOfSeconds, date) != null));
    }

    public static String formattedTimeRemaining(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd:HH:mm:ss");
        return formatter.format(date);
    }

    public static String timeLeftstring(Date date) {

        Components components = componentsFromDateToDate(Calendar.getInstance().getTime(), date);
        String timeLeft = components.day + "d" + ":" + components.hour + "h" + ":" + components.minute + "m" + ":" + components.second + "s";

        return timeLeft;
    }

    public static String formattedTimeRemainingExpiringIn (int seconds) {
        Date date = expiryWithlength(seconds);
        return date != null ? timeLeftstring(date) : "";
    }

    public static String formattedLargestComponent (int seconds) {

        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.SECOND, seconds);
        Date next = cal.getTime();

        Components components = componentsFromDateToDate(now, next);

        if (components.day > 0) {
            return components.day + " " + App.constants().Day_s_STR();
        }
        if (components.hour > 0) {
            return components.hour + " " + App.constants().Hour_s_STR();
        }
        if (components.minute > 0) {
            return components.minute + " " + App.constants().Minute_s_STR();
        }
        return "";
    }

    public static Components componentsFromDateToDate (Date fromDate, Date toDate) {

        Components components = new Components();
        long diff = (toDate.getTime() - fromDate.getTime()) / 1000;

        components.second = diff;
        components.minute = components.second / 60;
        components.hour = components.minute / 60;
        components.day = components.hour / 24;

        components.second = components.second % 60;
        components.minute = components.minute % 60;
        components.hour = components.hour % 24;

        return components;
    }

    public static int timestamp() {
        return Math.toIntExact(System.currentTimeMillis() / 1000);
    }
}
