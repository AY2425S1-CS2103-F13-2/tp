package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;

import seedu.address.logic.commands.SetNoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new NoteCommand object
 */
public class SetNoteCommandParser implements Parser<SetNoteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * NoteCommand
     * and returns a NoteCommand object for execution.
     */
    public SetNoteCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NOTE);

        if (!argMultimap.getPreamble().isEmpty() || argMultimap.getValue(PREFIX_NOTE).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetNoteCommand.MESSAGE_USAGE));
        }

        String note = argMultimap.getValue(PREFIX_NOTE).get();
        return new SetNoteCommand(note);
    }

}
