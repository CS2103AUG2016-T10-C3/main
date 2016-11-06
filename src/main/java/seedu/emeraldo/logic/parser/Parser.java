package seedu.emeraldo.logic.parser;

import seedu.emeraldo.commons.exceptions.IllegalValueException;
import seedu.emeraldo.commons.util.StringUtil;
import seedu.emeraldo.logic.commands.*;

import static seedu.emeraldo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.emeraldo.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.time.DateTimeException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final Pattern TASK_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

    private static final Pattern FIND_KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace

    private static final Pattern LIST_KEYWORD_ARGS_FORMAT =
            Pattern.compile("(?<keyword>\\S+)?"); //Only one keyword
    
    private static final Pattern LISTALL_KEYWORD_ARGS_FORMAT = 
    		Pattern.compile("(?<keyword>\\S+)?"); //Only one keyword
    
    //@@author A0139749L
    private static final Pattern TASK_DATA_ARGS_FORMAT = // '/' forward slashes are reserved for delimiter prefixes
            Pattern.compile("\"(?<description>.+)\""
                    + "(?<dateTime>((( by )|( on )|( from ))[^#]+)?)"
                    + "(?<tagArguments>(?: #[^#]+)*)"); // variable number of tags
    
    //@@author A0139342H
    private static final Pattern TASK_EDIT_ARGS_FORMAT = 
            Pattern.compile("(?<targetIndex>\\d+)" //index must be digits
            + "\\s+"                               //any number of whitespace
            + "(?<description>(\"[^\"]+\")?)"      //quote marks are reserved for start and end of description field
            + "( )?(?<dateTime>(((by )|(on )|(from ))[^#]+)?)"
            );
    
    //@@author A0139196U
    private static final Pattern TASK_TAG_ARGS_FORMAT = 
            Pattern.compile("(?<action>((add )|(delete )|(clear )))"
            + "(?<targetIndex>\\d+)" //index must be digits
            + "(?<tag>(?: #[^#]+)?)");    // one or zero tag
    //@@author
    
    private static final Pattern SAVE_LOCATION = Pattern.compile("(?<targetLocation>(([^\\/\\s]*\\/)+|default))");
    
    public Parser() {}

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws IllegalValueException 
     */
    public Command parseCommand(String userInput) throws IllegalValueException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord").toLowerCase();
        final String arguments = matcher.group("arguments");
                
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return prepareAdd(arguments);

        case SelectCommand.COMMAND_WORD:
            return prepareSelect(arguments);

        case DeleteCommand.COMMAND_WORD:
            return prepareDelete(arguments);
            
        case EditCommand.COMMAND_WORD:
            return prepareEdit(arguments);
            
        case TagCommand.COMMAND_WORD:
            return prepareTag(arguments);
       
        case CompleteCommand.COMMAND_WORD:
        	return prepareComplete(arguments);
        
        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return prepareFind(arguments);
            
        case FindAllCommand.COMMAND_WORD:
        	return prepareFindAll(arguments);

        case ListCommand.COMMAND_WORD:
            return prepareList(arguments);
        
        case ListAllCommand.COMMAND_WORD:
            return prepareListAll(arguments);
            
        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();
        
        case SaveToCommand.COMMAND_WORD:
            return prepareSaveTo(arguments);

        case MotivateMeCommand.COMMAND_WORD:
            return new MotivateMeCommand();

        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    //@@author A0139342H
    /**
     * Parses arguments in the context of the saveto command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareSaveTo(String args) {
    	//TO-DO: Implement exception handling
        final Matcher matcher = SAVE_LOCATION.matcher(args.trim());
        
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SaveToCommand.MESSAGE_USAGE));
        }
        
        if(args.trim().equals("default")){
            args = SaveToCommand.DEFAULT_LOCATION;
        }
        else{
            args = matcher.group("targetLocation");
        }
        
        return new SaveToCommand(args);
        
    }
    //@@author

    /**
     * Parses arguments in the context of the add task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareAdd(String args){
        final Matcher matcher = TASK_DATA_ARGS_FORMAT.matcher(args.trim());
        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        try {
            return new AddCommand(
                    matcher.group("description"),
                    matcher.group("dateTime"),
                    getTagsFromArgs(matcher.group("tagArguments"))
            );
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        } catch (DateTimeException dte){
        	return new IncorrectCommand(dte.getMessage());
        }
    }

    /**
     * Extracts the new task's tags from the add command's tag arguments string.
     * Merges duplicate tag strings.
     */
    private static Set<String> getTagsFromArgs(String tagArguments) throws IllegalValueException {
        // no tags
        if (tagArguments.isEmpty()) {
            return Collections.emptySet();
        }
        // replace first delimiter prefix, then split
        final Collection<String> tagStrings = Arrays.asList(tagArguments.replaceFirst(" #", "").split(" #"));
        return new HashSet<>(tagStrings);
    }

    /**
     * Parses arguments in the context of the delete task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareDelete(String args) {

        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return new DeleteCommand(index.get());
    }
    
    //@@author A0139196U
    /**
     * Parses arguments in the context of the edit person command.
     * 
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareEdit(String args) {
        
        final Matcher matcher = TASK_EDIT_ARGS_FORMAT.matcher(args.trim());
        
        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }
        
        Optional<Integer> index = parseIndex(matcher.group("targetIndex"));
        
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }
        
        try {
            String description = matcher.group("description");
            if(!description.isEmpty()){
                description = description.split("\"")[1];
            }
            
            return new EditCommand(
                    matcher.group("targetIndex"),
                    description,
                    matcher.group("dateTime").trim());
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }        
    }
    
    /**
     * Parses arguments in the context of the edit tag command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareTag(String args) {

        final Matcher matcher = TASK_TAG_ARGS_FORMAT.matcher(args.trim());
        
        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        }
        
        Optional<Integer> index = parseIndex(matcher.group("targetIndex"));
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        }
        
        if (matcher.group("action").trim().equalsIgnoreCase("clear")){
            try {
                return new TagCommand(
                        matcher.group("action"),
                        matcher.group("targetIndex")
                );
            } catch (IllegalValueException ive) {
                return new IncorrectCommand(ive.getMessage());
            }
        }
        else {
            try {
                return new TagCommand(
                        matcher.group("action"),
                        matcher.group("targetIndex"),
                        matcher.group("tag")
                );
            } catch (IllegalValueException ive) {
                return new IncorrectCommand(ive.getMessage());
            }
        }
        
    }
    
    //@@author A0142290N
    /**
     * Parses arguments in the context of the complete task command
     * @param args full command args string
     * @return the prepared command
     * @throws IllegalValueException
     */
    Command prepareComplete(String args) throws IllegalValueException {
    	
    	  Optional<Integer> index = parseIndex(args);
          if(!index.isPresent()){
              return new IncorrectCommand(
                      String.format(MESSAGE_INVALID_COMMAND_FORMAT, CompleteCommand.MESSAGE_USAGE));
          }

          return new CompleteCommand(index.get());
    	
    }
    //@@author

    /**
     * Parses arguments in the context of the select task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareSelect(String args) {
        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
        }

        return new SelectCommand(index.get());
    }

    /**
     * Returns the specified index in the {@code command} IF a positive unsigned integer is given as the index.
     *   Returns an {@code Optional.empty()} otherwise.
     */
    private Optional<Integer> parseIndex(String command) {
        final Matcher matcher = TASK_INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if(!StringUtil.isUnsignedInteger(index)){
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(index));

    }

    /**
     * Parses arguments in the context of the find task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareFind(String args) {
        final Matcher matcher = FIND_KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindCommand.MESSAGE_USAGE));
        }

        // keywords delimited by whitespace
        final String[] keywords = matcher.group("keywords").split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return new FindCommand(keywordSet);
    }
    
    //@@author A0142290N
    /**
     * Parses arguments in the context of the findall task command.
     *
     * @param args full command args string
     * @return the prepared command
     */   
    private Command prepareFindAll(String args) {
        final Matcher matcher = FIND_KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindAllCommand.MESSAGE_USAGE));
        }

        // keywords delimited by whitespace
        final String[] keywords = matcher.group("keywords").split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return new FindAllCommand(keywordSet);
    }

    //@@author A0139749L
    /**
     * Parses arguments in the context of the list task command.
     *
     * @param args full command args string
     * @return the prepared command
     */    
    private Command prepareList(String args) {
        final Matcher matcher = LIST_KEYWORD_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ListCommand.MESSAGE_USAGE));
        }

        return new ListCommand(args.trim());
    }
    
    private Command prepareListAll(String args) {
        final Matcher matcher = LISTALL_KEYWORD_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ListAllCommand.MESSAGE_USAGE));
        }

        return new ListAllCommand(args.trim());
    }
}
