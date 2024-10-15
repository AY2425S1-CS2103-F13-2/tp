package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;

/**
 * Display details of an event in the eventTory to the user.
 */
public class ViewEventCommand extends ViewCommand {

    public static final String MESSAGE_SUCCESS = "Viewing %s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Views the vendor identified by the index number used in the displayed event list.\n" + "Parameters: "
            + PREFIX_EVENT + "INDEX (must be a positive integer)\n" + "Example: " + COMMAND_WORD + " " + PREFIX_EVENT
            + "1";

    public ViewEventCommand(Index targetIndex) {
        super(targetIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Event> lastShownList = model.getFilteredEventList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        Event eventToView = lastShownList.get(targetIndex.getZeroBased());
        model.viewEvent(eventToView);

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(eventToView)));
    }
}
