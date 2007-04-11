package com.icesoft.tutorial.timezone;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

public class TimeZoneBean {
	private DateFormat serverFormat;
	private TimeZone serverTimeZone;
	private DateFormat selectedFormat;
	private TimeZone selectedTimeZone;
	private List timeZoneList;

	public TimeZoneBean() {
		initialize();
	}

	public String getServerTime() {
		return formatCurrentTime(serverFormat);
	}

	public String getServerTimeZoneName() {
		return formatTimeZone(serverTimeZone);
	}

	public String getSelectedTime() {
		return formatCurrentTime(selectedFormat);
	}

	public String getSelectedTimeZoneName() {
		return formatTimeZone(selectedTimeZone);
	}

	public void listen(ActionEvent event) {
		TimeZoneWrapper _timeZoneWrapper = getTimeZoneWrapper(event.getComponent().getClientId(FacesContext.getCurrentInstance()));
		if (_timeZoneWrapper != null) {
			selectedTimeZone = TimeZone.getTimeZone(_timeZoneWrapper.getId());
			selectedFormat = getFormat(selectedTimeZone);
		}
	}

	private static String formatCurrentTime(DateFormat dateFormat) {
		Calendar _calendar = dateFormat.getCalendar();
		_calendar.setTimeInMillis(System.currentTimeMillis());
		return dateFormat.format(_calendar.getTime());
	}

	private static String formatTimeZone(TimeZone timeZone) {
		String _displayName = timeZone.getDisplayName();
		if (_displayName == null) {
			return "";
		} else {
			int _index = _displayName.indexOf(' ');
			if (_index != -1) {
				return _displayName.substring(0, _index);
			} else {
				return _displayName;
			}
		}
	}

	private static DateFormat getFormat(TimeZone timeZone) {
		DateFormat _dateFormat = new SimpleDateFormat("EEE, HH:mm:ss");
		_dateFormat.setCalendar(Calendar.getInstance(timeZone));
		_dateFormat.setTimeZone(timeZone);
		return _dateFormat;
	}

	private TimeZoneWrapper getTimeZoneWrapper(String componentId) {
		int _size = timeZoneList.size();
		for (int i = 0; i < _size; i++) {
			TimeZoneWrapper _timeZoneWrapper = (TimeZoneWrapper)timeZoneList.get(i);
			if (_timeZoneWrapper.isRelevant(componentId)) {
				return _timeZoneWrapper;
			}
		}
		return null;
	}

	private void initialize() {
		serverTimeZone = TimeZone.getDefault();
		serverFormat = getFormat(serverTimeZone);
		selectedTimeZone = TimeZone.getTimeZone("Etc/GMT+0");
		selectedFormat = getFormat(selectedTimeZone);
		timeZoneList = new ArrayList(6);
		timeZoneList.add(new TimeZoneWrapper("Pacific/Honolulu", "GMTminus10"));
		timeZoneList.add(new TimeZoneWrapper("America/Anchorage", "GMTminus9"));
		timeZoneList.add(new TimeZoneWrapper("America/Los_Angeles", "GMTminus8"));
		timeZoneList.add(new TimeZoneWrapper("America/Phoenix", "GMTminus7"));
		timeZoneList.add(new TimeZoneWrapper("America/Chicago", "GMTminus6"));
		timeZoneList.add(new TimeZoneWrapper("America/New_York", "GMTminus5"));
	}
}
