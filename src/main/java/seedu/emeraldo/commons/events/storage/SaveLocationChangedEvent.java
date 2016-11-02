package seedu.emeraldo.commons.events.storage;

import seedu.emeraldo.commons.events.BaseEvent;

/** Indicates the save location of emeraldo.xml has changed*/
public class SaveLocationChangedEvent extends BaseEvent {
    
    public String filepath;
    
    public SaveLocationChangedEvent(String filepath){
        this.filepath = filepath;
    }
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return null;
    }

}
