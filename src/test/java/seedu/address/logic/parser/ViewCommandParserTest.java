package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EVENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_VENDOR;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ViewCommand;
import seedu.address.logic.commands.ViewEventCommand;
import seedu.address.logic.commands.ViewVendorCommand;

public class ViewCommandParserTest {

    private ViewCommandParser parser = new ViewCommandParser();

    @Test
    public void parse_validArgs_returnsViewVendorCommand() {
        assertParseSuccess(parser, " v/1", new ViewVendorCommand(INDEX_FIRST_VENDOR));
    }

    @Test
    public void parse_validArgs_returnsViewEventCommand() {
        assertParseSuccess(parser, " e/1", new ViewEventCommand(INDEX_FIRST_EVENT));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // invalid index - vendor
        assertParseFailure(parser, " v/1abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewVendorCommand.MESSAGE_USAGE));

        // invalid index - event
        assertParseFailure(parser, " e/1abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewEventCommand.MESSAGE_USAGE));

        // multiple prefixes
        assertParseFailure(parser, " v/1 e/1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));

        // no prefix
        assertParseFailure(parser, " a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
    }
}
