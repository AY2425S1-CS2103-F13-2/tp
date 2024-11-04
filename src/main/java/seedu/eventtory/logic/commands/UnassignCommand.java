package seedu.eventtory.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.eventtory.commons.core.index.Index;
import seedu.eventtory.logic.Messages;
import seedu.eventtory.logic.commands.exceptions.CommandException;
import seedu.eventtory.logic.commands.util.IndexResolverUtil;
import seedu.eventtory.model.Model;
import seedu.eventtory.model.event.Event;
import seedu.eventtory.model.vendor.Vendor;

/**
 * Unassigns a vendor from an event.
 */
public class UnassignCommand extends Command {

    public static final String COMMAND_WORD = "unassign";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Unassigns a vendor from an event. "
            + "Parameters: "
            + "v/<VENDOR_INDEX> e/<EVENT_INDEX> or e/<EVENT_INDEX> v/<VENDOR_INDEX>\n"
            + "Example: " + COMMAND_WORD + " v/1 e/2";

    public static final String MESSAGE_UNASSIGN_SUCCESS = "Vendor %s unassigned from Event %s";

    private final Index vendorIndex;
    private final Index eventIndex;

    /**
     * Creates an UnassignCommand to unassign the specified {@code Vendor} from the
     * specified {@code Event}.
     */
    public UnassignCommand(Index vendorIndex, Index eventIndex) {
        requireNonNull(vendorIndex);
        requireNonNull(eventIndex);
        this.vendorIndex = vendorIndex;
        this.eventIndex = eventIndex;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnassignCommand)) {
            return false;
        }

        UnassignCommand otherCommand = (UnassignCommand) other;
        return vendorIndex.equals(otherCommand.vendorIndex)
                && eventIndex.equals(otherCommand.eventIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Vendor vendor = IndexResolverUtil.resolveVendor(model, vendorIndex);
        Event event = IndexResolverUtil.resolveEvent(model, eventIndex);

        if (!model.isVendorAssignedToEvent(vendor, event)) {
            throw new CommandException(Messages.MESSAGE_VENDOR_NOT_ASSIGNED_TO_EVENT);
        }

        model.unassignVendorFromEvent(vendor, event);

        return new CommandResult(
                String.format(MESSAGE_UNASSIGN_SUCCESS,
                        vendorIndex.getOneBased(),
                        eventIndex.getOneBased()));
    }
}
