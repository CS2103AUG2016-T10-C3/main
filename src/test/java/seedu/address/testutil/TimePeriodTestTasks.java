package seedu.address.testutil;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import seedu.emeraldo.commons.exceptions.IllegalValueException;

//@@author A0139749L
/*
 * Provides data for testing of timePeriods
 */
public class TimePeriodTestTasks {
	TestTask t1, t2, t3, t4, t5, t6, t7, t8;
	
	public TimePeriodTestTasks(){
		try{
			t1 = new TaskBuilder().withDescription("Today's task").withDateTime("by " + generatesRequiredDate("t1") + ", 12:01").build();
            t2 = new TaskBuilder().withDescription("Tomorrow's task").withDateTime("on " + generatesRequiredDate("t2")).build();
            t3 = new TaskBuilder().withDescription("This week's monday task").withDateTime("on " + generatesRequiredDate("t3")).build();
            t4 = new TaskBuilder().withDescription("This week's sunday task").withDateTime("on " + generatesRequiredDate("t4")).build();
            t5 = new TaskBuilder().withDescription("Next week's monday task")
            		.withDateTime("from " + generatesRequiredDate("t5") + ", 2pm to " + generatesRequiredDate("t5") + ", 5pm").build();
            t6 = new TaskBuilder().withDescription("Next week's sunday task").withDateTime("by " + generatesRequiredDate("t6") + ", 7.20pm").build();
            t7 = new TaskBuilder().withDescription("Last week's sunday task").withDateTime("by " + generatesRequiredDate("t7") + ", 11:01").build();
            t8 = new TaskBuilder().withDescription("Yesterday's task").withDateTime("on " + generatesRequiredDate("t8")).build();
		}catch(IllegalValueException e){
			e.printStackTrace();
		}
	}
	
	public List<TestTask> generateAllTimePeriodTestTasks(){
	    return Arrays.asList(new TestTask[]{t1, t2, t3, t4, t5, t6, t7, t8});
	}

	public TestTask[] getExpectedList(String keywordForTesting) {
		TestTask[] expectedList;
		switch(keywordForTesting){
			case "today":
				expectedList = new TestTask[]{t1};
				break;
			case "tomorrow":
				expectedList = new TestTask[]{t2};
				break;
			case "thisweek":
				expectedList = new TestTask[]{t3,t4};
				break;
			case "nextweek":
				expectedList = new TestTask[]{t5,t6};
				break;
			default:
				expectedList = new TestTask[]{t1, t2, t3, t4, t5, t6, t7, t8};
		}
		return expectedList;
	}
	
	private String generatesRequiredDate(String taskRequiredDate){
		LocalDate requiredDate;
		switch(taskRequiredDate){
			case "t1":		//Today's date
				requiredDate = LocalDate.now();
				break;
			case "t2":		//Tomorrow's date
				requiredDate = LocalDate.now().plusDays(1);
				break;
			case "t3":		//This week's Monday date
				requiredDate = dateOfThisWeekSunday().minusDays(6);
				break;
			case "t4":		//This week's Sunday date
				requiredDate = dateOfThisWeekSunday();
				break;
			case "t5":		//Next week's Monday date
				requiredDate = dateOfThisWeekSunday().plusDays(1);
				break;
			case "t6":		//Next week's Sunday date
				requiredDate = dateOfThisWeekSunday().plusWeeks(1);
				break;
			case "t7":		//Last week's Sunday date
				requiredDate = dateOfThisWeekSunday().minusWeeks(1);
				break;
			case "t8":		//Yesterday's date
				requiredDate = LocalDate.now().minusDays(1);
				break;
			default:
				requiredDate = LocalDate.now();
		}
		return convertsLocalDate_Into_DateTimeAcceptedString(requiredDate);
	}
	
    //Returns a LocalDate object with the date of this week's Sunday
    private LocalDate dateOfThisWeekSunday(){
    	int noOfDaysFromTodayToSunday = 7 - LocalDate.now().getDayOfWeek().getValue();
    	return LocalDate.now().plusDays(noOfDaysFromTodayToSunday);
    }
	
	private String convertsLocalDate_Into_DateTimeAcceptedString(LocalDate localDate){
		int day = localDate.getDayOfMonth();
		int month = localDate.getMonthValue();
		int year = localDate.getYear();
		
		return day + "/" + month + "/" + year;
	}
}
