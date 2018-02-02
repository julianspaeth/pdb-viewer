package view.fxmlview;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class PDBViewerView {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private MenuItem loadMenuItem;

    @FXML
    private MenuItem loadFromPDBMenuItem;

    @FXML
    private MenuItem saveMenuItem;

    @FXML
    private MenuItem quitMenuItem;

    @FXML
    private Menu editMenu;

    @FXML
    private MenuItem undoMenuItem;

    @FXML
    private MenuItem redoMenuItem;

    @FXML
    private Menu viewMenu;

    @FXML
    private MenuItem increaseAtomsMenuItem;

    @FXML
    private MenuItem decreaseAtomsMenuItem;

    @FXML
    private MenuItem increaseBondsMenuItem;

    @FXML
    private MenuItem decreaseBondsMenuItem;

    @FXML
    private Menu chartMenu;

    @FXML
    private MenuItem aaChartMenuItem;

    @FXML
    private BorderPane viewBorderPane;

    @FXML
    private ToolBar toolbar;

    @FXML
    private CheckBox showAtomsCB;

    @FXML
    private CheckBox showBondsCB;

    @FXML
    private Button increaseAtomsBu;

    @FXML
    private Button decreaseAtomsBu;

    @FXML
    private Button increaseBondsBu;

    @FXML
    private Button decreaseBondsBu;

    @FXML
    private CheckBox showRibbonsCB;

    @FXML
    private Button undoBu;

    @FXML
    private Button redoBu;

    @FXML
    private StackPane proteinGraphStackPane;

    @FXML
    private Pane selectionPane;

    @FXML
    private Pane bottomPane;

    @FXML
    private CheckMenuItem showAtomsMenuItem;

    @FXML
    private CheckMenuItem showBondsMenuItem;

    @FXML
    private CheckMenuItem showRibbonsMenuItem;

    @FXML
    private MenuItem highlightStructureMenuItem;

    @FXML
    private MenuItem highlightAtomsMenuItem;

    @FXML
    private MenuItem spacingFillingModelMenuItem;

    @FXML
    private MenuItem mainChainAtomsModelMenuItem;

    @FXML
    private MenuItem backboneModelMenuItem;

    @FXML
    private MenuItem highlightAminoacidsMenuItem;

    @FXML
    private Label titleLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Button retryButton;

    private HBox structureHBox;

    @FXML
    private MenuItem ribbonModelMenuItem;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private Label atomsLa, bondsLa, aaLa, idLa;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private MenuItem highlightChainsMenuItem;

    @FXML
    private MenuItem structureModelMenuItem;

    public ScrollPane getScrollpane() {
        return scrollpane;
    }

    public MenuItem getLoadMenuItem() {
        return loadMenuItem;
    }

    public MenuItem getQuitMenuItem() {
        return quitMenuItem;
    }

    public Menu getEditMenu() {
        return editMenu;
    }

    public MenuItem getUndoMenuItem() {
        return undoMenuItem;
    }

    public MenuItem getRedoMenuItem() {
        return redoMenuItem;
    }

    public Menu getViewMenu() {
        return viewMenu;
    }

    public MenuItem getIncreaseAtomsMenuItem() {
        return increaseAtomsMenuItem;
    }

    public MenuItem getDecreaseAtomsMenuItem() {
        return decreaseAtomsMenuItem;
    }

    public MenuItem getIncreaseBondsMenuItem() {
        return increaseBondsMenuItem;
    }

    public MenuItem getDecreaseBondsMenuItem() {
        return decreaseBondsMenuItem;
    }

    public CheckBox getShowAtomsCB() {
        return showAtomsCB;
    }

    public CheckBox getShowBondsCB() {
        return showBondsCB;
    }

    public Button getIncreaseAtomsBu() {
        return increaseAtomsBu;
    }

    public Button getDecreaseAtomsBu() {
        return decreaseAtomsBu;
    }

    public Button getIncreaseBondsBu() {
        return increaseBondsBu;
    }

    public Button getDecreaseBondsBu() {
        return decreaseBondsBu;
    }

    public CheckBox getShowRibbonsCB() {
        return showRibbonsCB;
    }

    public Button getUndoBu() {
        return undoBu;
    }

    public Button getRedoBu() {
        return redoBu;
    }

    public StackPane getProteinGraphStackPane() {
        return proteinGraphStackPane;
    }

    public Pane getBottomPane() {
        return bottomPane;
    }

    public CheckMenuItem getShowAtomsMenuItem() {
        return showAtomsMenuItem;
    }

    public CheckMenuItem getShowBondsMenuItem() {
        return showBondsMenuItem;
    }

    public CheckMenuItem getShowRibbonsMenuItem() {
        return showRibbonsMenuItem;
    }

    public MenuItem getLoadFromPDBMenuItem() {
        return loadFromPDBMenuItem;
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }

    public Pane getSelectionPane() {
        return selectionPane;
    }

    public BorderPane getViewBorderPane() {
        return viewBorderPane;
    }

    public MenuItem getHighlightStructureMenuItem() {
        return highlightStructureMenuItem;
    }

    public MenuItem getHighlightAtomsMenuItem() {
        return highlightAtomsMenuItem;
    }

    public MenuItem getSpacingFillingModelMenuItem() {
        return spacingFillingModelMenuItem;
    }

    public MenuItem getMainChainAtomsModelMenuItem() {
        return mainChainAtomsModelMenuItem;
    }

    public MenuItem getBackboneModelMenuItem() {
        return backboneModelMenuItem;
    }

    public MenuItem getHighlightAminoacidsMenuItem() {
        return highlightAminoacidsMenuItem;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public Button getRetryButton() {
        return retryButton;
    }

    public Menu getChartMenu() {
        return chartMenu;
    }

    public MenuItem getAaChartMenuItem() {
        return aaChartMenuItem;
    }

    public HBox getStructureHbox() {
        return structureHBox;
    }

    public ToolBar getToolbar() {
        return toolbar;
    }

    public MenuItem getSaveMenuItem() {
        return saveMenuItem;
    }

    public MenuItem getRibbonModelMenuItem() {
        return ribbonModelMenuItem;
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public MenuItem getHighlightChainsMenuItem() {
        return highlightChainsMenuItem;
    }

    public MenuItem getStructureModelMenuItem() {
        return structureModelMenuItem;
    }

    public Label getAtomsLa() {
        return atomsLa;
    }

    public Label getBondsLa() {
        return bondsLa;
    }

    public Label getAaLa() {
        return aaLa;
    }

    public Label getIdLa() {
        return idLa;
    }

    public void initMenuAndToolbar() {
        this.getEditMenu().setDisable(false);
        this.getViewMenu().setDisable(false);
        this.getToolbar().setDisable(false);
        this.getChartMenu().setDisable(false);

        this.getShowAtomsCB().setSelected(true);
        this.getShowBondsCB().setSelected(true);
        this.getScrollpane().setVisible(true);
        this.getTitleLabel().setVisible(true);
        this.getSaveMenuItem().setDisable(false);
    }

    public void setStatusLabels(String id, int aa, int atoms, int bonds) {
        idLa.setText("ID: " + id);
        aaLa.setText("Amino Acids: " + Integer.toString(aa));
        atomsLa.setText("Atoms: " + Integer.toString(atoms));
        bondsLa.setText("Bonds: " + Integer.toString(bonds));
    }

    public void setToolbarMenuBindings() {
        this.getShowAtomsCB().selectedProperty().bindBidirectional(this.getShowAtomsMenuItem().selectedProperty());
        this.getShowAtomsCB().disableProperty().bindBidirectional(this.getShowAtomsMenuItem().disableProperty());
        this.getShowBondsCB().selectedProperty().bindBidirectional(this.getShowBondsMenuItem().selectedProperty());
        this.getShowBondsCB().disableProperty().bindBidirectional(this.getShowBondsMenuItem().disableProperty());
        this.getShowRibbonsCB().selectedProperty().bindBidirectional(this.getShowRibbonsMenuItem().selectedProperty());
        this.getShowRibbonsCB().disableProperty().bindBidirectional(this.getShowRibbonsMenuItem().disableProperty());
        this.getDecreaseAtomsBu().disableProperty().bindBidirectional(this.getDecreaseAtomsMenuItem().disableProperty());
        this.getIncreaseAtomsBu().disableProperty().bindBidirectional(this.getIncreaseAtomsMenuItem().disableProperty());
    }

    public void initScrollPane() {
        VBox structuresVBox = new VBox();
        structureHBox = new HBox();
        structureHBox.setSpacing(7);

        structuresVBox.setPrefHeight(60);

        structuresVBox.getChildren().add(structureHBox);
        getScrollpane().setContent(structuresVBox);
    }

    public Stage getStage() {
        return (Stage) rootPane.getScene().getWindow();
    }
}

