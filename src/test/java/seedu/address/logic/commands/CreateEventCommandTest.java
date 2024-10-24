package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.association.Association;
import seedu.address.model.event.Event;
import seedu.address.model.vendor.Vendor;
import seedu.address.testutil.EventBuilder;
import seedu.address.testutil.TypicalEvents;
import seedu.address.ui.UiState;

public class CreateEventCommandTest {
    @Test
    public void constructor_nullEvent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CreateEventCommand(null));
    }

    @Test
    public void execute_eventAcceptedByModel_addSuccessful() throws Exception {
        CreateEventCommandTest.ModelStubAcceptingEventAdded modelStub = new
            CreateEventCommandTest.ModelStubAcceptingEventAdded();
        Event validEvent = new EventBuilder().build();

        CommandResult commandResult = new CreateEventCommand(validEvent).execute(modelStub);

        assertEquals(String.format(CreateEventCommand.MESSAGE_SUCCESS, Messages.format(validEvent)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validEvent), modelStub.eventsAdded);
    }

    @Test
    public void execute_duplicateVendor_throwsCommandException() {
        Event validEvent = new EventBuilder().build();
        CreateEventCommand createEventCommand = new CreateEventCommand(validEvent);
        CreateEventCommandTest.ModelStub modelStub = new CreateEventCommandTest.ModelStubWithEvent(validEvent);

        assertThrows(CommandException.class, CreateEventCommand.MESSAGE_DUPLICATE_EVENT, ()
            -> createEventCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        CreateEventCommand aliceWeddingCommand = new CreateEventCommand(TypicalEvents.ALICE);
        CreateEventCommand bensonBirthdayCommand = new CreateEventCommand(TypicalEvents.BENSON);

        // same object -> returns true
        assertTrue(aliceWeddingCommand.equals(aliceWeddingCommand));

        // same values -> returns true
        CreateEventCommand besonBirthdayCommandCopy = new CreateEventCommand(TypicalEvents.BENSON);
        assertTrue(bensonBirthdayCommand.equals(besonBirthdayCommandCopy));

        // different types -> returns false
        assertFalse(aliceWeddingCommand.equals(1));

        // null -> returns false
        assertFalse(aliceWeddingCommand.equals(null));

        // different vendor -> returns false
        assertFalse(aliceWeddingCommand.equals(bensonBirthdayCommand));
    }

    @Test
    public void toStringMethod() {
        CreateEventCommand createEventCommand = new CreateEventCommand(TypicalEvents.HOON);
        String expected = CreateEventCommand.class.getCanonicalName() + "{toAdd=" + TypicalEvents.HOON + "}";
        assertEquals(expected, createEventCommand.toString());
    }

    /**
     * A default model stub that has all the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addVendor(Vendor vendor) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasVendor(Vendor vendor) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteVendor(Vendor target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setVendor(Vendor target, Vendor editedVendor) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasEvent(Event event) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addEvent(Event event) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setEvent(Event target, Event editedEvent) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteEvent(Event target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Vendor> getFilteredVendorList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredVendorList(Predicate<Vendor> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Event> getFilteredEventList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredEventList(Predicate<Event> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean isVendorAssignedToEvent(Vendor vendor, Event event) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void assignVendorToEvent(Vendor vendor, Event event) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void unassignVendorFromEvent(Vendor vendor, Event event) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Vendor> getAssociatedVendors(Event event) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Event> getAssociatedEvents(Vendor vendor) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setUiState(UiState uiState) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableObjectValue<UiState> getUiState() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void viewEvent(Event event) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableObjectValue<Event> getViewedEvent() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void viewVendor(Vendor vendor) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableObjectValue<Vendor> getViewedVendor() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Association> getAssociationList() {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single event.
     */
    private class ModelStubWithEvent extends CreateEventCommandTest.ModelStub {
        private final Event event;

        ModelStubWithEvent(Event event) {
            requireNonNull(event);
            this.event = event;
        }

        @Override
        public boolean hasEvent(Event event) {
            requireNonNull(event);
            return this.event.isSameEvent(event);
        }
    }

    /**
     * A Model stub that always accepts the event being added.
     */
    private class ModelStubAcceptingEventAdded extends CreateEventCommandTest.ModelStub {
        final ArrayList<Event> eventsAdded = new ArrayList<>();

        @Override
        public boolean hasEvent(Event event) {
            requireNonNull(event);
            return eventsAdded.stream().anyMatch(event::isSameEvent);
        }

        @Override
        public void addEvent(Event event) {
            requireNonNull(event);
            eventsAdded.add(event);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
