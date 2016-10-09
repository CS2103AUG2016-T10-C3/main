package seedu.address.testutil;

import seedu.emeraldo.model.tag.UniqueTagList;
import seedu.emeraldo.model.task.*;

/**
 * A mutable person object. For testing only.
 */
public class TestPerson implements ReadOnlyTask {

    private Description name;
    private DateTime dateTime;
    private Phone phone;
    private UniqueTagList tags;

    public TestPerson() {
        tags = new UniqueTagList();
    }

    public void setName(Description name) {
        this.name = name;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    @Override
    public Description getDescription() {
        return name;
    }

    @Override
    public Phone getPhone() {
        return phone;
    }

    @Override
    public DateTime getDateTime() {
        return dateTime;
    }

    @Override
    public UniqueTagList getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return getAsText();
    }

    public String getAddCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append("add " + this.getDescription().fullName + " ");
        sb.append("p/" + this.getPhone().value + " ");
        sb.append("a/" + this.getDateTime().value + " ");
        this.getTags().getInternalList().stream().forEach(s -> sb.append("t/" + s.tagName + " "));
        return sb.toString();
    }
}
