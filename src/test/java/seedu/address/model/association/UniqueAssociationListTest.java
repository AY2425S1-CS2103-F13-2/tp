package seedu.address.model.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalAssociations.AMY_WEDDING;
import static seedu.address.testutil.TypicalAssociations.BOB_BIRTHDAY;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.association.exceptions.AssociationNotFoundException;
import seedu.address.model.association.exceptions.DuplicateAssociationException;

public class UniqueAssociationListTest {

    private final UniqueAssociationList uniqueAssociationList = new UniqueAssociationList();

    @Test
    public void contains_nullAssociation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueAssociationList.contains(null));
    }

    @Test
    public void contains_associationNotInList_returnsFalse() {
        assertFalse(uniqueAssociationList.contains(AMY_WEDDING));
    }

    @Test
    public void contains_associationInList_returnsTrue() {
        uniqueAssociationList.add(AMY_WEDDING);
        assertTrue(uniqueAssociationList.contains(AMY_WEDDING));
    }

    @Test
    public void add_nullAssociation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueAssociationList.add(null));
    }

    @Test
    public void add_duplicateAssociation_throwsDuplicateAssociationException() {
        uniqueAssociationList.add(AMY_WEDDING);
        assertThrows(DuplicateAssociationException.class, () -> uniqueAssociationList.add(AMY_WEDDING));
    }

    @Test
    public void remove_nullAssociation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueAssociationList.remove(null));
    }

    @Test
    public void remove_associationDoesNotExist_throwsAssociationNotFoundException() {
        assertThrows(AssociationNotFoundException.class, () -> uniqueAssociationList.remove(AMY_WEDDING));
    }

    @Test
    public void remove_existingAssociation_removesAssociation() {
        uniqueAssociationList.add(AMY_WEDDING);
        uniqueAssociationList.remove(AMY_WEDDING);
        UniqueAssociationList expectedUniqueAssociationList = new UniqueAssociationList();
        assertEquals(expectedUniqueAssociationList, uniqueAssociationList);
    }

    @Test
    public void setAssociations_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueAssociationList.setAssociations((List<Association>) null));
    }

    @Test
    public void setAssociations_list_replacesOwnListWithProvidedList() {
        uniqueAssociationList.add(AMY_WEDDING);
        List<Association> associationList = Collections.singletonList(BOB_BIRTHDAY);
        uniqueAssociationList.setAssociations(associationList);
        UniqueAssociationList expectedUniqueAssociationList = new UniqueAssociationList();
        expectedUniqueAssociationList.add(BOB_BIRTHDAY);
        assertEquals(expectedUniqueAssociationList, uniqueAssociationList);
    }

    @Test
    public void setAssociations_listWithDuplicateAssociations_throwsDuplicateAssociationException() {
        List<Association> listWithDuplicateAssociations = Arrays.asList(AMY_WEDDING, AMY_WEDDING);
        assertThrows(DuplicateAssociationException.class, () -> uniqueAssociationList.setAssociations(
                    listWithDuplicateAssociations));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueAssociationList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueAssociationList.asUnmodifiableObservableList().toString(), uniqueAssociationList.toString());
    }
}
