package command;

import javafx.scene.Node;
import view.proteinview.AminoAcidView;
import view.proteinview.AtomView;
import view.proteinview.ProteinView;

public class IncreaseAtomsCommand extends ACommand {

    private ProteinView proteinView;

    public IncreaseAtomsCommand(ProteinView proteinView) {
        this.proteinView = proteinView;
        setName("Increase Atoms");
    }

    @Override
    public void execute() throws Exception {
        for (Node node : proteinView.getAminoAcidViewGroup().getChildren()) {
            AminoAcidView aaView = (AminoAcidView) node;
            for (AtomView atomView : aaView.getAtomsView()) {
                atomView.scaleAtom("increase");
            }
        }
    }

    @Override
    public void undo() throws Exception {
        for (Node node : proteinView.getAminoAcidViewGroup().getChildren()) {
            AminoAcidView aaView = (AminoAcidView) node;
            for (AtomView atomView : aaView.getAtomsView()) {
                atomView.scaleAtom("decrease");
            }
        }
    }

    @Override
    public void redo() throws Exception {
        execute();
    }


}
