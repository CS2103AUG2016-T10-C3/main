package seedu.emeraldo.model.task;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.emeraldo.commons.exceptions.IllegalValueException;

//@@author A0139749L
/**
 * Parses the date and time, and check for the validity of the inputs
 */
public class DateTimeParser {

    public static final String ON_KEYWORD_VALIDATION_REGEX = "on "
            + "(?<day>([12][0-9]|3[01]|0?[1-9]))"
            + "(?:( |/|-))"
            + "(?<monthInNumbers>([1][0-2]|0?[1-9])?)"
            + "(?<monthInWords>([\\p{Alpha}]{3,})?)"
            + "(?<year>(( |/|-)(([0-9][0-9])?[0-9][0-9]))?)";

    public static final String BY_KEYWORD_VALIDATION_REGEX = "by "
            + "(?<day>([12][0-9]|3[01]|0?[1-9]))"
            + "(?:( |/|-))"
            + "(?<monthInNumbers>([1][0-2]|0?[1-9])?)"
            + "(?<monthInWords>([\\p{Alpha}]{3,})?)"
            + "(?<year>(( |/|-)(([0-9][0-9])?[0-9][0-9]))?)"
            + "(\\s*,\\s*(?<hour>([1][0-9]|[2][0-3]|0?[0-9])))"
            + "(?:(:|\\.|)?)"
            + "(?<minute>([0-5][0-9])?)"
            + "(?<timePostFix>(([a]|[p])[m])?)";

    public static final String FROM_KEYWORD_VALIDATION_REGEX = "from "
            + "(?<day>([12][0-9]|3[01]|0?[1-9]))"
            + "(?:( |/|-))"
            + "(?<monthInNumbers>([1][0-2]|0?[1-9])?)"
            + "(?<monthInWords>([\\p{Alpha}]{3,})?)"
            + "(?<year>(( |/|-)(([0-9][0-9])?[0-9][0-9]))?)"
            + "(\\s*,\\s*(?<hour>([1][0-9]|[2][0-3]|0?[0-9])))"
            + "(?:(:|\\.|)?)"
            + "(?<minute>([0-5][0-9])?)"
            + "(?<timePostFix>(([a]|[p])[m])?)"
            + "( (?<aftKeyword>(to )))"
            + "(?<dayEnd>([12][0-9]|3[01]|0?[1-9]))"
            + "(?:( |/|-))"
            + "(?<monthEndInNumbers>([1][0-2]|0?[1-9])?)"
            + "(?<monthEndInWords>([\\p{Alpha}]{3,})?)"
            + "(?<yearEnd>(( |/|-)(([0-9][0-9])?[0-9][0-9]))?)"
            + "(\\s*,\\s*(?<hourEnd>([1][0-9]|[2][0-3]|0?[0-9])))"
            + "(?:(:|\\.|)?)"
            + "(?<minuteEnd>([0-5][0-9])?)"
            + "(?<timeEndPostFix>(([a]|[p])[m])?)";

    public static final Pattern DATETIME_VALIDATION_REGEX = Pattern.compile(
            "(?<preKeyword>((by )|(on )|(from )))"
            + "(?<day>([12][0-9]|3[01]|0?[1-9]))"
            + "(?:( |/|-|\\.))"
            + "(?<monthInNumbers>([1][0-2]|0?[1-9])?)"
            + "(?<monthInWords>([\\p{Alpha}]{3,})?)"
            + "(?<year>(( |/|-|\\.)(([0-9][0-9])?[0-9][0-9]))?)"
            + "(\\s*,\\s*(?<hour>([1][0-9]|[2][0-3]|0?[0-9])))?"
            + "(?:(:|\\.|)?)"
            + "(?<minute>([0-5][0-9])?)"
            + "(?<timePostFix>(([a]|[p])[m])?)"
            + "( (?<aftKeyword>(to )))?"
            + "(?<dayEnd>([12][0-9]|3[01]|0?[1-9]))?"
            + "(?:( |/|-|\\.))?"
            + "(?<monthEndInNumbers>([1][0-2]|0?[1-9])?)"
            + "(?<monthEndInWords>([\\p{Alpha}]{3,})?)"
            + "(?<yearEnd>(( |/|-)(([0-9][0-9])?[0-9][0-9]))?)"
            + "(\\s*,\\s*(?<hourEnd>([1][0-9]|[2][0-3]|0?[0-9])))?"
            + "(?:(:|\\.|)?)"
            + "(?<minuteEnd>([0-5][0-9])?)"
            + "(?<timeEndPostFix>(([a]|[p])[m])?)"
            );
    
