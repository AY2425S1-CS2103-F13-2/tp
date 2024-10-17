package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalVendors.ALICE;
import static seedu.address.testutil.TypicalVendors.BOB;
import static seedu.address.testutil.TypicalVendors.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.association.Association;
import seedu.address.model.event.Date;
import seedu.address.model.event.Event;
import seedu.address.model.event.Name;
import seedu.address.model.event.exceptions.DuplicateEventException;
import seedu.address.model.vendor.Vendor;
import seedu.address.model.vendor.exceptions.DuplicateVendorException;
import seedu.address.testutil.VendorBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();
    private final Event testEvent = new Event(new Name("Test Event"), new Date("2024-10-11"));
    private final Event anotherEvent = new Event(new Name("Another Event"), new Date("2024-10-11"));
    private final Event similarTestEvent = new Event(new Name("Test Event"), new Date("2023-05-20"));

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getVendorList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicateVendors_throwsDuplicateVendorException() {
        // Two vendors with the same identity fields
        Vendor editedAlice = new VendorBuilder(ALICE).withDescription(VALID_DESCRIPTION_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Vendor> newVendors = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newVendors);

        assertThrows(DuplicateVendorException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasVendor_nullVendor_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasVendor(null));
    }

    @Test
    public void hasVendor_vendorNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasVendor(ALICE));
    }

    @Test
    public void hasVendor_vendorInAddressBook_returnsTrue() {
        addressBook.addVendor(ALICE);
        assertTrue(addressBook.hasVendor(ALICE));
    }

    @Test
    public void hasVendor_vendorWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addVendor(ALICE);
        Vendor editedAlice = new VendorBuilder(ALICE).withDescription(VALID_DESCRIPTION_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasVendor(editedAlice));
    }

    @Test
    public void hasEvent_nullEvent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasEvent(null));
    }

    @Test
    public void hasEvent_eventNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasEvent(testEvent));
    }

    @Test
    public void hasEvent_eventInAddressBook_returnsTrue() {
        addressBook.addEvent(testEvent);
        assertTrue(addressBook.hasEvent(testEvent));
    }

    @Test
    public void hasEvent_eventWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addEvent(testEvent);
        assertTrue(addressBook.hasEvent(similarTestEvent));
    }

    // might be redundant has this is already tested in UniqueEventListTest.java
    @Test
    public void addEvent_nullEvent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.addEvent(null));
    }

    // might be redundant has this is already tested in UniqueEventListTest.java
    @Test
    public void addEvent_eventInAddressBook_throwsDuplicateEventException() {
        addressBook.addEvent(testEvent);
        assertThrows(DuplicateEventException.class, () -> addressBook.addEvent(testEvent));
    }

    // might be redundant has this is already tested in UniqueEventListTest.java
    @Test
    public void addEvent_eventWithSameIdentityFieldsInAddressBook_throwsDuplicateEventException() {
        addressBook.addEvent(testEvent);
        assertThrows(DuplicateEventException.class, () -> addressBook.addEvent(testEvent));
    }

    @Test
    public void getVendorList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getVendorList().remove(0));
    }

    @Test
    public void getEventList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getEventList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName()
                + "{vendors=" + addressBook.getVendorList() + ", "
                + "events=" + addressBook.getEventList()
                + "}";
        assertEquals(expected, addressBook.toString());
    }

    @Test
    public void getAssociatedVendors_noAssociations_returnsEmptyList() {
        ObservableList<Vendor> associatedVendors = addressBook.getAssociatedVendors(testEvent);
        assertEquals(FXCollections.observableArrayList(), associatedVendors);
    }

    @Test
    public void getAssociatedVendors_withAssociations_returnsCorrectVendors() {
        addressBook.addVendor(ALICE);
        addressBook.addVendor(BOB);
        addressBook.addEvent(testEvent);
        addressBook.assignVendorToEvent(ALICE, testEvent);
        addressBook.assignVendorToEvent(BOB, testEvent);

        ObservableList<Vendor> associatedVendors = addressBook.getAssociatedVendors(testEvent);
        ObservableList<Vendor> expectedVendors = FXCollections.observableArrayList(BOB, ALICE);

        assertEquals(expectedVendors, associatedVendors);
    }

    @Test
    public void getAssociatedEvents_noAssociations_returnsEmptyList() {
        ObservableList<Event> associatedEvents = addressBook.getAssociatedEvents(ALICE);
        assertEquals(FXCollections.observableArrayList(), associatedEvents);
    }

    @Test
    public void getAssociatedEvents_withAssociations_returnsCorrectEvents() {
        addressBook.addVendor(ALICE);
        addressBook.addEvent(testEvent);
        addressBook.addEvent(anotherEvent);
        addressBook.assignVendorToEvent(ALICE, testEvent);
        addressBook.assignVendorToEvent(ALICE, anotherEvent);

        ObservableList<Event> associatedEvents = addressBook.getAssociatedEvents(ALICE);
        ObservableList<Event> expectedEvents = FXCollections.observableArrayList(anotherEvent, testEvent);

        assertEquals(expectedEvents, associatedEvents);
    }

    /**
     * A stub ReadOnlyAddressBook whose vendors list can violate interface
     * constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Vendor> vendors = FXCollections.observableArrayList();
        private final ObservableList<Event> events = FXCollections.observableArrayList();

        AddressBookStub(Collection<Vendor> vendors) {
            this.vendors.setAll(vendors);
        }

        @Override
        public ObservableList<Vendor> getVendorList() {
            return vendors;
        }

        @Override
        public ObservableList<Event> getEventList() {
            return events;
        }

        @Override
        public ObservableList<Association> getAssociationList() {
            throw new AssertionError("should not be called");
        }
    }

}
