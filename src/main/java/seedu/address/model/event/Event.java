package seedu.address.model.event;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.commons.name.Name;
import seedu.address.model.commons.tag.Tag;
import seedu.address.model.id.UniqueId;

/**
 * Represents an Event in EventTory.
 * Guarantees: Name and date are not null.
 */
public class Event {
    private final Name name;
    private final Date date;
    private final UniqueId id;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Constructor for an Event with an automatically generated UniqueId.
     *
     * @param name The name of the event.
     * @param date The date of the event.
     * @param tags The tags of the event.
     */
    public Event(Name name, Date date, Set<Tag> tags) {
        requireAllNonNull(name, date, tags);
        this.id = new UniqueId();
        this.name = name;
        this.date = date;
        this.tags.addAll(tags);
    }

    /**
     * Constructor for an Event with a specified UniqueId.
     *
     * @param id The unique identifier for the event.
     * @param name The name of the event.
     * @param date The date of the event.
     */
    public Event(UniqueId id, Name name, Date date) {
        requireAllNonNull(id, name, date);
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public UniqueId getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    /**
     * Returns an immutable tag set, which throws
     * {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both events have the same name.
     * This defines a weaker notion of equality between two events.
     */
    public boolean isSameEvent(Event otherEvent) {
        if (otherEvent == this) {
            return true;
        }

        return otherEvent != null
                && otherEvent.getName().equals(getName());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Event)) {
            return false;
        }

        Event otherEvent = (Event) other;
        return name.equals(otherEvent.name)
                && date.equals(otherEvent.date)
                && tags.equals(otherEvent.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, date, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("id", id)
                .add("name", name)
                .add("date", date)
                .add("tags", tags)
                .toString();
    }
}
