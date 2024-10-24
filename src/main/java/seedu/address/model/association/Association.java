package seedu.address.model.association;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.model.id.UniqueId;

/**
 * Represents an association between a Vendor and an Event.
 * Guarantees: immutable; both UniqueIds are valid.
 */
public class Association {

    private final UniqueId vendorId;
    private final UniqueId eventId;

    /**
     * Constructs an {@code Association} between the given vendor and event IDs.
     */
    public Association(UniqueId vendorId, UniqueId eventId) {
        requireNonNull(vendorId);
        requireNonNull(eventId);
        this.vendorId = vendorId;
        this.eventId = eventId;
    }

    public UniqueId getVendorId() {
        return vendorId;
    }

    public UniqueId getEventId() {
        return eventId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Association)) {
            return false;
        }

        Association otherAssoc = (Association) other;
        return vendorId.equals(otherAssoc.vendorId) && eventId.equals(otherAssoc.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendorId, eventId);
    }

    @Override
    public String toString() {
        return String.format("Association[vendorId=%s, eventId=%s]", vendorId, eventId);
    }
}

