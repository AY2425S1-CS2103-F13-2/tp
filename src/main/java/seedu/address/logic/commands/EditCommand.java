package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VENDOR;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Parent abstract class for edit commands.
 * Contains the index of the target to be edited.
 */
public abstract class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the vendor or event identified "
            + "by the index number used in the displayed list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: " + PREFIX_VENDOR + "INDEX" + " <other vendor parameters> or "
            + PREFIX_EVENT + "INDEX (INDEX must be a positive integer)" + " <other event parameters>" + "\n"
            + "Example to edit a vendor: " + COMMAND_WORD + " " + PREFIX_VENDOR + "1 "
            + PREFIX_NAME + "Adam's Bakery "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_DESCRIPTION + "Pastries and cakes, bake in a day "
            + PREFIX_TAG + "pastry "
            + PREFIX_TAG + "fast" + "\n"
            + "Example to edit an event: " + COMMAND_WORD + " " + PREFIX_EVENT + "1 "
            + PREFIX_NAME + "John Baby Shower" + " "
            + PREFIX_DATE + "2021-10-10";

    public static final String MESSAGE_NOT_EDITED = "At least one valid field to edit must be provided.";

    protected final Index index;

    public EditCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.index = targetIndex;
    }

    /**
     * @param targetIndex in the filtered list to edit
     */
    public EditCommand(Index index, EditVendorDescriptor editVendorDescriptor) {
        requireNonNull(index);
        requireNonNull(editVendorDescriptor);

        this.index = index;
        this.editVendorDescriptor = new EditVendorDescriptor(editVendorDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Vendor> lastShownList = model.getFilteredVendorList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_VENDOR_DISPLAYED_INDEX);
        }

        Vendor vendorToEdit = lastShownList.get(index.getZeroBased());
        Vendor editedVendor = createEditedVendor(vendorToEdit, editVendorDescriptor);

        if (!vendorToEdit.isSameVendor(editedVendor) && model.hasVendor(editedVendor)) {
            throw new CommandException(MESSAGE_DUPLICATE_VENDOR);
        }

        model.setVendor(vendorToEdit, editedVendor);
        model.updateFilteredVendorList(PREDICATE_SHOW_ALL_VENDORS);
        return new CommandResult(String.format(MESSAGE_EDIT_VENDOR_SUCCESS, Messages.format(editedVendor)));
    }

    /**
     * Creates and returns a {@code Vendor} with the details of {@code vendorToEdit}
     * edited with {@code editVendorDescriptor}.
     */
    private static Vendor createEditedVendor(Vendor vendorToEdit, EditVendorDescriptor editVendorDescriptor) {
        assert vendorToEdit != null;

        Name updatedName = editVendorDescriptor.getName().orElse(vendorToEdit.getName());
        Phone updatedPhone = editVendorDescriptor.getPhone().orElse(vendorToEdit.getPhone());
        Description updatedDescription = editVendorDescriptor.getDescription().orElse(vendorToEdit.getDescription());
        Set<Tag> updatedTags = editVendorDescriptor.getTags().orElse(vendorToEdit.getTags());

        return new Vendor(vendorToEdit.getId(), updatedName, updatedPhone, updatedDescription, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editVendorDescriptor.equals(otherEditCommand.editVendorDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", index)
                .toString();
    }
}
