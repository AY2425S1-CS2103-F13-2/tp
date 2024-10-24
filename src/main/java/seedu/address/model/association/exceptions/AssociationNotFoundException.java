package seedu.address.model.association.exceptions;

/**
 * Signals that the specified association (Vendor, Event pair) is not found in the list.
 */
public class AssociationNotFoundException extends RuntimeException {
    public AssociationNotFoundException() {
        super("The specified association is not found");
    }
}

