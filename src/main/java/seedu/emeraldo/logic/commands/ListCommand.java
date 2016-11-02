package seedu.emeraldo.logic.commands;


/**
 * Lists all tasks in Emeraldo to the user.
 */
//@@author A0139749L
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_LIST_ALL = "Listed all tasks";
    
    public static final String MESSAGE_LIST_UNCOMPLETED = "Listed all uncompleted tasks";
    
    public static final String MESSAGE_LIST_KEYWORD = "Tasks with tag '%s' successfully listed!\n";
    
    public static final String MESSAGE_USAGE = "(1) " + COMMAND_WORD + " :  Lists all uncompleted tasks\n"
    		+ "(2) " + COMMAND_WORD + " [PRE-DEFINED KEYWORDS] :  Lists all tasks in the period specified\n"
            + "(3) " + COMMAND_WORD + " [KEYWORD] :  Lists all tasks with tags containing the specified keyword\n"
            + "Example:  " + COMMAND_WORD + "  |  " + COMMAND_WORD + " today  |  " + COMMAND_WORD + " tomorrow"
    		+ "  |  " + COMMAND_WORD + " homework";

    private String keyword;
    private String successMessage;
    
    public ListCommand(String keyword){
        this.keyword = keyword;
    }
    
    @Override
    public CommandResult execute() {
        if(keyword.isEmpty()){
            model.updateFilteredListToShowUncompleted();
            this.successMessage = MESSAGE_LIST_UNCOMPLETED;
        }else if(keyword.equalsIgnoreCase("all")){
        	model.updateFilteredListToShowAll();
        	this.successMessage = MESSAGE_LIST_ALL;
        }else if(keyword.equalsIgnoreCase("today")){
        	
        }else if(keyword.equalsIgnoreCase("tomorrow")){
        	
    	}else{
            model.updateFilteredTaskList(keyword);
            this.successMessage = String.format(MESSAGE_LIST_KEYWORD, keyword.toLowerCase())
            		+ getMessageForTaskListShownSummary(model.getFilteredTaskList().size());
        }
        return new CommandResult(successMessage);
    }
}
