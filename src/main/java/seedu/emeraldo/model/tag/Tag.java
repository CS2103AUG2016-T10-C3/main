package seedu.emeraldo.model.tag;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.emeraldo.commons.exceptions.IllegalValueException;

/**
 * Represents a Tag in Emeraldo.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    public static final String MESSAGE_TAG_CONSTRAINTS = "Tags names should be alphanumeric";
    public static final String TAG_VALIDATION_REGEX = "\\p{Alnum}+";
    public static int tagColors = 0;

    public String tagName;
    public int tagColor;

    private final ObservableList<Tag> internalList = FXCollections.observableArrayList();
    
    public Tag() {
    }

    /**
     * Validates given tag name, gives color 0.
     *
     * @throws IllegalValueException if the given tag name string is invalid.
     */
    public Tag(String name) throws IllegalValueException {
        assert name != null;
        name = name.trim();
        if (!isValidTagName(name)) {
            throw new IllegalValueException(MESSAGE_TAG_CONSTRAINTS);
        }
        this.tagName = name;
        this.tagColor = 0;
    }
    
    /**
     * Validates given tag name and color.
     *
     * @throws IllegalValueException if the given tag name string is invalid.
     */   
    public Tag(String name, int color) throws IllegalValueException {
        assert name != null;
        name = name.trim();
        if (!isValidTagName(name)) {
            throw new IllegalValueException(MESSAGE_TAG_CONSTRAINTS);
        }
        this.tagName = name;
        this.tagColor = color;
    }
    
    public void setTagColor(int tagColor){
        this.tagColor = tagColor;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(TAG_VALIDATION_REGEX);
    }
    
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Tag // instanceof handles nulls
                && this.tagName.equals(((Tag) other).tagName)); // state check
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

}
