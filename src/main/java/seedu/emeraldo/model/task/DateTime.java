package seedu.emeraldo.model.task;


import seedu.emeraldo.commons.exceptions.IllegalValueException;
import java.time.*;

/**
 * Represents a Task's date and time in Emeraldo.
 * Guarantees: immutable; is valid as declared in {@link #isValidDateTime(String)}
 */
public class DateTime {
    
    public static final String MESSAGE_DATETIME_CONSTRAINTS = "Task date and time must follow this format YY/MM/DD HH:MM";
    public static final String ADDRESS_VALIDATION_REGEX = "(.+)?";

    public final LocalDateTime value;
    public final LocalDate valueDate;
    public final LocalTime valueTime;

    /**
     * Validates given date and time.
     *
     * @throws IllegalValueException if given address string is invalid.
     */
    public DateTime(String dateTime) throws IllegalValueException {
        assert dateTime != null;
        if (!isValidDateTime(dateTime)) {
            throw new IllegalValueException(MESSAGE_DATETIME_CONSTRAINTS);
        }
        
        String [] dateTimeArr = dateTime.split(" ");
        String [] dateArr = dateTimeArr[0].split("/");
        int year = Integer.parseInt(dateArr[2]);
        int month = Integer.parseInt(dateArr[1]);
        int day = Integer.parseInt(dateArr[0]);
        this.valueDate = LocalDate.of(year, month, day);
        
        String [] timeArr = dateTimeArr[1].split(":");
        int hour = Integer.parseInt(timeArr[0]);
        int minute = Integer.parseInt(timeArr[1]);
        this.valueTime = LocalTime.of(hour, minute);
        
        this.value = LocalDateTime.of(this.valueDate, this.valueTime);
        
    }

    private static boolean isValidDateTime(String test) {
    	return test.matches(ADDRESS_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
    	
    	return value.toString();
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