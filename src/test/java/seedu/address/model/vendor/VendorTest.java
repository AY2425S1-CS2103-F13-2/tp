package seedu.address.model.vendor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalVendors.ALICE;
import static seedu.address.testutil.TypicalVendors.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.VendorBuilder;

public class VendorTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Vendor vendor = new VendorBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> vendor.getTags().remove(0));
    }

    @Test
    public void isSameVendor() {
        // same object -> returns true
        assertTrue(ALICE.isSameVendor(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameVendor(null));

        // same name, all other attributes different -> returns true
        Vendor editedAlice = new VendorBuilder(ALICE).withPhone(VALID_PHONE_BOB)
                .withDescription(VALID_DESCRIPTION_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameVendor(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new VendorBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameVendor(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Vendor editedBob = new VendorBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSameVendor(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new VendorBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSameVendor(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Vendor aliceCopy = new VendorBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different vendor -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Vendor editedAlice = new VendorBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new VendorBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different description -> returns false
        editedAlice = new VendorBuilder(ALICE).withDescription(VALID_DESCRIPTION_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new VendorBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Vendor.class.getCanonicalName() + "{id=" + ALICE.getId() + ", name=" + ALICE.getName()
                + ", phone=" + ALICE.getPhone() + ", description=" + ALICE.getDescription()
                + ", tags=" + ALICE.getTags() + "}";

        assertEquals(expected, ALICE.toString());
    }
}
