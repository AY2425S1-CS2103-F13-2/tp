package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalAssociations.AMY_WEDDING;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;

public class JsonAdaptedAssociationTest {

    private static final String INVALID_ID = "invalid-uuid";

    private static final String VALID_VENDOR_ID_AMY = AMY_WEDDING.getVendorId().toString();
    private static final String VALID_EVENT_ID_WEDDING = AMY_WEDDING.getEventId().toString();

    @Test
    public void toModelType_validAssociationDetails_returnsAssociation() throws Exception {
        JsonAdaptedAssociation association = new JsonAdaptedAssociation(AMY_WEDDING);
        assertEquals(AMY_WEDDING, association.toModelType());
    }

    @Test
    public void toModelType_invalidVendorId_throwsIllegalValueException() {
        JsonAdaptedAssociation association = new JsonAdaptedAssociation(INVALID_ID, VALID_EVENT_ID_WEDDING);
        String expectedMessage = "Invalid UUID format in association.";
        assertThrows(IllegalValueException.class, expectedMessage, association::toModelType);
    }

    @Test
    public void toModelType_invalidEventId_throwsIllegalValueException() {
        JsonAdaptedAssociation association = new JsonAdaptedAssociation(VALID_VENDOR_ID_AMY, INVALID_ID);
        String expectedMessage = "Invalid UUID format in association.";
        assertThrows(IllegalValueException.class, expectedMessage, association::toModelType);
    }

    @Test
    public void toModelType_nullVendorId_throwsIllegalValueException() {
        JsonAdaptedAssociation association = new JsonAdaptedAssociation(null, VALID_EVENT_ID_WEDDING);
        String expectedMessage = String.format(JsonAdaptedAssociation.MISSING_FIELD_MESSAGE_FORMAT, "vendorId");
        assertThrows(IllegalValueException.class, expectedMessage, association::toModelType);
    }

    @Test
    public void toModelType_nullEventId_throwsIllegalValueException() {
        JsonAdaptedAssociation association = new JsonAdaptedAssociation(VALID_VENDOR_ID_AMY, null);
        String expectedMessage = String.format(JsonAdaptedAssociation.MISSING_FIELD_MESSAGE_FORMAT, "eventId");
        assertThrows(IllegalValueException.class, expectedMessage, association::toModelType);
    }
}

