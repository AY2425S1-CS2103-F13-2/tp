package seedu.address.model.vendor;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.id.UniqueId;
import seedu.address.model.vendor.exceptions.DuplicateVendorException;
import seedu.address.model.vendor.exceptions.VendorNotFoundException;

/**
 * A list of vendors that enforces uniqueness between its elements and does not allow nulls.
 * A vendor is considered unique by comparing using {@code Vendor#isSameVendor(Vendor)}. As such, adding and updating of
 * vendors uses Vendor#isSameVendor(Vendor) for equality so as to ensure that the vendor being added or updated is
 * unique in terms of identity in the UniqueVendorList. However, the removal of a vendor uses Vendor#equals(Object) so
 * as to ensure that the vendor with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Vendor#isSameVendor(Vendor)
 */
public class UniqueVendorList implements Iterable<Vendor> {

    private final ObservableList<Vendor> internalList = FXCollections.observableArrayList();
    private final ObservableList<Vendor> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);
    private final Map<UniqueId, Vendor> vendorMap = new HashMap<>();

    /**
     * Returns true if the list contains an equivalent vendor as the given argument.
     */
    public boolean contains(Vendor toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameVendor);
    }

    /**
     * Adds a vendor to the list.
     * The vendor must not already exist in the list.
     */
    public void add(Vendor toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateVendorException();
        }
        vendorMap.put(toAdd.getId(), toAdd); // Use the Vendor's existing UniqueId
        internalList.add(toAdd);
    }

    /**
     * Replaces the vendor {@code target} in the list with {@code editedVendor}.
     * {@code target} must exist in the list.
     * The vendor identity of {@code editedVendor} must not be the same as another existing vendor in the list.
     */
    public void setVendor(Vendor target, Vendor editedVendor) {
        requireAllNonNull(target, editedVendor);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new VendorNotFoundException();
        }

        if (!target.isSameVendor(editedVendor) && contains(editedVendor)) {
            throw new DuplicateVendorException();
        }

        UniqueId vendorId = target.getId();
        vendorMap.put(vendorId, editedVendor);
        internalList.set(index, editedVendor);
    }

    /**
     * Removes the equivalent vendor from the list.
     * The vendor must exist in the list.
     */
    public void remove(Vendor toRemove) {
        requireNonNull(toRemove);
        UniqueId vendorId = toRemove.getId();
        if (!internalList.remove(toRemove)) {
            throw new VendorNotFoundException();
        }
        vendorMap.remove(vendorId);
    }

    /**
     * Replaces the contents of this list with the vendors from {@code replacement}.
     * The replacement {@code UniqueVendorList} must not contain duplicate vendors.
     */
    public void setVendors(UniqueVendorList replacement) {
        requireNonNull(replacement);

        List<Vendor> replacementVendors = replacement.internalList;
        if (!vendorsAreUnique(replacementVendors)) {
            throw new DuplicateVendorException();
        }

        vendorMap.clear();
        internalList.clear();

        for (Vendor vendor : replacementVendors) {
            add(vendor);
        }
    }

    /**
     * Replaces the contents of this list with {@code vendors}.
     * {@code vendors} must not contain duplicate vendors.
     */
    public void setVendors(List<Vendor> vendors) {
        requireAllNonNull(vendors);
        if (!vendorsAreUnique(vendors)) {
            throw new DuplicateVendorException();
        }

        vendorMap.clear();
        internalList.clear();

        for (Vendor vendor : vendors) {
            add(vendor);
        }
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Vendor> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    /**
     * Returns the Vendor corresponding to the given {@code UniqueId}.
     * If no such vendor exists, returns null.
     */
    public Vendor getVendorById(UniqueId vendorId) {
        requireNonNull(vendorId);
        return vendorMap.get(vendorId); // Retrieve the event from the map using the UniqueId
    }

    @Override
    public Iterator<Vendor> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueVendorList)) {
            return false;
        }

        UniqueVendorList otherUniqueVendorList = (UniqueVendorList) other;
        return internalList.equals(otherUniqueVendorList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code vendors} contains only unique vendors.
     */
    private boolean vendorsAreUnique(List<Vendor> vendors) {
        for (int i = 0; i < vendors.size() - 1; i++) {
            for (int j = i + 1; j < vendors.size(); j++) {
                if (vendors.get(i).isSameVendor(vendors.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
