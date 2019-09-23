package com.project.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public class TimeUtils {
	/**
	 * 由出生日期获得年龄  
	 */
    public static int getAge(String birthDay,String format){
    	Date date=getDateFromTime(format,birthDay);
    	
        Calendar cal = Calendar.getInstance();  
        if (cal.before(birthDay)) {  
            throw new IllegalArgumentException(  
                    "生日大于当前时间，不合法!");
        }  
        int yearNow = cal.get(Calendar.YEAR);  
        int monthNow = cal.get(Calendar.MONTH);  
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);  
        cal.setTime(date);  
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);  
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);  
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {  
                if (dayOfMonthNow < dayOfMonthBirth) age--;  
            }else{  
                age--;  
            }  
        }  
        return age;  
    }  
    
	/**
	 * 根据当前日期获得上周的开始时间
	 */
	public static String getLastWeekBegin(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar calendar1 = Calendar.getInstance();
		int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
		int offset1 = 1 - dayOfWeek;
		calendar1.add(Calendar.DATE, offset1 - 7);
		return sdf.format(calendar1.getTime());
	}
	/**
	 * 根据当前日期获得上周的结束时间
	 */
	public static String getLastWeekEnd(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
		int offset2 = 7 - dayOfWeek;
		calendar2.add(Calendar.DATE, offset2 - 7);
		return sdf.format(calendar2.getTime());
	}
	
	/**
	 * 今天在当前月中的第几天
	 */
	public static int getDayWithMonth() {
		Date date = new Date();
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		return ca.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 今天在当前年中的第几天
	 */
	public static int getDayWithYear() {
		Date date = new Date();
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		return ca.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 当前月最后一天
	 */
	public static String getCurMonthLastDay(String format) {
		DateFormat df = new SimpleDateFormat(format);
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		return df.format(ca.getTime());
	}

	/**
	 * 2个时间比大小
	 */
	public static int compareDate(String time1, String time2, String format){
		DateFormat df = new SimpleDateFormat(format);
		Date d1;
		Date d2;
		try {
			d1 = df.parse(time1);
			d2 = df.parse(time2);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("日期不合法");
		}
		// 相等
		return Long.compare(d1.getTime(), d2.getTime());
	}

	/**
	 * 2个时间相差天数
	 */
	public static int getDayCompareDate(String time1, String time2, String format){
		DateFormat df = new SimpleDateFormat(format);
		Date d1;
		Date d2;
		try {
			d1 = df.parse(time1);
			d2 = df.parse(time2);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("日期不合法");
		}
		long mis=d1.getTime()-d2.getTime();
		return (int) (mis/(24*60*60*1000));
	}
	
	/**
	 * 2个时间相差小时
	 */
	public static int getHourCompareDate(String time1, String time2, String format) throws ParseException {
		DateFormat df = new SimpleDateFormat(format);
		Date d1 = df.parse(time1);
		Date d2 = df.parse(time2);

		long mis=d1.getTime()-d2.getTime();
		return (int) (mis/(60*60*1000));
	}
	
	/**
	 * 2个时间相差分钟
	 */
	public static int getMinCompareDate(String format,String time1, String time2) throws ParseException {
		DateFormat df = new SimpleDateFormat(format);
		Date d1 = df.parse(time1);
		Date d2 = df.parse(time2);
		long mis=d1.getTime()-d2.getTime();
		return (int) (mis/(60*1000));
	}
	
	/**
	 * 时间转毫秒数
	 */
	public static long getMsecFromDate(String dateTime, String format) {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(new SimpleDateFormat(format).parse(dateTime));
			return c.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 毫秒数转时间
	 */
	public static String getTimeFromMs(long ms, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);// 设置日期格式
		return df.format(new Date(ms));
	}

	/**
	 * 获取当前时间
	 */
	public static String getCurrentTime(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);// 设置日期格式
		return df.format(new Date());
	}
	/**
	 * 获取当前时间（无参数）
	 */
	public static String getCurrentTime(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	/**
	 * 字符串转date
	 */
	public static Date getDateFromTime(String format, String time) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);// 小写的mm表示的是分钟
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获得某个时间后几天的时间
	 */
	public static String getTimeDayAfter(String format, String time, int day) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);// 小写的mm表示的是分钟
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return getTimeDayAfter(format,date,day);
	}
	/**
	 * 获得某个时间后几天的时间
	 */
	public static String getTimeDayAfter(String format, Date date, int day) {
		SimpleDateFormat dft = new SimpleDateFormat(format);// 设置日期格式

		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);

		return dft.format(now.getTime());
	}

	/**
	 * 获得某个时间后几天的时间(毫秒数)
	 */
	public static long getMisDayAfter(Date date, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);

		return now.getTime().getTime();
	}

	/**
	 * 获得某个时间后几个小时的时间
	 */
	public static String getTimeHoursAfter(String format, Date date, int hour) {
		SimpleDateFormat dft = new SimpleDateFormat(format);// 设置日期格式

		Calendar dar = Calendar.getInstance();
		dar.setTime(date);
		dar.add(Calendar.HOUR_OF_DAY, hour);

		return dft.format(dar.getTime());
	}

	/**
	 * 获得某个时间后几分钟的时间

	 */
	public static String getTimeMinuteAfter(String format, Date date, int minute) {
		SimpleDateFormat dft = new SimpleDateFormat(format);// 设置日期格式

		Calendar dar = Calendar.getInstance();
		dar.setTime(date);
		dar.add(Calendar.MINUTE, minute);
		return dft.format(dar.getTime());
	}
	
	/**
	 * 获得某个时间后几天的时间

	 */
	public static String getTimeMinuteAfter(String format, String time, int minute) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);// 小写的mm表示的是分钟
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return getTimeMinuteAfter(format,date,minute);
	}
	/**
	 * 获取当前日期
	 */
	public static String getCurrentDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}
}
