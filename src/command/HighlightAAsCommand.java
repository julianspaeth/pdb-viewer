package command;

import view.proteinview.ProteinView;

public class HighlightAAsCommand extends ACommand {

    private ProteinView proteinView;
    private String oldHighlighting;


    public HighlightAAsCommand(ProteinView proteinView) {
        this.proteinView = proteinView;
        setName("Color Amino Acids");
    }

    @Override
    public void execute() throws Exception {
        oldHighlighting = proteinView.getCurrentHighlighting();
        proteinView.setHighlighting("aminoacids");
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
