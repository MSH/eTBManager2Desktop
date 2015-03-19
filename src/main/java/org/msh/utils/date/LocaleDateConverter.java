package org.msh.utils.date;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.msh.etbm.desktop.app.Messages;

public class LocaleDateConverter {

	
	/**
	 * Return a string representation of a date ready for displaying
	 * @param dt
	 * @return
	 */
	static public String getDisplayDate(Date dt, boolean includeTime) {
		if (dt == null) {
			return "";
		}
		String patt = Messages.getString("locale.outputDatePattern");
		if (includeTime)
			patt += " HH:mm:ss";
		Locale locale = Locale.getDefault();
		SimpleDateFormat df = new SimpleDateFormat(patt, locale);
		
		return df.format(dt);
	}

	/**
	 * Return a string representation of the difference between two dates including
	 * time information. The string will be created in a human-readable way in the 
	 * format:<p/>
	 * <code>nn years, nn months, nn days, nn hous, nn minutes, nn seconds.</code>
	 * <p/>
	 * Value of 0 is hidden.
	 * @param period is the instance of {@link Period} containing the two dates
	 * @return String value
	 */
	public static String getAsTimeLength(Period period) {
		Calendar c = DateUtils.calcDifference(period.getIniDate(), period.getEndDate());

		long len = c.getTimeInMillis();

		String s = "";

		// is bigger than 24 hours ?
		if (len > 1000 * 60 * 60 * 24) {
			Date dt = period.getIniDate();
			
			int years = DateUtils.yearsBetween(dt, period.getEndDate());

			dt = DateUtils.incYears(dt, years);
			int months = DateUtils.monthsBetween(dt, period.getEndDate());
			
			dt = DateUtils.incMonths(dt, months);
			int days = DateUtils.daysBetween(dt, period.getEndDate());

			if (years > 0) {
				if (years == 1)
					 s = years + " " + Messages.getString("global.year");
				else s = years + " " + Messages.getString("global.years");
			}
			
			if (months > 0) {
				if (!s.isEmpty())
					s = s + ", ";
				if (months == 1)
					 s = s + months + " " + Messages.getString("global.month");
				else s = s + months + " " + Messages.getString("global.months");
			}

			if (days > 0) {
				if (!s.isEmpty())
					s = s + ", ";
				if (days == 1)
					 s = s + days + " " + Messages.getString("global.day");
				else s = s + days + " " + Messages.getString("global.days");
			}
		}
		else {
			int hours = (int)Math.floor((float)len / (1000F * 60F * 60F));
			if (hours > 0)
				len %= (hours * 1000 * 60 * 60);
			
			int min = (int)Math.floor((float)len / (1000F * 60F));
			if (min > 0)
				len %= (min * 1000 * 60);
			
			int sec = (int)Math.floor((float)len / 1000F);
			
			if (hours > 0) {
				if (hours > 1)
					 s = hours + " " + Messages.getString("global.hour");
				else s = hours + " " + Messages.getString("global.hours");
			}
			
			if (min > 0) {
				if (!s.isEmpty())
					s += " ";
				 s += min + " " + Messages.getString("global.min");
			}

			if ((hours == 0) && (sec > 0)) {
				if (!s.isEmpty())
					s += " ";
				s += sec + " " + Messages.getString("global.sec");
			}
		}
		return s;
	}


	/**
	 * Return a string representation of the difference between two dates with no
	 * time information. The string will be created in a human-readable way in the 
	 * format:<p/>
	 * <code>nn years, nn months, nn days.</code>
	 * <p/>
	 * Value of 0 is hidden.
	 * @param period is the instance of {@link Period} containing the two dates
	 * @return String value
	 */
	public static String getAsLength(Period period) {
		// make adjustment in the final date including 1 more day to count the exactly period of month or year
		Date dtIni = period.getIniDate();
		Date dtEnd = period.getEndDate();
		if ((dtIni == null) || (dtEnd == null))
			return "<null>";
		dtEnd = DateUtils.incDays(dtEnd, 1);
		
		int years = DateUtils.yearsBetween(dtIni, dtEnd);

		String s = "";
		if (years > 0) {
			if (years == 1)
				 s = years + " " + Messages.getString("global.year");
			else s = years + " " + Messages.getString("global.years");
			dtIni = DateUtils.incYears(dtIni, years);
		}
		
		int months = DateUtils.monthsBetween(dtIni, dtEnd);
		if (months > 0) {
			if (!s.isEmpty())
				s = s + ", ";
			if (months == 1)
				 s = s + months + " " + Messages.getString("global.month");
			else s = s + months + " " + Messages.getString("global.months");
			dtIni = DateUtils.incMonths(dtIni, months);
		}

		int days = DateUtils.daysBetween(dtIni, dtEnd);
		
		if (days > 0) {
			if (!s.isEmpty())
				s = s + ", ";
			if (days == 1)
				 s = s + days + " " + Messages.getString("global.day");
			else s = s + days + " " + Messages.getString("global.days");
		}
	
		return (s.isEmpty()? "-": s);
	}
	
	/**
	 * Converts to a string containing the length of two dates or in a number of days
	 * @param comp - the component containing the value
	 * @param obj - Can be a Date object indicating the beginning date or an Integer object indicating the number of days to be formated
	 * @return
	 */
	public static String getAsMonth(Period period) {
		// make adjustment in the final date including 1 more day to count the exactly period of month or year
		Date dtIni = period.getIniDate();
		Date dtEnd = period.getEndDate();
		if ((dtIni == null) || (dtEnd == null))
			return "<null>";
		dtEnd = DateUtils.incDays(dtEnd, 1);
		
		String s = "";
		/**
		 * Commmenting code to fetch period in years. Instead the period is fetched in months
		 */

//		int years = DateUtils.yearsBetween(dtIni, dtEnd);
//
//		
//		if (years > 0) {
//			if (years == 1)
//				 s = years + " " + Messages.getString("global.year");
//			else s = years + " " + Messages.getString("global.years");
//			dtIni = DateUtils.incYears(dtIni, years);
//		}
		
		int months = DateUtils.monthsBetween(dtIni, dtEnd);
		if (months > 0) {
			if (!s.isEmpty())
				s = s + ", ";
			if (months == 1)
				 s = s + months + " " + Messages.getString("global.month");
			else s = s + months + " " + Messages.getString("global.months");
			dtIni = DateUtils.incMonths(dtIni, months);
		}

		int days = DateUtils.daysBetween(dtIni, dtEnd);
		
		if (days > 0) {
			if (!s.isEmpty())
				s = s + ", ";
			if (days == 1)
				 s = s + days + " " + Messages.getString("global.day");
			else s = s + days + " " + Messages.getString("global.days");
		}
	
		return (s.isEmpty()? "-": s);
	}

	
	/**
	 * Return a human-readable string representation of a difference of two
	 * with the suffix "ago", indicating it occurred in the past 
	 * @param period instance of the {@link Period} class containing the two dates
	 * @return String value
	 */
	public static String getAsElapsedTime(Period period) {
		String s = getAsTimeLength(period);

		if (!s.isEmpty())
			s = MessageFormat.format(Messages.getString("global.timeago"), s);
		
		return s;
	}
}
