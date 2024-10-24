package seedu.address.model.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import seedu.address.model.id.UniqueId;

public class AssociationTest {

    private final UniqueId vendorId = new UniqueId(UUID.randomUUID().toString());
    private final UniqueId eventId = new UniqueId(UUID.randomUUID().toString());
    private final UniqueId differentVendorId = new UniqueId(UUID.randomUUID().toString());
    private final UniqueId differentEventId = new UniqueId(UUID.randomUUID().toString());

    private final Association association = new Association(vendorId, eventId);
    private final Association sameAssociation = new Association(vendorId, eventId);
    private final Association differentAssociation = new Association(differentVendorId, differentEventId);

    @Test
    public void constructor_nullValues_throwsNullPointerException() {
        // Test with vendorId null
        assertThrows(NullPointerException.class, () -> new Association(null, eventId));

        // Test with eventId null
        assertThrows(NullPointerException.class, () -> new Association(vendorId, null));

        // Test with both null
        assertThrows(NullPointerException.class, () -> new Association(null, null));
    }

    @Test
    public void getVendorId_validVendorId_success() {
        assertEquals(vendorId, association.getVendorId());
    }

    @Test
    public void getEventId_validEventId_success() {
        assertEquals(eventId, association.getEventId());
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        // Check that an association equals itself
        assertTrue(association.equals(association));
    }

    @Test
    public void equals_differentObjectSameValues_returnsTrue() {
        // Two different Association objects with the same vendorId and eventId should be equal
        assertTrue(association.equals(sameAssociation));
    }

    @Test
    public void equals_differentVendorId_returnsFalse() {
        // An association with different vendorId should not be equal
        Association diffVendorAssociation = new Association(differentVendorId, eventId);
        assertFalse(association.equals(diffVendorAssociation));
    }

    @Test
    public void equals_differentEventId_returnsFalse() {
        // An association with different eventId should not be equal
        Association diffEventAssociation = new Association(vendorId, differentEventId);
        assertFalse(association.equals(diffEventAssociation));
    }

    @Test
    public void equals_differentObject_returnsFalse() {
        // Check that two associations with different vendorId and eventId are not equal
        assertFalse(association.equals(differentAssociation));
    }

    @Test
    public void hashCode_consistentWithEquals() {
        // If two associations are equal, their hashCode should also be the same
        assertTrue(association.equals(sameAssociation));
        assertEquals(association.hashCode(), sameAssociation.hashCode());
    }

    @Test
    public void toString_checkFormat() {
        // Check the string representation of an association
        String expectedString = String.format("Association[vendorId=%s, eventId=%s]", vendorId, eventId);
        assertEquals(expectedString, association.toString());
    }
}
