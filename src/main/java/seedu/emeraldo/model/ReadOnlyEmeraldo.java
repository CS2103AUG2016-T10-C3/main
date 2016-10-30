package seedu.emeraldo.model;


import seedu.emeraldo.model.tag.Tag;
import seedu.emeraldo.model.tag.UniqueTagList;
import seedu.emeraldo.model.task.ReadOnlyTask;
import seedu.emeraldo.model.task.Task;
import seedu.emeraldo.model.task.UniqueTaskList;

import java.util.Iterator;
import java.util.List;

/**
 * Unmodifiable view of Emeraldo
 */
public interface ReadOnlyEmeraldo {

    UniqueTagList getUniqueTagList();

    UniqueTaskList getUniqueTaskList();
   
    /**
     * Returns an unmodifiable view of tasks list
     */
    List<ReadOnlyTask> getTaskList();

    /**
     * Returns an unmodifiable view of tags list
     */
    List<Tag> getTagList();
    
    //@@author A0142290N
    
    /**
     * Generates an integer representing a color for any tag that is created
     */
//    default int tagColorGenerator(String tagName) {
//    	//int tagColor = getUniqueTaskList().getNextTagColor();
//    	int tagColor = 0;
//    	Iterator<Task> taskIterator = getUniqueTaskList().iterator();
//    	
//    	while (taskIterator.hasNext()){
//    		Task task = getUniqueTaskList().iterator().next();
//	    	Iterator<Tag> tagIterator = task.getTags().iterator();
//	    	
//	    	//Returns tagColor of the existing tag if the input tag already exists
//	    	while (tagIterator.hasNext()){
//	    		Tag tag = tagIterator.next();
//	    		if(tag.toString().equals(tagName))
//	    			tagColor = tag.tagColor;
//	    	}
//    	}
//    	return tagColor;
//    }

    //@@author

}
