package seedu.emeraldo.logic.commands;

import seedu.emeraldo.commons.core.Messages;
import seedu.emeraldo.commons.core.UnmodifiableObservableList;
import seedu.emeraldo.commons.exceptions.IllegalValueException;
import seedu.emeraldo.commons.exceptions.TaskAlreadyCompletedException;
import seedu.emeraldo.model.task.ReadOnlyTask;
import seedu.emeraldo.model.task.Task;
import seedu.emeraldo.model.task.UniqueTaskList.TaskNotFoundException;

//@@author A0142290N
public class CompleteCommand extends Command {

	public static final String COMMAND_WORD = "completed";
	
	public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the task as complete and removes from main list."
			+ "Parameters: INDEX (must be a positive integer)"
			+ "Example: " + COMMAND_WORD + "1";
	
	public static final String MESSAGE_COMPLETE_TASK_SUCCESS = "Completed task: %1$s"; 
	public static final String MESSAGE_ALREADY_COMPLETED = "This task is already completed";
	
	public final int targetIndex;
	
	public CompleteCommand(int targetIndex) throws IllegalValueException {
		this.targetIndex = targetIndex;
	}
	
	@Override
	public CommandResult execute() {
		
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        
        Task taskToMarkComplete = (Task) lastShownList.get(targetIndex - 1);
        
        try {
            model.completedTask(taskToMarkComplete);           
        } catch (TaskAlreadyCompletedException tace){
        	return new CommandResult(MESSAGE_ALREADY_COMPLETED);
        }

        return new CommandResult(String.format(MESSAGE_COMPLETE_TASK_SUCCESS, taskToMarkComplete));
	}
}
