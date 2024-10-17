package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.association.Association;
import seedu.address.model.id.UniqueId;

/**
 * Jackson-friendly version of {@code Association}.
 */
public class JsonAdaptedAssociation {

    private final String vendorId;
    private final String eventId;

    /**
     * Constructs a {@code JsonAdaptedAssociation} with the given vendor and event IDs.
     */
    @JsonCreator
    public JsonAdaptedAssociation(@JsonProperty("vendorId") String vendorId,
                                  @JsonProperty("eventId") String eventId) {
        this.vendorId = vendorId;
        this.eventId = eventId;
    }

    /**
     * Converts a given {@code Association} into this class for Jackson use.
     */
    public JsonAdaptedAssociation(Association source) {
        this.vendorId = source.getVendorId().getId();
        this.eventId = source.getEventId().getId();
    }

    /**
     * Converts this Jackson-friendly adapted association object into the model's {@code Association} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public Association toModelType() throws IllegalValueException {
        // Convert the vendorId and eventId strings back into UniqueId objects
        UniqueId modelVendorId;
        UniqueId modelEventId;

        try {
            modelVendorId = new UniqueId(vendorId);
            modelEventId = new UniqueId(eventId);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException("Invalid UUID format in association.");
        }

        // Return the new Association object using the UniqueIds
        return new Association(modelVendorId, modelEventId);
    }
}

