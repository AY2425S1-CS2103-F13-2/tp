package seedu.eventtory.ui;

import java.util.logging.Logger;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.eventtory.commons.core.LogsCenter;
import seedu.eventtory.model.event.Event;

/**
 * Panel containing the list of events.
 */
public class EventListPanel extends UiPart<Region> {
    private static final String FXML = "EventListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(EventListPanel.class);

    @FXML
    private Label header;
    @FXML
    private ListView<Event> eventListView;

    /**
     * Creates a {@code EventListPanel} with the given {@code ObservableList}.
     */
    public EventListPanel(ObservableList<Event> eventList, String headerText,
        ObservableIntegerValue displayIndexOffset) {
        super(FXML);
        eventListView.setItems(eventList);
        eventListView.setCellFactory(listView -> new EventListViewCell(displayIndexOffset));
        header.setText(headerText);
    }

    /**
     * Alternate constructor for {@code EventListPanel} with a default index offset of 1.
     */
    public EventListPanel(ObservableList<Event> eventList, String headerText) {
        this(eventList, headerText, new SimpleIntegerProperty(1));
    }

    /**
     * Set the header of the event list panel.
     */
    public void setHeader(String headerText) {
        header.setText(headerText);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Event} using a {@code EventCard}.
     */
    class EventListViewCell extends ListCell<Event> {
        private final ObservableIntegerValue displayIndexOffset;

        public EventListViewCell(ObservableIntegerValue displayIndexOffset) {
            super();
            this.displayIndexOffset = displayIndexOffset;
        }

        @Override
        protected void updateItem(Event event, boolean empty) {
            super.updateItem(event, empty);

            if (empty || event == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new EventCard(event, getIndex() + displayIndexOffset.get()).getRoot());
            }
        }
    }

}
