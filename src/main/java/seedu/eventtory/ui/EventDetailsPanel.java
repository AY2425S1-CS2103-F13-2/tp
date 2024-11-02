package seedu.eventtory.ui;

import java.util.Comparator;
import java.util.logging.Logger;

import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.eventtory.commons.core.LogsCenter;
import seedu.eventtory.logic.Logic;
import seedu.eventtory.model.association.Association;
import seedu.eventtory.model.event.Event;
import seedu.eventtory.model.vendor.Vendor;

/**
 * Panel containing the list of vendors.
 */
public class EventDetailsPanel extends UiPart<Region> {
    private static final String FXML = "EventDetailsPanel.fxml";
    private Event event;
    private final Logic logic;
    private final Logger logger = LogsCenter.getLogger(VendorDetailsPanel.class);

    @FXML
    private Label name;
    @FXML
    private Label date;
    @FXML
    private Label noEventMsg;
    @FXML
    private GridPane detailsHolder;
    @FXML
    private StackPane detailsChildrenPlaceholder;
    @FXML
    private FlowPane tags;

    private ObservableList<Vendor> assignedVendors;

    private ObservableIntegerValue displayAssignedStartIndex;

    /**
     * Creates a {@code VendorListPanel} with the given {@code ObservableList}.
     */
    public EventDetailsPanel(Logic logic) {
        super(FXML);
        this.logic = logic;
        assignedVendors = FXCollections.observableArrayList();
        displayAssignedStartIndex = logic.getAssignedVendorsDisplayStartIdx();

        ObservableObjectValue<Event> observableEvent = logic.getViewedEvent();
        setEvent(observableEvent.get());
        showEventDetails();
        updateAssignedVendors();

        observableEvent.addListener((observable, oldValue, newValue) -> {
            setEvent(newValue);
            showEventDetails();
            updateAssignedVendors();
        });

        ObservableList<Association> associations = logic.getAssociationList();
        associations.addListener((ListChangeListener<? super Association>) change -> {
            updateAssignedVendors();
        });

        displayAssignedStartIndex.addListener((observable, oldValue, newValue) -> {
            updateAssignedVendors();
        });

        VendorListPanel vendorListPanel = new VendorListPanel(
            assignedVendors, "Assigned Vendors", displayAssignedStartIndex);
        detailsChildrenPlaceholder.getChildren().add(vendorListPanel.getRoot());
    }

    private void setEvent(Event event) {
        this.event = event;
        if (event != null) {
            name.setText(event.getName().fullName);
            date.setText(event.getDate().toString());
            // Empty tags will leave behind the last set of tags,
            // so we clear the tags before adding new tags
            event.getTags().stream().sorted(Comparator.comparing(tag -> tag.tagName))
                    .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        } else {
            name.setText("");
            date.setText("");
        }
    }

    private void showEventDetails() {
        if (event == null) {
            detailsHolder.setVisible(false);
            noEventMsg.setVisible(true);
        } else {
            detailsHolder.setVisible(true);
            noEventMsg.setVisible(false);
        }
    }

    private void updateAssignedVendors() {
        if (event == null) {
            assignedVendors.clear();
        } else {
            assignedVendors.setAll(logic.getAssociatedVendors(event));
        }
    }

}
