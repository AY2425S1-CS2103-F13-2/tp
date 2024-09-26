package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Gets the note for display
 */
public class GetNoteCommand extends Command {

    public static final String COMMAND_WORD = "note";

    public static final String MESSAGE_SUCCESS = "Here is your note: %1$s";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        String note = model.getNote();

        if (note == null || note.isEmpty()) {
            throw new CommandException("There is no note to display.");
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, note));
    }
}
