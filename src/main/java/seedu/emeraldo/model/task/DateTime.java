package seedu.emeraldo.model.task;


import seedu.emeraldo.commons.exceptions.IllegalValueException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;

/**
 * Represents a Task's date and time in Emeraldo.
 * Guarantees: immutable; is valid as declared in {@link #isValidDateTime(String)}
 */
public class DateTime {
    
	//@@author A0139749L
    private static final String MESSAGE_KEYWORD_FROM_CONSTRAINTS = "Invalid format! It should be "
            + "'from DD/MM/YYYY, HH:MM to DD/MM/YYYY, HH:MM'\n"
            + "Accepted date formats:  4/03/2016  |  4/03/16  |  4-03-16  |  4 March 16  |  4/03  |  4 Mar\n"
            + "Accepted time formats:  14:20  |  14.20  |  1420  |  2.20pm  |  2:20pm\n"
    		+ "Type 'help' to see the full list of accepted formats in the user guide";

    private static final String MESSAGE_KEYWORD_BY_CONSTRAINTS = "Invalid format! It should be "
            + "'by DD/MM/YYYY, HH:MM'\n"
            + "Accepted date formats:  4/03/2016  |  4/03/16  |  4-03-16  |  4 March 16  |  4/03  |  4 Mar\n"
            + "Accepted time formats:  14:20  |  14.20  |  1420  |  2.20pm  |  2:20pm\n"
    		+ "Type 'help' to see the full list of accepted formats in the user guide";

    private static final String MESSAGE_KEYWORD_ON_CONSTRAINTS = "Invalid format! It should be "
            + "'on DD/MM/YYYY'\n"
            + "Accepted date formats:  4/03/2016  |  4/03/16  |  4-03-16  |  4 March 16  |  4/03  |  4 Mar\n"
            + "Type 'help' to see the full list of accepted formats in the user guide";

    public static final String MESSAGE_DATETIME_CONSTRAINTS = "Command format is invalid! "
    		+ "It must be one of the following (type 'help' to see all accepted formats):\n"
            + "Keyword 'on' : on DD/MM/YYYY\n"
            + "Keyword 'by' : by DD/MM/YYYY, HH:MM\n"
            + "Keyword 'from' and 'to' : from DD/MM/YYYY, HH:MM to DD/MM/YYYY, HH:MM";

	private static final String MESSAGE_PERIOD_INVALID = "Start date/time is later or same as end date/time!";

    public final String value;
    public String context;
    public String overdueContext;
    public String eventContext;
    public String valueFormatted;
    public LocalDate valueDate;
    public LocalTime valueTime;
    public LocalDate valueDateEnd;
    public LocalTime valueTimeEnd;
    public LocalDate completedValueDate;
    public LocalTime completedValueTime;

    /**
     * Validates given date and time.
     *
     * @throws IllegalValueException if given date and time string is invalid.
     */
    public DateTime(String dateTime) throws IllegalValueException, DateTimeException{
        assert dateTime != null;
        final Matcher matcher = DateTimeParser.DATETIME_VALIDATION_REGEX.matcher(dateTime);
        
        if (!dateTime.isEmpty() && !matcher.matches()) {
            throw new IllegalValueException(MESSAGE_DATETIME_CONSTRAINTS);
        }
        
        this.completedValueDate = null;
        this.completedValueTime = null;
        
        if(dateTime.isEmpty()){
            this.valueDate = null;
            this.valueTime = null;
            this.valueDateEnd = null;
            this.valueTimeEnd = null;
            this.value = "";
            this.valueFormatted = "anytime";
            this.context = "";
            this.overdueContext = "";
            this.eventContext = "";
            
        } else {

            final String preKeyword = matcher.group("preKeyword").trim();
            
            if(preKeyword.equals("on")){
                if(!isValidFormatFor_GivenKeyword(dateTime, preKeyword))
                    throw new IllegalValueException(MESSAGE_KEYWORD_ON_CONSTRAINTS);
                
                this.valueDate = DateTimeParser.valueDateFormatter(matcher, preKeyword);
                this.context = setContext(valueDate, null);
                this.overdueContext = setOverdueContext(valueDate, null);
                this.eventContext = "";
                this.valueFormatted = DateTimeParser.valueFormatter(matcher, preKeyword) + context;
             
                this.valueTime = null;
                this.valueDateEnd = null;
                this.valueTimeEnd = null;
                
            }else if(preKeyword.equals("by")){
                if(!isValidFormatFor_GivenKeyword(dateTime, preKeyword))
                    throw new IllegalValueException(MESSAGE_KEYWORD_BY_CONSTRAINTS);
                 
                this.valueDate = DateTimeParser.valueDateFormatter(matcher, preKeyword);                
                this.valueTime = DateTimeParser.valueTimeFormatter(matcher, preKeyword); 
                this.context = setContext(valueDate, valueTime);
                this.overdueContext = setOverdueContext(valueDate, valueTime);  
                this.eventContext = "";
                this.valueFormatted = DateTimeParser.valueFormatter(matcher, preKeyword) + context;

                this.valueDateEnd = null;
                this.valueTimeEnd = null;
                
            }else if(preKeyword.equals("from")){
                if(!isValidFormatFor_GivenKeyword(dateTime, preKeyword))
                    throw new IllegalValueException(MESSAGE_KEYWORD_FROM_CONSTRAINTS);
                
                final String aftKeyword = matcher.group("aftKeyword").trim();
                
                this.valueDate = DateTimeParser.valueDateFormatter(matcher, preKeyword);                
                this.valueTime = DateTimeParser.valueTimeFormatter(matcher, preKeyword);
                this.context = setContext(valueDate, valueTime);

                this.valueDateEnd = DateTimeParser.valueDateFormatter(matcher, aftKeyword);
                this.valueTimeEnd = DateTimeParser.valueTimeFormatter(matcher, aftKeyword);
                
                if(!isStartDateTime_Before_EndDateTime(valueDate,valueTime,valueDateEnd,valueTimeEnd))
                    throw new IllegalValueException(MESSAGE_PERIOD_INVALID);
                
                this.overdueContext = setOverdueContext(valueDateEnd, valueTimeEnd); 
                this.eventContext = setEventContext(valueDate, valueTime, valueDateEnd, valueTimeEnd);
                
                this.valueFormatted = DateTimeParser.valueFormatter(matcher, preKeyword) + " "
                                    + DateTimeParser.valueFormatter(matcher, aftKeyword) + context + eventContext;                     
            }
            this.value = dateTime;
        }
    }

