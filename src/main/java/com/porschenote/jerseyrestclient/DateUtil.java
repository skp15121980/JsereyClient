package com.porschenote.jerseyrestclient;


import java.text.DateFormat;
import  java.text.ParsePosition ;
import java.text.SimpleDateFormat;
import  java.util.ArrayList ;
import  java.util.Calendar ;
import  java.util.Date ;
import  java.util.HashMap ;
import  java.util.List ;
import  java.util.Map ;
import java.util.TimeZone;

/**
 * 
 * Date tool
 * 
 * @author 段
 * 
 */
public class DateUtil {

	private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();

	private static final Object object = new Object();
	public static String getOracleFormattedTimeWithZone(Calendar timeWithZone) {

        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        DateFormat df = new SimpleDateFormat(dateFormat);

        // this is very important
        //TimeZone timeZone = timeWithZone.getTimeZone();
        TimeZone timeZone = timeWithZone.getTimeZone();

        if (timeZone != null) {
            df.setTimeZone(timeZone);
        }

        String dateTime = df.format(timeWithZone.getTime());
        String tzId = timeWithZone.getTimeZone().getID();
        dateTime += " " + tzId;

        return dateTime;

    }
	/**
	 * Get SimpleDateFormat
	 * 
	 * @param pattern
	 * Date format
	 * @return SimpleDateFormat对象
	 * @throws RuntimeException
	 * Exception: Illegal date format
	 */
	private static SimpleDateFormat getDateFormat(String pattern)
			throws RuntimeException {
		SimpleDateFormat dateFormat = threadLocal.get();
		if (dateFormat == null) {
			synchronized (object) {
				if (dateFormat == null) {
					dateFormat = new SimpleDateFormat(pattern);
					dateFormat.setLenient(false);
					threadLocal.set(dateFormat);
				}
			}
		}
		dateFormat.applyPattern(pattern);
		return dateFormat;
	}

