package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.association.Association;
import seedu.address.model.event.Event;
import seedu.address.model.id.UniqueId;
import seedu.address.model.vendor.Vendor;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_VENDOR = "Vendors list contains duplicate vendor(s).";
    public static final String MESSAGE_DUPLICATE_EVENT = "Event list contains duplicate event(s).";

    private final List<JsonAdaptedVendor> vendors = new ArrayList<>();
    private final List<JsonAdaptedEvent> events = new ArrayList<>();
    private final List<JsonAdaptedAssociation> associations = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given vendors.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("vendors") List<JsonAdaptedVendor> vendors,
                                       @JsonProperty("events") List<JsonAdaptedEvent> events,
                                       @JsonProperty("associations") List<JsonAdaptedAssociation> associations) {
        this.vendors.addAll(vendors);
        this.events.addAll(events);
        this.associations.addAll(associations);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        vendors.addAll(source.getVendorList().stream().map(JsonAdaptedVendor::new).toList());
        events.addAll(source.getEventList().stream().map(JsonAdaptedEvent::new).toList());
        associations.addAll(source.getAssociationList().stream()
                .map(JsonAdaptedAssociation::new).toList());
    }

    /**
    * Converts this address book into the model's {@code AddressBook} object.
    *
    * @throws IllegalValueException if there were any data constraints violated.
    */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();

        // Convert and add vendors
        for (JsonAdaptedVendor jsonAdaptedVendor : vendors) {
            Vendor vendor = jsonAdaptedVendor.toModelType();
            if (addressBook.hasVendor(vendor)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_VENDOR);
            }
            addressBook.addVendor(vendor);
        }

        // Convert and add events
        for (JsonAdaptedEvent jsonAdaptedEvent : events) {
            Event event = jsonAdaptedEvent.toModelType();
            if (addressBook.hasEvent(event)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_EVENT);
            }
            addressBook.addEvent(event);
        }

        // Convert and add associations
        for (JsonAdaptedAssociation jsonAdaptedAssociation : associations) {
            Association association = jsonAdaptedAssociation.toModelType();

            UniqueId vendorId = association.getVendorId();
            UniqueId eventId = association.getEventId();

            Vendor vendor = addressBook.getVendorById(vendorId);
            Event event = addressBook.getEventById(eventId);

            // Check if the vendor and event exist in the address book
            if (vendor == null || event == null) {
                throw new IllegalValueException("Association contains non-existent vendor or event.");
            }

            if (addressBook.isVendorAssignedToEvent(vendor, event)) {
                throw new IllegalValueException("Duplicate association found in JSON.");
            }

            addressBook.assignVendorToEvent(vendor, event);
        }

        return addressBook;
    }

}