    public static final Pattern COMPLETED_DATE_TIME_REGEX = Pattern.compile(
    		"Completed on "
    	    + "(?<day>(0?[1-9]|[12][0-9]|3[01]))"
            + "( )"
            + "(?<monthInWords>([\\p{Alpha}]{3}))"
            + "( )"
            + "(?<year>([0-9][0-9][0-9][0-9]))"
    		+ "( at )"
            + "(?<hour>([1][0-2]|0?[0-9]))"
            + "(?:(:|\\.))"
            + "(?<minute>([0-5][0-9]))"
            + "(?<timePostFix>(([a]|[p])[m]))"
    		);

	public static final String MESSAGE_INVALID_MONTH_IN_WORDS = "Invalid month! Check your spelling";

	public static final String MESSAGE_INVALID_DATE = "Invalid inputs for date! Check that the day, month and year matches\n"
			+ "Possible mistakes: having 31 as the day for a month with 30 days, e.g. 31 Nov";

	public static final String MESSAGE_TIME_FORMAT_UNINTERPRETABLE = "Invalid inputs for time!\n"
			+ " am or pm needs to be specified when only hour is entered";

	public static final String MESSAGE_INVALID_HOUR = "Invalid hour!\n"
			+ "Hour cannot be greater than 12 for time expressed in 12 hours format";
    
	/*
     * Format the date for creation of LocalDate object
     */
    public static LocalDate valueDateFormatter(Matcher matcher, String keyword)
    		throws IllegalValueException, DateTimeException{
        
        String day = matcher.group("day");
        String month = matcher.group("monthInNumbers");
        String year = matcher.group("year");
        int yearParsed;
        int monthParsed;
        int dayParsed;

        if(keyword.equals("to")){
            day = matcher.group("dayEnd");
            month = matcher.group("monthEndInNumbers");
            year = matcher.group("yearEnd");
        }

        if(month.isEmpty()){
            month = matcher.group("monthInWords").substring(0,3);
            if(keyword.equals("to"))
                month = matcher.group("monthEndInWords").substring(0,3);
            month = convertMonthFromWordsToNumbers(month);
        }

        if(year.isEmpty())
        	yearParsed = LocalDate.now().getYear();
        else{
        	year = year.substring(1);
        
        	if(Integer.parseInt(year) < 10)	//For years that are input with only the last 2 digits
        		yearParsed = Integer.parseInt(String.valueOf(LocalDate.now().getYear()).substring(0, 1) + "0" + year);
        	else if(Integer.parseInt(year) < 100)
        		yearParsed = Integer.parseInt(String.valueOf(LocalDate.now().getYear()).substring(0, 2) + year);
        	else
        		yearParsed = Integer.parseInt(year);
        }
        
        monthParsed = Integer.parseInt(month);
        dayParsed = Integer.parseInt(day);
        
        try {
        	return LocalDate.of(yearParsed, monthParsed, dayParsed);
        } catch (DateTimeException dte){
        	throw new DateTimeException(MESSAGE_INVALID_DATE);
        }
    }

    /* 
     * Format the time for creating a LocalTime object
     */
    public static LocalTime valueTimeFormatter(Matcher matcher, String keyword) throws DateTimeException{
        
        String hour = matcher.group("hour");
        String minute = matcher.group("minute");
        String timePostFix = matcher.group("timePostFix");
        
        int hourParsed;
        int minuteParsed;
        
        if(keyword.equals("to")){
            hour = matcher.group("hourEnd");
            minute = matcher.group("minuteEnd");
            timePostFix = matcher.group("timeEndPostFix");
        }
        
        if(timePostFix.isEmpty() && minute.isEmpty())
        	throw new DateTimeException(MESSAGE_TIME_FORMAT_UNINTERPRETABLE);
        
        if(minute.isEmpty())
        	minute = "00";
        
        hourParsed = Integer.parseInt(hour);
        minuteParsed = Integer.parseInt(minute);
        
        if(!timePostFix.isEmpty())
        	hourParsed = convert12HoursFormatTo24HoursFormat(hourParsed, timePostFix);
        
        	return LocalTime.of(hourParsed, minuteParsed);
    }
    
