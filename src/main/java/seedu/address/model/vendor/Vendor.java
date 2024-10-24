package seedu.address.model.vendor;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.commons.name.Name;
import seedu.address.model.commons.tag.Tag;
import seedu.address.model.id.UniqueId;

/**
 * Represents a Vendor in the description book.
 * Guarantees: details are present and not null, field values are validated,
 * immutable.
 */
public class Vendor {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final UniqueId id;

    // Data fields
    private final Description description;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Vendor(UniqueId id, Name name, Phone phone, Description description, Set<Tag> tags) {
        requireAllNonNull(name, phone, description, tags);
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.tags.addAll(tags);
    }

    /**
     * Every field must be present and not null.
     * Overloaded constructor for Vendor without Description.
     */
    public Vendor(Name name, Phone phone, Set<Tag> tags) {
        requireAllNonNull(name, phone, tags);
        this.id = new UniqueId();
        this.name = name;
        this.phone = phone;
        this.description = new Description("-");
        this.tags.addAll(tags);
    }

    /**
     * Overloaded constructor for Vendor where the ID is auto-generated.
     * Every field must be present and not null, except for the ID, which is generated automatically.
     */
    public Vendor(Name name, Phone phone, Description description, Set<Tag> tags) {
        requireAllNonNull(name, phone, description, tags);
        this.id = new UniqueId(); // Auto-generate the UniqueId
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.tags.addAll(tags);
    }

    public UniqueId getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Description getDescription() {
        return description;
    }

    /**
     * Returns an immutable tag set, which throws
     * {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both vendors have the same name.
     * This defines a weaker notion of equality between two vendors.
     */
    public boolean isSameVendor(Vendor otherVendor) {
        if (otherVendor == this) {
            return true;
        }

        return otherVendor != null
                && otherVendor.getName().equals(getName());
    }

    /**
     * Returns true if both vendors have the same identity and data fields.
     * This defines a stronger notion of equality between two vendors.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Vendor)) {
            return false;
        }

        Vendor otherVendor = (Vendor) other;
        return name.equals(otherVendor.name)
                && phone.equals(otherVendor.phone)
                && description.equals(otherVendor.description)
                && tags.equals(otherVendor.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(id, name, phone, description, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("id", id)
                .add("name", name)
                .add("phone", phone)
                .add("description", description)
                .add("tags", tags)
                .toString();
    }
}
