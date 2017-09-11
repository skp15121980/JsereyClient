package com.porschenote.jerseyrestclient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainClass {
public static void main(String[] args) {
/*	TimeZone timeZone = TimeZone.getTimeZone("UTC");
	Calendar calendar = Calendar.getInstance(timeZone);
	SimpleDateFormat simpleDateFormat = 
	       new SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", Locale.US);
	simpleDateFormat.setTimeZone(timeZone);

	System.out.println("Time zone: " + timeZone.getID());
	System.out.println("default time zone: " + TimeZone.getDefault().getID());
	System.out.println();

	System.out.println("UTC:     " + simpleDateFormat.format(calendar.getTime()));
	System.out.println("Default: " + calendar.getTime());
	
	DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

	Date date = null;
	try {
		date = utcFormat.parse("2012-08-15T22:56:02.038Z");
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	pstFormat.setTimeZone(TimeZone.getTimeZone("PST"));

	System.out.println(pstFormat.format(date));*/
	
	SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
	dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
	
	SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
	dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));

	//Local time zone   
	SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
	dateFormatLocal.setTimeZone(TimeZone.getDefault());
	//Time in GMT
	try {
		Date gmt= dateFormatLocal.parse( dateFormatGmt.format(new Date())) ;
		Date local= dateFormatLocal.parse( dateFormatLocal.format(new Date())) ;
		Date utc= dateFormatLocal.parse( dateFormatUTC.format(new Date())) ;
		System.out.println("gmt : "+gmt+" --- "+dateFormatGmt.format(new Date()));
		System.out.println("local :"+local+" --- "+dateFormatLocal.format(new Date()));
		System.out.println("utc :"+utc+" --- "+dateFormatUTC.format(new Date()));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
