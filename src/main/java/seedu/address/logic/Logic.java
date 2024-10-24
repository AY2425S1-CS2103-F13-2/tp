package seedu.address.logic;

import java.nio.file.Path;

import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.association.Association;
import seedu.address.model.event.Event;
import seedu.address.model.vendor.Vendor;
import seedu.address.ui.UiState;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the AddressBook.
     *
     * @see seedu.address.model.Model#getAddressBook()
     */
    ReadOnlyAddressBook getAddressBook();

    /** Returns an unmodifiable view of the filtered list of vendors */
    ObservableList<Vendor> getFilteredVendorList();

    /** Returns an unmodifiable view of the filtered list of events */
    ObservableList<Event> getFilteredEventList();

    /** Returns a view of the current selected vendor */
    ObservableObjectValue<Vendor> getViewedVendor();

    /** Returns a view of the current selected vendor */
    ObservableObjectValue<Event> getViewedEvent();

    /** Returns the current state of the UI */
    ObservableObjectValue<UiState> getUiState();

    /** Returns list of associated events */
    ObservableList<Event> getAssociatedEvents(Vendor vendor);

    /** Returns list of associated vendors */
    ObservableList<Vendor> getAssociatedVendors(Event event);

    /** Returns list of associations */
    ObservableList<Association> getAssociationList();

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);
}
