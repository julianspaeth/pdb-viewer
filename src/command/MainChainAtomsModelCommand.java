package command;

import view.fxmlview.PDBViewerView;
import view.proteinview.ProteinView;

public class MainChainAtomsModelCommand extends ACommand {

    private ProteinView proteinView;
    private PDBViewerView pdbView;
    private String oldModel;

    public MainChainAtomsModelCommand(ProteinView proteinView, PDBViewerView pdbView) {
        this.proteinView = proteinView;
        this.pdbView = pdbView;
        setName("Main chain model");
    }

    @Override
    public void execute() throws Exception {
        oldModel = proteinView.getCurrentModel();
        proteinView.setModel("mainchainatom");
        setToolbar(pdbView, proteinView, proteinView.getCurrentModel());

    }

    @Override
    public void undo() throws Exception {
        setToolbar(pdbView, proteinView, oldModel);
    }

    @Override
    public void redo() throws Exception {
        execute();
    }

}
