package seedu.address.model.id;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a unique identifier for entities like Vendor or Event.
 * Guarantees: immutable; the ID is a valid UUID.
 */
public class UniqueId {

    private final UUID id;

    /**
     * Constructs a {@code UniqueId} with a generated UUID.
     */
    public UniqueId() {
        this.id = UUID.randomUUID(); // Generate a random UUID
    }

    /**
     * Constructs a {@code UniqueId} from an existing UUID string.
     * This can be useful when loading from a persistent store.
     */
    public UniqueId(String uuidString) {
        this.id = UUID.fromString(uuidString); // Parse the UUID from a string
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UniqueId)) {
            return false;
        }
        UniqueId otherId = (UniqueId) other;
        return id.equals(otherId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}

