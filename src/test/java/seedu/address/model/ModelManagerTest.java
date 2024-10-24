package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_VENDORS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalEvents.BIRTHDAY;
import static seedu.address.testutil.TypicalEvents.WEDDING;
import static seedu.address.testutil.TypicalVendors.ALICE;
import static seedu.address.testutil.TypicalVendors.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.association.Association;
import seedu.address.model.commons.name.Name;
import seedu.address.model.event.Date;
import seedu.address.model.event.Event;
import seedu.address.model.event.UniqueEventList;
import seedu.address.model.vendor.NameContainsKeywordsPredicate;
import seedu.address.model.vendor.UniqueVendorList;
import seedu.address.model.vendor.Vendor;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.EventBuilder;
import seedu.address.testutil.TypicalEvents;
import seedu.address.testutil.TypicalVendors;
import seedu.address.testutil.VendorBuilder;
import seedu.address.ui.UiState;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasVendor_nullVendor_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasVendor(null));
    }

    @Test
    public void hasVendor_vendorNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasVendor(ALICE));
    }

    @Test
    public void hasVendor_vendorInAddressBook_returnsTrue() {
        modelManager.addVendor(ALICE);
        assertTrue(modelManager.hasVendor(ALICE));
    }

    @Test
    public void hasEvent_nullEvent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasEvent(null));
    }

    @Test
    public void hasEvent_eventNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasEvent(WEDDING));
    }

    @Test
    public void hasEvent_eventInAddressBook_returnsTrue() {
        modelManager.addEvent(WEDDING);
        assertTrue(modelManager.hasEvent(WEDDING));
    }

    @Test
    public void getFilteredVendorList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredVendorList().remove(0));
    }

    @Test
    public void getFilteredEventList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredEventList().remove(0));
    }

    @Test
    public void getUiState_setNewUiState_updateSuccessful() {
        ObjectProperty<UiState> observedState = new SimpleObjectProperty<>();
        modelManager.getUiState().addListener((observable, oldValue, newValue) -> {
            observedState.set(newValue);
        });

        modelManager.setUiState(UiState.EVENT_DETAILS);
        assertEquals(UiState.EVENT_DETAILS, observedState.get());

        modelManager.setUiState(UiState.VENDOR_DETAILS);
        assertEquals(UiState.VENDOR_DETAILS, observedState.get());
    }

    @Test
    public void getViewedEvent_setNewEvent_updateSuccessful() {
        ObjectProperty<Event> observedState = new SimpleObjectProperty<>();
        modelManager.getViewedEvent().addListener((observable, oldValue, newValue) -> {
            observedState.set(newValue);
        });

        Event event1 = new EventBuilder().withName("Event 1").build();
        Event event2 = new EventBuilder().withName("Event 2").build();

        modelManager.viewEvent(event1);
        assertEquals(event1, observedState.get());

        modelManager.viewEvent(event2);
        assertEquals(event2, observedState.get());
    }

    @Test
    public void getViewedVendor_setNewVendor_updateSuccessful() {
        ObjectProperty<Vendor> observedState = new SimpleObjectProperty<>();
        modelManager.getViewedVendor().addListener((observable, oldValue, newValue) -> {
            observedState.set(newValue);
        });

        Vendor vendor1 = new VendorBuilder().withName("Vendor 1").withPhone("123123").withDescription("Vendor 1")
                .build();
        Vendor vendor2 = new VendorBuilder().withName("Vendor 2").withPhone("321321").withDescription("Vendor 2")
                .build();

        modelManager.viewVendor(vendor1);
        assertEquals(vendor1, observedState.get());

        modelManager.viewVendor(vendor2);
        assertEquals(vendor2, observedState.get());
    }

    @Test
    public void getAssociatedVendors_noAssociations_returnsEmptyList() {
        ObservableList<Vendor> associatedVendors = modelManager.getAssociatedVendors(WEDDING);
        assertEquals(FXCollections.observableArrayList(), associatedVendors);
    }

    @Test
    public void getAssociatedVendors_withAssociations_returnsCorrectVendors() {
        modelManager.addVendor(ALICE);
        modelManager.addVendor(BENSON);
        modelManager.addEvent(WEDDING);
        modelManager.assignVendorToEvent(ALICE, WEDDING);
        modelManager.assignVendorToEvent(BENSON, WEDDING);

        ObservableList<Vendor> associatedVendors = modelManager.getAssociatedVendors(WEDDING);
        ObservableList<Vendor> expectedVendors = FXCollections.observableArrayList(BENSON, ALICE);

        associatedVendors.sort((v1, v2) -> v1.getName().fullName.compareTo(v2.getName().fullName));
        expectedVendors.sort((v1, v2) -> v1.getName().fullName.compareTo(v2.getName().fullName));

        assertEquals(expectedVendors, associatedVendors);
    }

    @Test
    public void getAssociatedEvents_noAssociations_returnsEmptyList() {
        ObservableList<Event> associatedEvents = modelManager.getAssociatedEvents(ALICE);
        assertEquals(FXCollections.observableArrayList(), associatedEvents);
    }

    @Test
    public void getAssociatedEvents_withAssociations_returnsCorrectEvents() {
        modelManager.addVendor(ALICE);
        modelManager.addEvent(WEDDING);
        modelManager.addEvent(BIRTHDAY);
        modelManager.assignVendorToEvent(ALICE, WEDDING);
        modelManager.assignVendorToEvent(ALICE, BIRTHDAY);

        ObservableList<Event> associatedEvents = modelManager.getAssociatedEvents(ALICE);
        ObservableList<Event> expectedEvents = FXCollections.observableArrayList(WEDDING, BIRTHDAY);

        associatedEvents.sort((e1, e2) -> e1.getName().fullName.compareTo(e2.getName().fullName));
        expectedEvents.sort((e1, e2) -> e1.getName().fullName.compareTo(e2.getName().fullName));

        assertEquals(expectedEvents, associatedEvents);
    }

    @Test
    public void getAssociation_newAssociation_updateSuccessful() {
        ObjectProperty<Association> observedState = new SimpleObjectProperty<>();
        modelManager.getAssociationList().addListener((ListChangeListener<? super Association>) change -> {
            if (change.next() && change.wasAdded()) {
                observedState.set((Association) change.getAddedSubList().get(0));
            }
        });

        modelManager.assignVendorToEvent(TypicalVendors.AMY, TypicalEvents.BIRTHDAY);

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

        Event uniqueEvent = null;
        for (Event event : uniqueEventList) {
            if (event.equals(TypicalEvents.BIRTHDAY)) {
                uniqueEvent = event;
                break;
            }
        }

        Association expectedAssociation = new Association(
                uniqueVendor.getId(),
                uniqueEvent.getId());
        assertEquals(observedState.get(), expectedAssociation);
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withVendor(ALICE).withVendor(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredVendorList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredVendorList(PREDICATE_SHOW_ALL_VENDORS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }
}

