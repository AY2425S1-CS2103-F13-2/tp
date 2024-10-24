package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventNameContainsKeywordsPredicate;
import seedu.address.model.id.UniqueId;
import seedu.address.model.vendor.NameContainsKeywordsPredicate;
import seedu.address.model.vendor.Vendor;
import seedu.address.testutil.EditEventDescriptorBuilder;
import seedu.address.testutil.EditVendorDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final UniqueId VALID_ID_AMY = new UniqueId("e54d0fd0-4eaa-11ec-81d3-0242ac130003");
    public static final UniqueId VALID_ID_BOB = new UniqueId("e54d0fd0-4eaa-11ec-81d3-0242ac130004");
    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_DESCRIPTION_AMY = "Block 312, Amy Street 1";
    public static final String VALID_DESCRIPTION_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String DESCRIPTION_DESC_AMY = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_AMY;
    public static final String DESCRIPTION_DESC_BOB = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    // empty string not allowed for description
    public static final String INVALID_DESCRIPTION_DESC = " " + PREFIX_DESCRIPTION;
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags
    public static final String INVALID_DATE_DESC = " " + PREFIX_DATE + "12-12-12"; // wrong date format

    public static final String VALID_NAME_WEDDING = "Wedding";
    public static final String VALID_NAME_BIRTHDAY = "Birthday";
    public static final String VALID_DATE_WEDDING = "2024-12-12";
    public static final String VALID_DATE_BIRTHDAY = "2023-01-01";
    public static final String VALID_TAG_CONFERENCE = "conference";
    public static final String VALID_TAG_CHARITY = "charity";
    public static final String VALID_TAG_WEDDING = "wedding";
    public static final String VALID_TAG_BIRTHDAY = "birthday";

    public static final String NAME_DESC_WEDDING = " " + PREFIX_NAME + VALID_NAME_WEDDING;
    public static final String NAME_DESC_BIRTHDAY = " " + PREFIX_NAME + VALID_NAME_BIRTHDAY;
    public static final String DATE_DESC_WEDDING = " " + PREFIX_DATE + VALID_DATE_WEDDING;
    public static final String DATE_DESC_BIRTHDAY = " " + PREFIX_DATE + VALID_DATE_BIRTHDAY;
    public static final String TAG_DESC_CONFERENCE = " " + PREFIX_TAG + VALID_TAG_CONFERENCE;
    public static final String TAG_DESC_CHARITY = " " + PREFIX_TAG + VALID_TAG_CHARITY;
    public static final String TAG_DESC_WEDDING = " " + PREFIX_TAG + VALID_TAG_WEDDING;
    public static final String TAG_DESC_BIRTHDAY = " " + PREFIX_TAG + VALID_TAG_BIRTHDAY;

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditVendorCommand.EditVendorDescriptor DESC_AMY;
    public static final EditVendorCommand.EditVendorDescriptor DESC_BOB;
    public static final EditEventCommand.EditEventDescriptor DESC_BIRTHDAY;
    public static final EditEventCommand.EditEventDescriptor DESC_WEDDING;

    static {
        DESC_AMY = new EditVendorDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withDescription(VALID_DESCRIPTION_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditVendorDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withDescription(VALID_DESCRIPTION_BOB)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
        DESC_BIRTHDAY = new EditEventDescriptorBuilder().withName(VALID_NAME_BIRTHDAY)
                .withDate(VALID_DATE_BIRTHDAY).build();
        DESC_WEDDING = new EditEventDescriptorBuilder().withName(VALID_NAME_WEDDING)
                .withDate(VALID_DATE_WEDDING).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult}
     * <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to
     * {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered vendor list and selected vendor in
     * {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Vendor> expectedFilteredList = new ArrayList<>(actualModel.getFilteredVendorList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredVendorList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the vendor at the given
     * {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showVendorAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredVendorList().size());

        Vendor vendor = model.getFilteredVendorList().get(targetIndex.getZeroBased());
        final String[] splitName = vendor.getName().fullName.split("\\s+");
        model.updateFilteredVendorList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredVendorList().size());
    }

    /**
     * Updates {@code model}'s filtered list to show only the event at the given
     * {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showEventAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredEventList().size());

        Event event = model.getFilteredEventList().get(targetIndex.getZeroBased());
        final String[] splitName = event.getName().fullName.split("\\s+");
        model.updateFilteredEventList(new EventNameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredEventList().size());
    }
}
