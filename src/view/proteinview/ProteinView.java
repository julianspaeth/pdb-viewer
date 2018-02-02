package view.proteinview;

import javafx.animation.TranslateTransition;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.util.Duration;
import model.proteinmodel.AminoAcid;
import model.proteinmodel.Atom;
import model.proteinmodel.Bond;

import java.util.ArrayList;

public class ProteinView extends Group {

    private Group aminoAcidViewGroup, bondViewGroup, ribbonViewGroup;
    private ArrayList<Double> minMaxXYZ = new ArrayList<>();
    private SubScene subScene;
    private Camera camera;
    private double FARCLIP = 15000, NEARCLIP = 0.01, TRANSLATEZ = -2000;
    private String currentHighlighting = "atoms", currentModel = "mainchainatom";

    public ProteinView() {
        this.aminoAcidViewGroup = new Group();
        this.bondViewGroup = new Group();
        this.ribbonViewGroup = new Group();

        this.getChildren().add(aminoAcidViewGroup);
        this.getChildren().add(bondViewGroup);
        this.getChildren().add(ribbonViewGroup);

        this.setSubScene();
    }

    public AminoAcidView getAminoAcidViewItem(AminoAcid aa) {
        AminoAcidView correspondingAA = null;
        for (Node node : aminoAcidViewGroup.getChildren()) {
            AminoAcidView aaView = (AminoAcidView) node;
            if (aaView.getAa() == aa) {
                correspondingAA = aaView;
            }
        }
        return correspondingAA;
    }

    public AtomView getAtomViewItem(Atom atom) {
        AtomView correspondingAtomView = null;
        for (Node node : aminoAcidViewGroup.getChildren()) {
            AminoAcidView aaView = (AminoAcidView) node;
            for (AtomView atomView : aaView.getAtomsView()) {
                if (atomView.getAtom() == atom) {
                    correspondingAtomView = atomView;
                }
            }
        }
        return correspondingAtomView;
    }

    public BondView getBondViewItem(Bond bond) {
        BondView correspondingBondView = null;
        for (Node node : bondViewGroup.getChildren()) {
            BondView bondView = (BondView) node;
            if (bondView.getBond() == bond) {
                correspondingBondView = bondView;
            }
        }
        return correspondingBondView;
    }

    public void resetProteinView() {
        this.bondViewGroup.getChildren().clear();
        this.aminoAcidViewGroup.getChildren().clear();
        this.ribbonViewGroup.getChildren().clear();
        setModel("mainchainatom");
        setHighlighting("atoms");
    }

    public Group getAminoAcidViewGroup() {
        return aminoAcidViewGroup;
    }

    public Group getBondViewGroup() {
        return bondViewGroup;
    }

    private void computeMinMaxCoordinates() {
        this.minMaxXYZ.clear();
        ArrayList<Double> xValues = new ArrayList<>();
        ArrayList<Double> yValues = new ArrayList<>();
        ArrayList<Double> zValues = new ArrayList<>();
        for (Node aa : aminoAcidViewGroup.getChildren()) {
            AminoAcidView aaView = (AminoAcidView) aa;
            for (Node groupNode : aaView.getChildren()) {
                Group group = (Group) groupNode;
                for (Node node : group.getChildren()) {
                    AtomView atomView = (AtomView) node;
                    xValues.add(atomView.getTranslateX());
                    yValues.add(atomView.getTranslateY());
                    zValues.add(atomView.getTranslateZ());
                }
            }
        }
        xValues.sort(Double::compareTo);
        yValues.sort(Double::compareTo);
        zValues.sort(Double::compareTo);

        this.minMaxXYZ.add(xValues.get(0));
        this.minMaxXYZ.add(xValues.get(xValues.size() - 1));
        this.minMaxXYZ.add(yValues.get(0));
        this.minMaxXYZ.add(yValues.get(yValues.size() - 1));
        this.minMaxXYZ.add(zValues.get(0));
        this.minMaxXYZ.add(zValues.get(zValues.size() - 1));
    }

    //TODO fix center without protein

