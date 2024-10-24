package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.commons.name.Name;
import seedu.address.model.commons.tag.Tag;
import seedu.address.model.id.UniqueId;
import seedu.address.model.vendor.Description;
import seedu.address.model.vendor.Phone;
import seedu.address.model.vendor.Vendor;

/**
 * Jackson-friendly version of {@link Vendor}.
 */
class JsonAdaptedVendor {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Vendor's %s field is missing!";

    private final String id;
    private final String name;
    private final String phone;
    private final String description;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedVendor} with the given vendor details.
     */
    @JsonCreator
    public JsonAdaptedVendor(@JsonProperty("id") String id, @JsonProperty("name") String name,
            @JsonProperty("phone") String phone, @JsonProperty("description") String description,
            @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.description = description;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code Vendor} into this class for Jackson use.
     */
    public JsonAdaptedVendor(Vendor source) {
        id = source.getId().toString();
        name = source.getName().fullName;
        phone = source.getPhone().value;
        description = source.getDescription().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted vendor object into the model's
     * {@code Vendor} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in
     *                               the adapted vendor.
     */
    public Vendor toModelType() throws IllegalValueException {
        final List<Tag> vendorTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            vendorTags.add(tag.toModelType());
        }

        if (id == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    UniqueId.class.getSimpleName()));
        }
        final UniqueId modelId = new UniqueId(id);

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (description == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName()));
        }
        if (!Description.isValidDescription(description)) {
            throw new IllegalValueException(Description.MESSAGE_CONSTRAINTS);
        }
        final Description modelDescription = new Description(description);

        final Set<Tag> modelTags = new HashSet<>(vendorTags);
        return new Vendor(modelId, modelName, modelPhone, modelDescription, modelTags);
    }

}
