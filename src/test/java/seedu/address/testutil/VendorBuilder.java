package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.commons.name.Name;
import seedu.address.model.commons.tag.Tag;
import seedu.address.model.id.UniqueId;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.model.vendor.Description;
import seedu.address.model.vendor.Phone;
import seedu.address.model.vendor.Vendor;

/**
 * A utility class to help with building Vendor objects.
 */
public class VendorBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_DESCRIPTION = "123, Jurong West Ave 6, #08-111";

    private UniqueId id;
    private Name name;
    private Phone phone;
    private Description description;
    private Set<Tag> tags;

    /**
     * Creates a {@code VendorBuilder} with the default details.
     */
    public VendorBuilder() {
        this.id = new UniqueId();
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        description = new Description(DEFAULT_DESCRIPTION);
        tags = new HashSet<>();
    }

    /**
     * Initializes the VendorBuilder with the data of {@code vendorToCopy}, including the ID.
     */
    public VendorBuilder(Vendor vendorToCopy) {
        id = vendorToCopy.getId();
        name = vendorToCopy.getName();
        phone = vendorToCopy.getPhone();
        description = vendorToCopy.getDescription();
        tags = new HashSet<>(vendorToCopy.getTags());
    }

    /**
     * Sets the {@code UniqueId} of the {@code Vendor} that we are building.
     */
    public VendorBuilder withId(UniqueId id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Vendor} that we are building.
     */
    public VendorBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and sets it to the
     * {@code Vendor} that we are building.
     */
    public VendorBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code Vendor} that we are building.
     */
    public VendorBuilder withDescription(String description) {
        this.description = new Description(description);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Vendor} that we are building.
     */
    public VendorBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Builds and returns the {@code Vendor}.
     * If the ID was not explicitly set, it will be auto-generated.
     */
    public Vendor build() {
        return new Vendor(id, name, phone, description, tags);
    }
}
