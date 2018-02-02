package view.proteinview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import model.proteinmodel.AminoAcid;
import model.proteinmodel.Atom;

import java.util.ArrayList;
import java.util.HashMap;

public class AminoAcidView extends Group {

    private ObservableList<AtomView> mainChainAtomsView = FXCollections.observableList(new ArrayList<>()), sideChainAtomsView = FXCollections.observableList(new ArrayList<>()), atomsView = FXCollections.observableList(new ArrayList<>());
    private Group mainChainAtoms = new Group(), sideChainAtoms = new Group();
    private HashMap<String, AtomView> atomsViewMap;
    private AminoAcid aa;

    public AminoAcidView(AminoAcid aa) {
        this.aa = aa;
        this.atomsViewMap = new HashMap<>();
        for (Atom atom : aa.getMainChainAtoms()) {
            AtomView atomView = new AtomView(atom);
            atomView.addTooltip(aa.getId(), atom.getAtom());
            atomView.setType("main");
            atomsViewMap.put(atom.getAtom(), atomView);
            mainChainAtomsView.add(atomView);
            mainChainAtoms.getChildren().add(atomView);
        }
        for (Atom atom : aa.getSideChainAtoms()) {
            AtomView atomView = new AtomView(atom);
            atomView.addTooltip(aa.getId(), atom.getAtom());
            atomView.setType("side");
            atomsViewMap.put(atom.getAtom(), atomView);
            sideChainAtomsView.add(atomView);
            sideChainAtoms.getChildren().add(atomView);
        }
        atomsView.addAll(mainChainAtomsView);
        atomsView.addAll(sideChainAtomsView);
        this.getChildren().add(mainChainAtoms);
        this.getChildren().add(sideChainAtoms);
        mainChainAtoms.visibleProperty().setValue(true);
    }

    public AminoAcid getAa() {
        return aa;
    }


    public ObservableList<AtomView> getAtomsView() {
        return atomsView;
    }

    public void setHighlighting(String highlighting) {
        for (AtomView atomView : atomsView) {
            atomView.setHighlighting(highlighting);
        }
    }

    public void setModel(String model) {
        for (AtomView atomView : atomsView) {
            atomView.setModel(model);
        }
    }
}