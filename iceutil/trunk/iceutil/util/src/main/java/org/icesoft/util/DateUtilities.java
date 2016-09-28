package org.icesoft.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateUtilities {
    private static final Logger LOGGER = Logger.getLogger(DateUtilities.class.getName());

    private static final DateFormat ISO_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    static {
        ISO_DATE_FORMATTER.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
    }

    public static Calendar getCurrentDay() {
        Calendar _calendar = Calendar.getInstance();
        _calendar.set(Calendar.HOUR, 0);
        _calendar.set(Calendar.MINUTE, 0);
        _calendar.set(Calendar.SECOND, 0);
        _calendar.set(Calendar.MILLISECOND, 0);
        return _calendar;
    }

    public static Calendar getCurrentHour() {
        Calendar _calendar = Calendar.getInstance();
        _calendar.set(Calendar.MINUTE, 0);
        _calendar.set(Calendar.SECOND, 0);
        _calendar.set(Calendar.MILLISECOND, 0);
        return _calendar;
    }

    public static Calendar getNextDay() {
        return getNextDay(getCurrentDay());
    }

    public static Calendar getNextDay(final Calendar calendar) {
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar;
    }

    public static Calendar getNextHour() {
        return getNextHour(getCurrentHour());
    }

    public static Calendar getNextHour(final Calendar calendar) {
        calendar.add(Calendar.HOUR, 1);
        return calendar;
    }

    public static Calendar getPreviousDay() {
        return getPreviousDay(getCurrentDay());
    }

    public static Calendar getPreviousDay(final Calendar calendar) {
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar;
    }

    public static Calendar getPreviousHour() {
        return getPreviousHour(getCurrentHour());
    }

    public static Calendar getPreviousHour(final Calendar calendar) {
        calendar.add(Calendar.HOUR, -1);
        return calendar;
    }

    public static String toISOString(final Date date) {
        return ISO_DATE_FORMATTER.format(date);
    }
}