    public DateTime(String dateTime, String completedDateTime) throws IllegalValueException, DateTimeException{
    	//Calls the other constructor to initialise the values less completedValueDate and completedValueTime
    	this(dateTime);
    	setCompletedDateTime(completedDateTime);
    }
    
    /*
     * Converts completedDateTime from a String into LocalDate and LocalTime
     */
    private void setCompletedDateTime(String completedDateTime) throws IllegalValueException{	
    	if(completedDateTime.isEmpty()){
        	this.completedValueDate = null;
        	this.completedValueTime = null;       	
    	}else{   
    		final Matcher matcher = DateTimeParser.COMPLETED_DATE_TIME_REGEX.matcher(completedDateTime); 
    		if(!matcher.matches()){
    			throw new IllegalValueException("Error in format of completedDateTime stored in Xml");
    		}
    		this.completedValueDate = DateTimeParser.valueDateCompletedFormatter(matcher); 
    		this.completedValueTime = DateTimeParser.valueTimeCompletedFormatter(matcher);
    		this.valueFormatted = completedDateTime;
    	}
    }
    
    private boolean isValidFormatFor_GivenKeyword(String dateTime, String keyword){
        switch(keyword){
            case "on":
                return dateTime.matches(DateTimeParser.ON_KEYWORD_VALIDATION_REGEX);
            case "by": 
                return dateTime.matches(DateTimeParser.BY_KEYWORD_VALIDATION_REGEX);
            case "from":
                return dateTime.matches(DateTimeParser.FROM_KEYWORD_VALIDATION_REGEX);
            default:
                return false;
        }
    }
    
    private boolean isStartDateTime_Before_EndDateTime(LocalDate startDate, LocalTime startTime,
    		LocalDate endDate, LocalTime endTime){
    	
    	//Condition returns true if startDate > endDate
    	if(startDate.isAfter(endDate))	
    		return false;
    	//Condition returns true if startDate == endDate and startTime >= endTime
    	else if(startDate.isEqual(endDate) && (startTime.equals(endTime) || startTime.isAfter(endTime))) 
    		return false;
    	else
    		return true;
    }
    

    /*
     * Used by XmlAdaptedTask to set its String completedDateTime 
     */
    public String completedDateTime_ToString(){
    	if(completedValueDate == null)
    		return "";
    	else
    		return this.toString();
    }
    
    //@@author A0142290N    
    public void setCompletedDateTime() throws IllegalValueException{
    	this.completedValueDate = LocalDate.now();
    	this.completedValueTime = LocalTime.now();
    	this.valueFormatted = "Completed on " + DateTimeParser.valueDateCompletedFormatter(completedValueDate) 
    		+ " at " + DateTimeParser.valueTimeCompletedFormatter(completedValueTime);
    }  

