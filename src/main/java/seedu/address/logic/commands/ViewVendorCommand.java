package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VENDOR;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.vendor.Vendor;
import seedu.address.ui.UiState;

/**
 * Display details of a vendor in the eventTory to the user.
 */
public class ViewVendorCommand extends ViewCommand {

    public static final String MESSAGE_SUCCESS = "Viewing %s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Views the vendor identified by the index number used in the displayed event list.\n" + "Parameters: "
            + PREFIX_VENDOR + "INDEX (must be a positive integer)\n" + "Example: " + COMMAND_WORD + " " + PREFIX_VENDOR
            + "1";

    public ViewVendorCommand(Index targetIndex) {
        super(targetIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Vendor> lastShownList = model.getFilteredVendorList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_VENDOR_DISPLAYED_INDEX);
        }

        Vendor vendorToView = lastShownList.get(targetIndex.getZeroBased());
        model.viewVendor(vendorToView);
        model.setUiState(UiState.VENDOR_DETAILS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(vendorToView)));
    }
}
