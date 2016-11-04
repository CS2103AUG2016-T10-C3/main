package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.TestTask;
import seedu.address.testutil.TimePeriodTestTasks;
import seedu.emeraldo.logic.commands.ListCommand;

public class ListCommandTest extends EmeraldoGuiTest{
	
	@Test
	public void execute_list_showsTodayTasks() {
        commandBox.runCommand("clear");
        
		TimePeriodTestTasks testTasks = new TimePeriodTestTasks();
		assertListResult("list today", "today", testTasks.getExpectedList("today"));
	}
	
	@Test
	public void execute_list_showsTomorrowTasks() {
    	TimePeriodTestTasks testTasks = new TimePeriodTestTasks();
		assertListResult("list tomorrow", "tomorrow", testTasks.getExpectedList("tomorrow"));
	}
	
	@Test
	public void execute_list_showsThisweekTasks() {
    	TimePeriodTestTasks testTasks = new TimePeriodTestTasks();
		assertListResult("list thisweek", "this week", testTasks.getExpectedList("thisweek"));
	}
	
	@Test
	public void execute_list_showsNextweekTasks() {
    	TimePeriodTestTasks testTasks = new TimePeriodTestTasks();
		assertListResult("list nextweek", "next week", testTasks.getExpectedList("nextweek"));
	}
	
    private void assertListResult(String command, String printedKeyword, TestTask... expectedHits) {
        commandBox.runCommand(command);
        assertListSize(expectedHits.length);
        assertResultMessage(String.format(ListCommand.MESSAGE_LIST_TIMEPERIOD, printedKeyword));
        assertTrue(taskListPanel.isListMatching(expectedHits));
    }
}
