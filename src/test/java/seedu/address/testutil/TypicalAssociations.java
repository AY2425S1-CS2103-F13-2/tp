package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.association.Association;
import seedu.address.model.event.Event;
import seedu.address.model.id.UniqueId;
import seedu.address.model.vendor.Vendor;

/**
 * A utility class containing a list of {@code Association} objects to be used in tests.
 */
public class TypicalAssociations {

    // You can define some typical UniqueIds here for vendors and events
    public static final UniqueId VENDOR_ID_AMY = new UniqueId("a88a7dc2-f439-11ec-b939-0242ac120002");
    public static final UniqueId VENDOR_ID_BOB = new UniqueId("b77b7dc3-f439-11ec-b939-0242ac120002");
    public static final UniqueId EVENT_ID_WEDDING = new UniqueId("c66c7dc4-f439-11ec-b939-0242ac120002");
    public static final UniqueId EVENT_ID_BIRTHDAY = new UniqueId("d55d7dc5-f439-11ec-b939-0242ac120002");

    // Create typical Associations using these IDs
    public static final Association AMY_WEDDING = new Association(VENDOR_ID_AMY, EVENT_ID_WEDDING);
    public static final Association BOB_BIRTHDAY = new Association(VENDOR_ID_BOB, EVENT_ID_BIRTHDAY);
    public static final Association AMY_BIRTHDAY = new Association(VENDOR_ID_AMY, EVENT_ID_BIRTHDAY);

    private TypicalAssociations() {} // Prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical associations.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Association association : getTypicalAssociations()) {
            ab.assignVendorToEvent(getTypicalVendorById(association.getVendorId()),
                                   getTypicalEventById(association.getEventId()));
        }
        return ab;
    }

    public static List<Association> getTypicalAssociations() {
        return new ArrayList<>(Arrays.asList(AMY_WEDDING, BOB_BIRTHDAY, AMY_BIRTHDAY));
    }

    // Helper methods to simulate getting vendors and events
    private static Vendor getTypicalVendorById(UniqueId vendorId) {
        if (vendorId.equals(VENDOR_ID_AMY)) {
            return TypicalVendors.AMY;
        } else if (vendorId.equals(VENDOR_ID_BOB)) {
            return TypicalVendors.BOB;
        }
        return null; // Add more mappings as needed
    }

    private static Event getTypicalEventById(UniqueId eventId) {
        if (eventId.equals(EVENT_ID_WEDDING)) {
            return TypicalEvents.WEDDING;
        } else if (eventId.equals(EVENT_ID_BIRTHDAY)) {
            return TypicalEvents.BIRTHDAY;
        }
        return null; // Add more mappings as needed
    }
}
