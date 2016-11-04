package seedu.emeraldo.logic.commands;

import static seedu.emeraldo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.HashSet;
import java.util.Set;

import seedu.emeraldo.commons.core.Messages;
import seedu.emeraldo.commons.core.UnmodifiableObservableList;
import seedu.emeraldo.commons.exceptions.IllegalValueException;
import seedu.emeraldo.commons.exceptions.TagListEmptyException;
import seedu.emeraldo.model.tag.Tag;
import seedu.emeraldo.model.tag.UniqueTagList;
import seedu.emeraldo.model.task.ReadOnlyTask;
import seedu.emeraldo.model.task.Task;

/**
* Edit the tags of a particular task in the task manager.
*/
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Add/Delete/Clear the tags to/of the task identified by the index number used in the last tasks listing.\n"
            + "Parameters: add/delete/clear INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " add" + " 1" + " #friends";
    
    public static final String MESSAGE_TAG_EDIT_SUCCESS = "Edited task: %1$s";
    private static final String MESSAGE_TAG_LIST_EMPTY = "Tags are empty!";    
    
    private String action;
    private int targetIndex;
    private Tag tag;
    
    
    public TagCommand(String action, String targetIndex, String tag) throws IllegalValueException {
        this.action = action.trim();
        this.targetIndex = Integer.parseInt(targetIndex);
        this.tag = new Tag(tag.replaceFirst(" #", ""));
    }
    
    @Override
    public CommandResult execute() {
        
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskTagToEdit = (Task) lastShownList.get(targetIndex - 1);
        
        if (action.equalsIgnoreCase("add")){
            model.addTag(taskTagToEdit, tag);
        }
        else if (action.equalsIgnoreCase("delete")){
            model.deleteTag(taskTagToEdit, tag);
        }
        else if (action.equalsIgnoreCase("clear")){
            try{
                model.clearTag(taskTagToEdit);
            } catch (TagListEmptyException tlee) {
                return new CommandResult(MESSAGE_TAG_LIST_EMPTY);
            }
        }
        else{
            return new CommandResult(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        }
       
        return new CommandResult(String.format(MESSAGE_TAG_EDIT_SUCCESS, targetIndex - 1));
    }

}