    /*
     * Formats the date and time for display
     */
    public static String valueFormatter(Matcher matcher, String keyword) throws IllegalValueException{
        
        String day = matcher.group("day");
        String month = matcher.group("monthInNumbers");
        String year = matcher.group("year");
        String hour = matcher.group("hour");
        String minute = matcher.group("minute");
        String timePostFix = matcher.group("timePostFix");

        if(keyword.equals("to")){
            day = matcher.group("dayEnd");
            month = matcher.group("monthEndInNumbers");
            year = matcher.group("yearEnd");
            hour = matcher.group("hourEnd");
            minute = matcher.group("minuteEnd");
            timePostFix = matcher.group("timeEndPostFix");
        }

        //Append the leading '0' if not present
        if(Integer.parseInt(day) < 10 && day.length() == 1)
        	day = "0" + day;
        
        //Check for month in words when month not in numbers
        if(month.isEmpty()){
            month = matcher.group("monthInWords").substring(0,3);
            if(keyword.equals("to"))
                month = matcher.group("monthEndInWords").substring(0,3);
            month = convertMonthFromWordsToNumbers(month);
        }
      
        int monthParsed = Integer.parseInt(month);

        //If no year is read in, the year will be current year
        if(year.isEmpty())
        	year = String.valueOf(LocalDate.now().getYear());
        else
        	year = year.substring(1);
        
        if (Integer.parseInt(year) < 10)	//For years that are input with only the last 2 digits
        	year = String.valueOf(LocalDate.now().getYear()).substring(0, 1) + "0" + year;
        else if (Integer.parseInt(year) < 100)
        	year = String.valueOf(LocalDate.now().getYear()).substring(0, 2) + year;
        
        //Format time in 24 hours format into 12 hours format for display
        String[] formattedInto12Hours = new String[2];
        if(timePostFix.isEmpty() && hour != null){
        	formattedInto12Hours = convert24HoursFormatTo12HoursFormat(hour);
        	hour = formattedInto12Hours[0];
        	timePostFix = formattedInto12Hours[1];
        }
        
        if(minute.isEmpty())
        	minute = "00";
        
        if(keyword.equals("on"))
            return keyword + " " + day + " " + convertMonthFromIntToWords(monthParsed) + " " + year;
        else{
            return keyword + " " + day + " " + convertMonthFromIntToWords(monthParsed) +  " " 
                    + year + ", " + hour + "." + minute + timePostFix;
        }
    }
    
    public static LocalDate valueDateCompletedFormatter(Matcher matcher) throws IllegalValueException{
        String day = matcher.group("day");
        String month = matcher.group("monthInWords");
        String year = matcher.group("year"); 
        month = convertMonthFromWordsToNumbers(month);

    	return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
    }
    
    public static LocalTime valueTimeCompletedFormatter(Matcher matcher) throws IllegalValueException{
        String hour = matcher.group("hour");
        String minute = matcher.group("minute");
        String timePostFix = matcher.group("timePostFix");
        
        int hourParsed = convert12HoursFormatTo24HoursFormat(Integer.parseInt(hour),timePostFix);
        
        return LocalTime.of(hourParsed, Integer.parseInt(minute));
    }
    
    //@@author A0142290N
    public static String valueDateCompletedFormatter(LocalDate date) throws IllegalValueException{
    	String day = Integer.toString(date.getDayOfMonth());
    	String month = convertMonthFromIntToWords(date.getMonthValue());
    	String year = Integer.toString(date.getYear());
    	
    	return day + " " + month + " " + year;
    }
    
    
    public static String valueTimeCompletedFormatter(LocalTime time){
    	String[] formattedInto12Hours = convert24HoursFormatTo12HoursFormat(Integer.toString(time.getHour()));
    	String minute = Integer.toString(time.getMinute());
    	String hour = formattedInto12Hours[0];
    	String timePostFix = formattedInto12Hours[1];
    	
    	return hour + "." + minute + timePostFix;
    }
    
