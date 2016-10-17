package seedu.emeraldo.model.task;


import seedu.emeraldo.commons.exceptions.IllegalValueException;
import java.time.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a Task's date and time in Emeraldo.
 * Guarantees: immutable; is valid as declared in {@link #isValidDateTime(String)}
 */
public class DateTime {
    
    public static final String MESSAGE_DATETIME_CONSTRAINTS = "Task date and time must follow this format DD/MM/YYYY HH:MM in 24 hours format";
    public static final Pattern DATETIME_VALIDATION_REGEX =
            Pattern.compile("((?<day>(0?[1-9]|[12][0-9]|3[01]))"            //Day regex
                    + "/(?<month>(0?[1-9]|[1][0-2]))/"                      //Month regex
                    + "(?<year>(([0-9][0-9])?[0-9][0-9])))"                 //Year regex
                    + "( (?<hour>([01][0-9]|[2][0-3])))?"                   //Hour regex
                    + "(:(?<minute>([0-5][0-9])))?");                       //Minute regex
    
    public final String value;
//    public final LocalDateTime valueDateTime;
    public final LocalDate valueDate;
    public final LocalTime valueTime;

    /**
     * Validates given date and time.
     *
     * @throws IllegalValueException if given date and time string is invalid.
     */
    public DateTime(String dateTime) throws IllegalValueException {
        assert dateTime != null;
        final Matcher matcher = DATETIME_VALIDATION_REGEX.matcher(dateTime.trim());
        if (!matcher.matches()) {
            throw new IllegalValueException(MESSAGE_DATETIME_CONSTRAINTS);
        }
        
        final String day = matcher.group("day");
        final String month = matcher.group("month");
        final String year = matcher.group("year");
        final String hour = matcher.group("hour");
        final String minute = matcher.group("minute");
        this.value = dateTime;
        
        if(dateTime.equals("")){            //Constructing an empty DateTime object 
//            this.valueDateTime = null;
            this.valueDate = null;
            this.valueTime = null;
            
        }else if(!dateTime.contains(":")){           //Constructing a DateTime object with Date only
            int yearParsed = Integer.parseInt(year);
            int monthParsed = Integer.parseInt(month);
            int dayParsed = Integer.parseInt(day);
            this.valueDate = LocalDate.of(yearParsed, monthParsed, dayParsed);
            
//            this.valueDateTime = null;
            this.valueTime = null;
            
        } else {                                    //Constructing a DateTime object with Date and time
            int yearParsed = Integer.parseInt(year);
            int monthParsed = Integer.parseInt(month);
            int dayParsed = Integer.parseInt(day);
            this.valueDate = LocalDate.of(yearParsed, monthParsed, dayParsed);
        
            int hourParsed = Integer.parseInt(hour);
            int minuteParsed = Integer.parseInt(minute);
            this.valueTime = LocalTime.of(hourParsed, minuteParsed);
        
//            this.valueDateTime = LocalDateTime.of(this.valueDate, this.valueTime);
        }
    }
/*
    private static boolean isValidDateTime(String test) {
        return test.matches(DATETIME_VALIDATION_REGEX);
    }
*/
    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DateTime // instanceof handles nulls
                && this.value.equals(((DateTime) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
