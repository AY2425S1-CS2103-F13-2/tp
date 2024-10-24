package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.association.Association;
import seedu.address.model.commons.exceptions.AssociationDeleteException;
import seedu.address.model.event.Event;
import seedu.address.model.vendor.Vendor;
import seedu.address.ui.UiState;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Vendor> filteredVendors;
    private final ObjectProperty<Vendor> selectedVendor;
    private final FilteredList<Event> filteredEvents;
    private final ObjectProperty<Event> selectedEvent;
    private final ObjectProperty<UiState> currentUiState;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredVendors = new FilteredList<>(this.addressBook.getVendorList());
        filteredEvents = new FilteredList<>(this.addressBook.getEventList());
        selectedVendor = new SimpleObjectProperty<>(null);
        selectedEvent = new SimpleObjectProperty<>(null);
        currentUiState = new SimpleObjectProperty<>(UiState.DEFAULT);
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    // =========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    // =========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasVendor(Vendor vendor) {
        requireNonNull(vendor);
        return addressBook.hasVendor(vendor);
    }

    @Override
    public void deleteVendor(Vendor target) throws AssociationDeleteException {
        addressBook.removeVendor(target);
    }

    @Override
    public void addVendor(Vendor vendor) {
        addressBook.addVendor(vendor);
        updateFilteredVendorList(PREDICATE_SHOW_ALL_VENDORS);
    }

    @Override
    public void setVendor(Vendor target, Vendor editedVendor) {
        requireAllNonNull(target, editedVendor);

        addressBook.setVendor(target, editedVendor);
    }

    @Override
    public boolean hasEvent(Event event) {
        requireNonNull(event);
        return addressBook.hasEvent(event);
    }

    @Override
    public void deleteEvent(Event target) throws AssociationDeleteException {
        addressBook.removeEvent(target);
    }

    @Override
    public void addEvent(Event event) {
        addressBook.addEvent(event);
        updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
    }

    @Override
    public void setEvent(Event target, Event editedEvent) {
        requireAllNonNull(target, editedEvent);

        addressBook.setEvent(target, editedEvent);
    }

    // =========== Assigning vendors and events =============================================================

    @Override
    public boolean isVendorAssignedToEvent(Vendor vendor, Event event) {
        requireAllNonNull(vendor, event);
        return addressBook.isVendorAssignedToEvent(vendor, event);
    }

    @Override
    public void assignVendorToEvent(Vendor vendor, Event event) {
        requireAllNonNull(vendor, event);
        addressBook.assignVendorToEvent(vendor, event);
    }

    @Override
    public void unassignVendorFromEvent(Vendor vendor, Event event) {
        requireAllNonNull(vendor, event);
        addressBook.unassignVendorFromEvent(vendor, event);
    }

    @Override
    public ObservableList<Event> getAssociatedEvents(Vendor vendor) {
        requireNonNull(vendor);
        return addressBook.getAssociatedEvents(vendor);
    }

    @Override
    public ObservableList<Vendor> getAssociatedVendors(Event event) {
        requireNonNull(event);
        return addressBook.getAssociatedVendors(event);
    }

    @Override
    public ObservableList<Association> getAssociationList() {
        return addressBook.getAssociationList();
    }

    // =========== Filtered Vendor List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Vendor} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Vendor> getFilteredVendorList() {
        return filteredVendors;
    }

    @Override
    public void updateFilteredVendorList(Predicate<Vendor> predicate) {
        requireNonNull(predicate);
        filteredVendors.setPredicate(predicate);
    }

    // =========== Filtered Event List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Event} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Event> getFilteredEventList() {
        return filteredEvents;
    }

    @Override
    public void updateFilteredEventList(Predicate<Event> predicate) {
        requireNonNull(predicate);
        filteredEvents.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook) && userPrefs.equals(otherModelManager.userPrefs)
                && filteredVendors.equals(otherModelManager.filteredVendors)
                && filteredEvents.equals(otherModelManager.filteredEvents);
    }

    // =========== Viewed Vendor Accessors =============================================================

    @Override
    public ObservableObjectValue<Vendor> getViewedVendor() {
        return selectedVendor;
    }

    @Override
    public void viewVendor(Vendor vendor) {
        requireNonNull(vendor);
        selectedVendor.setValue(vendor);
    }

    // =========== Viewed Events Accessors =============================================================

    @Override
    public ObservableObjectValue<Event> getViewedEvent() {
        return selectedEvent;
    }

    @Override
    public void viewEvent(Event event) {
        requireNonNull(event);
        selectedEvent.setValue(event);
    }

    // =========== UI State Accessors =============================================================
    @Override
    public ObservableObjectValue<UiState> getUiState() {
        return currentUiState;
    }

    @Override
    public void setUiState(UiState uiState) {
        requireNonNull(uiState);
        currentUiState.setValue(uiState);
    }

}
