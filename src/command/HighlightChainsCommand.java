package command;

import view.proteinview.ProteinView;

public class HighlightChainsCommand extends ACommand {

    private ProteinView proteinView;
    private String oldHighlighting;

    public HighlightChainsCommand(ProteinView proteinView) {
        this.proteinView = proteinView;
        setName("Color Chains");
    }

    @Override
    public void execute() throws Exception {
        oldHighlighting = proteinView.getCurrentHighlighting();
        proteinView.setHighlighting("chains");
    }

    @Override
    public void undo() throws Exception {
        proteinView.setHighlighting(oldHighlighting);
    }

    @Override
    public void redo() throws Exception {
        execute();
    }

}
