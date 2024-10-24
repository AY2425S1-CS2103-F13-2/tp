package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_VENDOR_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VENDOR;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalVendors.AMY;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.CreateVendorCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.association.Association;
import seedu.address.model.event.Event;
import seedu.address.model.event.UniqueEventList;
import seedu.address.model.vendor.UniqueVendorList;
import seedu.address.model.vendor.Vendor;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.EventBuilder;
import seedu.address.testutil.TypicalEvents;
import seedu.address.testutil.TypicalVendors;
import seedu.address.testutil.VendorBuilder;
import seedu.address.ui.UiState;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(
                temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete v/9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_VENDOR_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_MULTIVIEW_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION,
                String.format(LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION,
                String.format(LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void getFilteredVendorList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredVendorList().remove(0));
    }

    @Test
    public void getFilteredEventList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredEventList().remove(0));
    }

    @Test
    public void getUiState_setNewUiState_updateSuccessful() {
        ObjectProperty<UiState> observedState = new SimpleObjectProperty<>();
        logic.getUiState().addListener((observable, oldValue, newValue) -> {
            observedState.set(newValue);
        });

        model.setUiState(UiState.EVENT_DETAILS);
        assertEquals(UiState.EVENT_DETAILS, observedState.get());

        model.setUiState(UiState.VENDOR_DETAILS);
        assertEquals(UiState.VENDOR_DETAILS, observedState.get());
    }

    @Test
    public void getViewedEvent_setNewEvent_updateSuccessful() {
        ObjectProperty<Event> observedState = new SimpleObjectProperty<>();
        logic.getViewedEvent().addListener((observable, oldValue, newValue) -> {
            observedState.set(newValue);
        });

        Event event1 = new EventBuilder().withName("Event 1").build();
        Event event2 = new EventBuilder().withName("Event 2").build();

        model.viewEvent(event1);
        assertEquals(event1, observedState.get());

        model.viewEvent(event2);
        assertEquals(event2, observedState.get());
    }

    @Test
    public void getViewedVendor_setNewVendor_updateSuccessful() {
        ObjectProperty<Vendor> observedState = new SimpleObjectProperty<>();
        logic.getViewedVendor().addListener((observable, oldValue, newValue) -> {
            observedState.set(newValue);
        });

        Vendor vendor1 = new VendorBuilder().withName("Vendor 1").withPhone("123123").withDescription("Vendor 1")
                .build();
        Vendor vendor2 = new VendorBuilder().withName("Vendor 2").withPhone("321321").withDescription("Vendor 2")
                .build();

        model.viewVendor(vendor1);
        assertEquals(vendor1, observedState.get());

        model.viewVendor(vendor2);
        assertEquals(vendor2, observedState.get());
    }

    @Test
    public void getAssociation_newAssociation_updateSuccessful() {
        ObjectProperty<Association> observedState = new SimpleObjectProperty<>();
        ObservableList<Association> associations = logic.getAssociationList();
        associations.addListener((ListChangeListener<? super Association>) change -> {
            if (change.next() && change.wasAdded()) {
                observedState.set(change.getAddedSubList().get(0));
            }
        });

        model.assignVendorToEvent(TypicalVendors.AMY, TypicalEvents.BIRTHDAY);

        UniqueVendorList uniqueVendorList = new UniqueVendorList();
        uniqueVendorList.add(TypicalVendors.AMY);

        UniqueEventList uniqueEventList = new UniqueEventList();
        uniqueEventList.add(TypicalEvents.BIRTHDAY);

        Vendor uniqueVendor = null;
        for (Vendor vendor : uniqueVendorList) {
            if (vendor.equals(TypicalVendors.AMY)) {
                uniqueVendor = vendor;
                break;
            }
        }

        if (uniqueVendor == null) {
            throw new IllegalArgumentException("Vendor not found");
        }

        Event uniqueEvent = null;
        for (Event event : uniqueEventList) {
            if (event.equals(TypicalEvents.BIRTHDAY)) {
                uniqueEvent = event;
                break;
            }
        }

        if (uniqueEvent == null) {
            throw new IllegalArgumentException("Event not found");
        }

        Association expectedAssociation = new Association(uniqueVendor.getId(), uniqueEvent.getId());
        assertEquals(observedState.get(), expectedAssociation);
    }

    /**
     * Executes the command and confirms that - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage, Model expectedModel)
            throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     * @param e               the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        // Inject LogicManager with an AddressBookStorage that throws the IOException e
        // when saving
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(
                temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);

        logic = new LogicManager(model, storage);

        // Triggers the saveAddressBook method by executing an add command
        String addCommand = CreateVendorCommand.COMMAND_WORD + " " + PREFIX_VENDOR + NAME_DESC_AMY + PHONE_DESC_AMY
                + DESCRIPTION_DESC_AMY;
        Vendor expectedVendor = new VendorBuilder(AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addVendor(expectedVendor);
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }
}
