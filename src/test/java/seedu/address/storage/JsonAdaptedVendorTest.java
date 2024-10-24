package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedVendor.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalVendors.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.commons.name.Name;
import seedu.address.model.id.UniqueId;
import seedu.address.model.vendor.Description;
import seedu.address.model.vendor.Phone;

public class JsonAdaptedVendorTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_DESCRIPTION = " ";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_ID = BENSON.getId().toString();
    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_DESCRIPTION = BENSON.getDescription().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validVendorDetails_returnsVendor() throws Exception {
        JsonAdaptedVendor vendor = new JsonAdaptedVendor(BENSON);
        assertEquals(BENSON, vendor.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedVendor vendor = new JsonAdaptedVendor(VALID_ID, INVALID_NAME, VALID_PHONE, VALID_DESCRIPTION,
                VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, vendor::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedVendor vendor = new JsonAdaptedVendor(VALID_ID, null, VALID_PHONE, VALID_DESCRIPTION, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, vendor::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedVendor vendor = new JsonAdaptedVendor(VALID_ID, VALID_NAME, INVALID_PHONE, VALID_DESCRIPTION,
                VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, vendor::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedVendor vendor = new JsonAdaptedVendor(VALID_ID, VALID_NAME, null, VALID_DESCRIPTION, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, vendor::toModelType);
    }

    @Test
    public void toModelType_invalidDescription_throwsIllegalValueException() {
        JsonAdaptedVendor vendor = new JsonAdaptedVendor(VALID_ID, VALID_NAME, VALID_PHONE, INVALID_DESCRIPTION,
                VALID_TAGS);
        String expectedMessage = Description.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, vendor::toModelType);
    }

    @Test
    public void toModelType_nullDescription_throwsIllegalValueException() {
        JsonAdaptedVendor vendor = new JsonAdaptedVendor(VALID_ID, VALID_NAME, VALID_PHONE, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, vendor::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedVendor vendor = new JsonAdaptedVendor(VALID_ID, VALID_NAME, VALID_PHONE, VALID_DESCRIPTION,
                invalidTags);
        assertThrows(IllegalValueException.class, vendor::toModelType);
    }

    @Test
    public void toModelType_nullId_throwsIllegalValueException() {
        JsonAdaptedVendor vendor = new JsonAdaptedVendor(null, VALID_NAME, VALID_PHONE, VALID_DESCRIPTION, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, UniqueId.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, vendor::toModelType);
    }
}
