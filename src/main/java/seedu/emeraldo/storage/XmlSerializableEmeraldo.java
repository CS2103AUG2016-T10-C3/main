package seedu.emeraldo.storage;

import seedu.emeraldo.commons.exceptions.IllegalValueException;
import seedu.emeraldo.model.ReadOnlyEmeraldo;
import seedu.emeraldo.model.tag.Tag;
import seedu.emeraldo.model.tag.UniqueTagList;
import seedu.emeraldo.model.task.ReadOnlyTask;
import seedu.emeraldo.model.task.UniqueTaskList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An Immutable Emeraldo that is serializable to XML format
 */
@XmlRootElement(name = "emeraldo")
public class XmlSerializableEmeraldo implements ReadOnlyEmeraldo {

    @XmlElement
    private List<XmlAdaptedTask> tasks;
    @XmlElement
    private List<Tag> tags;

    {
        tasks = new ArrayList<>();
        tags = new ArrayList<>();
    }

    /**
     * Empty constructor required for marshalling
     */
    public XmlSerializableEmeraldo() {}

    /**
     * Conversion
     */
    public XmlSerializableEmeraldo(ReadOnlyEmeraldo src) {
        tasks.addAll(src.getTaskList().stream().map(XmlAdaptedTask::new).collect(Collectors.toList()));
        tags = src.getTagList();
    }

    @Override
    public UniqueTagList getUniqueTagList() {
        try {
            return new UniqueTagList(tags);
        } catch (UniqueTagList.DuplicateTagException e) {
            //TODO: better error handling
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UniqueTaskList getUniqueTaskList() {
        UniqueTaskList lists = new UniqueTaskList();
        for (XmlAdaptedTask p : tasks) {
            try {
                lists.add(p.toModelType());
            } catch (IllegalValueException e) {
                //TODO: better error handling
            }
        }
        return lists;
    }

    @Override
    public List<ReadOnlyTask> getTaskList() {
        return tasks.stream().map(p -> {
            try {
                return p.toModelType();
            } catch (IllegalValueException e) {
                e.printStackTrace();
                //TODO: better error handling
                return null;
            }
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Tag> getTagList() {
        return Collections.unmodifiableList(tags);
    }

}
