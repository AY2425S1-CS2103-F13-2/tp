package seedu.address.model.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_BIRTHDAY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_WEDDING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_WEDDING;
import static seedu.address.testutil.TypicalEvents.BIRTHDAY;
import static seedu.address.testutil.TypicalEvents.WEDDING;

import java.util.HashSet;
import java.util.Set;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import seedu.address.model.commons.name.Name;
import seedu.address.model.commons.tag.Tag;
import seedu.address.model.id.UniqueId;
import seedu.address.testutil.EventBuilder;

public class EventTest {

    private Name name = new Name(VALID_NAME_WEDDING);
    private Date date = new Date(VALID_DATE_WEDDING);
    private Set<Tag> tags = new HashSet<>();

    @Test
    public void constructor_nullValues_throwsNullPointerException() {
        // Check if each field being null throws NullPointerException
        assertThrows(NullPointerException.class, () -> new Event(null, date, tags));
        assertThrows(NullPointerException.class, () -> new Event(name, null, tags));
        // Check if each field being null throws NullPointerException
        assertThrows(NullPointerException.class, () -> new Event(name, date, null));
    }

    @Test
    public void eventConstructor_validValues_createsEvent() {
        Event event = new Event(name, date, tags);

        // Validate that the Event object is created with the correct values
        assertEquals(name, event.getName());
        assertEquals(date, event.getDate());
    }

    @Test
    public void isSameEvent_nullEvent_returnsFalse() {
        assertFalse(WEDDING.isSameEvent(null));
    }

    @Test
    public void isSameEvent_sameEvent_returnsTrue() {
        assertTrue(WEDDING.isSameEvent(WEDDING));
    }

    @Test
    public void isSameEvent_differentEvent_returnsFalse() {
        assertFalse(WEDDING.isSameEvent(BIRTHDAY));
    }

    @Test
    public void isSameEvent_eventWithSameIdentity_returnsTrue() {
        Event similarWedding = new EventBuilder(WEDDING).withDate(VALID_DATE_BIRTHDAY).build();
        assertTrue(WEDDING.isSameEvent(similarWedding));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Event event = new Event(name, date, tags);

        // Validate that an object equals itself
        assertTrue(event.equals(event));
    }

    @Test
    public void equals_differentObjectsSameValues_returnsTrue() {
        Name name = new Name("Workshop");
        Date date = new Date("2024-10-10");

        // Generate a valid UUID
        UniqueId id = new UniqueId(UUID.randomUUID().toString());
        Event event1 = new Event(id, name, date, tags);
        Event event2 = new Event(id, name, date, tags);

        // Validate that two different Event objects with the same values (including UniqueId) are equal
        assertTrue(event1.equals(event2));
    }

    @Test
    public void equals_differentValues_returnsFalse() {
        Name name1 = new Name("Workshop");
        Date date1 = new Date("2024-10-10");

        Name name2 = new Name("Seminar");
        Date date2 = new Date("2024-11-11"); // different date

        Event event1 = new Event(name1, date1, tags);
        Event event2 = new Event(name2, date2, tags);

        // Validate that two Event objects with different values are not equal
        assertFalse(event1.equals(event2));

        Event eventWithTwoTags = new EventBuilder().withTags("tag", "test").build();
        Event eventWithOneTags = new EventBuilder().withTags("tag").build();
        // Validate that two Event objects with different tags are not equal
        assertFalse(eventWithTwoTags.equals(eventWithOneTags));
    }

    @Test
    public void hashCode_consistencyWithEquals() {
        Name name = new Name("Workshop");
        Date date = new Date("2024-10-10");

        UniqueId id = new UniqueId(UUID.randomUUID().toString());
        Event event1 = new Event(id, name, date, tags);
        Event event2 = new Event(id, name, date, tags);

        // Check that if two objects are equal, their hashCodes are also equal
        assertTrue(event1.equals(event2));
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    public void toString_checkFormat() {
        Name name = new Name("Conference");
        Date date = new Date("2024-10-10");
        UniqueId id = new UniqueId(UUID.randomUUID().toString());
        Event event = new Event(id, name, date, tags);

        String expected = Event.class.getCanonicalName() + "{id=" + id.toString()
                + ", name=Conference, date=2024-10-10, tags=[]}";
        assertEquals(expected, event.toString());
    }
}

