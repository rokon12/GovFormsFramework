package bd.gov.forms.utils;

import java.sql.Timestamp;

import java.util.Date;

public class DateUtil {
	// private static final DateFormat sqlDateFormatter = new SimpleDateFormat(
	// "yyyy-MM-dd");

	public static Timestamp convertDateToTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}

	public static Date convertTimestampToDate(Timestamp timestamp) {
		return new Date(timestamp.getTime());
	}
}