	/**
	 * Get a value in the date. Such as getting the month
	 * 
	 * @param date
	 * Date
	 * @param dateType
	 * Date format
	 * @return values
	 */
	private static int getInteger(Date date, int dateType) {
		int num = 0;
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
			num = calendar.get(dateType);
		}
		return num;
	}

	/**
	 * Increase the value of a certain type in the date. Such as adding date
	 * 
	 * @param date
	 * Date string
	 * @param dateType
	 * Types of
	 * @param amount
	 * Value
	 * @return the calculated date string
	 */
	private static String addInteger(String date, int dateType, int amount) {
		String dateString = null;
		DateStyle dateStyle = getDateStyle(date);
		if (dateStyle != null) {
			Date myDate = StringToDate(date, dateStyle);
			myDate = addInteger(myDate, dateType, amount);
			dateString = DateToString(myDate, dateStyle);
		}
		return dateString;
	}

	/**
	 * Increase the value of a certain type in the date. Such as adding date
	 * 
	 * @param date
	 * Date
	 * @param dateType
	 * Types of
	 * @param amount
	 * Value
	 * @return the calculated date
	 */
	private  static  Date  addInteger ( Date  date , int  dateType , int  amount ) {
		Date myDate = null;
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(dateType, amount);
			myDate = calendar.getTime();
		}
		return myDate;
	}

	/**
	 * Get the exact date
	 * 
	 * @param timestamps
	 * Time long collection
	 * @return date
	 */
	private static Date getAccurateDate(List<Long> timestamps) {
		Date date = null;
		long timestamp = 0;
		Map<Long, long[]> map = new HashMap<Long, long[]>();
		List<Long> absoluteValues = new ArrayList<Long>();

		if (timestamps != null && timestamps.size() > 0) {
			if (timestamps.size() > 1) {
				for (int i = 0; i < timestamps.size(); i++) {
					for (int j = i + 1; j < timestamps.size(); j++) {
						long absoluteValue = Math.abs(timestamps.get(i)
								- timestamps.get(j));
						absoluteValues.add(absoluteValue);
						long[] timestampTmp = { timestamps.get(i),
								timestamps.get(j) };
						map.put(absoluteValue, timestampTmp);
					}
				}

				// There may be equal circumstances. Such as 2012-11 and 2012-11-01. The timestamp is equal. At this point minAbsoluteValue is 0
				// therefore can not take minAbsoluteValue by default
				long minAbsoluteValue = -1;
				if (!absoluteValues.isEmpty()) {
					minAbsoluteValue = absoluteValues.get(0);
					for (int i = 1; i < absoluteValues.size(); i++) {
						if (minAbsoluteValue > absoluteValues.get(i)) {
							minAbsoluteValue = absoluteValues.get(i);
						}
					}
				}

				if (minAbsoluteValue != -1) {
					long[] timestampsLastTmp = map.get(minAbsoluteValue);

					long dateOne = timestampsLastTmp[0];
					long dateTwo = timestampsLastTmp[1];
					if (absoluteValues.size() > 1) {
						timestamp = Math.abs(dateOne) > Math.abs(dateTwo) ? dateOne
								: dateTwo;
					}
				}
			} else {
				timestamp = timestamps.get(0);
			}
		}

		if (timestamp != 0) {
			date = new Date(timestamp);
		}
		return date;
	}

	/**
	 * Determines whether the string is a date string
	 * 
	 * @param date
	 * Date string
	 * @return true or false
	 */
	public static boolean isDate(String date) {
		boolean isDate = false;
		if (date != null) {
			if (getDateStyle(date) != null) {
				isDate = true;
			}
		}
		return isDate;
	}

	/**
	 * Get the date string for the date style. Failed to return null.
	 * 
	 * @param date
	 * Date string
	 * @return date style
	 */
	public static DateStyle getDateStyle(String date) {
		DateStyle dateStyle = null;
		Map<Long, DateStyle> map = new HashMap<Long, DateStyle>();
		List<Long> timestamps = new ArrayList<Long>();
		for (DateStyle style : DateStyle.values()) {
			if (style.isShowOnly()) {
				continue;
			}
			Date dateTmp = null;
			if (date != null) {
				try {
					ParsePosition pos =  new  ParsePosition ( 0 );
					dateTmp = getDateFormat(style.getValue()).parse(date, pos);
					if (pos.getIndex() != date.length()) {
						dateTmp = null;
					}
				} catch (Exception e) {
				}
			}
			if (dateTmp != null) {
				timestamps.add(dateTmp.getTime());
				map.put(dateTmp.getTime(), style);
			}
		}
		Date accurateDate = getAccurateDate(timestamps);
		if (accurateDate != null) {
			dateStyle = map.get(accurateDate.getTime());
		}
		return dateStyle;
	}

	/**
	 * Convert the date string to date. Failed to return null.
	 * 
	 * @param date
	 * Date string
	 * @return date
	 */
	public static Date StringToDate(String date) {
		DateStyle dateStyle = getDateStyle(date);
		return StringToDate(date, dateStyle);
	}

	/**
	 * Convert the date string to date. Failed to return null.
	 * 
	 * @param date
	 * Date string
	 * @param pattern
	 * Date format
	 * @return date
	 */
	public static Date StringToDate(String date, String pattern) {
		Date myDate = null;
		if (date != null) {
			try {
				myDate = getDateFormat(pattern).parse(date);
			} catch (Exception e) {
			}
		}
		return myDate;
	}

	/**
	 * Convert the date string to date. Failed to return null.
	 * 
	 * @param date
	 * Date string
	 * @param dateStyle
	 * Date style
	 * @return date
	 */
	public static Date StringToDate(String date, DateStyle dateStyle) {
		Date myDate = null;
		if (dateStyle != null) {
			myDate = StringToDate(date, dateStyle.getValue());
		}
		return myDate;
	}

	/**
	 * Convert the date to a date string. Failed to return null.
	 * 
	 * @param date
	 * Date
	 * @param pattern
	 * Date format
	 * @return date string
	 */
	public static String DateToString(Date date, String pattern) {
		String dateString = null;
		if (date != null) {
			try {
				dateString = getDateFormat(pattern).format(date);
			} catch (Exception e) {
			}
		}
		return dateString;
	}

	/**
	 * Convert the date to a date string. Failed to return null.
	 * 
	 * @param date
	 * Date
	 * @param dateStyle
	 * Date style
	 * @return date string
	 */
	public static String DateToString(Date date, DateStyle dateStyle) {
		String dateString = null;
		if (dateStyle != null) {
			dateString = DateToString(date, dateStyle.getValue());
		}
		return dateString;
	}

	/**
	 * Converts the date string to another date string. Failed to return null.
	 * 
	 * @param date
	 * Old date string
	 * @param newPattern
	 * New date format
	 * @return new date string
	 */
	public static String StringToString(String date, String newPattern) {
		DateStyle oldDateStyle = getDateStyle(date);
		return StringToString(date, oldDateStyle, newPattern);
	}

	/**
	 * Converts the date string to another date string. Failed to return null.
	 * 
	 * @param date
	 * Old date string
	 * @param newDateStyle
	 * New date style
	 * @return new date string
	 */
	public static String StringToString(String date, DateStyle newDateStyle) {
		DateStyle oldDateStyle = getDateStyle(date);
		return StringToString(date, oldDateStyle, newDateStyle);
	}

	/**
	 * Converts the date string to another date string. Failed to return null.
	 * 
	 * @param date
	 * Old date string
	 * @param olddPattern
	 * Old date format
	 * @param newPattern
	 * New date format
	 * @return new date string
	 */
	public static String StringToString(String date, String olddPattern,
			String newPattern) {
		return DateToString(StringToDate(date, olddPattern), newPattern);
	}

	/**
	 * Converts the date string to another date string. Failed to return null.
	 * 
	 * @param date
	 * Old date string
	 * @param olddDteStyle
	 * Old date style
	 * @param newParttern
	 * New date format
	 * @return new date string
	 */
	public static String StringToString(String date, DateStyle olddDteStyle,
			String newParttern) {
		String dateString = null;
		if (olddDteStyle != null) {
			dateString = StringToString(date, olddDteStyle.getValue(),
					newParttern);
		}
		return dateString;
	}

	/**
	 * Converts the date string to another date string. Failed to return null.
	 * 
	 * @param date
	 * Old date string
	 * @param olddPattern
	 * Old date format
	 * @param newDateStyle
	 * New date style
	 * @return new date string
	 */
	public static String StringToString(String date, String olddPattern,
			DateStyle newDateStyle) {
		String dateString = null;
		if (newDateStyle != null) {
			dateString = StringToString(date, olddPattern,
					newDateStyle.getValue());
		}
		return dateString;
	}

	/**
	 * Converts the date string to another date string. Failed to return null.
	 * 
	 * @param date
	 * Old date string
	 * @param olddDteStyle
	 * Old date style
	 * @param newDateStyle
	 * New date style
	 * @return new date string
	 */
	public static String StringToString(String date, DateStyle olddDteStyle,
			DateStyle newDateStyle) {
		String dateString = null;
		if (olddDteStyle != null && newDateStyle != null) {
			dateString = StringToString(date, olddDteStyle.getValue(),
					newDateStyle.getValue());
		}
		return dateString;
	}

	/**
	 * Year of the date added. Failed to return null.
	 * 
	 * @param date
	 * Date
	 * @param yearAmount
	 * Increase the number of. Can be negative
	 * @return Adds the year after the date string
	 */
	public static String addYear(String date, int yearAmount) {
		return addInteger(date, Calendar.YEAR, yearAmount);
	}

	/**
	 * Year of the date added. Failed to return null.
	 * 
	 * @param date
	 * Date
	 * @param yearAmount
	 * Increase the number of. Can be negative
	 * @return increase the date after the year
	 */
	public static Date addYear(Date date, int yearAmount) {
		return addInteger(date, Calendar.YEAR, yearAmount);
	}

	/**
	 * Increase the date of the month. Failed to return null.
	 * 
	 * @param date
	 * Date
	 * @param monthAmount
	 * Increase the number of. Can be negative
	 * @return Increases the date string after the month
	 */
	public static String addMonth(String date, int monthAmount) {
		return addInteger(date, Calendar.MONTH, monthAmount);
	}

	/**
	 * Increase the date of the month. Failed to return null.
	 * 
	 * @param date
	 * Date
	 * @param monthAmount
	 * Increase the number of. Can be negative
	 * @return increase the date after the month
	 */
	public static Date addMonth(Date date, int monthAmount) {
		return addInteger(date, Calendar.MONTH, monthAmount);
	}

	/**
	 * The number of days to increase the date. Failed to return null.
	 * 
	 * @param date
	 * Date string
	 * @param dayAmount
	 * Increase the number of. Can be negative
	 * @return Increase the number of days after the date string
	 */
	public static String addDay(String date, int dayAmount) {
		return addInteger(date, Calendar.DATE, dayAmount);
	}

	/**
	 * The number of days to increase the date. Failed to return null.
	 * 
	 * @param date
	 * Date
	 * @param dayAmount
	 * Increase the number of. Can be negative
	 * @return increase the number of days after the date
	 */
	public static Date addDay(Date date, int dayAmount) {
		return addInteger(date, Calendar.DATE, dayAmount);
	}

	/**
	 * Increase the date of the hour. Failed to return null.
	 * 
	 * @param date
	 * Date string
	 * @param hourAmount
	 * Increase the number of. Can be negative
	 * @return Increase the date string after the hour
	 */
	public static String addHour(String date, int hourAmount) {
		return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);
	}

	/**
	 * Increase the date of the hour. Failed to return null.
	 * 
	 * @param date
	 * Date
	 * @param hourAmount
	 * Increase the number of. Can be negative
	 * @return increase the date after the hour
	 */
	public static Date addHour(Date date, int hourAmount) {
		return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);
	}

	/**
	 * Increase the date of the minute. Failed to return null.
	 * 
	 * @param date
	 * Date string
	 * @param minuteAmount
	 * Increase the number of. Can be negative
	 * @return Increase the date string after the minute
	 */
	public static String addMinute(String date, int minuteAmount) {
		return addInteger(date, Calendar.MINUTE, minuteAmount);
	}

	/**
	 * Increase the date of the minute. Failed to return null.
	 * 
	 * @param date
	 * Date
	 * @param dayAmount
	 * Increase the number of. Can be negative
	 * @return increase the date after the minute
	 */
	public static Date addMinute(Date date, int minuteAmount) {
		return addInteger(date, Calendar.MINUTE, minuteAmount);
	}

	/**
	 * Increase the date of the second. Failed to return null.
	 * 
	 * @param date
	 * Date string
	 * @param dayAmount
	 * Increase the number of. Can be negative
	 * @return Increases the date string after the second
	 */
	public static String addSecond(String date, int secondAmount) {
		return addInteger(date, Calendar.SECOND, secondAmount);
	}

	/**
	 * Increase the date of the second. Failed to return null.
	 * 
	 * @param date
	 * Date
	 * @param dayAmount
	 * Increase the number of. Can be negative
	 * @return increase the date after the second
	 */
	public static Date addSecond(Date date, int secondAmount) {
		return addInteger(date, Calendar.SECOND, secondAmount);
	}

	/**
	 * Year of acquisition date. Failed to return 0.
	 * 
	 * @param date
	 * Date string
	 * @return year
	 */
	public static int getYear(String date) {
		return getYear(StringToDate(date));
	}

	/**
	 * Year of acquisition date. Failed to return 0.
	 * 
	 * @param date
	 * Date
	 * @return year
	 */
	public static int getYear(Date date) {
		return getInteger(date, Calendar.YEAR);
	}

	/**
	 * Get the month of the date. Failed to return 0.
	 * 
	 * @param date
	 * Date string
	 * @return month
	 */
	public static int getMonth(String date) {
		return getMonth(StringToDate(date));
	}

	/**
	 * Get the month of the date. Failed to return 0.
	 * 
	 * @param date
	 * Date
	 * @return month
	 */
	public static int getMonth(Date date) {
		return getInteger(date, Calendar.MONTH) + 1;
	}

	/**
	 * The number of days to get the date. Failed to return 0.
	 * 
	 * @param date
	 * Date string
	 * @return 天
	 */
	public static int getDay(String date) {
		return getDay(StringToDate(date));
	}

	/**
	 * The number of days to get the date. Failed to return 0.
	 * 
	 * @param date
	 * Date
	 * @return 天
	 */
	public static int getDay(Date date) {
		return getInteger(date, Calendar.DATE);
	}

	/**
	 * Get the date of the hour. Failed to return 0.
	 * 
	 * @param date
	 * Date string
	 * @return hours
	 */
	public static int getHour(String date) {
		return getHour(StringToDate(date));
	}

	/**
	 * Get the date of the hour. Failed to return 0.
	 * 
	 * @param date
	 * Date
	 * @return hours
	 */
	public static int getHour(Date date) {
		return getInteger(date, Calendar.HOUR_OF_DAY);
	}

	/**
	 * Get the minutes of the date. Failed to return 0.
	 * 
	 * @param date
	 * Date string
	 * @return minutes
	 */
	public static int getMinute(String date) {
		return getMinute(StringToDate(date));
	}

	/**
	 * Get the minutes of the date. Failed to return 0.
	 * 
	 * @param date
	 * Date
	 * @return minutes
	 */
	public static int getMinute(Date date) {
		return getInteger(date, Calendar.MINUTE);
	}

	/**
	 * Get the seconds of the date. Failed to return 0.
	 * 
	 * @param date
	 * Date string
	 * @return seconds
	 */
	public static int getSecond(String date) {
		return getSecond(StringToDate(date));
	}

	/**
	 * Get the seconds of the date. Failed to return 0.
	 * 
	 * @param date
	 * Date
	 * @return seconds
	 */
	public static int getSecond(Date date) {
		return getInteger(date, Calendar.SECOND);
	}

	/**
	 * Get date. Default yyyy-MM-dd format. Failed to return null.
	 * 
	 * @param date
	 * Date string
	 * @return date
	 */
	public static String getDate(String date) {
		return StringToString(date, DateStyle.YYYY_MM_DD);
	}

	/**
	 * Get date. Default yyyy-MM-dd format. Failed to return null.
	 * 
	 * @param date
	 * Date
	 * @return date
	 */
	public static String getDate(Date date) {
		return DateToString(date, DateStyle.YYYY_MM_DD);
	}

	/**
	 * Get the date of the date. Default HH: mm: ss format. Failed to return null.
	 * 
	 * @param date
	 * Date string
	 * @return time
	 */
	public static String getTime(String date) {
		return StringToString(date, DateStyle.HH_MM_SS);
	}

	/**
	 * Get the date of the date. Default HH: mm: ss format. Failed to return null.
	 * 
	 * @param date
	 * Date
	 * @return time
	 */
	public static String getTime(Date date) {
		return DateToString(date, DateStyle.HH_MM_SS);
	}

	/**
	 * Get the date of the date. Default yyyy-MM-dd HH: mm: ss format. Failed to return null.
	 * 
	 * @param date
	 * Date string
	 * @return time
	 */
	public static String getDateTime(String date) {
		return StringToString(date, DateStyle.YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * Get the date of the date. Default yyyy-MM-dd HH: mm: ss format. Failed to return null.
	 * 
	 * @param date
	 * Date
	 * @return time
	 */
	public static String getDateTime(Date date) {
		return DateToString(date, DateStyle.YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * Get the date of the week. Failed to return null.
	 * 
	 * @param date
	 * Date string
	 * @return week
	 */
	public static Week getWeek(String date) {
		Week week = null;
		DateStyle dateStyle = getDateStyle(date);
		if (dateStyle != null) {
			Date myDate = StringToDate(date, dateStyle);
			week = getWeek (myDate);
		}
		return week;
	}

	/**
	 * Get the date of the week. Failed to return null.
	 * 
	 * @param date
	 * Date
	 * @return week
	 */
	public static Week getWeek(Date date) {
		Week week = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int weekNumber = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		switch (weekNumber) {
		case  0 :
			week = Week.SUNDAY;
			break;
		case  1 :
			week = Week.MONDAY;
			break;
		case  2 :
			week = Week.TUESDAY;
			break;
		case  3 :
			week = Week.WEDNESDAY;
			break;
		case  4 :
			week = Week.THURSDAY;
			break;
		case  5 :
			week = Week.FRIDAY;
			break;
		case  6 :
			week = Week.SATURDAY;
			break;
		}
		return week;
	}

	/**
	 * Get the difference between the two dates
	 * 
	 * @param date
	 * Date string
	 * @param otherDate
	 * Another date string
	 * @return difference days. Returns -1 if failed
	 */
	public static int getIntervalDays(String date, String otherDate) {
		return getIntervalDays(StringToDate(date), StringToDate(otherDate));
	}

	/**
	 * @param date
	 * Date
	 * @param otherDate
	 * Another date
	 * @return difference days. Returns -1 if failed
	 */
	public static int getIntervalDays(Date date, Date otherDate) {
		int num = -1;
		Date dateTmp = DateUtil.StringToDate(DateUtil.getDate(date),
				DateStyle.YYYY_MM_DD);
		Date otherDateTmp = DateUtil.StringToDate(DateUtil.getDate(otherDate),
				DateStyle.YYYY_MM_DD);
		if (dateTmp != null && otherDateTmp != null) {
			long time = Math.abs(dateTmp.getTime() - otherDateTmp.getTime());
			num = (int) (time / (24 * 60 * 60 * 1000));
		}
		return num;
	}

	/**
	 * Age of acquisition period
	 * 
	 * @param date
	 * @param otherDate
	 * @return
	 * 
	 * 2014-12-2 6:45:02 pm
	 * 
	 * @return String
	 */
	public static String getAge(Date date, Date otherDate) {
		int dis = DateUtil.getIntervalDays(new Date(), otherDate);
		int year = dis / 365;
		int month = dis % 365 / 30;
		int day = dis % 365 % 31;
		String age = (year > 0 ? year + "岁" : "")
				+ (month > 0 ? month + "个月" : "") + (day + "天");
		return age;
	}

}