    public String setContext(LocalDate valueDate, LocalTime valueTime) {
    	String context = ""; 
    	Boolean timeIsNow = valueTime != null && valueTime.getHour() == LocalTime.now().getHour() && valueTime.getMinute() == LocalTime.now().getMinute();
    	Boolean dayIsToday = valueDate.isEqual(LocalDate.now());
    	Boolean timeIsLater = valueTime != null && valueTime.isAfter(LocalTime.now());
    	Boolean noTimeSpecified = valueTime == null;
    	Boolean dayIsTomorrow = valueDate.minusDays(1).isEqual(LocalDate.now());
    	Boolean dayIsBeforeToday = valueDate.isBefore(LocalDate.now());
    	String stringHours = ""; 
    	
    	//For tasks due today, now
    	if (dayIsToday && timeIsNow)   	
        	context = "(Today; Now)";
    		
        //For tasks that is due today, after current time
        else if (dayIsToday && timeIsLater){
        	stringHours = Long.toString(LocalTime.now().until(valueTime, ChronoUnit.HOURS));
            context = " (Today; in " + stringHours + " hours) ";
        }
    	
    	//For tasks due today at unspecified times
    	else if (dayIsToday && noTimeSpecified)
    		context = " (Today)";
        
    	//For tasks due tomorrow
        else if (dayIsTomorrow)
        	context = " (Tomorrow)";
    	
        else if (dayIsBeforeToday)
        	context = "";
    	
        else
            context = "";
    	
    	return context;
    }
    
    public String setOverdueContext(LocalDate valueDate, LocalTime valueTime) {
    	String overdueContext = "";
    	Boolean dayIsBeforeToday = valueDate.isBefore(LocalDate.now());
    	Boolean dayIsToday = valueDate.isEqual(LocalDate.now());
    	Boolean dayIsAfterToday = valueDate.isEqual(LocalDate.now());
    	Boolean timeIsBeforeNow = valueTime != null && valueTime.isBefore(LocalTime.now());
    	
    	//For tasks due before today
        if (dayIsBeforeToday){
        	int monthsDue = valueDate.until(LocalDate.now()).getMonths();
        	int yearsDue = valueDate.until(LocalDate.now()).getYears();
        	int daysDue = valueDate.until(LocalDate.now()).getDays();
        	String stringDaysDue = Integer.toString(daysDue);
        	String stringMonthsDue = Integer.toString(monthsDue);
        	String stringYearsDue = Integer.toString(yearsDue);
        	String periodDue = "";
        	
        	if (monthsDue > 0 && yearsDue > 0)
        		periodDue = stringYearsDue + " years, " + stringMonthsDue + " months and " + stringDaysDue + " days";
        	
        	else if (monthsDue > 0 && yearsDue == 0)
        		periodDue = stringMonthsDue + " months and " + stringDaysDue + " days ";
            
        	else if (monthsDue == 0 && yearsDue == 0){
        		if (daysDue == 1)
        			periodDue = valueDate.until(LocalDate.now()).getDays() + " day";
        		else
        			periodDue = valueDate.until(LocalDate.now()).getDays() + " days";
        	}
        	
        	else 
        		periodDue = "";
        	
        	overdueContext = "Overdue by " + periodDue;
        }
        
        //For tasks that is due today, at or before current time
        else if (dayIsToday && timeIsBeforeNow) {
        	String stringHoursDue = Long.toString(valueTime.until(LocalTime.now(), ChronoUnit.HOURS));       	
        	String periodDue = stringHoursDue + " hours ";
			
			overdueContext = "Due just now, " + periodDue + "ago"; 
		} 
			
		else if (dayIsAfterToday) {
			overdueContext = "";
		}
        
        return overdueContext;
    }
    
    public String setEventContext(LocalDate valueDate, LocalTime valueTime, LocalDate valueDateEnd, LocalTime valueTimeEnd) {
    	String eventContext = "";
    	LocalDateTime valueDateTime = LocalDateTime.of(valueDate, valueTime);
    	LocalDateTime valueDateTimeEnd = LocalDateTime.of(valueDateEnd, valueTimeEnd);
    	Boolean duringEventTime = valueDateTime.isBefore(LocalDateTime.now()) && valueDateTimeEnd.isAfter(LocalDateTime.now());
    	
    	if (duringEventTime)
    		eventContext = " (Today; Now)";
    	
    	else 
    		eventContext = "";
    	
    	return eventContext;
    }
    
    public String getOverdueContext(){
    	return overdueContext;
    }
    
    public String getEventContext(){
    	return eventContext;
    }

    @Override
    public String toString() {
        return valueFormatted;
    }
    //@@author
    
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DateTime // instanceof handles nulls
                && this.valueFormatted.equals(((DateTime) other).valueFormatted)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
