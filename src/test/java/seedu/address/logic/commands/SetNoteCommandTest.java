package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;

public class SetNoteCommandTest {

    @Test
    public void constructor_nullNote_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SetNoteCommand(null));
    }

    @Test
    public void execute_noteAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingSetNote modelStub = new ModelStubAcceptingSetNote();

        String validNote = "This is a valid note";
        CommandResult commandResult = new SetNoteCommand(validNote).execute(modelStub);

        assertEquals(String.format(SetNoteCommand.MESSAGE_SUCCESS, validNote), commandResult.getFeedbackToUser());
        assertEquals(validNote, modelStub.getNote());
    }

    @Test
    public void toStringMethod() {
        String note = "note";
        SetNoteCommand setNote = new SetNoteCommand(note);
        String expected = SetNoteCommand.class.getCanonicalName() + "{note=" + note + "}";

        assertEquals(expected, setNote.toString());
    }

    @Test
    public void equals() {
        String noteA = "This is a valid note";
        String noteB = "This is another valid note";
        SetNoteCommand setNoteA = new SetNoteCommand(noteA);
        SetNoteCommand setNoteB = new SetNoteCommand(noteB);

        // same object -> returns true
        assertTrue(setNoteA.equals(setNoteA));

        // same values -> returns true
        SetNoteCommand setNoteACopy = new SetNoteCommand(noteA);
        assertTrue(setNoteA.equals(setNoteACopy));

        // different types -> returns false
        assertFalse(setNoteA.equals(1));

        // null -> returns false
        assertFalse(setNoteA.equals(null));

        // different person -> returns false
        assertFalse(setNoteA.equals(setNoteB));
    }

    private class ModelStubAcceptingSetNote extends ModelStub {
        private String note;

        @Override
        public void setNote(String note) {
            requireNonNull(note);
            this.note = note;
        }

        @Override
        public String getNote() {
            return note;
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

    }

}
