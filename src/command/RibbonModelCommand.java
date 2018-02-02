package command;

import view.fxmlview.PDBViewerView;
import view.proteinview.ProteinView;

public class RibbonModelCommand extends ACommand {

    private ProteinView proteinView;
    private PDBViewerView pdbView;
    private String oldModel;

    public RibbonModelCommand(ProteinView proteinView, PDBViewerView pdbView) {
        this.proteinView = proteinView;
        this.pdbView = pdbView;
        setName("Ribbon model");
    }

    @Override
    public void execute() throws Exception {
        oldModel = proteinView.getCurrentModel();
        proteinView.setModel("ribbon");
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
