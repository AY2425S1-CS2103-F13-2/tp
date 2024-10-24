package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.association.Association;
import seedu.address.model.commons.exceptions.AssociationDeleteException;
import seedu.address.model.event.Event;
import seedu.address.model.vendor.Vendor;
import seedu.address.ui.UiState;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Vendor> PREDICATE_SHOW_ALL_VENDORS = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<Event> PREDICATE_SHOW_ALL_EVENTS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a vendor with the same identity as {@code vendor} exists in the address book.
     */
    boolean hasVendor(Vendor vendor);

    /**
     * Deletes the given vendor. The vendor must exist in the address book.
     */
    void deleteVendor(Vendor target) throws AssociationDeleteException;

    /**
     * Adds the given vendor. {@code vendor} must not already exist in the address book.
     */
    void addVendor(Vendor vendor);

    /**
     * Replaces the given vendor {@code target} with {@code editedVendor}. {@code target} must exist in the address
     * book. The vendor identity of {@code editedVendor} must not be the same as another existing vendor in the address
     * book.
     */
    void setVendor(Vendor target, Vendor editedVendor);

    /** Returns an unmodifiable view of the filtered vendor list */
    ObservableList<Vendor> getFilteredVendorList();

    /**
     * Updates the filter of the filtered vendor list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredVendorList(Predicate<Vendor> predicate);

    /**
     * Returns true if the given {@code vendor} is already assigned to the given {@code event}. {@code vendor} and
     * {@code event} must exist in the address book.
     */
    boolean isVendorAssignedToEvent(Vendor vendor, Event event);

    /**
     * Assigns the given vendor to the given event.
     */
    void assignVendorToEvent(Vendor vendor, Event event);

    /**
     * Unassigns the given vendor from the given event.
     */
    void unassignVendorFromEvent(Vendor vendor, Event event);

    /**
     * Returns list of associated vendors to an event.
     */
    ObservableList<Vendor> getAssociatedVendors(Event event);

    /**
     * Returns list of associated events to a vendor.
     */
    ObservableList<Event> getAssociatedEvents(Vendor vendor);

    /**
     * Returns list of associations.
     */
    ObservableList<Association> getAssociationList();

    /**
     * Returns the current selected vendor.
     */
    ObservableObjectValue<Vendor> getViewedVendor();

    /**
     * Sets the selected vendor.
     */
    void viewVendor(Vendor vendor);

    /*
     * Returns true if an event with the same identity as {@code event} exists in the address book.
     */
    boolean hasEvent(Event event);

    /**
     * Deletes the given event. The event must exist in the address book.
     */
    void deleteEvent(Event target) throws AssociationDeleteException;

    /**
     * Adds the given event. {@code event} must not already exist in the address book.
     */
    void addEvent(Event event);

    /**
     * Replaces the given event {@code target} with {@code editedEvent}. {@code target} must exist in the address
     * book. The event identity of {@code editedEvent} must not be the same as another existing event in the address
     * book.
     */
    void setEvent(Event target, Event editedEvent);

    /** Returns an unmodifiable view of the filtered event list */
    ObservableList<Event> getFilteredEventList();

    /**
     * Updates the filter of the filtered event list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredEventList(Predicate<Event> predicate);

    /**
     * Returns the current selected event.
     */
    ObservableObjectValue<Event> getViewedEvent();

    /**
     * Sets the selected Event.
     */
    void viewEvent(Event event);

    /**
     * Returns the current UI state.
     * @return {@code UiState} observable object.
     */
    public ObservableObjectValue<UiState> getUiState();

    /**
     * Sets the current UI state.
     * @param uiState {@code UiState} object.
     */
    public void setUiState(UiState uiState);
}
