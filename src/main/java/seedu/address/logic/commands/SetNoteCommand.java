package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;

import seedu.address.model.Model;

/**
 * Add a note to the address book.
 */
public class SetNoteCommand extends Command {

    public static final String COMMAND_WORD = "note";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a note to the address book. "
            + "Parameters: "
            + PREFIX_NOTE + "NOTE \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NOTE + "This is a note";

    public static final String MESSAGE_SUCCESS = "New note added: %1$s";

    private final String toAdd;

    /**
     * Creates a SetNoteCommand to add the specified {@code note}
     */
    public SetNoteCommand(String note) {
        requireNonNull(note);
        toAdd = note;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        model.setNote(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public String toString() {
        return "NoteCommand: " + toAdd;
    }

}
