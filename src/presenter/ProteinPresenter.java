package presenter;

import command.*;
import connection.Request;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.proteinmodel.AminoAcid;
import model.proteinmodel.Atom;
import model.proteinmodel.Bond;
import model.proteinmodel.Protein;
import model.selectionmodel.ASelectionModel;
import view.fxmlview.ChartViewerView;
import view.fxmlview.PDBLoaderView;
import view.fxmlview.PDBViewerView;
import view.proteinview.*;
import view.selectionview.BoundingBoxes2D;
import view.selectionview.LabelSelection;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProteinPresenter {

    private static final Logger LOG = Logger.getLogger(ProteinPresenter.class.getName());

    private Protein protein;
    private PDBViewerView pdbViewerView;
    private ProteinView proteinView;
    private double mouseOldX, mouseOldY;
    private Point3D pivotPoint;
    private Property<Transform> transformProperty;
    private CommandManager commandManager = new CommandManager();
    private ACommand increaseAtomsCommand, decreaseAtomsCommand, increaseBondsCommand, decreaseBondsCommand, showAtomsCommand, showBondsCommand, showRibbonsCommand, colorStructuresCommand, colorAtomsCommand, spacingFillingModelCommand, mainChainAtomsModelCommand, backboneModelCommand, highlightAAsCommand, ribbonModelCommand, highlightChainsCommand, structureModelCommand;
    private PDBLoaderView pdbLoaderView;
    private ChartViewerView chartViewerView;
    private Stage pdbLoaderStage = new Stage(), chartViewerStage = new Stage();
    private Scene scene;
    private ASelectionModel aaSelectionModel;
    private LabelSelection labelSelection;
    private ArrayList<Integer> selectedOrphanLabels = new ArrayList<>();

    public ProteinPresenter(Protein protein, ProteinView proteinView, PDBViewerView fxmlConnector, Scene scene) {
        this.protein = protein;
        this.proteinView = proteinView;
        this.pdbViewerView = fxmlConnector;
        this.scene = scene;
        this.pdbViewerView.getBottomPane().getChildren().add(proteinView.getSubScene());
        this.initChartViewer();
        this.initCommands();
        this.pdbViewerView.initScrollPane();
        this.setMenuActions();
        this.setAminoAcidChangeListener();
        this.setBondChangeListener();
        this.setUndoRedoListeners();
        this.setBindings();
        this.setWindowChangeListener();
        this.initAminoAcidSelectionModel();
        this.resetTransformProperty();
    }

    private void setMenuActions() {
        this.setFileMenuActions();
        this.setEditMenuActions();
        this.setViewMenuActions();
        this.setChartMenuActions();
        this.setToolbarActions();
    }

    private void setFileMenuActions() {
        pdbViewerView.getLoadMenuItem().setOnAction(e -> {
            LOG.info("Pressed: Load file from directory");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open PDB");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDB (*.pdb)", "*.pdb"));
            File selectedFile = fileChooser.showOpenDialog(pdbViewerView.getStage());
            if (selectedFile != null) {
                this.proteinView.resetProteinView();
                try {
                    this.protein.read(new BufferedReader(new FileReader(selectedFile)));
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                initProtein();
            }
        });

        pdbViewerView.getLoadFromPDBMenuItem().setOnAction(e -> {
            LOG.info("Pressed: Load from PDB");
            pdbLoaderStage.show();
        });

        pdbViewerView.getSaveMenuItem().setOnAction(e -> {
            LOG.info("Pressed: Save as PNG");
            WritableImage image = proteinView.getSubScene().snapshot(new SnapshotParameters(), null);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Protein View");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG (*.png)", "*.png"));
            File selectedFile = fileChooser.showSaveDialog(pdbViewerView.getStage());

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", selectedFile);
            } catch (IOException | IllegalArgumentException ex) {
                ex.printStackTrace();
                LOG.info("Error while saving the png.");
            }

        });

        pdbViewerView.getQuitMenuItem().setOnAction(e -> {
            Platform.exit();
            LOG.info("Good Bye!");
        });
    }

    private void setViewMenuActions() {
        this.pdbViewerView.getHighlightAminoacidsMenuItem().setOnAction(e -> {
            try {
                LOG.info("Highlight Aminoacids");
                this.commandManager.executeAndAdd(highlightAAsCommand);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getBackboneModelMenuItem().setOnAction(e -> {
            try {
                LOG.info("Backbone model");
                this.commandManager.executeAndAdd(backboneModelCommand);
                Rotate emptyRotate = new Rotate();
                this.transformProperty.setValue(emptyRotate.createConcatenation(transformProperty.getValue()));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getStructureModelMenuItem().setOnAction(e -> {
            try {
                LOG.info("Structure model");
                this.commandManager.executeAndAdd(structureModelCommand);
                Rotate emptyRotate = new Rotate();
                this.transformProperty.setValue(emptyRotate.createConcatenation(transformProperty.getValue()));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getMainChainAtomsModelMenuItem().setOnAction(e -> {
            try {
                LOG.info("Main chain model");
                this.commandManager.executeAndAdd(mainChainAtomsModelCommand);
                Rotate emptyRotate = new Rotate();
                this.transformProperty.setValue(emptyRotate.createConcatenation(transformProperty.getValue()));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getHighlightChainsMenuItem().setOnAction(e -> {
            try {
                LOG.info("Highlight Chains");
                this.commandManager.executeAndAdd(highlightChainsCommand);
                Rotate emptyRotate = new Rotate();
                this.transformProperty.setValue(emptyRotate.createConcatenation(transformProperty.getValue()));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getSpacingFillingModelMenuItem().setOnAction(e -> {
            try {
                LOG.info("Spacing filling model");
                this.commandManager.executeAndAdd(spacingFillingModelCommand);
                Rotate emptyRotate = new Rotate();
                this.transformProperty.setValue(emptyRotate.createConcatenation(transformProperty.getValue()));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getRibbonModelMenuItem().setOnAction(e -> {
            LOG.info("Ribbon model");
            try {
                this.commandManager.executeAndAdd(ribbonModelCommand);
                Rotate emptyRotate = new Rotate();
                this.transformProperty.setValue(emptyRotate.createConcatenation(transformProperty.getValue()));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getHighlightAtomsMenuItem().setOnAction(e -> {
            try {
                LOG.info("Highlight atoms");
                this.commandManager.executeAndAdd(colorAtomsCommand);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getHighlightStructureMenuItem().setOnAction(e -> {
            try {
                LOG.info("Highlight structure");
                this.commandManager.executeAndAdd(colorStructuresCommand);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getShowAtomsMenuItem().setOnAction(e -> {
            try {
                LOG.info("Show atoms");
                this.commandManager.executeAndAdd(showAtomsCommand);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getShowBondsMenuItem().setOnAction(e -> {
            try {
                LOG.info("Show bonds");
                this.commandManager.executeAndAdd(showBondsCommand);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getShowRibbonsMenuItem().setOnAction(e -> {
            try {
                LOG.info("Show Ribbons");
                commandManager.executeAndAdd(showRibbonsCommand);
                Rotate emptyRotate = new Rotate();
                this.transformProperty.setValue(emptyRotate.createConcatenation(transformProperty.getValue()));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getIncreaseAtomsMenuItem().setOnAction(e -> {
            try {
                LOG.info("Increase atoms");
                this.commandManager.executeAndAdd(increaseAtomsCommand);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getDecreaseAtomsMenuItem().setOnAction(e -> {
            try {
                LOG.info("Decrease atoms");
                this.commandManager.executeAndAdd(decreaseAtomsCommand);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getIncreaseBondsMenuItem().setOnAction(e -> {
            try {
                LOG.info("Increase bonds");
                this.commandManager.executeAndAdd(increaseBondsCommand);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getDecreaseBondsMenuItem().setOnAction(e -> {
            try {
                LOG.info("Decrease bonds");
                this.commandManager.executeAndAdd(decreaseBondsCommand);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    private void setEditMenuActions() {
        this.pdbViewerView.getUndoMenuItem().setOnAction(e -> {
            try {
                LOG.info("Undo");
                this.commandManager.undo();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getRedoMenuItem().setOnAction(e -> {
            try {
                LOG.info("Redo");
                this.commandManager.redo();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    private void setChartMenuActions() {
        pdbViewerView.getAaChartMenuItem().setOnAction(event -> {
            LOG.info("Pressed: Aminoacids");
            chartViewerStage.show();
        });
    }

    private void initCommands() {
        showAtomsCommand = new ShowAtomsCommand(proteinView, pdbViewerView);
        showBondsCommand = new ShowBondsCommand(proteinView, pdbViewerView);
        showRibbonsCommand = new ShowRibbonsCommand(proteinView, pdbViewerView);
        increaseAtomsCommand = new IncreaseAtomsCommand(proteinView);
        decreaseAtomsCommand = new DecreaseAtomsCommand(proteinView);
        increaseBondsCommand = new IncreaseBondsCommand(proteinView);
        decreaseBondsCommand = new DecreaseBondsCommand(proteinView);
        colorStructuresCommand = new HighlightStructuresCommand(proteinView);
        colorAtomsCommand = new HighlightAtomsCommand(proteinView);
        spacingFillingModelCommand = new SpacingFillingModelCommand(proteinView, pdbViewerView);
        mainChainAtomsModelCommand = new MainChainAtomsModelCommand(proteinView, pdbViewerView);
        backboneModelCommand = new BackboneModelCommand(proteinView, pdbViewerView);
        highlightAAsCommand = new HighlightAAsCommand(proteinView);
        ribbonModelCommand = new RibbonModelCommand(proteinView, pdbViewerView);
        highlightChainsCommand = new HighlightChainsCommand(proteinView);
        structureModelCommand = new StructureModelCommand(proteinView, pdbViewerView);
    }

    private void resetTransformProperty() {
        this.transformProperty = new SimpleObjectProperty<>(new Rotate());
        this.transformProperty.addListener((c, o, n) -> proteinView.getTransforms().setAll(n));
    }


    public void initPDBLoader(ObservableList<String> pdbIdList) {
        Parent pdbLoader;
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            pdbLoader = fxmlLoader.load(getClass().getResource("../view/fxml/pdbLoader.fxml").openStream());
            pdbLoaderView = fxmlLoader.getController();
            pdbLoaderStage.setTitle("Load PDB");
            pdbLoaderStage.setScene(new Scene(pdbLoader));
            pdbLoaderStage.setAlwaysOnTop(true);
            pdbLoaderView.getPdbIdListView().setEditable(false);
            FilteredList<String> filteredList = new FilteredList<>(pdbIdList, data -> true);
            pdbLoaderView.getPdbIdListView().setItems(filteredList);
            pdbLoaderView.getFilterTF().textProperty().addListener((javafx.beans.Observable obs) -> {
                String filter = pdbLoaderView.getFilterTF().getText();
                if (filter == null || filter.length() == 0) {
                    filteredList.setPredicate(s -> true);
                } else {
                    filteredList.setPredicate(s -> (s.toUpperCase()).contains(filter.toUpperCase()));
                }
            });

            pdbLoaderView.getCancelBu().setOnAction(e -> pdbLoaderStage.close());
            pdbLoaderView.getLoadBu().disableProperty().bind(pdbLoaderView.getPdbIdListView().getSelectionModel().selectedItemProperty().isNull());
            pdbLoaderView.getLoadBu().setOnAction(e -> {
                String id = (String) pdbLoaderView.getPdbIdListView().getSelectionModel().getSelectedItem();
                LOG.info("PDB ID: " + id);
                try {
                    if (id != null) {
                        ObservableList<String> file = Request.getFromURL("https://files.rcsb.org/download/" + id + ".pdb");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        for (String pdbid : file) {
                            String pdbid_n = pdbid + "\n";
                            baos.write(pdbid_n.getBytes());
                        }
                        byte[] bytes = baos.toByteArray();
                        InputStream in = new ByteArrayInputStream(bytes);
                        this.proteinView.resetProteinView();
                        protein.read(new BufferedReader(new InputStreamReader(in)));
                        initProtein();
                        pdbLoaderStage.close();
                    }
                } catch (IOException ex) {
                    pdbViewerView.getLoadMenuItem().setDisable(true);
                    pdbViewerView.getStatusLabel().setText(" Connection to PDB failed. Please check your internet connection and retry!");
                    pdbLoaderStage.close();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Could not load the pdb file. Please check your internet connection and click 'Retry' in the right down corner");
                    alert.show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initChartViewer() {
        Parent chartViewer = null;
        FXMLLoader fxmlLoader = new FXMLLoader();

        try {
            chartViewer = fxmlLoader.load(getClass().getResource("../view/fxml/chartViewer.fxml").openStream());
            chartViewerView = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        chartViewerStage.setTitle("Aminoacids Overview");
        Scene chartScene = new Scene(chartViewer);
        chartViewerStage.setScene(chartScene);
        chartViewerStage.setAlwaysOnTop(true);

        chartViewerView.getCancelBu().setOnAction(event -> {
            chartViewerStage.close();
        });

        chartViewerView.getSaveBu().setOnAction(event -> {
            LOG.info("Save chart");
            WritableImage image = chartViewerView.getAaChart().snapshot(new SnapshotParameters(), null);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save chart");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG (*.png)", "*.png"));
            File selectedFile = fileChooser.showSaveDialog(pdbViewerView.getStage());

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", selectedFile);
            } catch (IOException | IllegalArgumentException ex) {
                // TODO: handle exception here
            }
        });
    }

    private void setBondChangeListener() {
        this.protein.getBonds().addListener((ListChangeListener<Bond>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Object obj : c.getAddedSubList()) {
                        Bond bond = (Bond) obj;
                        if (bond.getSource() != null && bond.getTarget() != null) {
                            BondView bondView = new BondView(bond, proteinView.getAtomViewItem(bond.getSource()), proteinView.getAtomViewItem(bond.getTarget()));
                            this.proteinView.getBondViewGroup().getChildren().add(bondView);
                            if (bondView.getBond().getSource().getAa().getId() != bondView.getBond().getTarget().getAa().getId()) {
                                RibbonView ribbonView = new RibbonView(proteinView.getAminoAcidViewItem(bond.getSource().getAa()), proteinView.getAminoAcidViewItem(bond.getTarget().getAa()), bond.getTarget().getAa().getStructure());
                                proteinView.getRibbonViewGroup().getChildren().add(ribbonView);
                            }
                        }
                    }
                }
                if (c.wasRemoved()) {
                    for (Object obj : c.getRemoved()) {
                        Bond bond = (Bond) obj;
                        this.proteinView.getBondViewGroup().getChildren().remove(proteinView.getBondViewItem(bond));
                    }
                }
            }
        });
    }

    private void setAminoAcidChangeListener() {
        this.protein.getAminoAcids().addListener((ListChangeListener<AminoAcid>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Object obj : c.getAddedSubList()) {
                        AminoAcid aa = (AminoAcid) obj;
                        AminoAcidView aaView = new AminoAcidView(aa);
                        for (AtomView atomView : aaView.getAtomsView()) {
                            this.setAtomClickEvents(atomView);
                        }
                        this.proteinView.getAminoAcidViewGroup().getChildren().add(aaView);
                    }
                }
                if (c.wasRemoved()) {
                    for (Object obj : c.getRemoved()) {
                        AminoAcid aa = (AminoAcid) obj;
                        this.proteinView.getAminoAcidViewGroup().getChildren().remove(proteinView.getAminoAcidViewItem(aa));
                    }
                }
            }
            if (!this.protein.getAminoAcids().isEmpty()) {
                this.setPaneEvents();
                pdbViewerView.initMenuAndToolbar();
                TranslateTransition translateTransition = this.proteinView.centerProtein();
                translateTransition.setOnFinished(e -> {
                    Rotate emptyRotate = new Rotate();
                    this.transformProperty.setValue(emptyRotate.createConcatenation(transformProperty.getValue()));
                });
            }
            updateAaChart();
        });
    }

    private void setAtomClickEvents(AtomView atomView) {
        atomView.setOnMouseClicked(e -> {
            LOG.info("ATOM CLICK");
            if (!e.isShiftDown()) {
                LOG.info("Clear selection");
                this.aaSelectionModel.clearSelection();
            } else if (e.isShiftDown()) {
                LOG.info("Select AA");
                AminoAcid correspondingAA = null;
                for (AminoAcid aa : protein.getAminoAcids()) {
                    if (aa.getId() == atomView.getAtom().getAaId()) {
                        correspondingAA = aa;
                    }
                }

                for (Atom atom : correspondingAA.getMainChainAtoms()) {
                    this.aaSelectionModel.select(atom);
                }
                for (Atom atom : correspondingAA.getSideChainAtoms()) {
                    this.aaSelectionModel.select(atom);
                }
            }
            e.consume();
        });
    }

    private void initAminoAcidSelectionModel() {
        this.aaSelectionModel = new ASelectionModel(this.protein.getAtoms());
        this.aaSelectionModel.setItems(this.protein.getAtoms().toArray(new Atom[this.protein.getAtoms().size()]));
        List<Property> properties = setBoundingBoxProperties();
        BoundingBoxes2D boundingBoxes = new BoundingBoxes2D(pdbViewerView.getBottomPane(), this.proteinView, this.aaSelectionModel, properties.toArray(new Property[properties.size()]));
        pdbViewerView.getSelectionPane().getChildren().clear();
        pdbViewerView.getSelectionPane().getChildren().add(boundingBoxes.getRectangles());
        labelSelection = new LabelSelection(pdbViewerView.getStructureHbox(), this.aaSelectionModel);
    }

    private void setPaneEvents() {
        this.pdbViewerView.getProteinGraphStackPane().setOnMousePressed(e -> {
            this.mouseOldX = e.getSceneX();
            this.mouseOldY = e.getSceneY();
            this.pivotPoint = proteinView.getCenter();
        });

        this.pdbViewerView.getProteinGraphStackPane().setOnMouseDragged(e -> {
            double dx = (e.getSceneX() - mouseOldX);
            double dy = (e.getSceneY() - mouseOldY);
            Point3D axis = new Point3D(dy, -dx, 0);
            double angle = 0.25 * (new Point2D(dx, dy)).magnitude(); // .25 speed
            Rotate rotate = new Rotate(angle, axis);
            rotate.setPivotX(pivotPoint.getX());
            rotate.setPivotY(pivotPoint.getY());
            rotate.setPivotZ(pivotPoint.getZ());

            this.transformProperty.setValue(rotate.createConcatenation(transformProperty.getValue()));

            this.mouseOldX = e.getSceneX();
            this.mouseOldY = e.getSceneY();
        });

        this.pdbViewerView.getProteinGraphStackPane().setOnScroll(e -> {
            proteinView.getSubScene().getCamera().setTranslateZ(proteinView.getSubScene().getCamera().getTranslateZ() + e.getDeltaY() * 2);
            Rotate rotate = new Rotate();
            this.transformProperty.setValue(rotate.createConcatenation(transformProperty.getValue()));

        });

    }

    private void setToolbarActions() {
        this.pdbViewerView.getShowAtomsCB().setOnAction(e -> {
            try {
                commandManager.executeAndAdd(showAtomsCommand);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getShowBondsCB().setOnAction(e -> {
            try {
                commandManager.executeAndAdd(showBondsCommand);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getShowRibbonsCB().setOnAction(e -> {
            try {
                commandManager.executeAndAdd(showRibbonsCommand);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getIncreaseAtomsBu().setOnAction(e -> {
            try {
                LOG.info("Increase atoms");
                commandManager.executeAndAdd(increaseAtomsCommand);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getDecreaseAtomsBu().setOnAction(e -> {
            try {
                LOG.info("Decrease atoms");
                commandManager.executeAndAdd(decreaseAtomsCommand);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getIncreaseBondsBu().setOnAction(e -> {
            try {
                LOG.info("Increase bonds");
                commandManager.executeAndAdd(increaseBondsCommand);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getDecreaseBondsBu().setOnAction(e -> {
            try {
                LOG.info("Decrease bonds");
                this.commandManager.executeAndAdd(decreaseBondsCommand);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getRedoBu().setOnAction(e -> {
            try {
                LOG.info("Redo");
                this.commandManager.redo();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        this.pdbViewerView.getUndoBu().setOnAction(e -> {
            try {
                LOG.info("Undo");
                this.commandManager.undo();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }



    private void setBindings() {
        this.setSequenceStructureBindings();
        pdbViewerView.setToolbarMenuBindings();
        this.setProteinGraphSubSceneBindings();
        this.setWindowBindings();
    }

    private void setSequenceStructureBindings() {
        this.pdbViewerView.getTitleLabel().textProperty().bind(this.protein.titleProperty());
    }

    private void setProteinGraphSubSceneBindings() {
        this.proteinView.getSubScene().widthProperty().bind(pdbViewerView.getProteinGraphStackPane().widthProperty());
        this.proteinView.getSubScene().heightProperty().bind(pdbViewerView.getProteinGraphStackPane().heightProperty());
    }

    private void setWindowBindings() {
        pdbViewerView.getTitleLabel().prefWidthProperty().bind(pdbViewerView.getRootPane().widthProperty());
        pdbViewerView.getProteinGraphStackPane().prefWidthProperty().bind(pdbViewerView.getRootPane().widthProperty());
        pdbViewerView.getProteinGraphStackPane().prefHeightProperty().bind(pdbViewerView.getRootPane().heightProperty());
        pdbViewerView.getViewBorderPane().prefWidthProperty().bind(pdbViewerView.getRootPane().widthProperty());
        pdbViewerView.getViewBorderPane().prefHeightProperty().bind(pdbViewerView.getRootPane().heightProperty());
        pdbViewerView.getRootPane().prefWidthProperty().bind(scene.widthProperty());
        pdbViewerView.getRootPane().prefHeightProperty().bind(scene.heightProperty());
    }

    private void setWindowChangeListener() {
        scene.widthProperty().addListener((obserable) -> {
            if (!this.protein.getAminoAcids().isEmpty()) {
                TranslateTransition translateTransition = this.proteinView.centerProtein();
                translateTransition.setOnFinished(e -> {
                    Rotate emptyRotate = new Rotate();
                    this.transformProperty.setValue(emptyRotate.createConcatenation(transformProperty.getValue()));
                });
            }
        });
        scene.heightProperty().addListener((observable) -> {
            if (!this.protein.getAminoAcids().isEmpty()) {
                TranslateTransition translateTransition = this.proteinView.centerProtein();
                translateTransition.setOnFinished(e -> {
                    Rotate emptyRotate = new Rotate();
                    this.transformProperty.setValue(emptyRotate.createConcatenation(transformProperty.getValue()));
                });
            }
        });

    }

    private void setUndoRedoListeners() {
        this.commandManager.getRedoStack().addListener((ListChangeListener<ACommand>) c -> {
            if (commandManager.getRedoStack().isEmpty()) {
                this.pdbViewerView.getRedoBu().setDisable(true);
                this.pdbViewerView.getRedoMenuItem().setDisable(true);
            } else {
                this.pdbViewerView.getRedoBu().setDisable(false);
                this.pdbViewerView.getRedoMenuItem().setDisable(false);
            }
        });

        this.commandManager.getUndoStack().addListener((ListChangeListener<ACommand>) c -> {
            if (commandManager.getUndoStack().isEmpty()) {
                this.pdbViewerView.getUndoBu().setDisable(true);
                this.pdbViewerView.getUndoMenuItem().setDisable(true);
            } else {
                this.pdbViewerView.getUndoBu().setDisable(false);
                this.pdbViewerView.getUndoMenuItem().setDisable(false);
            }
        });
    }


    private List<Property> setBoundingBoxProperties() {
        List<Property> properties = new ArrayList<>();
        properties.add(transformProperty);
        properties.add(proteinView.getSubScene().getCamera().translateXProperty());
        properties.add(proteinView.getSubScene().getCamera().translateYProperty());
        properties.add(proteinView.getSubScene().getCamera().scaleXProperty());
        properties.add(proteinView.getSubScene().getCamera().scaleYProperty());
        properties.add(proteinView.getSubScene().getCamera().scaleZProperty());
        properties.add(proteinView.getSubScene().heightProperty());
        properties.add(proteinView.getSubScene().widthProperty());

        return properties;
    }

    private void fillStructureLabels() {
        pdbViewerView.getStructureHbox().getChildren().clear();
        int index = 0;
        char[] primaryStructure = protein.sequenceProperty().getValue().toCharArray();
        char[] secondaryStructure = protein.structureProperty().getValue().toCharArray();
        for (int i = 0; i < primaryStructure.length; i++) {
            Label label = new Label((Character.toString(primaryStructure[index]) + "\n" + Character.toString(secondaryStructure[index])));
            label.setPrefWidth(10);
            label.setLineSpacing(5);
            label.setId(Integer.toString(index));
            setLabelSelectionClickEvents(label);
            this.pdbViewerView.getStructureHbox().getChildren().add(label);
            index = index + 1;
        }
    }

    private void setLabelSelectionClickEvents(Label label) {
        Integer id = Integer.parseInt(label.getId());
        label.setOnMouseClicked(e -> {
            if (!e.isShiftDown()) {
                LOG.info("Clear selection");
                this.aaSelectionModel.clearSelection();
                for (Integer orphan : selectedOrphanLabels) {
                    labelSelection.removeOrphanLabelSelection(orphan);
                }
                selectedOrphanLabels.clear();
            } else if (e.isShiftDown()) {
                AminoAcid correspondingAA = null;
                for (AminoAcid aa : protein.getAminoAcids()) {
                    if (aa.getId() - 1 == id) {
                        correspondingAA = aa;
                        LOG.info("Select AA:" + aa + aa.getId());
                    }
                }

                try {
                    for (Atom atom : correspondingAA.getMainChainAtoms()) {
                        this.aaSelectionModel.select(atom);
                    }
                } catch (NullPointerException ex) {
                    labelSelection.setAtomNotShown(id);
                    selectedOrphanLabels.add(id);
                }

            }
            e.consume();
        });
    }

    private void updateAaChart() {
        chartViewerView.getAaChart().getData().clear();
        ObservableList<PieChart.Data> aasChartData = FXCollections.observableArrayList();
        for (Character aa : this.protein.getNumberOfAminoAcids().keySet()) {
            if (aa != null) {
                aasChartData.add(new PieChart.Data(aa.toString(), this.protein.getNumberOfAminoAcids().get(aa)));
            }
        }
        chartViewerView.getAaChart().setData(aasChartData);
    }

    private void initProtein() {
        this.resetTransformProperty();
        try {
            commandManager.executeAndAdd(mainChainAtomsModelCommand);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        this.commandManager.clear();
        initAminoAcidSelectionModel();
        pdbViewerView.setStatusLabels(protein.getId(), protein.getAminoAcids().size(), protein.getAtoms().size(), protein.getBonds().size());
        fillStructureLabels();
    }
}

