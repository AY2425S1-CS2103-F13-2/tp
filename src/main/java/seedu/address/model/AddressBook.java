package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.util.Pair;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.association.Association;
import seedu.address.model.association.UniqueAssociationList;
import seedu.address.model.event.Event;
import seedu.address.model.event.UniqueEventList;
import seedu.address.model.id.UniqueId;
import seedu.address.model.vendor.UniqueVendorList;
import seedu.address.model.vendor.Vendor;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameVendor comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniqueVendorList vendors;
    private final UniqueEventList events;
    private final UniqueAssociationList associations;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        vendors = new UniqueVendorList();
        associations = new UniqueAssociationList();
        events = new UniqueEventList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Vendors in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the vendor list with {@code vendors}.
     * {@code vendors} must not contain duplicate vendors.
     */
    public void setVendors(List<Vendor> vendors) {
        this.vendors.setVendors(vendors);
    }

    /**
     * Replaces the contents of the event list with {@code events}.
     * {@code events} must not contain duplicate events.
     */
    public void setEvents(List<Event> events) {
        this.events.setEvents(events);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setVendors(newData.getVendorList());
        setEvents(newData.getEventList());
    }

    //// vendor-level operations

    /**
     * Returns true if a vendor with the same identity as {@code vendor} exists in the address book.
     */
    public boolean hasVendor(Vendor vendor) {
        requireNonNull(vendor);
        return vendors.contains(vendor);
    }

    /**
     * Adds a vendor to the address book.
     * The vendor must not already exist in the address book.
     */
    public void addVendor(Vendor p) {
        vendors.add(p);
    }

    /**
     * Replaces the given vendor {@code target} in the list with {@code editedVendor}.
     * {@code target} must exist in the address book.
     * The vendor identity of {@code editedVendor} must not be the same as another existing vendor in the address book.
     */
    public void setVendor(Vendor target, Vendor editedVendor) {
        requireNonNull(editedVendor);

        vendors.setVendor(target, editedVendor);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeVendor(Vendor key) {
        vendors.remove(key);
    }

    //// assign operations

    /**
     * Returns true if the given {@code vendor} is already assigned to the given {@code event}.
     * {@code vendor} and {@code event} must exist in the respective lists.
     */
    public boolean isVendorAssignedToEvent(Vendor vendor, Event event) {
        requireAllNonNull(vendor, event);

        // Get the UniqueIds for vendor and event
        UniqueId vendorId = vendors.getVendorId(vendor);
        UniqueId eventId = events.getEventId(event);

        // Check for the association
        Association association = new Association(vendorId, eventId);
        return associations.contains(association);
    }

    /**
     * Assigns the given {@code vendor} in the list to {@code event}.
     * {@code vendor} and {@code event} must exist in their respective lists.
     */
    public void assignVendorToEvent(Vendor vendor, Event event) {
        requireAllNonNull(vendor, event);

        // Get the UniqueIds for vendor and event
        UniqueId vendorId = vendors.getVendorId(vendor);
        UniqueId eventId = events.getEventId(event);

        // Create the association and add to associations
        Association association = new Association(vendorId, eventId);
        associations.add(association);
    }

    /**
     * Unassigns the given {@code vendor} in the list from {@code event}.
     * {@code vendor} and {@code event} must exist in the address book.
     */
    void unassignVendorFromEvent(Vendor vendor, Event event) {
        requireAllNonNull(vendor, event);
        Pair<Vendor, Event> pair = new Pair<>(vendor, event);
        associations.remove(pair);
    }

    /**
     * Returns list of associated vendors to an event.
     */
    public ObservableList<Vendor> getAssociatedVendors(Event event) {
        requireNonNull(event);
        List<Vendor> vendorsList = associations.stream()
                .filter(pair -> pair.getValue().equals(event))
                .map(Pair::getKey)
                .collect(Collectors.toList());

        return FXCollections.observableArrayList(vendorsList);
    }

    /**
     * Returns list of associated events to a vendor.
     */
    public ObservableList<Event> getAssociatedEvents(Vendor vendor) {
        requireNonNull(vendor);
        List<Event> eventsList = associations.stream()
                .filter(pair -> pair.getKey().equals(vendor))
                .map(Pair::getValue)
                .collect(Collectors.toList());

        return FXCollections.observableArrayList(eventsList);
    }

    //// event-level operations

    /**
     * Returns true if a event with the same identity as {@code event} exists in the address book.
     */
    public boolean hasEvent(Event event) {
        requireNonNull(event);
        return events.contains(event);
    }

    /**
     * Adds an event to the address book.
     * The event must not already exist in the address book.
     */
    public void addEvent(Event event) {
        events.add(event);
    }

    // TODO add method to update existing Event with new Event object

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeEvent(Event key) {
        events.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("vendors", vendors)
                .add("events", events)
                .toString();
    }

    @Override
    public ObservableList<Vendor> getVendorList() {
        return vendors.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Event> getEventList() {
        return events.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Association> getAssociationList() {
        return associations.asUnmodifiableObservableList();
    }

    public Vendor getVendorById(UniqueId vendorId) {
        for (Vendor vendor : vendors.asUnmodifiableObservableList()) {
            if (vendors.getVendorId(vendor).equals(vendorId)) {
                return vendor;
            }
        }
        return null;
    }

    public Event getEventById(UniqueId eventId) {
        for (Event event : events.asUnmodifiableObservableList()) {
            if (events.getEventId(event).equals(eventId)) {
                return event;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return vendors.equals(otherAddressBook.vendors) && events.equals(otherAddressBook.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendors.hashCode(), events.hashCode());
    }
}