    //@@author A0139749L
    private static String convertMonthFromIntToWords(int monthParsed){
        String monthInWords;
        
        switch(monthParsed){
            case 1:
                monthInWords = "Jan";
                break;
            case 2:
                monthInWords = "Feb";
                break;
            case 3:
                monthInWords = "Mar";
                break;
            case 4:
                monthInWords = "Apr";
                break;
            case 5:
                monthInWords = "May";
                break;
            case 6:
                monthInWords = "Jun";
                break;
            case 7:
                monthInWords = "Jul";
                break;
            case 8:
                monthInWords = "Aug";
                break;
            case 9:
                monthInWords = "Sep";
                break;
            case 10:
                monthInWords = "Oct";
                break;
            case 11:
                monthInWords = "Nov";
                break;
            case 12:
                monthInWords = "Dec";
                break;             
            default: monthInWords = "Invalid month";
        }
        
        return monthInWords;
    }
    
    /**
     * Returns the month in numbers based on month in words
     * @throw IllegalValueException if month in words is not matchable
     **/
    private static String convertMonthFromWordsToNumbers(String monthInWords)  throws IllegalValueException{
        String monthInNumbers;
        switch(monthInWords.toLowerCase()){
            case "jan":
                monthInNumbers = "1";
                break;
            case "feb":
                monthInNumbers = "2";
                break;
            case "mar":
                monthInNumbers = "3";
                break;
            case "apr":
                monthInNumbers = "4";
                break;
            case "may":
                monthInNumbers = "5";
                break;
            case "jun":
                monthInNumbers = "6";
                break;
            case "jul":
                monthInNumbers = "7";
                break;
            case "aug":
                monthInNumbers = "8";
                break;
            case "sep":
                monthInNumbers = "9";
                break;
            case "oct":    
                monthInNumbers = "10";
                break;
            case "nov":
                monthInNumbers = "11";
                break;
            case "dec":
                monthInNumbers = "12";
                break;             
            default:
            	throw new IllegalValueException(MESSAGE_INVALID_MONTH_IN_WORDS);
        }
    
        return monthInNumbers;
    }
    
    /**
     * Convert hours in 12 hours format to 24 hours format
     * 
     * @param int hours
     * @param String timePostFix
     * @return int convertedHour
     */
    private static int convert12HoursFormatTo24HoursFormat(int hour, String timePostFix) throws DateTimeException{
    	
    	if(hour >= 13 || hour == 0)
    		throw new DateTimeException(MESSAGE_INVALID_HOUR);
    	
    	if(timePostFix.equalsIgnoreCase("pm") && hour != 12)
    		return hour + 12;
    	else if(timePostFix.equalsIgnoreCase("am") && hour == 12)
    		return 0;
    	else
    		return hour;
    }
    
    /**
     * Convert hours in 24 hours into 12 hours format
     * 
     * @param String hours
     * @param String timePostFix
     * @return String[] formattedInto12Hours
     */
    private static String[] convert24HoursFormatTo12HoursFormat(String hour){
    	int hourInInt = Integer.parseInt(hour);
    	String[] formattedInto12Hours = new String[2];
    	
    	if(hourInInt > 0 && hourInInt <= 11){
    		formattedInto12Hours[0] = String.valueOf(hourInInt);
    		formattedInto12Hours[1] = "am";
    	}else if(hourInInt == 0){
    		formattedInto12Hours[0] = "12";
    		formattedInto12Hours[1] = "am";
    	}else if(hourInInt == 12){
    		formattedInto12Hours[0] = String.valueOf(hourInInt);
    		formattedInto12Hours[1] = "pm";
    	}else{
    		formattedInto12Hours[0] = String.valueOf(hourInInt - 12);
    		formattedInto12Hours[1] = "pm";
    	}
    	
    	return formattedInto12Hours;
    }
   
}
