package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;

public class GetNoteCommandTest {

    private static final String note = "This is a valid note";

    @Test
    public void execute_getNoteFromModel_getSuccessful() throws Exception {
        ModelStubWithNote modelStub = new ModelStubWithNote();
        CommandResult commandResult = new GetNoteCommand().execute(modelStub);

        assertEquals(String.format(GetNoteCommand.MESSAGE_SUCCESS, note), commandResult.getFeedbackToUser());
    }

    @Test
    public void execute_getEmptyNoteFromModel_getSuccessful() throws Exception {
        ModelStubWithoutNote modelStub = new ModelStubWithoutNote();

        assertEquals(modelStub.getNote(), null);
        try {
            new GetNoteCommand().execute(modelStub);
        } catch (CommandException e) {
            assertEquals(GetNoteCommand.MESSAGE_NO_NOTE, e.getMessage());
        }
    }

    private class ModelStubWithoutNote extends ModelStub {
        @Override
        public String getNote() {
            return null;
        }
    }

    private class ModelStubWithNote extends ModelStub {
        @Override
        public String getNote() {
            return note;
        }
    }
}
