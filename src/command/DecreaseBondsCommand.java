package command;

import javafx.scene.Node;
import view.proteinview.BondView;
import view.proteinview.ProteinView;

public class DecreaseBondsCommand extends ACommand {

    private ProteinView proteinView;

    public DecreaseBondsCommand(ProteinView proteinView) {
        this.proteinView = proteinView;
        setName("Decrease Atoms");
    }

    @Override
    public void execute() throws Exception {
        for (Node node : proteinView.getBondViewGroup().getChildren()) {
            BondView bondView = (BondView) node;
            bondView.changeBondSize("decrease");
        }
    }

    @Override
    public void undo() throws Exception {
        for (Node node : proteinView.getBondViewGroup().getChildren()) {
            BondView bondView = (BondView) node;
            bondView.changeBondSize("increase");
        }
    }

    @Override
    public void redo() throws Exception {
        execute();
    }
}
