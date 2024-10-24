package seedu.address.model.association;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.association.exceptions.AssociationNotFoundException;
import seedu.address.model.association.exceptions.DuplicateAssociationException;

/**
 * A list of associations that enforces uniqueness between its elements (Association objects)
 * and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 */
public class UniqueAssociationList implements Iterable<Association> {

    private final ObservableList<Association> internalList = FXCollections.observableArrayList();
    private final ObservableList<Association> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent association to the given argument.
     */
    public boolean contains(Association toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds an association to the list.
     * The association must not already exist in the list.
     */
    public void add(Association toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateAssociationException();
        }
        internalList.add(toAdd);
    }

    /**
     * Removes the equivalent association from the list.
     * The association must exist in the list.
     */
    public void remove(Association toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new AssociationNotFoundException();
        }
    }

    /**
     * Replaces the contents of this list with {@code associations}.
     * {@code associations} must not contain duplicate associations.
     */
    public void setAssociations(List<Association> associations) {
        requireAllNonNull(associations);
        if (!associationsAreUnique(associations)) {
            throw new DuplicateAssociationException();
        }

        internalList.setAll(associations);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Association> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Association> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UniqueAssociationList)) {
            return false;
        }

        UniqueAssociationList otherList = (UniqueAssociationList) other;
        return internalList.equals(otherList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code associations} contains only unique associations.
     */
    private boolean associationsAreUnique(List<Association> associations) {
        Set<Association> seen = new HashSet<>();
        for (Association association : associations) {
            if (!seen.add(association)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return internalList.toString();
    }
}

