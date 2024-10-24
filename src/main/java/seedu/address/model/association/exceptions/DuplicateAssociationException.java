package seedu.address.model.association.exceptions;

/**
 * Signals that the operation will result in duplicate Associations (Associations are considered duplicates if they have
 * the same Vendor and Event pair).
 */
public class DuplicateAssociationException extends RuntimeException {
    public DuplicateAssociationException() {
        super("Operation would result in duplicate associations");
    }
}

