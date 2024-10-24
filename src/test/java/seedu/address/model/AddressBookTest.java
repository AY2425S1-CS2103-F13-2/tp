package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_BIRTHDAY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalEvents.BIRTHDAY;
import static seedu.address.testutil.TypicalEvents.WEDDING;
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
import seedu.address.model.commons.exceptions.AssociationDeleteException;
import seedu.address.model.event.Event;
import seedu.address.model.event.exceptions.DuplicateEventException;
import seedu.address.model.vendor.Vendor;
import seedu.address.model.vendor.exceptions.DuplicateVendorException;
import seedu.address.testutil.EventBuilder;
import seedu.address.testutil.VendorBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();
    private final Event testEvent = new Event(new Name("Test Event"), new Date("2024-10-11"));
    private final Event anotherEvent = new Event(new Name("Another Event"), new Date("2024-10-11"));

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
        assertFalse(addressBook.hasEvent(WEDDING));
    }

    @Test
    public void hasEvent_eventInAddressBook_returnsTrue() {
        addressBook.addEvent(WEDDING);
        assertTrue(addressBook.hasEvent(WEDDING));
    }

    @Test
    public void hasEvent_eventWithSameIdentityFieldsInAddressBook_returnsTrue() {
        Event similarWedding = new EventBuilder(WEDDING).withDate(VALID_DATE_BIRTHDAY).build();
        addressBook.addEvent(WEDDING);
        assertTrue(addressBook.hasEvent(similarWedding));
    }

    // might be redundant has this is already tested in UniqueEventListTest.java
    @Test
    public void addEvent_nullEvent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.addEvent(null));
    }

    // might be redundant has this is already tested in UniqueEventListTest.java
    @Test
    public void addEvent_eventInAddressBook_throwsDuplicateEventException() {
        addressBook.addEvent(WEDDING);
        assertThrows(DuplicateEventException.class, () -> addressBook.addEvent(WEDDING));
    }

    // might be redundant has this is already tested in UniqueEventListTest.java
    @Test
    public void addEvent_eventWithSameIdentityFieldsInAddressBook_throwsDuplicateEventException() {
        Event similarWedding = new EventBuilder(WEDDING).withDate(VALID_DATE_BIRTHDAY).build();
        addressBook.addEvent(WEDDING);
        assertThrows(DuplicateEventException.class, () -> addressBook.addEvent(similarWedding));
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
                + "events=" + addressBook.getEventList() + ", "
                + "associations=" + addressBook.getAssociationList()
                + "}";
        assertEquals(expected, addressBook.toString());
    }

    @Test
    public void removeVendor_vendorNotAssociated_success() {
        addressBook.addVendor(ALICE);
        addressBook.removeVendor(ALICE);
        assertFalse(addressBook.hasVendor(ALICE));
    }

    @Test
    public void removeVendor_vendorIsAssociatedWithEvent_throwsAssociationDeleteException() {
        addressBook.addVendor(ALICE);
        addressBook.addEvent(WEDDING);
        addressBook.assignVendorToEvent(ALICE, WEDDING);

        assertThrows(AssociationDeleteException.class, () -> addressBook.removeVendor(ALICE));
    }

    @Test
    public void removeEvent_eventNotAssociated_success() {
        addressBook.addEvent(WEDDING);
        addressBook.removeEvent(WEDDING);
        assertFalse(addressBook.hasEvent(WEDDING));
    }

    @Test
    public void removeEvent_eventIsAssociatedWithVendor_throwsAssociationDeleteException() {
        addressBook.addVendor(ALICE);
        addressBook.addEvent(WEDDING);
        addressBook.assignVendorToEvent(ALICE, WEDDING);

        assertThrows(AssociationDeleteException.class, () -> addressBook.removeEvent(WEDDING));
    }

    @Test
    public void getAssociatedVendors_noAssociations_returnsEmptyList() {
        ObservableList<Vendor> associatedVendors = addressBook.getAssociatedVendors(WEDDING);
        assertEquals(FXCollections.observableArrayList(), associatedVendors);
    }

    @Test
    public void getAssociatedVendors_withAssociations_returnsCorrectVendors() {
        addressBook.addVendor(ALICE);
        addressBook.addVendor(BOB);
        addressBook.addEvent(WEDDING);
        addressBook.assignVendorToEvent(ALICE, WEDDING);
        addressBook.assignVendorToEvent(BOB, WEDDING);

        ObservableList<Vendor> associatedVendors = addressBook.getAssociatedVendors(WEDDING);
        ObservableList<Vendor> expectedVendors = FXCollections.observableArrayList(ALICE, BOB);

        associatedVendors.sort((v1, v2) -> v1.getName().fullName.compareTo(v2.getName().fullName));
        expectedVendors.sort((v1, v2) -> v1.getName().fullName.compareTo(v2.getName().fullName));

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
        addressBook.addEvent(WEDDING);
        addressBook.addEvent(BIRTHDAY);
        addressBook.assignVendorToEvent(ALICE, WEDDING);
        addressBook.assignVendorToEvent(ALICE, BIRTHDAY);

        ObservableList<Event> associatedEvents = addressBook.getAssociatedEvents(ALICE);
        ObservableList<Event> expectedEvents = FXCollections.observableArrayList(WEDDING, BIRTHDAY);

        associatedEvents.sort((e1, e2) -> e1.getName().fullName.compareTo(e2.getName().fullName));
        expectedEvents.sort((e1, e2) -> e1.getName().fullName.compareTo(e2.getName().fullName));

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
