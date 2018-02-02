package view.selectionview;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import model.proteinmodel.Atom;
import model.selectionmodel.ASelectionModel;

import java.util.logging.Logger;

public class LabelSelection {

    private HBox structureHBox;

    /**
     * constructor
     */
    public LabelSelection(HBox structureHBox, ASelectionModel<Atom> nodeSelectionModel) {
        this.structureHBox = structureHBox;
        nodeSelectionModel.getSelectedItems().addListener((ListChangeListener<Atom>) c -> {
            while (c.next()) {
                for (Atom atom : c.getRemoved()) {
                    removeLabelSelection(atom.getAaId());
                }
                for (Atom atom : c.getAddedSubList()) {
                    setLabelSelected(atom.getAaId());
                }
            }
        });
    }

    private void removeLabelSelection(int index) {
        Label primaryLa = (Label) structureHBox.getChildren().get(index - 1);
        primaryLa.setStyle("-fx-background-color: white; -fx-font-family: Monospaced");
    }

    private void setLabelSelected(int index) {
        Label primaryLa = (Label) structureHBox.getChildren().get(index - 1);
        primaryLa.setStyle("-fx-background-color: #daa520; -fx-font-family: Monospaced");
    }

    public void setAtomNotShown(int index) {
        Label primaryLa = (Label) structureHBox.getChildren().get(index);
        primaryLa.setStyle("-fx-background-color: #FF4136; -fx-font-family: Monospaced");
    }

    public void removeOrphanLabelSelection(int index) {
        removeLabelSelection(index + 1);
    }

}