    public Point3D getCenter() {
        this.computeMinMaxCoordinates();
        double centerX = ((minMaxXYZ.get(0) + minMaxXYZ.get(1)) / 2);
        double centerY = ((minMaxXYZ.get(2) + minMaxXYZ.get(3)) / 2);
        double centerZ = ((minMaxXYZ.get(4) + minMaxXYZ.get(5)) / 2);

        return new Point3D(centerX, centerY, centerZ);
    }

    public TranslateTransition centerProtein() {
        double xDiff = this.camera.getTranslateX() - this.getCenter().getX();
        double yDiff = this.camera.getTranslateY() - this.getCenter().getY();
        double zDiff = 0 - this.getCenter().getZ();

        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setNode(this);
        translateTransition.setToX(xDiff);
        translateTransition.setToY(yDiff);
        translateTransition.setToZ(zDiff);

        translateTransition.setDuration(Duration.millis(300));
        translateTransition.play();

        return translateTransition;
    }

    private void setSubScene() {
        this.camera = new PerspectiveCamera(true);
        this.camera.setFarClip(FARCLIP);
        this.camera.setNearClip(NEARCLIP);
        this.camera.setTranslateZ(TRANSLATEZ);
        this.subScene = new SubScene(this, 0, 0, true, SceneAntialiasing.BALANCED);
        this.subScene.setCamera(camera);
        setCameraBindings();
    }

    private void setCameraBindings() {
        this.getSubScene().getCamera().translateXProperty().bind(this.getSubScene().widthProperty().divide(2));
        this.getSubScene().getCamera().translateYProperty().bind(this.getSubScene().heightProperty().divide(2));
    }

    public SubScene getSubScene() {
        return this.subScene;
    }

    public void setHighlighting(String highlighting) {
        currentHighlighting = highlighting;
        for (Node node : this.getAminoAcidViewGroup().getChildren()) {
            AminoAcidView aaView = (AminoAcidView) node;
            aaView.setHighlighting(highlighting);
        }

        for (Node node : this.getBondViewGroup().getChildren()) {
            BondView bondView = (BondView) node;
            bondView.setHighlighting(highlighting);
        }

        for (Node node : this.getRibbonViewGroup().getChildren()) {
            RibbonView ribbonView = (RibbonView) node;
            ribbonView.setHighlighting(highlighting);
        }
    }


    public void setModel(String model) {
        currentModel = model;
        for (Node node : this.getAminoAcidViewGroup().getChildren()) {
            AminoAcidView aaView = (AminoAcidView) node;
            aaView.setModel(model);
        }

        for (Node node : this.getBondViewGroup().getChildren()) {
            BondView bondView = (BondView) node;
            bondView.setModel(model);
        }

        for (Node node : this.getRibbonViewGroup().getChildren()) {
            RibbonView ribbonView = (RibbonView) node;
            ribbonView.setModel(model);
        }

        switch (model) {
            case "spacingfilling":
                bondViewGroup.visibleProperty().setValue(false);
                aminoAcidViewGroup.visibleProperty().setValue(true);
                ribbonViewGroup.visibleProperty().setValue(false);
                break;
            case "mainchainatom":
                bondViewGroup.visibleProperty().setValue(true);
                aminoAcidViewGroup.visibleProperty().setValue(true);
                ribbonViewGroup.visibleProperty().setValue(false);
                break;
            case "backbone":
                bondViewGroup.visibleProperty().setValue(true);
                aminoAcidViewGroup.visibleProperty().setValue(true);
                ribbonViewGroup.visibleProperty().setValue(false);
                break;
            case "ribbon":
                bondViewGroup.visibleProperty().setValue(true);
                aminoAcidViewGroup.visibleProperty().setValue(true);
                ribbonViewGroup.visibleProperty().setValue(true);
                break;
            case "structure":
                bondViewGroup.visibleProperty().setValue(true);
                aminoAcidViewGroup.visibleProperty().setValue(true);
                ribbonViewGroup.visibleProperty().setValue(true);
                break;
        }
    }


    public String getCurrentModel() {
        return currentModel;
    }

    public String getCurrentHighlighting() {
        return currentHighlighting;
    }

    public Group getRibbonViewGroup() {
        return ribbonViewGroup;
    }
}
