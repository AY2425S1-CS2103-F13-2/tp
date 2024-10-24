package seedu.address.ui;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The Main Window. Provides the basic application layout containing a menu bar and space where other JavaFX elements
 * can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private static final String EVENT_LIST_HEADER = "Events List";
    private static final String VENDOR_LIST_HEADER = "Vendors List";
    private static final String ASSIGNABLE_EVENT_HEADER = "Assignable Events";
    private static final String ASSIGNABLE_VENDOR_HEADER = "Assignable Vendors";
    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private VendorListPanel vendorListPanel;
    private EventListPanel eventListPanel;
    private VendorDetailsPanel vendorDetailsPanel;
    private EventDetailsPanel eventDetailsPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private GridPane panelsHolder;

    @FXML
    private StackPane leftPanelPlaceholder;

    @FXML
    private StackPane rightPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();

        logic.getUiState().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
            case DEFAULT:
                setDefaultView();
                break;
            case VENDOR_DETAILS:
                setVendorDetailsView();
                break;
            case EVENT_DETAILS:
                setEventDetailsView();
                break;
            case VENDOR_LIST:
                setVendorList();
                break;
            case EVENT_LIST:
                setEventList();
                break;
            default:
                singleView();
                break;
            }
        });
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666 is fixed in later version of SDK. According to the bug
         * report, TextInputControl (TextField, TextArea) will consume function-key events. Because CommandBox contains
         * a TextField, and ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will not work when the
         * focus is in them because the key event is consumed by the TextInputControl(s). For now, we add following
         * event filter to capture such key events and open help window purposely so to support accelerators even when
         * focus is in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        splitView();

        eventDetailsPanel = new EventDetailsPanel(logic);
        vendorDetailsPanel = new VendorDetailsPanel(logic);

        vendorListPanel = new VendorListPanel(logic.getFilteredVendorList(), VENDOR_LIST_HEADER);
        leftPanelPlaceholder.getChildren().add(vendorListPanel.getRoot());

        eventListPanel = new EventListPanel(logic.getFilteredEventList(), EVENT_LIST_HEADER);
        rightPanelPlaceholder.getChildren().add(eventListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Set main window to display two panels.
     */
    void splitView() {
        panelsHolder.getColumnConstraints().get(1).setPercentWidth(50);
        panelsHolder.getColumnConstraints().get(1).setMaxWidth(Double.MAX_VALUE);
    }

    /**
     * Set main window to display only one panel.
     */
    void singleView() {
        panelsHolder.getColumnConstraints().get(1).setPercentWidth(0);
        panelsHolder.getColumnConstraints().get(1).setMaxWidth(0);
    }

    /**
     * Set main window to display default view.
     */
    void setDefaultView() {
        splitView();
        eventListPanel.setHeader(EVENT_LIST_HEADER);
        rightPanelPlaceholder.getChildren().clear();
        rightPanelPlaceholder.getChildren().add(eventListPanel.getRoot());
        vendorListPanel.setHeader(VENDOR_LIST_HEADER);
        leftPanelPlaceholder.getChildren().clear();
        leftPanelPlaceholder.getChildren().add(vendorListPanel.getRoot());
    }

    /**
     * Set main window to display vendor details.
     */
    void setVendorDetailsView() {
        splitView();
        leftPanelPlaceholder.getChildren().clear();
        leftPanelPlaceholder.getChildren().add(vendorDetailsPanel.getRoot());
        eventListPanel.setHeader(ASSIGNABLE_EVENT_HEADER);
        rightPanelPlaceholder.getChildren().clear();
        rightPanelPlaceholder.getChildren().add(eventListPanel.getRoot());
    }

    /**
     * Set main window to display event details.
     */
    void setEventDetailsView() {
        splitView();
        leftPanelPlaceholder.getChildren().clear();
        leftPanelPlaceholder.getChildren().add(eventDetailsPanel.getRoot());
        vendorListPanel.setHeader(ASSIGNABLE_VENDOR_HEADER);
        rightPanelPlaceholder.getChildren().clear();
        rightPanelPlaceholder.getChildren().add(vendorListPanel.getRoot());
    }

    /**
     * Set main window to display vendor list.
     */
    void setVendorList() {
        singleView();
        vendorListPanel.setHeader(VENDOR_LIST_HEADER);
        leftPanelPlaceholder.getChildren().clear();
        leftPanelPlaceholder.getChildren().add(vendorListPanel.getRoot());
    }

    /**
     * Set main window to display event list.
     */
    void setEventList() {
        singleView();
        eventListPanel.setHeader(EVENT_LIST_HEADER);
        leftPanelPlaceholder.getChildren().clear();
        leftPanelPlaceholder.getChildren().add(eventListPanel.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    public VendorListPanel getVendorListPanel() {
        return vendorListPanel;
    }

    /**
     * Executes the command and returns the result.
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
