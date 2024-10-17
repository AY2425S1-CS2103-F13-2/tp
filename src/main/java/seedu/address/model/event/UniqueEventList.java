package seedu.address.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.event.exceptions.DuplicateEventException;
import seedu.address.model.event.exceptions.EventNotFoundException;
import seedu.address.model.id.UniqueId;

/**
 * A list of events that enforces uniqueness between its elements and does not allow nulls.
 * An event is considered unique by comparing using {@code Event#isSameEvent(Event)}.
 * As such, adding and updating of events uses Event#isSameEvent(Event) for equality to
 * ensure that the event being added or updated is unique in terms of identity in the UniqueEventList.
 * Supports a minimal set of list operations.
 *
 * @see Event#isSameEvent(Event)
 */
public class UniqueEventList implements Iterable<Event> {

    private final ObservableList<Event> internalList = FXCollections.observableArrayList();
    private final ObservableList<Event> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);
    private final Map<UniqueId, Event> eventMap = new HashMap<>();

    /**
     * Returns true if the list contains an equivalent event as the given argument.
     */
    public boolean contains(Event toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameEvent);
    }

    /**
     * Adds an event to the list, generating a new unique {@code UniqueId} for the event.
     * The event must not already exist in the list.
     */
    public void add(Event toAdd) {
        requireNonNull(toAdd);
        UniqueId eventId = new UniqueId(); // Generate a new UniqueId (UUID)
        if (contains(toAdd)) {
            throw new DuplicateEventException();
        }
        eventMap.put(eventId, toAdd); // Store the event in the map with its unique ID
        internalList.add(toAdd); // Also add the event to the internal list
    }

    /**
     * Returns the unique ID of the given {@code event}.
     * Throws an {@code EventNotFoundException} if the event is not found.
     */
    public UniqueId getEventId(Event event) {
        requireNonNull(event);
        for (Map.Entry<UniqueId, Event> entry : eventMap.entrySet()) {
            if (entry.getValue().equals(event)) {
                return entry.getKey(); // Return the associated UniqueId
            }
        }
        throw new EventNotFoundException(); // Event not found
    }

    /**
     * Removes the equivalent event from the list.
     * The event must exist in the list.
     */
    public void remove(Event toRemove) {
        requireNonNull(toRemove);

        UniqueId eventId = getEventId(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new EventNotFoundException();
        }

        // Remove from both the internal list and the map
        eventMap.remove(eventId);
    }

    /**
     * Replaces the contents of this list with the vendors from {@code replacement}.
     * The replacement {@code UniqueVendorList} must not contain duplicate vendors.
     */
    public void setEvents(UniqueEventList replacement) {
        requireNonNull(replacement);

        List<Event> replacementEvents = replacement.internalList;
        if (!eventsAreUnique(replacementEvents)) {
            throw new DuplicateEventException();
        }

        eventMap.clear();
        internalList.clear();

        for (Event event : replacementEvents) {
            add(event);
        }
    }

    /**
     * Replaces the contents of this list with {@code events}.
     * {@code events} must not contain duplicate events.
     */
    public void setEvents(List<Event> events) {
        requireAllNonNull(events);
        if (!eventsAreUnique(events)) {
            throw new DuplicateEventException();
        }

        eventMap.clear(); // Clear the map
        internalList.clear(); // Clear the internal list

        for (Event event : events) {
            add(event); // Use the add method to assign new UniqueIds
        }
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Event> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Event> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UniqueEventList)) {
            return false;
        }

        UniqueEventList otherUniqueEventList = (UniqueEventList) other;
        return internalList.equals(otherUniqueEventList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code events} contains only unique events.
     */
    private boolean eventsAreUnique(List<Event> events) {
        for (int i = 0; i < events.size() - 1; i++) {
            for (int j = i + 1; j < events.size(); j++) {
                if (events.get(i).isSameEvent(events.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
