package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SetNoteCommand;

public class SetNoteCommandParserTest {
    private SetNoteCommandParser parser = new SetNoteCommandParser();

    @Test
    public void parse_validArgs_returnsSetNoteCommand() {
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + " " + PREFIX_NOTE + "This is a valid note",
                new SetNoteCommand("This is a valid note"));
    }

    @Test
    public void parse_invalidArgs_throwsError() {
        try {
            parser.parse("");
        } catch (Exception e) {
            assertEquals(e.getMessage(), String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetNoteCommand.MESSAGE_USAGE));
        }
    }

}
